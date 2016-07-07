package com.paperpad.mybox.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class AViewFlipper extends ViewFlipper {

	int color;
	
	public AViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.color = Color.WHITE;
//		View v = new View(context);
//		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
//		v.setBackgroundColor(0xffffffff);
//		addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, 100));
	}
	
	public void setFlipperColor(int color) {
		this.color = color;
	}

	Paint paint = new Paint();

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		float margin = 3;
		float radius = 5;
		float cx = width / 2 - (2 * (radius + margin)  * getChildCount() / 2);
		float cy = height - 20;

		canvas.save();
//		Rect dst = new Rect((int)radius, (int)radius, (int)radius, (int)radius);


		if(getChildCount() <= 1) return;
		for (int i = 0; i < getChildCount(); i++)
		{
			if (i == getDisplayedChild())
			{
				paint.setColor(this.color);
//				paint.setDither(true);
				paint.setAntiAlias(true);
//				Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_stat_cercle_white);
//				canvas.drawBitmap(bitmap , null, dst , paint);
				canvas.drawCircle(cx, cy, radius, paint);  /** For circle indicators **/
				//RectF rect = new RectF(cx, cy, radius * 4, radius * 4);
				//canvas.drawRect(rect, paint);

			} else
			{
				paint.setColor(Color.LTGRAY);
//				Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_stat_cercle_white);
//				canvas.drawBitmap(bitmap , null, dst , paint);
				canvas.drawCircle(cx, cy, radius, paint);
			}
			cx += 4 * (radius + margin);
		}

		canvas.restore();
	}

}
