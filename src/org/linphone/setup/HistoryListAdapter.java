package org.linphone.setup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;
import org.linphone.LinphoneActivity;
import org.linphone.LinphoneUtils;
import org.linphone.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryListAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ArrayList<JSONObject> mEntries = new ArrayList<JSONObject>();
	private Bitmap outgoingCall;
	private boolean isInstanciated;
	
	public HistoryListAdapter(Context context, boolean isInstanciated){
		mContext = context;
		this.isInstanciated = isInstanciated;
		mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		outgoingCall = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.call_status_outgoing);
	}
	
	@Override
	public int getCount() {
		return mEntries.size();
	}
	
	public Context getContext(){
		return mContext;
	}

	@Override
	public Object getItem(int position) {
		return mEntries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    public void upDateEntries(ArrayList<JSONObject> entries) {
        mEntries = entries;
        this.notifyDataSetChanged();
     }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mLayoutInflater.inflate(R.layout.history_cell_simple, parent,false);
		}
		
		final HistoryLog log = new HistoryLog(mEntries.get(position));
		TextView contact = (TextView) view.findViewById(R.id.sipUri);
		contact.setSelected(true); // For automated horizontal scrolling of long texts
		String dname = LinphoneUtils.getContactName(mContext,log.numero_appele);
		if(dname != null && dname.trim().length()>0){
			contact.setText(dname);
		}else{
			contact.setText(log.numero_appele);
		}
		
		contact.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				LinphoneActivity.instance().setAddresGoToDialerAndCall(log.numero_appele, log.getNom(mContext), null);
			}
		});
//		ImageView delete = (ImageView) view.findViewById(R.id.delete);
		
		ImageView callDirection = (ImageView) view.findViewById(R.id.icon);
		callDirection.setImageBitmap(outgoingCall);
		
		TextView country = (TextView) view.findViewById(R.id.country);
		country.setText(log.pays_appele);
		
		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(log.historydate());
		
		ImageView detail = (ImageView) view.findViewById(R.id.detail);
		detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInstanciated) {
					LinphoneActivity.instance().displayHistoryDetail(log);
				}
			}
		});
		view.setTag(log);
		return view;
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private String timestampToHumanDate(Calendar cal) {
		SimpleDateFormat dateFormat;
		if (isToday(cal)) {
			return mContext.getString(R.string.today);
		} else if (isYesterday(cal)) {
			return mContext.getString(R.string.yesterday);
		} else {
			dateFormat = new SimpleDateFormat(mContext.getResources().getString(R.string.history_date_format));
		}
		
		return dateFormat.format(cal.getTime());
	}
	
	private boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
	
	private boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }
	
	private boolean isYesterday(Calendar cal) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.roll(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(cal, yesterday);
    }		
}

