package com.hsbc.java;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by eric on 2015/1/6.
 */
public class MultiThreadHtmlPictureDownloader {
    private static final String HTML_FILE = "source.html";

    public volatile static Stack<String> allPictureLinks = new Stack<String>();

    public static void main(String[] args) throws IOException, InterruptedException {
       Long startTime = System.currentTimeMillis();
       MultiThreadHtmlPictureDownloader downloader = new MultiThreadHtmlPictureDownloader();
       String htmlSource = downloader.readHtmlFile(HTML_FILE);
       allPictureLinks = downloader.getAllPictureLinks(htmlSource);
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 5; i++){
           Thread thread = new Thread(new ImageDownloadThread());
           pool.execute(thread);
        }
       
        pool.shutdown();

        try {
            pool.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    private Stack<String> getAllPictureLinks(String htmlSource){
//       List<String> allPictureLinks = new ArrayList<String>();
        Document doc = Jsoup.parse(htmlSource);
        Elements media = doc.select("[src]");
        for(Element src : media){
            if(src.tagName().equalsIgnoreCase("IMG") && src.attr("src").startsWith("http") && src.attr("src").endsWith(".jpg")){
                allPictureLinks.add(src.attr("src"));
            }
        }
        return allPictureLinks;
    }
}
