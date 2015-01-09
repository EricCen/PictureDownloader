package com.hsbc.java;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by eric on 2015/1/8.
 */
public class ImageDownloadThread implements Runnable {
    @Override
    public void run() {

        BufferedImage image = null;

        while (!MultiThreadHtmlPictureDownloader.allPictureLinks.empty()) {
            String imageLink = MultiThreadHtmlPictureDownloader.allPictureLinks.pop();
            try {
                image = ImageIO.read(new URL(imageLink));
                String fileName = imageLink.substring(imageLink.lastIndexOf("/") + 1, imageLink.length() - 4) + ".jpg";
                File outputImage = new File(fileName);
                ImageIO.write(image, "jpg", outputImage);
                System.out.println("Downloaded picture: " + fileName);
            } catch (IOException e) {
                continue;
            }
        }

    }
}
