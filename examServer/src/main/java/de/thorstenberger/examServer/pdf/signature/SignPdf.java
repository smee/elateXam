/*

Copyright (C) 2010 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.thorstenberger.examServer.pdf.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfLiteral;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSigGenericPKCS;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.TSAClientBouncyCastle;

/**
 * Sign pdfs using a cryptographic key. Also add a timestamp that proves reliably that the document was signed at this
 * specific point in time.
 *
 * @author Steffen Dienst
 *
 */
public class SignPdf {
  protected final static Log log = LogFactory.getLog(SignPdf.class);

  public static void main(final String[] args) throws IOException, SignatureException, KeyStoreException, DocumentException {
    final SignatureInfos infos = new SignatureInfos();
    infos.save(new File("signature.properties"));

    signAndTimestamp(new File("d:/temp/pki.pdf"),
        new File("d:/temp/pki_timestamp.pdf"));

  }


  /**
   * Add a signature and a cryptographic timestamp to a pdf document. See www.ietf.org/rfc/rfc3161.txt. Proves that this
   * pdf had the current content at the current point in time.
   *
   * @param originalPdf
   * @param targetPdf
   * @param infos
   * @throws SignatureException
   * @throws KeyStoreException
   * @throws IOException
   * @throws DocumentException
   */
  public static void signAndTimestamp(final InputStream originalPdf, final OutputStream targetPdf, final SignatureInfos infos) throws SignatureException, KeyStoreException, IOException, DocumentException {
    signAndTimestamp(originalPdf, targetPdf, infos.getPrivateKey(), infos.getCertificateChain(), null, infos.getTimeStampServerUrl(), infos.getSignatureReason(), infos.getSignatureLocation(), infos.getSignatureContact());
  }

  private static void signAndTimestamp(final File fIn, final File fOut) throws SignatureException, KeyStoreException, FileNotFoundException, IOException, DocumentException {
    signAndTimestamp(new FileInputStream(fIn), new FileOutputStream(fOut), new SignatureInfos());
  }

  /**
   * Add a signature and a cryptographic timestamp to a pdf document. See www.ietf.org/rfc/rfc3161.txt. Proves that this
   * pdf had the current content at the current point in time.
   *
   * @param originalPdf
   * @param targetPdf
   * @param pk
   * @param certChain
   * @param revoked
   * @param tsaAddress
   *          address of a rfc 3161 compatible timestamp server
   * @param reason
   *          reason for the signature
   * @param location
   *          location of signing
   * @param contact
   *          emailaddress of the person who is signing
   * @throws IOException
   * @throws DocumentException
   * @throws SignatureException
   */
  public static void signAndTimestamp(
      final InputStream originalPdf,
      final OutputStream targetPdf,
      final PrivateKey pk,
      final X509Certificate[] certChain,
      final CRL[] revoked,
      final String tsaAddress,
      final String reason,
      final String location,
      final String contact) throws IOException, DocumentException, SignatureException {
    // only an estimate, depends on the certificates returned by the TSA
    final int timestampSize = 4400;
    Security.addProvider(new BouncyCastleProvider());

    final PdfReader reader = new PdfReader(originalPdf);
    final PdfStamper stamper = PdfStamper.createSignature(reader, targetPdf, '\0');
    final PdfSignatureAppearance sap = stamper.getSignatureAppearance();

    // comment next lines to have an invisible signature
    sap.setVisibleSignature(new Rectangle(450, 650, 500, 700), 1, null);
    sap.setLayer2Text("");

    final PdfSigGenericPKCS sig = new PdfSigGenericPKCS.PPKMS("BC");
    final HashMap<PdfName, Integer> exclusionSizes = new HashMap<PdfName,
        Integer>();

    // some informational fields
    sig.setReason(reason);
    sig.setLocation(location);
    sig.setContact(contact);
    sig.setName(PdfPKCS7.getSubjectFields(certChain[0]).getField("CN"));
    sig.setDate(new PdfDate(Calendar.getInstance()));

    // signing stuff
    final byte[] digest = new byte[256];
    final byte[] rsaData = new byte[20];
    sig.setExternalDigest(digest, rsaData, "RSA");
    sig.setSignInfo(pk, certChain, revoked);
    final PdfString contents = (PdfString) sig.get(PdfName.CONTENTS);
    // *2 to get hex size, +2 for delimiters
    PdfLiteral contentsLit = new PdfLiteral((contents.toString().length() +
        timestampSize) * 2 + 2);
    exclusionSizes.put(PdfName.CONTENTS, new Integer(contentsLit.getPosLength()));
    sig.put(PdfName.CONTENTS, contentsLit);

    // certification; will display dialog or blue bar in Acrobat Reader

    sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);

    // process all the information set above
    sap.setCryptoDictionary(sig);
    sap.preClose(exclusionSizes);

    // calculate digest (hash)
    try {
      final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
      final byte[] buf = new byte[8192];
      int n;
      final InputStream inp = sap.getRangeStream();
      while ((n = inp.read(buf)) != -1) {
        messageDigest.update(buf, 0, n);
      }
      final byte[] hash = messageDigest.digest();

      // make signature (SHA1 the hash, prepend algorithm ID, pad, and encrypt with RSA)
      final Signature sign = Signature.getInstance("SHA1withRSA");
      sign.initSign(pk);
      sign.update(hash);
      final byte[] signature = sign.sign();

      // prepare the location of the signature in the target PDF
      contentsLit = (PdfLiteral) sig.get(PdfName.CONTENTS);
      final byte[] outc = new byte[(contentsLit.getPosLength() - 2) / 2];
      final PdfPKCS7 pkcs7 = sig.getSigner();
      pkcs7.setExternalDigest(signature, hash, "RSA");
      final PdfDictionary dic = new PdfDictionary();

      byte[] ssig = pkcs7.getEncodedPKCS7();
      try {
        // try to retrieve cryptographic timestamp from configured tsa server
        ssig = pkcs7.getEncodedPKCS7(null, null, new TSAClientBouncyCastle(tsaAddress), null);
      } catch (final RuntimeException e) {
        log.error("Could not retrieve timestamp from server.", e);
      }
      System.arraycopy(ssig, 0, outc, 0, ssig.length);

      // add the timestamped signature
      dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));

      // finish up
      sap.close(dic);
    } catch (final InvalidKeyException e) {
      throw new RuntimeException("Internal implementation error! No such signature type.", e);
    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException("Internal implementation error! No such algorithm type.", e);
    }
  }

}
