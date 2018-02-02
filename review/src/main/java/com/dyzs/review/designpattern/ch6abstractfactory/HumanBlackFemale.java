package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 */

public class HumanBlackFemale extends AbstractHumanSkinBlack {

    @Override
    public void sex() {
        System.out.println("女性--黑人");
    }
}
