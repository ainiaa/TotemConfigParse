package com.coding91.ui;

import com.coding91.utils.FileUtils;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class NoticeMessageJFrame extends javax.swing.JFrame {

    public NoticeMessageJFrame() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("resources/images/sync.png")));//这个不能以 '/'开头
        initComponents();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//点击窗口的关闭 退出程序
        Map<String, String> configMap = FileUtils.loadSetting("resources/data/setting.properties");
        dsParseConfjToolBar.setToolTipText(configMap.get("versionInfo"));
        bottomStatusjLabel.setText(configMap.get("versionInfo"));

        try {
            printer();
        } catch (IOException ex) {

        }
    }

//    public static void main(String args[]) {
//        NoticeMessageJFrame nm = new NoticeMessageJFrame();
//        nm.setVisible(true);
//    }
    /**
     *
     * @param msg
     */
    public static void noticeMessage(String msg) {
        System.out.println(msg);
    }

    /**
     *
     * @param ex
     */
    public static void noticeMessage(Exception ex) {
        String exMsg = ex.toString();
        System.out.println(exMsg + new Throwable().getStackTrace()[1].toString());
    }

    public static void showMessageDialogMessage(Exception ex) {
        String exMsg = ex.toString();
        JOptionPane.showMessageDialog(null, exMsg + new Throwable().getStackTrace()[1].toString(), "错误信息提示", JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorMsg(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, null);
        System.exit(0);
    }

    public static void showMessageDialogMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        dsParseConfjToolBar = new javax.swing.JToolBar();
        bottomStatusjLabel = new javax.swing.JLabel();
        noticeMessagejScrollPane = new javax.swing.JScrollPane();

        setBackground(new java.awt.Color(51, 51, 51));

        dsParseConfjToolBar.setRollover(true);
        dsParseConfjToolBar.setMaximumSize(new java.awt.Dimension(520, 22));
        dsParseConfjToolBar.setMinimumSize(new java.awt.Dimension(520, 22));

        bottomStatusjLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bottomStatusjLabel.setFocusable(false);
        bottomStatusjLabel.setMaximumSize(new java.awt.Dimension(520, 20));
        bottomStatusjLabel.setMinimumSize(new java.awt.Dimension(520, 20));
        bottomStatusjLabel.setName(""); // NOI18N
        dsParseConfjToolBar.add(bottomStatusjLabel);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(noticeMessagejScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(dsParseConfjToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addComponent(noticeMessagejScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dsParseConfjToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jLayeredPane1.setLayer(dsParseConfjToolBar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(noticeMessagejScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void printer() throws IOException {
        ConsoleTextArea consoleTextArea;

        try {
            consoleTextArea = new ConsoleTextArea();
            consoleTextArea.setFont(java.awt.Font.decode("monospaced"));
            consoleTextArea.setColumns(20);
            consoleTextArea.setRows(4);
            noticeMessagejScrollPane.setViewportView(consoleTextArea);
        } catch (IOException e) {
            System.err.println("cannot create LoopedStreams" + e);
            System.exit(1);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bottomStatusjLabel;
    private javax.swing.JToolBar dsParseConfjToolBar;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane noticeMessagejScrollPane;
    // End of variables declaration//GEN-END:variables
}
