package com.pwdgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.pwdgame.animation.AnimationUtils;
import com.pwdgame.library.R;
import com.pwdgame.util.Utility;
import com.pwdgame.util.ViewUtils;
/**
 * 等待光圈效果，类似QQ通话
 * @author xieyuan
 *
 */
public class WaitView extends View implements OnDrawEventListener{

	private static final int UPDATE_MSG_ID=0x01;
	private static final int RESUME_MSG_ID=0x02;
	
	private Bitmap bitmap;
	private int mWidth,mHeight;
	private Paint mPaint=new Paint();
	private Canvas mCanvas=new Canvas();
	
	//当前步骤 0光圈环绕 1光圈散开 2全部显示
	private int step;
	
	private boolean isDestroy=false;
	
	/**
	 * 
	 */
	private int lightDegree=0;
	
	private int lightResetTime;
	
	/**背景*/
	private int backgroundColor=Color.WHITE;
	
	/*** 头像（图标）*/
	private Bitmap iconBitmap;
	
	private int iconWidth,iconHeight,iconStartX,iconStartY;
	
	
	/*************************光环*****************************/
	private Bitmap ringBitmap;
	
	private int ringWidth,ringHeight;
	
	/**图片旋转矩阵*/
	//private Matrix ringBitmapMatrix; 
	
	/**光圈旋转速度*/
	private int ringSpeed;
	
	private int ringX,ringY;
	
	/**旋转角度*/
	private float ringDegrees;
	
	/**********************光环散开**************************/
	private int lightX,lightY;
			
	/**light */
	private int lightStroke;
	
	private int scatterOneWidth;
	private int scatterTwoWidth;
	
	/**散开线条颜色*/
	private int scatterColor=0xffc2c1c0;
	
	/** 光圈最大宽度*/
	private int lightMaxWidth;
	
	private int lightMaxRadius;
	
	/** 光圈当前的宽度*/
	private int lightRadius;	
	
	/**光圈Alpha*/
	private int lightAlpha=255;
	
	/**光圈扩散速度*/
	private int lightSpeed;
	
	private boolean isStop=false;
	
