package jtb.magazin.store;

import android.content.Context;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jtb.magazin.CustomHttpClient;
import jtb.magazin.MainActivity;
import jtb.magazin.model.ArticleModel;

public final class GetBasic {

	public static void getBasic(Context context, int from, int count, ArrayList<ArticleModel> items, 
			ArrayList<String> ids,HashMap<String, Integer> hashmap, ArticleStore store, String type) {
			//Gson je externa kniznica ktora pomaha spravit z objektu retazec a naopak
			Gson gson = new Gson();
		
		try {
			//naplnime store so zakladnymi datami

			String url = "http://194.50.215.137/api/common/get_basic/"+((MainActivity)(context)).lang;

			
			JSONObject json = null;
			//ak nemame pripojenie k internetu snazime sa dostat z pamati clanky ktore uz skor sa nacitali a ulozili do suboru
				if (!((MainActivity)(context)).isNetworkAvailable()){
					for (int i=from; i<from+count; i++){
			   			//System.out.println(i);
			   			if (i>=ids.size()) break;
			   			File cacheFile = new File(((MainActivity)context).getExternalCacheDir ().toString() +"/cache/articles/"+ids.get(i)+
			   		"-"+((MainActivity)(context)).lang+"-"+items.get(i).getLastChange()+".txt");
			   			//System.out.println(cacheFile.toString());
			   			if (cacheFile.isFile()){
						String fromFile = ((MainActivity)(context)).readFromFile(cacheFile);							
						ArticleModel item = gson.fromJson(fromFile, ArticleModel.class);
						items.get(i).setTitle(item.getTitle());
				   		items.get(i).setDatePublish(item.getDatePublish());
				   		items.get(i).setPerex(item.getPerex());
				   		items.get(i).setImageUrl(item.getImageUrl());
				   		//System.out.println(item.getImageUrl());
			   			}
					//} else ((MainActivity)context).errorMessage("Internet", "Nemate pripojenie k internetu");
			   		}
					
					
				} else {
				
				//na server posielame zoznam id-ciek z ktorych potrebujeme dostat zakladne data
					if (ids.size()>0){
				   		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>(); 
				   		for (int i=from; i<from+count; i++){
				   			
				   			if (i==ids.size()) break;
				   		//	System.out.println(ids.get(i));
				   			postParameters.add(new BasicNameValuePair("id["+i+"]",ids.get(i)));  
				   		}
                        System.out.println("chcem dostat getBasic");
                        String response= CustomHttpClient.executeHttpPost(url,((MainActivity)(context)).authHash, postParameters);
                        System.out.println("dostal som getBasic");
				   		JSONObject json2 = new JSONObject(response);
                        /*if (type.equals("klub")){
                            System.out.println(json2);
                        }*/

				   		JSONArray jsonBasic = json2.getJSONArray("result");
				   		//System.out.println(jsonBasic);
				   		
					   		for (int i = 0; i < jsonBasic.length(); i++) {
					   
					   		JSONObject jsonArticle = jsonBasic.getJSONObject(i);
					   		
					   		String title=jsonArticle.getString("title");
					   		String perex=jsonArticle.getString("perex");
					   		String thumbURL=jsonArticle.getString("thumbURL");
					   	//	System.out.println(thumbURL);
					   		
					   		//String categoryID=jsonArticle.getString("categoryID");
                            String publishDate="";
                            String price="";
                            if (type.equals("klub")){
                                price=jsonArticle.getString("price");
                            } else {
                                publishDate=jsonArticle.getString("publishDate");
                            }

					   		//potrebujeme vediet pre dane source-id ktore je v poradi v items. To nam umoznuje hashMap	
					   		int j = hashmap.get(jsonArticle.getString("source")+"-"+jsonArticle.getString("id"));
					   		//System.out.println(thumbURL);
					   	//	System.out.println(items.get(j).getLastChange());
					   		items.get(j).setTitle(title);
                            if (type.equals("klub")){
                                items.get(j).setPrice(price);
                            } else {
                                items.get(j).setDatePublish(publishDate);
                            }

					   		items.get(j).setPerex(perex);
					   		items.get(j).setImageUrl(thumbURL);
					   	//	items.get(j).setCategoryID(categoryID);
					   		
					   		//ak sme v magnuse tak navyse ziskavame aj articleID a magazin ID
					   		if (items.get(j).getSource().equals("3")){
					   			//System.out.println(jsonArticle);
					   			String articleID = jsonArticle.getString("articleID");
					   			String magazinId = jsonArticle.getString("magazineID");
								items.get(j).setMagazinID(magazinId);
								items.get(j).setMagnusArticleId(articleID);		
								items.get(j).setZipUrl("");			
					   		}
						
					   		String jsonItem = gson.toJson(items.get(j));
					   		//ak sme ziskali data pre dany clanok ulozime si do suboru pre pripad vypadnutia internetu
							File cacheFile = new File(((MainActivity)context).getExternalCacheDir ().toString() +"/cache/articles/"+items.get(j).getSource()+
							"-"+items.get(j).getId()+"-"+((MainActivity)(context)).lang+"-"+items.get(j).getLastChange()+".txt");
							((MainActivity)(context)).writeToFile(cacheFile,jsonItem);
					   		}//end for
				   		}//end ids.size()>0
                    store.getBasicError=false;
					}

			   	} catch (Exception e) {
			   		//error.setText(R.string.connection);
			   		System.out.println("from GetBasic"+e);
                    store.getBasicError=true;
			   	}
			}
		}