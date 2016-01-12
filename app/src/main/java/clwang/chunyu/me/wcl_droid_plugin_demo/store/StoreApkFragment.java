package clwang.chunyu.me.wcl_droid_plugin_demo.store;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import clwang.chunyu.me.wcl_droid_plugin_demo.ApkItem;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;

/**
 * 本地Apk的页面
 * <p/>
 * Created by wangchenlong on 16/1/8.
 */
public class StoreApkFragment extends Fragment {

    @Bind(R.id.list_rv_recycler) RecyclerView mRvRecycler;

    private StoreAdapter mStoreAdapter; // 适配器

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

    // 开始加载
    private void startLoad() {
        new Thread("ApkScanner") {
            @Override
            public void run() {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                PackageManager pm = getActivity().getPackageManager();
                for (File apk : file.listFiles()) {
                    if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                        final PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), 0);
                        mStoreAdapter.addApkItems(new ApkItem(getActivity(), info, apk.getPath()));
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
