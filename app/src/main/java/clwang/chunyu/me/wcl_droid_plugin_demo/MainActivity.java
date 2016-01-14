package clwang.chunyu.me.wcl_droid_plugin_demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_tl_tabs) TabLayout mTlTabs;
    @Bind(R.id.main_vp_container) ViewPager mVpContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        mVpContainer.setAdapter(adapter);
        mTlTabs.setupWithViewPager(mVpContainer);
    }
}
