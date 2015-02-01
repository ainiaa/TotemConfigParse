package com.coding91.parser;

import com.coding91.logic.BuildConfigLogic;
import com.coding91.logic.TransformConfigLogic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import jxl.read.biff.BiffException;
import com.coding91.utils.ArrayUtils;
import com.coding91.utils.DateTimeUtils;
import com.coding91.utils.ExcelParser;
import com.coding91.utils.FileUtils;

/**
 *
 * @author Administrator
 */
public class ConfigParser {

    /**
     * Creates new form TotemConfigParseJFrame
     */
    public ConfigParser() {
        Map<String, String> configMap = FileUtils.loadSetting("./setting.properties");
        configBaseDir = configMap.get("configMap");
        outputDirectory = configMap.get("outputDirectory");
    }

    private Map<String, String> initFuncList() {
        Map funcList = new HashMap();
        funcList.put("DS_SHOP_OBJ_ITEM", "shopItem.xls");
        funcList.put("ACTIVITY_LIB", "activityLibraryInfo.xls");
        funcList.put("AVATAR_ITEMS", "avatarItems.xls");
        funcList.put("FEED_INFO", "feedInfo.xls");
        funcList.put("GOODS_ORDER", "goodsOrder.xls");
        funcList.put("REQUEST_INFO", "requestInfo.xls");
        funcList.put("GAME", "game.xls");
        funcList.put("MISSION_INFO", "missionInfo.xls");
        funcList.put("BINDING_RECIPE", "bindingRecipe.xls");
        funcList.put("DESSERT_INFO", "dessertInfo.xls");
        return funcList;
    }

