/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.utils;

import com.coding91.parser.BuildConfigContent;
import static com.coding91.parser.ConfigParser.itemIdAndItemName;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FileUtils {

    /**
     * 写入文件
     *
     * @param contents
     * @param descFile
     * @param encoding
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeToFile(String contents, String descFile, String encoding) throws FileNotFoundException, IOException {
        File fileOutput = new File(descFile);
        writeToFile(contents, fileOutput, "UTF-8");
    }

    /**
     * 写入文件
     *
     * @param contents
     * @param descFile
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeToFile(String contents, File descFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        writeToFile(contents, descFile, "UTF-8");
    }

    /**
     * 写入文件
     *
     * @param contents
     * @param descFile
     * @param encoding
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     */
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

    public String getItemName(String itemId, String lang) {
        itemIdAndItemName = BuildConfigContent.buildItemIdAndItemName();
        return itemIdAndItemName.get(itemId).get(lang);
    }

    /**
     * 
     * @param filePath
     * @return 
     */
    public static Map<String, String> loadFieldDefaultValueProperty(String filePath) {
        HashMap finalResult = new HashMap();

        String finalFilePath = FileUtils.class.getClassLoader().getResource(filePath).toString();
        finalFilePath = finalFilePath.substring(6);//删除字符串卡头的 file:/
        File f = new File(finalFilePath);
        if (f.exists()) {
            Properties prop = new Properties();
            try {
                prop.load(new InputStreamReader(FileUtils.class.getClassLoader().getResourceAsStream(filePath), "UTF-8"));
            } catch (IOException ex) {
                //showMessageDialogMessage(ex);//todo
            }
            Set<Map.Entry<Object, Object>> propertyEntrySet = prop.entrySet();
            for(Map.Entry<Object, Object> currentProperty : propertyEntrySet) {
                finalResult.put(currentProperty.getKey().toString(), currentProperty.getValue().toString());
            }
        }
        return finalResult;
    }
    
    public static Map<String, String> loadSetting(String filePath) {
        String configBaseDir = "", outputDirectory = "";
        HashMap finalResult = new HashMap();

        String finalFilePath = FileUtils.class.getClassLoader().getResource(filePath).toString();
        finalFilePath = finalFilePath.substring(6);//删除字符串卡头的 file:/
        File f = new File(finalFilePath);
        if (f.exists()) {
            Properties prop = new Properties();
            try {
                prop.load(new InputStreamReader(FileUtils.class.getClassLoader().getResourceAsStream(filePath), "UTF-8"));
            } catch (IOException ex) {
                //showMessageDialogMessage(ex);//todo
            }
            if (!prop.getProperty("configBaseDir", "").isEmpty()) {
                configBaseDir = prop.getProperty("configBaseDir");

            }
            if (!prop.getProperty("outputDirectory", "").isEmpty()) {
                outputDirectory = prop.getProperty("outputDirectory");
            }

            finalResult.put("configBaseDir", configBaseDir);
            finalResult.put("outputDirectory", outputDirectory);
        }
        return finalResult;
    }
}
