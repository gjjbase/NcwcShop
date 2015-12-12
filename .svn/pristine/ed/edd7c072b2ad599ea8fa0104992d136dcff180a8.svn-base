package com.ncwc.shop.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.view.View;

/**
 * 图片处理类 本地图片转化成Base64
 *
 * @author Administrator
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ImageTools {
    /**
     * 文件转bitmap
     *
     * @param filepath
     * @return
     */
    public static Bitmap pathbitmap(String filepath) {
        FileInputStream fs = null;
        BufferedInputStream bs = null;
        BitmapFactory.Options options = null;
        try {
            fs = new FileInputStream(filepath);
            bs = new BufferedInputStream(fs);
            options = setBitmapOption(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(bs, null, options);
    }

    /**
     * 文件转base64
     *
     * @param filepath
     * @return
     */
    public static String bitmap64(String filepath) {
        FileInputStream fs = null;
        BufferedInputStream bs = null;
        Bitmap bt = null;
        String encode = null;
        BitmapFactory.Options options = null;
        try {
            fs = new FileInputStream(filepath);
            bs = new BufferedInputStream(fs);
            options = setBitmapOption(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        bt = BitmapFactory.decodeStream(bs, null, options);

//        bt = BitmapFactory.decodeFile(filepath);
        bt = compressImageFromFile(filepath);
        try {
            // 先将bitmap转换为普通的字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bt.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            // 将普通字节数组转换为base64数组
            encode = new String(Base64.encode(buffer, Base64.DEFAULT), "UTF-8");
            // // encode = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bs.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;

    }

    private static BitmapFactory.Options setBitmapOption(String file) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        // 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
        BitmapFactory.decodeFile(file, opt);

        // int outWidth = opt.outWidth; // 获得图片的实际高和宽
        // int outHeight = opt.outHeight;
        opt.inDither = false;
        opt.inPreferredConfig = Config.RGB_565;
        // 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
        opt.inSampleSize = 1;
        // 设置缩放比,1表示原比例，2表示原来的四分之一....
        // 计算缩放比
        // if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
        // int sampleSize = (outWidth / width + outHeight / height) / 2;
        // opt.inSampleSize = sampleSize;
        // }

        opt.inJustDecodeBounds = false;// 最后把标志复原
        return opt;
    }

    // 图片压缩
    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1080f;//
        float ww = 720f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 高斯模糊
     *
     * @param bkg
     * @param view
     */
    @SuppressLint("NewApi")
    public static void blur(Context context, Bitmap bkg, View view) {
        float scaleFactor = 16;
        float radius = 4;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap转Drawable
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable convertBitmap2Drawable(Context context, Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bitmap);
        // 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        return bd;
    }

    /**
     * bitmap转文件
     *
     * @param bitmap
     * @param file
     */
    public static void saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
