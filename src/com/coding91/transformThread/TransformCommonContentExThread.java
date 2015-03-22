/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.transformThread;

import com.coding91.logic.BuildConfigLogic;
import com.coding91.parser.BuildConfigContent;
import com.coding91.parser.ConfigParser;
import static com.coding91.parser.ConfigParser.showMessageDialogMessage;
import com.coding91.utils.FileUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class TransformCommonContentExThread extends Thread {

    public TransformCommonContentExThread(String outputPath, String fileName, Map<String, Map<String, List<String>>> modelInfo, String[][] commonContent, String idField, Map<String, Map<String, String>> specialField) {
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.modelInfo = modelInfo;
        this.commonContent = commonContent;
        this.idField = idField;
        this.specialField = specialField;
    }

    public Thread transformCommonThread(final String currentLang) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                for (int i = 1; i < commonContent.length; i++) {
                    BuildConfigContent bcc = new BuildConfigContent();
                    Map<String, String> singleRowInfo = bcc.buildSingleRowStrEx(commonContent[i], modelInfo, currentLang, idIndex, idField, specialField);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty()) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                        } catch (FileNotFoundException ex) {
                            showMessageDialogMessage(ex);
                        } catch (IOException ex) {
                            showMessageDialogMessage(ex);
                        }
                        if (i == 1) {//第一行 没有必要添加\r\n
                            allContent.append(currentAllItemInfo);
                        } else {
                            allContent.append("\r\n").append(currentAllItemInfo);
                        }
                        ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + outputPath);
                    }
                }
                try {
                    ConfigParser.notifyMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + outputPath);
                    String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
                    FileUtils.writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                } catch (FileNotFoundException ex) {
                    showMessageDialogMessage(ex);
                } catch (IOException ex) {
                    showMessageDialogMessage(ex);
                }
            }
        });
        return currentThread;
    }

    private String outputPath;
    private String fileName;
    private Map<String, Map<String, List<String>>> modelInfo;
    private String[][] commonContent;
    private String idField;
    private Map specialField;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Map<String, List<String>>> getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(Map<String, Map<String, List<String>>> modelInfo) {
        this.modelInfo = modelInfo;
    }

    public String[][] getCommonContent() {
        return commonContent;
    }

    public void setCommonContent(String[][] commonContent) {
        this.commonContent = commonContent;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public Map getSpecialField() {
        return specialField;
    }

    public void setSpecialField(Map specialField) {
        this.specialField = specialField;
    }
}
