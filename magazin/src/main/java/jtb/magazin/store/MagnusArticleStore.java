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
import jtb.magazin.model.ArticleModel;

// tu sa vytvara store pre jeden casopis
public class MagnusArticleStore {
	
	// rozlisujeme vsetky clanky, hlavne clanky v gride, a clanky ktore sa zobrazuju v bocnom menu
	private ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
	private ArrayList<ArticleModel> linksItems = new ArrayList<ArticleModel>();
	private ArrayList<ArticleModel> itemsAll = new ArrayList<ArticleModel>();

	String deviceHash;
	ArrayList<String> magnusArticleIds = new ArrayList<String>();
	@SuppressLint("SimpleDateFormat")
	public MagnusArticleStore(Context context,String magazinId){
		
		this.deviceHash=((MainActivity)context).authHash;
	
		//***********************************************************spracovanie JSONU*******************************************************************
        String response="";
			
			String url = "http://194.50.215.137/api/archive/get_item/"+magazinId;
			String forHash = Functions.sha1Hash(url+deviceHash);
			File cacheFile = new File(((MainActivity)context).getExternalCacheDir ().toString() +"/cache/magnus_articles_"+forHash+".txt");
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
	
			if (json!=null) {
			JSONObject jsonResult = json.getJSONObject("result");
			//System.out.println(jsonResult);
			JSONArray jsonArticles = jsonResult.getJSONArray("articles");
			
			//System.out.println(jsonArticles);
			
			for (int i = 0; i < jsonArticles.length(); i++) {
				JSONObject c = jsonArticles.getJSONObject(i);

				String id = c.getString("id");
				String articleID = c.getString("articleID");
				String lastChange = c.getString("lastChange");
                String publishDate = c.getString("publishDate");
				String title = c.getString("title");
				String locked = c.getString("locked");
				String perex = c.getString("perex");
				String file = c.getString("file");
				String zip = c.getString("zip");
				String image = c.getString("image");
			//	System.out.println(locked);
							
				ArticleModel model = new ArticleModel();
				model.setId(id);
				model.setMagazinID(magazinId);
				model.setMagnusArticleId(articleID);
				model.setLastChange(lastChange);
                model.setDatePublish(publishDate);
				model.setPerex(perex);
				model.setTitle(title);
				model.setFile(file);
				model.setZipUrl(zip);
				model.setImageUrl(image);
				model.setSource("3");
				model.setPos(i);
				model.setLocked(locked);
				
				items.add(model);	
				itemsAll.add(model);
				magnusArticleIds.add(articleID);

			}

			JSONArray jsonLinks = jsonResult.getJSONArray("links");
			
			for (int i = 0; i < jsonLinks.length(); i++) {
				JSONObject c = jsonLinks.getJSONObject(i);

				String id = c.getString("id");
				String articleID = c.getString("articleID");
				String title = c.getString("title");
				String perex = c.getString("perex");
				String file = c.getString("file");
				String zip = c.getString("zip");
				String lastChange = c.getString("lastChange");
			//	String image = c.getString("image");
		
				ArticleModel model = new ArticleModel();
				model.setId(id);
				model.setMagazinID(magazinId);
				model.setMagnusArticleId(articleID);
				model.setPerex(perex);
				model.setTitle(title);
				model.setFile(file);
				model.setZipUrl(zip);
				model.setSource("3");
				model.setLastChange(lastChange);
				model.setPos(i+jsonArticles.length());
				model.setLocked("0");
			//	model.setImageUrl(image);
			
				linksItems.add(model);	
				itemsAll.add(model);
				magnusArticleIds.add(articleID);
				//ids.add(id);
			}
			((MainActivity)context).currentItems=itemsAll;
			((MainActivity)context).gridCurrentItems=itemsAll;
			}
			} catch (Exception e) {
				//error.setText(R.string.connection);
				System.out.println("chyba z magnusArticleStore - Links: "+e);
				((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error6));
			}
   //***********************************************************koniec spracovania JSONU*******************************************************************
		
			
			((MainActivity)context).magnusArticleIds=magnusArticleIds;
			((MainActivity)context).magnusArticleItems=itemsAll;
		
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
	
	public ArrayList<ArticleModel> getLinksItems() {
		return linksItems;
	}
	
}
