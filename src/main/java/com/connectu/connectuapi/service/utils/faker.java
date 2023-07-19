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
    public static String generateRandomString(int minLength, int maxLength) {
        String characters = "在現代社會中，工作已成為我們生活的一部分。隨著科技的進步和全球化的影響，工作環境正在發生著巨大的變革。這些變化帶來了一些挑戰，同時也開啟了新的機會。\n" +
                "\n" +
                "一個主要的挑戰是適應快速變化的科技環境。隨著人工智能、大數據和自動化技術的普及，許多傳統的工作正在面臨被取代的風險。然而，這同時也創造了新的機會。我們可以利用科技的力量來改善工作效率，從而釋放出更多時間來專注於創造性和戰略性的任務。\n" +
                "\n" +
                "另一個挑戰是處理工作和生活之間的平衡。現代的工作節奏快速且繁忙，對我們的時間和精力提出了很大的要求。因此，我們需要學會管理時間，設定優先順序，並保持身心健康。這也是一個機會，讓我們探索彈性工作時間和遠程工作的模式，從而實現更好的工作與生活平衡。\n" +
                "\n" +
                "另外，團隊合作也是現代工作中的重要課題。越來越多的工作需要團隊間的合作和協調。這意味著我們需要培養良好的溝通和協作能力，學會在團隊中建立共享的目標和價值觀。同時，多元化的團隊結構也為創新和創造力提供了更多的機會，不同背景和觀點的人們能夠帶來更多的創意和解決問題的能力。\n" +
                "\n" +
                "最後，職業發展也是現代工作中的重要議題。工作市場變得競爭激烈，我們需要不斷學習和提升自己的技能，以應對變化中的需求。這也是一個機會，讓我們能夠";
        StringBuilder sb = new StringBuilder();
        int length = (int) (Math.random() * (maxLength - minLength + 1) + minLength);
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}