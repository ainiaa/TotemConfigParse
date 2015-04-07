/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.utils;

import com.coding91.ui.NoticeMessageJFrame;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ReflectMethod {

    public static void main(String[] args) {
        ConvertFunction convert = new ConvertFunction();
        List<Func> dst = new ArrayList();
        String funcStr = "convert_if_exist(\"1\",\"2\");convert_if_exist(\"a\",\"b\",\"c\");";//假设从数据库中读出出来了
		Utils.initFunctions(convert, dst, funcStr);
        for (int i = 0; i < dst.size(); i++) {
            try {
                dst.get(i).call("defaultKeya", "defaultValueb");//调用，默认的两个参数此时传入，和数据库配置中的field1,field2无关，视业务而定
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                // TODO Auto-generated catch block
               NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
            }
        }
    }
}
