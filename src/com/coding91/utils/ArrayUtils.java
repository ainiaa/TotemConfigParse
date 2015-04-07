package com.coding91.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ArrayUtils {

    // 二维数组纵向合并  
    public static String[][] arrayUnite(String[][] content1, String[][] content2) {
        String[][] newArrey = new String[][]{};
        List<String[]> list = new ArrayList();
        list.addAll(Arrays.<String[]>asList(content1));
        list.addAll(Arrays.<String[]>asList(content2));
        return list.toArray(newArrey);
    }

    public static String[] arrayPop(String[] oldArrayContent) {
        int rows = oldArrayContent.length;
        String[] cleanupedArrayContent = new String[rows - 1];
        for (int row = 0; row < rows - 1; row++) {
            cleanupedArrayContent[row] = oldArrayContent[row + 1];
        }

        return cleanupedArrayContent;
    }

    public static String[][] arrayPop(String[][] oldArrayContent) {
        int rows = oldArrayContent.length;
        int cols = oldArrayContent[0].length;
        String[][] cleanupedArrayContent = new String[rows - 1][cols];
        for (int row = 0; row < rows - 1; row++) {
            cleanupedArrayContent[row] = oldArrayContent[row + 1];
        }

        return cleanupedArrayContent;
    }

    public static String arrayImplode(String[] content, String implode) {
        StringBuilder finalStringBuffer = new StringBuilder();
        for (int index = 0; index < content.length; index++) {
            if (index != 0) {
                finalStringBuffer.append(implode);
            }
            finalStringBuffer.append(content[index]);
        }
        return finalStringBuffer.toString();
    }

    public static String[] arrayFilterByIndex(String[] oldArrayContent, String[] indexes) {
        String[] finalData = new String[indexes.length];
        int newIndex = 0;
        for (String index : indexes) {
            finalData[newIndex++] = oldArrayContent[Integer.parseInt(index)];
        }
        return finalData;
    }

    public static String[] arraySlice(String[] oldArrayContent, int startIndex) {
        int length = getMinLength(oldArrayContent, startIndex, -1);
        return arraySlice(oldArrayContent, startIndex, length);
    }

    public static int getMinLength(String[] oldArrayContent, int startIndex, int length) {
        int arrayContentLength = oldArrayContent.length;
        int minLength;
        if (length < 0) {
            minLength = arrayContentLength - startIndex;
        } else if (startIndex > length) {
            minLength = 0;
        } else if (startIndex + length > arrayContentLength) {
            minLength = arrayContentLength;
        } else {
            minLength = startIndex + length;
        }
        return minLength;
    }

    public static String[] arraySlice(String[] oldArrayContent, int startIndex, int length) {
        String[] finalArrayContent = new String[length];
        if (startIndex < 0) {
            startIndex = oldArrayContent.length + startIndex;
        }
        int minLength = getMinLength(oldArrayContent, startIndex, length);
        int index = 0;
        while (startIndex < minLength) {
            finalArrayContent[index++] = oldArrayContent[startIndex++];
        }
        return finalArrayContent;
    }

    public static HashMap<String, String> arrayCombine(String[] arrayKeys, String[] arrayValues) {
        HashMap<String, String> hashMap = new HashMap();
        if (arrayKeys.length == arrayValues.length) {
            for (int i = 0; i < arrayKeys.length; i++) {
                hashMap.put(arrayKeys[i], arrayValues[i]);
            }
        }
        return hashMap;
    }

    public static String[] arrayTrim(String[] oldArrayContent) {
        ArrayList<String> tmpData = new ArrayList();
        for (String tmpContent : oldArrayContent) {
            String trimedContent = tmpContent.trim();
            if (!trimedContent.isEmpty()) {
                tmpData.add(trimedContent);
            }
        }
        return (String[]) tmpData.toArray(new String[tmpData.size()]); // TODO (String[]) tmpData.toArray(); 会报类型转换异常
    }

    public static String stringJoin(String[] origon, String glue) {
        StringBuffer sb;
        sb = new StringBuffer();
        int origonLength = origon.length;
        for (int i = 0; i < origonLength; i++) {
            sb.append(origon[i]);
            if (i < origonLength - 1) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }

    public static String[] getStringArrayBySingleIndex(String[][] content, int arrayIndex) {
        int rowCount = content.length;
        String[] stringArray = new String[rowCount];
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            stringArray[rowNum] = content[rowNum][arrayIndex];
        }
        return stringArray;
    }

    public static String[][] getStringArrayByIndex(String[][] content, int[] arrayIndex) {
        int rowCount = content.length;
        int colCount = content[0].length;
        String[][] stringArray = new String[rowCount][arrayIndex.length];
        int arrayIndexValue;
        int j = 0, row, column, arrayIndexLength = arrayIndex.length;
        for (row = 0; row < rowCount; row++) {
            for (column = 0; column < arrayIndexLength; column++) {
                arrayIndexValue = arrayIndex[column];
                stringArray[row][column] = content[row][arrayIndexValue];
            }
        }

        String[][] finalStringArray = new String[j][colCount];
        System.arraycopy(stringArray, 0, finalStringArray, 0, j);
        return finalStringArray;
    }

    public static String[][] getStringArrayByField(String[][] content, String value, int index) {
        int rowCount = content.length;
        int colCount = content[0].length;
        String[][] stringArray = new String[rowCount][colCount];
        int j = 0, row, startRow = -1, endRow = -1;

        for (row = 0; row < rowCount; row++) {
            String indexValue = content[row][index];
            if (indexValue.equals(value)) {
                if (startRow == -1) {
                    startRow = row;
                }
                endRow = row;
                stringArray[j++] = content[row];
            }
        }
        String[][] finalStringArray = new String[j][colCount];
        System.arraycopy(stringArray, 0, finalStringArray, 0, j);
        return finalStringArray;
    }

    public static int getFirstIndexFromArray(String[] content, String findMe) {
        int firstIndex = -1;
        String currentContent;
        int contentLength = content.length;
        for (int i = 0; i < contentLength; i++) {
            currentContent = content[i];
            if (currentContent.equals(findMe)) {
                firstIndex = i;
                break;
            }
        }
        return firstIndex;
    }

    public static int getLastIndexFromArray(String[] content, String findMe) {
        int lastIndex = -1;
        String currentContent;
        int contentLength = content.length;
        for (int i = contentLength; i > 0; i--) {
            currentContent = content[i];
            if (currentContent.equals(findMe)) {
                lastIndex = i;
                break;
            }
        }
        return lastIndex;
    }
    
    public static String[] getModelFromStringArray(String[][] content) {
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

}
