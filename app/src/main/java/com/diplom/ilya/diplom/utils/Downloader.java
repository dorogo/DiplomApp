package com.diplom.ilya.diplom.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 11.03.17.
 */
public class Downloader {

    public static void DownloadFile(String fileURL, File directory) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(fileURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                return "Server returned HTTP " + connection.getResponseCode()
//                        + " " + connection.getResponseMessage();
//            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            System.out.println("FILE LENGHTH: " + fileLength);
            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream("/sdcard/file_name.extension");

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
//                if (isCancelled()) {
//                    input.close();
//                    return;
//                }
                total += count;
                // publishing the progress....
//                if (fileLength > 0) // only if total length is known
//                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            e.printStackTrace();
            return;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return;
//        try {
//
//            FileOutputStream f = new FileOutputStream(directory);
//            URL u = new URL(fileURL);
//            HttpURLConnection c = (HttpURLConnection) u.openConnection();
//            c.setRequestMethod("GET");
//            c.setDoOutput(true);
//            c.connect();
//
//            InputStream in = c.getInputStream();
//
//            byte[] buffer = new byte[1024];
//            int len1 = 0;
//            while ((len1 = in.read(buffer)) > 0) {
//                f.write(buffer, 0, len1);
//            }
//            f.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}