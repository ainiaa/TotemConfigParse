/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.transformRunable;

import com.coding91.logic.ParseConfigLogic;
import static com.coding91.logic.TransformConfigLogic.getModel;
import com.coding91.parser.ConfigParser;
import static com.coding91.parser.ConfigParser.getLangs;
import static com.coding91.parser.ConfigParser.showMessageDialogMessage;
import com.coding91.transformThread.TransformCommonContentExThread;
import com.coding91.utils.DateTimeUtils;
import com.coding91.utils.ExcelParserUtils;
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

    /**
     *
     * @param configFilePath
     * @param sheetName
     * @param outputPath
     * @param fileName
     * @param idField
     * @param extraParams
     * @param startTime
     */
    public TransformExRunable(String configFilePath, String sheetName, String outputPath, String fileName, String idField, Map<String, Map> extraParams, long startTime) {
        this.configFilePath = configFilePath;
        this.sheetName = sheetName;
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.idField = idField;
        this.extraParams = extraParams;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        try {
            int sheetIndex = ExcelParserUtils.getSheetIndexBySheetName(configFilePath, sheetName);
            final String[][] originContent = ExcelParserUtils.parseXls(configFilePath, sheetIndex, true);

            Map<String, String[]> combineFields;
            if (extraParams.containsKey("combineFields")) {
                combineFields = (Map<String, String[]>) extraParams.get("combineFields");
            } else {
                combineFields = null;
            }

            final Map<String, Map<String, List>> modelInfo = getModel(originContent[0], combineFields);
            final Map<String, Map<String, List>> fullModelInfo;
            if (combineFields != null) {
                fullModelInfo = getModel(originContent[0]);
            } else {
                fullModelInfo = modelInfo;
            }
            String[] langList = getLangs();
            for (final String currentLang : langList) {
                final String[][] singleLangContent;
                if (combineFields != null) {
                    singleLangContent = ParseConfigLogic.cleanupOriginContent(originContent, currentLang, fullModelInfo, combineFields);//todo 不同的配置项逻辑可以不同， 这个可以整理出来
                } else {
                    singleLangContent = originContent;
                }
                TransformCommonContentExThread tcce = new TransformCommonContentExThread(outputPath, fileName, modelInfo, singleLangContent, idField, extraParams);
                Thread currentThread = tcce.transformCommonThread(currentLang);
                currentThread.start();
                threadList.add(currentThread);
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
                    Thread.sleep(500);//停止0.5s再执行
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
    private Map extraParams;
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
        return extraParams;
    }

    public void setSpecialField(Map specialField) {
        this.extraParams = specialField;
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
}
