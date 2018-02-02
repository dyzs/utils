package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 */

public class HumanWhiteMale extends AbstractHumanSkinWhite {

    @Override
    public void sex() {
        System.out.println("男性--白人");
    }
}
