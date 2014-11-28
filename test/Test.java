/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Test {

    public static void main(String[] args) {
        String originField = "de_de_item_name";
        String prefix = originField.substring(0, 5);
        String currentFiled = originField.substring(6);
        System.out.println("prefix:" + prefix + "  currentFiled:" + currentFiled);
    }
}
