/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totemconfigparse;

import java.util.Map;

/**
 *
 * @author Administrator
 */
public class TransformCommonContentParam {

    private String configFilePath;
    private String outputPath;
    private String fileName;
    private String sheetName;
    private String idField;
    private Map<String, String> specialField;
    private String keys;
    private String contentSplitFragment;
    private Map<String, String> fieldDefaultValue;

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
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

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public Map<String, String> getSpecialField() {
        return specialField;
    }

    public void setSpecialField(Map<String, String> specialField) {
        this.specialField = specialField;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getContentSplitFragment() {
        return contentSplitFragment;
    }

    public void setContentSplitFragment(String contentSplitFragment) {
        this.contentSplitFragment = contentSplitFragment;
    }

    public Map<String, String> getFieldDefaultValue() {
        return fieldDefaultValue;
    }

    public void setFieldDefaultValue(Map<String, String> fieldDefaultValue) {
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public TransformCommonContentParam(String configFilePath, String outputPath, String fileName, String idField, Map<String, String> specialField, String keys, String contentSplitFragment, Map<String, String> fieldDefaultValue, String sheetName) {
        this.configFilePath = configFilePath;
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.idField = idField;
        this.specialField = specialField;
        this.keys = keys;
        this.contentSplitFragment = contentSplitFragment;
        this.sheetName = sheetName;
    }

    public TransformCommonContentParam(String configFilePath, String outputPath, String fileName, String idField, Map<String, String> specialField, String keys, String contentSplitFragment, Map<String, String> fieldDefaultValue) {
        this.configFilePath = configFilePath;
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.idField = idField;
        this.specialField = specialField;
        this.keys = keys;
        this.contentSplitFragment = contentSplitFragment;
        this.sheetName = "Worksheet";
    }

    public TransformCommonContentParam(String configFilePath, String outputPath, String fileName, String sheetName, String idField, Map<String, String> specialField) {
        this.configFilePath = configFilePath;
        this.outputPath = outputPath;
        this.fileName = fileName;
        this.idField = idField;
        this.specialField = specialField;
        this.keys = "";
        this.contentSplitFragment = null;
        this.sheetName = "Worksheet";
    }

}
