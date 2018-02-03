package com.dyzs.review.designpattern.ch7facedepattern;

/**
 * @author maidou, created on 2018/2/3.
 * 写具体的实现类
 */

public class LetterProcessImpl implements ILetterProcess {

    @Override
    public void writeContext(String context) {
        System.out.println("填写信的内容...." + context);
    }

    @Override
    public void fillEnvelope(String envelope) {
        System.out.println("填写收件人地址及姓名...." + envelope);
    }

    @Override
    public void letterIntoEnvelope() {
        System.out.println("把信放到信封中....");
    }

    @Override
    public void sendLetter() {
        System.out.println("邮递信件...");
    }
}
