package org.linphone;

import org.linphone.asycntasks.BuyCreditTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionFragment extends Fragment implements OnClickListener{

	private LayoutInflater mInflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.options, container, false);
		LinearLayout faq = (LinearLayout) view.findViewById(R.id.faqlayer);
		faq.setOnClickListener(this);
		
		LinearLayout addcredit = (LinearLayout) view.findViewById(R.id.addcreditlayer);
		addcredit.setOnClickListener(this);
		
		LinearLayout assistance = (LinearLayout) view.findViewById(R.id.assistancelayer);
		assistance.setOnClickListener(this);

		LinearLayout profil = (LinearLayout) view.findViewById(R.id.profillayer);
		profil.setOnClickListener(this);
		
//		LinearLayout contact = (LinearLayout) view.findViewById(R.id.contactlayer);
//		contact.setOnClickListener(this);
		
		TextView disconnect = (TextView) view.findViewById(R.id.disconnect);
		disconnect.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		String weburl = "https://www.africallshop.com/"+getActivity().getString(R.string.applang)+"/faq/android.php";
		if(id == R.id.faqlayer){
//			String weburl = getActivity().getString(R.string.faq_url);
			LinphoneActivity.instance().displayWeburl(weburl);
		}else if(id == R.id.disconnect){
			LinphoneActivity.instance().exit();
		}else if(id == R.id.assistancelayer){
			LinphoneActivity.instance().displayAssistance();
		}else if(id == R.id.profillayer){
			LinphoneActivity.instance().displayProfil();
		}else if(id == R.id.addcreditlayer){
			new BuyCreditTask(getActivity()).execute("");
		}
	}

	
	@Override
	public void onResume() {
		super.onResume();
		
		if (LinphoneActivity.isInstanciated()) {
			LinphoneActivity.instance().selectMenu(FragmentsAvailable.SETTINGS);
			
			if (getResources().getBoolean(R.bool.show_statusbar_only_on_dialer)) {
				LinphoneActivity.instance().hideStatusBar();
			}
		}
	}

	
}
