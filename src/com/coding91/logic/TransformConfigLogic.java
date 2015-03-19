/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.logic;

import com.coding91.parser.BuildConfigContent;
import com.coding91.parser.ConfigParser;
import static com.coding91.parser.ConfigParser.getLangs;
import static com.coding91.parser.ConfigParser.notifyMessage;
import static com.coding91.parser.ConfigParser.showMessageDialogMessage;
import com.coding91.transformRunable.TransformRunable;
import com.coding91.utils.DateTimeUtils;
import com.coding91.utils.ExcelParser;
import com.coding91.utils.FileUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import jxl.read.biff.BiffException;

/**
 *
 * @author Administrator
 */
public class TransformConfigLogic {

    /**
     * 甜品店 shop object item
     *
     * @param configFilePath
     * @param outputPath
     */
    public static void transformShopObjectItem(final String configFilePath, final String outputPath) {
        JOptionPane.showMessageDialog(null, "转换开始");
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int sheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, "Worksheet");
                    final String[][] dsShopObjectContentArray = ExcelParser.parseXls(configFilePath, sheetIndex, true);
                    final Map<String, Map<String, List<String>>> modelInfo = TransformConfigLogic.getModel(dsShopObjectContentArray[0]);
                    String[] langList = getLangs();
                    final Map<String, String> fieldDefaultPair = FileUtils.loadSetting("resources/data/config/defaultvalue/shopItem.properties", true);

