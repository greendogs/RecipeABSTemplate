package com.recipes.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class RecipesListAdapter extends BaseAdapter {

		private Context mContext;
		
		public RecipesListAdapter(Context context) {
			mContext = context;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return RecipesListFragment.recipeName.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row, null);
				holder = new ViewHolder();
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// connect views object and views id on xml
			holder.imgThumbnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
			holder.txtRecipeName = (TextView) convertView.findViewById(R.id.txtRecipeName);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			
			// set data to textview and imageview
			holder.txtRecipeName.setText(RecipesListFragment.recipeName[position]);
			holder.txtTime.setText(
					mContext.getString(R.string.prep_time)+" "+RecipesListFragment.prepareTime[position]+"  "+
					mContext.getString(R.string.cook_time)+" "+RecipesListFragment.cookTime[position]);
			int image = mContext.getResources().getIdentifier(RecipesListFragment.thumbnail[position], "drawable", mContext.getPackageName());
			holder.imgThumbnail.setImageResource(image);
			
			return convertView;
		}

		// method to create instance of views
		static class ViewHolder {
			ImageView imgThumbnail;
			TextView txtRecipeName, txtTime;
		}
		
		
	}