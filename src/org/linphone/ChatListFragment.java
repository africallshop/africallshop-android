package org.linphone;
/*
ChatListFragment.java
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.json.JSONArray;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.mediastream.Log;
import org.linphone.setup.SMS;
import org.linphone.setup.SMSListAdapter;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

/**
 * @author Sylvain Berfini
 */
public class ChatListFragment extends Fragment implements OnClickListener, OnItemClickListener {
	private Handler mHandler = new Handler();
	
	private LayoutInflater mInflater;
	private List<String> mConversations, mDrafts;
	private ListView chatList;
	private TextView edit, ok, newDiscussion, noChatHistory;
	private ImageView clearFastChat;
	private EditText fastNewChat;
	private boolean isEditMode = false;
	private boolean useLinphoneStorage;
	private ProgressDialog progress;
	SMSListAdapter smsAdapter;
	
	GetSMSTask getsmstask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;

		View view = inflater.inflate(R.layout.chatlist, container, false);
		chatList = (ListView) view.findViewById(R.id.chatList);
		chatList.setOnItemClickListener(this);
		registerForContextMenu(chatList);
		
		noChatHistory = (TextView) view.findViewById(R.id.noChatHistory);
		
		edit = (TextView) view.findViewById(R.id.edit);
		edit.setOnClickListener(this);
		
		newDiscussion = (TextView) view.findViewById(R.id.newDiscussion);
		newDiscussion.setOnClickListener(this);
		
		ok = (TextView) view.findViewById(R.id.ok);
		ok.setOnClickListener(this);
		
		clearFastChat = (ImageView) view.findViewById(R.id.clearFastChatField);
		clearFastChat.setOnClickListener(this);
		
		fastNewChat = (EditText) view.findViewById(R.id.newFastChat);
		
		instance = this;
		
		smsAdapter = new SMSListAdapter(getActivity().getApplicationContext(), LinphoneActivity.isInstanciated());
		chatList.setAdapter(smsAdapter);
		
		return view;
	}
	
	private void hideAndDisplayMessageIfNoChat() {
//		if (mConversations.size() == 0 && mDrafts.size() == 0) {
//			noChatHistory.setVisibility(View.VISIBLE);
//			chatList.setVisibility(View.GONE);
//		} else {
			noChatHistory.setVisibility(View.GONE);
			chatList.setVisibility(View.VISIBLE);
//			chatList.setAdapter(new ChatListAdapter(useLinphoneStorage));			
			if(getsmstask == null){
				getsmstask = new GetSMSTask(smsAdapter, getActivity());
				getsmstask.execute();
			}else{
				if(getsmstask.getStatus() == AsyncTask.Status.RUNNING){
					Toast.makeText(getActivity(), getActivity().getString(R.string.already_running), Toast.LENGTH_LONG).show();
				}else if(getsmstask.getStatus() == AsyncTask.Status.FINISHED){
					getsmstask = new GetSMSTask(smsAdapter, getActivity());
					getsmstask.execute();
				}
			}
//		}
	}
	
	public void refresh() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
