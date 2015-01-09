/*
LinphoneLauncherActivity.java
Copyright (C) 2011  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.linphone;

import static android.content.Intent.ACTION_MAIN;

import org.linphone.mediastream.Log;
import org.linphone.setup.RemoteProvisioningActivity;
import org.linphone.tutorials.TutorialLauncherActivity;
import org.linphone.utils.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 
 * Launch Linphone main activity when Service is ready.
 * 
 * @author Guillaume Beraudo
 *
 */
public class LinphoneLauncherActivity extends Activity implements OnClickListener{



	private RelativeLayout next;
	public static String TAG = "LinphoneLauncherActivity";
//	boolean result = false;
	
//	private BroadcastReceiver broadcastMessageReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Used to change for the lifetime of the app the name used to tag the logs
		new Log(getResources().getString(R.string.app_name), !getResources().getBoolean(R.bool.disable_every_log));
		
		// Hack to avoid to draw twice LinphoneActivity on tablets
        if (getResources().getBoolean(R.bool.isTablet)) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
		setContentView(R.layout.launcher);
		initUI();
//		Log.i(LinphoneManager.getLc().getDefaultProxyConfig());
	}

	
	private void initUI() {
		next = (RelativeLayout) findViewById(R.id.setup_next);
		next.setOnClickListener(this);
		next.setEnabled(true);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.setup_next) {
			if(LoginActivity.isLogged(this)){
				startActivity(new Intent(LinphoneLauncherActivity.this, LinphoneActivity.class));
				finish();
			}else{
				startActivity(new Intent(LinphoneLauncherActivity.this, LoginActivity.class));
				finish();
			}
		}		
	}
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	android.util.Log.v(TAG, " RC "+requemestCode+ " RSC"+resultCode);
//        
//        if(requestCode == LoginActivity.REQUEST_CODE && resultCode == LoginActivity.REQUEST_RELOAD){
//        	startActivity(new Intent().setClass(LinphoneLauncherActivity.this, LinphoneActivity.class).setData(getIntent().getData()));
//        	Toast.makeText(getBaseContext(), "Bonjour Result", Toast.LENGTH_LONG).show();
//        }
//        if(requestCode == LoginActivity.REQUEST_CODE && resultCode == LoginActivity.REQUEST_QUIT){
//        	finish();
//        }
        
    }
	@Override
	protected void onResume(){
		super.onResume();
//		IntentFilter messageIntentFilter = new IntentFilter(Config.INTENT_LAUNCHER);
//		broadcastMessageReceiver=  new BroadcastReceiver(){
//			@Override
//			public void onReceive(Context context, Intent intent){
//				// dispatch according to 
////				dispatch(intent);
//			}
//		};
//		// register the broadcast receiver listening to any message coming from a background process
//		this.registerReceiver(broadcastMessageReceiver, messageIntentFilter);
		
//		Toast.makeText(getBaseContext(), "Resume", Toast.LENGTH_LONG).show();
		
//		if(LoginActivity.isLogged(this)){
//			
//	        new Handler().postDelayed(new Runnable() {
//	            @Override
//	            public void run() {
//	            	startActivity(new Intent().setClass(LinphoneLauncherActivity.this, LinphoneActivity.class).setData(getIntent().getData()));
//	            }
//	        },2*1000);
//		}
	}
	
//	private void dispatch(Intent intent){
//		int action = intent.getIntExtra("action", 0);
//		Toast.makeText(getBaseContext(), "Bonjour broadcast", Toast.LENGTH_LONG).show();
//		switch (action) {
//		case LoginActivity.REQUEST_RELOAD:
//			startActivity(new Intent().setClass(LinphoneLauncherActivity.this, LinphoneActivity.class).setData(getIntent().getData()));
//			Toast.makeText(this, "Yes", Toast.LENGTH_LONG).show();
//			break;
//		case LoginActivity.REQUEST_QUIT:
//			finish();
//			break;			
//		}
//	}
   
	@Override
	protected void onPause(){
		super.onPause();
//		this.unregisterReceiver(this.broadcastMessageReceiver);
	}
	
	@SuppressLint("NewApi")
	private void reload(){
		if (Build.VERSION.SDK_INT >= 11) {
			recreate();
		} /*else {
			Intent intent = getIntent();
			overridePendingTransition(0, 0);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			finish();

			overridePendingTransition(0, 0);
			startActivity(intent);
		}*/
	}
}


