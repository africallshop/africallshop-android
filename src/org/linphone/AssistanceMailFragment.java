package org.linphone;

import java.util.ArrayList;
import java.util.List;

import org.linphone.setup.Keyvalue;
import org.linphone.setup.KeyvalueAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class AssistanceMailFragment extends Fragment implements OnClickListener {

	private LayoutInflater mInflater;
	private ListView assistanceList;
	private View view;
	private KeyvalueAdapter adapter;
	private String  topic, body;
	private TextView send, cancel;
//	Intent emailIntent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		topic = getArguments().getString("topic");
		body = getArguments().getString("body");
		
		
		view = mInflater.inflate(R.layout.assistance_email, container, false);
		
		send = (TextView)view.findViewById(R.id.sendbtn); 
		send.setOnClickListener(this);
		cancel = (TextView)view.findViewById(R.id.cancelbtnass);
		if(LinphoneActivity.instance().onTablet()){
			cancel.setVisibility(View.INVISIBLE);
		}else{
			cancel.setOnClickListener(this);
		}
		
		assistanceList = (ListView) view.findViewById(R.id.assistanceList);
		adapter = new KeyvalueAdapter(getActivity().getBaseContext(), buildList());
		assistanceList.setAdapter(adapter);
		return view;
	}
	
	
	private List<Keyvalue> buildList(){
		List<Keyvalue> list = new ArrayList<Keyvalue>();
		list.add(new Keyvalue(getActivity().getString(R.string.email_to), "support@africallshop.com").setType(Keyvalue.TYPE.SIMPLE_EMAIL).setKeyTextColor(R.color.gray_color));
		list.add(new Keyvalue("Cc/Bcc","").setType(Keyvalue.TYPE.SIMPLE_EMAIL).setKeyTextColor(R.color.gray_color));
		list.add(new Keyvalue(getActivity().getString(R.string.email_subject), topic).setType(Keyvalue.TYPE.SIMPLE_EMAIL).setKeyTextColor(R.color.gray_color));
		list.add(new Keyvalue("", body).setType(Keyvalue.TYPE.SIMPLE_EMAIL));
		return list;
	}
	
	public void updatemailcontent(String t, String b){
		topic = t;
		body = b;
		adapter.UpdateDataEntries(buildList());
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.sendbtn){
			SendEmail();
		}else if(id == R.id.cancelbtnass){
			getFragmentManager().popBackStack();
		}
	}
	
	private void SendEmail(){
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
	    String[] TO = {"support@africallshop.com"};
	    emailIntent.setData(Uri.parse("mailto:"));
	    emailIntent.setType("text/html");
	    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, topic);
	    emailIntent.putExtra(Intent.EXTRA_TEXT,  Html.fromHtml(body));
	    getActivity().startActivity(Intent.createChooser(emailIntent, "Email:"));
	}
	
}
