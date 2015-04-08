package com.coding91.utils;

/*   
 * 文件名:     ZipUtils.java   
 * 版权：        xxxxxxxx.com. Copyright 1999-2010, All rights reserved   
 * 描述：       是压缩工具类,此类根据com.jcraft.jzlib地三方提供的核心类进行.压缩和解压缩。   
 * 修改人：        
 * 修改时间：   2010-09-13   
 * 跟踪单号：       
 * 修改单号：       
 * 修改内容：    新增   
 可以到google是去下载jzlib4me20100516.rar 也就是jzlib4me的google项目为第三方支持包.   
 这个ZipUtil.java的zlib支持J2ME.也就是将zlib的压缩和解压缩的两个方法可以放到J2ME项目中.但也需要jzlib4me20100516.rar包.  
 */
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;

/**
 * 压缩工具包
 */
public class ZipUtils {

    public static byte[] deflate(byte[] bContent) {
        try {
            String sys_compress = "LZIP";
            byte[] temp = null;

            switch (sys_compress) {
                case "LZIP":
                    temp = ZipUtils.zLib(bContent);
                    break;
                case "GZIP":
                    temp = ZipUtils.gZip(bContent);
                    break;
                case "ZIP":
                    temp = ZipUtils.zip(bContent);
                    break;
            }
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] inflate(byte[] bContent) {
        try {
            String sys_compress = "LZIP";
            byte[] temp = null;
            switch (sys_compress) {
                case "LZIP":
                    temp = ZipUtils.unZLib(bContent);
                    break;
                case "GZIP":
                    temp = ZipUtils.unGZip(bContent);
                    break;
                case "ZIP":
                    temp = ZipUtils.unZip(bContent);
                    break;
            }
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 输入数据的最大长度    
    private static final int MAXLENGTH = 1024000000;

    // 设置缓存大小    
    private static final int BUFFERSIZE = 1024;

    // 压缩选择方式：    
    //    
    // /** Try o get the best possible compression */    
    // public static final int COMPRESSION_MAX = JZlib.Z_BEST_COMPRESSION;    
    //    
    // /** Favor speed over compression ratio */    
    // public static final int COMPRESSION_MIN = JZlib.Z_BEST_SPEED;    
    //    
    // /** No compression */    
    // public static final int COMPRESSION_NONE = JZlib.Z_NO_COMPRESSION;    
    //    
    // /** Default compression */    
    // public static final int COMPRESSION_DEFAULT =    
    // JZlib.Z_DEFAULT_COMPRESSION;    
    /**
     * ZLib压缩数据
     *
     * @param bContent
     * @return
     * @throws IOException
     */
    public static byte[] zLib(byte[] bContent) throws IOException {

        byte[] data = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ZOutputStream zOut = new ZOutputStream(out,
                    JZlib.Z_BEST_COMPRESSION); // 压缩级别,缺省为1级    
            DataOutputStream objOut = new DataOutputStream(zOut);
            objOut.write(bContent);
            objOut.flush();
            zOut.close();
            data = out.toByteArray();
            out.close();

        } catch (IOException e) {
            throw e;
        }
        return data;
    }

    /**
     * ZLib解压数据
     *
     * @param bContent
     * @return
     * @throws IOException
     */
    public static byte[] unZLib(byte[] bContent) throws IOException {

        byte[] data = new byte[MAXLENGTH];
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bContent);
            ZInputStream zIn = new ZInputStream(in);
            DataInputStream objIn = new DataInputStream(zIn);

            int len = 0;
            int count = 0;
            while ((count = objIn.read(data, len, len + BUFFERSIZE)) != - 1) {
                len = len + count;
            }

            byte[] trueData = new byte[len];
            System.arraycopy(data, 0, trueData, 0, len);

            objIn.close();
            zIn.close();
            in.close();

            return trueData;

        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * GZip压缩数据
     *
     * @param bContent
     * @return
     * @throws java.io.IOException
     * @returnException
     */
    public static byte[] gZip(byte[] bContent) throws IOException {

        byte[] data = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            GZIPOutputStream gOut = new GZIPOutputStream(out, bContent.length);  // 压缩级别,缺省为1级    
            DataOutputStream objOut = new DataOutputStream(gOut);
            objOut.write(bContent);
            objOut.flush();
            gOut.close();
            data = out.toByteArray();
            out.close();

        } catch (IOException e) {
            throw e;
        }
        return data;
    }

    /**
     * GZip解压数据
     *
     * @param bContent
     * @return
     * @throws java.io.IOException
     * @returnException
     */
    public static byte[] unGZip(byte[] bContent) throws IOException {

        byte[] data = new byte[MAXLENGTH];
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bContent);
            GZIPInputStream pIn = new GZIPInputStream(in);
            DataInputStream objIn = new DataInputStream(pIn);

            int len = 0;
            int count;
            while ((count = objIn.read(data, len, len + BUFFERSIZE)) != - 1) {
                len = len + count;
            }

            byte[] trueData = new byte[len];
            System.arraycopy(data, 0, trueData, 0, len);

            objIn.close();
            pIn.close();
            in.close();

            return trueData;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * *
     * 压缩Zip
     *
     * @param bContent
     * @return
     * @throws IOException
     */
    public static byte[] zip(byte[] bContent) throws IOException {

        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bContent.length);
            zip.putNextEntry(entry);
            zip.write(bContent);
            zip.closeEntry();
            zip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * *
     * . * 解压Zip
     *
     * @param bContent
     * @return
     * @throws IOException
     */
    public static byte[] unZip(byte[] bContent) throws IOException {
        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bContent);
            ZipInputStream zip = new ZipInputStream(bis);
            while (zip.getNextEntry() != null) {
                byte[] buf = new byte[1024];
                int num;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((num = zip.read(buf, 0, buf.length)) != - 1) {
                    baos.write(buf, 0, num);
                }
                b = baos.toByteArray();
                baos.flush();
                baos.close();
            }
            zip.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public static String arrayImplode(String[] content, String implode) {
        String finalStr = "";
        for (int index = 0; index < content.length; index++) {
            if (index != content.length - 1 && index != 0) {
                finalStr += implode + content[index];
            } else {
                finalStr = content[index];
            }
        }

        return finalStr;
    }

    public static void writeToFile(String contents, File descFile, String encoding) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        if (!descFile.getParentFile().exists()) {
            if (!descFile.getParentFile().mkdirs()) {
                JOptionPane.showMessageDialog(null, "创建目录文件所在的目录失败", "信息提示", JOptionPane.ERROR_MESSAGE);
                System.out.println("创建目录文件所在的目录失败！");
            }
        }
        if (!descFile.exists()) {
            descFile.createNewFile();
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descFile), encoding));
        writer.write(contents);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {
        try {
            String input = "水电费his大家fks打飞机速度快放假了速度快放假速度发生的飞机上的考虑防静电速度开飞机上打开了房间速度快让他文件";
            byte[] compressed = new byte[100];
            Deflater compresser = new Deflater();
//        Inflater compressers = new Inflater();
            System.out.println("Charset.forName:" + Charset.forName("utf8"));
            System.out.println("Charset.xx:" + new String(input.getBytes(Charset.forName("utf8"))));
            compresser.setInput(input.getBytes(Charset.forName("utf8")));
            compresser.finish();
            compresser.deflate(compressed);
            File test_file = new File(System.getProperty("user.dir"), "test_file");
            System.out.println("user.dir :" + System.getProperty("user.dir"));
            try {
                if (!test_file.exists()) {
                    test_file.createNewFile();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(test_file);
                    fos.write(compressed);
                } catch (Exception e) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
