package com.coding91.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import jxl.read.biff.BiffException;
import com.coding91.utils.ArrayUtils;
import com.coding91.utils.ExcelParserUtils;
import com.coding91.utils.FileUtils;

/**
 *
 * @author Administrator
 */
public class ConfigParser {

    /**
     * Creates new form TotemConfigParseJFrame
     */
    public ConfigParser() {
        Map<String, String> configMap = FileUtils.loadSetting("resources/data/setting.properties");
        configBaseDir = configMap.get("configMap");
        outputDirectory = configMap.get("outputDirectory");
    }

    private Map<String, String> initFuncList() {
        Map funcList = new HashMap();
        funcList.put("DS_SHOP_OBJ_ITEM", "shopItem.xls");
        funcList.put("ACTIVITY_LIB", "activityLibraryInfo.xls");
        funcList.put("AVATAR_ITEMS", "avatarItems.xls");
        funcList.put("FEED_INFO", "feedInfo.xls");
        funcList.put("GOODS_ORDER", "goodsOrder.xls");
        funcList.put("REQUEST_INFO", "requestInfo.xls");
        funcList.put("GAME", "game.xls");
        funcList.put("MISSION_INFO", "missionInfo.xls");
        funcList.put("BINDING_RECIPE", "bindingRecipe.xls");
        funcList.put("DESSERT_INFO", "dessertInfo.xls");
        return funcList;
    }

    public static String[][] itemLangInfoCfg;
    public static String[][] itemBaseInfoCfg;
    public static HashMap<String, HashMap<String, String>> itemIdAndItemName;
    public static HashMap<String, HashMap<String, String>> collectActivityCommonConf;

    public String[][] itemLangInfoCfg(String configFilePath) {
        try {
            int langSheetIndex = ExcelParserUtils.getSheetIndexBySheetName(configFilePath, "itemLang");
            itemLangInfoCfg = ExcelParserUtils.parseXls(configFilePath, langSheetIndex, true);
        } catch (IOException e) {
            itemLangInfoCfg = new String[0][0];
            showMessageDialogMessage(e);
        } catch (BiffException e) {
            itemLangInfoCfg = new String[0][0];
            showMessageDialogMessage(e);
        }
        return itemLangInfoCfg;
    }

    public String[] getItemLangs(String configFilePath) {
        String langs[];
        try {
            String[][] itemLangCfg = itemLangInfoCfg(configFilePath);
            String[] itemLangModelName = ArrayUtils.getModelFromStringArray(itemLangCfg);
            langs = ArrayUtils.arraySlice(itemLangModelName, 1);
        } catch (Exception e) {
            langs = new String[0];
            showMessageDialogMessage(e);
        }
        return langs;
    }

    public static String[] itemLangs;

    public String[] getItemLangFileNames(String configFilePath) {
        Map<String, String> fileNameMap = new HashMap<String, String>();
        fileNameMap.put("Chinese", "zh_tw");
        fileNameMap.put("English", "en_us");
        fileNameMap.put("German", "de_de");
        fileNameMap.put("Dutch", "nl_nl");
        fileNameMap.put("French", "fr_fr");
        fileNameMap.put("GermanSe", "de_se");
        fileNameMap.put("DutchSe", "nl_se");
        fileNameMap.put("FrenchSe", "fr_se");
        fileNameMap.put("Japanese", "ja_jp");

        String langs[] = getItemLangs(configFilePath);
        for (int index = 0; index < langs.length; index++) {
            langs[index] = fileNameMap.get(langs[index]);
        }
        itemLangs = langs;
        return langs;
    }

    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }

    public static void showErrorMsg(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, null);
        System.exit(0);
    }

    public static void transformFinish(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static int getFieldIndexByFieldName(List<String> fileNameList, List<Integer> fileIndexList, String fieldName) {
        int finalIndex = -1;
        int len = fileNameList.size();
        for (int i = 0; i < len; i++) {
            if (fileNameList.get(i).equals(fieldName)) {
                finalIndex = fileIndexList.get(i);
                break;
            }
        }
        return finalIndex;
    }

    public static int getFieldIndexByFieldName(List<String> modelInfo, String fieldName) {
        int finalIndex = -1;
        int len = modelInfo.size();
        for (int i = 0; i < len; i++) {
            if (modelInfo.get(i).equals(fieldName)) {
                finalIndex = i;
                break;
            }
        }
        return finalIndex;
    }

    public static void notifyMessage(String msg) {
        System.out.println("notifyMessage :" + msg);
    }

    public static String[] getLangs() {
        return new String[]{"zh_tw", "de_de", "fr_fr", "en_us", "es_es"};
    }

    public static String getInitials(String s) {
        return getInitials(s, true);
    }

    public static String getInitials(String s, boolean withFirstLetter) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        s = sb.toString();
        sb = new StringBuilder();

        if (withFirstLetter) {
            sb.append(s.charAt(0));
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println(" ==========");
    }

    public static void showMessageDialogMessage(Exception ex) {
        String exMsg = ex.toString();
        JOptionPane.showMessageDialog(null, exMsg + new Throwable().getStackTrace()[1].toString(), "错误信息提示", JOptionPane.ERROR_MESSAGE);
    }

    private String configBaseDir;
    private String outputDirectory;
}
