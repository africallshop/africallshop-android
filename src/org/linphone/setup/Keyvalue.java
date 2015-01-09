package org.linphone.setup;

import org.linphone.R;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Keyvalue {
	private int ID;
	private String identifier;
	private String key;
	private String value;
	private boolean keypositioncentered=false;
	private boolean valuepositioncentered=true;
	private boolean keybold = false;
	private boolean keyvisible = true;
	private int valuegrativy = Gravity.LEFT;
	private int backgroundColor = android.R.color.white;
	private int keyTextColor = android.R.color.black;
	private int ValueTextColor = android.R.color.black;
	private TYPE type = TYPE.SIMPLE;
	private OnClickListener emaillistener;
//	LayoutParams layoutParams; 
	private Context mContext;
	
	OnValueListener onValueListener;
	
	public Keyvalue(){
	}
	
	public Keyvalue(String k, String v){
		setKey(k);
		setValue(v);
	}
	
	public Keyvalue(String k, String v, TYPE viewtype){
		this(k, v);
		if(viewtype!= null){
			type= viewtype;
		}
	}
	
	private View getHeaderView(){
		LinearLayout view = new LinearLayout(mContext);
		LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(viewParams);
		view.setBackgroundResource(this.backgroundColor);
		// Layout Prams
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = 12;
		layoutParams.bottomMargin = 12;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.kevalue_header_row, null);
		layout.setLayoutParams(layoutParams);
		// TextView filling
		TextView keypart = (TextView) layout.findViewById(R.id.keypart);
		keypart.setText(getKey());
		view.addView(layout);
		return view;
	}
	
	private View getMailSimpleView(){
		LinearLayout view = new LinearLayout(mContext);
		LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(viewParams);
		view.setBackgroundResource(this.backgroundColor);
		// Layout Prams
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = 12;
		layoutParams.bottomMargin = 12;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.kevalue_simple_row_email, null);
		layout.setLayoutParams(layoutParams);
		// TextView filling
		TextView keypart = (TextView) layout.findViewById(R.id.keypart);
		keypart.setTextColor(mContext.getResources().getColor(keyTextColor));
		if(keybold){
			keypart.setTypeface(null, Typeface.BOLD);
		}
		keypart.setText(getKey()!= null && getKey().trim().length() >0 ? getKey()+":" : "");
		TextView valuepart = (TextView) layout.findViewById(R.id.valuepart);
