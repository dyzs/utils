package com.dyzs.app.presenter;


import com.dyzs.app.view.IHardcodeFilterView;
import com.dyzs.base.BasePresenter;

public class HardcodeFilterPresenter extends BasePresenter {
    private IHardcodeFilterView iView;
    public HardcodeFilterPresenter(IHardcodeFilterView iView) {
        super(iView);
        this.iView = iView;
    }


    public void operateStartFilter() {
        String rootPath = "";



    }
}
