/**
 * 
 */
package com.paperpad.mybox.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.MyBox;
import com.paperpad.mybox.models.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class ListOrderHistoryAdapter extends BaseAdapter {

	private List<Product> products;
	private Context context;
	private Product product;
	
	private Typeface font, font_discript;
	private static String POLICE, POLICE_DISCRIPT;
	
	private DatabaseController appController;
	private Colors colors;
	private int count;


	/**
	 * @param products
	 * @param context
	 * 
	 */
	public ListOrderHistoryAdapter(List<Product> products, Context context) {
		this.products = products;
		this.context = context;
		
		POLICE = "fonts/gill-sans-light.ttf";
		POLICE_DISCRIPT = "fonts/gill-sans-light.ttf"; //mt-italic.ttf";

		font = Typeface.createFromAsset(context.getAssets(), POLICE); 
		font_discript = Typeface.createFromAsset(context.getAssets(), POLICE_DISCRIPT);

		appController = new DatabaseController(context);

		try {
			colors = appController.getColorsDao().queryForAll().get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		count = products.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

//	public void setReelCount() {
//		
//		this.count = count - 1;
//		
//	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Product getItem(int position) {
		// TODO Auto-generated method stub
		return products.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = inflater.inflate(R.layout.box_item, parent, false);
			convertView.setMinimumHeight(100);
//			LinearLayout albumItem = (LinearLayout)convertView.findViewById(R.id.album_item);
//			albumItem.setBackgroundColor(Color.WHITE);
//			LinearLayout innerAlbumItem = (LinearLayout)convertView.findViewById(R.id.inner_album_item);
////			innerAlbumItem.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getText_color(), "44")));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getText_color(), "44")));

			drawable.addState(new int[]{}, new ColorDrawable(Color.WHITE));
			convertView.setBackground(drawable);
//			innerAlbumItem.setBackgroundDrawable(drawable);
			holder.title = (TextView)convertView.findViewById(R.id.titleBox);
			holder.boxDesc = (TextView)convertView.findViewById(R.id.benifits_of);
			holder.priceBox = (TextView)convertView.findViewById(R.id.boxPriceItem);
			holder.position = position;
			convertView.setTag(holder);
			
		}
		
		holder = (ViewHolder) convertView.getTag();
		product = getItem(position);  
		
		if(product == null)return convertView;
		
		MyBox box = null;
		try {
 			List<MyBox> boxs = appController.getMyBoxDao().queryForEq("id_coffret", product.getId_product());
			if(boxs != null && boxs.size() > 0) {
				
				box = boxs.get(0);
				
				holder.title.setTextColor(colors.getColor(colors.getTitle_color()));
				holder.title.setText(box.getTitre_coffret());
				holder.priceBox.setTextColor(colors.getColor(colors.getText_color()));
				holder.priceBox.setText(box.getPrix()+ " "+BoxsMainActivity.currency);
				
			}else {
				holder.title.setVisibility(View.GONE);
				holder.priceBox.setVisibility(View.GONE);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//holder.title.setTypeface(font);
		holder.boxDesc.setText(context.getString(R.string.benifits_of)+" "+product.getPrenom_beneficiaire()+" "+product.getNom_beneficiaire()+" "+context.getString(R.string.valid_until)+" "+product.getDate_validite());
		holder.boxDesc.setTextColor(colors.getColor(colors.getTitle_color()));
		//holder.boxDesc.setTypeface(font_discript);
		
		
		//holder.priceBox.setTypeface(font_discript);
		
		
		return convertView;
	}
	
	public float getTotalPrice() {
		float totalPrice = 0.0f;
		
		if(product == null)return 0;
		
	    for(int i = 0 ; i < products.size(); i++) {
		MyBox box = null;
		try {
			List<MyBox> boxs = appController.getMyBoxDao().queryForEq("id_coffret", product.getId_product());
			if(boxs != null && boxs.size() > 0) {
				box = boxs.get(0);
				totalPrice += box.getPrix();
			}
			else
				totalPrice += 0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }

		return totalPrice;
	}
	
	static class ViewHolder {
		TextView title;
		TextView boxDesc; 
		int position;
		TextView priceBox;
		}

	/**
	 * @return the boxs
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * @param boxs the boxs to set
	 */
	public void setBoxs(List<Product> products) {
		this.products = products;
	}

}
