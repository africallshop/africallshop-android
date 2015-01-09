package org.linphone;

import java.util.ArrayList;
import java.util.List;

import org.linphone.setup.Keyvalue;
import org.linphone.setup.KeyvalueAdapter;
import org.linphone.utils.Utils;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("ResourceAsColor")
public class MailFragment extends Fragment {

	private LayoutInflater mInflater;
	private ListView assistanceList;
	private View view;
	private KeyvalueAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		view = inflater.inflate(R.layout.assistance_email, container, false);
		assistanceList = (ListView) view.findViewById(R.id.assistanceList);
		adapter = new KeyvalueAdapter(getActivity().getBaseContext(), buildList());
		assistanceList.setAdapter(adapter);
		return view;
	}
	
	
	private List<Keyvalue> buildList(){
		String versionName = " ..... ";
		String AppName = " ..... ";
		try {
		    versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName + "/" + 
		    		getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode ;
		    AppName = getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()).toString();
		} catch (Exception e) {
		}
		List<Keyvalue> list = new ArrayList<Keyvalue>();
		list.add(new Keyvalue(getActivity().getString(R.string.assistance_from_header),"").setType(Keyvalue.TYPE.HEADER).setBackgroundColor(android.R.color.transparent));
		list.add(new Keyvalue(getActivity().getString(R.string.assistance_email_subjet_hint),
				getActivity().getString(R.string.email_content_hint)).setType(Keyvalue.TYPE.EMAILFORM));
		
		list.add(new Keyvalue(getActivity().getString(R.string.assistance_device_header),"").setType(Keyvalue.TYPE.HEADER).setBackgroundColor(android.R.color.transparent));
		list.add(new Keyvalue("Device", android.os.Build.MODEL));
		list.add(new Keyvalue("Android", Utils.getSystemName()));
		list.add(new Keyvalue("AppName", AppName));
		list.add(new Keyvalue("AppVersion", versionName));
		
		return list;
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
