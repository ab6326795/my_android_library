package com.pwdgame.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class TextRoundDrawable extends Drawable{

	private Paint mBitmapPaint=new Paint();

	private String text="空";
	private float textSize=100;
	private float radius=100;
	private int textColor=0xFFFFFFFF;
	private int bgColor=0xff9a9a9a;

	//需要绘制的位图
	private Bitmap drawBitmap;
	
	//已经处理完成的位图
	private Bitmap textBitmap;
	
	private Bitmap bkBitmap1,bkBitmap2;
	
	private boolean isPressed=false;

    private Bitmap foreBitmap;

	public TextRoundDrawable(Context mContext,String text,int textSize,int radius){
		if(text==null)
			text="";
		this.text=text.replaceAll(" ", "");  //去除文字中的空格
		this.textSize=textSize;
		this.radius=radius;
        //foreBitmap= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.head_fore);
        
        try {
        	setupPaints(mContext);	
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}
	public TextRoundDrawable(Context mContext,Bitmap bitmap,int radius){
		this.text=null;		
		this.drawBitmap=bitmap;	
		this.radius=radius;
		setupPaints(mContext);
	}
	
	private void setupPaints(Context mContext){
		 mBitmapPaint.setTextSize(textSize);
		 mBitmapPaint.setAntiAlias(true);
		 mBitmapPaint.setTypeface(Typeface.DEFAULT_BOLD);		 
		 /*Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/FangZhengBlod.ttf");//Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
		 mBitmapPaint.setTypeface(font);*/
		 //mBitmapPaint.setFakeBoldText(true);
		 //mBitmapPaint.setStrokeWidth(30);
		 
		 if(text!=null)
			 textBitmap=getTextBitmap(text, (int)radius);
		 else if(drawBitmap!=null)
			 textBitmap=getTextBitmap(drawBitmap);
	}
	
	
	public Bitmap getTextBitmap(){
		return textBitmap;
	}
	private Bitmap getTextBitmap(String name,int r){
		int textLen=name.length();
		if(textLen<=0)
			return null;
		Bitmap output=null;
		try {
			output = Bitmap.createBitmap(r,r, Config.ARGB_8888);	
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}		
		if(output==null)
			return null;
		
		Canvas canvas = new Canvas(output);
		 
		
		 Rect rect = new Rect(0, 0, r,r);
		 RectF rectF = new RectF(rect);
		 
		 mBitmapPaint.setColor(bgColor);
		 
		 canvas.drawARGB(0, 0, 0, 0);
		 canvas.drawRoundRect(rectF, r, r, mBitmapPaint);	
		 
		 mBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    
		 mBitmapPaint.setColor(textColor);
        
		//需要绘制的文字，最多两个，否则一个
		String text=textLen>=2? name.substring(textLen-2, textLen):name;
		//测量绘制所需的宽度
		Rect textRect=new Rect();
		mBitmapPaint.getTextBounds(text, 0, text.length(), textRect);
		int textWidth=(int) mBitmapPaint.measureText(text);//textRect.width();
		int textHeight=textRect.height();
		
		int startX=r>textWidth? (r-textWidth)/2:-((textWidth-r)/2);
		int startY=r>textHeight? (r-textHeight)/2+textHeight:(-((textHeight-r)/2))+textHeight;

        //绘制文字
		canvas.drawText(text, startX, startY, mBitmapPaint);

        //加盖一层遮罩
		if(foreBitmap!=null)
           canvas.drawBitmap(foreBitmap,new Rect(0,0,foreBitmap.getWidth(),foreBitmap.getHeight()),new Rect(0,0,r,r),null);

        foreBitmap=null;
        mBitmapPaint=null; 
		//canvas.drawRoundRect(rectF, r-1, r-1, mBitmapPaint);
		
		return output;
	}
	

	private Bitmap getTextBitmap(Bitmap bitmap){

		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		Bitmap output=null;
		try {
			output = Bitmap.createBitmap((int)radius,(int)radius, Config.ARGB_8888);	
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}		
		if(output==null)
			return null;
		
		Canvas canvas = new Canvas(output);
		 
		
		 Rect rect = new Rect(0, 0, width,height);
		 RectF rectF = new RectF(0,0,radius,radius);
		 
		 mBitmapPaint.setColor(bgColor);
		 
		 canvas.drawARGB(0, 0, 0, 0);
		 canvas.drawRoundRect(rectF, radius,radius, mBitmapPaint);	
		 
		 mBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    
		 mBitmapPaint.setColor(textColor);
        
		 canvas.drawBitmap(bitmap, rect,rectF, mBitmapPaint);
		//canvas.drawRoundRect(rectF, r-1, r-1, mBitmapPaint);
		
		 //将原图设置为NULL
		 bitmap=null;
		 mBitmapPaint=null;
		 
		return output;
	}
	
	@Override
	public void draw(Canvas canvas) {
		//mPaint.setShadowLayer(5f, 5.0f, 5.0f, 0xffffffff);
/*		Rect rectR=new Rect(0, 0, textBitmap.getWidth(), textBitmap.getHeight());
		if(textBitmap!=null)
			canvas.drawBitmap(textBitmap, rectR,rectR,mPaint);
		if(bkBitmap1!=null&&bkBitmap2!=null){
			
			Rect rect=new Rect(0, 0, textBitmap.getWidth(),textBitmap.getHeight());
			Rect bkRect=new Rect(0, 0, bkBitmap1.getWidth(), bkBitmap1.getHeight());
			
			if(isPressed)
				canvas.drawBitmap(bkBitmap2, bkRect,rect, mPaint);
			else
			    canvas.drawBitmap(bkBitmap1, bkRect,rect, mPaint);
		}*/
	}
	
	
	@Override
    public int getIntrinsicWidth() {
        return (int) this.radius;
    }

    /**
     * Return the intrinsic height of the underlying drawable object. Returns
     * -1 if it has no intrinsic height, such as with a solid color.
     */
    public int getIntrinsicHeight() {
        return (int) this.radius;
    }
	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

}
