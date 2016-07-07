package com.paperpad.mybox;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;

public class MyProvider extends ContentProvider {

	String pathFile;
	private Context context;
	public MyProvider(String pathFile, Context context) {
		this.pathFile = pathFile;
		
		this.context = context;
	}
	
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		
	    //File cacheDir = context.getCacheDir();
	     File privateFile = new File(pathFile);
	    // Log.w("MyProvider", pathFile + "  \n cacheDir :"+ cacheDir.getPath()+ " \n private : "+ privateFile.getPath());
		return ParcelFileDescriptor.open(privateFile, ParcelFileDescriptor.MODE_READ_ONLY);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	public MyProvider() {
		super();
		// TODO Auto-generated constructor stub
	}

}
