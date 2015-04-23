package com.coding91.parser;

import com.coding91.logic.ParseConfigLogic;
import com.coding91.ui.NoticeMessageJFrame;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Administrator
 */
public class BuildConfigContent {

    public static String leadingString = "  ";
    public static String doubleLeadingString = "    ";
    public static String tripleLeadingString = "      ";
    public static String fourfoldLeadingString = "        ";

    public static ParseConfigLogic getInstance() {
        Class classType = ParseConfigLogic.class;
        ParseConfigLogic invokertester;
        try {
            invokertester = (ParseConfigLogic) classType.newInstance();
            return invokertester;
        } catch (InstantiationException | IllegalAccessException ex) {
            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
        }
        return null;
    }

    public static String commonSingleFieldString(String field, String content, String leadingString, boolean contentUseQuote) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n
        String contentFormat;
        if (contentUseQuote) {
            contentFormat = "'%s' => '%s',\r\n";
        } else {
            contentFormat = "'%s' => %s,\r\n";
        }
        return leadingString + String.format(contentFormat, field, content);
    }

    /**
     *
     * @param singleRowInfoContent
     * @param modelInfo
     * @param lang
     * @param idIndex
     * @param idField
     * @param extraParams
     * @return
     */
    public static Map buildSingleRowString(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, Map> extraParams) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        StringBuilder singleRowStringBuilder = new StringBuilder();
        StringBuilder allRowsStringBuilder = new StringBuilder();
        boolean isNeedAllRowsContent = true;
        String id;
        if (-1 == idIndex) {//不需要idIndex
            isNeedAllRowsContent = false;
            id = "";
        } else {
            id = singleRowInfoContent[idIndex];
        }
        singleRowStringBuilder.append("array (").append("\r\n");
        if (isNeedAllRowsContent) {
            Map idFieldInfo;
            String idNeedWrapped = null;
            if (extraParams.containsKey("idFieldInfo")) {
                idFieldInfo = extraParams.get("idFieldInfo");
                if (idFieldInfo.containsKey("idNeedWrapped")) {
                    idNeedWrapped = (String) idFieldInfo.get("idNeedWrapped");
                }
            }
            String format;
            if (null != idNeedWrapped && idNeedWrapped.equals("1")) {
                format = "%s'%s' => \r\n%sarray (\r\n";
            } else {
                format = "%s%s => \r\n%sarray (\r\n";
            }
            allRowsStringBuilder.append(String.format(format, leadingString, id, leadingString));
        }

        Map defaultValueMap = extraParams.get("defaultValue");
        Map globalDefaultValueMap = extraParams.get("globalDefaultValue");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (currentField.equals(idField)) {
                id = currentFieldContent;
            }

            String currentFieldSingleContent;
            String currentFieldAllRowsContent = "";

            boolean isContentNeedQuote = isContentNeedQuote(currentField, currentFieldContent, defaultValueMap, globalDefaultValueMap);
            if (isContentEmpty(currentField, currentFieldContent, defaultValueMap, globalDefaultValueMap)) {//为空
                isContentNeedQuote = false;
                currentFieldContent = getDefaultValue(currentField, defaultValueMap, globalDefaultValueMap);
                currentFieldSingleContent = commonSingleFieldString(currentField, currentFieldContent, leadingString, isContentNeedQuote);
                if (isNeedAllRowsContent) {
                    currentFieldAllRowsContent = commonSingleFieldString(currentField, currentFieldContent, doubleLeadingString, isContentNeedQuote);
                }
            } else if (extraParams.containsKey(currentField)) {
                try {
                    Map parseFieldFunctionInfo = extraParams.get(currentField);

                    String parseFieldFunctionName = (String) parseFieldFunctionInfo.get("parseFunction");
//                    parseFieldFunctionInfo.put("fieldName", currentField);//直接传递过去 放置错乱
//                    parseFieldFunctionInfo.put("fieldValue", currentFieldContent);

                    String[] contentKey = (String[]) parseFieldFunctionInfo.get("contentKey");
                    Map isContentNeedQuoteMap = new HashMap();
                    for (String currentContentKey : contentKey) {
                        isContentNeedQuoteMap.put(currentContentKey, isContentNeedQuote(currentContentKey, currentFieldContent, defaultValueMap, globalDefaultValueMap));
                    }

                    parseFieldFunctionInfo.put("isContentNeedQuoteMap", isContentNeedQuoteMap);
                    parseFieldFunctionInfo.put("defaultValueMap", defaultValueMap);
                    parseFieldFunctionInfo.put("globalDefaultValueMap", globalDefaultValueMap);

                    Method parseField = ParseConfigLogic.class.getDeclaredMethod(parseFieldFunctionName, new Class[]{Map.class, String.class, String.class, Boolean.class});//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);

                    String format = "%s'%s' => %s,\r\n";
                    currentFieldSingleContent = String.format(format, leadingString, currentField, parseField.invoke(getInstance(), new Object[]{parseFieldFunctionInfo, currentField, currentFieldContent, false}));
                    if (isNeedAllRowsContent) {
                        currentFieldAllRowsContent = String.format(format, doubleLeadingString, currentField, parseField.invoke(getInstance(), new Object[]{parseFieldFunctionInfo, currentField, currentFieldContent, true}));
                    }
                } catch (IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
                    currentFieldSingleContent = "";
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                }
            } else {
                currentFieldSingleContent = commonSingleFieldString(currentField, currentFieldContent, leadingString, isContentNeedQuote);//singleRowStringBuilder.append(commonSingleFieldString(currentField, currentFieldContent, "    ", true));
                if (isNeedAllRowsContent) {
                    currentFieldAllRowsContent = commonSingleFieldString(currentField, currentFieldContent, doubleLeadingString, isContentNeedQuote);
                }
            }

            singleRowStringBuilder.append(currentFieldSingleContent);
            if (isNeedAllRowsContent) {
                allRowsStringBuilder.append(currentFieldAllRowsContent);
            }
        }

        singleRowStringBuilder.append(");");
        if (isNeedAllRowsContent) {
            allRowsStringBuilder.append(leadingString).append("),");
        }

        Map finalInfo = new HashMap();
        finalInfo.put(idField, id);
        finalInfo.put("singleRowInfo", singleRowStringBuilder.toString());
        finalInfo.put("allRowsInfo", allRowsStringBuilder.toString());
        return finalInfo;
    }

    /**
     * todo 这个还需要处理
     *
     * @param currentField
     * @param currentFieldValue
     * @param defaultValueMap
     * @param globalDefaultValueMap
     * @return
     */
    public static boolean isContentEmpty(String currentField, String currentFieldValue, Map<String, String> defaultValueMap, Map<String, String> globalDefaultValueMap) {
        boolean isContentEmpty = false;
        String defalutValue = "|''|0|'0'";
        String currentFieldDefaultKey = currentField + ".emptyValue";
        String commonDefaultKey = "common.emptyValue";
        if (defaultValueMap.containsKey(currentFieldDefaultKey)) {//设置了默认值
            defalutValue = defaultValueMap.get(currentFieldDefaultKey);
        } else if (defaultValueMap.containsKey(commonDefaultKey)) {// 如果设置了通用默认值 直接使用该默认值
            defalutValue = defaultValueMap.get(commonDefaultKey);
        }
        if (defalutValue == null) {
            defalutValue = "null";
        }

        String[] defalutValueArray = defalutValue.split("\\|");

        for (String currentDefaultValue : defalutValueArray) {
            if (currentDefaultValue.equals(currentFieldValue)) {
                isContentEmpty = true;
                break;
            }
        }

        return isContentEmpty;
    }

    public static boolean isContentNeedQuote(String currentField, String currentFieldValue, Map<String, String> defaultValueMap, Map<String, String> globalDefaultValueMap) {
        String needQuote = "1";
        String currentFieldDefaultKey = currentField + ".needQuote";
        String commonDefaultKey = "common.needQuote";
        if (defaultValueMap.containsKey(currentFieldDefaultKey)) {//设置了默认值
            needQuote = defaultValueMap.get(currentFieldDefaultKey);
        } else if (defaultValueMap.containsKey(commonDefaultKey)) {// 如果设置了通用默认值 直接使用该默认值
            needQuote = defaultValueMap.get(commonDefaultKey);
        }
        return "1".equals(needQuote);
    }

    /**
     * 获得默认值
     *
     * @param currentField
     * @param defaultValueMap
     * @param globalDefaultValueMap
     * @return
     */
    public static String getDefaultValue(String currentField, Map<String, String> defaultValueMap, Map<String, String> globalDefaultValueMap) {
        String defalutValue = "''";
        String currentFieldDefaultKey = currentField + ".default";
        String currentFieldTypeKey = currentField + ".type";
        String commonDefaultKey = "common.default";
        String typeDefaultKey = "type.default";
        if (defaultValueMap.containsKey(currentFieldDefaultKey)) {//设置了默认值
            defalutValue = defaultValueMap.get(currentFieldDefaultKey);
        } else if (defaultValueMap.containsKey(currentFieldTypeKey)) {//设置为当前类型 可以根据当前类型去的默认值  当前配置项的 该类型默认值  eg type.int='',没有 找找 global.properties 的  type.int=''
            String specialType = defaultValueMap.get(currentFieldTypeKey);//eg:  market_purchase_limit.type=array
            String specialTypeDefalutKey = "type." + specialType;//type.array=array()
            if (defaultValueMap.containsKey(specialTypeDefalutKey)) {
                defalutValue = defaultValueMap.get(specialTypeDefalutKey);
            } else if (globalDefaultValueMap.containsKey(specialTypeDefalutKey)) {
                defalutValue = globalDefaultValueMap.get(specialTypeDefalutKey);
            }
        } else if (defaultValueMap.containsKey(commonDefaultKey)) {// 如果设置了通用默认值 直接使用该默认值
            defalutValue = defaultValueMap.get(commonDefaultKey);
        } else if (defaultValueMap.containsKey(typeDefaultKey) && globalDefaultValueMap.containsKey(defaultValueMap.get(typeDefaultKey))) {//设置了默认类型，到 global.properties 找该类型的默认值
            defalutValue = globalDefaultValueMap.get(defaultValueMap.get(typeDefaultKey));
        }
        if (defalutValue == null) {
            defalutValue = "null";
        }
        return defalutValue;
    }

    /**
     *
     * @param singleRowInfoContent
     * @param modelInfo
     * @param lang
     * @param idIndex
     * @param idField
     * @param specialField
     * @param keys
     * @param contentSplitFragment
     * @param defaultValue
     * @return
     */
    public Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField, String keys, String contentSplitFragment, Map<String, String> defaultValue) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
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
                    Method parseField = ParseConfigLogic.class.getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    //@todo 这一部分可以重构成动态参数的方式 以后在修改  下面这一块调用是存在问题的 需要考虑应该怎么将实参传递过去!!!!
                    if (paramType.size() == 3) {
                        singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "  "}));//@todo 精彩的写法
                    } else if (paramType.size() == 4) {
                        singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    ", keys}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "  ", keys}));//@todo 精彩的写法
                    } else if (paramType.size() == 5) {
                        singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    ", contentSplitFragment, keys}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "  ", contentSplitFragment, keys}));//@todo 精彩的写法
                    }
                } catch (IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    ", true));
                allRowsStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "  ", true));
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

    public static Map buildMissionSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField, String keys, String contentSplitFragment, Map combineFields) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
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
                    Method parseField = ParseConfigLogic.class.getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    //@todo 这一部分可以重构成动态参数的方式 以后在修改
                    if (paramType.size() == 3) {
                        singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "  "}));//@todo 精彩的写法
                    } else if (paramType.size() == 4) {
                        singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    ", contentSplitFragment}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "  ", contentSplitFragment}));//@todo 精彩的写法
                    } else if (paramType.size() == 5) {
                        singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, keys, currentFieldContent, "    ", contentSplitFragment}));//@todo 精彩的写法
                        allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, keys, currentFieldContent, "  ", contentSplitFragment}));//@todo 精彩的写法
                    }
                } catch (IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                }
            } else {
                singleRowStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "    ", true));
                allRowsStringbuffer.append(commonSingleFieldString(currentField, currentFieldContent, "  ", true));
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

    public static Map buildMissionTriggerSingleRowStr(String[] singleRowInfoContent, int index) {
        Map finalInfo = new HashMap();
        StringBuilder singleRowStringbuffer = new StringBuilder();
        StringBuilder allRowsStringbuffer = new StringBuilder();
        if (singleRowInfoContent[4].isEmpty() || singleRowInfoContent[4].equals("0")) {
            singleRowStringbuffer.append(" '").append(singleRowInfoContent[0]).append("';");
            allRowsStringbuffer.append("  ").append(index).append(" => '").append(singleRowInfoContent[0]).append("',");
            finalInfo.put("id", String.valueOf(index));
        } else {
            finalInfo.put("id", "");
        }

        finalInfo.put("singleRowInfo", singleRowStringbuffer.toString());
        finalInfo.put("allRowsInfo", allRowsStringbuffer.toString());
        return finalInfo;
    }

}
