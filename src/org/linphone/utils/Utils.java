package org.linphone.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.linphone.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	
	// fonction qui permet de retourner le chemin du fichier
	// audion courant depuis les préférences
	public static String getStringFromPreference(Context context, String name){		

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString( name , null);
		return value;
	}

	// fonction qui permet de mettre un type string dans les préférences partagées
	// audion courant depuis les préférences
	public  static void putStringToPreference(Context context, String key, String value){		

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void showToast(Context context, String text){
		//Retrieve the layout inflator
		LayoutInflater inflater = LayoutInflater.from(context);
		//Parameter 2 - Custom layout ID present in linearlayout tag of XML
		View layout = inflater.inflate(R.layout.toast_custom,null);
		((TextView) layout.findViewById(R.id.toasttext)).setText(Html.fromHtml(text));
		//Return the application context
		Toast toast = new Toast(context);
		//Set toast duration
		toast.setDuration(Toast.LENGTH_LONG);
		//Set the custom layout to Toast
		toast.setView(layout);
		//Display toast
		toast.show();
	}
	
	
	public static String getSystemName(){
		StringBuilder builder = new StringBuilder();
		builder.append(Build.VERSION.RELEASE);

		Field[] fields = Build.VERSION_CODES.class.getFields();
		for (Field field : fields) {
		    String fieldName = field.getName();
		    int fieldValue = -1;

		    try {
		        fieldValue = field.getInt(new Object());
		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    } catch (NullPointerException e) {
		        e.printStackTrace();
		    }

		    if (fieldValue == Build.VERSION.SDK_INT) {
		        builder.append("/ ").append(fieldName);
		        builder.append("/ Api ").append(fieldValue);
		    }
		}
		return builder.toString();
	}
	
	public static String getMyPhoneNumber(Context context){
    	TelephonyManager mTelephonyMgr;
    	mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
    	return mTelephonyMgr.getLine1Number();
    }
	
	public static String getAppName(Context context){
		String appname = "...";
		try {
		     appname = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
		} catch (Exception e) {
		}
		return appname;
	} 
	
	public static String getAppVersion(Context context){
		String appversion = "...";
		try {
			appversion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + "/" + 
					context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode ;
		} catch (Exception e) {
		}
		return appversion;
	} 
	
//	public static Drawable getImage(String name, Context context) {
//	    return context.getResources().getDrawable(
//	    		context.getResources().getIdentifier(name, "drawable",
//	    				context.getPackageName()));
//	}
	
	
	public static void sHowDialog(Context context, String title, String message){
		new AlertDialog.Builder(context)
		.setTitle(title)
        .setMessage(message)
        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();						
			}
		})
        .show();
	}
	
	public static final Map<String, Integer> inconMAp = new HashMap<String, Integer>();
	static {
		 map("qa.png", R.drawable.qa);
		 map("ci.png", R.drawable.ci);
		 map("iq.png", R.drawable.iq);
		 map("bw.png", R.drawable.bw);
		 map("ao.png", R.drawable.ao);
		 map("uy.png", R.drawable.uy);
		 map("td.png", R.drawable.td);
		 map("ge.png", R.drawable.ge);
		 map("nz.png", R.drawable.nz);
		 map("jm.png", R.drawable.jm);
		 map("bd.png", R.drawable.bd);
		 map("bf.png", R.drawable.bf);
		 map("nl.png", R.drawable.nl);
		 map("kg.png", R.drawable.kg);
		 map("ae.png", R.drawable.ae);
		 map("sm.png", R.drawable.sm);
		 map("to.png", R.drawable.to);
		 map("fr.png", R.drawable.fr);
		 map("th.png", R.drawable.th);
		 map("lt.png", R.drawable.lt);
		 map("br.png", R.drawable.br);
		 map("id.png", R.drawable.id);
		 map("us.png", R.drawable.us);
		 map("gy.png", R.drawable.gy);
		 map("gh.png", R.drawable.gh);
		 map("az.png", R.drawable.az);
		 map("pg.png", R.drawable.pg);
		 map("tz.png", R.drawable.tz);
		 map("mc.png", R.drawable.mc);
		 map("so.png", R.drawable.so);
		 map("cd.png", R.drawable.cd);
		 map("la.png", R.drawable.la);
		 map("jp.png", R.drawable.jp);
		 map("pe.png", R.drawable.pe);
		 map("gn.png", R.drawable.gn);
		 map("bi.png", R.drawable.bi);
		 map("er.png", R.drawable.er);
		 map("vc.png", R.drawable.vc);
		 map("mt.png", R.drawable.mt);
		 map("pl.png", R.drawable.pl);
		 map("zm.png", R.drawable.zm);
		 map("ma.png", R.drawable.ma);
		 map("ht.png", R.drawable.ht);
		 map("tr.png", R.drawable.tr);
		 map("lc.png", R.drawable.lc);
		 map("bo.png", R.drawable.bo);
		 map("cr.png", R.drawable.cr);
		 map("tm.png", R.drawable.tm);
		 map("gb.png", R.drawable.gb);
		 map("cy.png", R.drawable.cy);
		 map("mk.png", R.drawable.mk);
		 map("kp.png", R.drawable.kp);
		 map("ml.png", R.drawable.ml);
		 map("np.png", R.drawable.np);
		 map("ks.png", R.drawable.ks);
		 map("cz.png", R.drawable.cz);
		 map("il.png", R.drawable.il);
		 map("es.png", R.drawable.es);
		 map("sg.png", R.drawable.sg);
		 map("am.png", R.drawable.am);
		 map("be.png", R.drawable.be);
		 map("mn.png", R.drawable.mn);
		 map("km.png", R.drawable.km);
		 map("hn.png", R.drawable.hn);
		 map("rw.png", R.drawable.rw);
		 map("tv.png", R.drawable.tv);
		 map("si.png", R.drawable.si);
		 map("bn.png", R.drawable.bn);
		 map("gw.png", R.drawable.gw);
		 map("ar.png", R.drawable.ar);
		 map("dm.png", R.drawable.dm);
		 map("gr.png", R.drawable.gr);
		 map("tl.png", R.drawable.tl);
		 map("mr.png", R.drawable.mr);
		 map("va.png", R.drawable.va);
		 map("cg.png", R.drawable.cg);
		 map("kw.png", R.drawable.kw);
		 map("md.png", R.drawable.md);
		 map("hr.png", R.drawable.hr);
		 map("is.png", R.drawable.is);
		 map("pa.png", R.drawable.pa);
		 map("sl.png", R.drawable.sl);
		 map("tn.png", R.drawable.tn);
		 map("lu.png", R.drawable.lu);
		 map("pk.png", R.drawable.pk);
		 map("mw.png", R.drawable.mw);
		 map("dz.png", R.drawable.dz);
		 map("ly.png", R.drawable.ly);
		 map("mz.png", R.drawable.mz);
		 map("ir.png", R.drawable.ir);
		 map("ph.png", R.drawable.ph);
		 map("ga.png", R.drawable.ga);
		 map("bh.png", R.drawable.bh);
		 map("gt.png", R.drawable.gt);
		 map("it.png", R.drawable.it);
		 map("et.png", R.drawable.et);
		 map("co.png", R.drawable.co);
		 map("cu.png", R.drawable.cu);
		 map("mu.png", R.drawable.mu);
		 map("mh.png", R.drawable.mh);
		 map("cv.png", R.drawable.cv);
		 map("fi.png", R.drawable.fi);
		 map("tw.png", R.drawable.tw);
		 map("lb.png", R.drawable.lb);
		 map("fj.png", R.drawable.fj);
		 map("nr.png", R.drawable.nr);
		 map("se.png", R.drawable.se);
		 map("mg.png", R.drawable.mg);
		 map("ki.png", R.drawable.ki);
		 map("dj.png", R.drawable.dj);
		 map("dk.png", R.drawable.dk);
		 map("ee.png", R.drawable.ee);
		 map("li.png", R.drawable.li);
		 map("pt.png", R.drawable.pt);
		 map("kr.png", R.drawable.kr);
		 map("om.png", R.drawable.om);
		 map("au.png", R.drawable.au);
		 map("st.png", R.drawable.st);
		 map("za.png", R.drawable.za);
		 map("bg.png", R.drawable.bg);
		 map("ls.png", R.drawable.ls);
		 map("in.png", R.drawable.in);
		 map("ke.png", R.drawable.ke);
		 map("mv.png", R.drawable.mv);
		 map("sa.png", R.drawable.sa);
		 map("sr.png", R.drawable.sr);
		 map("gq.png", R.drawable.gq);
		 map("ag.png", R.drawable.ag);
		 map("ye.png", R.drawable.ye);
		 map("my.png", R.drawable.my);
		 map("sc.png", R.drawable.sc);
		 map("ch.png", R.drawable.ch);
		 map("fm.png", R.drawable.fm);
		 map("ec.png", R.drawable.ec);
		 map("sy.png", R.drawable.sy);
		 map("no.png", R.drawable.no);
		 map("kh.png", R.drawable.kh);
		 map("sz.png", R.drawable.sz);
		 map("bs.png", R.drawable.bs);
		 map("mm.png", R.drawable.mm);
		 map("na.png", R.drawable.na);
		 map("rs.png", R.drawable.rs);
		 map("tg.png", R.drawable.tg);
		 map("cm.png", R.drawable.cm);
		 map("ie.png", R.drawable.ie);
		 map("kz.png", R.drawable.kz);
		 map("by.png", R.drawable.by);
		 map("sv.png", R.drawable.sv);
		 map("ws.png", R.drawable.ws);
		 map("ne.png", R.drawable.ne);
		 map("jo.png", R.drawable.jo);
		 map("zw.png", R.drawable.zw);
		 map("uz.png", R.drawable.uz);
		 map("vu.png", R.drawable.vu);
		 map("ni.png", R.drawable.ni);
		 map("sn.png", R.drawable.sn);
		 map("at.png", R.drawable.at);
		 map("bj.png", R.drawable.bj);
		 map("lk.png", R.drawable.lk);
		 map("af.png", R.drawable.af);
		 map("ba.png", R.drawable.ba);
		 map("pw.png", R.drawable.pw);
		 map("bt.png", R.drawable.bt);
		 map("cl.png", R.drawable.cl);
		 map("cf.png", R.drawable.cf);
		 map("ug.png", R.drawable.ug);
		 map("ng.png", R.drawable.ng);
		 map("al.png", R.drawable.al);
		 map("kn.png", R.drawable.kn);
		 map("ve.png", R.drawable.ve);
		 map("sb.png", R.drawable.sb);
		 map("sk.png", R.drawable.sk);
		 map("hu.png", R.drawable.hu);
		 map("ua.png", R.drawable.ua);
		 map("tt.png", R.drawable.tt);
		 map("tj.png", R.drawable.tj);
		 map("cn.png", R.drawable.cn);
		 map("py.png", R.drawable.py);
		 map("vn.png", R.drawable.vn);
		 map("gm.png", R.drawable.gm);
		 map("eg.png", R.drawable.eg);
		 map("gd.png", R.drawable.gd);
		 map("bb.png", R.drawable.bb);
		 map("lv.png", R.drawable.lv);
		 map("me.png", R.drawable.me);
		 map("eh.png", R.drawable.eh);
		 map("ro.png", R.drawable.ro);
		 map("do.png", R.drawable.do_);
		 map("sd.png", R.drawable.sd);
		 map("mx.png", R.drawable.mx);
		 map("ad.png", R.drawable.ad);
		 map("ca.png", R.drawable.ca);
		 map("lr.png", R.drawable.lr);
		 map("bz.png", R.drawable.bz);
		 map("ru.png", R.drawable.ru);
		 map("de.png", R.drawable.de);
	}
	
	public static void map(String k, Integer v) {
		inconMAp.put(k, v);
	}
}
