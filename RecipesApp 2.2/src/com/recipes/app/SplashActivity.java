/**
 * Author		: Taufan E.
 * Website		: pongodev.com
 * App Name		: Recipes App
 * Release Date	: April 2012
 * Version		: 2.2
 */

package com.recipes.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {
	
	// create an instance of ProgressBar
	ProgressBar prgLoading;
	
	int progress = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
    	prgLoading.setProgress(progress);

    	// run progressbar via asynctask
		new Loading().execute();
    }
    
    
    public class Loading extends AsyncTask<Void, Void, Void>{
    	@Override
		 protected void onPreExecute() {}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			// create progress bar loading
			while(progress < 100){
				try {
					Thread.sleep(1000);
					progress += 30;
					prgLoading.setProgress(progress);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			// when progressbar finish call HomeActivity class
			Intent i = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(i);
		}
    }
}