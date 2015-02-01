/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coding91.utils;

/**
 *
 * @author Administrator
 */
public class StringUtils {

    /**
     * 字符串替换
     *
     * @param originSource
     * @param search
     * @param replacement
     * @return
     */
    public static String replace(String originSource, String search, String replacement) {
        if (originSource == null) {
            return null;
        }
        int i = 0;
        if ((i = originSource.indexOf(search, i)) >= 0) {
            char[] cSrc = originSource.toCharArray();
            char[] cTo = replacement.toCharArray();
            int len = search.length();
            StringBuilder buf = new StringBuilder(cSrc.length);
            buf.append(cSrc, 0, i).append(cTo);
            i += len;
            int j = i;
            while ((i = originSource.indexOf(search, i)) > 0) {
                buf.append(cSrc, j, i - j).append(cTo);
                i += len;
                j = i;
            }
            buf.append(cSrc, j, cSrc.length - j);
            return buf.toString();
        }
        return originSource;
    }
    
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }
}
