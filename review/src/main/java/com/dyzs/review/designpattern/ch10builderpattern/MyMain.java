package com.dyzs.review.designpattern.ch10builderpattern;

import java.util.ArrayList;

/**
 * @author maidou, created on 2018/2/4.
 */

public class MyMain {
    public static void main(String[] args) {
        /*两个实现类都完成，我们再来看牛叉公司要的要求，
        我先要 1 个奔驰的模型，这个模型的要求是跑的时候，
        先发动引擎，然后再挂档启动，然后停下来，不需要喇叭，那怎么实现呢：*/
        ArrayList<String> sequence = new ArrayList<>();
        sequence.add("engine boom");
        sequence.add("start");
        sequence.add("stop");
        /*BenzModel benzModel = new BenzModel();
        benzModel.setSequence(sequence);
        benzModel.run();*/

        /*
         * 客户告诉牛叉公司，我要这样一个模型，然后牛叉公司就告诉我老大
         * 说要这样一个模型，这样一个顺序，然后我就来制造
         */
        /*PorscheBuilder builder = new PorscheBuilder();
        //把顺序给这个builder类，制造出这样一个车出来
        builder.setSequence(sequence);
        PorscheModel porsche = (PorscheModel) builder.getCarModel();
        porsche.run();*/
    }
}
