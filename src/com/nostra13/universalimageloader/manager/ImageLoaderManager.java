package com.nostra13.universalimageloader.manager;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pwdgame.library.R;
/**
 * 异步图片加载类
 * @author Administrator
 *
 */
public class ImageLoaderManager {

	private static DisplayImageOptions ops;
	private static ImageLoader mImageLoader;
	
	private ImageLoaderManager(){
		
	}
	
	public static void init(Context mContext){
		
		//初始化ImageLoader
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(2097152) //2 * 1024 * 1024
				.denyCacheImageMultipleSizesInMemory()				
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration. 初始化图片加载器
		mImageLoader=ImageLoader.getInstance();
		mImageLoader.init(config);
	}

	public static DisplayImageOptions getDisplayImageOptions(){
		if(ops==null){
			synchronized (mImageLoader) {
				ops = new DisplayImageOptions.Builder()
			      .showStubImage(R.drawable.detailicon_default)
			      .showImageForEmptyUri(R.drawable.detailicon_default)
			      .showImageOnFail(R.drawable.detailicon_default)
			      .cacheInMemory()
			      .cacheOnDisc()
			      .build();							
			}
	
		}		
		return ops;
	}
	
	public static void displayImage(String uri,ImageView imageView){
		mImageLoader.displayImage(uri, imageView,getDisplayImageOptions());
	}
	
	public static void displayImage(String uri,ImageView imageView,DisplayImageOptions options){
		mImageLoader.displayImage(uri, imageView,options);
	}
	/*public static void displayImage(String uri,final ImageView imageView,int width,int height){
		
		ImageSize mImageSize=new ImageSize(width, height);
		
		DisplayImageOptions ops = new DisplayImageOptions.Builder()
	      .showStubImage(R.drawable.detailicon_default)
	      .showImageForEmptyUri(R.drawable.detailicon_default)
	      .showImageOnFail(R.drawable.detailicon_default)
	      .cacheInMemory()
	      .cacheOnDisc()	     
	      .build();			

		
		mImageLoader.loadImage(uri, mImageSize, ops, new SimpleImageLoadingListener(){  
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				
				imageView.setImageBitmap(loadedImage);
			}
		});

	}*/
	
	public static ImageLoader getImageLoader(){
		return mImageLoader;
	}
}
