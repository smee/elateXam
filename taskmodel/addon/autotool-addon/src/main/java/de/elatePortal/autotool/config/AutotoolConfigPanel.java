/*

Copyright (C) 2006 Steffen Dienst

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
package de.elatePortal.autotool.config;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.xmlrpc.XmlRpcException;

import autotool.AutotoolServices;
import autotool.AutotoolTaskConfig;
import autotool.SignedAutotoolTaskConfig;

public class AutotoolConfigPanel extends JPanel {

    public static void main(final String[] args) throws InterruptedException {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final AutotoolConfigPanel atcp = new AutotoolConfigPanel();
        f.add(atcp);
        f.pack();
        f.setVisible(true);
        // Thread.sleep(3000);
        // ComboBoxModel cbm=atcp.getJTaskTypesCb().getModel();
        // for (int i = 0; i < cbm.getSize(); i++) {
        // String o=(String) cbm.getElementAt(i);
        // if(!o.contains("Quiz") || o.startsWith("Algebraic"))
        // continue;
        // cbm.setSelectedItem(o);
        // atcp.invalidate();
        // atcp.repaint();
        // Thread.sleep(50);
        // atcp.getJVerifyButton().doClick();
        // while(atcp.getSignature().length()==0)
        // Thread.sleep(50);
        // atcp.getJGenerateXmlButton().doClick();
        // }
    }

    private JComboBox jTaskTypesCb = null;
    private JButton jRefreshTasks = null;
    private JTextPane jConfigTextPane = null;
    private JButton jVerifyButton = null;
    private final Map<String, String> task2configMap;
    String currentTaskType = null;
    private String url;
    private JTextField jAutotoolUrlTf = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private AutotoolServices ats;
    private JLabel jLabel3 = null;
    private JTextField jSignatureTF = null;
    private JButton jGenerateXmlButton = null;
    private int idCounter = 0;

    /**
     * This method initializes
     *
     */
    public AutotoolConfigPanel() {
        super();
        task2configMap = new HashMap<String, String>();
        initialize();
    }

    private String escapeXML(final String text) {
        return text.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\\'", "&apos;")
                .replaceAll("\\\"", "&quot;");
    }

    private String generateXML() {
        final StringBuilder sb = new StringBuilder("<addonSubTaskDef id=\"auto").append(idCounter++).append(
                "\" taskType=\"autotool\" interactiveFeedback=\"true\">\n")
                .append("  <Memento>\n")
                .append("     <taskType>").append(getTaskType()).append("</taskType>\n")
                .append("     <configString>").append(escapeXML(getConfigString())).append("</configString>\n")
                .append("     <autotoolServerUrl>").append(getUrl()).append("</autotoolServerUrl>\n")
                .append("     <signature>").append(getSignature()).append("</signature>\n")
                .append("  </Memento>\n")
                .append("</addonSubTaskDef>\n");
        return sb.toString();
    }

    public String getConfigString() {
        return getConfigTextPane().getText();
    }

    /**
     * This method initializes jConfigPane
     *
     * @return javax.swing.JTextPane
     */
    private JTextPane getConfigTextPane() {
        if (jConfigTextPane == null) {
            jConfigTextPane = new JTextPane();
            jConfigTextPane.setPreferredSize(new java.awt.Dimension(300, 200));
        }
        return jConfigTextPane;
    }

    /**
     * This method initializes jAutotoolUrlTf
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJAutotoolUrlTf() {
        if (jAutotoolUrlTf == null) {
      jAutotoolUrlTf = new JTextField("http://autolat.imn.htwk-leipzig.de/cgi-bin/autotool-0.2.0.cgi");
        }
        return jAutotoolUrlTf;
    }

    /**
     * This method initializes jGenerateXmlButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJGenerateXmlButton() {
        if (jGenerateXmlButton == null) {
            jGenerateXmlButton = new JButton();
            jGenerateXmlButton.setText("Print XML");
            jGenerateXmlButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final String xmlSnippet = generateXML();
                    System.out.println(xmlSnippet);
                    JOptionPane.showMessageDialog(AutotoolConfigPanel.this, xmlSnippet, "Copied to clipboard:",
                            JOptionPane.PLAIN_MESSAGE);
                    final StringSelection text = new StringSelection(xmlSnippet);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(text, text);
                }
            });
        }
        return jGenerateXmlButton;
    }

    /**
     * This method initializes jSignatureTF
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJSignatureTF() {
        if (jSignatureTF == null) {
            jSignatureTF = new JTextField();
            jSignatureTF.setEnabled(false);
        }
        return jSignatureTF;
    }

    /**
     * This method initializes jTaskTypesCb
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getJTaskTypesCb() {
        if (jTaskTypesCb == null) {
            jTaskTypesCb = new JComboBox();
            jTaskTypesCb.addItem("Please press \"Refresh\" to get task types...");
            jTaskTypesCb.addItemListener(new ItemListener() {
                public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !e.getItem().equals(currentTaskType)) {
                        task2configMap.put(currentTaskType, getConfigTextPane().getText());
                        currentTaskType = (String) e.getItem();
                        String config = task2configMap.get(currentTaskType);
                        if (config == null) {
                            try {
                                config = ats.getConfig(currentTaskType).getConfigString();
                            } catch (final XmlRpcException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                        getConfigTextPane().setText(config);
                    }
                }
            });
        }
        return jTaskTypesCb;
    }

    /**
     * This method initializes jVerifyButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJVerifyButton() {
        if (jVerifyButton == null) {
            jVerifyButton = new JButton();
            jVerifyButton.setText("Verify");
            jVerifyButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (ats != null) {
                        try {
                            jVerifyButton.setEnabled(false);
                            final SignedAutotoolTaskConfig satc = ats.getSignedConfig(new AutotoolTaskConfig() {
                                    public String getConfigString() {
                                    return AutotoolConfigPanel.this.getConfigString();
                                    }

                                public String getDocumentation() {
                                    return "";
                                    }

                                public String getTaskType() {
                                    return currentTaskType;
                                    }

                                public void setConfigString(final String arg0) {
                                    }
                                                                });
                            getJSignatureTF().setText(satc.getSignature());
                            getJGenerateXmlButton().setEnabled(true);
                        } catch (final XmlRpcException e1) {
                            e1.printStackTrace();
                            jVerifyButton.setEnabled(true);
                            jSignatureTF.setText("");
                            getJGenerateXmlButton().setEnabled(false);
                        }
                    }
                }
            });
            getConfigTextPane().getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(final DocumentEvent e) {
                    handleChange();
                    }

                private void handleChange() {
                    jVerifyButton.setEnabled(true);
                    jSignatureTF.setText("");
                    getJGenerateXmlButton().setEnabled(false);
                    }

                public void insertUpdate(final DocumentEvent e) {
                    handleChange();
                    }

                public void removeUpdate(final DocumentEvent e) {
                    handleChange();
                    }
                                });
            getJTaskTypesCb().addItemListener(new ItemListener() {
                    public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !e.getItem().equals(currentTaskType)) {
                        jSignatureTF.setText("");
                    }
                    }

            });
            // ask autotool for task types automatically on gui creation
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    getRefreshButton().doClick();
                }
            });
        }
        return jVerifyButton;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getRefreshButton() {
        if (jRefreshTasks == null) {
            jRefreshTasks = new JButton();
            jRefreshTasks.setText("Refresh");
            jRefreshTasks.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    setUrl(getJAutotoolUrlTf().getText());
                }
            });
        }
        return jRefreshTasks;
    }

    public String getSignature() {
        return jSignatureTF.getText();
    }

    public String getTaskType() {
        return currentTaskType;
    }

    public String getUrl() {
        return url;
    }

    private void initAutotoolServices() {
        try {
            this.ats = new AutotoolServices(new URL(url));
            final List<String> tasktypes = ats.getTaskTypes();
            final DefaultComboBoxModel model = (DefaultComboBoxModel) getJTaskTypesCb().getModel();
            model.removeAllElements();
            for (final String s : tasktypes) {
                model.addElement(s);
            }
        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final XmlRpcException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        final GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.gridx = 3;
        gridBagConstraints31.gridy = 3;
        final GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 3;
        gridBagConstraints21.weightx = 1.0;
        gridBagConstraints21.gridx = 1;
        final GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.gridx = 0;
        gridBagConstraints12.gridy = 3;
        jLabel3 = new JLabel();
        jLabel3.setText("Signature");
        final GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.gridy = 2;
        jLabel2 = new JLabel();
        jLabel2.setText("Configuration");
        final GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 1;
        jLabel1 = new JLabel();
        jLabel1.setText("Tasktypes");
        final GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 0;
        jLabel = new JLabel();
        jLabel.setText("Autotoolurl");
        final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new java.awt.Insets(10, 10, 10, 5);
        gridBagConstraints3.gridx = 1;
        final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.insets = new java.awt.Insets(10, 10, 10, 0);
        gridBagConstraints2.gridy = 2;
        final GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints11.gridy = 2;
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.weighty = 1.0;
        gridBagConstraints11.gridwidth = 2;
        gridBagConstraints11.insets = new java.awt.Insets(10, 10, 10, 0);
        gridBagConstraints11.gridx = 1;
        final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(12, 10, 13, 5);
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridx = 3;
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 2);
        this.setLayout(new GridBagLayout());
        this.add(getJTaskTypesCb(), gridBagConstraints);
        this.add(getRefreshButton(), gridBagConstraints1);
        this.add(getConfigTextPane(), gridBagConstraints11);
        this.add(getJVerifyButton(), gridBagConstraints2);
        this.add(getJAutotoolUrlTf(), gridBagConstraints3);
        this.add(jLabel, gridBagConstraints4);
        this.add(jLabel1, gridBagConstraints5);
        this.add(jLabel2, gridBagConstraints6);
        this.add(jLabel3, gridBagConstraints12);
        this.add(getJSignatureTF(), gridBagConstraints21);
        this.add(getJGenerateXmlButton(), gridBagConstraints31);
    }

    public void setConfigString(final String value) {
        getConfigTextPane().setText(value);
    }

    public void setSignature(final String value) {
        getJSignatureTF().setText(value);
    }

    public void setTaskType(final String value) {
        currentTaskType = value;
        if (!taskTypeKnown(value)) {
            getJTaskTypesCb().addItem(value);
        }
        getJTaskTypesCb().setSelectedItem(value);
    }

    public void setUrl(final String value) {
        this.url = value;
        initAutotoolServices();
    }

    private boolean taskTypeKnown(final String value) {
        final ComboBoxModel model = getJTaskTypesCb().getModel();
        final int len = model.getSize();
        int ptr = 0;
        while (ptr < len) {
            if (model.getElementAt(ptr++).equals(value))
              return true;
        }
        return false;
    }
}
