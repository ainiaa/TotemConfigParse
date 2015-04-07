package com.coding91.utils;

/**
 *
 * @author Administrator
 */
public class ConvertFunction implements IFunction {

    public final int PRE_ARGS_NUM = 2;   //默认参数个数，根据需要自行修改
    public final Class<?>[] PRE_ARGS_TYPE = new Class<?>[]{String.class, String.class};   //默认的参数的类型，根据需要自行修改

    public int convert_if_exist(String name, String value, String field1, String field2) { //可修改成自个的业务逻辑
        System.out.println("name = " + name);
        System.out.println("value = " + value);
        System.out.println(field1 + " " + field2);
        System.out.println("convert_if_exist(String name, String value, String field1, String field2) =================================" );
        return 0;
    }

    public int convert_if_exist(String name, String value, String field1, String field2, String field3) {//可修改成自己的业务逻辑
        System.out.println("name = " + name);
        System.out.println("value = " + value);
        System.out.println(field1 + " " + field2 + " " + field3);
        System.out.println("convert_if_exist(String name, String value, String field1, String field2, String field3) =================================" );
        return 0;
    }//要添加函数，均在这个类中添加并配置进配置文件中即可
}
