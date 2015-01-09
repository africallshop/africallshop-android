package org.linphone;

import static android.content.Intent.ACTION_MAIN;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private ServiceWaitThread mThread;
	private Handler mHandler;
	public static String TAG = "SplashActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
         
//// METHOD 1
//         /****** Create Thread that will sleep for 5 seconds *************/        
//        Thread background = new Thread() {
//            public void run() {
//                try {
//                    // Thread will sleep for 5 seconds
//                    sleep(5*1000);
//                     
//                    // After 5 seconds redirect to another intent
//                    Intent i=new Intent(getBaseContext(),FirstScreen.class);
//                    startActivity(i);
//                     
//                    //Remove activity
//                    finish();
//                     
//                } catch (Exception e) {
//                 
//                }
//            }
//        };
//        // start thread
//        background.start();
//METHOD 2
        
		mHandler = new Handler();
		
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
//                Intent i = new Intent(SplashActivity.this, LinphoneLauncherActivity.class);
//                startActivity(i);
//                // close this activity
//                finish();
            	toggleNext();
            }
        }, 3*1000); // wait for 5 seconds
        
    }
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    
    private void toggleNext(){
    	if (LinphoneService.isReady()) {
			onServiceReady();
		} else {
			// start linphone as background
			startService(new Intent(ACTION_MAIN).setClass(this, LinphoneService.class));
			mThread = new ServiceWaitThread();
			mThread.start();
		}
    }
    
    
	protected void onServiceReady() {
		
		final Class<? extends Activity> classToStart;
		if(!LoginActivity.isLogged(this)){
			classToStart = LinphoneLauncherActivity.class;
		}else{
			classToStart = LinphoneActivity.class;
		}
		
		android.util.Log.e(TAG, " Linphone ready RSC");
//		final Class<? extends Activity> classToStart;
//		if (getResources().getBoolean(R.bool.show_tutorials_instead_of_app)) {
//			classToStart = TutorialLauncherActivity.class;
//		} else if (getResources().getBoolean(R.bool.display_sms_remote_provisioning_activity) && LinphonePreferences.instance().isFirstRemoteProvisioning()) {
//			classToStart = RemoteProvisioningActivity.class;
//		} else {
//			classToStart = LinphoneActivity.class;
//		}
		
		LinphoneService.instance().setActivityToLaunchOnIncomingReceived(classToStart);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				if(!result){
					startActivity(new Intent().setClass(SplashActivity.this, classToStart).setData(getIntent().getData()));
					finish();
//					LinphoneLauncherActivity.this.finish();
					
//				}
//				else{
//					android.util.Log.e(TAG, " Start for result");
//					startActivityForResult(new Intent(LinphoneLauncherActivity.this, classToStart), LoginActivity.REQUEST_CODE);
//				}
			}
		}, 1000);
	}


	private class ServiceWaitThread extends Thread {
		public void run() {
			while (!LinphoneService.isReady()) {
				try {
					sleep(30);
				} catch (InterruptedException e) {
					throw new RuntimeException("waiting thread sleep() has been interrupted");
				}
			}

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					onServiceReady();
				}
			});
			mThread = null;
		}
	}
    
}
