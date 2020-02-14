package com.desperado.dice;

/**
 * Created liuxun on 2020-02-07
 * Email:liuxun@yy.com
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * description： BitmapUtil
 * author：pz
 * data：2016/10/24
 */
public class BitmapUtil {

    public static void saveImageToGallery(final Context context, final Bitmap bitmap) {
        //确认有没有SD卡
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    String imgName =  "dice.jpg";
                    String s = null;
                    Cursor cursor = null;
                    try {
                        s = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, imgName, null);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        cursor = ((Activity) context).managedQuery(Uri.parse(s), proj, null, null, null);
                        if (cursor == null) {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(s)));
                        } else {
                            //兼容华为4x（型号） 5.0系统问题（保存进文件后，相册并没有刷新）
                            int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            String img_path = cursor.getString(actual_image_column_index);
                            MediaScannerConnection.scanFile(context, new String[]{img_path}, null, null);
                        }
                    } catch (Exception e) {
                        Log.i("BitmapUtil", e.toString());
                    }
                }
            }
        }).start();
    }
}
