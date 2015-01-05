package com.hsbc.java;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 2014/12/27.
 */
public class PictureDownloader {

    public static void main(String[] args) throws InterruptedException {

        StringBuffer url = new StringBuffer("http://www.dapenti.com/blog/blog.asp?name=tupian&page=");
//        String pageInfo = "&page=";
        int pageID = 1;
        boolean flag = true;
        List<String> allPictureLinks = new ArrayList<String>();
        while(flag){
           try{

               Document doc = Jsoup.connect(url.toString() + pageID).get();
               Elements media = doc.select("[src]");
               for(Element src : media){
                   if(src.tagName().equalsIgnoreCase("IMG") && src.attr("src").startsWith("http") && src.attr("src").endsWith(".jpg")){
                       allPictureLinks.add(src.attr("src"));
                   }
               }
               for(int i = 0; i < allPictureLinks.size(); i++){
                   try{
                       BufferedImage image  = ImageIO.read(new URL(allPictureLinks.get(i)));
                       String fileName = "YiTu" + pageID + "of" + i + ".jpg" ;
                       File outputImage = new File(fileName);
                       ImageIO.write(image,"jpg", outputImage);
                   } catch(IllegalArgumentException ex){
                       continue;
                   }
               }
               pageID++;
               allPictureLinks.clear();
           }catch (IOException ex){
               flag = false;
               System.out.println("Reach the end of Website");
           }
        }
    }
}
