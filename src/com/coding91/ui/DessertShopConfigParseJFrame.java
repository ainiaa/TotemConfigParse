package com.coding91.ui;

import com.coding91.parser.ConfigParserDispatch;
import com.coding91.utils.FileUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Administrator
 */
public class DessertShopConfigParseJFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form TotemConfigParseJFrame
     */
    public DessertShopConfigParseJFrame() {
        Map<String, String> configMap = FileUtils.loadSetting("resources/data/setting.properties");
        configBaseDir = configMap.get("configBaseDir");
        outputDirectory = configMap.get("outputDirectory");
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        funcbuttonGroup = new javax.swing.ButtonGroup();
        allInOnejbuttonGroup = new javax.swing.ButtonGroup();
        jLayeredPane = new javax.swing.JLayeredPane();
        funjPanel = new javax.swing.JPanel();
        shopObjItemjRadioButton = new javax.swing.JRadioButton();
        activityLibraryjRadioButton = new javax.swing.JRadioButton();
        avatarItemsjRadioButton = new javax.swing.JRadioButton();
        bindingRecipejRadioButton = new javax.swing.JRadioButton();
        feedInfojRadioButton = new javax.swing.JRadioButton();
        goodsOrderjRadioButton = new javax.swing.JRadioButton();
        requestInfojRadioButton = new javax.swing.JRadioButton();
        gamejRadioButton = new javax.swing.JRadioButton();
        missionInfojRadioButton = new javax.swing.JRadioButton();
        dessertInfojRadioButton = new javax.swing.JRadioButton();
        giftPackagejRadioButton = new javax.swing.JRadioButton();
        selectConfgFilejPanel = new javax.swing.JPanel();
        configFilejLabel = new javax.swing.JLabel();
        configFilejTextField = new javax.swing.JTextField();
        configFilejButton = new javax.swing.JButton();
        outputjLabel = new javax.swing.JLabel();
        outputjTextField = new javax.swing.JTextField();
        outputjButton = new javax.swing.JButton();
        operationjPanel = new javax.swing.JPanel();
        parsejButton = new javax.swing.JButton();
        closejButton = new javax.swing.JButton();
        dsParseConfjToolBar = new javax.swing.JToolBar();
        bottomStatusjLabel = new javax.swing.JLabel();
        allInOnejPanel = new javax.swing.JPanel();
        allInOneYesjRadioButton = new javax.swing.JRadioButton();
        allInOneNojRadioButton = new javax.swing.JRadioButton();
        jMenuBar = new javax.swing.JMenuBar();
        filejMenu = new javax.swing.JMenu();
        settingjMenuItem = new javax.swing.JMenuItem();
        editjMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        funjPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("解析内容"));

        funcbuttonGroup.add(shopObjItemjRadioButton);
        shopObjItemjRadioButton.setSelected(true);
        shopObjItemjRadioButton.setText("shopItem");
        shopObjItemjRadioButton.setActionCommand("SHOP_ITEM");

        funcbuttonGroup.add(activityLibraryjRadioButton);
        activityLibraryjRadioButton.setText("activityLibraryInfo");
        activityLibraryjRadioButton.setActionCommand("ACTIVITY_LIB");

        funcbuttonGroup.add(avatarItemsjRadioButton);
        avatarItemsjRadioButton.setText("avatarItems");
        avatarItemsjRadioButton.setActionCommand("AVATAR_ITEMS");

        funcbuttonGroup.add(bindingRecipejRadioButton);
        bindingRecipejRadioButton.setText("bindingRecipe");
        bindingRecipejRadioButton.setActionCommand("BINDING_RECIPE");

        funcbuttonGroup.add(feedInfojRadioButton);
        feedInfojRadioButton.setText("feedInfo");
        feedInfojRadioButton.setActionCommand("FEED_INFO");

        funcbuttonGroup.add(goodsOrderjRadioButton);
        goodsOrderjRadioButton.setText("goodsOrder");
        goodsOrderjRadioButton.setActionCommand("GOODS_ORDER");

        funcbuttonGroup.add(requestInfojRadioButton);
        requestInfojRadioButton.setText("requestInfo");
        requestInfojRadioButton.setActionCommand("REQUEST_INFO");

        funcbuttonGroup.add(gamejRadioButton);
        gamejRadioButton.setText("game");
        gamejRadioButton.setActionCommand("GAME");

        funcbuttonGroup.add(missionInfojRadioButton);
        missionInfojRadioButton.setText("missionInfo");
        missionInfojRadioButton.setActionCommand("MISSION_INFO");

        funcbuttonGroup.add(dessertInfojRadioButton);
        dessertInfojRadioButton.setText("dessertInfo");
        dessertInfojRadioButton.setActionCommand("DESSERT_INFO");

        funcbuttonGroup.add(giftPackagejRadioButton);
        giftPackagejRadioButton.setText("giftPackage");
        giftPackagejRadioButton.setActionCommand("GIFT_PACKAGE");

        javax.swing.GroupLayout funjPanelLayout = new javax.swing.GroupLayout(funjPanel);
        funjPanel.setLayout(funjPanelLayout);
        funjPanelLayout.setHorizontalGroup(
            funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(funjPanelLayout.createSequentialGroup()
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bindingRecipejRadioButton)
                    .addComponent(shopObjItemjRadioButton)
                    .addComponent(giftPackagejRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(feedInfojRadioButton)
                    .addComponent(activityLibraryjRadioButton)
                    .addComponent(dessertInfojRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(missionInfojRadioButton)
                    .addComponent(avatarItemsjRadioButton)
                    .addComponent(requestInfojRadioButton))
                .addGap(27, 27, 27)
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gamejRadioButton)
                    .addComponent(goodsOrderjRadioButton))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        funjPanelLayout.setVerticalGroup(
            funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(funjPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shopObjItemjRadioButton)
                    .addComponent(activityLibraryjRadioButton)
                    .addComponent(avatarItemsjRadioButton)
                    .addComponent(goodsOrderjRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bindingRecipejRadioButton)
                    .addComponent(feedInfojRadioButton)
                    .addComponent(requestInfojRadioButton)
                    .addComponent(gamejRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(funjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missionInfojRadioButton)
                    .addComponent(dessertInfojRadioButton)
                    .addComponent(giftPackagejRadioButton))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jLayeredPane.add(funjPanel);
        funjPanel.setBounds(10, 0, 520, 160);

        selectConfgFilejPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("选择配置文件"));

        configFilejLabel.setText("配置文件：");

        configFilejTextField.setText(configBaseDir);
        configFilejTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                configFilejTextFieldMouseClicked(evt);
            }
        });

        configFilejButton.setText("浏览");
        configFilejButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                configFilejButtonMouseClicked(evt);
            }
        });

        outputjLabel.setText("输出目录：");

        outputjTextField.setText(outputDirectory);
        outputjTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outputjTextFieldMouseClicked(evt);
            }
        });

        outputjButton.setText("浏览");
        outputjButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outputjButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout selectConfgFilejPanelLayout = new javax.swing.GroupLayout(selectConfgFilejPanel);
        selectConfgFilejPanel.setLayout(selectConfgFilejPanelLayout);
        selectConfgFilejPanelLayout.setHorizontalGroup(
            selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectConfgFilejPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputjLabel)
                    .addComponent(configFilejLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(configFilejTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .addComponent(outputjTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(configFilejButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputjButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(78, 78, 78))
        );
        selectConfgFilejPanelLayout.setVerticalGroup(
            selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectConfgFilejPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(configFilejLabel)
                    .addComponent(configFilejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(configFilejButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selectConfgFilejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputjLabel)
                    .addComponent(outputjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputjButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane.add(selectConfgFilejPanel);
        selectConfgFilejPanel.setBounds(10, 220, 520, 80);

        operationjPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("操作"));

        parsejButton.setText("解析");
        parsejButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parsejButtonMouseClicked(evt);
            }
        });

        closejButton.setText("取消");
        closejButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closejButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout operationjPanelLayout = new javax.swing.GroupLayout(operationjPanel);
        operationjPanel.setLayout(operationjPanelLayout);
        operationjPanelLayout.setHorizontalGroup(
            operationjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(operationjPanelLayout.createSequentialGroup()
                .addContainerGap(139, Short.MAX_VALUE)
                .addComponent(parsejButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(closejButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );
        operationjPanelLayout.setVerticalGroup(
            operationjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(operationjPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(operationjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parsejButton)
                    .addComponent(closejButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane.add(operationjPanel);
        operationjPanel.setBounds(10, 310, 520, 67);

        dsParseConfjToolBar.setRollover(true);
        dsParseConfjToolBar.setMaximumSize(new java.awt.Dimension(520, 22));
        dsParseConfjToolBar.setMinimumSize(new java.awt.Dimension(520, 22));

        bottomStatusjLabel.setFocusable(false);
        bottomStatusjLabel.setMaximumSize(new java.awt.Dimension(520, 20));
        bottomStatusjLabel.setMinimumSize(new java.awt.Dimension(520, 20));
        bottomStatusjLabel.setName(""); // NOI18N
        dsParseConfjToolBar.add(bottomStatusjLabel);

        jLayeredPane.add(dsParseConfjToolBar);
        dsParseConfjToolBar.setBounds(0, 380, 530, 20);

        allInOnejPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("一次性全转换"));

        allInOnejbuttonGroup.add(allInOneYesjRadioButton);
        allInOneYesjRadioButton.setText("是");
        allInOneYesjRadioButton.setActionCommand("YES");

        allInOnejbuttonGroup.add(allInOneNojRadioButton);
        allInOneNojRadioButton.setSelected(true);
        allInOneNojRadioButton.setText("否");
        allInOneNojRadioButton.setActionCommand("NO");

        javax.swing.GroupLayout allInOnejPanelLayout = new javax.swing.GroupLayout(allInOnejPanel);
        allInOnejPanel.setLayout(allInOnejPanelLayout);
        allInOnejPanelLayout.setHorizontalGroup(
            allInOnejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(allInOnejPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(allInOneYesjRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(allInOneNojRadioButton)
                .addContainerGap(410, Short.MAX_VALUE))
        );
        allInOnejPanelLayout.setVerticalGroup(
            allInOnejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(allInOnejPanelLayout.createSequentialGroup()
                .addGroup(allInOnejPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allInOneYesjRadioButton)
                    .addComponent(allInOneNojRadioButton))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jLayeredPane.add(allInOnejPanel);
        allInOnejPanel.setBounds(10, 160, 520, 60);

        filejMenu.setText("文件");

        settingjMenuItem.setText("设置");
        settingjMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingjMenuItemMouseClicked(evt);
            }
        });
        filejMenu.add(settingjMenuItem);

        jMenuBar.add(filejMenu);

        editjMenu.setText("编辑");
        jMenuBar.add(editjMenu);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void parsejButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_parsejButtonMouseClicked
        
        ControllerJFrame.showNoticeMessageJFrame();
        
        String allInOneFunc = "NO";
        if (funcbuttonGroup.getSelection() != null) {
            allInOneFunc = funcbuttonGroup.getSelection().getActionCommand();
        }

        String msg = "";
        if ("YES".equals(allInOneFunc)) {//全部转换
            //配置文件
            String configFilePath = configFilejTextField.getText();
            //输出路径
            String outputPath = outputjTextField.getText();
            Map<String, String> funcMap = initFuncList();
            funcMap.entrySet().stream().forEach((entry) -> {
                String func = entry.getKey();
                String excelFileName = entry.getValue();
                ConfigParserDispatch.transformSingleExcel(func, configFilePath + "/" + excelFileName, outputPath);
            });
        } else {//转换某一个配置项
            String func = "";
            if (funcbuttonGroup.getSelection() != null) {
                func = funcbuttonGroup.getSelection().getActionCommand();
            }

            //配置文件
            String configFilePath = configFilejTextField.getText();
            //输出路径
            String outputPath = outputjTextField.getText();

            if (func.isEmpty()) {
                msg = "请选择解析内容 ";
            } else if (configFilePath.isEmpty()) {
                msg = "请选择待解析文件(xls) ";
            } else if (outputPath.isEmpty()) {
                msg = "请选择输出路径";
            }
            if (!msg.isEmpty()) {
                JOptionPane.showMessageDialog(null, msg, "信息提示", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            ConfigParserDispatch.transformSingleExcel(func, configFilePath, outputPath);
        }

//        JOptionPane.showMessageDialog(null, "转换成功");
//        this.dispose();
    }//GEN-LAST:event_parsejButtonMouseClicked

    private Map<String, String> initFuncList() {
        Map funcList = new HashMap();
        funcList.put("SHOP_ITEM", "shopItem.xls");
        funcList.put("ACTIVITY_LIB", "activityLibraryInfo.xls");
        funcList.put("AVATAR_ITEMS", "avatarItems.xls");
        funcList.put("GOODS_ORDER", "goodsOrder.xls");
        funcList.put("BINDING_RECIPE", "bindingRecipe.xls");
        funcList.put("FEED_INFO", "feedInfo.xls");
        funcList.put("REQUEST_INFO", "requestInfo.xls");
        funcList.put("GAME", "game.xls");
        funcList.put("GIFT_PACKAGE", "giftPackage.xls");
        funcList.put("DESSERT_INFO", "dessertInfo.xls");
        funcList.put("MISSION_INFO", "missionInfo.xls");
        return funcList;
    }

    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }

    public static void showErrorMsg(String msg, String title) {
        showErrorMsg(msg, title, true);
    }

    public static void showErrorMsg(String msg, String title, boolean needExit) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, null);
        if (needExit) {
            System.exit(0);
        }
    }

    public static void showErrorMsgWithoutExit(String msg, String title) {
        showErrorMsg(msg, title, false);
    }

    public void transformFinish(String message) {
        JOptionPane.showMessageDialog(null, message);
        this.dispose();
    }

    private void closejButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closejButtonMouseClicked
        this.dispose();
    }//GEN-LAST:event_closejButtonMouseClicked

    private void settingjMenuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingjMenuItemMouseClicked
    }//GEN-LAST:event_settingjMenuItemMouseClicked

    private void outputjButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputjButtonMouseClicked
        this.outputjTextFieldMouseClicked(evt);
    }//GEN-LAST:event_outputjButtonMouseClicked

    private void outputjTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputjTextFieldMouseClicked
        File f = new File(outputDirectory);
        JFileChooser jfc = new JFileChooser(f);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result;
        result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            outputjTextField.setText(file.getPath());
        }
    }//GEN-LAST:event_outputjTextFieldMouseClicked

    private void configFilejButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_configFilejButtonMouseClicked
        this.configFilejTextFieldMouseClicked(evt);
    }//GEN-LAST:event_configFilejButtonMouseClicked

    private void configFilejTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_configFilejTextFieldMouseClicked
        File f = new File(configBaseDir);
        JFileChooser jfc = new JFileChooser(f);
        //        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result;
        result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            configFilejTextField.setText(file.getPath());
        }
    }//GEN-LAST:event_configFilejTextFieldMouseClicked


    private static void showMessageDialogMessage(Exception ex) {
        String exMsg = ex.toString();
        JOptionPane.showMessageDialog(null, exMsg + new Throwable().getStackTrace()[1].toString(), "错误信息提示", JOptionPane.ERROR_MESSAGE);
    }

    private final String configBaseDir;
    private final String outputDirectory;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton activityLibraryjRadioButton;
    private javax.swing.JRadioButton allInOneNojRadioButton;
    private javax.swing.JRadioButton allInOneYesjRadioButton;
    private javax.swing.JPanel allInOnejPanel;
    private javax.swing.ButtonGroup allInOnejbuttonGroup;
    private javax.swing.JRadioButton avatarItemsjRadioButton;
    private javax.swing.JRadioButton bindingRecipejRadioButton;
    private javax.swing.JLabel bottomStatusjLabel;
    private javax.swing.JButton closejButton;
    private javax.swing.JButton configFilejButton;
    private javax.swing.JLabel configFilejLabel;
    private javax.swing.JTextField configFilejTextField;
    private javax.swing.JRadioButton dessertInfojRadioButton;
    private javax.swing.JToolBar dsParseConfjToolBar;
    private javax.swing.JMenu editjMenu;
    private javax.swing.JRadioButton feedInfojRadioButton;
    private javax.swing.JMenu filejMenu;
    private javax.swing.ButtonGroup funcbuttonGroup;
    private javax.swing.JPanel funjPanel;
    private javax.swing.JRadioButton gamejRadioButton;
    private javax.swing.JRadioButton giftPackagejRadioButton;
    private javax.swing.JRadioButton goodsOrderjRadioButton;
    private javax.swing.JLayeredPane jLayeredPane;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JRadioButton missionInfojRadioButton;
    private javax.swing.JPanel operationjPanel;
    private javax.swing.JButton outputjButton;
    private javax.swing.JLabel outputjLabel;
    private javax.swing.JTextField outputjTextField;
    private javax.swing.JButton parsejButton;
    private javax.swing.JRadioButton requestInfojRadioButton;
    private javax.swing.JPanel selectConfgFilejPanel;
    private javax.swing.JMenuItem settingjMenuItem;
    private javax.swing.JRadioButton shopObjItemjRadioButton;
    // End of variables declaration//GEN-END:variables
}
