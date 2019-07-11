package com.hualong.duolabao.tool;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018-01-07.
 */
public class getPostBody {

    public static String postBody(String strURL,String params,String tocken) {

        System.out.println(strURL);
        try {
            URL url = new URL(strURL+tocken);// 创建连接
            HttpURLConnection connection = (HttpURLConnection)
                    url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");// 设置请求方式
            connection.setRequestProperty("Accept","application/json");// 设置接收数据的格式
            connection.setRequestProperty("Content-Type","application/json");// 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream(),"GBK");// utf-8编码
            out.append(params); out.flush(); out.close(); // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1){
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0; int destPos = 0;
                while ((readLen = is.read(temp)) > 0){
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "GBK");
                System.out.println(result);
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "error";

    }

    public static String getBody(String strURL,String params,String tocken) {

        System.out.println(strURL);
        System.out.println(params);
        try {
            URL url = new URL(strURL+tocken);// 创建连接
            HttpURLConnection connection = (HttpURLConnection)
                    url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");// 设置请求方式
            connection.setRequestProperty("Accept","application/json");// 设置接收数据的格式
            connection.setRequestProperty("Content-Type","application/json");// 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream(),"UTF-8");// utf-8编码
            out.append(params); out.flush(); out.close(); // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1){
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0; int destPos = 0;
                while ((readLen = is.read(temp)) > 0){
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8");
                System.out.println(result);
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "error";

    }

    public static void main(String[] args) {

//        String result=postBody("https://api.weixin.qq.com/card/user/getcardlist?access_token=","{\n" +
//                "  \"openid\": \"gh_48536e411705\",\n" +
//                "  \"card_id\": \"pZJo4w9jUCCK_w0nZy3kJY2nnbQ0\"\n" +
//                "}","5_12JFI0kX6D83iDciBBF4R9PFvOPyOw0evSPup3eZT7cZqp8wBh1S00_QzOhWQnqq1q-jwUFbtpkDL_ApfaJ9sQ8zO_xv1bqK_pZQ4VcX9IRDXBmGLHwbLecTG5MSqcLZef_s_PHh-fbUzwRTXNAhAIAEUH");
//
//        result="["+result+"]";
//        JSONArray array = new JSONArray(result);
//        JSONObject obj = array.getJSONObject(0);
//
//        System.out.println(obj.toString());


        //http://www.shangbenkeji.com:8888/OpenApi/UserApi/AddUserFromBody
        String result=postBody("http://www.shangbenkeji.com:8888/OpenApi/UserApi/AddUserFromBody","贾巨成","");

        result="["+result+"]";
        System.out.println(result.toString());
//        JSONArray array = new JSONArray(result);
//        JSONObject obj = array.getJSONObject(0);
//
//        System.out.println(obj.toString());

    }


}
