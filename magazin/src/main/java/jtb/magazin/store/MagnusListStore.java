package jtb.magazin.store;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import jtb.magazin.CustomHttpClient;
import jtb.magazin.Functions;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.MagnusListModel;

//ziskava sa zoznam vsetkych magazinov a zakladnych udajov o nich
public class MagnusListStore  {
	
	private ArrayList<MagnusListModel> items = new ArrayList<MagnusListModel>();
	String deviceHash;
    public boolean error = false;
	@SuppressLint("SimpleDateFormat")
	public MagnusListStore(Context context){
		
	deviceHash=((MainActivity)(context)).authHash;

//***********************************************************spracovanie JSONU*******************************************************************
        String response="";
	ArrayList<String> ids = new ArrayList<String>(); 

	String url = "http://194.50.215.137/api/archive/get/"+((MainActivity)(context)).lang;
	System.out.println(((MainActivity)(context)).lang);
	String forHash = Functions.sha1Hash(url+deviceHash);
	File cacheFile =new File(((MainActivity)context).getExternalCacheDir ().toString() +"/cache/magnus_list_"+forHash+".txt");
	JSONObject json = null;
	try {
		if (!((MainActivity)(context)).isNetworkAvailable()){
			if (cacheFile.isFile()){
				String fromFile = ((MainActivity)(context)).readFromFile(cacheFile);				
				json= new JSONObject(fromFile);
			} else ((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error6));
		} else {
			response = CustomHttpClient.executeHttpGet(url,deviceHash);
			json = new JSONObject(response);
	   	//	System.out.println(jsonAuth);
	   		String jsonString = json.toString();
	   	//	System.out.println(jsonString);
	   		((MainActivity)(context)).writeToFile(cacheFile,jsonString);
		}
	
	if (json!=null){
	JSONArray jsonIds = json.getJSONArray("result");
	
	for (int i = 0; i < jsonIds.length(); i++) {
		JSONObject c = jsonIds.getJSONObject(i);
		//System.out.println(c);
		String id = c.getString("id");
		String title = c.getString("title");
		String subtitle = c.getString("subtitle");
		String perex = c.getString("perex");
		String cover1 = c.getString("cover1");
		String cover2 = c.getString("cover2");
		String lastChange = c.getString("lastChange");
		
		MagnusListModel model = new MagnusListModel();
		model.setId(id);
	model.setTitle(title);
	model.setSubTitle(subtitle);
	model.setPerex(perex);
	model.setLastChange(lastChange);
	model.setCover1(cover1);
	model.setCover2(cover2);	
	items.add(model);
        //ids.add(id);
	}
		
	}
} catch (Exception e) {
	//error.setText(R.string.connection);
	System.out.println("z MagnusListStore"+e);
        error = true;
	//((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error6));
}
   	 
   //***********************************************************koniec spracovania JSONU*******************************************************************
		
	}
	
	public int getCount(){
		
		return items.size();
		
	}
	
	public MagnusListModel getItem(int index){
		
		return items.get(index);
	}

	public ArrayList<MagnusListModel> getItems() {
		return items;
	}
	
	
}
