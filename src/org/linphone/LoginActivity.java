package org.linphone;

import org.linphone.LinphonePreferences.AccountBuilder;
import org.linphone.LinphoneSimpleListener.LinphoneOnRegistrationStateChangedListener;
import org.linphone.asycntasks.LoginTask;
import org.linphone.core.LinphoneAddress.TransportType;
import org.linphone.core.LinphoneCore.MediaEncryption;
import org.linphone.core.LinphoneCore.RegistrationState;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.utils.Config;
import org.linphone.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
	public static String TAG = "LoginActivity";
	

	private Handler mHandler = new Handler();
	public static final int REQUEST_CODE = 100;
	public static final int REQUEST_RELOAD = 200;
	public static final int REQUEST_QUIT = 300;
	private int REQUEST_RESULT = 0;
	
	
	private RelativeLayout back, next;
	Button connection, create_account;
	EditText email, password;
	TextView forgottenpassword;
	
	private LinphonePreferences mPrefs;
	
    /**
     * Broadcast receiver process running in background  
     */
    private BroadcastReceiver broadcastMessageReceiver;
//    private Intent broadcastIntent;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hack to avoid to draw twice LinphoneActivity on tablets
        if (getResources().getBoolean(R.bool.isTablet)) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.login);
        mPrefs = LinphonePreferences.instance();
		initUi();
		

	}
	
	private void checkforValidation(){
		if(Utils.getStringFromPreference(this, "status") != null && Utils.getStringFromPreference(this, "status").equals("disabled")){
			if(Utils.getStringFromPreference(this, Config.JSON_ID) != null && Integer.parseInt(Utils.getStringFromPreference(this, Config.JSON_ID))>0){
				startActivity(new Intent(LoginActivity.this, PhonenumbervalidationActivity.class));
			}
			if(Utils.getStringFromPreference(this, Config.JSON_ID) != null && Integer.parseInt(Utils.getStringFromPreference(this, Config.JSON_ID))==0){
				startActivity(new Intent(LoginActivity.this, CodevalidationActivity.class));
			}
		}
	}
	private void initUi(){
		back = (RelativeLayout) findViewById(R.id.setup_back);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		
		next = (RelativeLayout) findViewById(R.id.setup_next);
		next.setVisibility(View.GONE);
		
		create_account = (Button) findViewById(R.id.create_account_button);
		create_account.setOnClickListener(this);
		connection = (Button) findViewById(R.id.connexion_button);
		connection.setOnClickListener(this);
		
		forgottenpassword = (TextView)findViewById(R.id.forget_password);
		forgottenpassword.setOnClickListener(this);
		
		email = (EditText) findViewById(R.id.user_email);
		password = (EditText) findViewById(R.id.user_password);
		
	}
	
	@Override
	public void onClick(View v) {		
		String urlink = "https://www.africallshop.com/"+this.getString(R.string.applang)+"/connexion/android.php?motDePasse=1#motDePasse";
		
//		Toast.makeText(this, urlink, Toast.LENGTH_LONG).show();
		int id = v.getId();
		
		if (id == R.id.setup_back) {
			startActivity(new Intent(LoginActivity.this, LinphoneLauncherActivity.class));
			finish();
		}
		if (id == R.id.create_account_button) {
			startActivity(new Intent(LoginActivity.this, SubscribeActivity.class));
			finish();
		}
		if (id == R.id.connexion_button) {
			new LoginTask(this).execute(email.getText().toString().trim(),password.getText().toString().trim());
		}
		if (id == R.id.forget_password) {
			startActivity(new Intent().setClass(LoginActivity.this, WebActivity.class).putExtra(WebActivity.WEBLINK, urlink));
		}		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		IntentFilter messageIntentFilter = new IntentFilter(Config.INTENT_ACTION);
		broadcastMessageReceiver=  new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				// dispatch according to 
				dispatch(intent);
			}
		};
		// register the broadcast receiver listening to any message coming from a background process
		this.registerReceiver(broadcastMessageReceiver, messageIntentFilter);
