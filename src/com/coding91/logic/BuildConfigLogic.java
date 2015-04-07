package com.coding91.logic;

import static com.coding91.parser.ConfigParser.collectActivityCommonConf;
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
     * ds opengraph
     *
     * @param func
     * @param sheetNum
     * @param content
     * @return
     */
    public static String buildFinalDsOpengraphStringFromStringArray(String func, int sheetNum, String[][] content) {
        int rows = content.length;
        String buildedContent = "";
        buildedContent += "//******************************************************************************************************************\r\n//ds opengraph\r\n";

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

    public static String buildStringFromStringArray(String func, int sheetNum, String[][] content) {
        String buildedContent = "";
        if ("DS_SHOP_OBJ_ITEM".equals(func)) {//ds shop object item
            buildedContent += BuildConfigLogic.buildFinalDsOpengraphStringFromStringArray(func, sheetNum, content);
        }
        return buildedContent;
    }

    public static String buildSingleRowStoredPath(String lang, String itemId, String outputPath, String dirName, String fileName) {
        return outputPath + "/" + lang + "/" + dirName + "/" + fileName + itemId + ".php";
    }

    public static String buildSingleRowStoredPath(String lang, String itemId, String outputPath, String fileName) {
        return outputPath + "/" + lang + "/" + fileName + itemId + ".php";
    }

    public static String buildSingleItemStoredPath(String lang, String itemId, String outputPath) {
        return outputPath + "/" + lang + "/shopItem/shopItem" + itemId + ".php";
    }
}
