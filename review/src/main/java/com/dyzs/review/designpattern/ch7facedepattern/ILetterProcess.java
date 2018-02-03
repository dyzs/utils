package com.dyzs.review.designpattern.ch7facedepattern;

/**
 * @author maidou, created on 2018/2/3.
 * 定义一个写信过程
 */

public interface ILetterProcess {
    //首先要写信的内容
    void writeContext(String context);

    //其次写信封
    void fillEnvelope(String envelope);

    //把信放到信封里
    void letterIntoEnvelope();

    //然后邮递
    void sendLetter();


}
