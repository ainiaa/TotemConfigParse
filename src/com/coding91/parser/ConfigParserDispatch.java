package com.coding91.parser;

import com.coding91.logic.TransformConfigLogic;
import com.coding91.utils.FileUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jeff Liu
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
        String sheetName = "Worksheet";
        String idField;
        String fileName;
        Map<String, Map<String, ?>> specialField = new HashMap();
        Map defaultValue = new HashMap();
        if ("DS_SHOP_OBJ_ITEM".equals(func)) {//shopItem
            TransformConfigLogic.transformShopObjectItem(configFilePath, outputPath);
        } else if ("ACTIVITY_LIB".equals(func)) {//activityLibraryInfo @todo 看看是不是好用
            Map activityInfoParam = new HashMap();
            activityInfoParam.put("contentKey", new String[]{"activity_type", "activity_id"});
            activityInfoParam.put("contentSeparator", new String[]{",", ":"});
            activityInfoParam.put("parseFunction", "parseCommonMultipleEx");
            specialField.put("activity_info", activityInfoParam);

            Map unlockRecipeParam = new HashMap();
            unlockRecipeParam.put("contentKey", new String[]{});
            unlockRecipeParam.put("contentSeparator", new String[]{","});
            unlockRecipeParam.put("parseFunction", "parseCommonMultipleEx");
            specialField.put("unlockRecipe", unlockRecipeParam);

            fileName = "activityLibraryInfo";
            idField = "id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, specialField);
        } else if ("AVATAR_ITEMS".equals(func)) {//avatarItems todo 有数据错乱的情况 需要后续处理
            Map suiteArrayParam = new HashMap();
            suiteArrayParam.put("contentKey", new String[]{});
            suiteArrayParam.put("contentSeparator", new String[]{","});
            suiteArrayParam.put("parseFunction", "parseCommonMultipleEx");
            specialField.put("suite_array", suiteArrayParam);
            fileName = "avatarItems";
            idField = "item_id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, specialField);
        } else if ("FEED_INFO".equals(func)) {//feedInfo done
            fileName = "feedInfo";
            idField = "feed_id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, specialField);
        } else if ("GOODS_ORDER".equals(func)) {//goodsOrder done
            fileName = "goodsOrder";
            idField = "id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, specialField);
        } else if ("REQUEST_INFO".equals(func)) {//requestInfo done
            fileName = "requestInfo";
            idField = "request_id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, specialField);
        } else if ("BINDING_RECIPE".equals(func)) {//bindingRecipe @todo 有数据错乱的情况 需要后续处理
            Map recipesParam = new HashMap();
            recipesParam.put("contentKey", new String[]{});
            recipesParam.put("contentSeparator", new String[]{"|", ","});
            recipesParam.put("parseFunction", "parseCommonMultipleEx");
            specialField.put("recipes", recipesParam);

            Map activateDataParam = new HashMap();
            activateDataParam.put("contentKey", new String[]{"activate_id", "activate_item_id", "activate_num", "activate_type"});
            activateDataParam.put("contentSeparator", new String[]{"|", ",", ":"});
            activateDataParam.put("parseFunction", "parseCommonMultipleEx");
            specialField.put("activate_data", activateDataParam);

            fileName = "bindingRecipe";
            idField = "item_id";

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, specialField);

        } else if ("GAME".equals(func)) {//game todo 还没有处理
            fileName = "game";
            Map randomShelfProductListParam = new HashMap();
            randomShelfProductListParam.put("contentKey", new String[]{});
            randomShelfProductListParam.put("contentSeparator", new String[]{"|", ","});
            randomShelfProductListParam.put("parseFunction", "parseCommonMultipleEx");
            specialField.put("randomShelfProductList", randomShelfProductListParam);
            
            Map rankScoreRewardsParam = new HashMap();
            rankScoreRewardsParam.put("contentKey", new String[]{});
            rankScoreRewardsParam.put("contentSeparator", new String[]{"|", ","});
            rankScoreRewardsParam.put("parseFunction", "parseGameRankScoreRewards");
            specialField.put("randomShelfProductList", randomShelfProductListParam);
            
//            specialField.put("randomShelfProductList", "parseCommonMultiple@3");
//            specialField.put("rankScoreRewards", "parseGameRankScoreRewards@3");
            TransformConfigLogic.transformCommonSingleFileContent(configFilePath, outputPath, fileName, sheetName, specialField);
        } else if ("MISSION_INFO".equals(func)) {//mission Info  @todo 还有一写文件没有生成
//            specialField.put("reward_data", "parseMissionInfoRewardData@3");
//            specialField.put("mission_require", "parseMissionInfoMissionRequire@3");
//            fileName = "missionInfo";
//            idField = "mission_id";
//            String keys = "activity_type,activity_id";
//            String contentSplitFragment = "|!,";
//            Map<String, String[]> combineFields = new HashMap();
//            combineFields.put("mission_require", new String[]{"mission_sub_require", "mission_sub_require_desc", "mission_sub_require_tips"});
//            TransformConfigLogic.transformMissionContent(configFilePath, outputPath, fileName, sheetName, idField, specialField, keys, contentSplitFragment, combineFields);
        } else if ("DESSERT_INFO".equals(func)) {//@todo 需要

        }
    }
}
