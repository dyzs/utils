package com.dyzs.review.designpattern.ch16observerpattern;

import java.util.ArrayList;


/**
 * @author NKlaus, created on 2018/2/6.
 */

public class HanFeiZi implements IHanFeiZi, IObservable {
    //定义个变长数组，存放所有的观察者
    private ArrayList<IObserver> observerList = new ArrayList<>();

    @Override
    public void haveBreakfast() {
        System.out.println("韩非子:开始吃饭了...");
        this.notifyObservers("韩非子在吃饭");
    }

    @Override
    public void haveFun() {
        System.out.println("韩非子:开始娱乐了...");
        this.notifyObservers("韩非子在娱乐");
    }

    @Override
    public void addObserver(IObserver observer) {
        this.observerList.add(observer);
    }

    @Override
    public void deleteObserver(IObserver observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void notifyObservers(String context) {
        for(IObserver observer:observerList){
            observer.update(context);
        }
    }
}
