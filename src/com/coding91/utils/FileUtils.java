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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FileUtils {
    public static void writeToFile(String contents, String descFile, String encoding) throws FileNotFoundException, IOException {
        File fileOutput = new File(descFile);
        writeToFile(contents, fileOutput, "UTF-8");
    }
    public static void writeToFile(String contents, File descFile) throws UnsupportedEncodingException, FileNotFoundException, IOException {
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
    
    public String getItemName(String itemId, String lang) {
        itemIdAndItemName = BuildConfigContent.buildItemIdAndItemName();
        return itemIdAndItemName.get(itemId).get(lang);
    }
}
