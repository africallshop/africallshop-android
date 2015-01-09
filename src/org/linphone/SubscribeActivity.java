package org.linphone;

import org.linphone.asycntasks.InscriptionTask;
import org.linphone.utils.Config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubscribeActivity extends Activity implements OnClickListener{
	private RelativeLayout back, next;
	Button validate;
	EditText email, password, confirmpassword;
	TextView termeusage;
    private BroadcastReceiver broadcastMessageReceiver;
    
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hack to avoid to draw twice LinphoneActivity on tablets
        if (getResources().getBoolean(R.bool.isTablet)) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.subscribe);
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

		
		termeusage = (TextView)findViewById(R.id.term_usage);
		termeusage.setOnClickListener(this);
		
		email = (EditText) findViewById(R.id.user_email);
		password = (EditText) findViewById(R.id.user_password);
		confirmpassword = (EditText) findViewById(R.id.user_validate_password);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.setup_back) {
			startActivity(new Intent(SubscribeActivity.this, LoginActivity.class));
			finish();
		}
		if (id == R.id.validate_button) {
				new InscriptionTask(this).execute(
						getString(R.string.applang),
						email.getText().toString(),password.getText().toString(),
						confirmpassword.getText().toString());	
		}
		
		if (id == R.id.term_usage) {
			String weburl = getString(R.string.usage_terms);
			startActivity(new Intent().setClass(SubscribeActivity.this, WebActivity.class).putExtra(WebActivity.WEBLINK, weburl));
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
			case Config.ACTION_PHONE_VALIDATION:
				new AlertDialog.Builder(this)
		        .setMessage(getString(R.string.error_phonenumber_need_validation))
		        .setNeutralButton(R.string.verify_now, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();
						startActivity(new Intent(SubscribeActivity.this, PhonenumbervalidationActivity.class));
						finish();
					}
				})
		        .show();
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		this.unregisterReceiver(this.broadcastMessageReceiver);
	}
	
    @Override
    public void onBackPressed(){ 	
    	startActivity(new Intent(SubscribeActivity.this, LoginActivity.class));
    	finish();    	
    }
}
