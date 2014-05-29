package com.recipes.app;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.recipes.suggestion.SuggestionProvider;

public class SearchableActivity extends SherlockFragmentActivity
	implements SearchListFragment.OnListSelectedListener{

	// set argument
	final static String ARG_ID = "id";
	
	static String keyword = "";

	// declare variable to store screen resolution
	DisplayMetrics metrics;
	int widthPixels, heightPixels;
	float widthDpi, heightDpi, widthInches, heightInches;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      keyword = intent.getStringExtra(SearchManager.QUERY);
	      SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
	                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
	        suggestions.saveRecentQuery(keyword, null);
	    }
	    
		setContentView(R.layout.activity_searchable);
		
		
		// get device screen resolution
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		// get screen width and height
		widthPixels = metrics.widthPixels;
		heightPixels = metrics.heightPixels;

		// convert screen width and height pixels to dpi 
		widthDpi = metrics.xdpi;
		heightDpi = metrics.ydpi;

		// get screen width and height in inch
		widthInches = widthPixels / widthDpi;
		heightInches = heightPixels / heightDpi;
		

		// change actionbar title
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");    
		if ( 0 == titleId ) 
		        titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
			
		// and change the title color to white
		TextView txtActionbarTitle = (TextView)findViewById(titleId);
		txtActionbarTitle.setTextColor(getResources().getColor(R.color.actionbar_title_color));
		
		

		// if app in run single pane layout
		if (findViewById(R.id.fragment_container2) != null) {
            
            // however, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // create an instance of ExampleFragment
            SearchListFragment searchListFrag = new SearchListFragment();

            // in case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            searchListFrag.setArguments(getIntent().getExtras());

            // add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container2, searchListFrag).commit();
        }
		
	}

	// when item on list selected call this method
	@Override
	public void onListSelected(String id) {
		// TODO Auto-generated method stub

		// get screen resolution in inch
		long diagonalInches = Math.round(Math.sqrt(
			    (widthInches * widthInches) 
			    + (heightInches * heightInches)));
		Log.d("SCREEN SIZE", diagonalInches+"");

		// create an instance of SearchDetailFragment
		SearchDetailFragment searchDetailFrag = (SearchDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.search_detail_fragment);

		// handling for tablet 
		if(searchDetailFrag != null){
			
			// if detailFrag is available, we're in two-pane layout...
            // call a method in the SearchDetailFragment to update its content
			searchDetailFrag.updateDetail(id);
		}else {
    	   
    	   // if the frag is not available, we're in the one-pane layout and must swap frags...
           // create fragment and give it an argument for the selected article
       		SearchDetailFragment newSearchDetailFrag = new SearchDetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_ID, id);
            newSearchDetailFrag.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            
            // replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container2, newSearchDetailFrag);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_UNSET);

            // commit the transaction
            transaction.commit();
        }
	}
	
}
