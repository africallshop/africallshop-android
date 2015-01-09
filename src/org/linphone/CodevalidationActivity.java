package org.linphone;

import org.linphone.LinphonePreferences.AccountBuilder;
import org.linphone.asycntasks.LoginTask;
import org.linphone.asycntasks.ValidateCodeTask;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneAddress.TransportType;
import org.linphone.core.LinphoneCore.MediaEncryption;
import org.linphone.utils.Config;
import org.linphone.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CodevalidationActivity extends Activity implements OnClickListener{
	private RelativeLayout back, next;
	Button validate;
	EditText sms_code;
	public static int REQUEST_CODE = 200;
    private BroadcastReceiver broadcastMessageReceiver;
	private LinphonePreferences mPrefs;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hack to avoid to draw twice LinphoneActivity on tablets
        if (getResources().getBoolean(R.bool.isTablet)) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.codevalidation);
        mPrefs = LinphonePreferences.instance();
		initUi();
	}
	
	
	private void initUi(){
		back = (RelativeLayout) findViewById(R.id.setup_back);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		
		next = (RelativeLayout) findViewById(R.id.setup_next);
		next.setVisibility(View.GONE);
		
		validate = (Button) findViewById(R.id.validate_button);
		validate.setOnClickListener(this);
		
		sms_code = (EditText) findViewById(R.id.sms_code);

	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.setup_back) {
			startActivity(new Intent(CodevalidationActivity.this, PhonenumbervalidationActivity.class));
			finish();
		}
		if (id == R.id.validate_button) {
			new ValidateCodeTask(this).execute(sms_code.getText().toString());	
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
	}
	
	private void dispatch(Intent intent){
		int action = intent.getIntExtra("action", 0);
		
		switch (action) {
			case Config.ACTION_LOGGED_IN:
					if(accountCreated()){
						launchHome();
//						
					}else{
						Toast.makeText(this, getString(R.string.task_status_ko), Toast.LENGTH_LONG).show();
					}
				break;
			case Config.ACTION_GOT_CODE_VALIDATE:
				String login = intent.getStringExtra("login");
				String pass = intent.getStringExtra("password");
				new LoginTask(this).execute(login,pass);
			default:
				break;
		}
	}
	
	
	private boolean accountCreated(){
		int created = LinphonePreferences.instance().getAccountCount();
		if (created == 0){
			return createaccount();
		}
		return true;
	}
	
	
	@Override
	protected void onPause(){
		super.onPause();
		this.unregisterReceiver(this.broadcastMessageReceiver);
	}
	
	private void launchHome(){
		startActivity(new Intent(CodevalidationActivity.this, LinphoneActivity.class));
		finish();
	}
	
	private boolean createaccount(){
		boolean result = false;
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
			} catch (LinphoneCoreException e) {
			}
			
			mPrefs.firstLaunchSuccessful();
			return result;
	}
	
    @Override
    public void onBackPressed(){ 	
    	startActivity(new Intent(CodevalidationActivity.this, PhonenumbervalidationActivity.class));
    	finish();    	
    }
}
