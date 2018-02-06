package com.dyzs.review.designpattern.ch16observerpattern;

import java.util.Observer;

/**
 * @author maidou, created on 2018/2/6.
 */

public class MyMain {
    public static void main(String[] args) throws InterruptedException {
        //三个观察者产生出来
        IObserver lisi = new LiSi();
        IObserver wangsi = new WangSi();
        IObserver liusi = new Liusi();

        //定义出韩非子
        HanFeiZi hanFeiZi = new HanFeiZi();
        //我们后人根据历史，描述这个场景，有三个人在观察韩非子
        hanFeiZi.addObserver(lisi);
        hanFeiZi.addObserver(wangsi);
        hanFeiZi.addObserver(liusi);

        //然后这里我们看看韩非子在干什么
        hanFeiZi.haveFun();

        System.out.println("----------------------------------");
        Observer lisi2 = new LiSiVer2();

        HanFeiZiVer2 hanFeiZiVer2 = new HanFeiZiVer2();
        hanFeiZiVer2.addObserver(lisi2);
        
        hanFeiZiVer2.haveBreakfast();

    }
}