	public WaitView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);				
		init();	
	}

	public WaitView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		parseAttributes(context.obtainStyledAttributes(attrs, 
				R.styleable.WaitView));
		init();	
	}

	public WaitView(Context context) {
		super(context);		
		init();	
	}
	
	private void parseAttributes(TypedArray a){
		Drawable iconDrawable=a.getDrawable(R.styleable.WaitView_iconBitmap);
		Drawable ringDrawable=a.getDrawable(R.styleable.WaitView_ringBitmap);
		scatterOneWidth=(int) a.getDimension(R.styleable.WaitView_scatterOneWidth, 0);
		scatterTwoWidth=(int) a.getDimension(R.styleable.WaitView_scatterTwoWidth,0);
		scatterColor= a.getColor(R.styleable.WaitView_scatterColor,scatterColor);
		step=(int)a.getInt(R.styleable.WaitView_step, 0);
		
		if(iconDrawable!=null){
			iconBitmap=((BitmapDrawable)iconDrawable).getBitmap();
		}/*else{
			iconBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.hux);
		}*/
		
		if(ringDrawable!=null){
			ringBitmap=((BitmapDrawable)ringDrawable).getBitmap();
		}/*else{
			ringBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.hux_ring);
		}*/
		
		if(scatterOneWidth==0){
			Utility.dipToPx(getContext(), 25);	
		}
		if(scatterTwoWidth==0){
			Utility.dipToPx(getContext(), 45);
		}
		
		a.recycle();
	}
	
	private void init(){

		//头像
		
		iconWidth=iconBitmap.getWidth();
		iconHeight=iconBitmap.getHeight();
		
		//光环
	
		ringWidth=ringBitmap.getWidth();
		ringHeight=ringBitmap.getHeight();
		//ringBitmapMatrix=new Matrix();
		
		//线条粗细
		lightStroke=Utility.dipToPx(getContext(), 2);
		
		//外围宽度
	    //lightMaxWidth=Utility.dipToPx(getContext(), 50);

		//半径
		lightMaxRadius=Math.max(scatterOneWidth, scatterTwoWidth)+iconWidth/2;
		lightRadius=0;
			
		
		lightMaxWidth=scatterOneWidth;
		///////////////
		//mPaint.setColor(Color.BLUE);
				
		/**因为宽度会随屏幕密度不同而不同，所以速度也应该如此*/
		ringSpeed=Utility.dipToPx(getContext(),1.8f);
		lightSpeed=Utility.dipToPx(getContext(), 0.60f);
	    
		mPaint.setStrokeWidth(lightStroke);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		
	}
	
	public void setIcon(Bitmap bitmap){
		this.iconBitmap=bitmap;			
	}
	
	public void setRingBitmap(Bitmap bitmap){
		this.ringBitmap=bitmap;
	}
	
	@Override
	public void setBackgroundColor(int color){
		this.backgroundColor=color;
	}
	/**
	 * 设置当前动画步骤
	 * @param step
	 *  0光圈环绕 1光圈散开 2全部显示
	 */
	public void setStep(int step){
		this.step=step;	
	}
	
	
	public int getIconWidth(){
		if(this.ringBitmap!=null)
			return ringBitmap.getWidth();
		return 0;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){		
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int widthMeasureSpec){
		int result=0;
		int specMode=MeasureSpec.getMode(widthMeasureSpec);  
	    int specSize=MeasureSpec.getSize(widthMeasureSpec);  
	    
	    if(specMode==MeasureSpec.EXACTLY){
	    	//父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
	    	result=specSize;  
	    }else{
	    	//MeasureSpec.UNSPECIFIED:  //UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小; 
	    	 result=lightMaxRadius*2+getPaddingLeft()+getPaddingRight();  
	    	 if(specMode==MeasureSpec.AT_MOST){
	    		 //AT_MOST(至多)，子元素至多达到指定大小的值。
	    		 result = Math.min(result, specSize);
	    	 }
	    	 
	    }
	  
	    return result; 
	}
	
	private int measureHeight(int heightMeasureSpec){
		int result=0;
		int specMode=MeasureSpec.getMode(heightMeasureSpec);  
	    int specSize=MeasureSpec.getSize(heightMeasureSpec);  
 
	    if(specMode==MeasureSpec.EXACTLY){
	    	//父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
	    	result=specSize;  
	    }else{
	    	//MeasureSpec.UNSPECIFIED:  //UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小; 
	    	result=lightMaxRadius*2+getPaddingTop()+getPaddingBottom();    
	    	 if(specMode==MeasureSpec.AT_MOST){
	    		 //AT_MOST(至多)，子元素至多达到指定大小的值。
	    		 result = Math.min(result, specSize);
	    	 }
	    	 
	    }
	  
	    return result;  
	}
	
	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh){
		if(bitmap!=null&&!bitmap.isRecycled()){
			bitmap.recycle();
			bitmap=null;
		}
		bitmap=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mWidth=w;
		mHeight=h;	
		/////////////////////////init
		iconStartX=(mWidth-ringWidth)/2;				 //change
		iconStartY=lightMaxRadius-ringHeight/2;
		
		ringX=iconStartX-(ringWidth-iconWidth)/2;
		ringY=iconStartY-(ringHeight-iconHeight)/2;
		
		lightX=mWidth/2;
		lightY=lightMaxRadius;				
		
		mCanvas.setBitmap(bitmap);		
		
		//默认绘制一次
		OnDrawEvent(mCanvas,-1);
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		synchronized (this) {
			if(bitmap!=null){
				canvas.drawBitmap(bitmap, 0,0, mPaint);
			}				
		}
		mHandler.sendEmptyMessage(UPDATE_MSG_ID);
	}
	
	/**
	 * 绘图
	 */
	@Override
	public  void OnDrawEvent(Canvas canvas,int step)  {

		if(iconBitmap==null||ringBitmap==null)
			return;
		synchronized (iconBitmap) {
			if(step==1||step==2){

				//绘制圆弧
				mPaint.setARGB(lightAlpha,Color.red(scatterColor),Color.green(scatterColor),Color.blue(scatterColor));
				
				canvas.drawCircle(lightX, lightY, lightRadius+iconWidth/2, mPaint);
				
				mPaint.setAlpha(255);
			}
			//绘制图片 ,大小和光圈
			ViewUtils.drawImage(mCanvas, iconBitmap, iconStartX, iconStartY,ringWidth,ringHeight,0,0,iconBitmap.getWidth(),iconBitmap.getHeight());
			
			if(step==0||step==2){

				//ringBitmapMatrix.reset();			
				
				/* * 旋转 1 */
				canvas.save();
				canvas.rotate(ringDegrees,iconStartX+iconWidth/2,iconStartY+iconHeight/2);
				ViewUtils.drawImage(mCanvas,ringBitmap,ringX,ringY, mPaint);
				canvas.restore();			
				//旋转2
				/*ringBitmapMatrix.setRotate(ringDegrees,ringWidth/2,ringHeight/2);
				//平移到指定位置
				ringBitmapMatrix.postTranslate(iconStartX-(ringWidth-iconWidth)/2,iconStartY-(ringHeight-iconHeight)/2);
				canvas.drawBitmap(ringBitmap,ringBitmapMatrix, mPaint);*/
				
	/*			
				//创建旋转的图片			
				 Bitmap bitmapcute = Bitmap.createBitmap(ringBitmap, 0, 0, ringWidth, ringHeight, ringBitmapMatrix, true);
				 int newW=bitmapcute.getWidth();
				 int newH=bitmapcute.getHeight();*/
				//绘制光圈环绕
				 //Utils.drawImage(mCanvas, bitmapcute,(mWidth-newW)/2, iconStartY-(newH-ringHeight)/2, mPaint);
				//Utils.drawImage(mCanvas, bitmapcute, iconStartX-(newW-ringWidth)/2, iconStartY-(newH-ringHeight)/2, mPaint);		
				//销毁			
				//bitmapcute=null;			

			}	
		}

		invalidate();
	}
	
	/**
	 * 逻辑管理
	 */
	public synchronized void OnLogic(int step){

		if(step==0||step==2){
			//光圈环绕
			ringDegrees+=ringSpeed;
			if(ringDegrees>=360){
				ringDegrees=0;
			}
			
		}
        
		if(step==1||step==2){  //光圈散开
			if(isStop)
	        	return;
			
			if(lightRadius>=lightMaxWidth&&lightAlpha<=0){
				lightRadius=0;				
				lightDegree++;
				if(lightDegree==0||lightDegree==2){
				   lightMaxWidth=scatterOneWidth;
				   
				   if(lightDegree==2){ //第三次就要暂停一会		
						isStop=true;
						lightMaxWidth=0;
					    lightDegree=0;
					    lightResetTime=2000;
						mHandler.sendEmptyMessageDelayed(RESUME_MSG_ID, lightResetTime);
				   }
				}else if(lightDegree==1){ //第二次范围更大
					lightMaxWidth=scatterTwoWidth;
				}
				
				
			}
	
			lightRadius+=lightSpeed;
			//这里不这样255/lightMaxWidth，是因为会有舍入导致alpha偏差
			lightAlpha=(int) (255f/lightMaxWidth*(Math.max(lightMaxWidth-lightRadius,0)));			
        }
	}
	
	
	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case UPDATE_MSG_ID:
					
					bitmap.eraseColor(0);								
					//绘制
					OnDrawEvent(mCanvas,step);										
					//逻辑变化
					OnLogic(step);
					
					mHandler.removeMessages(UPDATE_MSG_ID);
					if(!isDestroy)
					   mHandler.sendEmptyMessageDelayed(UPDATE_MSG_ID, 150);
					break;
				case RESUME_MSG_ID:			
					lightMaxWidth=scatterOneWidth;
					isStop=false;					
					break;
				default:
					break;
			}
		};
	};
	
	
	/**
	 * 销毁
	 */
	public synchronized void recycle(){

		isDestroy=true;
		mHandler.removeMessages(UPDATE_MSG_ID);
		mCanvas=null;

		synchronized (this) {
			
			if(iconBitmap!=null&&!iconBitmap.isRecycled()){
				iconBitmap.recycle();
				iconBitmap=null;
			}
			if(ringBitmap!=null&&!ringBitmap.isRecycled()){
				ringBitmap.recycle();
				ringBitmap=null;
			}
			
			if(bitmap!=null&&!bitmap.isRecycled()){
				bitmap.recycle();
				bitmap=null;
			}
		}

	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		recycle();
	}
}

interface OnDrawEventListener{
	public void OnDrawEvent(Canvas canvas,int type);
}
