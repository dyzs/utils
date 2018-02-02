package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/1.
 */

public abstract class AbstractHumanSkinYellow implements Human {

    @Override
    public void laugh() {
        System.out.println("黄种人笑");
    }

    @Override
    public void cry() {
        System.out.println("黄种人哭");
    }

    @Override
    public void talk() {
        System.out.println("黄种人说闽南话");
    }


}
