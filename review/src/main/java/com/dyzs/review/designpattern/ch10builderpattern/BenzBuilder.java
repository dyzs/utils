package com.dyzs.review.designpattern.ch10builderpattern;

import java.util.ArrayList;

/**
 * @author NKlaus, created on 2018/2/4.
 */

public class BenzBuilder extends CarBuilder {
    private BenzModel benz = new BenzModel();

    @Override
    public void setSequence(ArrayList<String> sequence) {
        this.benz.setSequence(sequence);
    }

    @Override
    public CarModel getCarModel() {
        return this.benz;
    }

    @Override
    public void start() {
        this.benz.start();
    }
}
