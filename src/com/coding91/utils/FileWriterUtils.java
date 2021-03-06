/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.utils;

import com.coding91.ui.NoticeMessageJFrame;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Administrator
 */
public class FileWriterUtils {

    /**
     * A方法追加文件：使用RandomAccessFile
     *
     * @param fileName
     * @param content
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            // 文件长度，字节数
            try ( // 打开一个随机访问文件流，按读写方式
                    RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw")) {
                // 文件长度，字节数
                long fileLength = randomFile.length();
                //将写文件指针移到文件尾。
                randomFile.seek(fileLength);
                randomFile.writeBytes(content);
            }
        } catch (IOException ex) {
            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            try ( //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                    FileWriter writer = new FileWriter(fileName, true)) {
                writer.write(content);
            }
        } catch (IOException ex) {
            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        String fileName = "C:/temp/newTemp.txt";
        String content = "new append!";
        //按方法A追加文件
        FileWriterUtils.appendMethodA(fileName, content);
        FileWriterUtils.appendMethodA(fileName, "append end. \n");
        //显示文件内容
        FileReaderUtils.readFileByLines(fileName);
        //按方法B追加文件
        FileWriterUtils.appendMethodB(fileName, content);
        FileWriterUtils.appendMethodB(fileName, "append end. \n");
        //显示文件内容
        FileReaderUtils.readFileByLines(fileName);
    }

}
