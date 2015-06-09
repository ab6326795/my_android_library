package com.pwdgame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * 图片剪裁VIEW
 * @author Administrator
 *
 */
public class CropImageView extends View{

	protected Context mContext;	
	protected Bitmap bitmap;
	protected Paint paint=new Paint();
	//输出宽高
	protected int outputW;
	protected int outputH;
	//视图宽高
	protected int width;
	protected int height;	
	//图片宽高
	protected int imgW;
	protected int imgH;
	
	protected int imgX,imgY;
	
	public CropImageView(Context context){
		super(context);
		this.mContext=context;
		setFocusable(true);
	}
	
    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
		this.mContext=context;
		setFocusable(true);
    }
	public CropImageView(Context context,Bitmap bitmap,int outputW,int outputH) {
		super(context);
		this.mContext=context;
		this.bitmap=bitmap;
		this.outputW=outputW;
		this.outputH=outputH;
		this.width=getWidth();
		this.height=getHeight();
		
		imgW=bitmap.getWidth();
		imgH=bitmap.getHeight();
		setFocusable(true);
	}

	public void init(Bitmap bitmap,int outputW,int outputH){
		this.bitmap=bitmap;
		this.outputW=outputW;
		this.outputH=outputH;

		imgW=bitmap.getWidth();
		imgH=bitmap.getHeight();
	}
	
	@Override
	public void draw(Canvas canvas){		
		this.width=getWidth();
		this.height=getHeight();					

		paint.setColor(0xFF000000);
		//居中绘制图片
	
		canvas.drawBitmap(bitmap, new Rect(0,0,imgW,imgH), new Rect(imgX,imgY,imgW+imgX,imgH+imgY), paint);
		
		paint.setColor(0xaa000000);
	
		Rect frame=getFrameRect();
		//上
		canvas.drawRect(0, 0, width, frame.top, paint);
		//左
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		//右
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		//下
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
	
		
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		imgX=(getWidth()-imgW)/2;
		imgY=(getHeight()-imgH)/2;
	}
	/**
	 * 返回剪裁区域
	 * @return
	 */
	public Rect getFrameRect(){
		int left=(width-outputW)/2;
		int right=left+outputW;
		int top=(height-outputH)/2;
		int bottom=top+outputH;
		return new Rect(left, top, right, bottom);
	}
	
	public Bitmap getResultBitmap(){
		Bitmap bit=Bitmap.createBitmap(outputW, outputH, Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bit);
		
		Rect rect=getFrameRect();
		int left=rect.left-imgX;
		int top=rect.top-imgY;
		int right=left+outputW;
		int bottom=top+outputH;
		canvas.drawBitmap(bitmap,  new Rect(left,top, right, bottom), new Rect(0, 0, outputW, outputH), null);
		canvas.save();
		
		return bit;
	}
	
	private int startX,startY;
	@Override
	public boolean onTouchEvent(MotionEvent event){
		int x=(int) event.getX();
		int y=(int) event.getY();
		
		switch(event.getAction()& MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			startX=x;
			startY=y;
			break;
		case MotionEvent.ACTION_MOVE:			
			this.imgX+=x-startX;
			this.imgY+=y-startY;
			checkBoundary();
			startX=x;
			startY=y;
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		invalidate();
		return true;
	}
	
	public void checkBoundary(){
		if(this.imgX<-imgW+10){
			this.imgX=-imgW+10;
		}
		
		if(this.imgX+10>width){
			this.imgX=width-10;
		}
		
		if(this.imgY<-imgH+10){
			this.imgY=-imgH+10;
		}
		
		if(this.imgY+10>height){
			this.imgY=height-10;
		}
	}
}
