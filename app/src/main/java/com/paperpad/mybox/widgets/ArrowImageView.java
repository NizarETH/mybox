/**
 * 
 */
package com.paperpad.mybox.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author euphordev02
 *
 */
public class ArrowImageView extends ImageView {

	private Paint paint;

	/**
	 * @param context
	 */
	public ArrowImageView(Context context) {
		super(context);
		paint = new Paint();
		paint.setColor(Color.BLACK);
	}
	
	/**
	 * @param context
	 */
	public ArrowImageView(Context context, Paint paint) {
		super(context);
		this.paint = paint;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ArrowImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ArrowImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Rect rect = canvas.getClipBounds();
		Log.i(" bounds of arrow", rect.left+" bottom "+rect.bottom+" top "+rect.top + " right "+rect.right);
		
		Path path = new Path();
		Point p1 = new Point();
		Point p2 = new Point();
		Point p3 = new Point();
		Point p4 = new Point();
		Point p5 = new Point();
		Point p6 = new Point();
		int a = rect.bottom/8;
		int d = rect.bottom/2;
		int c = d/2; // la valeur de translation pour centrer le triangle
		p1.x = c+a;
		p1.y = 0;
		p2.x = c+a+d;
		p2.y = d;
		p3.x = c+a;
		p3.y = 2*d;
		p4.x = c+0;
		p4.y = 2*d-a;
		p5.x = c+d-a;
		p5.y = d;
		p6.x = c+0;
		p6.y = a;
		
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p4.x, p4.y);
		path.lineTo(p5.x, p5.y);
		path.lineTo(p6.x, p6.y);
		path.close();
		if (paint == null) {
			paint = new Paint();
			paint.setColor(Color.WHITE);
		} 
		canvas.drawPath(path, paint );
		super.onDraw(canvas);
	}

	/**
	 * @return the paint
	 */
	public Paint getPaint() {
		return paint;
	}

	/**
	 * @param paint the paint to set
	 */
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

}
