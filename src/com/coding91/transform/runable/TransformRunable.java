package com.coding91.transform.runable;

import com.coding91.logic.ParseConfigLogic;
import static com.coding91.logic.TransformConfigLogic.getModel;
import static com.coding91.parser.ConfigParser.getLangs;
import com.coding91.transform.thread.TransformCommonContentThread;
import com.coding91.ui.NoticeMessageJFrame;
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
public class TransformRunable implements Runnable {

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
    public TransformRunable(String configFilePath, String sheetName, String outputPath, String fileName, String idField, Map<String, Map> extraParams, long startTime) {
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
            if (extraParams.containsKey("combineFields")) {//有些字段是有多个字段合并来的
                combineFields = (Map<String, String[]>) extraParams.get("combineFields");
            } else {
                combineFields = null;
            }

            final Map<String, Map<String, List>> modelInfo = getModel(originContent[0], combineFields);

            String[] langList = getLangs();
            for (final String currentLang : langList) {
                final String[][] singleLangContent;
                if (combineFields != null) {
                    Map<String, Map<String, List>> fullModelInfo = getModel(originContent[0]);
                    singleLangContent = ParseConfigLogic.cleanupOriginContent(originContent, currentLang, fullModelInfo, combineFields);
                } else {
                    singleLangContent = originContent;
                }
                TransformCommonContentThread tcce = new TransformCommonContentThread(outputPath, fileName, modelInfo, singleLangContent, idField, extraParams);
                Thread currentThread = tcce.transformCommonThread(currentLang);
                currentThread.start();
                threadList.add(currentThread);
            }

            threadList.stream().forEach((Thread thread) -> { //等待所有线程结束
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
                }
            });

            long endTime = System.currentTimeMillis();
            long diff = endTime - startTime;
            NoticeMessageJFrame.noticeMessage(fileName + "转换完成。耗时:" + DateTimeUtils.formatTimeDuration(diff) + "\r\n\r\n");
//            ConfigParser.transformFinish("完成转换!");
        } catch (IOException | BiffException ex) {
            NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
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
