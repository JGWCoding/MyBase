package com._basebase.base.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * 填充 顶部状态栏 和 系统底部导航栏或者叫底部虚拟按钮(返回键和home键和返回首页键)
 */
 class FillStatusBarAndNavView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        apply {
            requestApplyInsets()
        }
    }
}