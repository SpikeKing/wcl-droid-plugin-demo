package clwang.chunyu.me.wcl_droid_plugin_demo.store;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import clwang.chunyu.me.wcl_droid_plugin_demo.ApkItem;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;

/**
 * 本地Apk的页面
 * Created by wangchenlong on 16/1/8.
 */
public class StoreApkFragment extends Fragment {

    @Bind(R.id.list_rv_recycler) RecyclerView mRvRecycler;

    final Handler handler = new Handler();
    private StoreAdapter mStoreAdapter;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRvRecycler.setLayoutManager(llm);

        mStoreAdapter = new StoreAdapter();
        mRvRecycler.setAdapter(mStoreAdapter);
        startLoad();
    }

    private void startLoad() {
        new Thread("ApkScanner") {
            @Override
            public void run() {
//                File file = Environment.getExternalStorageDirectory();
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                List<File> apks = new ArrayList<File>(10);
                for (File apk : file.listFiles()) {
                    if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                        apks.add(apk);
                    }
                }

                file = new File(Environment.getExternalStorageDirectory(), "360Download");
                if (file.exists() && file.isDirectory()) {
                    for (File apk : file.listFiles()) {
                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                            apks.add(apk);
                        }
                    }
                }
                PackageManager pm = getActivity().getPackageManager();
                for (final File apk : apks) {
                    try {
                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                            final PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), 0);
                            if (info != null) {
                                try {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mStoreAdapter.addApkItems(new ApkItem(getActivity(), info, apk.getPath()));
                                        }
                                    });
                                } catch (Exception e) {
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
