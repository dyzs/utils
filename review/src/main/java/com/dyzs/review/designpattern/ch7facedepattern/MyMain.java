package com.dyzs.review.designpattern.ch7facedepattern;

/**
 * @author maidou, created on 2018/2/2.
 * start write letter
 */

public class MyMain {
    public static void main(String[] args) {
        /*//创建一个处理信件的过程
        LetterProcessImpl myLetter = new LetterProcessImpl();
        //开始写信
        myLetter.writeContext("It's me again, last time i wrote to you....");
        //开始写信封
        myLetter.fillEnvelope("from dyzs");
        myLetter.letterIntoEnvelope();
        myLetter.sendLetter();*/


        System.out.println("-------------------");
        //现代化的邮局，有这项服务，邮局名称叫Hell Road
        ModelLetterProcess hellRoadPostOffice = new ModelLetterProcess();
        //你只要把信的内容和收信人地址给他，他会帮你完成一系列的工作；
        String address = "Happy Road No. 666,God Province,Heaven"; //定义一个地址
        String context = "Hello,It's me,do you know who I am? I'm your old lover. I'd like to....";
        hellRoadPostOffice.modelSendLetter(context, address);
    }
}
