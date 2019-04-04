package com.dyzs.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyzs.app.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author dyzs
 * Created on 2018/1/17.
 */

public class SyncViewFragment extends Fragment {
    private ArrayList<String> mList;
    private static final String[] STR = new String[]{"1", "2", "3", "4", "5", "6", "4", "5", "6", "4", "5", "6", "4", "5", "6", "4", "5", "6", "4", "5", "6"};
    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList = new ArrayList<>();
        mList.addAll(Arrays.asList(STR));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_sync_view, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAdapter = new Adapter();
        mRecyclerView = view.findViewById(R.id.rv_container);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

    }



    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_adater, null);
            return new VHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            VHolder vHolder = (VHolder) viewHolder;
            vHolder.mTextView.setText(mList.get(i));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private class VHolder extends RecyclerView.ViewHolder {

            private TextView mTextView;
            public VHolder(@NonNull View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv_text);
            }
        }
    }
}
