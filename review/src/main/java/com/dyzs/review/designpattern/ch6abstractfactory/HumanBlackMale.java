package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 */

public class HumanBlackMale extends AbstractHumanSkinBlack {

    @Override
    public void sex() {
        System.out.println("男性--黑人");
    }
}
