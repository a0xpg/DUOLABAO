package com.hualong.duolabao.tool;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Administrator on 2018-06-25.
 */
@SuppressWarnings("unchecked")
public class FileBean {

    /**
     * 把成字符串的内容转化输出流
     *
     * @param
     * @return
     */
    public static InputStream getStringStream(String sInputString) {

        if (sInputString != null && !sInputString.trim().equals("")) {

            try {

                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());

                return tInputStringStream;

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }

        return null;

    }

    /**
     * 把输入流的内容转化成字符串
     *
     * @param is
     * @return
     */
    public static String readInputStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            is.close();
            baos.close();
            //或者用这种方法
            //byte[] result=baos.toByteArray();
            //return new String(result);
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    public static void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    /**
     * 将图片文件转为字符串
     *
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //String imgFile = "d:\\111.jpg";// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 将图片文件转为byte数字
     *
     * @param imgFile
     * @return
     */
    public static byte[] getImageByte(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //String imgFile = "d:\\111.jpg";// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回Base64编码过的字节数组字符串
        return data;
    }

    /**
     * 将字符串转为图片
     *
     * @param imgStr
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFile) throws Exception {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] b = decoder.decodeBuffer(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        // 生成jpeg图片
        String imgFilePath = imgFile;// 新生成的图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();
        return true;
    }

    /**
     * 图片是否符合 jpg gjf png格式
     *
     * @param
     * @return
     */
    public static boolean isRightFormat(String format) {

        return (format.equals("jpg") || format.equals("gif") || format.equals("png")) ? true : false;
    }

    /**
     * 对图片进行放大
     *
     * @param originalImage 原始图片
     * @param times         放大倍数
     * @return
     */

    public static BufferedImage zoomInImage(BufferedImage originalImage, Integer times) {

        int width = originalImage.getWidth() * times;
        int height = originalImage.getHeight() * times;

        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 对图片进行放大
     *
     * @param srcPath 原始图片路径(绝对路径)
     * @param newPath 放大后图片路径（绝对路径）
     * @param times   放大倍数
     * @return 是否放大成功
     */

    public static boolean zoomInImage(String srcPath, String newPath, Integer times) {

        BufferedImage bufferedImage = null;
        try {

            File of = new File(srcPath);
            if (of.canRead()) {

                bufferedImage = ImageIO.read(of);

            }
        } catch (Exception e) {
            //TODO: 打印日志
            return false;
        }
        if (bufferedImage != null) {

            bufferedImage = zoomInImage(bufferedImage, times);
            try {
                //TODO: 这个保存路径需要配置下子好一点
                //保存修改后的图像,全部保存为JPG格式
                ImageIO.write(bufferedImage, "JPG", new File(newPath));
            } catch (IOException e) {
                // TODO 打印错误信息
                return false;
            }
        }
        return true;

    }

    /**
     * 对图片进行缩小
     *
     * @param originalImage 原始图片
     * @param times         缩小倍数
     * @return 缩小后的Image
     */
    public static BufferedImage zoomOutImage(BufferedImage originalImage, Integer times) {
        int width = originalImage.getWidth() / times;
        int height = originalImage.getHeight() / times;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 对图片进行放大
     *
     * @param srcPath 原始图片路径(绝对路径)
     * @param newPath 放大后图片路径（绝对路径）
     * @param times   放大倍数
     * @return 是否放大成功
     */

    public static boolean zoomOutImage(String srcPath, String newPath, Integer times) {
        BufferedImage bufferedImage = null;
        try {
            File of = new File(srcPath);
            if (of.canRead()) {
                bufferedImage = ImageIO.read(of);
            }
        } catch (IOException e) {
            //TODO: 打印日志
            return false;
        }
        if (bufferedImage != null) {
            bufferedImage = zoomOutImage(bufferedImage, times);
            try {
                //TODO: 这个保存路径需要配置下子好一点
                //保存修改后的图像,全部保存为JPG格式
                ImageIO.write(bufferedImage, "JPG", new File(newPath));
            } catch (IOException e) {
                // TODO 打印错误信息
                return false;
            }
        }
        return true;
    }

    //图片到byte数组
    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    //byte数组到图片
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    //byte数组到16进制字符串
    public static String byte2string(byte[] data) {
        if (data == null || data.length <= 1) return "0x";
        if (data.length > 200000) return "0x";
        StringBuffer sb = new StringBuffer();
        int buf[] = new int[data.length];
        //byte数组转化成十进制
        for (int k = 0; k < data.length; k++) {
            buf[k] = data[k] < 0 ? (data[k] + 256) : (data[k]);
        }
        //十进制转化成十六进制
        for (int k = 0; k < buf.length; k++) {
            if (buf[k] < 16) sb.append("0" + Integer.toHexString(buf[k]));
            else sb.append(Integer.toHexString(buf[k]));
        }
        return "0x" + sb.toString().toUpperCase();
    }

    public static void main(String[] args) throws IOException {
//        String file=readInputStream(
//                new FileInputStream(
//                        new File("C:\\Users\\Administrator\\Desktop\\why\\baishang.png")
//                ));
//        System.out.println(file);
//        InputStream stream=getStringStream(file);
//        inputstreamtofile(stream,new File("C:\\Users\\Administrator\\Desktop\\why\\baishang9.png"));
//
//
//        boolean testIn = zoomInImage("E:/11.jpg","E:\\in.jpg",4);
//        if(testIn){
//            System.out.println("in ok");
//        }
//        boolean testOut = zoomOutImage("E:/11.jpg","E:\\out.jpg",4);
//        if(testOut){
//            System.out.println("out ok");
//        }
//        String str=getImageStr("C:\\Users\\Administrator\\Desktop\\why\\baishang.png");
//        System.out.println(str);
//        try {
//            generateImage(str,"C:\\Users\\Administrator\\Desktop\\why\\baishang10.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}