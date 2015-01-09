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
       Thread t1 = new Thread(new ImageDownloadThread());
       Thread t2 = new Thread(new ImageDownloadThread());
        Thread t3 = new Thread(new ImageDownloadThread());
        Thread t4 = new Thread(new ImageDownloadThread());
        Thread t5 = new Thread(new ImageDownloadThread());
     /* for(int i = 0; i < 5; i++){
          Thread t = new Thread(new ImageDownloadThread());
          t.start();
          t.join();
      }*/


        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);

        pool.shutdown();

        try {
            pool.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        System.out.println("Time took to finish downloading: " + (endTime - startTime));
     /*  downloader.download(allPictureLinks);*/
      /* Long endTime = System.currentTimeMillis();
       System.out.println("Time took to finish downloading: " + (endTime - startTime));*/
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

   /* private void download(List<String> allPictureLinks) throws IOException {
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
    }*/
}
