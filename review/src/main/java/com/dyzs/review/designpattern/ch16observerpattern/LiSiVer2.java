package com.dyzs.review.designpattern.ch16observerpattern;

import java.util.Observable;
import java.util.Observer;

/**
 * @author NKlaus, created on 2018/2/6.
 */

public class LiSiVer2 implements Observer {
    @Override
    public void update(Observable observable, Object obj) {
        System.out.println("李斯：观察到李斯活动，开始向老板汇报了...");
        this.reportToQiShiHuang(obj.toString());
        System.out.println("李斯：汇报完毕，秦老板赏给他两个萝卜吃吃...\n");
    }

    //汇报给秦始皇
    private void reportToQiShiHuang(String reportContext){
        System.out.println("李斯：报告，秦老板！韩非子有活动了--->"+reportContext);
    }
}
