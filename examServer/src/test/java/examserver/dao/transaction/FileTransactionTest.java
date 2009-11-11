package examserver.dao.transaction;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.thorstenberger.examServer.dao.AbstractTransactionalFileIO;

/**
 * @author Steffen Dienst
 *
 */
public class FileTransactionTest {
	class FileContentIncrementer extends AbstractTransactionalFileIO{

		public FileContentIncrementer(String workingDirectory) {
	    super(workingDirectory);
    }
		public void append(byte b) {
			getFRM().setDefaultTransactionTimeout(10000);
			String txId = startTransaction();
			try {
	      OutputStream os = getFRM().writeResource(txId, "file.txt",true);
	      os.write(b);
	      os.close();
	      getFRM().commitTransaction(txId);
      } catch (ResourceManagerException e) {
      	e.printStackTrace();
	      Assert.fail(e.getMessage());
      } catch (IOException e) {
	      e.printStackTrace();
	      Assert.fail(e.getMessage());
      }
		}
		public void write(String fileresource,String contents) throws IOException, ResourceManagerException {
			String txId = startTransaction();
			IOUtils.write(contents,getFRM().writeResource(txId, fileresource));
			commitTransaction(txId);
		}
		public void appendWOTransaction(byte b) {
			try {
			FileOutputStream fos = new FileOutputStream(new File(this.workingPath,"file.txt"),true);
			fos.write(b);
			fos.close();
			}catch(IOException e) {
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
		}
		public InputStream readFile() throws ResourceManagerException {
			return getFRM().readResource("file.txt");
    }
		
	}
	private FileContentIncrementer io;
	private File tempDir;
	
	@Before
	public void setup() throws IOException {
		tempDir = File.createTempFile("transaction", "test");
		tempDir.delete();
		tempDir.mkdir();
		
		io = new FileContentIncrementer(tempDir.getAbsolutePath());
	}
	
	@Test
	public void shouldNotOverwriteConcurrentFileChanges() throws Exception {
		// given 10 concurrent threads
		ExecutorService execService = Executors.newFixedThreadPool(10);
		Runnable runnable = new Runnable() {
	    public void run() {
//	    	io.appendWOTransaction((byte)'x');
	    	io.append((byte)'x');
	    }
    };
		// when appending a character 100 times
    for(int i = 0; i< 1000;i++)
    	execService.submit(runnable);
    execService.shutdown();
    execService.awaitTermination(1, TimeUnit.MINUTES);
		// then
		assertEquals(1000,IOUtils.toByteArray(io.readFile()).length);
	}
	@Test
  public void shouldCreateSubdirsInTransactions() throws Exception {
	  // given
	  String subdir = "foo/bar/test.txt";
	  // when
	  io.write(subdir, "test");
	  // then
	  Assert.assertTrue(new File(tempDir,subdir).exists());
  }

}