//		checkforValidation();
	}
	
	private void dispatch(Intent intent){
		int action = intent.getIntExtra("action", 0);
		
		switch (action) {
			case Config.ACTION_LOGGED_IN:
					if(accountCreated()){
						launchHome();
					}else{
						Toast.makeText(this, getString(R.string.task_status_ko), Toast.LENGTH_LONG).show();
					}
				break; 
			 			
			case Config.ACTION_PHONE_VALIDATION:
				new AlertDialog.Builder(this)
		        .setMessage(getString(R.string.error_phonenumber_need_validation))
		        .setNeutralButton(R.string.verify_now, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();
						startActivity(new Intent(LoginActivity.this, PhonenumbervalidationActivity.class));
						finish();
					}
				})
		        .show();
//				
				break;
				
//			case Config.ACTION_CODE_VALIDATION:
//				startActivity(new Intent(LoginActivity.this, CodevalidationActivity.class));
//				break; 
				
			case Config.ACTION_SECURITY_POPUP:
				new AlertDialog.Builder(this)
		        .setMessage(getString(R.string.account_disabled))
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();
					}
				}) 
		        .show();
				
//				Toast.makeText(this, getString(R.string.account_disabled), Toast.LENGTH_LONG).show();
//				startActivity(new Intent(LoginActivity.this, CodevalidationActivity.class));
				break;
				
