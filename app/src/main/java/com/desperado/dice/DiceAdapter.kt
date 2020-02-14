package com.desperado.dice

/**
 *Created liuxun on 2020-02-07
 *Email:liuxun@yy.com
 */


import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.reflect.Field


class DiceAdapter(context: Context) :
    RecyclerView.Adapter<DiceAdapter.ViewHolder>() {
    private var diceResultList = mutableListOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_dice: ImageView

        init {
            iv_dice = view.findViewById(R.id.iv_dice) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.iv_dice.context)
            .asGif()
            .load(diceResultList.get(position))
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?, model: Any,
                    target: Target<GifDrawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    gifDrawable: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    try {
                        //设置循环播放次数为1次
                        gifDrawable.setLoopCount(1)
                        // 计算动画时长
                        var duration = 0
                        //GifDecoder decoder = gifDrawable.getDecoder();//4.0开始没有这个方法了
                        val state = gifDrawable.constantState
                        if (state != null) {
                            //不能混淆GifFrameLoader和GifState类
                            val gifFrameLoader = getValue(state, "frameLoader")
                            if (gifFrameLoader != null) {
                                val decoder = getValue(gifFrameLoader, "gifDecoder")
                                if (decoder != null && decoder is GifDecoder) {
                                    for (i in 0 until gifDrawable.frameCount) {
                                        duration += (decoder as GifDecoder).getDelay(i)
                                    }
                                }
                            }
                        }
                    } catch (e: Throwable) {
                    }

                    return false
                }
            })
            .into(holder.iv_dice)
    }

    override fun getItemCount(): Int {
        return diceResultList?.size
    }

    fun updateData(result: MutableList<String>) {
        this.diceResultList = result
        notifyDataSetChanged()
    }

    /**
     * 通过字段名从对象或对象的父类中得到字段的值
     * @param object 对象实例
     * @param fieldName 字段名
     * @return 字段对应的值
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getValue(`object`: Any?, fieldName: String): Any? {
        if (`object` == null) {
            return null
        }
        if (TextUtils.isEmpty(fieldName)) {
            return null
        }
        var field: Field? = null
        var clazz: Class<*> = `object`.javaClass
        while (clazz != Any::class.java) {
            try {
                field = clazz.getDeclaredField(fieldName)
                field!!.setAccessible(true)
                return field!!.get(`object`)
            } catch (e: Exception) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }

            clazz = clazz.getSuperclass()!!
        }

        return null
    }
}