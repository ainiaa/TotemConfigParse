/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.parser;

import static com.coding91.parser.ConfigParser.itemLangInfoCfg;
import static com.coding91.parser.ConfigParser.itemLangs;
import com.coding91.utils.ArrayUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class BuildConfigContent {

    public static HashMap buildItemIdAndItemName() {
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

    public static BuildConfigContent getInstance() {
        Class classType = BuildConfigContent.class;
        BuildConfigContent invokertester;
        try {
            invokertester = (BuildConfigContent) classType.newInstance();
            return invokertester;
        } catch (InstantiationException ex) {
            Logger.getLogger(BuildConfigContent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BuildConfigContent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, Map<String, String> specialField) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        StringBuilder singleRowStringbuffer = new StringBuilder();
        singleRowStringbuffer.append("array (").append("\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (specialField.containsKey(currentField)) {
                try {
                    String parseFieldFunctionName = specialField.get(currentField);
                    Method parseField = BuildConfigContent.class.getClass().getDeclaredMethod(parseFieldFunctionName, new Class[]{String.class, String.class, String.class});//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
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

    public static Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, Map<String, String> specialField, String keys) {
        Map<String, List<Integer>> fieldIndex = (Map<String, List<Integer>>) modelInfo.get("fieldIndex");
        Map<String, List<String>> fieldName = (Map<String, List<String>>) modelInfo.get("fieldName");
        List<Integer> currentFieldIndexList = fieldIndex.get(lang);
        List<String> currentFieldNameList = fieldName.get(lang);
        Integer[] currentFieldIndex = currentFieldIndexList.toArray(new Integer[currentFieldIndexList.size()]);
        String[] currentFieldName = currentFieldNameList.toArray(new String[currentFieldNameList.size()]);
        StringBuilder singleRowStringbuffer = new StringBuilder();
        singleRowStringbuffer.append("array (").append("\r\n");
        for (int i = 0; i < currentFieldIndex.length; i++) {
            int currentIndex = currentFieldIndex[i];
            String currentField = currentFieldName[i];
            String currentFieldContent = singleRowInfoContent[currentIndex];
            if (specialField.containsKey(currentField)) {
                try {
                    String parseFieldFunctionName = specialField.get(currentField);
                    Method parseField = BuildConfigContent.class.getClass().getDeclaredMethod(parseFieldFunctionName, new Class[]{String.class, String.class, String.class});//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
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

    public static String commonSingleFieldString(String field, String content, String leadingString) {
        content = content.replaceAll("<br>", "\r\n");//将<br>替换为\r\n
        String contentFormat = "'%s' => '%s',\r\n";
        return leadingString + String.format(contentFormat, field, content);
    }

    public static Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField) {
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
                        paramCount = 3;
                    } else {
                        paramCount = Integer.valueOf(parseFieldFunctionInfo[1]);
                    }

                    List<Class> paramType = new ArrayList();
                    for (int k = 0; k < paramCount; k++) {
                        paramType.add(String.class);
                    }
                    Class clazz[] = paramType.toArray(new Class[]{});
                    Method parseField = BuildConfigContent.class.getClass().getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
                    parseField.setAccessible(true);
                    singleRowStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "    "}));//@todo 精彩的写法
                    allRowsStringbuffer.append(parseField.invoke(getInstance(), new Object[]{currentField, currentFieldContent, "  "}));//@todo 精彩的写法
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
                    Method parseField = BuildConfigContent.class.getClass().getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
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

    public static Map buildSingleRowStr(String[] singleRowInfoContent, Map modelInfo, String lang, int idIndex, String idField, Map<String, String> specialField, String keys, String contentSplitFragment) {
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
                    Method parseField = BuildConfigContent.class.getClass().getDeclaredMethod(parseFieldFunctionName, clazz);//getMethod 方法 只能获取public 方法
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

}
