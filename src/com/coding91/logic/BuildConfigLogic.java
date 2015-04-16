package com.coding91.logic;

import java.util.Map;

/**
 *
 * @author Administrator
 */
public class BuildConfigLogic {

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
        String format = "%s/%s/%s/%s%s.php";
        return String.format(format, outputPath, lang, dirName, fileName, itemId);
    }
}
