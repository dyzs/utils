package com.dyzs.review.designpattern.ch4multition;

/**
 * @author maidou, created on 2018/2/1.
 */

public class Minister {
    /**
     * @param args
     */
    public static void main(String[] args) {
        int ministerNum =10; //10个大臣
        for(int i=0; i<ministerNum; i++){
            Emperor emperor = Emperor.getInstance();
            System.out.print("第"+(i+1)+"个大臣参拜的是：");
            emperor.emperorInfo();
        }
    }
}
