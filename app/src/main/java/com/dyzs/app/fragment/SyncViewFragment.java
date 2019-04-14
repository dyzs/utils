package com.dyzs.app.fragment;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyzs.app.R;
import com.dyzs.app.manager.ContactInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

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


        // createExportContacts();
        // exportContacts();

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

    private void createExportContacts() {
        Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        exportContacts();
                        //restoreContacts();
                        emitter.onNext("PPChaos");
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    /**
     * 默认导出方式
     */
    public void exportContacts() {
        try {
            String path = Environment.getExternalStorageDirectory().getPath() + "/contacts_oppo.vcf";
            ContentResolver cr = getActivity().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            int index = cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
            FileOutputStream fout = new FileOutputStream(path);
            byte[] data = new byte[1024];
            while (cur.moveToNext()) {
                String lookupKey = cur.getString(index);
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
                AssetFileDescriptor fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fin = fd.createInputStream();
                int len = -1;
                while ((len = fin.read(data)) != -1) {
                    fout.write(data, 0, len);
                }
                fin.close();
            }
            fout.close();
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreContacts() {
        try {
            ContactInfo.ContactHandler contactHandler = new ContactInfo.ContactHandler();
            contactHandler.restoreContacts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
