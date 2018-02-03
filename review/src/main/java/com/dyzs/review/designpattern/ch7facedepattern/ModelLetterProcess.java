package com.dyzs.review.designpattern.ch7facedepattern;

/**
 * @author maidou, created on 2018/2/3.
 * 到了非常时期, 信件内容需要审核
 */

public class ModelLetterProcess {
    private LetterProcessImpl letterProcess = new LetterProcessImpl();
    private Police letterPolice = new Police();

    //写信，封装，投递，一体化了
    public void modelSendLetter(String context, String envelope) {
        letterProcess.writeContext(context);
        letterProcess.fillEnvelope(envelope);
        letterProcess.letterIntoEnvelope();

        if (letterPolice.checkLetter(context)) {
            letterProcess.sendLetter();
        }

    }
}
