package com.dgkj.tianmi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dgkj.tianmi.listener.OnPictureCompressFinishListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/** Bitmap压缩工具
 * Created by Android004 on 2016/11/8.
 */
public class BitmapCompressUtil {
    private  int targetWidth,targetHeight;//屏幕宽高
    private Context context;

    public BitmapCompressUtil(Context context) {
        this.context = context;
        targetWidth = Integer.parseInt(SharedPreferencesUnit.getInstance(context).get("windowWidth"));
        targetHeight = Integer.parseInt(SharedPreferencesUnit.getInstance(context).get("windowHeight"));
    }

    /**
     * 根据图片文件路径按屏幕宽高比例大小压缩图片
     * @param filepath 文件路径
     * @return 压缩后的bitmap
     */
    public  void compressFromFile(String filepath,OnPictureCompressFinishListener listener){
        LogUtil.i("TAG","准备压缩图片---");
        Bitmap bitmap = null;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;//只解码图片边框
        bitmap = BitmapFactory.decodeFile(filepath,option);
        int realWidth = option.outWidth;//图片真实宽高
        int realHeight = option.outHeight;
        int rate = 1;//压缩比例，默认为1(宽高各为压缩对象的 1/rate)
        if(realWidth>realHeight && realWidth>targetWidth){//真实宽大于高且大于屏宽
            rate = realWidth/targetWidth;//以真实宽与屏宽比例为准
        }else if(realHeight>realWidth && realHeight>targetHeight ) {//真实高大于宽且大于屏高
            rate = realHeight/targetHeight;//以真实高与屏高比例为准
        }
        if(rate < 1){
            rate = 1;
        }
        option.inJustDecodeBounds = false;
        option.inSampleSize = rate;
        bitmap = BitmapFactory.decodeFile(filepath, option);
        LogUtil.i("TAG","图片初次压缩完成===="+bitmap);
         compressQuality(bitmap,listener);//按屏幕宽高压缩后根据质量比例再次压缩
    }


    /**
     * 根据指定质量比例压缩bitmap
     * @param bitmap
     * @return
     */
    public String compressQuality(Bitmap bitmap,OnPictureCompressFinishListener listener){
        LogUtil.i("TAG","开始二次压缩图片");
        Bitmap bm = null;
        int quality = 100;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP,quality,bos);//先不压缩，获取图片大小
       while (bos.toByteArray().length/1024 > 1024*100){//图片大于80k
            quality -= 10;//每次压缩10%直至图片小于80k
           bos.reset();//清空bos,重新压缩
           bitmap.compress(Bitmap.CompressFormat.WEBP,quality,bos);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());//将压缩后的数据流放入新的解码流中
        String dirPath = ImageUtil.getDirPath();
        File file = new File(dirPath, System.currentTimeMillis() + ".png");
        if(!file.exists()){
            try {
                file.getParentFile().mkdir();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buff = new byte[1024*10];
            int b;
            while ((b=bis.read(buff)) != -1){
                    outputStream.write(buff,0,b);
            }
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  bm = BitmapFactory.decodeStream(bis);//不易OOM
        //return bm;
        LogUtil.i("TAG","图片二次压缩完成===="+file.getAbsolutePath());
        listener.onCompressFinish(file.getAbsolutePath());
        return file.getAbsolutePath();
    }


    /**
     * 跟据bitmap的大小按屏幕比例压缩
     * @param bitmap
     * @return
     */
    public String compressFromBitmap(Bitmap bitmap, OnPictureCompressFinishListener listener){
        Bitmap bm = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP,100,bos);
        if(bos.toByteArray().length > 1024*1024){//图片大于1M，先压缩一半
            bos.reset();
            bitmap.compress(Bitmap.CompressFormat.WEBP,50,bos);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());//将压缩后的数据流放入新的解码流中,再根据屏幕宽高再次压缩

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bis,null,option);
        int realWidth = option.outWidth;//图片真实宽高
        int realHeight = option.outHeight;
        int rate = 1;//压缩比例，默认为1(宽高各为压缩对象的 1/rate)
        if(realWidth>realHeight && realWidth>targetWidth){//真实宽大于高且大于屏宽
            rate = realWidth/targetWidth;//以真实宽与屏宽比例为准
        }else if(realHeight>realWidth && realHeight>targetHeight ) {//真实高大于宽且大于屏高
            rate = realHeight/targetHeight;//以真实高与屏高比例为准
        }
        if(rate < 1){
            rate = 1;
        }
        option.inJustDecodeBounds = false;
        option.inSampleSize = rate;
        bis = new ByteArrayInputStream(bos.toByteArray());
        bm = BitmapFactory.decodeStream(bis,null,option);

        return compressQuality(bm,listener);//按屏幕宽高压缩后根据质量比例再次压缩
    }

    }



