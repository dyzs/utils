package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 */

public class HumanWhiteFemale extends AbstractHumanSkinWhite {



    @Override
    public void sex() {
        System.out.println("女性--白人");
    }
}
