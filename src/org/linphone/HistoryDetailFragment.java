package org.linphone;
/*
HistoryDetailFragment.java
Copyright (C) 2012  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.setup.Keyvalue;
import org.linphone.setup.KeyvalueAdapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Sylvain Berfini
 */
public class HistoryDetailFragment extends Fragment implements OnClickListener {
	private ImageView addToContacts;
	private Button dialBack, chat;
	private View view;
	private TextView contactName, contactAddress, callDirection, time, date, cost, country, backbtn;
	private String sipUri, displayName, pictureUri,contact;
	private ListView historyDetailList;
	private KeyvalueAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sipUri = getArguments().getString("SipUri");
		displayName = getArguments().getString("DisplayName");
		pictureUri = getArguments().getString("PictureUri");
		String status = getArguments().getString("CallStatus");
		String callTime = getArguments().getString("CallTime");
		String callDate = getArguments().getString("CallDate");
		String callCost = getArguments().getString("CallCost");
		String callCountry = getArguments().getString("CallCountry");
		String contact = getArguments().getString("Contact");
		
		view = inflater.inflate(R.layout.history_detail, container, false);
		
		historyDetailList = (ListView) view.findViewById(R.id.historyDetailList);
		
		dialBack = (Button) view.findViewById(R.id.dialBack);
		dialBack.setOnClickListener(this);
		
		
		backbtn = (TextView) view.findViewById(R.id.backbtn);
		backbtn.setOnClickListener(this);
		
		chat = (Button) view.findViewById(R.id.chat);
		chat.setOnClickListener(this);
		if (getResources().getBoolean(R.bool.disable_chat))
			view.findViewById(R.id.chatRow).setVisibility(View.GONE);
		
		addToContacts = (ImageView) view.findViewById(R.id.addToContacts);
		addToContacts.setOnClickListener(this);
		
		contactName = (TextView) view.findViewById(R.id.contactName);
//		if (displayName == null && getResources().getBoolean(R.bool.only_display_username_if_unknown) && LinphoneUtils.isSipAddress(sipUri)) {
		displayName = LinphoneUtils.getContactName(getActivity(), contact);
//		}
		
		contactAddress = (TextView) view.findViewById(R.id.contactAddress);
//		callDirection = (TextView) view.findViewById(R.id.callDirection);
		
		time = (TextView) view.findViewById(R.id.time);
		date = (TextView) view.findViewById(R.id.date);
		cost = (TextView) view.findViewById(R.id.cost);
		country = (TextView) view.findViewById(R.id.country);
		
		displayHistory(status, callTime, callDate,callCost,callCountry, contact);
		
		return view;
	}
	
	private void displayHistory(String status, String callTime, String callDate, String callCost, String callCountry, String contact) {
//		contactName.setText(displayName == null ? sipUri : displayName);
//		if (getResources().getBoolean(R.bool.never_display_sip_addresses)) {
//			contactAddress.setText(LinphoneUtils.getUsernameFromAddress(sipUri));
//		} else {
//			contactAddress.setText(sipUri);
//		}
		contactName.setText(displayName);
//		if (status.equals("Missed")) {
//			callDirection.setText(getString(R.string.call_state_missed));
//		} else if (status.equals("Incoming")) {
//			callDirection.setText(getString(R.string.call_state_incoming));
//		} else if (status.equals("Outgoing")) {
//			callDirection.setText(getString(R.string.call_state_outgoing));
//		} else {
//			callDirection.setText(status);
//		}
		
		time.setText(callTime == null ? "" : callTime);
//		date.setText(timestampToHumanDate(callDate));
		date.setText(callDate);
		cost.setText(callCost);
		country.setText(callCountry);
		contactAddress.setText(contact);		
		
//		adapter = new KeyvalueAdapter(getActivity().getBaseContext(), buildList(callTime, callDate, callCost, callCountry));
//		historyDetailList.setAdapter(adapter);
		
		this.contact = contact;
//		LinphoneAddress lAddress;
//		try {
//			lAddress = LinphoneCoreFactory.instance().createLinphoneAddress(sipUri);
//			LinphoneUtils.findUriPictureOfContactAndSetDisplayName(lAddress, view.getContext().getContentResolver());
//			String displayName = lAddress.getDisplayName();
//			if (displayName != null) {
//				view.findViewById(R.id.addContactRow).setVisibility(View.GONE);
//			}
//		} catch (LinphoneCoreException e) {
//			e.printStackTrace();
//		}
//		Toast.makeText(getActivity(), contact, Toast.LENGTH_LONG).show();
	}
	
	
	@SuppressLint("ResourceAsColor")
	private List<Keyvalue> buildList(String callTime, String callDate, String callCost, String callCountry){
		List<Keyvalue> list = new ArrayList<Keyvalue>();
//		list.add(new Keyvalue(getActivity().getString(R.string.date_label), callDate).setBackgroundColor(android.R.color.transparent).setKeybold(true));
//		list.add(new Keyvalue(getActivity().getString(R.string.time_label), callTime).setBackgroundColor(android.R.color.transparent).setKeybold(true));
//		list.add(new Keyvalue(getActivity().getString(R.string.cost_label), callCost).setBackgroundColor(android.R.color.transparent).setKeybold(true));
//		list.add(new Keyvalue(getActivity().getString(R.string.country_label), callCountry).setBackgroundColor(android.R.color.transparent).setKeybold(true));
		return list;
	}

	public void changeDisplayedHistory(String sipUri, String displayName, String pictureUri, String status, String callTime, String callDate, String callCost,String callCountry, String contact) {		
//		if (displayName == null && getResources().getBoolean(R.bool.only_display_username_if_unknown) && LinphoneUtils.isSipAddress(sipUri)) {
//			displayName = LinphoneUtils.getUsernameFromAddress(sipUri);
//		}

		this.sipUri = sipUri;
		this.displayName = displayName;
		this.pictureUri = pictureUri;
		this.contact = contact;
		displayHistory(status, callTime, callDate, callCost, callCountry, contact);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (LinphoneActivity.isInstanciated()) {
			LinphoneActivity.instance().selectMenu(FragmentsAvailable.HISTORY_DETAIL);
			
			if (getResources().getBoolean(R.bool.show_statusbar_only_on_dialer)) {
				LinphoneActivity.instance().hideStatusBar();
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.dialBack) {
//			LinphoneActivity.instance().setAddresGoToDialerAndCall(sipUri, displayName, pictureUri == null ? null : Uri.parse(pictureUri));
			LinphoneActivity.instance().setAddresGoToDialerAndCall(contact, displayName, null);
		} else if (id == R.id.chat) {
//			LinphoneActivity.instance().displayChat(sipUri);
			LinphoneActivity.instance().displayChatMessage(contact);
		} else if (id == R.id.addToContacts) {
			String uriToAdd = sipUri;
			if (getResources().getBoolean(R.bool.never_display_sip_addresses)) {
				uriToAdd = LinphoneUtils.getUsernameFromAddress(sipUri);
			}
			LinphoneActivity.instance().displayContactsForEdition(uriToAdd);
		}else if(id == R.id.backbtn){
			getFragmentManager().popBackStack();
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private String timestampToHumanDate(String timestamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(timestamp));
		
		SimpleDateFormat dateFormat;
		dateFormat = new SimpleDateFormat(getResources().getString(R.string.history_detail_date_format));
		return dateFormat.format(cal.getTime());
	}
}
