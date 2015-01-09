package org.linphone.setup;

import java.util.List;

import org.linphone.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.AbsListView.LayoutParams;

public class KeyvalueAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Keyvalue> mEntries;
	
	public KeyvalueAdapter(Context context, List<Keyvalue> entries){
		mContext = context;
		mEntries = entries;
	}

	@Override
	public int getCount() {
		return mEntries.size();
	}

	@Override
	public  Keyvalue getItem(int position) {
		return mEntries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	//10-29 19:36:19.397: E/AndroidRuntime(2685): java.lang.ClassCastException: android.widget.RelativeLayout$LayoutParams cannot be cast to android.widget.AbsListView$LayoutParams

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		final Keyvalue keyvalue = mEntries.get(position);
		keyvalue.setmContext(mContext);
		v = keyvalue.setID(position).getView();
//		RelativeLayout rlayout = new RelativeLayout(mContext);
//		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		rlayout.setLayoutParams(layoutParams);
//		rlayout.addView(v);
		return v;
	}
	
	public void UpdateDataEntries(List<Keyvalue> entries){
		mEntries = entries;
		notifyDataSetChanged();
	}

}