//			case Config.ACTION_QUIT_PROGRESS_BAR:
//				Toast.makeText(this, "quit progress bar", Toast.LENGTH_LONG).show();
//			break;
			
			default:
				break;
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		this.unregisterReceiver(this.broadcastMessageReceiver);
	}
	
	private boolean accountCreated(){
		int created = LinphonePreferences.instance().getAccountCount();
		if (created == 0){
			return createaccount();
		}
		return true;
	}

	private void checkcurrentaccount(){
		int n = LinphonePreferences.instance().getAccountCount();
		if(n==1){
			LinphonePreferences.instance().deleteAccount(n);
		}
	}
	
	private void launchHome(){
		Log.v(TAG, "Go to homes");
//		Intent data = new Intent();
//		if(getParent() == null){
//			setResult(REQUEST_RELOAD, data);
//		}else{
//			getParent().setResult(REQUEST_RELOAD, data);
//		}
		startActivity(new Intent(LoginActivity.this, LinphoneActivity.class));
		finish();
//		sendBroadcast(REQUEST_RELOAD);
	}
	
	private  void leaveApp(){
		Log.v(TAG, "Leave the APP");
		Intent data = new Intent();
		if(getParent() == null){
			setResult(REQUEST_QUIT, data);
		}else{
			getParent().setResult(REQUEST_QUIT, data);
		}
		finish();
//		sendBroadcast(REQUEST_QUIT);
	}
	
	private boolean createaccount(){
		boolean result = false;
//		LinphoneManager.removeListener(registrationListener);
//		LinphoneManager.addListener(registrationListener);
		AccountBuilder builder = new AccountBuilder(LinphoneManager.getLc());
		builder
			.setUsername(Utils.getStringFromPreference(this, Config.JSON_USERNAME))
			.setPassword(Utils.getStringFromPreference(this, Config.JSON_PASSWORD))
			.setDisplayName(Utils.getStringFromPreference(this, Config.JSON_DISPLAYNAME))
			.setProxy(Utils.getStringFromPreference(this, Config.JSON_PROXY))
			.setDomain(Utils.getStringFromPreference(this, Config.JSON_DOMAINE))
			.setTransport(TransportType.LinphoneTransportTls)
			.setOutboundProxyEnabled(true)
			.setAvpfEnabled(true)
			.setAvpfRRInterval(3);
			
			mPrefs.setStunServer(getString(R.string.default_stun));
			mPrefs.setIceEnabled(true);
			mPrefs.setMediaEncryption(MediaEncryption.SRTP);
			mPrefs.setPushNotificationEnabled(false);
			try {
				builder.saveNewAccount();
				result = true;
				Utils.putStringToPreference(this,Config.USER, email.getText().toString());
				Utils.putStringToPreference(this,Config.PWD, password.getText().toString());
			} catch (LinphoneCoreException e) {
			}
			
			mPrefs.firstLaunchSuccessful();
			return result;
	}

	public static boolean isLogged(Context context){		
		boolean result = (
				Utils.getStringFromPreference(context,Config.JSON_USERNAME) != null && 
				Utils.getStringFromPreference(context,Config.JSON_USERNAME).trim().length() > 0 &&
				Utils.getStringFromPreference(context,Config.JSON_PASSWORD) != null &&
				Utils.getStringFromPreference(context,Config.JSON_PASSWORD).trim().length() >0 );
		return result;
	}

	public static void Loggout(Context context){
		int nbr = LinphonePreferences.instance().getAccountCount();
		if(nbr>0){
			for(int i=0; i<nbr ; i++){
				LinphonePreferences.instance().deleteAccount(i);
			}
		}
		Utils.putStringToPreference(context, "status",null);
		Utils.putStringToPreference(context, Config.JSON_ID,null);
		Utils.putStringToPreference(context, Config.JSON_USERNAME, "");
		Utils.putStringToPreference(context, Config.JSON_PASSWORD, "");
		Utils.putStringToPreference(context, Config.USER, "");
		Utils.putStringToPreference(context, Config.PWD, "");
	}
	
    @Override
    public void onBackPressed(){ 	
    	startActivity(new Intent(LoginActivity.this, LinphoneLauncherActivity.class));
    	finish();    	
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
    
//	private void startSipService(){
//		mHandler = new Handler();
//		
//		if (LinphoneService.isReady()) {
//			onServiceReady();
//		} else {
//			// start linphone as background  
//			startService(new Intent(ACTION_MAIN).setClass(this, LinphoneService.class));
//			mThread = new ServiceWaitThread();
//			mThread.start();
//		}
//	}
	
//	protected void onServiceReady() {
//	final Class<? extends Activity> classToStart;
//	classToStart = LinphoneActivity.class;
//	
//	LinphoneService.instance().setActivityToLaunchOnIncomingReceived(classToStart);
//	mHandler.postDelayed(new Runnable() {
//		@Override
//		public void run() {
//			startActivity(new Intent().setClass(LoginActivity.this, classToStart).setData(getIntent().getData()));
////			finish();
//		}
//	}, 1000);
//}
	
	
//	private class ServiceWaitThread extends Thread {
//	public void run() {
//		while (!LinphoneService.isReady()) {
//			try {
//				sleep(30);
//			} catch (InterruptedException e) {
//				throw new RuntimeException("waiting thread sleep() has been interrupted");
//			}
//		}
//
//		mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				onServiceReady();
//			}
//		});
//		mThread = null;
//	}
//}
	
	private LinphoneOnRegistrationStateChangedListener registrationListener = new LinphoneOnRegistrationStateChangedListener() {
		public void onRegistrationStateChanged(LinphoneProxyConfig proxy, RegistrationState state, String message) {
			if (state == RegistrationState.RegistrationOk) {
				LinphoneManager.removeListener(registrationListener);
				
				if (LinphoneManager.getLc().getDefaultProxyConfig() != null) {
					mHandler .post(new Runnable () {
						public void run() {
//							launchEchoCancellerCalibration(true);
							Toast.makeText(LoginActivity.this, "launchEchoCancellerCalibration", Toast.LENGTH_LONG).show();
						}
					});
				}
			} else if (state == RegistrationState.RegistrationFailed) {
				LinphoneManager.removeListener(registrationListener);
				mHandler.post(new Runnable () {
					public void run() {
						Toast.makeText(LoginActivity.this, getString(R.string.first_launch_bad_login_password), Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	};
	
    private void sendBroadcast(int action){
//    	broadcastIntent.putExtra("action", action);
//    	sendBroadcast(broadcastIntent);
    }
    

}
