package com.dyzs.review.designpattern.ch5factorymethod;

/**
 * @author maidou, created on 2018/2/1.
 */

public class HumanSkinWhite implements Human {

    @Override
    public void laugh() {
        System.out.println("白人会笑");
    }

    @Override
    public void cry() {
        System.out.println("白人会哭");
    }

    @Override
    public void talk() {
        System.out.println("白人说...............");
    }
}
