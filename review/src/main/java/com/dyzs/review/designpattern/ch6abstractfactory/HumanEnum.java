package com.dyzs.review.designpattern.ch6abstractfactory;

/**
 * @author maidou, created on 2018/2/2.
 */
public enum  HumanEnum {

    //把世界上所有人类型都定义出来
    YellowMaleHuman("com.dyzs.review.designpattern.ch6abstractfactory.HumanYellowMale"),

    YellowFemaleHuman("com.dyzs.review.designpattern.ch6abstractfactory.HumanYellowFemale"),

    WhiteFemaleHuman("com.dyzs.review.designpattern.ch6abstractfactory.HumanWhiteFemale"),

    WhiteMaleHuman("com.dyzs.review.designpattern.ch6abstractfactory.HumanWhiteMale"),

    BlackFemaleHuman("com.dyzs.review.designpattern.ch6abstractfactory.HumanBlackFemale"),

    BlackMaleHuman("com.dyzs.review.designpattern.ch6abstractfactory.HumanBlackMale");


    private String value = "";
    //定义构造函数，目的是Data(value)类型的相匹配
    private HumanEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
