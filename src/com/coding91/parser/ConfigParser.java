package com.coding91.parser;

import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ConfigParser {


    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
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

    /**
     * 
     * @param modelInfo
     * @param fieldName
     * @return 
     */
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

    public static String[] getLangArray() {
        return new String[]{"zh_tw", "de_de", "fr_fr", "en_us", "es_es"};
    }
}
