package com.dyzs.review.designpattern.ch4multition;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author maidou, created on 2018/2/1.
 * Multition Pattern
 * 多例模式, 假如一个国家有两个皇帝
 */

public class Emperor {
    private static int maxNumOfEmperor = 2;
    private static ArrayList<String> emperorInfoList = new ArrayList<>(maxNumOfEmperor);
    private static ArrayList<Emperor> emperorList = new ArrayList<>(maxNumOfEmperor);
    private static int countNumOfEmperor =0; //正在被人尊称的是那个皇帝, 当前的皇帝
    private Emperor(){}

    private Emperor(String info) {
        emperorInfoList.add(info);
    }

    static {
        //把所有的皇帝都产生出来
        for (int i =0; i < maxNumOfEmperor; i++) {
            emperorList.add(new Emperor("I'm the king " + i + ""));
        }
    }

    public static Emperor getInstance() {
        Random random = new Random();
        countNumOfEmperor = random.nextInt(maxNumOfEmperor);
        return emperorList.get(countNumOfEmperor);
    }

    // 输出当前参拜的皇帝名称
    public static void emperorInfo() {
        System.out.println("当前的皇帝是：" + emperorInfoList.get(countNumOfEmperor));
    }
}
