package org.linphone.setup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.mediastream.Log;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class SMS implements Parcelable{
	public String id;
	public String destination;
	public String contenu;
	public String prix;
	public String date_envoi;
	public String status="1";

	public SMS(){
	}
	
	public SMS(JSONObject json){
//		this();
		try{
			id = json.getString("id");
			destination = json.getString("destination");
			contenu = json.getString("contenu");
			prix = json.getString("prix");
			date_envoi = json.getString("date");
			status = json.getString("status");
		}catch(Exception e){
			Log.e(e.getMessage());
		}
	}
	
	public SMS(String numero, String text, String latestid){
//		this();
		id = ""+ (Integer.parseInt(latestid)+1);
		destination = numero;
		contenu = text;
		date_envoi = getCurrentDate();
	}
	
	public String getCurrentDate(){
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(cal.getTime()).toString();
//		Time t = new Time(Time.getCurrentTimezone());
//		t.setToNow();
//		return t.format("yyyy-MM-dd HH:mm:ss").toString();
	}
	
	public SMS(String dest){
		destination = dest;
	}
    
	public SMS(Parcel in ){
		id = in.readString();
		destination = in.readString();
		contenu = in.readString();
		prix = in.readString();
		date_envoi = in.readString();
		status = in.readString();
		
	}
	
	public int getId(){
		return Integer.parseInt(id);
	}
	
	public String getShortMessage(){
		String m = contenu;
		if(contenu.length() >15){
			m = contenu.substring(0, 14)+" .... ";
		}
		return m;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public LinphoneChatMessage.State isdelivered(){
		if(status.equals("1"))
			return LinphoneChatMessage.State.Delivered;
		return LinphoneChatMessage.State.NotDelivered;
	}
	
	public Calendar getdate(){
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fmt.setCalendar(cal);
	    try {
			cal.setTime(fmt.parse(date_envoi));
		} catch (ParseException e) {
			Log.e("PARSE", date_envoi+" "+e.toString());
		}
	    return cal;
	}
	
	public long getTime(){
		return getdate().getTimeInMillis();
	}
	
	public String historydate(){
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = getdate();
		fmt.setCalendar(cal);
		return fmt.format(getdate().getTime());
	}
	
	public String historydetaildate(){
		SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
		Calendar cal = getdate();
		fmt.setCalendar(cal);
		return fmt.format(getdate().getTime());
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(destination);
		dest.writeString(contenu);
		dest.writeString(prix);
		dest.writeString(date_envoi);
		dest.writeString(status);
	}


    public static final Parcelable.Creator<SMS> CREATOR
            = new Parcelable.Creator<SMS>() {
        public SMS createFromParcel(Parcel in) {
            return new SMS(in);
        }

        public SMS[] newArray(int size) {
            return new SMS[size];
        }
    };

}