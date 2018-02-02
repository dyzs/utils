package com.dyzs.review.designpattern.ch6abstractfactory;

import com.dyzs.review.designpattern.ch5factorymethod.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author maidou, created on 2018/2/2.
 *
 */
public interface HumanFactory {

    Human createYellowHuman();

    Human createBlackHuman();

    Human createWhiteHuman();

}
