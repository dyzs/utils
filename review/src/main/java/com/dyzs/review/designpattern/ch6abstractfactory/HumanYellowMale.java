package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 */

public class HumanYellowMale extends AbstractHumanSkinYellow {
    @Override
    public void sex() {
        System.out.println("男--黄种人");
    }
}
