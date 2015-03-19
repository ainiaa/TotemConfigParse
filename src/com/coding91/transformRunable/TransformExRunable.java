/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.transformRunable;

import static com.coding91.logic.TransformConfigLogic.getModel;
import com.coding91.parser.ConfigParser;
import static com.coding91.parser.ConfigParser.getLangs;
import static com.coding91.parser.ConfigParser.showMessageDialogMessage;
import com.coding91.transformThread.TransformCommonContentThread;
import com.coding91.utils.DateTimeUtils;
import com.coding91.utils.ExcelParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jxl.read.biff.BiffException;

/**
 *
 * @author Administrator
 */
public class TransformExRunable implements Runnable {

    @Override
    public void run() {
        try {
            int sheetIndex = ExcelParser.getSheetIndexBySheetName(configFilePath, sheetName);
            final String[][] commonContent = ExcelParser.parseXls(configFilePath, sheetIndex, true);

            final Map<String, Map<String, List<String>>> modelInfo = getModel(commonContent[0]);
            String[] langList = getLangs();
            TransformCommonContentThread transformCommonContentThread = new TransformCommonContentThread();
            transformCommonContentThread.setCommonContent(commonContent);//commonContent
            transformCommonContentThread.setDefaultValue(defalutValue);
            transformCommonContentThread.setFileName(fileName);//fileName
            transformCommonContentThread.setIdField(idField);//idField
            transformCommonContentThread.setModelInfo(modelInfo);//modelInfo
            transformCommonContentThread.setOutputPath(outputPath);//outputPath
            transformCommonContentThread.setSpecialField(specialField);//specialField 
            for (final String currentLang : langList) {
                // start single lang 
                Thread currentThread = transformCommonContentThread.transformCommonThread(currentLang);
                currentThread.start();
                threadList.add(currentThread);
                //end single lang
            }

            boolean allThreadFinished;
            do {
                allThreadFinished = false;
                try {
                    for (Thread t : threadList) {
                        if (t.getState() != Thread.State.TERMINATED) {
                            allThreadFinished = false;
                            break;
                        } else {
                            allThreadFinished = true;
                        }
                    }
                    Thread.sleep(1000);//停止1s再执行
                } catch (InterruptedException ex) {
                    showMessageDialogMessage(ex);
                }
            } while (!allThreadFinished);//
            long endTime = System.currentTimeMillis();
            long diff = endTime - startTime;
            ConfigParser.notifyMessage("转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff));
            ConfigParser.transformFinish("完成转换!");
        } catch (IOException ex) {
            showMessageDialogMessage(ex);
        } catch (BiffException ex) {
            showMessageDialogMessage(ex);
        }
    }

    private String configFilePath;
    private String sheetName;
    private String outputPath;
    private String fileName;
    private String idField;
    private Map specialField;
    private Map defalutValue;
    private long startTime;
    private List<Thread> threadList = new ArrayList();

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<Thread> getThreadList() {
        return threadList;
    }

    public void setThreadList(List<Thread> threadList) {
        this.threadList = threadList;
    }

    public Map getDefalutValue() {
        return defalutValue;
    }

    public void setDefalutValue(Map defalutValue) {
        this.defalutValue = defalutValue;
    }
}