                    for (final String currentLang : langList) {
                        // start single lang 
                        Thread currentThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int itemIdIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), "item_id");
                                StringBuilder allItemInfo = new StringBuilder();
                                allItemInfo.append(" return array (\r\n");
                                for (int i = 1; i < dsShopObjectContentArray.length; i++) {
                                    Map<String, String> singleRowInfo = BuildConfigLogic.buildSingleItemStr(dsShopObjectContentArray[i], modelInfo, currentLang, itemIdIndex, fieldDefaultPair);
                                    String itemId = singleRowInfo.get("itemId");
                                    String singleItemInfo = singleRowInfo.get("singleItemInfo");
                                    String currentAllItemInfo = singleRowInfo.get("allItemInfo");
                                    String descFile = BuildConfigLogic.buildSingleItemStoredPath(currentLang, itemId, outputPath);
                                    try {
                                        FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                                    } catch (FileNotFoundException ex) {
                                        showMessageDialogMessage(ex);
                                    } catch (IOException ex) {
                                        showMessageDialogMessage(ex);
                                    }
                                    if (i == 1) {//第一行 没有必要添加\r\n
                                        allItemInfo.append(currentAllItemInfo);
                                    } else {
                                        allItemInfo.append("\r\n").append(currentAllItemInfo);
                                    }
                                    notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / dsShopObjectContentArray.length) + "%|正在生成文件:" + outputPath);
                                }
                                try {
                                    notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                                    String descFile = BuildConfigLogic.buildSingleItemStoredPath(currentLang, "", outputPath);
                                    FileUtils.writeToFile("<?php\r\n" + allItemInfo.toString() + "\r\n);", descFile, "UTF-8");
                                } catch (FileNotFoundException ex) {
                                    showMessageDialogMessage(ex);
                                } catch (IOException ex) {
                                    showMessageDialogMessage(ex);
                                }
                            }
                        });
                        currentThread.start();
                        threadList.add(currentThread);
                        //end single lang
                    }

                    boolean allThreadFinished;
                    do {
                        allThreadFinished = false;
                        try {
                            for (Thread t : threadList) {
                                if (t.getState() != Thread.State.TERMINATED) {
                                    allThreadFinished = false;
                                    break;
                                } else {
                                    allThreadFinished = true;
                                }
                            }
                            Thread.sleep(1000);//停止1s再坚持
                        } catch (InterruptedException ex) {
                            showMessageDialogMessage(ex);
                        }
                    } while (!allThreadFinished);//
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;
                    notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    ConfigParser.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        thread.start();
    }

    /**
     * mission 特例
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     * @param idField
     * @param specialField
     * @param keys
     * @param contentSplitFragment
     * @param combineFields
     */
    public static void transformMissionContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField, final String keys, final String contentSplitFragment, final Map combineFields) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int sheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, sheetName);
                    final String[][] originContent = ExcelParser.parseXls(configFilePath, sheetIndex, true);

                    final Map<String, Map<String, List>> modelInfo = getModel(originContent[0], combineFields);
                    final Map<String, Map<String, List>> fullModelInfo = getModel(originContent[0]);
                    String[] langList = getLangs();

                    for (final String currentLang : langList) {
                        // start single lang 
                        final String[][] singleLangContent = ParseConfigLogic.cleanupOriginContent(originContent, currentLang, fullModelInfo, combineFields);
                        Thread currentThread = transformMissionThread(currentLang, outputPath, fileName, modelInfo, singleLangContent, idField, specialField, keys, contentSplitFragment, combineFields);
                        currentThread.start();
                        threadList.add(currentThread);
                        //end single lang
                    }

                    boolean allThreadFinished;
                    do {
                        allThreadFinished = false;
                        try {
                            for (Thread t : threadList) {
                                if (t.getState() != Thread.State.TERMINATED) {
                                    allThreadFinished = false;
                                    break;
                                } else {
                                    allThreadFinished = true;
                                }
                            }
                            Thread.sleep(1000);//停止1s再执行
                        } catch (InterruptedException ex) {
                            showMessageDialogMessage(ex);
                        }
                    } while (!allThreadFinished);//
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;
//                    bottomStatusjLabel.setText("转换完成。耗时:" + DessertShopConfigParseJFrame.formatTimeDuration(diff));//todo 这个还没有实现
                    ConfigParser.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        }).start();
    }

    /**
     * 甜品店 活动库
     *
     * @param configFilePath
     * @param func
     * @param outputPath
     */
    public static void transformActivityLib(final String configFilePath, String func, final String outputPath) {
        JOptionPane.showMessageDialog(null, "转换开始");
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int sheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, "Worksheet");
                    final String[][] activityLibCfg = ExcelParser.parseXls(configFilePath, sheetIndex, true);

                    final Map<String, Map<String, List<String>>> modelInfo = getModel(activityLibCfg[0]);
                    String[] langList = getLangs();

                    for (final String currentLang : langList) {
                        // start single lang 
                        Thread currentThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int activityIdIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), "id");
                                StringBuilder allActivityInfo = new StringBuilder();
                                allActivityInfo.append(" return array (\r\n");
                                Map<String, String> specialField = new HashMap();
                                String idField = "id";

                                specialField.put("activity_info", "parseActivityInfo");
                                for (int i = 1; i < activityLibCfg.length; i++) {
                                    Map<String, String> singleRowInfo = BuildConfigContent.buildSingleRowStr(activityLibCfg[i], modelInfo, currentLang, activityIdIndex, idField, specialField, null, null, null);
                                    String id = singleRowInfo.get(idField);
                                    String singleItemInfo = singleRowInfo.get("singleRowInfo");
                                    String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, "activityLibraryInfo", "activityLibraryInfo");
                                    try {
                                        FileUtils.writeToFile("<?php\r\n" + singleItemInfo, descFile, "UTF-8");
                                    } catch (FileNotFoundException ex) {
                                        showMessageDialogMessage(ex);
                                    } catch (IOException ex) {
                                        showMessageDialogMessage(ex);
                                    }
                                    if (i == 1) {//第一行 没有必要添加\r\n
                                        allActivityInfo.append(currentAllItemInfo);
                                    } else {
                                        allActivityInfo.append("\r\n").append(currentAllItemInfo);
                                    }
                                    ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / activityLibCfg.length) + "%|正在生成文件:" + outputPath);
                                }
                                try {
                                    ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, "activityLibraryInfo", "activityLibraryInfo");
                                    FileUtils.writeToFile("<?php\r\n" + allActivityInfo.toString(), descFile, "UTF-8");
                                } catch (FileNotFoundException ex) {
                                    showMessageDialogMessage(ex);
                                } catch (IOException ex) {
                                    showMessageDialogMessage(ex);
                                }
                            }
                        });
                        currentThread.start();
                        threadList.add(currentThread);
                        //end single lang
                    }

                    boolean allThreadFinished;
                    do {
                        allThreadFinished = false;
                        try {
                            for (Thread t : threadList) {
                                if (t.getState() != Thread.State.TERMINATED) {
                                    allThreadFinished = false;
                                    break;
                                } else {
                                    allThreadFinished = true;
                                }
                            }
                            Thread.sleep(1000);//停止1s再坚持
                        } catch (InterruptedException ex) {
                            showMessageDialogMessage(ex);
                        }
                    } while (!allThreadFinished);//
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;
                    ConfigParser.notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    ConfigParser.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        thread.start();
    }

    /**
     * 通用
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     * @param idField
     * @param specialField
     */
    public static void transformCommonContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField) {
        final long startTime = System.currentTimeMillis();

        TransformRunable transformRunable = new TransformRunable();
        transformRunable.setConfigFilePath(configFilePath);
        transformRunable.setOutputPath(outputPath);
        transformRunable.setFileName(fileName);
        transformRunable.setSheetName(sheetName);
        transformRunable.setIdField(idField);
        transformRunable.setSpecialField(specialField);
        transformRunable.setStartTime(startTime);

        new Thread(transformRunable).start();
    }

    /**
     * 通用
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     * @param idField
     * @param specialField
     * @param defaultValue
     */
    public static void transformCommonContentEx(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField, final Map defaultValue) {
        final long startTime = System.currentTimeMillis();

        TransformRunable transformRunable = new TransformRunable();
        transformRunable.setConfigFilePath(configFilePath);
        transformRunable.setOutputPath(outputPath);
        transformRunable.setFileName(fileName);
        transformRunable.setSheetName(sheetName);
        transformRunable.setIdField(idField);
        transformRunable.setSpecialField(specialField);
        transformRunable.setStartTime(startTime);
        transformRunable.setDefalutValue(defaultValue);
        new Thread(transformRunable).start();
    }
    
    /**
     * 通用
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     * @param idField
     * @param specialField
     * @param keys
     * @param contentSplitFragment
     * @param defaultValue
     */
    public static void transformCommonContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField, final String keys, final String contentSplitFragment, final Map defaultValue) {
        final long startTime = System.currentTimeMillis();

        TransformRunable transformRunable = new TransformRunable();
        transformRunable.setConfigFilePath(configFilePath);
        transformRunable.setOutputPath(outputPath);
        transformRunable.setFileName(fileName);
        transformRunable.setSheetName(sheetName);
        transformRunable.setIdField(idField);
        transformRunable.setSpecialField(specialField);
        transformRunable.setStartTime(startTime);
        transformRunable.setKeys(keys);
        transformRunable.setDefalutValue(defaultValue);
        transformRunable.setContentSplitFragment(contentSplitFragment);
        new Thread(transformRunable).start();
    }

    /**
     * 通用
     *
     * @param configFilePath
     * @param outputPath
     * @param fileName
     * @param sheetName
     * @param specialField
     */
    public static void transformCommonSingleFileContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final Map specialField) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int sheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, sheetName);
                    final String[][] commonContent = ExcelParser.parseXls(configFilePath, sheetIndex, true);

                    final Map<String, Map<String, List<String>>> modelInfo = getModel(commonContent[0]);
                    String[] langList = getLangs();

                    for (final String currentLang : langList) {
                        // start single lang 
                        Thread currentThread = transformCommonSingleFileThread(currentLang, outputPath, fileName, modelInfo, commonContent, specialField);
                        currentThread.start();
                        threadList.add(currentThread);
                        //end single lang
                    }

                    boolean allThreadFinished;
                    do {
                        allThreadFinished = false;
                        try {
                            for (Thread t : threadList) {
                                if (t.getState() != Thread.State.TERMINATED) {
                                    allThreadFinished = false;
                                    break;
                                } else {
                                    allThreadFinished = true;
                                }
                            }
                            Thread.sleep(1000);//停止1s再执行
                        } catch (InterruptedException ex) {
                            showMessageDialogMessage(ex);
                        }
                    } while (!allThreadFinished);//
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;
                    ConfigParser.notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    ConfigParser.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        }).start();
    }

    private static Thread transformCommonSingleFileThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List<String>>> modelInfo, final String[][] commonContent, final Map specialField) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < commonContent.length; i++) {
                    //String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField, String keys, String contentSplitFragment
                    Map<String, String> singleRowInfo = BuildConfigContent.buildSingleRowStr(commonContent[i], modelInfo, currentLang, -1, null, specialField, null, null, null);
                    String singleItemInfo = singleRowInfo.get("singleRowInfo");
                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, fileName);
                    try {
                        FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                    } catch (FileNotFoundException ex) {
                        showMessageDialogMessage(ex);
                    } catch (IOException ex) {
                        showMessageDialogMessage(ex);
                    }
                    ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                }
            }
        });
        return currentThread;
    }

    public static Thread transformMissionThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List>> modelInfo, final String[][] commonContent, final String idField, final Map specialField, final String keys, final String contentSplitFragment, final Map combineFields) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = BuildConfigContent.buildMissionSingleRowStr(commonContent[i], modelInfo, currentLang, idIndex, idField, specialField, keys, contentSplitFragment, combineFields);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty()) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                        } catch (FileNotFoundException ex) {
                            showMessageDialogMessage(ex);
                        } catch (IOException ex) {
                            showMessageDialogMessage(ex);
                        }
                        if (i == 1) {//第一行 没有必要添加\r\n
                            allContent.append(currentAllItemInfo);
                        } else {
                            allContent.append("\r\n").append(currentAllItemInfo);
                        }
                        ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                    }
                }
                try {
                    ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    FileUtils.writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                } catch (FileNotFoundException ex) {
                    showMessageDialogMessage(ex);
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        return currentThread;
    }

    private static Thread transformCommonThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List<String>>> modelInfo, final String[][] commonContent, final String idField, final Map specialField, final String keys, final String contentSplitFragment, final Map defaultValue) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = BuildConfigContent.buildSingleRowStr(commonContent[i], modelInfo, currentLang, idIndex, idField, specialField, keys, contentSplitFragment, specialField);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty()) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                        } catch (FileNotFoundException ex) {
                            showMessageDialogMessage(ex);
                        } catch (IOException ex) {
                            showMessageDialogMessage(ex);
                        }
                        if (i == 1) {//第一行 没有必要添加\r\n
                            allContent.append(currentAllItemInfo);
                        } else {
                            allContent.append("\r\n").append(currentAllItemInfo);
                        }
                        ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                    }
                }
                try {
                    ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    FileUtils.writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                } catch (FileNotFoundException ex) {
                    showMessageDialogMessage(ex);
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
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

        for (String key : combineFields.keySet()) {
            String[] value = combineFields.get(key);
            int len = value.length;
            for (int i = 0; i < len; i++) {
                needSkipedFields.add(value[i]);
            }
        }

        return needSkipedFields;
    }

    public static Map getModel(String[] dsOpengraphCfg, Map<String, String[]> combineFields) {
        Map<String, List<Integer>> fieldIndex = new HashMap();
        Map<String, List<String>> fieldName = new HashMap();
        String[] langList = getLangs();
        List needSkipedFields = needSkipedFields(combineFields);

        for (int i = 0; i < langList.length; i++) {
            String currentLang = langList[i];
            fieldIndex.put(currentLang, new ArrayList());
            fieldName.put(currentLang, new ArrayList());
        }
        for (int j = 0; j < dsOpengraphCfg.length; j++) {
            String originField = dsOpengraphCfg[j];
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
                    if (!needSkipedFields.contains(originField)) {//需要掉过的field不需要出现在最终的model中
                        for (int i = 0; i < langList.length; i++) {
                            String currentLang = langList[i];
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
     * @param dsOpengraphCfg
     * @return
     */
    public static Map getModel(String[] dsOpengraphCfg) {
        Map<String, List<Integer>> fieldIndex = new HashMap();
        Map<String, List<String>> fieldName = new HashMap();
        String[] langList = getLangs();
        for (int i = 0; i < langList.length; i++) {
            String currentLang = langList[i];
            fieldIndex.put(currentLang, new ArrayList());
            fieldName.put(currentLang, new ArrayList());
        }
        for (int j = 0; j < dsOpengraphCfg.length; j++) {
            String originField = dsOpengraphCfg[j];
            boolean currentPrefixIsLang = false;
            String prefix = "";
            if (!originField.trim().isEmpty()) {
                if (originField.length() >= 6) {
                    prefix = originField.substring(0, 5);
                    currentPrefixIsLang = Arrays.asList(langList).contains(prefix);
                }
                if (currentPrefixIsLang) {//当前field 只属于某一个lang
                    List currentModelIndex = fieldIndex.get(prefix);
                    List currentModelField = fieldName.get(prefix);
                    currentModelIndex.add(j);
                    String currentFiled = originField.substring(6);
                    currentModelField.add(currentFiled);
                } else {//当前field 属于所有lang
                    for (int i = 0; i < langList.length; i++) {
                        String currentLang = langList[i];
                        List currentModelIndex = fieldIndex.get(currentLang);
                        List currentModelField = fieldName.get(currentLang);
                        currentModelIndex.add(j);
                        currentModelField.add(originField);
                    }
                }
            }
        }

        Map finalInfo = new HashMap();
        finalInfo.put("fieldIndex", fieldIndex);
        finalInfo.put("fieldName", fieldName);
        return finalInfo;
    }
}
