package com.example.zw_engineering.util;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.zw_engineering.R;
import com.example.zw_engineering.ui.addition.AdditionFragment;
import com.example.zw_engineering.ui.calendar.CalendarFragment;
import com.example.zw_engineering.ui.home.HomeFragment;
import com.example.zw_engineering.ui.searching.SearchingFragment;
import com.example.zw_engineering.ui.setting.SettingFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class MyFragmentUtils {
    public static HashMap<String, WeakReference<Fragment>> hm = new HashMap<>();
    public static Fragment showCurrentFragment = null;
    public static ArrayList<String> showFragments = new ArrayList<>();

    public static void addNotMain(@NonNull final FragmentManager fm,
                                  @NonNull final Fragment add,
                                  @IdRes final int containerId) {
        addNotMain(fm, add, containerId, false, false);
    }

    public static void addNotMain(FragmentManager fm, Fragment add,
                                  @IdRes final int containerId,
                                  final boolean isHide,
                                  final boolean isAddStack) {
//        WeakReference<Fragment> fragmentWeakReference = hm.get(add.getClass().getName());
//        if(fragmentWeakReference !=null&&fragmentWeakReference.get()!=null){
//            Fragment fragment = fragmentWeakReference.get();
//            hideMainFragment = showCurrentFragment;
//            FragmentUtils.hide(showCurrentFragment);
//            showCurrentFragment = fragment;
//            FragmentUtils.show(fragment);
//        }else {
//            hm.put(add.getClass().getName(),new WeakReference<Fragment>(add));
//            hideMainFragment = showCurrentFragment;
//            FragmentUtils.hide(showCurrentFragment);
//            showCurrentFragment = add;
//            FragmentUtils.add(fm, add, containerId, isHide, isAddStack);
//        }
        //这里需要的是换新的Fragment,保存不了Fragment状态
        hm.put(add.getClass().getName(), new WeakReference<Fragment>(add));
        FragmentUtils.hide(showCurrentFragment);
        showCurrentFragment = add;
        FragmentUtils.add(fm, add, containerId, isHide, isAddStack);
        if (isAddStack == true) {
            showFragments.add(add.getClass().getName());
        }
    }


    public static void addMain(@NonNull final FragmentManager fm,
                               @IdRes final int containerId) {
        hm.put(AdditionFragment.class.getName(), new WeakReference<Fragment>(new AdditionFragment()));
        hm.put(CalendarFragment.class.getName(), new WeakReference<Fragment>(new CalendarFragment()));
        hm.put(HomeFragment.class.getName(), new WeakReference<Fragment>(new HomeFragment()));
        hm.put(SearchingFragment.class.getName(), new WeakReference<Fragment>(new SearchingFragment()));
        hm.put(SettingFragment.class.getName(), new WeakReference<Fragment>(new SettingFragment()));
        FragmentUtils.add(fm, hm.get(AdditionFragment.class.getName()).get(), containerId, true);
        FragmentUtils.add(fm, hm.get(CalendarFragment.class.getName()).get(), containerId, true);
        FragmentUtils.add(fm, hm.get(HomeFragment.class.getName()).get(), containerId, true);
        FragmentUtils.add(fm, hm.get(SearchingFragment.class.getName()).get(), containerId, true);
        FragmentUtils.add(fm, hm.get(SettingFragment.class.getName()).get(), containerId, true);
//        FragmentUtils.addNotMain(fm, hm.values().toArray(new Fragment[0]), containerId, null, 127);
//        Fragment fragment = hm.get(HomeFragment.class.getName()).get();
        FragmentUtils.show(getFragment(hm.get(SearchingFragment.class.getName()), SearchingFragment.class));
        showCurrentFragment = hm.get(SearchingFragment.class.getName()).get();
        showFragments.clear();
        showFragments.add(showCurrentFragment.getClass().getName());
    }

    public static Fragment getFragment(WeakReference<Fragment> weak, Class clazz) {
        if (weak != null && weak.get() != null) {
            return weak.get();
        }
        LogUtils.e("创建了新Fragment:"+clazz);
        Fragment fragment = null;
        if (clazz.getName().equals(HomeFragment.class.getName())) {
            fragment = new HomeFragment();
        } else if (clazz.getName().equals(AdditionFragment.class.getName())) {
            fragment = new AdditionFragment();
        } else if (clazz.getName().equals(CalendarFragment.class.getName())) {
            fragment = new CalendarFragment();
        } else if (clazz.getName().equals(SearchingFragment.class.getName())) {
            fragment = new SearchingFragment();
        } else if (clazz.getName().equals(SettingFragment.class.getName())) {
            fragment = new SettingFragment();
        }
//        else if(clazz.getName().equals(HomeFragment.class.getName())){
//            fragment = new HomeFragment();
//        }else if(clazz.getName().equals(HomeFragment.class.getName())){
//            fragment = new HomeFragment();
//        }else if(clazz.getName().equals(HomeFragment.class.getName())){
//
//        }
        if (fragment == null) {
            fragment = new HomeFragment();
        }
        putFragment(fragment);
        return fragment;
    }

    public static void putFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        hm.put(fragment.getClass().getName(), new WeakReference<>(fragment));
    }

    public static void show(AppCompatActivity activity, int index) {
        if (showFragments.size() >= 2) {
            for (int i = showFragments.size() - 1; i > 0; i--) {
                hm.get(showFragments.get(i)).get().getFragmentManager().popBackStack();
            }
        }
        showFragments.clear();
        switch (index) {
            case 0:
                if (!(showCurrentFragment instanceof HomeFragment)) {
                    show(HomeFragment.class, activity);
                }
                break;
            case 1:
                if (!(showCurrentFragment instanceof SettingFragment)) {
                    show(SettingFragment.class,activity);
                }
                break;
            case 2:
                if (!(showCurrentFragment instanceof AdditionFragment)) {
//                    show(AdditionFragment.class,activity);
                    FragmentManager fragmentManager = showCurrentFragment.getFragmentManager();
                    FragmentUtils.remove(showCurrentFragment);
                    AdditionFragment fragment = new AdditionFragment();
                    FragmentUtils.add(fragmentManager, fragment, R.id.nav_host_fragment);
                    putFragment(fragment);
                    showCurrentFragment = fragment;
                } else {
                    FragmentManager fragmentManager = showCurrentFragment.getFragmentManager();
                    FragmentUtils.remove(showCurrentFragment);
                    AdditionFragment fragment = new AdditionFragment();
                    FragmentUtils.add(fragmentManager, fragment, R.id.nav_host_fragment);
                    putFragment(fragment);
                    showCurrentFragment = fragment;
                }
                break;
            case 3:
                if (!(showCurrentFragment instanceof CalendarFragment)) {
                    show(CalendarFragment.class,activity);
                }
                break;
            case 4:
                if (!(showCurrentFragment instanceof SearchingFragment)) {
                    show(SearchingFragment.class,activity);
                }
                break;
        }
        FragmentUtils.show(showCurrentFragment);
        showFragments.add(showCurrentFragment.getClass().getName());
    }

    private static void show(Class clazz, AppCompatActivity activity) {
        Fragment fragment = getFragment(hm.get(clazz.getName()), clazz);
        FragmentManager manager = activity.getSupportFragmentManager();
        if (fragment==showCurrentFragment){
            return;
        }
        if (fragment.getFragmentManager() == null) {
            FragmentUtils.add(manager, fragment, R.id.nav_host_fragment);
            FragmentUtils.show(fragment);
            if (showCurrentFragment != null) {
                FragmentUtils.hide(showCurrentFragment);
            }
        } else {
            if (showCurrentFragment==null){
                FragmentUtils.show(fragment);
            }else {
                FragmentUtils.showHide(fragment, showCurrentFragment);
            }
        }
        showCurrentFragment = fragment;
    }

    public static void showFragment() {
//        LogUtils.e(showFragments.size());
//        if (showFragments.size()<=1){
//            System.exit(0); //退出程序
//        }
        if (showFragments.size() >= 2) {
            Fragment fragment = hm.get(showFragments.get(showFragments.size() - 2)).get();
            FragmentUtils.hide(showCurrentFragment);
//            if (showCurrentFragment.getFragmentManager() != null) {
//                showCurrentFragment.getFragmentManager().popBackStack();
//            }
            FragmentUtils.show(fragment);
            showCurrentFragment = fragment;
            showFragments.remove(showFragments.size() - 1);
        }
    }

    public static void removeAll(FragmentManager fragmentManager) {
        FragmentUtils.removeAll(fragmentManager);
    }
}
