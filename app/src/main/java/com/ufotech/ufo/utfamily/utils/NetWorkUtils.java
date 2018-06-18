package com.ufotech.ufo.utfamily.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 07:46
 *  @description ：   各種工具類
 *  -----------------------------------------------------------------------------------------
 */
public class NetWorkUtils {

    /**
     * 檢查網路是否可用
     * @param context 上下文
     * @return 是或否
     */
    public static boolean isNetworkAvailable(Context context) {
        if(context !=null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = Objects.requireNonNull(cm).getActiveNetworkInfo();
            if(info !=null){
                return info.isAvailable();
            }
        }
        return false;
    }


    /**
     * 將圖片 Set 到 ImageView 上
     *
     * @param imgView ImageView 的元件 ID
     * @param imgUrl 圖片網址，String 格式
     */
    public static void convert(final ImageView imgView, String imgUrl) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                String url = params[0];
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                imgView.setImageBitmap (result);
                super.onPostExecute(result);
            }
        }.execute(imgUrl);
    }
    /**
     * 讀取網路圖片，型態為Bitmap
     *
     * @param imgUrl 圖片網址
     * @return Bitmap格式的圖片
     */
    private static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 向指定URL發送GET方法的請求
     *
     * @param url
     * 發送請求的URL
     * @param param
     * 請求參數，請求參數應該是name1=value1&name2=value2的形式。
     * @return URL所代表遠程資源的響應
     */
    public static  String sendGet(String url, String param) {
        String result =  "" ;
        BufferedReader in =  null ;
        try  {
            String urlName = url +  "?"  + param;
            URL realUrl =  new  URL(urlName);
            // 打開和URL之間的連接
            URLConnection conn = realUrl.openConnection();
            // 設置通用的請求屬性
            conn.setRequestProperty( "accept" ,  "*/*" );
            conn.setRequestProperty( "connection" ,  "Keep-Alive" );
            conn.setRequestProperty( "user-agent" ,
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)" );
            // 建立實際的連接
            conn.connect();
            // 獲取所有響應頭字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍歷所有的響應頭字段
            for  (String key : map.keySet()) {
                System.out.println(key +  "--->"  + map.get(key));
            }
            // 定義BufferedReader輸入流來讀取URL的響應
            in =  new  BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while  ((line = in.readLine()) !=  null ) {
                result +=  "/n"  + line;
            }
        }  catch  (Exception e) {
            System.out.println( "發送GET請求出現異常！"  + e);
            e.printStackTrace();
        }
        // 使用finally塊來關閉輸入流
        finally  {
            try  {
                if  (in !=  null ) {
                    in.close();
                }
            }  catch  (IOException ex) {
                ex.printStackTrace();
            }
        }
        return  result;
    }
    /**
     * 向指定URL發送POST方法的請求
     *
     *  @param  url
     * 發送請求的URL
     *  @param  param
     * 請求參數，請求參數應該是name1=value1&name2=value2的形式。
     *  @return  URL所代表遠程資源的響應
     */
    public static  String sendPost(String url, String param) {
        PrintWriter out =  null ;
        BufferedReader in =  null ;
        String result =  "" ;
        try  {
            URL realUrl =  new  URL(url);
            // 打開和URL之間的連接
            URLConnection conn = realUrl.openConnection();
            // 設置通用的請求屬性
            conn.setRequestProperty( "accept" ,  "*/*" );
            conn.setRequestProperty( "connection" ,  "Keep-Alive" );
            conn.setRequestProperty( "user-agent" ,
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)" );
            // 發送POST請求必須設置如下兩行
            conn.setDoOutput( true );
            conn.setDoInput( true );
            // 獲取URLConnection對像對應的輸出流
            out =  new  PrintWriter(conn.getOutputStream());
            // 發送請求參數
            out.print(param);
            // flush輸出流的緩衝
            out.flush();
            // 定義BufferedReader輸入流來讀取URL的響應
            in =  new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while  ((line = in.readLine()) !=  null ) {
                result +=  "/n"  + line;
            }
        }  catch  (Exception e) {
            System.out.println( "發送POST請求出現異常！"  + e);
            e.printStackTrace();
        }
        // 使用finally塊來關閉輸出流、輸入流
        finally  {
            try  {
                if  (out !=  null ) {
                    out.close();
                }
                if  (in !=  null ) {
                    in.close();
                }
            }  catch  (IOException ex) {
                ex.printStackTrace();
            }
        }
        return  result;
    }


    /**
     * Created by rick.wu on 2016/6/16.
     * @description：從外部分享連結至APP
     */
    public static String sharingIntent(Context context, Intent intent){
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                return handleSendText(intent);
            } else if (type.startsWith("image/")) {

                return handleSendImage(context, intent);
            }
        }
        return "";
    }
    private static String handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            return sharedText;
        }
        return "";
    }
    private static String handleSendImage(Context context, Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            if(imageUri.getPath().endsWith(".jpg") || imageUri.getPath().endsWith(".png")){
                return imageUri.getPath();
            }
            return getRealPathFromURI(context, imageUri);
        }
        return "";
    }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
