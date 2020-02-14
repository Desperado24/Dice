package com.desperado.dice

import android.app.Application
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.Toast
import com.yhao.floatwindow.*

/**
 *Created liuxun on 2020-02-07
 *Email:liuxun@yy.com
 */
class DiceApplication : Application() {
    private val TAG = "DiceApplication"
    override fun onCreate() {
        super.onCreate()
        var diceView = DiceView(this)
        FloatWindow
            .with(applicationContext)
            .setView(diceView!!)
            .setWidth(DensityUtil.dip2px(this, 250f)) //设置悬浮控件宽高
            .setHeight(DensityUtil.dip2px(this, 300f))
            .setX(Screen.width, 0.3f)
            .setY(Screen.height, 0.5f)
            .setMoveType(MoveType.slide, 100, -100)
            .setMoveStyle(500, BounceInterpolator())
            .setViewStateListener(mViewStateListener)
            .setPermissionListener(mPermissionListener)
            .setOnDiceClickCallback(object : OnDiceClickCallback {
                override fun onDiceClick() {
                    diceView?.dice()
                }
            })
            .setDesktopShow(true)
            .build()
    }


    private val mPermissionListener = object : PermissionListener {
        override fun onSuccess() {
            Log.d(TAG, "onSuccess")
        }

        override fun onFail() {
            Log.d(TAG, "onFail")
        }
    }

    private val mViewStateListener = object : ViewStateListener {
        override fun onPositionUpdate(x: Int, y: Int) {
            Log.d(TAG, "onPositionUpdate: x=$x y=$y")
        }

        override fun onShow() {
            Log.d(TAG, "onShow")
        }

        override fun onHide() {
            Log.d(TAG, "onHide")
        }

        override fun onDismiss() {
            Log.d(TAG, "onDismiss")
        }

        override fun onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart")
        }

        override fun onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd")
        }

        override fun onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop")
        }
    }

}