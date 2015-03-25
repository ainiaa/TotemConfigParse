/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.logic;

import static com.coding91.parser.ConfigParser.getFieldIndexByFieldName;
import com.coding91.utils.ArrayUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ParseConfigLogic {

    /**
     *
     * @param field
     * @param content
     * @param leadingString
     * @return
     */
    public static String parseActivityInfo(String field, String content, String leadingString) {
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
    public static String parseCommonMultiple(String field, String content, String leadingString) {
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
    public static String parseBindingRecipeActivateData(String field, String content, String leadingString) {
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

    public static String parseMissionInfoRewardData(String field, String content, String leadingString) {
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

    public static String parseMissionInfoMissionRequire(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31   //fck 这个太恶心了，  content 只有9项内容，其他的内容再另一个content中，且 还需要进行分割。。。 坑爹啊。。
        String[] contentArray = content.split(MISSION_REQUIRE_FIRST_FLOOR);//第一层
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        String leadingString2 = leadingString + leadingString;
        String leadingString3 = leadingString + leadingString + leadingString;
        String secondFloorContentFormat = "%s%d => array (\r\n"
                + leadingString3 + "%s'require_index' => '%s',\r\n"
                + leadingString3 + "%s'mission_require_icon' => '%s',\r\n"
                + leadingString3 + "%s'mission_require_type' => '%s',\r\n"
                + leadingString3 + "%s'require_item_id' => '%s',\r\n"
                + leadingString3 + "%s'require_item_num' => '%s',\r\n"
                + leadingString3 + "%s'require_type' => '%s',\r\n"
                + leadingString3 + "%s'require_id' => '%s',\r\n"
                + leadingString3 + "%s'is_skippable' => '%s',\r\n"
                + leadingString3 + "%s'require_skip_cash' => '%s',\r\n"
                + leadingString3 + "%s'mission_sub_require' => '%s',\r\n"
                + leadingString3 + "%s'mission_sub_require_desc' => '%s',\r\n"
                + leadingString3 + "%s'mission_sub_require_tips' => '%s',\r\n"
                + leadingString3 + "%s),\r\n";
        if (!content.isEmpty()) {
            tmpContent.append("array(\r\n");
            int firstFoolrIndex = 0;
            for (String currentFirstFloorContent : contentArray) {
                if (!currentFirstFloorContent.isEmpty()) {
                    String[] currentSecondFloorContentArray = currentFirstFloorContent.split(MISSION_REQUIRE_SECOND_FLOOR);//第二层
                    tmpContent.append(leadingString).append(leadingString)
                            .append(String.format(secondFloorContentFormat, leadingString2, firstFoolrIndex,
                                            currentSecondFloorContentArray[0],
                                            currentSecondFloorContentArray[1],
                                            currentSecondFloorContentArray[2],
                                            currentSecondFloorContentArray[3],
                                            currentSecondFloorContentArray[4],
                                            currentSecondFloorContentArray[5],
                                            currentSecondFloorContentArray[6],
                                            currentSecondFloorContentArray[7],
                                            currentSecondFloorContentArray[8],
                                            currentSecondFloorContentArray[9],
                                            currentSecondFloorContentArray[10],
                                            currentSecondFloorContentArray[11]
                                    ));
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
     *
     * String originContent =
     * "115:49201:4:0,116:49202:4:0,113:50134:5:1,116:50135:5:1|117:49203:4:0,118:49204:4:0,119:50136:5:1,122:50137:5:1|119:49205:4:0,120:49206:4:0,125:50138:5:1,128:50139:5:1";
     * String[] flagment = new String[]{"|", ",", ":"}; //String[] contentKey =
     * new String[]{"activate_id", "activate_item_id", "activate_num",
     * "activate_type"}; String[] contentKey = new String[]{}; int index = 0;
     * String content = parseCommonMultipleEx(originContent, flagment,
     * contentKey, index);
     *
     * @param fieldName
     * @param fieldValue
     * @param contentSeparator
     * @param contentKey
     * @param index
     * @return
     */
    public static String parseCommonMultipleEx(String fieldName, String fieldValue, String[] contentSeparator, String[] contentKey, int index) {
        StringBuilder finalContent = new StringBuilder();
        finalContent.append("\r\narray(\r\n");
        if (!fieldValue.isEmpty()) {//内容不为空
            String[] contentChunk = fieldValue.split("\\" + contentSeparator[index]);
            if (contentSeparator.length == index + 1) {//已经是最后一层了
                if (contentKey.length > 0) {//最后一层需要将内容和key对应起来 key1 => 'key1content',key2 => 'key1content2',...
                    for (int i = 0; i < contentKey.length; i++) {
                        String content = "";
                        if (i <= contentChunk.length - 1) {
                            content = contentChunk[i];
                        }
                        finalContent.append(String.format("'%s' => '%s',\r\n", contentKey[i], content));
                    }
                } else {//直接使用逗号分割放入array()中即可
                    for (int i = 0; i < contentChunk.length; i++) {
                        String content = contentChunk[i];
                        if (i == 0) {//第一个要多添加一个\r\n
                            finalContent.append("\r\n");
                        }
                        finalContent.append(String.format("%d => '%s',\r\n", i, content));
                    }
                }
            } else {
                ++index;
                int currentIndex = 0;
                for (String currentChunk : contentChunk) {
                    finalContent.append(currentIndex++).append(" => ").append(parseCommonMultipleEx(fieldName, currentChunk, contentSeparator, contentKey, index)).append(",\r\n");
                }
            }
        }
        finalContent.append(")");
        if (contentSeparator.length == index + 1) {
            finalContent.append("");
        }
        return finalContent.toString();
    }

    public static String parseCommonMultipleEx(String fieldName, String fieldValue, Map<String, ?> parseFunctionParam, String index) {
        String[] contentSeparator = (String[]) parseFunctionParam.get("contentSeparator");
        String[] contentKey = (String[]) parseFunctionParam.get("contentKey");
        int indexIntValue = Integer.valueOf(index);
        return parseCommonMultipleEx(fieldName, fieldValue, contentSeparator, contentKey, indexIntValue);
    }

    public static String parseCommonMultipleEx(Map parseFunctionParam, String index) {
        String[] contentSeparator = (String[]) parseFunctionParam.get("contentSeparator");
        String[] contentKey = (String[]) parseFunctionParam.get("contentKey");
        int indexIntValue = Integer.valueOf(index);
        String fieldName = (String) parseFunctionParam.get("fieldName");
        String fieldValue = (String) parseFunctionParam.get("fieldValue");
        return parseCommonMultipleEx(fieldName, fieldValue, contentSeparator, contentKey, indexIntValue);
    }

    public static String parseCommonMultipleEx(Map parseFunctionParam) {
        return parseCommonMultipleEx(parseFunctionParam, "0");
    }

    public static String parseCommonMultipleEx(Map parseFunctionParam, String fieldName, String fieldValue) {

        String[] contentSeparator = (String[]) parseFunctionParam.get("contentSeparator");
        String[] contentKey = (String[]) parseFunctionParam.get("contentKey");
        int indexIntValue = Integer.valueOf("0");
        return parseCommonMultipleEx(fieldName, fieldValue, contentSeparator, contentKey, indexIntValue);
    }

    private String parseGameRankScoreRewards(Map parseFunctionParam, String fieldName, String fieldValue) {

        String leadingString = "    ";
        String rewardStringFormat = "\r\n  %d => \r\n       array(\r\n        'itemId' => '%s',\r\n         'itemNum' => '%s',\r\n      ),";
        //500|1:5,56004:50,56001:5000;
        String[] singleRankScore = fieldValue.split(";");
        String finalContent = "\r\n  array(\r\n";
        for (int i = 0; i < singleRankScore.length; i++) {
            String currentSingleRankScore = singleRankScore[i];
            if (!currentSingleRankScore.isEmpty()) {
                String[] singleRankScoreItem = currentSingleRankScore.split("\\|");
                finalContent += leadingString + leadingString + i + " => \r\n      array(\r\n";
                finalContent += leadingString + leadingString + leadingString + "'score' => " + singleRankScoreItem[0] + ",\r\n";
                finalContent += leadingString + leadingString + leadingString + "'reward' => \r\n      array(\r\n";
                String currentSingleRankScoreItem = singleRankScoreItem[1];
                if (!currentSingleRankScoreItem.isEmpty()) {
                    String[] singleRankScoreItemReward = currentSingleRankScoreItem.split(",");
                    for (int j = 0; j < singleRankScoreItemReward.length; j++) {
                        String currentSingleRankScoreItemReward = singleRankScoreItemReward[j];
                        if (!currentSingleRankScoreItemReward.isEmpty()) {
                            String[] singleRankScoreItemRewardItem = currentSingleRankScoreItemReward.split(":");
                            finalContent += leadingString + leadingString + leadingString + leadingString + String.format(rewardStringFormat, j, singleRankScoreItemRewardItem[0], singleRankScoreItemRewardItem[1]) + "\r\n";
                        }
                    }
                }
            }
            finalContent += leadingString + leadingString + leadingString + "),\r\n";
            finalContent += leadingString + "),\r\n";
        }

        finalContent += leadingString + ")";
        return finalContent;
    }

    private String parseDessertInfoNormalDessertData(Map parseFunctionParam, String fieldName, String fieldValue) {
        String contentFormat = "\r\n    array (\n"
                + "      0 => \n"
                + "      array (\n"
                + "        'normal_dessert_id' => '%s',\n"
                + "        'normal_dessert_num' => \n"
                + "        array (\n"
                + "          %s"
                + "        ),\n"
                + "      ),\n"
                + "    )";
        String[] singleContentFlagment = fieldValue.split(":");
        String[] singleOuptputNum = singleContentFlagment[1].split("\\|");
        String ouptputNumFormat = "          %d => '%s',\r\n";
        StringBuilder ouptputNumContent = new StringBuilder();
        for (int i = 0; i < singleOuptputNum.length; i++) {
            ouptputNumContent.append(String.format(ouptputNumFormat, i, singleOuptputNum[i]));
        }
        return String.format(contentFormat, singleContentFlagment[0], ouptputNumContent.toString());
    }

    private String parseDessertInfoCondimentsDessertData(Map parseFunctionParam, String fieldName, String fieldValue) {
        StringBuilder finalContent = new StringBuilder();
        finalContent.append("\r\n    array (\r\n");
        String contentFormat = "%d => \n"
                + "      array (\n"
                + "        'condiments_dessert_id' => '%s',\n"
                + "        'condiments_dessert_num' => \n"
                + "        array (\n"
                + "         %s"
                + "        ),\n"
                + "        'level_up_cook_times' => '%s',\n"
                + "        'unlock_pink_star_require' => %s,\n"
                + "        'cook_success_rate' => '%s',\n"
                + "      ),\r\n";
        String[] contentFlagment = fieldValue.split(",");
        for (int i = 0; i < contentFlagment.length; i++) {
            String[] singleContentFlagment = contentFlagment[i].split(":");
            String[] singleCondimentsDessertNum = singleContentFlagment[1].split("\\|");
            String ouptputNumFormat = "          %d => '%s',\r\n";
            StringBuilder ouptputNumContent = new StringBuilder();
            for (int j = 0; j < singleCondimentsDessertNum.length; j++) {
                ouptputNumContent.append(String.format(ouptputNumFormat, j, singleCondimentsDessertNum[j]));
            }
            finalContent.append(String.format(contentFormat, i, singleContentFlagment[0], ouptputNumContent.toString(), singleContentFlagment[2], singleContentFlagment[3], singleContentFlagment[4]));
        }
        finalContent.append("    )");
        return finalContent.toString();
    }

    private String parseDessertInfoLevelUpCookTimes(Map parseFunctionParam, String fieldName, String fieldValue) {
        String contentFormat = "\r\n    array (\n"
                + "      %s"
                + "    )";
        String[] singleContentFlagment = fieldValue.split(",");
        String ouptputNumFormat = "          %s => '%s',\r\n";
        StringBuilder ouptputNumContent = new StringBuilder();
        for (int i = 0; i < singleContentFlagment.length; i++) {
            String[] singleOuptputNum = singleContentFlagment[i].split(":");
            ouptputNumContent.append(String.format(ouptputNumFormat, singleOuptputNum[0], singleOuptputNum[1]));
        }
        return String.format(contentFormat, ouptputNumContent.toString());
    }

    private String parseGiftPackageFixData(Map parseFunctionParam, String fieldName, String fieldValue) {
        String contentFormat = "\r\n    array (\n"
                + "      %s"
                + "    )";
        String[] singleContentFlagment = fieldValue.split(",");
        String ouptputNumFormat = "      %d => \n"
                + "      array (\n"
                + "        'index' => '%s',\n"
                + "        'item_id' => '%s',\n"
                + "        'num' => '%s',\n"
                + "      ),\r\n";
        StringBuilder ouptputNumContent = new StringBuilder();
        for (int i = 0; i < singleContentFlagment.length; i++) {
            String[] singleOuptputNum = singleContentFlagment[i].split(":");
            ouptputNumContent.append(String.format(ouptputNumFormat, i, singleOuptputNum[0], singleOuptputNum[1], singleOuptputNum[2]));
        }
        return String.format(contentFormat, ouptputNumContent.toString());
    }

    /**
     * 将 1:12,2:22,3:33 转换为 array ( 0 => array('key' => 1,'value' => 12), 1 =>
     * array('key' => 2,'value' => 22), 2 => array('key' => 3,'value' => 33), )
     * currentField, currentFieldContent, " ", contentSplitFragment
     *
     * @param field
     * @param keys
     * @param content
     * @param leadingString
     * @return
     */
    public static String parseCommonMultipleWithKeyValue(String field, String content, String leadingString, String keys) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n '11:6,2:31
        String[] contentArray = content.split(",");
        StringBuilder tmpContent = new StringBuilder();
        tmpContent.append(leadingString).append("'").append(field).append("' => ");
        if (!content.isEmpty()) {
            tmpContent.append("array(\r\n");
            String[] keyArray = keys.split(",");
            int index = 0;
            for (String currentContent : contentArray) {
                String[] contentFactor = currentContent.split(":");
                tmpContent.append(leadingString).append(leadingString).append(leadingString).append(leadingString).append(index++).append(" => array(\r\n");
                if (contentFactor.length == keyArray.length) {
                    for (int i = 0; i < keyArray.length; i++) {
                        tmpContent.append(String.format("'%s' => '%s',\r\n", keyArray[i], contentFactor[i]));
                    }
                }
                tmpContent.append("),\r\n");
            }
            tmpContent.append(")");
        }
        tmpContent.append(",\r\n");
        return tmpContent.toString();
    }

    public static String[][] cleanupOriginContent(final String[][] originContent, String currentLang, Map<String, Map<String, List>> modelInfo, Map<String, String[]> combineFields) {
        String[][] finalContent = new String[originContent.length][originContent[0].length];
//        System.arraycopy(originContent, 0, finalContent, 0, originContent.length);//这个会影响到 originContent 这个数组。。。。
        for (int i = 0; i < originContent.length; i++) {
            System.arraycopy(originContent[i], 0, finalContent[i], 0, originContent[i].length);
        }
        List<String> fileNameInModelInfoList = modelInfo.get("fieldName").get(currentLang);
        List<Integer> fileIndexInModelInfoList = modelInfo.get("fieldIndex").get(currentLang);
        Map<Integer, Integer[]> finalIndexMap = new HashMap();
        for (Map.Entry<String, String[]> entry : combineFields.entrySet()) {
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
}
