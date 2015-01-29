package com.coding91.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.swing.JOptionPane;
import jxl.read.biff.BiffException;
import com.coding91.utils.ArrayUtils;
import com.coding91.utils.DateTimeUtils;
import com.coding91.utils.ExcelParser;

/**
 *
 * @author Administrator
 */
public class ConfigParser {

    /**
     * Creates new form TotemConfigParseJFrame
     */
    public ConfigParser() {
        loadSetting("./setting.properties");
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
                transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment);
            } else if ("AVATAR_ITEMS".equals(func)) {//avatarItems
                specialField.put("suite_array", "parseCommonMultiple@3");
                fileName = "avatarItems";
                idField = "item_id";
                transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("FEED_INFO".equals(func)) {//feedInfo
                fileName = "feedInfo";
                idField = "feed_id";
                transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("GOODS_ORDER".equals(func)) {//goodsOrder
                fileName = "goodsOrder";
                idField = "id";
                transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("REQUEST_INFO".equals(func)) {//request_id
                fileName = "requestInfo";
                idField = "request_id";
                transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("GAME".equals(func)) {//game
                fileName = "game";
                specialField.put("randomShelfProductList", "parseCommonMultiple@3");
                specialField.put("rankScoreRewards", "parseGameRankScoreRewards@3");
                transformCommonSingleFileContent(configFilePath, outputPath, fileName, sheetName, specialField);
            } else if ("BINDING_RECIPE".equals(func)) {//bindingRecipe
                specialField.put("recipes", "parseCommonMultiple@4");
                specialField.put("activate_data", "parseBindingRecipeActivateData@3");
                fileName = "bindingRecipe";
                idField = "item_id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = "|!,";
                transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment);
            } else if ("MISSION_INFO".equals(func)) {//mission Info  @todo 还有一写文件没有生成
                specialField.put("reward_data", "parseMissionInfoRewardData@3");
                specialField.put("mission_require", "parseMissionInfoMissionRequire@3");
                fileName = "missionInfo";
                idField = "mission_id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = "|!,";
                Map<String, String[]> combineFields = new HashMap();
                combineFields.put("mission_require", new String[]{"mission_sub_require", "mission_sub_require_desc", "mission_sub_require_tips"});
                transformMissionContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment, combineFields);
            } else if ("DESSERT_INFO".equals(func)) {//@todo 需要

            }
        }
    }

    public static String[][] itemLangInfoCfg;
    public static String[][] itemBaseInfoCfg;
    public static HashMap<String, HashMap<String, String>> itemIdAndItemName;
    public static HashMap<String, HashMap<String, String>> collectActivityCommonConf;

    public String getItemName(String itemId, String lang) {
        itemIdAndItemName = buildItemIdAndItemName();
        return itemIdAndItemName.get(itemId).get(lang);
    }

    public HashMap buildItemIdAndItemName() {
        HashMap finalInfo = new HashMap();
        HashMap itemNameInfo;
        int rows = itemLangInfoCfg.length;
        for (int rowNum = 1; rowNum < rows; rowNum++) {
            String[] itemNameInfoArray = ArrayUtils.arraySlice(itemLangInfoCfg[rowNum], 1);
            itemNameInfo = ArrayUtils.arrayCombine(itemLangs, itemNameInfoArray);
            finalInfo.put(itemLangInfoCfg[rowNum][0], itemNameInfo);
        }
        return finalInfo;
    }

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

    public static void writeToFile(String contents, String descFile, String encoding) throws FileNotFoundException, IOException {
        File fileOutput = new File(descFile);
        writeToFile(contents, fileOutput, "UTF-8");
    }

    public Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, Map<String, String> specialField) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        currentFieldIndexList = null;
        currentFieldNameList = null;
        StringBuilder singleRowStringbuffer = new StringBuilder();
        singleRowStringbuffer.append("array (").append("\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (specialField.containsKey(currentField)) {
                try {
                    String parseFieldFunctionName = specialField.get(currentField);
                    Method parseField = this.getClass().getDeclaredMethod(parseFieldFunctionName, new Class[]{String.class, String.class, String.class});//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                } catch (IllegalAccessException ex) {
                    System.out.println(ex.getMessage());
                } catch (SecurityException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.out.println(ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    "));
            }
        }

        singleRowStringbuffer.append(");");

        Map finalInfo = new HashMap();
        finalInfo.put("singleRowInfo", singleRowStringbuffer.toString());
        return finalInfo;
    }

    public Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, Map<String, String> specialField, String keys) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        currentFieldIndexList = null;
        currentFieldNameList = null;
        StringBuilder singleRowStringbuffer = new StringBuilder();
        singleRowStringbuffer.append("array (").append("\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (specialField.containsKey(currentField)) {
                try {
                    String parseFieldFunctionName = specialField.get(currentField);
                    Method parseField = this.getClass().getDeclaredMethod(parseFieldFunctionName, new Class[]{String.class, String.class, String.class});//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                } catch (IllegalAccessException ex) {
                    System.out.println(ex.getMessage());
                } catch (SecurityException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.out.println(ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    "));
            }
        }

        singleRowStringbuffer.append(");");

        Map finalInfo = new HashMap();
        finalInfo.put("singleRowInfo", singleRowStringbuffer.toString());
        return finalInfo;
    }

    public Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        currentFieldIndexList = null;
        currentFieldNameList = null;
        StringBuilder singleRowStringbuffer = new StringBuilder();
        StringBuilder allRowsStringbuffer = new StringBuilder();
        String id = singleRowInfoContent[idIndex];
        singleRowStringbuffer.append("array (").append("\r\n");
        allRowsStringbuffer.append("  ").append(id).append(" => \r\n").append("  array (\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (currentField.equals(idField)) {
                id = currentFieldContent;
            }
            if (specialField.containsKey(currentField)) {
                try {
                    String[] parseFieldFunctionInfo = specialField.get(currentField).split("@");
                    String parseFieldFunctionName = parseFieldFunctionInfo[0];
                    int paramCount;
                    if (parseFieldFunctionInfo.length == 1) {
                        paramCount = 3;
                    } else {
                        paramCount = Integer.valueOf(parseFieldFunctionInfo[1]);
                    }

                    List<Class> paramType = new ArrayList();
                    for (int k = 0; k < paramCount; k++) {
                        paramType.add(String.class);
                    }
                    Class clazz[] = paramType.toArray(new Class[]{});
                    Method parseField = this.getClass().getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                    allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "  "}));//@todo 精彩的写法
                } catch (IllegalAccessException ex) {
                    System.out.println(ex.getMessage());
                } catch (SecurityException ex) {
                    System.out.println(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.out.println(ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    "));
                allRowsStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "  "));
            }
        }

        singleRowStringbuffer.append(");");
        allRowsStringbuffer.append("  ),");

        Map finalInfo = new HashMap();
        finalInfo.put(idField, id);
        finalInfo.put("singleRowInfo", singleRowStringbuffer.toString());
        finalInfo.put("allRowsInfo", allRowsStringbuffer.toString());
        return finalInfo;
    }

    public Map buildMissionSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField, String keys, String contentSplitFragment, Map combineFields) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        currentFieldIndexList = null;
        currentFieldNameList = null;
        StringBuilder singleRowStringbuffer = new StringBuilder();
        StringBuilder allRowsStringbuffer = new StringBuilder();
        String id = singleRowInfoContent[idIndex];
        singleRowStringbuffer.append("array (").append("\r\n");
        allRowsStringbuffer.append("  '").append(id).append("' => \r\n").append("  array (\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (currentField.equals(idField)) {
                id = currentFieldContent;
            }
            if (specialField.containsKey(currentField)) {
                try {
                    String[] parseFieldFunctionInfo = specialField.get(currentField).split("@");
                    String parseFieldFunctionName = parseFieldFunctionInfo[0];
                    int paramCount;
                    if (parseFieldFunctionInfo.length == 1) {
                        paramCount = 4;
                    } else {
                        paramCount = Integer.valueOf(parseFieldFunctionInfo[1]);
                    }

                    List<Class> paramType = new ArrayList();
                    for (int k = 0; k < paramCount; k++) {
                        paramType.add(String.class);
                    }
                    Class clazz[] = paramType.toArray(new Class[]{});
                    Method parseField = this.getClass().getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    //@todo 这一部分可以重构成动态参数的方式 以后在修改
                    if (paramType.size() == 3) {
                        singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "  "}));//@todo 精彩的写法
                    } else if (paramType.size() == 4) {
                        singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    ", contentSplitFragment}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "  ", contentSplitFragment}));//@todo 精彩的写法
                    } else if (paramType.size() == 5) {
                        singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, keys, currentFieldContent, "    ", contentSplitFragment}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, keys, currentFieldContent, "  ", contentSplitFragment}));//@todo 精彩的写法
                    }

                } catch (IllegalAccessException ex) {
                    System.out.println("IllegalAccessException:" + ex.getMessage());
                } catch (SecurityException ex) {
                    System.out.println("SecurityException:" + ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    System.out.println("IllegalArgumentException:" + ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.out.println("InvocationTargetException:" + ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    System.out.println("NoSuchMethodException:" + ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    "));
                allRowsStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "  "));
            }
        }

        singleRowStringbuffer.append(");");
        allRowsStringbuffer.append("  ),");

        Map finalInfo = new HashMap();
        finalInfo.put(idField, id);
        finalInfo.put("singleRowInfo", singleRowStringbuffer.toString());
        finalInfo.put("allRowsInfo", allRowsStringbuffer.toString());
        return finalInfo;
    }

    public Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField, String keys, String contentSplitFragment) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        currentFieldIndexList = null;
        currentFieldNameList = null;
        StringBuilder singleRowStringbuffer = new StringBuilder();
        StringBuilder allRowsStringbuffer = new StringBuilder();
        String id = singleRowInfoContent[idIndex];
        singleRowStringbuffer.append("array (").append("\r\n");
        allRowsStringbuffer.append("  ").append(id).append(" => \r\n").append("  array (\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (currentField.equals(idField)) {
                id = currentFieldContent;
            }
            if (specialField.containsKey(currentField)) {
                try {
                    String[] parseFieldFunctionInfo = specialField.get(currentField).split("@");
                    String parseFieldFunctionName = parseFieldFunctionInfo[0];
                    int paramCount;
                    if (parseFieldFunctionInfo.length == 1) {
                        paramCount = 4;
                    } else {
                        paramCount = Integer.valueOf(parseFieldFunctionInfo[1]);
                    }

                    List<Class> paramType = new ArrayList();
                    for (int k = 0; k < paramCount; k++) {
                        paramType.add(String.class);
                    }
                    Class clazz[] = paramType.toArray(new Class[]{});
                    Method parseField = this.getClass().getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    //@todo 这一部分可以重构成动态参数的方式 以后在修改
                    if (paramType.size() == 3) {
                        singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "  "}));//@todo 精彩的写法
                    } else if (paramType.size() == 4) {
                        singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "    ", contentSplitFragment}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, currentFieldContent, "  ", contentSplitFragment}));//@todo 精彩的写法
                    } else if (paramType.size() == 5) {
                        singleRowStringbuffer.append(parseField.invoke(this, new Object[]{currentField, keys, currentFieldContent, "    ", contentSplitFragment}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(this, new Object[]{currentField, keys, currentFieldContent, "  ", contentSplitFragment}));//@todo 精彩的写法
                    }

                } catch (IllegalAccessException ex) {
                    System.out.println("IllegalAccessException:" + ex.getMessage());
                } catch (SecurityException ex) {
                    System.out.println("SecurityException:" + ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    System.out.println("IllegalArgumentException:" + ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.out.println("InvocationTargetException:" + ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    System.out.println("NoSuchMethodException:" + ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    "));
                allRowsStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "  "));
            }
        }

        singleRowStringbuffer.append(");");
        allRowsStringbuffer.append("  ),");

        Map finalInfo = new HashMap();
        finalInfo.put(idField, id);
        finalInfo.put("singleRowInfo", singleRowStringbuffer.toString());
        finalInfo.put("allRowsInfo", allRowsStringbuffer.toString());
        return finalInfo;
    }

    private String parseActivityInfo(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentArray = content.split(",");
        StringBuilder tmpContent = new StringBuilder();
        int index = 0;
        String tpl = leadingString + "%d => \n"
                + leadingString + "array (\n"
                + leadingString + "  'activity_type' => '%d',\n"
                + leadingString + "  'activity_id' => '%d',\n"
                + leadingString + "),";
        for (String currentContent : contentArray) {
            String[] contentFactor = currentContent.split(":");
            if (contentFactor.length == 2) {
                tmpContent.append(String.format(tpl, index++, contentFactor[0], contentFactor[1]));
            }
        }
        return tmpContent.toString();
    }

    /**
     * 将 "a","b","c" 转换为 array( "a","b","c")
     *
     * @param field
     * @param content
     * @param leadingString
     * @return
     */
    private String parseCommonMultiple(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentArray = content.split(",");
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        if (!content.isEmpty()) {
            tmpContent.append("array(");
            int index = 0;
            for (String currentContent : contentArray) {
                tmpContent.append(index++).append(" => '").append(currentContent).append("',");
            }
            tmpContent.append(")");
        } else {
            tmpContent.append("NULL");
        }
        tmpContent.append(",\r\n");

        return tmpContent.toString();
    }

    /**
     * 将 "a","b","c" 转换为 array( "a","b","c")
     *
     * @param field
     * @param content
     * 161:49501:4:0,162:49502:4:0,215:50501:5:1,218:50502:5:1|163:49503:4:0,164:49504:4:0,221:50503:5:1,224:50504:5:1|165:49505:4:0,166:49506:4:0,227:50505:5:1,230:50506:5:1
     * @param leadingString
     * @return
     */
    private String parseBindingRecipeActivateData(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentArray = content.split("\\|");//第一层
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        if (!content.isEmpty()) {
            tmpContent.append("array(\r\n");
            int firstFoolrIndex = 0;
            for (String currentContent : contentArray) {
                int secondFoolrIndex = 0;
                if (!currentContent.isEmpty()) {
                    String[] currentContentArray = currentContent.split(",");//第二层
                    tmpContent.append(leadingString).append(leadingString).append(firstFoolrIndex).append(" => array(\r\n");
                    for (String currentItem : currentContentArray) {
                        if (!currentItem.isEmpty()) {
                            String[] singleFactors = currentItem.split(":");//第三层
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(secondFoolrIndex).append(" => array(\r\n");//),\r\n
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(leadingString).append(leadingString).append("'activate_id' => '").append(singleFactors[0]).append("',\r\n");
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(leadingString).append(leadingString).append("'activate_item_id' => '").append(singleFactors[1]).append("',\r\n");
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(leadingString).append(leadingString).append("'activate_num' => '").append(singleFactors[2]).append("',\r\n");
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(leadingString).append(leadingString).append("'activate_type' => '").append(singleFactors[3]).append("',\r\n");
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append("),\r\n");//
                        } else {//第二层
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(secondFoolrIndex).append(" => array(),\r\n");
                        }
                        secondFoolrIndex++;
                    }
                    tmpContent.append(leadingString).append(leadingString).append("),\r\n");
                } else {
                    tmpContent.append(leadingString).append(leadingString).append(firstFoolrIndex).append(" => array(),\r\n");
                }
                firstFoolrIndex++;
            }
            tmpContent.append(")");
        } else {
            tmpContent.append("NULL");
        }
        tmpContent.append(",\r\n");

        return tmpContent.toString();
    }

    private String parseMissionInfoRewardData(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentArray = content.split(",");//第一层
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        String leadingString2 = leadingString + leadingString;
        String leadingString3 = leadingString + leadingString + leadingString;
        String secondFloorContentFormat = "%s => array (\r\n"
                + "%s'reward_index' => '%s',\r\n"
                + "%s'reward_item_type' => '%s',\r\n"
                + "%s'reward_item_id' => '%s',\r\n"
                + "%s'reward_item_num' => '%s',\r\n"
                + "%s),\r\n";
        if (!content.isEmpty()) {
            tmpContent.append("array(\r\n");
            int firstFoolrIndex = 0;
            for (String currentFirstFloorContent : contentArray) {
                if (!currentFirstFloorContent.isEmpty()) {
                    String[] currentSecondFloorContentArray = currentFirstFloorContent.split(":");//第二层
                    tmpContent.append(leadingString).append(leadingString).append(firstFoolrIndex).append(String.format(secondFloorContentFormat, leadingString2, leadingString3, currentSecondFloorContentArray[0], leadingString3, currentSecondFloorContentArray[1], leadingString3, currentSecondFloorContentArray[2], leadingString3, currentSecondFloorContentArray[3], leadingString2));
                } else {
                    tmpContent.append(leadingString).append(leadingString).append(firstFoolrIndex).append(" => array(),\r\n");
                }
                firstFoolrIndex++;
            }
            tmpContent.append(")");
        } else {
            tmpContent.append("NULL");
        }
        tmpContent.append(",\r\n");

        return tmpContent.toString();
    }

    private String parseMissionInfoMissionRequire(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31   //fck 这个太恶心了，  content 只有9项内容，其他的内容再另一个content中，且 还需要进行分割。。。 坑爹啊。。
        String[] contentArray = content.split(MISSION_REQUIRE_FIRST_FLOOR);//第一层
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        String leadingString2 = leadingString + leadingString;
        String leadingString3 = leadingString + leadingString + leadingString;
        String secondFloorContentFormat = "%s%d => array (\r\n"
                + "%s'require_index' => '%s',\r\n"
                + "%s'mission_require_icon' => '%s',\r\n"
                + "%s'mission_require_type' => '%s',\r\n"
                + "%s'require_item_id' => '%s',\r\n"
                + "%s'require_item_num' => '%s',\r\n"
                + "%s'require_type' => '%s',\r\n"
                + "%s'require_id' => '%s',\r\n"
                + "%s'is_skippable' => '%s',\r\n"
                + "%s'require_skip_cash' => '%s',\r\n"
                + "%s'mission_sub_require' => '%s',\r\n"
                + "%s'mission_sub_require_desc' => '%s',\r\n"
                + "%s'mission_sub_require_tips' => '%s',\r\n"
                + "%s),\r\n";
        if (!content.isEmpty()) {
            tmpContent.append("array(\r\n");
            int firstFoolrIndex = 0;
            for (String currentFirstFloorContent : contentArray) {
                if (!currentFirstFloorContent.isEmpty()) {
                    String[] currentSecondFloorContentArray = currentFirstFloorContent.split(MISSION_REQUIRE_SECOND_FLOOR);//第二层
                    tmpContent.append(leadingString).append(leadingString)
                            .append(String.format(secondFloorContentFormat, leadingString2, firstFoolrIndex,
                                            leadingString3, currentSecondFloorContentArray[0],
                                            leadingString3, currentSecondFloorContentArray[1],
                                            leadingString3, currentSecondFloorContentArray[2],
                                            leadingString3, currentSecondFloorContentArray[3],
                                            leadingString3, currentSecondFloorContentArray[4],
                                            leadingString3, currentSecondFloorContentArray[5],
                                            leadingString3, currentSecondFloorContentArray[6],
                                            leadingString3, currentSecondFloorContentArray[7],
                                            leadingString3, currentSecondFloorContentArray[8],
                                            leadingString3, currentSecondFloorContentArray[9],
                                            leadingString3, currentSecondFloorContentArray[10],
                                            leadingString3, currentSecondFloorContentArray[11],
                                            leadingString2));
                } else {
                    tmpContent.append(leadingString).append(leadingString).append(firstFoolrIndex).append(" => array(),\r\n");
                }
                firstFoolrIndex++;
            }
            tmpContent.append(")");
        } else {
            tmpContent.append("NULL");
        }
        tmpContent.append(",\r\n");

        return tmpContent.toString();
    }
    private static final String MISSION_REQUIRE_SECOND_FLOOR = "!@@@@!";
    private static final String MISSION_REQUIRE_FIRST_FLOOR = "-##-";

    /**
     * 将 "a","b","c" 转换为 array( "a","b","c")
     *
     * @param field
     * @param content
     * @param leadingString
     * @return
     */
    private String parseCommonMultiple(String field, String content, String leadingString, String contentSplitFragment) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentSplitFragmentArray = contentSplitFragment.split("!");
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => array(\r\n");
        //分隔内容
        String firstSplitFragment, secondSplitFragment, thirdSplitFragment, fourthSplitFragment;
        String[] firstContentArray, secondContentArray, thirdContentArray, fourthContentArray;
        int firstIndex = 0, secondIndex = 0, thirdIndex = 0, fourthIndex = 0;
        switch (contentSplitFragmentArray.length) {
            case 1:
                firstSplitFragment = contentSplitFragmentArray[0];
                firstContentArray = content.split("\\" + firstSplitFragment);
                for (String firstContentFragement : firstContentArray) {
                    tmpContent.append(leadingString).append(leadingString).append(firstIndex++).append(" => '").append(firstContentFragement).append("',\r\n");
                }
                break;
            case 2:
                firstSplitFragment = contentSplitFragmentArray[0];
                secondSplitFragment = contentSplitFragmentArray[1];
                firstContentArray = content.split("\\" + firstSplitFragment);
                for (String firstContentFragement : firstContentArray) {
                    secondIndex = 0;
                    thirdIndex = 0;
                    fourthIndex = 0;
                    if (!firstContentFragement.isEmpty()) {
                        tmpContent.append(leadingString).append(leadingString).append(firstIndex++).append(" => array(\r\n");
                        secondContentArray = firstContentFragement.split("\\" + secondSplitFragment);
                        for (String secondContentFragement : secondContentArray) {
                            tmpContent.append(leadingString).append(leadingString).append(leadingString).append(secondIndex++).append(" => '").append(secondContentFragement).append("',\r\n");
                        }
                        tmpContent.append(leadingString).append(leadingString).append("),\r\n");
                    } else {
                        tmpContent.append(leadingString).append(leadingString).append(firstIndex++).append(" => array(),\r\n");
                    }
                }
                break;
            case 3:
                firstSplitFragment = contentSplitFragmentArray[0];
                secondSplitFragment = contentSplitFragmentArray[1];
                thirdSplitFragment = contentSplitFragmentArray[2];
                firstContentArray = content.split("\\" + firstSplitFragment);
                for (String firstContentFragement : firstContentArray) {
                    secondIndex = 0;
                    thirdIndex = 0;
                    fourthIndex = 0;
                    if (!firstContentFragement.isEmpty()) {
                        tmpContent.append(leadingString).append(leadingString).append(firstIndex++).append(" => array(\r\n");
                        secondContentArray = firstContentFragement.split("\\" + secondSplitFragment);
                        for (String secondContentFragement : secondContentArray) {
                            if (!secondContentFragement.isEmpty()) {
                                tmpContent.append(leadingString).append(leadingString).append(leadingString).append(secondIndex++).append(" => array(\r\n");
                                thirdContentArray = secondContentFragement.split("\\" + thirdSplitFragment);
                                for (String thirdContentFragement : thirdContentArray) {
                                    tmpContent.append(leadingString).append(leadingString).append(leadingString).append(leadingString).append(thirdIndex++).append(" => '").append(thirdContentFragement).append("',\r\n");
                                }
                                tmpContent.append(leadingString).append(leadingString).append(leadingString).append("),\r\n");
                            } else {
                                tmpContent.append(leadingString).append(leadingString).append(leadingString).append(secondIndex++).append(" => array(),\r\n");
                            }
                            //tmpContent.append(leadingString).append(leadingString).append(leadingString).append(secondIndex++).append(" => '").append(secondContentFragement).append("',\r\n");
                        }
                        tmpContent.append(leadingString).append(leadingString).append("),\r\n");
                    } else {
                        tmpContent.append(leadingString).append(leadingString).append(firstIndex++).append(" => array(),\r\n");
                    }
                }
                break;
            case 4:
                break;
            default://这么负载
        }

        tmpContent.append(leadingString).append("),\r\n");
        return tmpContent.toString();
    }

    private String parseGameRankScoreRewards(String field, String content, String leadingString) {

        String rewardStringFormat = "array('itemId' => %s, 'itemNum' => %s),";
        //500|1:5,56004:50,56001:5000;
        String[] singleRankScore = content.split(";");
        String test = leadingString + "'" + field + "' => array(\r\n";
        for (int i = 0; i < singleRankScore.length; i++) {
            String currentSingleRankScore = singleRankScore[i];
            if (!currentSingleRankScore.isEmpty()) {
                String[] singleRankScoreItem = currentSingleRankScore.split("\\|");
                test += leadingString + leadingString + "array(\r\n";
                test += leadingString + leadingString + leadingString + "'score' => " + singleRankScoreItem[0] + ",\r\n";
                test += leadingString + leadingString + leadingString + "'reward' => array(\r\n";
                String currentSingleRankScoreItem = singleRankScoreItem[1];
                if (!currentSingleRankScoreItem.isEmpty()) {
                    String[] singleRankScoreItemReward = currentSingleRankScoreItem.split(",");
                    for (int j = 0; j < singleRankScoreItemReward.length; j++) {
                        String currentSingleRankScoreItemReward = singleRankScoreItemReward[j];
                        if (!currentSingleRankScoreItemReward.isEmpty()) {
                            String[] singleRankScoreItemRewardItem = currentSingleRankScoreItemReward.split(":");
                            test += leadingString + leadingString + leadingString + leadingString + String.format(rewardStringFormat, singleRankScoreItemRewardItem[0], singleRankScoreItemRewardItem[1]) + "\r\n";
                        }
                    }
                }
            }
            test += leadingString + leadingString + leadingString + "),\r\n";
            test += leadingString + "),\r\n";
        }

        test += leadingString + "),";
        return test;
    }

    /**
     * 将 1:12,2:22,3:33 转换为 array ( 0 => array('key' => 1,'value' => 12), 1 =>
     * array('key' => 2,'value' => 22), 2 => array('key' => 3,'value' => 33), )
     *
     * @param field
     * @param content
     * @param leadingString
     * @return
     */
    private String parseCommonMultipleWithKeyValue(String field, String keys, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentArray = content.split(",");
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        if (!content.isEmpty()) {
            tmpContent.append("array(");
            String[] keyArray = keys.split(",");
            for (String currentContent : contentArray) {
                String[] contentFactor = currentContent.split(":");
                tmpContent.append(leadingString).append(leadingString).append("array(");
                if (contentFactor.length == keyArray.length) {
                    for (int i = 0; i < keyArray.length; i++) {
                        tmpContent.append(String.format("'%s' => '%s',", keyArray[i], contentFactor[i]));
                    }
                }
                tmpContent.append("),");
            }
            tmpContent.append(")");
        }
        tmpContent.append(",\r\n");
        return tmpContent.toString();
    }

    public String commonSingleFieldString(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n
        String contentFormat = "'%s' => '%s',\r\n";
        return leadingString + String.format(contentFormat, field, content);
    }

    public Map buildSingleItemStr(String[] itemBaseInfoContent, Map modelInfo, String lang, int itemIdIndex) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        currentFieldIndexList = null;
        currentFieldNameList = null;
        StringBuilder singleItemStringbuffer = new StringBuilder();
        StringBuilder allItemsStringbuffer = new StringBuilder();
        String itemId = itemBaseInfoContent[itemIdIndex];
        singleItemStringbuffer.append("array (").append("\r\n");
        allItemsStringbuffer.append("  ").append(itemId).append(" => \r\n").append("  array (\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = itemBaseInfoContent[currentIndex];
            if (currentField.equals("item_id")) {
                itemId = currentFieldContent;
            }
            currentFieldContent = currentFieldContent.replaceAll("<br>", "\r\n");//将<br>替换为\r\n
            singleItemStringbuffer.append("  '");
            singleItemStringbuffer.append(currentField);
            singleItemStringbuffer.append("'");
            singleItemStringbuffer.append("=>");
            singleItemStringbuffer.append("'");
            singleItemStringbuffer.append(currentFieldContent);
            singleItemStringbuffer.append("'");
            singleItemStringbuffer.append(",");
            singleItemStringbuffer.append("\r\n");

            allItemsStringbuffer.append("    '");
            allItemsStringbuffer.append(currentField);
            allItemsStringbuffer.append("'");
            allItemsStringbuffer.append("=>");
            allItemsStringbuffer.append("'");
            allItemsStringbuffer.append(currentFieldContent);
            allItemsStringbuffer.append("'");
            allItemsStringbuffer.append(",");
            allItemsStringbuffer.append("\r\n");
        }

        singleItemStringbuffer.append(");");
        allItemsStringbuffer.append("  ),");

        Map finalInfo = new HashMap();
        finalInfo.put("itemId", itemId);
        finalInfo.put("singleItemInfo", singleItemStringbuffer.toString());
        finalInfo.put("allItemInfo", allItemsStringbuffer.toString());
        return finalInfo;
    }

    public String buildSingleRowStoredPath(String lang, String itemId, String outputPath, String dirName, String fileName) {
        return outputPath + "/" + lang + "/" + dirName + "/" + fileName + itemId + ".php";
    }

    public String buildSingleRowStoredPath(String lang, String itemId, String outputPath, String fileName) {
        return outputPath + "/" + lang + "/" + fileName + itemId + ".php";
    }

    public String buildSingleItemStoredPath(String lang, String itemId, String outputPath) {
        return outputPath + "/" + lang + "/objItem/objItem" + itemId + ".php";
    }

    public void transformFinish(String message) {
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

                    final Map<String, Map<String, List<String>>> modelInfo = getModel(dsOpengraphCfg[0]);
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
                                    Map<String, String> singleRowInfo = buildSingleItemStr(dsOpengraphCfg[i], modelInfo, currentLang, itemIdIndex);
                                    String itemId = singleRowInfo.get("itemId");
                                    String singleItemInfo = singleRowInfo.get("singleItemInfo");
                                    String currentAllItemInfo = singleRowInfo.get("allItemInfo");
                                    String descFile = buildSingleItemStoredPath(currentLang, itemId, outputPath);
                                    try {
                                        writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
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
                                    String descFile = buildSingleItemStoredPath(currentLang, "", outputPath);
                                    writeToFile("<?php\r\n" + allItemInfo.toString() + "\r\n);", descFile, "UTF-8");
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

    /**
     * 甜品店 活动库
     *
     * @param configFilePath
     * @param func
     * @param outputPath
     */
    public void transformActivityLib(final String configFilePath, String func, final String outputPath) {
        JOptionPane.showMessageDialog(null, "转换开始");
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        final ConfigParser currentUIObject = this;
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
                                    Map<String, String> singleRowInfo = buildSingleRowStr(activityLibCfg[i], modelInfo, currentLang, activityIdIndex, idField, specialField);
                                    String id = singleRowInfo.get(idField);
                                    String singleItemInfo = singleRowInfo.get("singleRowInfo");
                                    String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                                    String descFile = buildSingleRowStoredPath(currentLang, id, outputPath, "activityLibraryInfo", "activityLibraryInfo");
                                    try {
                                        writeToFile("<?php\r\n" + singleItemInfo, descFile, "UTF-8");
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
                                    notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / activityLibCfg.length) + "%|正在生成文件:" + outputPath);
                                }
                                try {
                                    notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                                    String descFile = buildSingleRowStoredPath(currentLang, "", outputPath, "activityLibraryInfo", "activityLibraryInfo");
                                    writeToFile("<?php\r\n" + allActivityInfo.toString(), descFile, "UTF-8");
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

    public void notifyMessage(String msg) {
        System.out.println("notifyMessage :" + msg);
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
    public void transformCommonContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        final ConfigParser currentUIObject = this;

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
                        Thread currentThread = transformCommonThread(currentLang, outputPath, fileName, modelInfo, commonContent, idField, specialField);
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
                    notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    currentUIObject.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        }).start();
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
    public void transformMissionContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField, final String keys, final String contentSplitFragment, final Map combineFields) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        final ConfigParser currentUIObject = this;

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
                        final String[][] singleLangContent = cleanupOriginContent(originContent, currentLang, fullModelInfo, combineFields);
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
                    notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    currentUIObject.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        }).start();
    }

    public String[][] cleanupOriginContent(final String[][] originContent, String currentLang, Map<String, Map<String, List>> modelInfo, Map<String, String[]> combineFields) {
        String[][] finalContent = new String[originContent.length][originContent[0].length];
//        System.arraycopy(originContent, 0, finalContent, 0, originContent.length);//这个会影响到 originContent 这个数组。。。。
        for (int i = 0; i < originContent.length; i++) {
            System.arraycopy(originContent[i], 0, finalContent[i], 0, originContent[i].length);
        }
        List<String> fileNameInModelInfoList = modelInfo.get("fieldName").get(currentLang);
        List<Integer> fileIndexInModelInfoList = modelInfo.get("fieldIndex").get(currentLang);
        Map<Integer, Integer[]> finalIndexMap = new HashMap();
        for (Entry<String, String[]> entry : combineFields.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            int valueLen = value.length;
            int keyIndex = getFieldIndexByFieldName(fileNameInModelInfoList, key);
            List<Integer> current = new ArrayList();
            for (int i = 0; i < valueLen; i++) {
                int currentValueIndex = getFieldIndexByFieldName(fileNameInModelInfoList, fileIndexInModelInfoList, value[i]);
                current.add(currentValueIndex);
            }
            finalIndexMap.put(keyIndex, current.toArray(new Integer[current.size()]));
        }
        int rowNum = finalContent.length;
        for (int rowCount = 1; rowCount < rowNum; rowCount++) {
            int columnNum = finalContent[rowCount].length;
            for (int columnCount = 0; columnCount < columnNum; columnCount++) {
                if (finalIndexMap.containsKey(columnCount)) {
                    String originColumnContent = finalContent[rowCount][columnCount];
                    String[] originColumnContentArray = originColumnContent.split(",");
                    for (int i = 0; i < originColumnContentArray.length; i++) {
                        originColumnContentArray[i] = originColumnContentArray[i].replaceAll(":", MISSION_REQUIRE_SECOND_FLOOR);
                    }
                    int originColumnContentNum = originColumnContentArray.length;
                    Integer[] combineColumnArray = finalIndexMap.get(columnCount);
                    int combineColumnNum = combineColumnArray.length;
                    for (int combineColumnIndex = 0; combineColumnIndex < combineColumnNum; combineColumnIndex++) {
                        String[] currentCombineColumnContentArray = finalContent[rowCount][combineColumnArray[combineColumnIndex]].split("\\|");
                        if (currentCombineColumnContentArray.length == originColumnContentArray.length) {//有的时候 数据会不一致  直接无视 艹 坑爹玩意
                            for (int originColumnContentIndex = 0; originColumnContentIndex < originColumnContentNum; originColumnContentIndex++) {
                                originColumnContentArray[originColumnContentIndex] = originColumnContentArray[originColumnContentIndex] + MISSION_REQUIRE_SECOND_FLOOR + currentCombineColumnContentArray[originColumnContentIndex];
                            }
                        } else {
                            for (int originColumnContentIndex = 0; originColumnContentIndex < originColumnContentNum; originColumnContentIndex++) {
                                originColumnContentArray[originColumnContentIndex] = originColumnContentArray[originColumnContentIndex] + MISSION_REQUIRE_SECOND_FLOOR + " ";
                            }
                        }

                    }
                    //combineColumnArray
                    finalContent[rowCount][columnCount] = ArrayUtils.arrayImplode(originColumnContentArray, MISSION_REQUIRE_FIRST_FLOOR);
                }
            }
        }
        return finalContent;
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
     */
    public void transformCommonContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final String idField, final Map specialField, final String keys, final String contentSplitFragment) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        final ConfigParser currentUIObject = this;

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
                        Thread currentThread = transformCommonThread(currentLang, outputPath, fileName, modelInfo, commonContent, idField, specialField, keys, contentSplitFragment);
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
                    notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    currentUIObject.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
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
     * @param specialField
     */
    public void transformCommonSingleFileContent(final String configFilePath, final String outputPath, final String fileName, final String sheetName, final Map specialField) {
        final long startTime = System.currentTimeMillis();
        final List<Thread> threadList = new ArrayList();
        final ConfigParser currentUIObject = this;

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
                    notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
                    currentUIObject.transformFinish("完成转换!");
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                } catch (BiffException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        }).start();
    }

    private Thread transformCommonSingleFileThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List<String>>> modelInfo, final String[][] commonContent, final Map specialField) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = buildSingleRowStr(commonContent[i], modelInfo, currentLang, specialField);
                    String singleItemInfo = singleRowInfo.get("singleRowInfo");
                    String descFile = buildSingleRowStoredPath(currentLang, "", outputPath, fileName);
                    try {
                        writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                    } catch (FileNotFoundException ex) {
                        showMessageDialogMessage(ex);
                    } catch (IOException ex) {
                        showMessageDialogMessage(ex);
                    }
                    notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                }
            }
        });
        return currentThread;
    }

    private Thread transformCommonThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List<String>>> modelInfo, final String[][] commonContent, final String idField, final Map specialField) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = buildSingleRowStr(commonContent[i], modelInfo, currentLang, idIndex, idField, specialField);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty()) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
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
                        notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                    }
                }
                try {
                    notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                    String descFile = buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                } catch (FileNotFoundException ex) {
                    showMessageDialogMessage(ex);
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        return currentThread;
    }

    private Thread transformMissionThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List>> modelInfo, final String[][] commonContent, final String idField, final Map specialField, final String keys, final String contentSplitFragment, final Map combineFields) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = buildMissionSingleRowStr(commonContent[i], modelInfo, currentLang, idIndex, idField, specialField, keys, contentSplitFragment, combineFields);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty()) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
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
                        notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                    }
                }
                try {
                    notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                    String descFile = buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                } catch (FileNotFoundException ex) {
                    showMessageDialogMessage(ex);
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        return currentThread;
    }

    private Thread transformCommonThread(final String currentLang, final String outputPath, final String fileName, final Map<String, Map<String, List<String>>> modelInfo, final String[][] commonContent, final String idField, final Map specialField, final String keys, final String contentSplitFragment) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                for (int i = 1; i < commonContent.length; i++) {
                    Map<String, String> singleRowInfo = buildSingleRowStr(commonContent[i], modelInfo, currentLang, idIndex, idField, specialField, keys, contentSplitFragment);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty()) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
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
                        notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                    }
                }
                try {
                    notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                    String descFile = buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
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
    private List<String> needSkipedFields(Map<String, String[]> combineFields) {
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

    public Map getModel(String[] dsOpengraphCfg, Map<String, String[]> combineFields) {
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
    public Map getModel(String[] dsOpengraphCfg) {
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

    public String getSingleRowContent(String[] dsOpengraphCfg) {

        return "";
    }

    public String getSingleRowContentByLang(String[] dsOpengraphCfg, String lang) {

        return "";
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

    /**
     * ds opengraph
     *
     * @param func
     * @param sheetNum
     * @param content
     * @return
     */
    public String buildFinalDsOpengraphStringFromStringArray(String func, int sheetNum, String[][] content) {
        int rows = content.length;
        String buildedContent = "";
        buildedContent += "//******************************************************************************************************************\r\n"
                + "//ds opengraph\r\n";

        buildedContent += "$J7CONFIG['collectActivityItemExtend'] = array(\r\n";
        String collectActivityItemExtendFormat = "    '%s' => array(\r\n"
                + "        'iExtraOutput' => array(\r\n"
                + "            '%s' => array(\r\n"
                + "                'iActiveStartTime' =>  strtotime('%s'),\r\n"
                + "                'iActiveEndTime' =>  strtotime('%s'),\r\n"
                + "                'iRate' =>  '%s',\r\n"
                + "                'iOutputNum' =>  '%s',\r\n"
                + "            ),\r\n"
                + "        ),\r\n"
                + "    ),\r\n";
        for (int rowNum = 1; rowNum < rows; rowNum++) {
            if (!content[rowNum][0].isEmpty()) {
                String period = content[rowNum][1];
                HashMap<String, String> singleCollectActivityCommonConf = collectActivityCommonConf.get(period);
                String iActiveStartTime = singleCollectActivityCommonConf.get("startTime");
                String iActiveEndTime = singleCollectActivityCommonConf.get("endTime");
                String iOutputItemId = singleCollectActivityCommonConf.get("scoreId");
                String iRate = content[rowNum][2];
                String iOutputNum = content[rowNum][3];
                String itemId = content[rowNum][0];
                buildedContent += String.format(collectActivityItemExtendFormat, itemId, iOutputItemId, iActiveStartTime, iActiveEndTime, iRate, iOutputNum);
            } else {
                break;
            }
        }
        buildedContent += ");";
        return buildedContent;
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

    public String buildStringFromStringArray(String func, int sheetNum, String[][] content) {
        String buildedContent = "";
        if ("DS_SHOP_OBJ_ITEM".equals(func)) {//ds shop object item
            buildedContent += buildFinalDsOpengraphStringFromStringArray(func, sheetNum, content);
        }
        return buildedContent;
    }

    public void writeToFile(String contents, File descFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        writeToFile(contents, descFile, "UTF-8");
    }

    public static void writeToFile(String contents, File descFile, String encoding) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        if (!descFile.getParentFile().exists()) {
            if (!descFile.getParentFile().mkdirs()) {
                JOptionPane.showMessageDialog(null, "创建目录文件所在的目录失败", "信息提示", JOptionPane.ERROR_MESSAGE);
                System.out.println("创建目录文件所在的目录失败！");
            }
        }
        if (!descFile.exists()) {
            descFile.createNewFile();
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descFile), encoding));
        writer.write(contents);
        writer.flush();
        writer.close();
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

    private void loadSetting(String file_path) {
        File f = new File(file_path);
        if (f.exists()) {
            Properties prop = new Properties();
            FileInputStream fis;
            try {
                fis = new FileInputStream(file_path);
                try {
                    prop.load(fis);
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                }
                if (!prop.getProperty("configBaseDir", "").isEmpty()) {
                    try {
                        configBaseDir = new String(prop.getProperty("configBaseDir").getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        showMessageDialogMessage(ex);
                    }
                }
                if (!prop.getProperty("outputDirectory", "").isEmpty()) {
                    try {
                        outputDirectory = new String(prop.getProperty("outputDirectory").getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        showMessageDialogMessage(ex);
                    }
                }
            } catch (FileNotFoundException ex) {
                showMessageDialogMessage(ex);
            }
        }
    }

    private static void showMessageDialogMessage(Exception ex) {
        String exMsg = ex.toString();
        JOptionPane.showMessageDialog(null, exMsg + new Throwable().getStackTrace()[1].toString(), "错误信息提示", JOptionPane.ERROR_MESSAGE);
    }

    public static String replace(String strSource, String strFrom, String strTo) {
        if (strSource == null) {
            return null;
        }
        int i = 0;
        if ((i = strSource.indexOf(strFrom, i)) >= 0) {
            char[] cSrc = strSource.toCharArray();
            char[] cTo = strTo.toCharArray();
            int len = strFrom.length();
            StringBuilder buf = new StringBuilder(cSrc.length);
            buf.append(cSrc, 0, i).append(cTo);
            i += len;
            int j = i;
            while ((i = strSource.indexOf(strFrom, i)) > 0) {
                buf.append(cSrc, j, i - j).append(cTo);
                i += len;
                j = i;
            }
            buf.append(cSrc, j, cSrc.length - j);
            return buf.toString();
        }
        return strSource;
    }

    private String configBaseDir;
    private String outputDirectory;
}
