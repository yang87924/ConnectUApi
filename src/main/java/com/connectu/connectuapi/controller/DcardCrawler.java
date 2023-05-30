package com.connectu.connectuapi.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DcardCrawler {
    public static void main(String[] args) {
        try {
            // 使用Jsoup连接到PTT网站并获取页面内容
            Document doc = Jsoup.connect("https://www.ptt.cc/bbs/Gossiping/index.html").get();

            // 选择文章列表的元素
            Elements postElements = doc.select(".r-ent");

            // 遍历文章列表并输出标题和内容
            for (Element postElement : postElements) {
                // 获取文章标题
                Element titleElement = postElement.selectFirst(".title > a");
                String title = titleElement.text();

                // 获取文章内容
                Element contentElement = postElement.selectFirst(".meta > .author");
                String content = contentElement.text();

                // 输出标题和内容
                System.out.println("Title: " + title);
                System.out.println("Content: " + content);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
