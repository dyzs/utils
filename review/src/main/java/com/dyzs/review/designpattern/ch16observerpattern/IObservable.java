package com.dyzs.review.designpattern.ch16observerpattern;

/**
 * @author NKlaus, created on 2018/2/6.
 */

public interface IObservable {
    //增加一个观察者
    void addObserver(IObserver observer);

    //删除一个观察者，——我不想让你看了
    void deleteObserver(IObserver observer);

    //既然要观察，我发生改变了他也应该用所动作——通知观察者
    void notifyObservers(String context);
}
