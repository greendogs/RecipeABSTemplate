package com.recipes.app;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
public class RecipesListFragment extends SherlockFragment {
	
	// create interface for RecipesListFragment
	OnListSelectedListener mCallback;

	// create an instance of DBHelper class
	DBHelper dbhelper;
	
	// create instance of list and ListAdapter
	ListView list;
	RecipesListAdapter rla;
	
	// create ArrayList variable to store data from database
	ArrayList<ArrayList<Object>> data;
	
	// create array variables to store data
	static String[] id;
	static String[] recipeName;
	static String[] prepareTime;
	static String[] cookTime;
	static String[] thumbnail;
	
	// declare OnListSelected interface
	public interface OnListSelectedListener{
		public void onListSelected(String id);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		
		list = (ListView) v.findViewById(R.id.list);
		rla = new RecipesListAdapter(getActivity());
		
		dbhelper = new DBHelper(getActivity());
		
		// create database
		try{
			dbhelper.createDataBase();
		}catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		
		// open database
		try{
			dbhelper.openDataBase();
		}catch (SQLException sqle) {
			throw sqle;
		}
		
		// call asynctask class to get data from database
		new getRecipesList().execute();
				
		
		// listener to get selected id when list item clicked
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub

				
				// pass id to onListSelected method on HomeActivity
				mCallback.onListSelected(id[position]);

				// set the item as checked to be highlighted when in two-pane layout
				list.setItemChecked(position, true);
			}
		});
		return v;
	}
	

    @Override
    public void onStart() {
        super.onStart();

        // when in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.detail_fragment) != null) {
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // this makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
	// asynctask class that is used to fetch data from database in background
	public class getRecipesList extends AsyncTask<Void, Void, Void>{
		
		ProgressDialog progress;
    	
    	@Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
    		
    		// show progress dialog when fetching data from database
    		progress= ProgressDialog.show(
    				getActivity(), 
    				"", 
    				getString(R.string.loading_data), 
    				true);
    				
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getDataFromDatabase();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			progress.dismiss();
			list.setAdapter(rla);
			
		}
	}
	
	// method to get data from database
    public void getDataFromDatabase(){
    	
    	// get all data and store them to ArrayList variable
    	data = dbhelper.getAllData();
    	
    	// define length of array variables
    	id = new String[data.size()];
    	recipeName = new String[data.size()];
    	thumbnail = new String[data.size()];
    	prepareTime = new String[data.size()];
    	cookTime = new String[data.size()];
    	
    	// store all data into array variables
    	for(int i=0;i<data.size();i++){
    		ArrayList<Object> row = data.get(i);
    		
    		id[i] = row.get(0).toString();
    		recipeName[i] = row.get(1).toString();
    		thumbnail[i] = row.get(2).toString().trim();
    		prepareTime[i] = row.get(3).toString();
    		cookTime[i] = row.get(4).toString();
    		
    	}
    	
    	
    }
    

    
}
