package com._basebase.base.myeditcore.mybutterknife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 手写 注解框架
 */
public class ButterKnife {

    /**
     * 注入
     */
    public static void inject(Activity activity) {
        injectContentView(activity);
        findViewById(activity);
        setOnClickListener(activity);
        setOnLongClickListener(activity);
    }

    /**
     * 注入主布局文件
     */
    private static void injectContentView(Activity activity) {
        // 获取到类对象
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView注解
        MyContentView contentView = clazz.getAnnotation(MyContentView.class);
        // 存在
        if (contentView != null) {
            // 获取到布局ID
            int contentViewLayoutId = contentView.value();
            try {
                // 获取类对象的方法
                Method method = clazz.getMethod("setContentView", int.class);
                // 这个必须要设置
                method.setAccessible(true);
                // 调用类对象的方法
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 声明 View
     */
    private static void findViewById(Activity activity) {
        //获取Activity的class
        Class<? extends Activity> clazz = activity.getClass();
        //获取该类中的所有声明的属性
        Field[] declaredFields = clazz.getDeclaredFields();
        //遍历所有属性，找到用@ViewById注解了的属性
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            //获取属性上的注解对象
            //@ViewById(R.id.textView) R.id.textView--value
            //TextView textView;//属性
            MyBindView annotation = field.getAnnotation(MyBindView.class);
            if (annotation != null) {
                // 获取设置的 ID
                int viewId = annotation.value();
                // TODO 是不是很熟悉
                View view = activity.findViewById(viewId);
                try {
                    //私有属性也可以动态注入（不写该句代码，private声明的属性会报异常）
                    field.setAccessible(true);
                    // 设置属性
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理OnClick注解
     */
    private static void setOnClickListener(final Activity activity) {
        // findViewById  setOnClick
        // 1.获取该Activity的所有方法
        Class<?> clazz = activity.getClass();

        // 下面的方法是获取所有的方法
        //Method[] methods = clazz.getDeclaredMethods();
        // 2.遍历方法获取所有的值
        //for (final Method method:methods){
        // 2.1 获取OnClick注解
        //OnClickView onClick = method.getAnnotation(OnClickView.class);

        try {

            //            Method[] declaredMethods = clazz.getDeclaredMethods();//            不写死方法名使用这个
            // 获取到点击事件的注解 注意这里的方法名必须是 onClick()
            final Method method = clazz.getMethod("onClick", View.class);
            OnClick onClick = method.getAnnotation(OnClick.class);
            // 2.2 该方法上是否有OnClick注解
            if (onClick != null) {
                // 2.3 获取OnClick里面所有的值
                int[] viewIds = onClick.value();// @OnClick({R.id.text_view,R.id.button})

                // 2.4 先findViewById , setOnclick
                for (int viewId : viewIds) {
                    // 先findViewById
                    final View view = activity.findViewById(viewId);
                    // 后设置setOnclick
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 首先需要判断 方法是否需要检测网络
                            // 3.反射调用原来配置了OnClick的方法
                            method.setAccessible(true);// 私有的方法

                            try {
                                method.invoke(activity);// 调用无参的方法
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    method.invoke(activity, view);// 调用有参的方法 view 代表当前点击的View
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
//            }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理onLongClick注解
     */
    private static void setOnLongClickListener(final Activity activity) {
        try {
            Class<?> clazz = activity.getClass();
            final Method method = clazz.getMethod("onLongClick", View.class);
            OnLongClick onClick = method.getAnnotation(OnLongClick.class);
            if (onClick != null) {
                int[] viewIds = onClick.value();
                for (int viewId : viewIds) {
                    final View view = activity.findViewById(viewId);
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            method.setAccessible(true);
                            try {
                                method.invoke(activity);
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    method.invoke(activity, view);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            return true;
                        }
                    });
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}