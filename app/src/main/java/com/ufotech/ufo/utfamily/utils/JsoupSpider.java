package com.ufotech.ufo.utfamily.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 07:45
 *  @description ：   Jsoup框架進行網頁爬蟲 ( https://blog.csdn.net/zeng308041977/article/details/76713912 )
 *  -----------------------------------------------------------------------------------------
 */
public class JsoupSpider {

    // http://www.nipic.com/photo/jingguan/shanshui/index.html?page= 後面的數字逐個加1
    // 直到2010
    public static  String getAllURL(String url) {
        StringBuffer sb =  null ;
        try  {
            sb =  new  StringBuffer();
            for  ( int  i =  1 ; i <=  2010 ; i++) {
                sb.append(url + i).append( "\r\n" );
            }

        }  catch  (Exception e) {
            // TODO: handle exception
        }
        return  sb.toString();
    }

    // 在大圖的網址獲取詳細的圖片地址
    public static  String getPicURL(String picHrefUrl) {
        String picURL =  null ;
        if  (picHrefUrl !=  null ) {
            try  {
                Document doc = Jsoup.connect(picHrefUrl).get();
                Element element = doc.getElementById( "J_worksImg" );
                picURL = element.attr( "src" );
            }  catch  (Exception e) {
                // TODO Auto-generated catch block
                new  RuntimeException( " " );
            }
        }
        return  picURL;
    }

    // 下載圖片到本地磁盤
    public static void  downloadPicToLocal(String picSourceURL, String picDestPath) {
        BufferedOutputStream bos =  null ;
        BufferedInputStream bis =  null ;
        try  {
            URL url =  new  URL(picSourceURL);
            bis =  new  BufferedInputStream(url.openStream());
            bos =  new  BufferedOutputStream( new  FileOutputStream(picDestPath));
            byte [] b =  new byte [ 1024  *  1024 ];
            int  len =  0 ;
            while  ((len = bis.read(b)) != - 1 ) {
                bos.write(b,  0 , len);
                bos.flush();
            }
        }  catch  (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch  (Exception e) {
            // TODO Auto-generated catch block
            new  RuntimeException( " " );
        }  finally  {
            try  {
                bos.close();
            }  catch  (Exception e) {
                // TODO Auto-generated catch block
            }
            try  {
                bis.close();
            }  catch  (Exception e) {
                // TODO Auto-generated catch block
            }
        }
    }

    // 獲取文件後綴名
    public static  String getNameExtension(String picURL) {
        String extension =  null ;

        try  {
            int  lastIndexOf =  0 ;
            if  (picURL !=  null  && !picURL.equals( "" )) {
                if  (picURL.endsWith( ".jpg" ))
                    lastIndexOf = picURL.lastIndexOf( ".jpg" );
                if  (picURL.endsWith( ".png" ))
                    lastIndexOf = picURL.lastIndexOf( ".png" );
                extension = picURL.substring(lastIndexOf);
            }
        }  catch  (Exception e) {
            // TODO: handle exception
        }
        return  extension;
    }

    // 通過Jsoup獲取網頁的單個圖片地址_通過傳入網頁地址和生成圖片目錄
    // 目標網址為http://www.nipic.com/photo/jingguan/shanshui/index.html?page=1
    public static boolean  getOnePic(String webURL, String picDestURL) {
        boolean  flag =  true ;
        try  {
            // webURL="http://www.nipic.com/photo/jingguan/shanshui/index.html?page="
            // 後面的數字逐個加1 直到2010
            // 創建Document對象，拿到元素對象再操作屬性。
            File file =  new  File(picDestURL);
            if  (!file.exists()) {
                file.mkdir();
            }
            int  count =  2220 ;
            for  ( int  i =  112 ; i <=  2010 ; i++) {
                // 分2010次拿到每頁的網址
                try  {
                    String mainURL = webURL + i;
                    Document doc = Jsoup.connect(mainURL).get();
                    // 拿到每個頁面的每個class元素
                    Elements elements = doc.getElementsByClass( "relative block works-detail hover-none works-img-box" );
                    for  (Element element : elements) {
                        try  {
                            // 獲取每個頁面的大圖的網址
                            String picHrefUrl = element.attr( "href" );
                            // 獲取每個大圖的詳細地址
                            String picURL = getPicURL(picHrefUrl);
                            // 從圖片的詳細地址開始下載單個圖片到本地目標路徑
                            downloadPicToLocal(picURL, picDestURL +  "\\"  + (++count) + getNameExtension(picURL));
                            System.out.println( "已抓取"  + count +  "張圖片。" );
                        }  catch  (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }  catch  (Exception e) {
                    // TODO: handle exception
                }

            }
            // 計數器歸零
            count =  0 ;
        }  catch  (Exception e) {
            // TODO Auto-generated catch block
            flag =  false ;
            new  RuntimeException( "在服務器找不到對應圖片，正在尋找下一個圖片中。。。" );
        }
        return  flag;
    }

    public static void  main(String[] args) {
        boolean  result = getOnePic( "http://www.nipic.com/photo/jingguan/shanshui/index.html?page=" ,
                "G:\\PictureSpider" );
        System.out.println(result);

    }
}