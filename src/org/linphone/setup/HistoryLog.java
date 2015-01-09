package org.linphone.setup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;
import org.linphone.LinphoneUtils;
import org.linphone.mediastream.Log;

import android.content.Context;


public class HistoryLog{
	public String id;
	public String date_appel;
	public String duree_appel;
	public String numero_appele;
	public String prix_appele;
	public String pays_appele;
	public String nom_contact;
	
	public HistoryLog(JSONObject json){
		try{
			id = json.getString("id");
			date_appel = json.getString("date_appel");
			duree_appel = json.getString("duree_appel");
			numero_appele = json.getString("numero_appele");
			prix_appele = json.getString("prix_appele");
			pays_appele = json.getString("pays_appele");
			nom_contact = json.getString("nom_contact");
		}catch(Exception e){
			
		}
	}
	
	public Calendar getdate(){
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fmt.setCalendar(cal);
	    try {
			cal.setTime(fmt.parse(date_appel));
		} catch (ParseException e) {
			Log.e("PARSE", " "+e.toString());
		}
	    return cal;
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
	
	public String getNom(Context c){
		return LinphoneUtils.getContactName(c, numero_appele);
	}
}

