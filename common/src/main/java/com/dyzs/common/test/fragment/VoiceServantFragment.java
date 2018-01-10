package com.dyzs.common.test.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyzs.common.R;
import com.dyzs.common.ui.VoiceServant;

/**
 * Created by maidou on 2018/1/8.
 */

public class VoiceServantFragment extends Fragment{
    VoiceServant voice_servant;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_voice_servant, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        voice_servant = (VoiceServant) view.findViewById(R.id.voice_servant);
        voice_servant.setPointerDecibel(123);
    }
}
