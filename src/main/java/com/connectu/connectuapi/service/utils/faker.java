package com.connectu.connectuapi.service.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class faker {
    public static String generateFakeArticle(int targetLength) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < targetLength) {
            String word = segmenter.sentenceProcess(generateRandomChinese()).get(0);
            sb.append(word);
        }
        return sb.substring(0, targetLength);
    }

    private static String generateRandomChinese() {
        int min = 0x4E00; // 中文字符的起始unicode編碼
        int max = 0x9FFF; // 中文字符的結束unicode編碼
        Random random = new Random();
        int codePoint = random.nextInt(max - min + 1) + min;
        return new String(Character.toChars(codePoint));
    }
    public static  String getSystemTime(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        //String formattedDate = dateFormat.format(currentDate);
        return dateFormat.format(currentDate);
    }
}
