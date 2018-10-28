package cn.szvone.img.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//========搜狗图床========
public class SinaApi {

    public static String uploadImg(String imgBase64,String ck) {


        String url = "http://picupload.service.weibo.com/interface/pic_upload.php?mime=image%2Fjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog";



        String ret = "";

        URL u = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        //尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Cookie", ck);


            PrintWriter printWriter = new PrintWriter(con.getOutputStream());
            // 发送请求参数
            printWriter.write("b64_data="+ URLEncoder.encode(imgBase64));//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();

            //读取返回内容
            inputStream = con.getInputStream();


            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufr = new BufferedReader(isr);
            String str;



            while ((str = bufr.readLine()) != null) {
                ret+=str;
            }

            ret = VoneUtil.getSubString(ret,"\"name\":\"pic_1\",\"pid\":\"","\"}");

            if (!ret.equals("")){
                ret = "http://wx1.sinaimg.cn/large/"+ret+".jpg";
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }



    public static String login(String user,String pass){

        String url = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)&_=";
        String post = "entry=sso&gateway=1&from=null&savestate=30&useticket=0&pagerefer=&vsnf=1"
                    + "&su="+Base64.encode(user.getBytes())+"&service=sso&sp="+pass
                    + "&sr=1024*768&encoding=UTF-8&cdult=3&domain=sina.com.cn&prelt=0&returntype=TEXT";
        String ret = "";
        URL u = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        //尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);


            PrintWriter printWriter = new PrintWriter(con.getOutputStream());
            printWriter.write(post);
            printWriter.flush();

            //读取返回内容
            inputStream = con.getInputStream();


            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufr = new BufferedReader(isr);
            String str;



            while ((str = bufr.readLine()) != null) {
                ret+=str;
            }

            //获取cookie
            Map<String, List<String>> map=con.getHeaderFields();
            Set<String> set=map.keySet();
            for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                //System.out.println(key);
                if (key!=null && key.equals("Set-Cookie")) {
                    //System.out.println("key=" + key+",开始获取cookie");
                    List<String> list = map.get(key);
                    StringBuilder builder = new StringBuilder();
                    for (String str1 : list) {
                        builder.append(str1).toString();
                    }
                    ret=builder.toString();
                    //System.out.println("得到的cookie="+ret);
                }
            }

            ret = VoneUtil.getSubString(ret,"SUB=",";");
            if (!ret.equals("")){
                ret = "SUB="+ret;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }


    public static void main(String[] args) {
        //System.out.println(login("13040753884","601514119"));

        //System.out.println("["+uploadImg("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")+"]");
    }
}