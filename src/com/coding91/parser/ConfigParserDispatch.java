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
        Map<String, Map> extraParams = new HashMap();
        Map globalDefaultValueMap = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/global.properties");
        extraParams.put("globalDefaultValue", globalDefaultValueMap);
        if ("DS_SHOP_OBJ_ITEM".equals(func)) {//shopItem done
            Map activityInfoParam = new HashMap();
            activityInfoParam.put("contentKey", new String[]{"require_item_id", "require_item_num", "require_type", "require_id"});
            activityInfoParam.put("contentSeparator", new String[]{",", ":"});
            activityInfoParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("item_property", activityInfoParam);

            Map marketPurchaseLimitParam = new HashMap();
            marketPurchaseLimitParam.put("contentKey", new String[]{"level", "purchase_limit_num"});
            marketPurchaseLimitParam.put("contentSeparator", new String[]{",", ":"});
            marketPurchaseLimitParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("market_purchase_limit", marketPurchaseLimitParam);

            Map activateDataParam = new HashMap();
            activateDataParam.put("contentKey", new String[]{"activate_id", "activate_item_id", "activate_num", "activate_type"});
            activateDataParam.put("contentSeparator", new String[]{",", ":"});
            activateDataParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("activate_data", activateDataParam);

            Map recipeListParam = new HashMap();
            recipeListParam.put("contentKey", new String[]{});
            recipeListParam.put("contentSeparator", new String[]{","});
            recipeListParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("recipe_list", recipeListParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/shopItem.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            fileName = "shopItem";
            idField = "item_id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("ACTIVITY_LIB".equals(func)) {//activityLibraryInfo done
            Map activityInfoParam = new HashMap();
            activityInfoParam.put("contentKey", new String[]{"activity_type", "activity_id"});
            activityInfoParam.put("contentSeparator", new String[]{",", ":"});
            activityInfoParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("activity_info", activityInfoParam);

            Map unlockRecipeParam = new HashMap();
            unlockRecipeParam.put("contentKey", new String[]{});
            unlockRecipeParam.put("contentSeparator", new String[]{","});
            unlockRecipeParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("unlockRecipe", unlockRecipeParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/activityLibraryInfo.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            fileName = "activityLibraryInfo";
            idField = "id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("AVATAR_ITEMS".equals(func)) {//avatarItems done
            Map suiteArrayParam = new HashMap();
            suiteArrayParam.put("contentKey", new String[]{});
            suiteArrayParam.put("contentSeparator", new String[]{","});
            suiteArrayParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("suite_array", suiteArrayParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/avatarItems.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            fileName = "avatarItems";
            idField = "item_id";
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("FEED_INFO".equals(func)) {//feedInfo done
            fileName = "feedInfo";
            idField = "feed_id";

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/feedInfo.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("GOODS_ORDER".equals(func)) {//goodsOrder done
            fileName = "goodsOrder";
            idField = "id";
            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/goodsOrder.properties");
            extraParams.put("defaultValue", fieldDefaultPair);
            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("REQUEST_INFO".equals(func)) {//requestInfo done
            fileName = "requestInfo";
            idField = "request_id";

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/requestInfo.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("BINDING_RECIPE".equals(func)) {//bindingRecipe done
            Map recipesParam = new HashMap();
            recipesParam.put("contentKey", new String[]{});
            recipesParam.put("contentSeparator", new String[]{"|", ","});
            recipesParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("recipes", recipesParam);

            Map activateDataParam = new HashMap();
            activateDataParam.put("contentKey", new String[]{"activate_id", "activate_item_id", "activate_num", "activate_type"});
            activateDataParam.put("contentSeparator", new String[]{"|", ",", ":"});
            activateDataParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("activate_data", activateDataParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/bindingRecipe.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            fileName = "bindingRecipe";
            idField = "item_id";

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("GAME".equals(func)) {//game done
            fileName = "game";
            Map randomShelfProductListParam = new HashMap();
            randomShelfProductListParam.put("contentKey", new String[]{});
            randomShelfProductListParam.put("contentSeparator", new String[]{","});
            randomShelfProductListParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("randomShelfProductList", randomShelfProductListParam);

            Map rankScoreRewardsParam = new HashMap();
            rankScoreRewardsParam.put("contentKey", new String[]{});
            rankScoreRewardsParam.put("contentSeparator", new String[]{"|", ",", ":"});
            rankScoreRewardsParam.put("parseFunction", "parseGameRankScoreRewards");
            extraParams.put("rankScoreRewards", rankScoreRewardsParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/game.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, null, extraParams);
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
        } else if ("DESSERT_INFO".equals(func)) {//@dessertInfo done
            Map ingredientDataParam = new HashMap();
            ingredientDataParam.put("contentKey", new String[]{"ingredient_id", "ingredient_num", "require_type", "require_id", "is_skippable"});
            ingredientDataParam.put("contentSeparator", new String[]{",", ":"});
            ingredientDataParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("ingredient_data", ingredientDataParam);

            Map condimentsDataParam = new HashMap();
            condimentsDataParam.put("contentKey", new String[]{"condiments_id", "condiments_num", "require_type", "require_id", "is_skippable"});
            condimentsDataParam.put("contentSeparator", new String[]{",", ":"});
            condimentsDataParam.put("parseFunction", "parseCommonMultipleEx");
            extraParams.put("condiments_data", condimentsDataParam);

            Map normalDessertDataParam = new HashMap();
            normalDessertDataParam.put("contentKey", new String[]{});
            normalDessertDataParam.put("contentSeparator", new String[]{"|", ":"});
            normalDessertDataParam.put("parseFunction", "parseDessertInfoNormalDessertData");
            extraParams.put("normal_dessert_data", normalDessertDataParam);//todo 

            Map condimentsDessertDataParam = new HashMap();
            condimentsDessertDataParam.put("contentKey", new String[]{});
            condimentsDessertDataParam.put("contentSeparator", new String[]{",", "|", ":"});
            condimentsDessertDataParam.put("parseFunction", "parseDessertInfoCondimentsDessertData");
            extraParams.put("condiments_dessert_data", condimentsDessertDataParam);//todo

            Map levelUpCookTimesParam = new HashMap();
            levelUpCookTimesParam.put("contentKey", new String[]{});
            levelUpCookTimesParam.put("contentSeparator", new String[]{",", ":"});
            levelUpCookTimesParam.put("parseFunction", "parseDessertInfoLevelUpCookTimes");
            extraParams.put("level_up_cook_times", levelUpCookTimesParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/dessertInfo.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            fileName = "dessertInfo";
            idField = "recipe_id";

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        } else if ("GIFT_PACKAGE".equals(func)) {//GIFT_PACKAGE
            Map fixedDataParam = new HashMap();
            fixedDataParam.put("contentKey", new String[]{});
            fixedDataParam.put("contentSeparator", new String[]{",", ":"});
            fixedDataParam.put("parseFunction", "parseGiftPackageFixData");
            extraParams.put("fixed_data", fixedDataParam);

            Map<String, String> fieldDefaultPair = FileUtils.loadFieldDefaultValueProperty("resources/data/config/defaultvalue/giftPackage.properties");
            extraParams.put("defaultValue", fieldDefaultPair);

            fileName = "giftPackage";
            idField = "id";

            TransformConfigLogic.transformCommonContentEx(configFilePath, outputPath, fileName, sheetName, idField, extraParams);
        }
    }
}
