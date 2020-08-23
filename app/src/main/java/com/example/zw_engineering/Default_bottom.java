package com.example.zw_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zw_engineering.bean.CalendarBean;
import com.example.zw_engineering.ui.addition.AdditionFragment;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyFragmentUtils;
import com.example.zw_engineering.util.MyNetWorkManager;
import com.example.zw_engineering.util.MySelectViewManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class Default_bottom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_setting, R.id.navigation_addition, R.id.navigation_calendar, R.id.navigation_searching)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        //这里不使用这个
        MyFragmentUtils.addMain(getSupportFragmentManager(), R.id.nav_host_fragment);
//        MyFragmentUtils.show(this,4); //最好不要用这个,因为退出一次FragmentManager有记录,再进来就会有问题(没有使用add进视图)

        findViewById(R.id.fragment_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.show(Default_bottom.this, 0);
            }
        });
        findViewById(R.id.fragment_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MySelectViewManager().init(Default_bottom.this, new MySelectViewManager.SelectOption() {
                    @Override
                    public void select(int option) {
                        if (option == 0) {
                            //TODO 这里需要跳转到修改密码页面
                            ToastUtils.showLong("Change Password");
                        } else {
//                            ToastUtils.showLong("Logout");
                            startActivity(new Intent(Default_bottom.this, MainActivity.class));
                            finish();
                        }
                    }
                }, "Change Password", "Logout");
//                MyFragmentUtils.show(Default_bottom.this,1);
            }
        });
        findViewById(R.id.fragment_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MySelectViewManager().init(Default_bottom.this, new MySelectViewManager.SelectOption() {
                    @Override
                    public void select(int option) {
                        if (option == 0) {
                            AdditionFragment.reportID = null;
                            MyFragmentUtils.show(Default_bottom.this, 2);
                        } else {
                            new MySelectViewManager().dateSelect(new MySelectViewManager.SelectDate() {
                                @Override
                                public void select(Date date) {
                                    String date2String = TimeUtils.date2String(date, "yyyyMMdd");
                                    final ArrayList<String> list = new ArrayList<>();
                                    if (ConstantUtil.search_list != null && ConstantUtil.search_list.size() < 500) {
                                        for (int i = 0; i < ConstantUtil.search_list.size(); i++) {
                                            if (ConstantUtil.search_list.get(i).reportID.contains(date2String)) {
                                                list.add(ConstantUtil.search_list.get(i).reportID);
                                            }
                                        }
                                        if(list.size()<=0){
                                            ToastUtils.showLong("No report");
                                            return;
                                        }
                                        new MySelectViewManager().init(Default_bottom.this, list, new MySelectViewManager.SelectOption() {
                                            @Override
                                            public void select(int option) {
                                                AdditionFragment.reportID = list.get(option);
                                                LogUtils.e(AdditionFragment.reportID);
                                                MyFragmentUtils.show(Default_bottom.this, 2);
                                            }
                                        });
                                    } else {
                                         date2String = TimeUtils.date2String(date, "yyyy-MM-dd");
                                        final ArrayList<CalendarBean> beans = new ArrayList<>();
                                        MyNetWorkManager.calendarGetReportIDData(date2String, beans, new Runnable() {
                                            @Override
                                            public void run() {
                                                list.clear();
                                                for (int i = 0; i < beans.size(); i++) {
                                                    list.add(beans.get(i).reportID);
                                                }
                                                if(list.size()<=0){
                                                    ToastUtils.showLong("No report");
                                                    return;
                                                }
                                                new MySelectViewManager().init(Default_bottom.this, list, new MySelectViewManager.SelectOption() {
                                                    @Override
                                                    public void select(int option) {
                                                        AdditionFragment.reportID = list.get(option);
                                                        LogUtils.e(AdditionFragment.reportID);
                                                        MyFragmentUtils.show(Default_bottom.this, 2);
                                                    }
                                                });
                                            }
                                        });
                                    }

                                }
                            });
//                            AdditionFragment.reportID = ConstantUtil.last_report_id;
                        }

                    }
                }, "add new", "show last report");

            }
        });
        findViewById(R.id.fragment_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentUtils.show(Default_bottom.this, 3);
            }
        });
        findViewById(R.id.fragment_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("点击了" + 4);
                MyFragmentUtils.show(Default_bottom.this, 4);
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        for (int i = 0; i < fragments.size(); i++) {
//            LogUtils.e("onCreate"+fragments.get(i).getClass());
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyFragmentUtils.showFragment();
    }
}
