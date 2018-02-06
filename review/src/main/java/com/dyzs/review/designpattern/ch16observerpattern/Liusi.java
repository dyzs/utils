package com.dyzs.review.designpattern.ch16observerpattern;

/**
 * @author NKlaus, created on 2018/2/6.
 */

public class Liusi implements IObserver {
    @Override
    public void update(String context) {
        System.out.println("刘斯：观察到韩非子活动，开始动作了...");
        this.happy(context);
        System.out.println("刘斯：真被乐死了\n");
    }

    //一看韩非子有变化，他就快乐
    private void happy(String context){
        System.out.println("刘斯：因为" +context+",——所以我快乐呀！" );
    }
}
