package com.dyzs.review.designpattern.ch16observerpattern;

import java.util.Observable;

/**
 * @author NKlaus, created on 2018/2/6.
 * 韩非子是一个呗观察者, 继承 {@link java.util.Observable}
 */

public class HanFeiZiVer2 extends Observable implements IHanFeiZi {

    @Override
    public void haveBreakfast() {
        System.out.println("韩非子:开始吃饭了...");
        // 通知所有观察者
        this.setChanged();
        this.notifyObservers("韩非子在吃饭");
    }

    @Override
    public void haveFun() {
        System.out.println("韩非子:开始娱乐了...");
        this.setChanged();
        this.notifyObservers("韩非子在娱乐");
    }
}
