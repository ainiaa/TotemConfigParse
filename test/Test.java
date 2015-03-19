
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static String DELETE_DOING = "DEL RDIS DOING FAILURE(ALL) ";
    public static String DELETE_FINISH = "DEL RDIS FINISH FAILURE(ALL) ";
    public static String DELETE_DOING_SUB = "HDEL RDIS DOING FAILURE ";
    public static String DELETE_FINISH_SUB = "HDEL RDIS FINISH FAILURE ";
    public static String SET_DOING_SUB = "HSET RDIS DOING FAILURE,";
    public static String SET_FINISH_SUB = "HSET RDIS FINISH FAILURE,";
    public static String DELETE_USER_EX_SUB = "HDEL RDIS USER_EX FAILURE,";

    public static void main(String[] args) {
//        String originField = "de_de_item_name";
//        String prefix = originField.substring(0, 5);
//        String currentFiled = originField.substring(6);
//        System.out.println("prefix:" + prefix + "  currentFiled:" + currentFiled);

//        FileWriter fw = null;
//        int totalRows = 2000000;
//        try {
//            fw = new FileWriter("d:/uid.txt");
//            for (int i = 0; i < totalRows; i++) {
//                fw.write("100000374596806,10\r\n");
//            }
//
//            fw.close();
//        } catch (IOException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Map<String, String> fileNameMapping = new HashMap();
//        fileNameMapping.put(DELETE_DOING, "deleteDoing.txt");
//        fileNameMapping.put(DELETE_FINISH, "deleteFinish.txt");
//        fileNameMapping.put(DELETE_DOING_SUB, "deleteDoingSub.txt");
//        fileNameMapping.put(DELETE_FINISH_SUB, "deleteFinishSub.txt");
//        fileNameMapping.put(SET_DOING_SUB, "setDoingSub.txt");
//        fileNameMapping.put(SET_FINISH_SUB, "setFinishSub.txt");
//        fileNameMapping.put(DELETE_USER_EX_SUB, "deleteUserEx.txt");
//
//        readFileByLines("d:/www/tmp/log.txt", fileNameMapping);
        //====================================
        String originContent = "115:49201:4:0,116:49202:4:0,113:50134:5:1,116:50135:5:1|117:49203:4:0,118:49204:4:0,119:50136:5:1,122:50137:5:1|119:49205:4:0,120:49206:4:0,125:50138:5:1,128:50139:5:1";
        String[] flagment = new String[]{"|", ",", ":"};
        //String[] contentKey = new String[]{"activate_id", "activate_item_id", "activate_num", "activate_type"};
        String[] contentKey = new String[]{};
        int index = 0;
        String content = parseCommonMultipleEx(originContent, flagment, contentKey, index);
        System.out.println(content);
    }

    public static String parseCommonMultipleEx(String originContent, String[] flagment, String[] contentKey, int index) {
        StringBuilder finalContent = new StringBuilder();
        finalContent.append("array(");
        if (!originContent.isEmpty()) {//内容不为空
            String[] contentChunk = originContent.split("\\" + flagment[index]);
            if (flagment.length == index + 1) {//已经是最后一层了
                if (contentKey.length > 0) {//最后一层需要将内容和key对应起来 key1 => 'key1content',key2 => 'key1content2',...
                    for (int i = 0; i < contentKey.length; i++) {
                        String content = "";
                        if (i <= contentChunk.length - 1) {
                            content = contentChunk[i];
                        }
                        finalContent.append(String.format("'%s'=>'%s',",  contentKey[i], content));
                    }
                } else {//直接使用逗号分割放入array()中即可
                    for (String content : contentChunk) {
                        finalContent.append(String.format("'%s',", content));
                    }
                }
            } else {
                ++index;
                for (String currentChunk : contentChunk) {
                    finalContent.append(parseCommonMultipleEx(currentChunk, flagment, contentKey, index)).append(",");
                }
            }
        }
        finalContent.append(")");
        return finalContent.toString();
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName
     * @param fileNameMapping
     */
    public static void readFileByLines(String fileName, Map<String, String> fileNameMapping) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            FileWriter deleteDoingWriter = null;
            FileWriter deleteFinishWriter = null;
            FileWriter deleteDoingSubWriter = null;
            FileWriter deleteFinishSubWriter = null;
            FileWriter setDoingSubWriter = null;
            FileWriter setFinishSubWriter = null;
            FileWriter deleteSuserExSubWriter = null;
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                tempString = tempString.trim();
                if (tempString.startsWith(DELETE_DOING)) {
                    tempString = tempString.replace(DELETE_DOING, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteDoingWriter == null) {
                            deleteDoingWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_DOING));
                        }
                        if (info.length == 3) {
                            deleteDoingWriter.write(info[0] + "," + info[2] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_FINISH)) {
                    tempString = tempString.replace(DELETE_FINISH, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteFinishWriter == null) {
                            deleteFinishWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_FINISH));
                        }
                        if (info.length == 3) {
                            deleteFinishWriter.write(info[0] + "," + info[2] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_DOING_SUB)) {
                    tempString = tempString.replace(DELETE_DOING_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteDoingSubWriter == null) {
                            deleteDoingSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_DOING_SUB));
                        }
                        if (info.length == 4) {
                            deleteDoingSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_FINISH_SUB)) {
                    tempString = tempString.replace(DELETE_FINISH_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteFinishSubWriter == null) {
                            deleteFinishSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_FINISH_SUB));
                        }
                        if (info.length == 4) {
                            deleteFinishSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(SET_DOING_SUB)) {
                    tempString = tempString.replace(SET_DOING_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (setDoingSubWriter == null) {
                            setDoingSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(SET_DOING_SUB));
                        }
                        if (info.length == 4) {
                            setDoingSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(SET_FINISH_SUB)) {
                    tempString = tempString.replace(SET_FINISH_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (setFinishSubWriter == null) {
                            setFinishSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(SET_FINISH_SUB));
                        }
                        if (info.length == 4) {
                            setFinishSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                } else if (tempString.startsWith(DELETE_USER_EX_SUB)) {
                    tempString = tempString.replace(DELETE_USER_EX_SUB, "");
                    if (!tempString.isEmpty()) {
                        String[] info = tempString.split(",");
                        if (deleteSuserExSubWriter == null) {
                            deleteSuserExSubWriter = new FileWriter(LOG_BASE_DIR + fileNameMapping.get(DELETE_USER_EX_SUB));
                        }
                        if (info.length == 4) {
                            deleteSuserExSubWriter.write(info[0] + "," + info[2] + "," + info[3] + "\r\n");
                        } else {
                            System.out.println("line " + line + ": " + tempString + " info len:" + info.length);
                        }
                    }
                }
//                System.out.println("line " + line + ": " + tempString);
                line++;
            }

            if (deleteDoingWriter != null) {
                deleteDoingWriter.close();
            }
            if (deleteFinishWriter != null) {
                deleteFinishWriter.close();
            }
            if (deleteDoingSubWriter != null) {
                deleteDoingSubWriter.close();
            }
            if (deleteFinishSubWriter != null) {
                deleteFinishSubWriter.close();
            }
            if (setDoingSubWriter != null) {
                setDoingSubWriter.close();
            }
            if (setFinishSubWriter != null) {
                setFinishSubWriter.close();
            }
            if (deleteSuserExSubWriter != null) {
                deleteSuserExSubWriter.close();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    private static final String LOG_BASE_DIR = "d:/www/tmp/";
}
