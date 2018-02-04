package com.dyzs.review.designpattern.ch10builderpattern;

import java.util.ArrayList;

/**
 * @author NKlaus, created on 2018/2/4.
 */

public class PorscheBuilder extends CarBuilder {
    private PorscheModel porsche = new PorscheModel();
    @Override
    public void setSequence(ArrayList<String> sequence) {
        this.porsche.setSequence(sequence);
    }

    @Override
    public CarModel getCarModel() {
        return this.porsche;
    }

    @Override
    public void start() {
        this.porsche.start();
    }
}
