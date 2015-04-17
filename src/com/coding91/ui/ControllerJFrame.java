package com.coding91.ui;

import javax.swing.UIManager;

/**
 *
 * @author Jeff Liu
 */
public class ControllerJFrame {

    private static DessertShopConfigParseJFrame dessertShopConfigParseJFrame;
    private static NoticeMessageJFrame noticeMessageJFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//
//                }
//            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
        }

        dessertShopConfigParseJFrame = new DessertShopConfigParseJFrame();
        noticeMessageJFrame = new NoticeMessageJFrame();
        dessertShopConfigParseJFrame.setVisible(true);
        dessertShopConfigParseJFrame.setLocationRelativeTo(null);
    }

    public static void dispose() {
        dessertShopConfigParseJFrame.dispose();
        noticeMessageJFrame.dispose();
        System.exit(0);
    }

    public static void showNoticeMessageJFrame() {
        if (noticeMessageJFrame == null) {
            noticeMessageJFrame = new NoticeMessageJFrame();
        }
        if (dessertShopConfigParseJFrame == null) {
            dessertShopConfigParseJFrame = new DessertShopConfigParseJFrame();
        }
        noticeMessageJFrame.setLocationRelativeTo(null);
        dessertShopConfigParseJFrame.setVisible(false);
        noticeMessageJFrame.setVisible(true);

    }

    public static void showDessertShopConfigParseJFrame() {
        if (noticeMessageJFrame == null) {
            noticeMessageJFrame = new NoticeMessageJFrame();
        }
        if (dessertShopConfigParseJFrame == null) {
            dessertShopConfigParseJFrame = new DessertShopConfigParseJFrame();
        }
        dessertShopConfigParseJFrame.setLocationRelativeTo(null);
        noticeMessageJFrame.setVisible(false);
        dessertShopConfigParseJFrame.setVisible(true);
    }
}
