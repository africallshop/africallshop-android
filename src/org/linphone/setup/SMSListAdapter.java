package org.linphone.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.linphone.LinphoneActivity;
import org.linphone.LinphoneUtils;
import org.linphone.R;
import org.linphone.mediastream.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SMSListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private HashMap<String, List<SMS>> mEntries;
	private String[] mkeys;
	private boolean isInstanciated;
	
	public SMSListAdapter(Context context, boolean isInstanciated){
		mContext = context;
		this.isInstanciated = isInstanciated;
		mEntries = new HashMap<String, List<SMS>>();
		mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Context getContext(){
		return mContext;
	}
	
	@Override
	public int getCount() {
		return mEntries.size();
	}
	
	public SMS getSMS(String destination){
		SMS sms = null;
		try{
			sms = (SMS) mEntries.get(destination).toArray()[0];
		}catch(Exception e){
//			Log.e(" no sms found for "+ destination);
			sms = new SMS(destination);
		}
		return sms;
	}

	public List<SMS> getSMSlogs(String destination){
		List<SMS> list = mEntries.get(destination);
		list = list != null ? list: new ArrayList<SMS>();
		return list;
	}
	
	@Override
	public List<SMS> getItem(int position) {
		return mEntries.get(mkeys[position]);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;//convertView;
		if (convertView != null) {
			view = convertView;
//			viewholder = new SmsViewholder(view);
//			view.setTag(viewholder);
		} else {
//			viewholder = (SmsViewholder) view.getTag();
			view = mLayoutInflater.inflate(R.layout.chatlist_cell, parent,false);
		}
		
		int nbr = getItem(position).size();
		final SMS smslog = (SMS) getItem(position).toArray()[0];
		final List<SMS> logs = getItem(position);
		TextView contact = (TextView) view.findViewById(R.id.sipUri);
		contact.setSelected(true); // For automated horizontal scrolling of long texts
		String dname = LinphoneUtils.getContactName(mContext,smslog.destination);
		String nbrtext = "";
		if(nbr > 1){
			nbrtext = " ("+nbr+")";
		}
		if(dname != null && dname.trim().length()>0){
			contact.setText(dname+nbrtext);
		}else{
			contact.setText(smslog.destination+nbrtext);
		}
		
		TextView message = (TextView) view.findViewById(R.id.lastMessage);
		message.setText(smslog.getShortMessage());
		
		LinearLayout wrapper  = (LinearLayout) view.findViewById(R.id.wrapper);
		wrapper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInstanciated) {
					LinphoneActivity.instance().displayChatMessage(smslog,logs);
				}
			}
		});
		return view;
	}
	
    public void upDateEntries(HashMap<String, List<SMS>> entries) {
        mEntries = entries;
        mkeys = mEntries.keySet().toArray(new String[entries.size()]);
        this.notifyDataSetChanged();
     }
}
