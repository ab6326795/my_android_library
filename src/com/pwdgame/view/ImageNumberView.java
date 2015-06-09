package com.pwdgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.pwdgame.library.R;

public class ImageNumberView  extends View{

	private Bitmap numberBitmap;
	private int height;
	private int tileWidth;
	private int number;
	
	public ImageNumberView(Context context) {
		this(context,null,0);		
	}
	

	public ImageNumberView(Context context, AttributeSet attrs) {
		this(context, attrs,0);	
	}
	
	public ImageNumberView(Context context, AttributeSet attrs, int defStyle) {		
		super(context, attrs, defStyle);	
		if(attrs!=null){
			parseAttributes(context.obtainStyledAttributes(attrs, 
					R.styleable.ImageNumberView));
		}
	}


	private void parseAttributes(TypedArray a){
		Drawable drawable=a.getDrawable(R.styleable.ImageNumberView_numberBitmap);
		setNumberBitmap(drawable);
		number=a.getInt(R.styleable.ImageNumberView_number, 0);
		
		a.recycle();
	}
	
	/**
	 * 设置显示Number的位图
	 * @param res
	 */
	public void setNumberBitmap(Drawable drawable){
		if(drawable!=null){
			if(numberBitmap!=null){
				numberBitmap.recycle();
			}
			numberBitmap=((BitmapDrawable)drawable).getBitmap();
			if(numberBitmap!=null){
				tileWidth=numberBitmap.getWidth()/10;
				height=numberBitmap.getHeight();
			}
		}
	}
	
	/**
	 * 设置要绘制的数字
	 * @param number
	 */
	public void setNumber(int number){
		this.number=number;
		invalidate();
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
	    	 int len=String.valueOf(number).length();
	    	 result=len*tileWidth+getPaddingLeft()+getPaddingRight();  
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
	    	result=height+getPaddingTop()+getPaddingBottom();    
	    	 if(specMode==MeasureSpec.AT_MOST){
	    		 //AT_MOST(至多)，子元素至多达到指定大小的值。
	    		 result = Math.min(result, specSize);
	    	 }
	    	 
	    }
	  
	    return result;  
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		
		String NumberStr=String.valueOf(number);
		int len=NumberStr.length();
	
		Rect srcTemp=new Rect();
		Rect desTemp=new Rect();

		int x,y;
		x=y=0;
		
		for(int i=0;i<len;i++){
			int temp=Integer.parseInt(NumberStr.substring(i,i+1));
			srcTemp.set(tileWidth*temp,0,tileWidth*(temp+1),height);
			desTemp.set(x+tileWidth*i, y,x+tileWidth*(i+1),y+height);
			canvas.drawBitmap(numberBitmap, srcTemp, desTemp,null);
			
		}
	
		
	}
	
	
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		/*if(numberBitmap!=null&&!numberBitmap.isRecycled()){
			numberBitmap.recycle();
			numberBitmap=null;
		}*/
	}
}
