package com.coding91.transform.thread;

import com.coding91.logic.BuildConfigLogic;
import com.coding91.parser.BuildConfigContent;
import com.coding91.parser.ConfigParser;
import com.coding91.ui.NoticeMessageJFrame;
import com.coding91.utils.FileUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class TransformCommonContentThread extends Thread {

    public TransformCommonContentThread(String outputPath, String fileName, Map<String, Map<String, List>> modelInfo, String[][] commonContent, String idField, Map<String, Map<String, String>> extraParams) {
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.modelInfo = modelInfo;
        this.commonContent = commonContent;
        this.idField = idField;
        this.extraParams = extraParams;
    }

    public Thread transformCommonThread(final String currentLang) {
        Thread currentThread = new Thread(new Runnable() {
            @Override
            public void run() {

                int idIndex;
                if (idField != null) {
                    idIndex = ConfigParser.getFieldIndexByFieldName(modelInfo.get("fieldName").get(currentLang), idField);
                } else {
                    idIndex = -1;
                }

                StringBuilder allContent = new StringBuilder();
                allContent.append(" return array (\r\n");
                NoticeMessageJFrame.setTotalFileCountMap(currentLang + ":" + fileName, commonContent.length - 1);
                for (int i = 1; i < commonContent.length; i++) {
//                    NoticeMessageJFrame.setCurrentProcessing(1);
                    Map<String, String> singleRowInfo = BuildConfigContent.buildSingleRowString(commonContent[i], modelInfo, currentLang, idIndex, idField, extraParams);
                    String id = singleRowInfo.get(idField);
                    if (!id.isEmpty() && idField != null) {//空id 直接无视
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String currentAllItemInfo = singleRowInfo.get("allRowsInfo");
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                        } catch (FileNotFoundException ex) {
                            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                        } catch (IOException ex) {
                            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                        }
                        if (i == 1) {//第一行 没有必要添加\r\n
                            allContent.append(currentAllItemInfo);
                        } else {
                            allContent.append("\r\n").append(currentAllItemInfo);
                        }
                        NoticeMessageJFrame.noticeMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + descFile);
                    } else if (idField == null) {
                        String singleItemInfo = singleRowInfo.get("singleRowInfo");
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, id, outputPath, fileName, fileName);
                        try {
                            FileUtils.writeToFile("<?php\r\n return " + singleItemInfo, descFile, "UTF-8");
                        } catch (FileNotFoundException ex) {
                            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                        } catch (IOException ex) {
                            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                        }
                        NoticeMessageJFrame.noticeMessage("语言：" + currentLang + "完成度:" + (i * 100 / commonContent.length) + "%|正在生成文件:" + descFile);
                    }
                }
                try {
                    if (idField != null) {
                        String descFile = BuildConfigLogic.buildSingleRowStoredPath(currentLang, "", outputPath, fileName, fileName);
//                        NoticeMessageJFrame.setCurrentProcessing(1);
                        NoticeMessageJFrame.noticeMessage("语言：" + currentLang + "完成度:" + "100%|正在生成文件:" + descFile);
                        System.out.println("######################################################" + fileName + "100%");
                        FileUtils.writeToFile("<?php\r\n" + allContent.toString() + "\r\n);", descFile, "UTF-8");
                    }
                } catch (FileNotFoundException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                } catch (IOException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                }
            }
        });
        return currentThread;
    }

    public Map getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map extraParams) {
        this.extraParams = extraParams;
    }

    private String outputPath;
    private String fileName;
    private Map<String, Map<String, List>> modelInfo;
    private String[][] commonContent;
    private String idField;
    private Map extraParams;

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

    public Map<String, Map<String, List>> getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(Map<String, Map<String, List>> modelInfo) {
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
        return extraParams;
    }

    public void setSpecialField(Map specialField) {
        this.extraParams = specialField;
    }
}
