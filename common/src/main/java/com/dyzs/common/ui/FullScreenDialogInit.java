package com.dyzs.common.ui;

/**
 * @author NKlaus, created on 2018/2/6.
 */

class FullScreenDialogInit {
    static void init(final FullScreenDialogVer2 dialogVer2) {
        FullScreenDialogVer2.Builder builder = dialogVer2.mBuilder;
        dialogVer2.setContentView(builder.view);
        dialogVer2.setInterruptKeyEvent(builder.interruptKeyEvent);
    }
}
