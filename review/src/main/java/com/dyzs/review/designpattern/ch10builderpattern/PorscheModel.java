package com.dyzs.review.designpattern.ch10builderpattern;

/**
 * @author NKlaus, created on 2018/2/4.
 */

public class PorscheModel extends CarModel {
    @Override
    protected void start() {
        System.out.println("保时捷跑起来是这个样子的...");
    }

    @Override
    protected void stop() {
        System.out.println("保时捷应该这样停车...");
    }

    @Override
    protected void alarm() {
        System.out.println("保时捷的喇叭声音是这个样子的...");
    }

    @Override
    protected void engineBoom() {
        System.out.println("保时捷的引擎是这个声音的...");
    }
}