    /**
     * 转换单个excel
     *
     * @param func
     * @param configFilePath
     * @param outputPath
     */
    public void transformSingleExcel(String func, String configFilePath, String outputPath) {
        String msg = "";
        if (func.isEmpty()) {
            msg = "请选择解析内容 ";
        } else if (configFilePath.isEmpty()) {
            msg = "请选择待解析文件(xls) ";
        } else if (outputPath.isEmpty()) {
            msg = "请选择输出路径";
        }
        if (!msg.isEmpty()) {
            JOptionPane.showMessageDialog(null, msg, "信息提示", JOptionPane.ERROR_MESSAGE);
        } else {
            String sheetName = "Worksheet";
            String idField;
            String fileName;
            Map specialField = new HashMap();

            if ("DS_SHOP_OBJ_ITEM".equals(func)) {//shopItem
                transformShopObjectItem(configFilePath, func, outputPath);
            } else if ("ACTIVITY_LIB".equals(func)) {//activityLibraryInfo
                specialField.put("activity_info", "parseCommonMultipleWithKeyValue@4");
                specialField.put("unlockRecipe", "parseCommonMultiple@4");
                fileName = "activityLibraryInfo";
                idField = "id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = ",!:";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment);
            } else if ("AVATAR_ITEMS".equals(func)) {//avatarItems
                specialField.put("suite_array", "parseCommonMultiple@3");
                fileName = "avatarItems";
                idField = "item_id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("FEED_INFO".equals(func)) {//feedInfo
                fileName = "feedInfo";
                idField = "feed_id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("GOODS_ORDER".equals(func)) {//goodsOrder
                fileName = "goodsOrder";
                idField = "id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("REQUEST_INFO".equals(func)) {//request_id
                fileName = "requestInfo";
                idField = "request_id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("GAME".equals(func)) {//game
                fileName = "game";
                specialField.put("randomShelfProductList", "parseCommonMultiple@3");
                specialField.put("rankScoreRewards", "parseGameRankScoreRewards@3");
                TransformConfigLogic.transformCommonSingleFileContent(configFilePath, outputPath, fileName, sheetName, specialField);
            } else if ("BINDING_RECIPE".equals(func)) {//bindingRecipe
                specialField.put("recipes", "parseCommonMultiple@4");
                specialField.put("activate_data", "parseBindingRecipeActivateData@3");
                fileName = "bindingRecipe";
                idField = "item_id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = "|!,";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment);
            } else if ("MISSION_INFO".equals(func)) {//mission Info  @todo 还有一写文件没有生成
                specialField.put("reward_data", "parseMissionInfoRewardData@3");
                specialField.put("mission_require", "parseMissionInfoMissionRequire@3");
                fileName = "missionInfo";
                idField = "mission_id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = "|!,";
                Map<String, String[]> combineFields = new HashMap();
                combineFields.put("mission_require", new String[]{"mission_sub_require", "mission_sub_require_desc", "mission_sub_require_tips"});
                TransformConfigLogic.transformMissionContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment, combineFields);
            } else if ("DESSERT_INFO".equals(func)) {//@todo 需要

            }
        }
    }

    public static String[][] itemLangInfoCfg;
    public static String[][] itemBaseInfoCfg;
    public static HashMap<String, HashMap<String, String>> itemIdAndItemName;
    public static HashMap<String, HashMap<String, String>> collectActivityCommonConf;

    public String[][] itemLangInfoCfg(String configFilePath) {
        try {
            int langSheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, "itemLang");
            itemLangInfoCfg = ExcelParser.parseXls(configFilePath, langSheetIndex, true);
        } catch (IOException e) {
            itemLangInfoCfg = new String[0][0];
            showMessageDialogMessage(e);
        } catch (BiffException e) {
            itemLangInfoCfg = new String[0][0];
            showMessageDialogMessage(e);
        }
        return itemLangInfoCfg;
    }

    public String[] getItemLangs(String configFilePath) {
        String langs[];
        try {
            String[][] itemLangCfg = itemLangInfoCfg(configFilePath);
            String[] itemLangModelName = getModelFromStringArray(itemLangCfg);
            langs = ArrayUtils.arraySlice(itemLangModelName, 1);
        } catch (Exception e) {
            langs = new String[0];
            showMessageDialogMessage(e);
        }
        return langs;
    }

    public static String[] itemLangs;

    public String[] getItemLangFileNames(String configFilePath) {
        Map<String, String> fileNameMap = new HashMap<String, String>();
        fileNameMap.put("Chinese", "zh_tw");
        fileNameMap.put("English", "en_us");
        fileNameMap.put("German", "de_de");
        fileNameMap.put("Dutch", "nl_nl");
        fileNameMap.put("French", "fr_fr");
        fileNameMap.put("GermanSe", "de_se");
        fileNameMap.put("DutchSe", "nl_se");
        fileNameMap.put("FrenchSe", "fr_se");
        fileNameMap.put("Japanese", "ja_jp");

        String langs[] = getItemLangs(configFilePath);
        for (int index = 0; index < langs.length; index++) {
            langs[index] = fileNameMap.get(langs[index]);
        }
        itemLangs = langs;
        return langs;
    }

    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }

    public static void showErrorMsg(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, null);
        System.exit(0);
    }

    public static void transformFinish(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static int getFieldIndexByFieldName(List<String> fileNameList, List<Integer> fileIndexList, String fieldName) {
        int finalIndex = -1;
        int len = fileNameList.size();
        for (int i = 0; i < len; i++) {
            if (fileNameList.get(i).equals(fieldName)) {
                finalIndex = fileIndexList.get(i);
                break;
            }
        }
        return finalIndex;
    }

    public static int getFieldIndexByFieldName(List<String> modelInfo, String fieldName) {
        int finalIndex = -1;
        int len = modelInfo.size();
        for (int i = 0; i < len; i++) {
            if (modelInfo.get(i).equals(fieldName)) {
                finalIndex = i;
                break;
            }
        }
        return finalIndex;
    }

    /**
     * 甜品店 shop object item
     *
     * @param configFilePath
     * @param func
     * @param outputPath
     */
    public void transformShopObjectItem(final String configFilePath, String func, final String outputPath) {
        JOptionPane.showMessageDialog(null, "转换开始");
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        final ConfigParser currentUIObject = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int sheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, "Worksheet");
                    final String[][] dsOpengraphCfg = ExcelParser.parseXls(configFilePath, sheetIndex, true);

                    final Map<String, Map<String, List<String>>> modelInfo = TransformConfigLogic.getModel(dsOpengraphCfg[0]);
                    String[] langList = getLangs();

                    for (final String currentLang : langList) {
                        // start single lang 
                        Thread currentThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int itemIdIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), "item_id");
                                StringBuilder allItemInfo = new StringBuilder();
                                allItemInfo.append(" return array (\r\n");
                                for (int i = 1; i < dsOpengraphCfg.length; i++) {
                                    Map<String, String> singleRowInfo = BuildConfigLogic.buildSingleItemStr(dsOpengraphCfg[i], modelInfo, currentLang, itemIdIndex);
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
                                    notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / dsOpengraphCfg.length) + "%|正在生成文件:" + outputPath);
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
                    currentUIObject.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        thread.start();
    }

    public static void notifyMessage(String msg) {
        System.out.println("notifyMessage :" + msg);
    }

    public static String[] getLangs() {
        return new String[]{"zh_tw", "de_de", "fr_fr", "en_us", "es_es"};
    }

    public static String getInitials(String s) {
        return getInitials(s, true);
    }

    public static String getInitials(String s, boolean withFirstLetter) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        s = sb.toString();
        sb = new StringBuilder();

        if (withFirstLetter) {
            sb.append(s.charAt(0));
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    public String[] getModelFromStringArray(String[][] content) {
        String[] model;
        int i = 0;
        for (; i < content[0].length; i++) {
            if (content[0][i].isEmpty()) {
                break;
            }
        }
        model = new String[i];
        System.arraycopy(content[0], 0, model, 0, i);
        return model;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println(" ==========");
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    public static void showMessageDialogMessage(Exception ex) {
        String exMsg = ex.toString();
        JOptionPane.showMessageDialog(null, exMsg + new Throwable().getStackTrace()[1].toString(), "错误信息提示", JOptionPane.ERROR_MESSAGE);
    }

    private String configBaseDir;
    private String outputDirectory;
}
