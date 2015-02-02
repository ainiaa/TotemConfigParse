package com.coding91.parser;

import com.coding91.logic.TransformConfigLogic;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ConfigParserDispatch {
    /**
     * 转换单个excel
     *
     * @param func
     * @param configFilePath
     * @param outputPath
     */
    public static void transformSingleExcel(String func, String configFilePath, String outputPath) {
        String msg = "";
        if (func.isEmpty()) {
            msg = "请选择解析内容 ";
        } else if (configFilePath.isEmpty()) {
            msg = "请选择待解析文件(xls) ";
        } else if (outputPath.isEmpty()) {
            msg = "请选择输出路径";
        }
        if (!msg.isEmpty()) {
            JOptionPane.showMessageDialog(null, msg, "信息提示", JOptionPane.ERROR_MESSAGE);
        } else {
            String sheetName = "Worksheet";
            String idField;
            String fileName;
            Map specialField = new HashMap();
            Map defaultValue = new HashMap();
            if ("DS_SHOP_OBJ_ITEM".equals(func)) {//shopItem
                TransformConfigLogic.transformShopObjectItem(configFilePath, func, outputPath);
            } else if ("ACTIVITY_LIB".equals(func)) {//activityLibraryInfo @todo 看看是不是好用
                specialField.put("activity_info", "parseCommonMultipleWithKeyValue@4");
                specialField.put("unlockRecipe", "parseCommonMultiple@4");
                fileName = "activityLibraryInfo";
                idField = "id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = ",!:";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment, defaultValue);
            } else if ("AVATAR_ITEMS".equals(func)) {//avatarItems
                specialField.put("suite_array", "parseCommonMultiple@3");
                fileName = "avatarItems";
                idField = "item_id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("FEED_INFO".equals(func)) {//feedInfo
                fileName = "feedInfo";
                idField = "feed_id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("GOODS_ORDER".equals(func)) {//goodsOrder
                fileName = "goodsOrder";
                idField = "id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("REQUEST_INFO".equals(func)) {//request_id
                fileName = "requestInfo";
                idField = "request_id";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField);
            } else if ("GAME".equals(func)) {//game
                fileName = "game";
                specialField.put("randomShelfProductList", "parseCommonMultiple@3");
                specialField.put("rankScoreRewards", "parseGameRankScoreRewards@3");
                TransformConfigLogic.transformCommonSingleFileContent(configFilePath, outputPath, fileName, sheetName, specialField);
            } else if ("BINDING_RECIPE".equals(func)) {//bindingRecipe @todo 看看是不是好用
                specialField.put("recipes", "parseCommonMultiple@4");
                specialField.put("activate_data", "parseBindingRecipeActivateData@3");
                fileName = "bindingRecipe";
                idField = "item_id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = "|!,";
                TransformConfigLogic.transformCommonContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment, defaultValue);
            } else if ("MISSION_INFO".equals(func)) {//mission Info  @todo 还有一写文件没有生成
                specialField.put("reward_data", "parseMissionInfoRewardData@3");
                specialField.put("mission_require", "parseMissionInfoMissionRequire@3");
                fileName = "missionInfo";
                idField = "mission_id";
                String keys = "activity_type,activity_id";
                String contentSplitFragment = "|!,";
                Map<String, String[]> combineFields = new HashMap();
                combineFields.put("mission_require", new String[]{"mission_sub_require", "mission_sub_require_desc", "mission_sub_require_tips"});
                TransformConfigLogic.transformMissionContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment, combineFields);
            } else if ("DESSERT_INFO".equals(func)) {//@todo 需要

            }
        }
    }
}
