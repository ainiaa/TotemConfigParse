package com.coding91.logic;

import com.coding91.parser.BuildConfigContent;
import com.coding91.transform.runable.TransformRunable;
import com.coding91.ui.DessertShopConfigParseJFrame;
import com.coding91.ui.NoticeMessageJFrame;
import com.coding91.utils.DateTimeUtils;
import com.coding91.utils.ExcelParserUtils;
import com.coding91.utils.FileUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.read.biff.BiffException;

/**
 *
 * @author Administrator
 */
public class TransformConfigLogic {

    /**
     * mission 特例
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     */
    public static void transformMissionTriggerContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int sheetIndex = ExcelParserUtils.getSheetIndexBySheetName(configFilePath, sheetName);
                    final String[][] originContent = ExcelParserUtils.parseXls(configFilePath, sheetIndex, true);

                    String[] langList = DessertShopConfigParseJFrame.getLangList().toArray(new String[]{}); 

                    for (final String currentLang : langList) {
                        // start single lang 
                        Thread currentThread = transformMissionTriggerThread(currentLang, outputPath, fileName, originContent);
                        currentThread.start();
                        threadList.add(currentThread);
                        //end single lang
                    }

                    threadList.stream().forEach((t) -> {
                        try {
                            t.join();
                        } catch (InterruptedException ex) {
                            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                        }
                    });

                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;
                    NoticeMessageJFrame.noticeMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
//                    ConfigParser.transformFinish("完成转换!");
                } catch (IOException | BiffException ex) {
                    NoticeMessageJFrame.showMessageDialogMessage(ex);
                }
            }
        }).start();
    }

    /**
     * 通用
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     * @param idField
     * @param extraParams
     */
    public static void transformCommonContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map<String, Map> extraParams) {
        final long startTime = System.currentTimeMillis();
        TransformRunable transformRunExable = new TransformRunable(configFilePath, sheetName, outputPath, fileName, idField, extraParams, startTime);
        Thread transformThread = new Thread(transformRunExable);
        transformThread.start();
    }

    public static Thread transformMissionTriggerThread(final String currentLang, final String outputPath, final String fileName, final String[][] commonContent) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                int index = 0;
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = BuildConfigContent.buildMissionTriggerSingleRowStr(commonContent[i], index);
                    String id = singleRowInfo.get("id");
                    if (!id.isEmpty()) {//空id 直接无视
                        index++;
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                        } catch (FileNotFoundException ex) {
                            NoticeMessageJFrame.showMessageDialogMessage(ex);
                        } catch (IOException ex) {
                            NoticeMessageJFrame.showMessageDialogMessage(ex);
                        }
                        if (i == 1) {//第一行 没有必要添加\r\n
                            allContent.append(currentAllItemInfo);
                        } else {
                            allContent.append("\r\n").append(currentAllItemInfo);
                        }
                        NoticeMessageJFrame.noticeMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + descFile);
                    }
                }
                try {
                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    FileUtils.writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                    NoticeMessageJFrame.noticeMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + descFile + "\r\n\r\n");
                } catch (FileNotFoundException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                } catch (IOException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                }
            }
        });
        return currentThread;
    }

    /**
     * 需要跳过的field
     *
     * @param combineFields
     * @return
     */
    private static List<String> needSkipedFields(Map<String, String[]> combineFields) {
        List<String> needSkipedFields = new ArrayList();
        if (combineFields != null && combineFields.size() > 0) {
            combineFields.keySet().stream().map((key) -> combineFields.get(key)).forEach((value) -> {
                int len = value.length;
                for (int i = 0; i < len; i++) {
                    needSkipedFields.add(value[i]);
                }
            });
        }
        return needSkipedFields;
    }

    public static Map getModel(String[] originSingleContent, Map<String, String[]> combineFields) {
        Map<String, List<Integer>> fieldIndex = new HashMap();
        Map<String, List<String>> fieldName = new HashMap();
        String[] langList = DessertShopConfigParseJFrame.getLangList().toArray(new String[]{});
        List needSkipedFields = needSkipedFields(combineFields);

        for (String currentLang : langList) {
            fieldIndex.put(currentLang, new ArrayList());
            fieldName.put(currentLang, new ArrayList());
        }
        for (int j = 0; j < originSingleContent.length; j++) {
            String originField = originSingleContent[j];
            boolean currentPrefixIsLang = false;
            String prefix = "";
            if (!originField.trim().isEmpty()) {
                if (originField.length() >= 6) {//查看是否为属于某个特定的语言
                    prefix = originField.substring(0, 5);
                    currentPrefixIsLang = Arrays.asList(langList).contains(prefix);
                }
                if (currentPrefixIsLang) {//当前field 只属于某一个lang
                    List currentModelIndex = fieldIndex.get(prefix);
                    List currentModelField = fieldName.get(prefix);
                    String currentFiled = originField.substring(6);
                    if (!needSkipedFields.contains(currentFiled)) {//需要掉过的field不需要出现在最终的model中
                        currentModelIndex.add(j);
                        currentModelField.add(currentFiled);
                    }
                } else {//当前field 属于所有lang
                    if (!needSkipedFields.contains(originField)) { //需要掉过的field不需要出现在最终的model中
                        for (String currentLang : langList) {
                            List currentModelIndex = fieldIndex.get(currentLang);
                            List currentModelField = fieldName.get(currentLang);
                            currentModelIndex.add(j);
                            currentModelField.add(originField);
                        }
                    }
                }
            }
        }

        Map finalInfo = new HashMap();
        finalInfo.put("fieldIndex", fieldIndex);
        finalInfo.put("fieldName", fieldName);
        return finalInfo;
    }

    /**
     *
     * @param originSingleContent
     * @return
     */
    public static Map getModel(String[] originSingleContent) {
        return getModel(originSingleContent, null);
    }
}
