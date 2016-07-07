/**
 * 
 */
package com.paperpad.mybox.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Illustration;
import com.paperpad.mybox.models.MyBox;
import com.paperpad.mybox.util.ColorTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class MyBoxListCategoriesAdapter extends BaseAdapter {

	private List<MyBox> boxs;
	private Context context;
	private MyBox box;
	
	private Typeface font, font_discript;
	private Colors colors;
	private static String POLICE, POLICE_DISCRIPT;


	/**
	 * @param boxs
     * @param context
	 * @param colors 
	 * 
	 */
	public MyBoxListCategoriesAdapter(List<MyBox> boxs, Context context, Colors colors) {
		this.boxs = boxs;
		this.context = context;
		this.colors = colors;
		POLICE = "fonts/OpenSans-Semibold.ttf"; //gill-sans-light.ttf";
		POLICE_DISCRIPT = "fonts/OpenSans-Regular.ttf"; //"fonts/gill-sans-light.ttf"; //mt-italic.ttf";


		font = Typeface.createFromAsset(context.getAssets(), POLICE); 
		font_discript = Typeface.createFromAsset(context.getAssets(), POLICE_DISCRIPT);

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return boxs.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public MyBox getItem(int position) {
		// TODO Auto-generated method stub
		return boxs.get(position);
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
		box = getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = inflater.inflate(R.layout.mybox_list_item, parent, false);
//			LinearLayout albumItem = (LinearLayout)convertView.findViewById(R.id.album_item);
//			albumItem.setBackgroundColor(Color.WHITE);
//			LinearLayout innerAlbumItem = (LinearLayout)convertView.findViewById(R.id.inner_album_item);
////			innerAlbumItem.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getText_color(), "44")));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getText_color(), "44")));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			convertView.setBackgroundDrawable(drawable);
//			innerAlbumItem.setBackgroundDrawable(drawable);
			holder.title = (TextView)convertView.findViewById(R.id.titleBox);
			holder.boxDesc = (TextView)convertView.findViewById(R.id.descBox);
			holder.imgBox = (ImageView)convertView.findViewById(R.id.imgBox);
			holder.priceBox = (TextView)convertView.findViewById(R.id.priceBox);
			holder.imgArrow = (ImageView)convertView.findViewById(R.id.imgArrow);
			holder.position = position;


			holder.title.setTypeface(font, Typeface.BOLD);
			holder.title.setTextColor(colors.getColor(colors.getTitle_color()));

			holder.boxDesc.setTypeface(font_discript);
			holder.boxDesc.setTextColor(colors.getColor(colors.getText_color()));


			holder.priceBox.setTypeface(font_discript, Typeface.BOLD);
			holder.priceBox.setTextColor(colors.getColor(colors.getText_color()));

			Glide.with(context).load(R.drawable.white_next).transform(new ColorTransformation(context, colors.getColor(colors.getAlternate_background_color()))).into(holder.imgArrow);

			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		

		holder.title.setText(box.getTitre_coffret());

		//holder.title.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		holder.boxDesc.setText(Utils.removeHtmlTags(box.getIntroduction())/*Html.fromHtml(box.getIntroduction()).toString()*/);

		if (box.getIllustrations().size()>0) {
				
				holder.imgBox.setScaleType(ScaleType.CENTER_CROP);
				Illustration illustration = box.getIllustrations().iterator().next();
				String path;
				if (!illustration.getPath().isEmpty()) {
					path = illustration.getPath();
					Glide.with(context).load(new File(path)).into(holder.imgBox);
				}else {
					path = illustration.getLink();
					Glide.with(context).load(path).into(holder.imgBox);
				}
		}


		holder.priceBox.setText(box.getPrix()+ " "+BoxsMainActivity.currency);
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView boxDesc; 
		ImageView imgBox; // T : TOP , B : BOTTOM , L : LEFT , R : RIGHT
		int position;
		TextView priceBox;
		ImageView imgArrow;
		}

	/**
	 * @return the boxs
	 */
	public List<MyBox> getBoxs() {
		return boxs;
	}

	/**
	 * @param boxs the boxs to set
	 */
	public void setBoxs(List<MyBox> boxs) {
		this.boxs = boxs;
	}

}
