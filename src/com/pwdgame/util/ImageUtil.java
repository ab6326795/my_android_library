package com.pwdgame.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class ImageUtil {
	public static Bitmap drawableToBitmap(Drawable drawable) {
        
        Bitmap bitmap = Bitmap
                        .createBitmap(
                                        drawable.getIntrinsicWidth(),
                                        drawable.getIntrinsicHeight(),
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int width=drawable.getIntrinsicWidth();
        int height=drawable.getIntrinsicHeight();
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
   }
  
	/**
	 * 

    * BitmapFactory.Options.inPurgeable; 
         *  
         * 如果 inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap 
         * 用于存储Pixel的内存空间在系统内存不足时可以被回收， 
         * 在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel）， 
         * 系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组。  
         * 为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据。 
         *  
         * 在inPurgeable为false时表示创建的Bitmap的Pixel内存空间不能被回收， 
         * 这样BitmapFactory在不停decodeByteArray创建新的Bitmap对象， 
         * 不同设备的内存不同，因此能够同时创建的Bitmap个数可能有所不同， 
         * 200个bitmap足以使大部分的设备重新OutOfMemory错误。 
         * 当isPurgable设为true时，系统中内存不足时， 
         * 可以回收部分Bitmap占据的内存空间，这时一般不会出现OutOfMemory 错误。 


	 * @param mContext
	 * @param resId
	 * @return
	 */
	public static Bitmap drawableToBitmap(Context mContext,int resId){
	   
		
    	BitmapFactory.Options opt = new BitmapFactory.Options();
    	opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
    	opt.inPurgeable = true;
    	opt.inInputShareable = true;
    	
    	Drawable drawable=mContext.getResources().getDrawable(resId);
    	//int width=drawable.getIntrinsicWidth();
    	//int height=drawable.getIntrinsicHeight();
    	//opt.outHeight=mContext.getResources().getDrawable(resId).getIntrinsicHeight()
    	// 获取资源图片
    	InputStream is = mContext.getResources().openRawResource(resId);
    	return BitmapFactory.decodeStream(is, null, opt);
	}
	public static void saveBitmap(Bitmap mBitmap,File saveFile)  {
        File f = saveFile;
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    } 
/*	public static Bitmap drawableToBitmap(Context mContext,Drawable drawable){
		
	}*/
}
