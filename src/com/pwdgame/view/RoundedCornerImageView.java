package com.pwdgame.view;

import com.pwdgame.library.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedCornerImageView extends ImageView {

	private final float density = getContext().getResources()
			.getDisplayMetrics().density;
	private float roundness;

	public RoundedCornerImageView(Context context) {
		this(context,null);
	}

	public RoundedCornerImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}

	public RoundedCornerImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		if(attrs!=null){
			parseAttributes(context.obtainStyledAttributes(attrs, 
					R.styleable.XYImageView));
		}
	}


	private void parseAttributes(TypedArray a){	
		roundness=(int) a.getDimension(R.styleable.XYImageView_round,0);		
		a.recycle();
	}

	@Override
	public void draw(Canvas canvas) {
		final Bitmap composedBitmap;
		final Bitmap originalBitmap;
		final Canvas composedCanvas;
		final Canvas originalCanvas;
		final Paint paint;
		final int height;
		final int width;

		width = getWidth();

		height = getHeight();

		composedBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		originalBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		composedCanvas = new Canvas(composedBitmap);
		originalCanvas = new Canvas(originalBitmap);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		super.draw(originalCanvas);

		composedCanvas.drawARGB(0, 0, 0, 0);

		//int min=Math.min(width, height);
		composedCanvas.drawRoundRect(new RectF(0, 0,width,height),
				this.roundness, this.roundness, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		composedCanvas.drawBitmap(originalBitmap, 0, 0, paint);

		canvas.drawBitmap(composedBitmap, 0, 0, new Paint());
	}

	public float getRoundness() {
		return this.roundness / this.density;
	}

	/**
	 * 设置圆角半径
	 * @param roundness
	 */
	public void setRoundness(float roundness) {
		this.roundness = roundness * this.density;
	}

}
