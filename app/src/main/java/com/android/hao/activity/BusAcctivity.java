package com.android.hao.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.hao.R;
import com.android.hao.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class BusAcctivity extends AppCompatActivity {

    private static final String TAG = "BusAcctivity";
    private String path = "GetBusStationInfo";
    private ExpandableListView mElv;
    private List<String> mGroup = new ArrayList<>();
    private List<List<Map<String, String>>> mChild = new ArrayList<>();


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: mGroup:" + mGroup.toString());
            Log.e(TAG, "handleMessage: mChild:" + mChild.toString());
            mElv.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_acctivity);

        initView();
        initData();
//        initTimer();

    }

//    private void initTimer() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mGroup.clear();
//                mChild.clear();
//                initData();
//            }
//        }, 0, 3000);
//    }

    private void initData() {
        mGroup.add("一号站台");
        mGroup.add("二号站台");

        Map<String, String> map = new HashMap<>();
        map.put("BusStationId", "1");
        OkHttpUtils.getDataFromIntent(path, map, new OkHttpUtils.CallBack() {
            @Override
            public void successful(String string) {
                Log.e(TAG, "successful: body1:" + string);
                parseJosn(string);
//                handler.sendEmptyMessage(0);
                Map<String, String> map = new HashMap<>();
                map.put("BusStationId", "2");
                OkHttpUtils.getDataFromIntent(path, map, new OkHttpUtils.CallBack() {
                    @Override
                    public void successful(String string) {
                        Log.e(TAG, "successful: body2:" + string);
                        parseJosn(string);
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void exception(Exception e) {

                    }
                });
            }

            @Override
            public void exception(Exception e) {

            }
        });

    }
    //[{"Distance":79700,"BusId":1},{"Distance":41000,"BusId":2}]\n
    private void parseJosn(String string) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = null;
        try {
            JSONArray ja = new JSONArray(string);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String distance = jo.getString("Distance");
                int busId = jo.getInt("BusId");
                map = new HashMap<>();
                map.put("dis"+i, distance);
                map.put("id"+i, busId+"");
                list.add(map);
            }
            mChild.add(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mElv = findViewById(R.id.elv);
    }

    class MyAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return mGroup.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChild.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroup.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mChild.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.elv_group_item, null);
                viewHolder = new ViewHolder();
                viewHolder.gtv = view.findViewById(R.id.gtv);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (mGroup != null && mGroup.size() > 0) {
                viewHolder.gtv.setText(mGroup.get(groupPosition));
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.elv_child_item, null);
                viewHolder = new ViewHolder();
                viewHolder.ctvId = view.findViewById(R.id.ctvid);
                viewHolder.ctvDis = view.findViewById(R.id.ctvdis);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (mChild != null && mChild.size() > 0) {
                viewHolder.ctvDis.setText(mChild.get(groupPosition).get(childPosition).get("dis"+childPosition)+"米");
                viewHolder.ctvId.setText(mChild.get(groupPosition).get(childPosition).get("id"+childPosition)+"号公交距本站距离:");
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class ViewHolder{
        TextView gtv, ctvId, ctvDis;
    }
}
