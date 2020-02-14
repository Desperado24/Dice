package com.desperado.dice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.CountDownTimer
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_dice.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random
import android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.FileNotFoundException


/**
 *Created liuxun on 2020-02-07
 *Email:liuxun@yy.com
 */
class DiceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    var diceResList = mutableListOf<String>()
    var diceAdapter: DiceAdapter? = null
    val pref = context.getSharedPreferences("dice", Context.MODE_PRIVATE)
    var times = 0
    var isSave = false

    private val SHARE_IMAGE_FILE_ROOT_ROOT_PATH =
        Environment.getRootDirectory().path + "/dice/"
    private val SHARE_IMAGE_FILE_ROOT_PATH = SHARE_IMAGE_FILE_ROOT_ROOT_PATH + "Camera/"
    private val SHARE_IMAGE_FILE_PATH = SHARE_IMAGE_FILE_ROOT_PATH + "dice.jpg"

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_dice, this)
        var diceNumLimit = pref.getInt("diceNumLimit", 3)
        rv_dice.layoutManager = GridLayoutManager(context, diceNumLimit)
        diceAdapter = DiceAdapter(context)
        rv_dice.adapter = diceAdapter
        diceResList.add("https://img.xiazaizhijia.com/uploads/2016/0505/20160505084809808.gif")
        diceResList.add("https://img.xiazaizhijia.com/uploads/2016/0505/20160505084833951.gif")
        diceResList.add("https://img.xiazaizhijia.com/uploads/2016/0505/20160505084857542.gif")
        diceResList.add("https://img.xiazaizhijia.com/uploads/2016/0505/20160505084916366.gif")
        diceResList.add("https://img.xiazaizhijia.com/uploads/2016/0505/20160505084944502.gif")
        diceResList.add("https://img.xiazaizhijia.com/uploads/2016/0505/20160505085005540.gif")
    }

    fun dice() {
        timer.cancel()
        var diceNumLimit = pref.getInt("diceNumLimit", 3)
        rv_dice.layoutManager = GridLayoutManager(context, diceNumLimit)
        Log.i("DiceView", "diceNumLimit:diceNumLimit" + diceNumLimit + "\n\n")
        var diceResultList = mutableListOf<String>()
        for (index in 1..diceNumLimit) {
            var diceNum = Random.nextInt(0, diceResList.size)
            Log.i("DiceView", "diceNum" + (diceNum + 1))
            diceResultList.add(diceResList.get(diceNum))
        }
        var hasBoom = true
        var lastResult = diceResultList.get(0)
        diceResultList.forEach {
            if (!lastResult.equals(it)) {
                hasBoom = false
            }
        }
//        if (hasBoom) {
//            iv_dice.setImageResource(R.drawable.ico_bg)
//        }
        diceAdapter?.updateData(diceResultList)
        times++
        tv_dice_num.setText("次数：" + times)
        timer.start()
    }

    var timer = object : CountDownTimer(5000, 5000) {
        override fun onTick(sin: Long) {
        }

        override fun onFinish() {
            BitmapUtil.saveImageToGallery(context, generatBitmap(this@DiceView))
        }
    }

    private fun generatBitmap(recyclerView: RelativeLayout): Bitmap {
        var bitmap = Bitmap.createBitmap(
            recyclerView.getWidth(),
            recyclerView.getHeight(),
            Bitmap.Config.ARGB_8888
        );
        var c = Canvas(bitmap);
        c.drawColor(Color.WHITE);
        recyclerView.draw(c);
        return bitmap
    }
}