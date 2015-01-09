package org.linphone;

import org.linphone.asycntasks.ValidatePhoneTask;
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

public class PhonenumbervalidationActivity extends Activity implements OnClickListener{
	private RelativeLayout back, next;
	Button validate;
	EditText phone_number;
	public static int REQUEST_CODE = 300;
	private BroadcastReceiver broadcastMessageReceiver;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hack to avoid to draw twice LinphoneActivity on tablets
        if (getResources().getBoolean(R.bool.isTablet)) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.phonenumbervalidation);
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
		
		phone_number = (EditText) findViewById(R.id.phone_number);

	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.setup_back) {
			startActivity(new Intent(PhonenumbervalidationActivity.this, LoginActivity.class));
			finish();
		}
		if (id == R.id.validate_button) {
			if(phone_number.getText().toString().trim().length()<7){
			new AlertDialog.Builder(this)
	        .setMessage(getString(R.string.phonenumber_lentgh_ko))
	        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();						
				} 
			})
	        .show();
			}else{
				new ValidatePhoneTask(this).execute(phone_number.getText().toString());
			}
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
			case Config.ACTION_CODE_VALIDATION:
				new AlertDialog.Builder(this)
		        .setMessage(getString(R.string.validate_phone_ok))
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();
						startActivity(new Intent(PhonenumbervalidationActivity.this, CodevalidationActivity.class));
						finish();
					}
				})
		        .show();
				break;
		}
	}
	
    @Override
    public void onBackPressed(){ 	
    	startActivity(new Intent(PhonenumbervalidationActivity.this, LoginActivity.class));
    	finish();    	
    }
    
	@Override
	protected void onPause(){
		super.onPause();
		this.unregisterReceiver(this.broadcastMessageReceiver);
	}
	
}