//		valuepart.setGravity(this.valuegrativy);
//		valuepart.setTextColor(mContext.getResources().getColor(ValueTextColor));
		valuepart.setText(getValue() != null? Html.fromHtml(getValue()): getValue());
		view.addView(layout);
		return view;
	}
	
	private View getSimpleView(){
		LinearLayout view = new LinearLayout(mContext);
		LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(viewParams);
		view.setBackgroundResource(this.backgroundColor);
		// Layout Prams
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = 12;
		layoutParams.bottomMargin = 12;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.kevalue_simple_row, null);
		layout.setLayoutParams(layoutParams);
		// TextView filling
		TextView keypart = (TextView) layout.findViewById(R.id.keypart);
		keypart.setTextColor(mContext.getResources().getColor(keyTextColor));
		if(keybold){
			keypart.setTypeface(null, Typeface.BOLD);
		}
		keypart.setText(getKey());
		TextView valuepart = (TextView) layout.findViewById(R.id.valuepart);
		valuepart.setGravity(this.valuegrativy);
		valuepart.setTextColor(mContext.getResources().getColor(ValueTextColor));
		valuepart.setText(getValue() != null? Html.fromHtml(getValue()): getValue());
		view.addView(layout);
		return view;
	}
	
	private View getMailView(){
		LinearLayout view = new LinearLayout(mContext);
		LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(viewParams);
		view.setBackgroundResource(this.backgroundColor);
		// Layout Prams
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = 12;
		layoutParams.bottomMargin = 12;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.kevalue_email_row, null);
		layout.setLayoutParams(layoutParams);
		// TextView filling
		EditText keypart = (EditText) layout.findViewById(R.id.keypart);
		keypart.setHint(getKey());
		
		EditText valuepart = (EditText) layout.findViewById(R.id.valuepart);
		valuepart.setHint(getValue());
		
		if(onValueListener != null && identifier != null){
			keypart.addTextChangedListener(new TextWatcher(){
		        public void afterTextChanged(Editable s) {
		            
		        }
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){
		        	
		        }
		        public void onTextChanged(CharSequence s, int start, int before, int count){
		        	onValueListener.onEditChanged(getIdentifier()+"_topic", s.toString());
		        }
		    }); 
			
			valuepart.addTextChangedListener(new TextWatcher(){
		        public void afterTextChanged(Editable s) {
		            
		        }
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){
		        	
		        }
		        public void onTextChanged(CharSequence s, int start, int before, int count){
		        	onValueListener.onEditChanged(getIdentifier()+"_body", s.toString());
		        }
		    }); 
		}
		
		Button emailbutton = (Button) layout.findViewById(R.id.emailbutton);
		emailbutton.setOnClickListener(emaillistener);
		
		view.addView(layout);
		return view;
	}
	
	
	private View getSimpleEditView(){
		LinearLayout view = new LinearLayout(mContext);
		LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(viewParams);
		view.setBackgroundResource(this.backgroundColor);
		// Layout Prams
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = 12;
		layoutParams.bottomMargin = 12;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.kevalue_simple_row_edit, null);
		layout.setLayoutParams(layoutParams);
		// TextView filling
		TextView keypart = (TextView) layout.findViewById(R.id.keypart);
		keypart.setTextColor(mContext.getResources().getColor(keyTextColor));
		if(keybold){
			keypart.setTypeface(null, Typeface.BOLD);
		}
		keypart.setText(getKey());
		EditText valuepart = (EditText) layout.findViewById(R.id.valuepart);
		valuepart.setGravity(this.valuegrativy);
		valuepart.setTextColor(mContext.getResources().getColor(ValueTextColor));
		valuepart.setText(getValue());
		if(onValueListener != null && identifier != null){
			valuepart.addTextChangedListener(new TextWatcher(){
		        public void afterTextChanged(Editable s) {
		            
		        }
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){
		        	
		        }
		        public void onTextChanged(CharSequence s, int start, int before, int count){
		        	onValueListener.onEditChanged(getIdentifier(), s.toString());
		        }
		    }); 
		}
		view.addView(layout);
		return view;
	}
	
	public View getView(){
		View v = null;
		switch(type){
		case HEADER:
			v = getHeaderView();
			break;
		case SIMPLE:
			v = getSimpleView();
			break;
		case EMAILFORM:
			v = getMailView();
			break;
		case SIMPLE_EMAIL:
			v = getMailSimpleView();
			break;
		case SIMPLE_EDIT:
			v = getSimpleEditView();
			break;
		}
		return v;
	}
	
	public Keyvalue setType(TYPE t){
		this.type = t;
		return this;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Context getmContext() {
		return mContext;
	}

	public Keyvalue setmContext(Context mContext) {
		this.mContext = mContext;
		return this;
	}


	public boolean isKeypositioncentered() {
		return keypositioncentered;
	}

	public Keyvalue setKeypositioncentered(boolean keypositioncentered) {
		this.keypositioncentered = keypositioncentered;
		return this;
	}


	public boolean isValuepositioncentered() {
		return valuepositioncentered;
	}

	public Keyvalue setValuepositioncentered(boolean valuepositioncentered) {
		this.valuepositioncentered = valuepositioncentered;
		return this;
	}


	public int getBackgroundColor() {
		return backgroundColor;
	}

	public Keyvalue setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}


	public int getValuegrativy() {
		return valuegrativy;
	}

	public Keyvalue setValuegrativy(int valuegrativy) {
		this.valuegrativy = valuegrativy;
		return this;
	}


	public OnClickListener getEmaillistener() {
		return emaillistener;
	}

	public Keyvalue setEmaillistener(OnClickListener emaillistener) {
		this.emaillistener = emaillistener;
		return this;
	}


	public int getKeyTextColor() {
		return keyTextColor;
	}

	public Keyvalue setKeyTextColor(int keyTextColor) {
		this.keyTextColor = keyTextColor;
		return this;
	}


	public int getValueTextColor() {
		return ValueTextColor;
	}

	public Keyvalue setValueTextColor(int valueTextColor) {
		ValueTextColor = valueTextColor;
		return this;
	}


	public boolean isKeybold() {
		return keybold;
	}

	public Keyvalue setKeybold(boolean keybold) {
		this.keybold = keybold;
		return this;
	}


	public boolean isKeyvisible() {
		return keyvisible;
	}

	public Keyvalue setKeyvisible(boolean keyvisible) {
		this.keyvisible = keyvisible;
		return this;
	}


	public int getID() {
		return ID;
	}

	public Keyvalue setID(int iD) {
		ID = iD;
		return this;
	}


	public String getIdentifier() {
		return identifier;
	}

	public Keyvalue setIdentifier(String identifier) {
		this.identifier = identifier;
		return this;
	}


	public enum TYPE{
		HEADER,
		SIMPLE,
		SIMPLE_EMAIL,
		SIMPLE_EDIT,
		EMAILFORM,
	}
	
	public Keyvalue setOnValueListener(OnValueListener listener){
		onValueListener = listener;
		return this;
	}
	
}



