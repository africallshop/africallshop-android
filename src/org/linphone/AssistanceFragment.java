package org.linphone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.linphone.setup.Keyvalue;
import org.linphone.setup.KeyvalueAdapter;
import org.linphone.setup.OnValueListener;
import org.linphone.utils.Utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class AssistanceFragment extends Fragment implements  OnClickListener, OnValueListener{

	private LayoutInflater mInflater;
	private ListView assistanceList;
	private View view;
	private KeyvalueAdapter adapter;
	private TextView mail, cancel;
	private HashMap<String, String> values;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		view = mInflater.inflate(R.layout.assistance, container, false);
		assistanceList = (ListView) view.findViewById(R.id.assistanceList);
		values = new HashMap<String, String>();
		
		mail = (TextView)view.findViewById(R.id.mailformbtn); 
		mail.setOnClickListener(this);
		cancel = (TextView)view.findViewById(R.id.cancelbtnass);
		if(LinphoneActivity.instance().onTablet()){
			cancel.setVisibility(View.INVISIBLE);
		}else{
			cancel.setOnClickListener(this);
		}
		
		adapter = new KeyvalueAdapter(getActivity().getBaseContext(), buildList());
		assistanceList.setAdapter(adapter);
		return view;
	}
	
	
	private List<Keyvalue> buildList(){

		List<Keyvalue> list = new ArrayList<Keyvalue>();
		list.add(new Keyvalue(getActivity().getString(R.string.assistance_from_header),"")
			.setType(Keyvalue.TYPE.HEADER)
			.setBackgroundColor(R.color.gray_color)
		);
		list.add(new Keyvalue(getActivity().getString(R.string.assistance_email_subjet_hint),getActivity().getString(R.string.email_content_hint))
			.setType(Keyvalue.TYPE.EMAILFORM)
			.setIdentifier("support_email")
			.setOnValueListener(this)
		);
		
		list.add(new Keyvalue(getActivity().getString(R.string.assistance_device_header),"")
			.setType(Keyvalue.TYPE.HEADER)
			.setBackgroundColor(R.color.gray_color)
		);
		list.add(new Keyvalue("Device", android.os.Build.MODEL));
		list.add(new Keyvalue("Android", Utils.getSystemName()));
		list.add(new Keyvalue("AppName", Utils.getAppName(getActivity())));
		list.add(new Keyvalue("AppVersion", Utils.getAppVersion(getActivity())));	
		return list;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		if (LinphoneActivity.isInstanciated()) {
			LinphoneActivity.instance().selectMenu(FragmentsAvailable.SETTINGS);
			
			if (getResources().getBoolean(R.bool.show_statusbar_only_on_dialer)) {
				LinphoneActivity.instance().hideStatusBar();
			}
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.mailformbtn){
			String all = "";
			for (Entry<String, String> e : values.entrySet()){
				all += e.getValue()+" ";
			}
//			Toast.makeText(getActivity(), all, Toast.LENGTH_LONG).show();
			LinphoneActivity.instance().displayAssistanceMail(buildTopic(), buildBody());
		}else if(id == R.id.cancelbtnass){
			getFragmentManager().popBackStack();
		}
	}
	
	public String buildTopic(){
		return values.get("support_email_topic");
	}
	
	public String buildBody(){
		String html = values.get("support_email_body")+"<br><br>"+
				"Device:<br>"+android.os.Build.MODEL+"<br><br>"+
				"Android:<br>" +Utils.getSystemName()+"<br><br>"+
				"App:<br>"+Utils.getAppName(getActivity())+" "+Utils.getAppVersion(getActivity());
		return html;
	}


	@Override
	public void onEditChanged(String identifier, String value) {
		values.put(identifier, value);		
	}


	@Override
	public void onButtonSelect(String identifier) {
		// TODO Auto-generated method stub
		
	}
}
