package com.recipes.app;

import java.util.ArrayList;

import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class DetailFragment extends SherlockFragment{
    final static String ARG_ID = "id";
    String mCurrentId = "";
	
	// create an instance of DBHelper class
	DBHelper dbhelper;
	
	// create instance of views
	ImageView imgPreview;
	TextView txtRecipeName, txtTime, txtSummary, txtIngredients, txtDirections, txtAlert;
	ScrollView sclDetail;
	
	// create variable to store data
	String recipeName, preview, prepareTime, cookTime, serves, summary, ingredients, directions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		// if activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // this is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentId = savedInstanceState.getString(ARG_ID);
        }
        
		View v = inflater.inflate(R.layout.fragment_detail, container, false);
		
		imgPreview = (ImageView) v.findViewById(R.id.imgPreview);
		txtRecipeName = (TextView) v.findViewById(R.id.txtRecipeName);
		txtTime = (TextView) v.findViewById(R.id.txtTime);
		txtSummary = (TextView) v.findViewById(R.id.txtSummary);
		txtIngredients = (TextView) v.findViewById(R.id.txtIngredients);
		txtDirections = (TextView) v.findViewById(R.id.txtDirections);
		sclDetail = (ScrollView) v.findViewById(R.id.sclDetail);
		txtAlert = (TextView) v.findViewById(R.id.txtAlert);
		
		dbhelper = new DBHelper(getActivity());
		
		// open database
		try{
			dbhelper.openDataBase();
		}catch (SQLException sqle) {
			throw sqle;
		}
		
        
		return v;
	}
	
	@Override
    public void onStart() {
        super.onStart();

        // during startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // set article based on argument passed in
        	updateDetail(args.getString(ARG_ID));
        } else if (!mCurrentId.equals("")) {
            // set article based on saved instance state defined during onCreateView
        	updateDetail(mCurrentId);
        }
    }
	
	void updateDetail(String id){
		mCurrentId = id;
		new getRecipeDetail().execute();
	}
	
	// asynctask class that is used to fetch data from database in background
	public class getRecipeDetail extends AsyncTask<Void, Void, Void>{
			
	   	
	   	@Override
		protected void onPreExecute() { }
	    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getDetailFromDatabase();
			return null;
		}
			
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			if(!recipeName.equals("")){
				// display data
				sclDetail.setVisibility(View.VISIBLE);
				txtAlert.setVisibility(View.GONE);
				txtRecipeName.setText(recipeName);
				int imagePreview = getResources().getIdentifier(preview, "drawable", getActivity().getPackageName());
				imgPreview.setImageResource(imagePreview);
				txtTime.setText(getString(R.string.prep_time)+" "+prepareTime+"  "+
						getString(R.string.cook_time)+" "+cookTime+"  "+
						getString(R.string.serves)+" "+serves);
				txtSummary.setText(Html.fromHtml(summary));
				txtIngredients.setText(Html.fromHtml(ingredients));
				txtDirections.setText(Html.fromHtml(directions));
			}
		}
	}
	
	// method to get data from database
	public void getDetailFromDatabase(){
    	ArrayList<Object> row = dbhelper.getDetail(mCurrentId);
    	
    	// store data to variables
    	recipeName = row.get(0).toString();
    	preview = row.get(1).toString();
    	prepareTime = row.get(2).toString();
    	cookTime = row.get(3).toString();
    	serves = row.get(4).toString();
    	summary = row.get(5).toString();
    	ingredients = row.get(6).toString();
    	directions = row.get(7).toString();
    	
    }
	

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the current article selection in case we need to recreate the fragment
        outState.putString(ARG_ID, mCurrentId);
    }
    
}
