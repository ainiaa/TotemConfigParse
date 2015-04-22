/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.test;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Administrator
 */
public class StringUtilsTest {

    public static void main(String[] args) {
        String origin = "  ";
        origin = StringUtils.repeat(origin, 2);
        System.out.println("a" + origin + "b");
    }
}
