/**
 * 
 */
package com.paperpad.mybox.helpers;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author euphordev02
 *
 */
public class ImageUtils {
	
	public static Point getSizeOfImage(InputStream inputStream){
		

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
		int imageWidth = bitmapOptions.outWidth;
		int imageHeight = bitmapOptions.outHeight;
//		try {
//			inputStream.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return new Point(imageWidth, imageHeight);
		
	}
	
	/**
	 * Copy all data from InputStream and write using OutputStream
	 * 
	 * @param is
	 *            InputStream
	 * @param os
	 *            OutputStream
	 */
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			
			Log.e(" Copy Stream Exception "," HERE <<>>>");
		}
	}
	
	/** a function that calculate the width required for a fixed height by conserving ratio.
	 * @param w : image width
	 * @param h : image height
	 * @param fixedHeight : the fixed Height
	 * @return the width required for a fixed requiredHeight
	 */
	public static int getHeight(int w, int h, int fixedHeight){
		
		return (int)(((float)(fixedHeight*w))/((float)h));
	}
	
}
