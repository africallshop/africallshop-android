package org.linphone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.linphone.asycntasks.SetProfilTask;
import org.linphone.setup.Keyvalue;
import org.linphone.setup.KeyvalueAdapter;
import org.linphone.setup.OnValueListener;
import org.linphone.utils.Config;
import org.linphone.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ProfilFragment extends Fragment implements OnClickListener, OnValueListener{

	private LayoutInflater mInflater;
	private ListView profileList;
	private View view;
	private KeyvalueAdapter adapter;
	private TextView save;
	private TextView cancel;
	private HashMap<String, String> values;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		view = mInflater.inflate(R.layout.profil, container, false);
		values = new HashMap<String, String>();
		profileList = (ListView) view.findViewById(R.id.profileList);
		adapter = new KeyvalueAdapter(getActivity().getBaseContext(), buildList());
		profileList.setAdapter(adapter);
		save = (TextView)view.findViewById(R.id.save); 
		save.setOnClickListener(this);
		cancel = (TextView)view.findViewById(R.id.backbtn);
		if(LinphoneActivity.instance().onTablet()){
			cancel.setVisibility(View.INVISIBLE);
		}else{
			cancel.setOnClickListener(this);
		}
		return view;
	}
	
	private List<Keyvalue> buildList(){
		List<Keyvalue> list = new ArrayList<Keyvalue>();
		list.add(new Keyvalue(getActivity().getString(R.string.profile_first_name),Utils.getStringFromPreference(getActivity(),Config.JSON_FIRSTNAME))
			.setType(Keyvalue.TYPE.SIMPLE_EDIT)
			.setIdentifier(Config.JSON_FIRSTNAME)
			.setOnValueListener(this)
			);
		values.put(Config.JSON_FIRSTNAME, Utils.getStringFromPreference(getActivity(),Config.JSON_FIRSTNAME));
		list.add(new Keyvalue(getActivity().getString(R.string.profile_last_name),Utils.getStringFromPreference(getActivity(),Config.JSON_LASTNAME))
			.setType(Keyvalue.TYPE.SIMPLE_EDIT)
			.setIdentifier(Config.JSON_LASTNAME)
			.setOnValueListener(this)
			);
		values.put(Config.JSON_LASTNAME, Utils.getStringFromPreference(getActivity(),Config.JSON_LASTNAME));
		list.add(new Keyvalue(getActivity().getString(R.string.profile_zip_code),Utils.getStringFromPreference(getActivity(),Config.JSON_ZIPCODE))
			.setType(Keyvalue.TYPE.SIMPLE_EDIT)
			.setIdentifier(Config.JSON_ZIPCODE)
			.setOnValueListener(this)
			);
		values.put(Config.JSON_ZIPCODE, Utils.getStringFromPreference(getActivity(),Config.JSON_ZIPCODE));
		list.add(new Keyvalue(getActivity().getString(R.string.profile_address),Utils.getStringFromPreference(getActivity(),Config.JSON_ADDRESS))
			.setType(Keyvalue.TYPE.SIMPLE_EDIT)
			.setIdentifier(Config.JSON_ADDRESS)
			.setOnValueListener(this)
			);
		values.put(Config.JSON_ADDRESS, Utils.getStringFromPreference(getActivity(),Config.JSON_ADDRESS));
		list.add(new Keyvalue(getActivity().getString(R.string.profile_city),Utils.getStringFromPreference(getActivity(),Config.JSON_CITY))
			.setType(Keyvalue.TYPE.SIMPLE_EDIT)
			.setIdentifier(Config.JSON_CITY)
			.setOnValueListener(this)
			);
		values.put(Config.JSON_CITY, Utils.getStringFromPreference(getActivity(),Config.JSON_CITY));
		list.add(new Keyvalue(getActivity().getString(R.string.profile_country),Utils.getStringFromPreference(getActivity(),Config.JSON_COUNTRY))
			.setType(Keyvalue.TYPE.SIMPLE_EDIT)
			.setIdentifier(Config.JSON_COUNTRY)
			.setOnValueListener(this)
			);
		values.put(Config.JSON_COUNTRY, Utils.getStringFromPreference(getActivity(),Config.JSON_COUNTRY));
		list.add(new Keyvalue(getActivity().getString(R.string.profile_phone_number),Utils.getMyPhoneNumber(getActivity())));		
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.save){
			new SetProfilTask(getActivity()).execute(
					values.get(Config.JSON_FIRSTNAME),
					values.get(Config.JSON_LASTNAME),
					values.get(Config.JSON_ZIPCODE),
					values.get(Config.JSON_ADDRESS),
					values.get(Config.JSON_COUNTRY),
					Utils.getStringFromPreference(getActivity(), Config.JSON_CALLERID)
					);
		}else if(id == R.id.backbtn){
//			LinphoneActivity.instance().backbutton(FragmentsAvailable.PROFILE.toString());
			getFragmentManager().popBackStackImmediate();
//			getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)); 
		}	
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