//				mConversations = LinphoneActivity.instance().getChatList();
//				mDrafts = LinphoneActivity.instance().getDraftChatList();
//				mConversations.removeAll(mDrafts);
//				hideAndDisplayMessageIfNoChat();
			}
		});
		
		hideAndDisplayMessageIfNoChat();
	}
	
	private boolean isVersionUsingNewChatStorage() {
		try {
			Context context = LinphoneActivity.instance();
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode >= 2200;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void displayProgress(){
		progress = new ProgressDialog(LinphoneActivity.instance());
		progress.setTitle(getString(R.string.wait));
		progress.setMessage(getString(R.string.importing_messages));
		progress.setCancelable(false);
		progress.setIndeterminate(true);
		progress.show();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//Check if the is the first time we show the chat view since we use liblinphone chat storage
//		useLinphoneStorage = getResources().getBoolean(R.bool.use_linphone_chat_storage);
//		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LinphoneActivity.instance());
//		boolean updateNeeded = prefs.getBoolean(getString(R.string.pref_first_time_linphone_chat_storage), true);
//		updateNeeded = updateNeeded && !isVersionUsingNewChatStorage();
//		if (useLinphoneStorage && updateNeeded) {
//			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
//                private ProgressDialog pd;
//                @Override
//                protected void onPreExecute() {
//                         pd = new ProgressDialog(LinphoneActivity.instance());
//                         pd.setTitle(getString(R.string.wait));
//                         pd.setMessage(getString(R.string.importing_messages));
//                         pd.setCancelable(false);
//                         pd.setIndeterminate(true);
//                         pd.show();
//                }
//                @Override
//                protected Void doInBackground(Void... arg0) {
//                        try {
//                        	if (importAndroidStoredMessagedIntoLibLinphoneStorage()) {
//                				prefs.edit().putBoolean(getString(R.string.pref_first_time_linphone_chat_storage), false).commit();
//                				LinphoneActivity.instance().getChatStorage().restartChatStorage();
//                			}
//                        } catch (Exception e) {
//                               e.printStackTrace();
//                        }
//                        return null;
//                 }
//                 @Override
//                 protected void onPostExecute(Void result) {
//                         pd.dismiss();
//                 }
//			};
//        	task.execute((Void[])null);
//		}
		
		if (LinphoneActivity.isInstanciated()) {
			LinphoneActivity.instance().selectMenu(FragmentsAvailable.CHATLIST);
			LinphoneActivity.instance().updateChatListFragment(this);
			
			if (getResources().getBoolean(R.bool.show_statusbar_only_on_dialer)) {
				LinphoneActivity.instance().hideStatusBar();
			}
		}
		refresh();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, getString(R.string.delete));
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		if (info == null || info.targetView == null) {
			return false;
		}
//		String sipUri = (String) info.targetView.getTag();
//		
//		LinphoneActivity.instance().removeFromChatList(sipUri);
//		mConversations = LinphoneActivity.instance().getChatList();
//		mDrafts = LinphoneActivity.instance().getDraftChatList();
//		mConversations.removeAll(mDrafts);
//		hideAndDisplayMessageIfNoChat();
		return true;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.clearFastChatField) {
			fastNewChat.setText("");
		}
		else if (id == R.id.ok) {
//			edit.setVisibility(View.VISIBLE);
//			ok.setVisibility(View.GONE);
//			isEditMode = false;
//			hideAndDisplayMessageIfNoChat();
		}
		else if (id == R.id.edit) {
//			edit.setVisibility(View.GONE);
//			ok.setVisibility(View.VISIBLE);
//			isEditMode = true;
//			hideAndDisplayMessageIfNoChat();
		}
		else if (id == R.id.newDiscussion) {
			String sipUri = fastNewChat.getText().toString();
			if (sipUri.equals("")) {
				LinphoneActivity.instance().displayContacts(false);
			} else {
				
//				if (!LinphoneUtils.isSipAddress(sipUri)) {
//					if (LinphoneManager.getLc().getDefaultProxyConfig() == null) {
//						return;
//					}
//					sipUri = sipUri + "@" + LinphoneManager.getLc().getDefaultProxyConfig().getDomain();
//				}
//				if (!LinphoneUtils.isStrictSipAddress(sipUri)) {
//					sipUri = "sip:" + sipUri;
//				}
				
				LinphoneActivity.instance().displayChatMessage(smsAdapter.getSMS(sipUri), smsAdapter.getSMSlogs(sipUri));
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		String sipUri = (String) view.getTag();
		
//		if (LinphoneActivity.isInstanciated() && !isEditMode) {
//			LinphoneActivity.instance().displayChat(sipUri);
//		} else if (LinphoneActivity.isInstanciated()) {
//			LinphoneActivity.instance().removeFromChatList(sipUri);
//			LinphoneActivity.instance().removeFromDrafts(sipUri);
//			
//			mConversations = LinphoneActivity.instance().getChatList();
//			mDrafts = LinphoneActivity.instance().getDraftChatList();
//			mConversations.removeAll(mDrafts);
//			hideAndDisplayMessageIfNoChat();
//			
//			LinphoneActivity.instance().updateMissedChatCount();
//		}
	}
	
	private boolean importAndroidStoredMessagedIntoLibLinphoneStorage() {
		Log.w("Importing previous messages into new database...");
		try {
			ChatStorage db = LinphoneActivity.instance().getChatStorage();
			List<String> conversations = db.getChatList();
			for (int j = conversations.size() - 1; j >= 0; j--) {
				String correspondent = conversations.get(j);
				LinphoneChatRoom room = LinphoneManager.getLc().getOrCreateChatRoom(correspondent);
				for (ChatMessage message : db.getMessages(correspondent)) {
					LinphoneChatMessage msg = room.createLinphoneChatMessage(message.getMessage(), message.getUrl(), message.getStatus(), Long.parseLong(message.getTimestamp()), true, message.isIncoming());
					if (message.getImage() != null) {
						String path = saveImageAsFile(message.getId(), message.getImage());
						if (path != null)
							msg.setExternalBodyUrl(path);
					}
					msg.store();
				}
				db.removeDiscussion(correspondent);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private String saveImageAsFile(int id, Bitmap bm) {
		try {
			String path = Environment.getExternalStorageDirectory().toString();
			if (!path.endsWith("/"))
				path += "/";
			path += "Pictures/";
			File directory = new File(path);
			directory.mkdirs();
			
			String filename = getString(R.string.picture_name_format).replace("%s", String.valueOf(id));
			File file = new File(path, filename);
			
			OutputStream fOut = null;
			fOut = new FileOutputStream(file);

			bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
			
			return path + filename;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	class ChatListAdapter extends BaseAdapter {
		private boolean useNativeAPI;
		
		ChatListAdapter(boolean useNativeAPI) {
			this.useNativeAPI = useNativeAPI;
		}
		
		public int getCount() {
			return mConversations.size() + mDrafts.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			if (convertView != null) {
				view = convertView;
			} else {
				view = mInflater.inflate(R.layout.chatlist_cell, parent, false);
				
			}
			String contact;
			boolean isDraft = false;
			if (position >= mDrafts.size()) {
				contact = mConversations.get(position - mDrafts.size());
			} else {
				contact = mDrafts.get(position);
				isDraft = true;
			}
			view.setTag(contact);
			int unreadMessagesCount = LinphoneActivity.instance().getChatStorage().getUnreadMessageCount(contact);
			
			LinphoneAddress address;
			try {
				address = LinphoneCoreFactory.instance().createLinphoneAddress(contact);
			} catch (LinphoneCoreException e) {
				Log.e("Chat view cannot parse address",e);
				return view;
			}
			LinphoneUtils.findUriPictureOfContactAndSetDisplayName(address, view.getContext().getContentResolver());

			String message = "";
			if (useNativeAPI) {
				LinphoneChatRoom chatRoom = LinphoneManager.getLc().getOrCreateChatRoom(contact);
				LinphoneChatMessage[] history = chatRoom.getHistory(20);
				if (history != null && history.length > 0) {
					for (int i = history.length - 1; i >= 0; i--) {
						LinphoneChatMessage msg = history[i];
						if (msg.getText() != null && msg.getText().length() > 0) {
							message = msg.getText();
							break;
						}
					}
				}
			} else {
				List<ChatMessage> messages = LinphoneActivity.instance().getChatMessages(contact);
				if (messages != null && messages.size() > 0) {
					int iterator = messages.size() - 1;
					ChatMessage lastMessage = null;
					
					while (iterator >= 0) {
						lastMessage = messages.get(iterator);
						if (lastMessage.getMessage() == null) {
							iterator--;
						} else {
							iterator = -1;
						}
					}
					message = (lastMessage == null || lastMessage.getMessage() == null) ? "" : lastMessage.getMessage();
				}
			}
			TextView lastMessageView = (TextView) view.findViewById(R.id.lastMessage);
			lastMessageView.setText(message);
			
			TextView sipUri = (TextView) view.findViewById(R.id.sipUri);
			sipUri.setSelected(true); // For animation
			
			if (getResources().getBoolean(R.bool.only_display_username_if_unknown) && address.getDisplayName() != null && LinphoneUtils.isSipAddress(address.getDisplayName())) {
				address.setDisplayName(LinphoneUtils.getUsernameFromAddress(address.getDisplayName()));
			} else if (getResources().getBoolean(R.bool.only_display_username_if_unknown) && LinphoneUtils.isSipAddress(contact)) {
				contact = LinphoneUtils.getUsernameFromAddress(contact);
			} 
			
			sipUri.setText(address.getDisplayName() == null ? contact : address.getDisplayName());
			if (isDraft) {
				view.findViewById(R.id.draft).setVisibility(View.VISIBLE);
			}
			
			
			ImageView delete = (ImageView) view.findViewById(R.id.delete);
			TextView unreadMessages = (TextView) view.findViewById(R.id.unreadMessages);
			
			if (unreadMessagesCount > 0) {
				unreadMessages.setVisibility(View.VISIBLE);
				unreadMessages.setText(String.valueOf(unreadMessagesCount));
			} else {
				unreadMessages.setVisibility(View.GONE);
			}
			
			if (isEditMode) {
				delete.setVisibility(View.VISIBLE);
			} else {
				delete.setVisibility(View.INVISIBLE);
			}
			
			return view;
		}
	}
	
	
	/*************************************************************************
	 * 
	 * 
	 * 
	 * 
	 *************************************************************************/

	public class SmsViewholder{
		public TextView contact;
		public TextView message;
		public LinearLayout wrapper; 
		
		public SmsViewholder(View view){
			contact =  (TextView) view.findViewById(R.id.sipUri);
			message = (TextView) view.findViewById(R.id.lastMessage);
			wrapper = (LinearLayout) view.findViewById(R.id.wrapper);
		}
	}
	
	private static ChatListFragment instance;
	
	static final boolean isInstanciated() {
		return instance != null;
	}
	
	public static final ChatListFragment instance() {
		if (instance != null)
			return instance;
		throw new RuntimeException("LinphoneActivity not instantiated yet");
	}
	

	
		
	public class GetSMSTask extends AsyncTask<Void, Void, HashMap<String, List<SMS>>> {
		
		public static final String TAG = "GetSMSTask";
		private Intent broadcastIntent;
		private int CONNECTION_STATE = 0;
		private String login;
		private String password; 
		Context context;
		private int http_status_code = 200;
		SMSListAdapter mAdapter;
		private ProgressDialog progressBar;
		
		public GetSMSTask(SMSListAdapter adapter, Context c){
			mAdapter = adapter;
			this.context = c;
			this.progressBar = new ProgressDialog(context);
		}
		
		private HashMap<String, List<SMS>> loadInBackground(){
//			if(!NetworkUtils.IsOnline(mAdapter.getContext())){
//				CONNECTION_STATE = ConnectionState.NO_CONNECTION;
//			}else if(!NetworkUtils.PingUrl(Config.SERVER_DOMAINE)){
//				CONNECTION_STATE = ConnectionState.SERVER_NOT_AVAILABLE;
//			}else{
				login = Utils.getStringFromPreference(mAdapter.getContext(), Config.USER);
				password = Utils.getStringFromPreference(mAdapter.getContext(), Config.PWD);
				return mainProcess();
//			}
//			return null;
		}

		private HashMap<String, List<SMS>> mainProcess(){
//			this.progressBar.show();
			HashMap<String, List<SMS>> mHistories = new HashMap<String, List<SMS>>();
			RestClient client = new RestClient(Config.API_SMS_URL);
			client.addParam("login", login);
			client.addParam("password", password);
			try{
			    client.execute(RequestMethod.POST);
			    http_status_code = client.getResponseCode();
			    String response = client.getResponse();
//			    Log.e(response);
			    if(http_status_code == 200){
			    	if(!response.trim().equalsIgnoreCase("KO")){
			    		response = "["+response.replace("}{", "},{")+"]";
//			    		Log.e(response);
			    		JSONArray histories = new JSONArray(response);
			    		for(int i=0; i<histories.length(); i++){
			    			if(histories.getJSONObject(i) != null){
			    				SMS sms = new SMS(histories.getJSONObject(i));
			    				//mHistories.add();
			    				if(!mHistories.containsKey(sms.destination)){
			    					List<SMS> list = new ArrayList<SMS>();
			    					list.add(sms);
			    					mHistories.put(sms.destination, list);
			    				}else
			    					mHistories.get(sms.destination).add(sms);
			    			}
			    		} 
			    	}else{
			    		Toast.makeText(context, "Contact:"+context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
			    	}
			    }
			}catch(ConnectionPoolTimeoutException e){
				CONNECTION_STATE = ConnectionState.TIMEOUT;
			}catch(ConnectTimeoutException e){	
				CONNECTION_STATE = ConnectionState.TIMEOUT;
			}catch(SocketTimeoutException e){
				CONNECTION_STATE = ConnectionState.TIMEOUT;
			}catch(Exception e){
				CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
//				Log.e("HTTP Error "+e.toString());
			}
			return mHistories;  
		}
		
		@Override
		protected HashMap<String, List<SMS>> doInBackground(Void... params) {
			return loadInBackground();
//			return new ArrayList<JSONObject>();
		}
		
	    protected void onPostExecute(HashMap<String, List<SMS>> entries) {
	    	if(entries != null){
	    		mAdapter.upDateEntries(entries);
	    	}
	    	this.progressBar.dismiss();
	    	
	     }
	    
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressBar.setMessage(context.getString(R.string.task_status_processing));
			this.progressBar.setCanceledOnTouchOutside(false);
			this.progressBar.show();
			
		}
		
	    @Override
	    protected void onCancelled() {
	        running = false;
	    }
	    private boolean running = true;
	}
}


