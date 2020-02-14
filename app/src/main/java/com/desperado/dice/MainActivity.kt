package com.desperado.dice

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.animation.BounceInterpolator
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import com.yhao.floatwindow.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 *Created liuxun on 2020-02-07
 *Email:liuxun@yy.com
 */
class MainActivity : Activity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pref = getSharedPreferences("dice", Context.MODE_PRIVATE)
        var diceNumLimit = pref.getInt("diceNumLimit", 3)
        when (diceNumLimit) {
            3 -> {
                rg_dice.check(rb_3.id)
            }
            4 -> {
                rg_dice.check(rb_4.id)
            }
            5 -> {
                rg_dice.check(rb_5.id)
            }
        }
        rg_dice.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when (p1) {
                    rb_3.id -> {
                        diceNumLimit = 3
                    }
                    rb_4.id -> {
                        diceNumLimit = 4
                    }
                    rb_5.id -> {
                        diceNumLimit = 5
                    }
                }
                val editor = getSharedPreferences("dice", Context.MODE_PRIVATE).edit()
                editor.putInt("diceNumLimit", diceNumLimit)
                editor.commit()
            }
        })
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }
}