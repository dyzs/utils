package com.dyzs.review.designpattern.ch5factorymethod;

/**
 * @author maidou, created on 2018/2/1.
 */

public class HumanSkinBlack implements Human {

    @Override
    public void laugh() {
        System.out.println("黑人会笑");
    }

    @Override
    public void cry() {
        System.out.println("黑人会哭");
    }

    @Override
    public void talk() {
        System.out.println("黑人会说黑话");
    }
}
