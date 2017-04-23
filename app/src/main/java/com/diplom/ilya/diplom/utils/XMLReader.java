package com.diplom.ilya.diplom.utils;

import android.os.Environment;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 23.03.17.
 */
public class XMLReader {
    public static String defaultPath = "";

    public static boolean externalMemoryAvailable() {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            System.out.println(Environment.isExternalStorageRemovable());
//            System.out.println("SD-карта не доступна: " + Environment.getExternalStorageState());
////            return;
//        }


//        if (Environment.isExternalStorageRemovable()) {
//            device support sd card. We need to check sd card availability.
            String state = Environment.getExternalStorageState();
        System.out.println(Environment.getExternalStorageDirectory() + " " + Environment.getDataDirectory());
            return state.equals(Environment.MEDIA_MOUNTED);
//        } else {
//            device not support sd card.
//            return false;
//        }
    }

    public static List<String> readXml(String path) {

        ArrayList<String> tmpList = new ArrayList<>();



        System.out.println("sd card:" + externalMemoryAvailable());

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            File file = new File(Environment.getExternalStorageDirectory() + "/.androidTest/xml/" + path);
            FileInputStream fis = new FileInputStream(file);
            xpp.setInput(new InputStreamReader(fis));

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if (!xpp.isWhitespace()) {
                            tmpList.add(xpp.getText());
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }

        }catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return tmpList;
    }
    public static List<List<String>> readXml(String path,String divideByTag) {

        ArrayList<String> tmpList = new ArrayList<>();
        List<List<String>> tmpList2 = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            File file = new File(Environment.getExternalStorageDirectory() + "/.androidTest/xml/" + path);
            FileInputStream fis = new FileInputStream(file);
            xpp.setInput(new InputStreamReader(fis));

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals(divideByTag)) {
                            tmpList2.add(tmpList);
                            tmpList = new ArrayList<>();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (!xpp.isWhitespace()) {
                            tmpList.add(xpp.getText());
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        }catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return tmpList2;
    }
}
