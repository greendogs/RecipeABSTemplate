package com.recipes.app;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class HomeActivity extends SherlockFragmentActivity
	implements RecipesListFragment.OnListSelectedListener {
	
	// set argument
	final static String ARG_ID = "id";
	
	// create an instance of DBHelper class
	DBHelper dbhelper;
	
	// declare variable to store screen resolution
	DisplayMetrics metrics;
	int widthPixels, heightPixels;
	float widthDpi, heightDpi, widthInches, heightInches;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
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
				
		
		// if app run in single pane layout
		if (findViewById(R.id.fragment_container) != null) {
            
            // however, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // create an instance of RecipesListFragment
            RecipesListFragment recipesFrag = new RecipesListFragment();

            // in case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            recipesFrag.setArguments(getIntent().getExtras());

            // add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, recipesFrag).commit();
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
		
		// create an instance of DetailFragment
		DetailFragment detailFrag = (DetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

		// handling for tablet 
		if(detailFrag != null){
			
			// if detailFrag is available, we're in two-pane layout...
            // call a method in the DetailFragment to update its content
			detailFrag.updateDetail(id);
		}else {
    	   
            // if the frag is not available, we're in the one-pane layout and must swap frags...
            // create fragment and give it an argument for the selected article
        	DetailFragment newFragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_ID, id);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            
            // replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_UNSET);

            // commit the transaction
            transaction.commit();
        }
	}
	
	// create option menu
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu, menu);
		
		// get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    // assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    // do not iconify the widget; expand it by default
	    searchView.setIconifiedByDefault(false); 

		return true;
	}
		
	// listener for option menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case R.id.menuShare:
	    		// share google play link of this app to other app such as email, facebook, etc
		        Intent iShare = new Intent(Intent.ACTION_SEND);
				iShare.setType("text/plain");
				iShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
				iShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.message)+" "+getString(R.string.gplay_web_url));
				startActivity(iShare.createChooser(iShare, getString(R.string.share_via)));
				return true;
		    case R.id.menuRate:
		    	// open google play app to ask user to rate & review this app
		        Intent iRate = new Intent(Intent.ACTION_VIEW);
				iRate.setData(Uri.parse(getString(R.string.gplay_url)));
				startActivity(iRate);
				return true;
		    case R.id.menuAbout:
		        // open About app page
		        Intent iAbout = new Intent(this, AboutActivity.class);
				startActivity(iAbout);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
}
