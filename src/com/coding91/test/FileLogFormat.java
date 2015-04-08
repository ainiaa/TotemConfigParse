package com.coding91.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileLogFormat {

    public static void main(String[] args) {
        BufferedReader reader;
        File file = new File("D:/www/tmp/10.183.131.236-20150309-php_2701sell.log");
        String root = "d:/www/tmp/";
        Map<String, FileWriter> fileWriteMap = new HashMap();
        String[] typeArray = new String[]{"tid"};//$act, $value, $value2 , $tid (map_id), $item
        try {
            reader = new BufferedReader(new FileReader(file));
            String partter;
            FileWriter currentWriter;
            while ((partter = reader.readLine()) != null) {
                //act,value,value2,tid,item
                Map<String, String> info = formatMsg(partter);
//                System.out.println(info.get("msg"));
                for (String type : typeArray) {
                    String fileName = generateFileName(info, root, type);

                    if (fileWriteMap.containsKey(fileName)) {
                        currentWriter = fileWriteMap.get(fileName);
                    } else {
                        File tmpFile = new File(fileName);
                        if (!tmpFile.exists()) {
                            tmpFile.getParentFile().mkdirs();//先创建父文件夹 然后在创建文件 要不然会抛异常
                            tmpFile.createNewFile();
                        }
                        currentWriter = new FileWriter(tmpFile, true);
                        fileWriteMap.put(fileName, currentWriter);
                    }
                    currentWriter.write(info.get("msg") + "\r\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileLogFormat.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fileWriteMap.entrySet().stream().forEach((entry) -> {
                try {
                    entry.getValue().close();
                } catch (IOException ex) {
                    Logger.getLogger(FileLogFormat.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * 文件
     *
     * @param info
     * @param root
     * @param type
     * @return
     */
    public static String generateFileName(Map<String, String> info, String root, String type) {
        String uid = info.get("uid");
        type = info.get(type);
        return root + "/" + uid + "/" + type + ".txt";
    }

    public static Map<String, String> formatMsg(String originMsg) {
        String[] flagment = originMsg.split("\\] \\[");
        StringBuilder finalMsg = new StringBuilder();
        String uid = "", time = "", act = "", tid = "";
        for (int i = 0; i < flagment.length; i++) {
            if (i == 0) {
                flagment[i] = flagment[i].replace("[", "");
                time = flagment[i];
            } else if (i == flagment.length - 1) {
                flagment[i] = flagment[i].replace("]", "");
                flagment[i] = flagment[i].replace("{\"msg\":\"", "");
                flagment[i] = flagment[i].replace("\"}", "");
                flagment[i] = flagment[i].replace(" ", ",");
                String[] info = flagment[i].split(",");
                uid = info[1];//uid
                act = info[2];//act
                tid = info[5];//map_id
            }

            finalMsg.append(flagment[i]);
            if (i != flagment.length - 1) {
                finalMsg.append("|");
            }
//            System.out.println(String.valueOf(i) + ":" + flagment[i]);
        }
//        System.out.println(finalMsg.toString());
        Map<String, String> finalInfo = new HashMap();
        finalInfo.put("msg", finalMsg.toString());
        finalInfo.put("uid", uid);
        finalInfo.put("serverTime", time);
        finalInfo.put("act", act);
        finalInfo.put("tid", tid);
        return finalInfo;
    }

    public static void connectToMysql() {
        String driver = "com.mysql.jdbc.Driver";

        // URL指向要访问的数据库名scutcs
        String url = "jdbc:mysql://localhost:3306/test";

        // MySQL配置时的用户名
        String user = "root";

        // Java连接MySQL配置时的密码
        String password = "";

        try {

            // 加载驱动程序
            Class.forName(driver);

            // 连续数据库
            Connection conn = DriverManager.getConnection(url, user, password);

            if (!conn.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            }

            // statement用来执行SQL语句
            Statement statement = conn.createStatement();

            // 要执行的SQL语句
            String sql = "select * from message";

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FileLogFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
