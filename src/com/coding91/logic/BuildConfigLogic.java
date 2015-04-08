package com.coding91.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class BuildConfigLogic {

    public static Map buildSingleItemStr(String[] itemBaseInfoContent, Map modelInfo, String lang, int itemIdIndex, Map<String, String> fieldDefaultPair) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        StringBuilder singleItemStringbuffer = new StringBuilder();
        StringBuilder allItemsStringbuffer = new StringBuilder();
        String itemId = itemBaseInfoContent[itemIdIndex];
        singleItemStringbuffer.append("array (").append("\r\n");
        allItemsStringbuffer.append("  ").append(itemId).append(" => \r\n").append("  array (\r\n");
        String singleItemformat = "  '%s' => %s, \r\n";
        String allItemsformat = "    '%s' => %s, \r\n";
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = itemBaseInfoContent[currentIndex];
            currentFieldContent = getFiledFinalValue(currentField, currentFieldContent, fieldDefaultPair);
            if (currentField.equals("item_id")) {
                itemId = currentFieldContent;
            }
            currentFieldContent = currentFieldContent.replaceAll("<br>", "\r\n");//将<br>替换为\r\n
            singleItemStringbuffer.append(String.format(singleItemformat, currentField, currentFieldContent));
            allItemsStringbuffer.append(String.format(allItemsformat, currentField, currentFieldContent));
        }

        singleItemStringbuffer.append(");");
        allItemsStringbuffer.append("  ),");

        Map finalInfo = new HashMap();
        finalInfo.put("itemId", itemId);
        finalInfo.put("singleItemInfo", singleItemStringbuffer.toString());
        finalInfo.put("allItemInfo", allItemsStringbuffer.toString());
        return finalInfo;
    }

    /**
     * 获得默认值
     * @param fieldName
     * @param fieldValue
     * @param fieldDefaultPair
     * @return 
     */
    public static String getFiledFinalValue(String fieldName, String fieldValue, Map<String, String> fieldDefaultPair) {
        boolean needFormat = true;
        if (fieldValue.equals("0") || fieldValue.isEmpty()) {//当前值为空 尝试获得默认值
            if (fieldDefaultPair.containsKey(fieldName + ".default")) {//默认值里面有
                fieldValue = fieldDefaultPair.get(fieldName + ".default");
                needFormat = false;
            } else if (fieldDefaultPair.containsKey(fieldName + ".type") && fieldDefaultPair.containsKey(fieldDefaultPair.get(fieldName + ".type"))) {
                fieldValue = fieldDefaultPair.get(fieldDefaultPair.get(fieldName + ".type"));
                needFormat = false;
            }
        } 
        if (needFormat){
            fieldValue = "'" + fieldValue + "'";
        }
        return fieldValue;
    }

    /**
     * 
     * @param lang
     * @param itemId
     * @param outputPath
     * @param dirName
     * @param fileName
     * @return 
     */
    public static String buildSingleRowStoredPath(String lang, String itemId, String outputPath, String dirName, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(outputPath).append("/").append(lang).append("/").append(dirName).append("/").append(fileName).append(itemId).append(".php");
        String format = "%s/%s/%s/%s%s.php";
        return String.format(format, outputPath, lang, dirName, fileName, itemId);
    }

    /**
     * 
     * @param lang
     * @param itemId
     * @param outputPath
     * @param fileName
     * @return 
     */
    public static String buildSingleRowStoredPath(String lang, String itemId, String outputPath, String fileName) {
        String format = "%s/%s/%s%s.php";
        return String.format(format, outputPath, lang, fileName, itemId);
    }
}
