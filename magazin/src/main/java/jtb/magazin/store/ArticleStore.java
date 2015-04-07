package jtb.magazin.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jtb.magazin.Constants;
import jtb.magazin.CustomHttpClient;
import jtb.magazin.MainActivity;
import jtb.magazin.model.ArticleModel;

//zakladny store ktory zhromazduje modely daneho typu, typ1 - jet, typ2 - informacny servis
public class ArticleStore  {
	
	private ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
	private ArrayList<String> ids = new ArrayList<String>(); 
	private HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
	private int nextIndex=0;
	public boolean error = false;
    public boolean getBasicError = false;
	String deviceHash;
	@SuppressLint("SimpleDateFormat")
	public ArticleStore(Context context, String type){
	
		//System.out.println(context.getSharedPreferences("string", Context.MODE_PRIVATE).getString("string", ""));
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			//	System.out.println(prefs.getString("myKey"," oops no value found"));
				deviceHash=((MainActivity)(context)).authHash;
				//System.out.print(deviceHash);
		
		//***********************************************************spracovanie JSONU*******************************************************************

			
   	 try {
   		//kontaktuje sa server pre ziskanie zoznamu id-ciek clankov, ich source, stav locked a last change
         String response;
         if (type.equals("klub")){
             response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/club/get/"+((MainActivity)(context)).lang,deviceHash);
         } else {
             response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/news/get/"+type+"/"+((MainActivity)(context)).lang,deviceHash);
         }
   		
   		JSONObject json = new JSONObject(response);
   		JSONArray jsonIds = json.getJSONArray("result");

   				
   		for (int i = 0; i < jsonIds.length(); i++) {

   			JSONObject c = jsonIds.getJSONObject(i);
   			String id = c.getString("id");
            if (i==0)System.out.println(id);
   			String locked = c.getString("locked");
   			String source = c.getString("source");
   			String lastChange = c.getString("lastChange");
   			//System.out.println(id);
   			ArticleModel model = new ArticleModel();
   			model.setLocked(locked);
   			model.setSource(source);
   			model.setId(id);
   			model.setLastChange(lastChange);
   			model.setPos(i);
   			items.add(model);
   			//tu je dolezite ulozit si poradie source-je zdroj clanku. jet a servis predstavuju zdroj 1 a magnus zdroj 3
   			hashmap.put(source+"-"+id, i);
   			ids.add(source+"-"+id);
   		}
   		

   	} catch (Exception e) {
   		//error.setText(R.string.connection);
   		System.out.println("from ArticleStore"+e);
   		error=true;
   	}
   	//zatial sa nahrali iba zakladne informacie a to id,source,locked a lastChange. Od servera potrebujeme dalsie veci ako nazov clanku, perex, 
   	 //url obrazku To pri velkom pocte clankov zahltilo server a preto ich budeme nacitavat postupne
   	int count =Constants.LOADED_ARTICLES;
   	if(((MainActivity)(context)).fromResume){
		count=((MainActivity)(context)).actualPos*8+16;	
		if (count<Constants.LOADED_ARTICLES) count = Constants.LOADED_ARTICLES;
	}
   	// V getBasic naplname uz vytvoreny model dalsimi datami ako vyssie spominany nazov, perex, url obrazka
   	 GetBasic.getBasic(context, nextIndex, count, items, ids, hashmap,this,type);
   	 //premenna nextIndex nam signalizuje pre dany store nasledujuci index modelu clanku ktory este nie je kompletne naplneny datami
   	nextIndex=Constants.LOADED_ARTICLES;
	if(((MainActivity)(context)).fromResume){
		nextIndex=count;
	}
	
//	System.out.println(nextIndex);
   	 
   	 
   //***********************************************************koniec spracovania JSONU*******************************************************************
		
	}
	
	public int getCount(){
		
		return items.size();
		
	}
	
	public ArticleModel getItem(int index){
		
		return items.get(index);
	}

	public ArrayList<ArticleModel> getItems() {
		return items;
	}

	public int getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(int lastIndex) {
		this.nextIndex = lastIndex;
	}
	
	public ArrayList<String> getIds() {
		return ids;
	}

	public void setIds(ArrayList<String> ids) {
		this.ids = ids;
	}

	public HashMap<String, Integer> getHashmap() {
		return hashmap;
	}

	public void setHashmap(HashMap<String, Integer> hashmap) {
		this.hashmap = hashmap;
	}
	
}
