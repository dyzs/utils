package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 * nv wa
 */

public class MyMain {
    public static void main(String[] args) {

        //第一条生产线，男性生产线
        HumanFactory maleFactory = new HumanMaleFactory();

        //第二条生产线，女性生产线
        HumanFactory femaleFactory = new HumanFemaleFactory();

        Human maleBlack = maleFactory.createBlackHuman();

        Human femaleWhite = femaleFactory.createWhiteHuman();

        maleBlack.cry();
        maleBlack.laugh();
        maleBlack.sex();
        maleBlack.talk();

        femaleWhite.cry();
        femaleWhite.laugh();
        femaleWhite.sex();
        femaleWhite.talk();

    }
}
