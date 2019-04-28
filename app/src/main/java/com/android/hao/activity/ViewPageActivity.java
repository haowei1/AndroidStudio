package com.android.hao.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.android.hao.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        initUI();

    }

    private void initUI() {

        ViewPager viewPager = findViewById(R.id.viewPage);
        PagerTabStrip tab = findViewById(R.id.tab);

        tab.setTabIndicatorColor(Color.RED);
        tab.setDrawFullUnderline(true);
        tab.setBackgroundColor(Color.CYAN);
        tab.setTextSpacing(50);

        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<View> listViews = new ArrayList<>();

        listTitle.add("微信");
        listTitle.add("通讯录");
        listTitle.add("发现");
        listTitle.add("我");

        listViews.add(View.inflate(getApplicationContext(), R.layout.page_item1,null));
        listViews.add(View.inflate(getApplicationContext(), R.layout.page_item2,null));
        listViews.add(View.inflate(getApplicationContext(), R.layout.page_item3,null));
        listViews.add(View.inflate(getApplicationContext(), R.layout.page_item4,null));

        viewPager.setAdapter(new MyAdapter(listTitle, listViews));
    }

    class MyAdapter extends PagerAdapter{

        private List<String> list_title = null;
        private List<View> list_view = null;

        public MyAdapter(List<String> listTitle, List<View> listView) {
            this.list_title = listTitle;
            this.list_view = listView;
        }

        @Override
        public int getCount() {
            if (list_view != null) {
                return list_view.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(list_view.get(position));
            return list_title.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(list_view.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list_title.get(position);
        }
    }
}
