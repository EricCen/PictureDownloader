package com.hsbc.java;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 2015/1/6.
 */
public class HtmlPictureDownloader {
    private static final String HTML_FILE = "source.html";

    public static void main(String[] args) throws IOException {
       Long startTime = System.currentTimeMillis();
       HtmlPictureDownloader downloader = new HtmlPictureDownloader();
       String htmlSource = downloader.readHtmlFile(HTML_FILE);
       List<String> allPictureLinks = downloader.getAllPictureLinks(htmlSource);
       downloader.download(allPictureLinks);
       Long endTime = System.currentTimeMillis();
       System.out.println("Time took to finish downloading: " + (endTime - startTime));
    }

    private String readHtmlFile(String htmlFile) throws IOException {
        File file = new File(htmlFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String readLine = "";
        StringBuffer content = new StringBuffer();
        while((readLine = reader.readLine()) != null){
            content.append(readLine);
        }
        System.out.println("Finish read html file");
        System.out.println(content);
        return content.toString();
    }

    private List<String> getAllPictureLinks(String htmlSource){
       List<String> allPictureLinks = new ArrayList<String>();
        Document doc = Jsoup.parse(htmlSource);
        Elements media = doc.select("[src]");
        for(Element src : media){
            if(src.tagName().equalsIgnoreCase("IMG") && src.attr("src").startsWith("http") && src.attr("src").endsWith(".jpg")){
                allPictureLinks.add(src.attr("src"));
            }
        }
        return allPictureLinks;
    }

    private void download(List<String> allPictureLinks) throws IOException {
        System.out.println("start to download picture...");
        for(int i = 0; i < allPictureLinks.size(); i++){
            try{
                BufferedImage image  = ImageIO.read(new URL(allPictureLinks.get(i)));
                String fileName = "Maya"  + i + ".jpg" ;
                File outputImage = new File(fileName);
                ImageIO.write(image,"jpg", outputImage);
                System.out.println("Downloaded picture: " + fileName);
            } catch(IllegalArgumentException ex){
                continue;
            } catch(FileNotFoundException ex){
                continue;
            }
        }
        System.out.println("Finish downloading picture");
    }
}
