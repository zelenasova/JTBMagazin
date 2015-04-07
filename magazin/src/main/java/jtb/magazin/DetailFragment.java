package jtb.magazin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jtb.magazin.UI.CustomWebView;
import jtb.magazin.UI.RightMenuFrame;
import jtb.magazin.asyncTasks.LoadArticle;
import jtb.magazin.model.ArticleModel;

//trieda ktora zobrazuje detail clanku
@SuppressLint({ "ValidFragment", "SetJavaScriptEnabled", "NewApi" })
public class DetailFragment extends Fragment {	
	
	boolean loadingFinished = true;
    boolean redirect = false;
    int i;
    String id;
    String[] related;
    ArrayList<ArticleModel> related_items;
    SharedPreferences prefs;
    Context context;
    int arrow_koef=20;
    String source;
    String zipUrl;
    ArticleModel model;
    CustomWebView myWebView;
    String deviceHash;
    LinearLayout spinner;
    String zipFile="";
    String newDir = "";
    boolean isDownloaded=false;
    boolean isUnReached=false;
    ProgressDialog pDialog;
    RelativeLayout rel;
    RelativeLayout pozadie;
    FrameLayout overlay;
 
    public DetailFragment(Context context,ArticleModel model) { 
		this.id=model.getId();
		this.model=model;
		this.context=context;
		this.source=model.getSource();
		prefs = ((MainActivity)context).prefs;
	}
    public DetailFragment() { 
    }
    //konstruktoru sa posuva model ktory ma zobrazit
	public DetailFragment(Context context,ArticleModel model,LinearLayout spinner) {
		this.id=model.getId();
		this.model=model;
		this.context=context;
		this.source=model.getSource();
		prefs = ((MainActivity)context).prefs;
		this.spinner=spinner;
		
	}

	@SuppressWarnings("static-access")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
       deviceHash=((MainActivity)getActivity()).authHash;

        String response="";
        //sipky ktore sa zobrazuju pri presuvani clanku
        //ImageView next_article  = (ImageView)rootView.findViewById(R.id.next_article);
		//ImageView prev_article  = (ImageView)rootView.findViewById(R.id.prev_article);
		rel  = (RelativeLayout)rootView.findViewById(R.id.webviewWrapper);
		pozadie  = (RelativeLayout)rootView.findViewById(R.id.webviewWrap);
        overlay  = (FrameLayout) rootView.findViewById(R.id.webView_overlay);
		
		//podla nastaveni zvolime pozadie
		if (prefs.getBoolean("bg_white",true)){
			pozadie.setBackgroundColor(Color.WHITE);
		} else {
            pozadie.setBackgroundColor(Color.BLACK);
            rel.setBackgroundColor(Color.BLACK);
        }
		
		//vytvarame objekt triedy CustomWebView kde sa spracuvavaju dotyky
        //myWebView = new CustomWebView(context,model,next_article,prev_article,rel);
        myWebView = new CustomWebView(context,model,rel);
        myWebView.clearCache(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        	//myWebView.setWebContentsDebuggingEnabled(true);
        }
        myWebView.setBackgroundColor(Color.TRANSPARENT);
        myWebView.setVisibility(View.INVISIBLE);
        
      //***********************************************************JET**********************************************************************
        //ak je clanok z svet ocami banky alebo informacneho servisu
        if (!(source.equals("3"))){
		try {
	   		String lastChange = model.getLastChange();	
	   		newDir="/"+Constants.STORAGE_FILE+"/articles/"+source+"-"+id+"-"+((MainActivity)getActivity()).lang+"-"+lastChange;
	   		File currentArticle = new File(((MainActivity)context).getExternalCacheDir ().toString()  + newDir);
	   		File currentArticleIndex = new File(((MainActivity)context).getExternalCacheDir ().toString()  + newDir+"/index.html");
	   		//zistujeme ci sa clanok s danym lastChange nachadza v pamati tabletu
	        if(!(currentArticle.isDirectory() && currentArticleIndex.isFile() )) {
	        	if (((MainActivity)getActivity()).isNetworkAvailable()){
	        		//ak sa nenachadza vymazeme stare s neaktualnym lastChange a stiahneme clanok do pamate
                    //System.out.println(newDir);
                    Functions.deleteOldDirectory(source+"-"+id,context);
                    //System.out.println("http://194.50.215.137/api/news/get_detail/" + id+"/"+((MainActivity)context).lang);
                    if (source.equals("1")){
                        response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/news/get_detail/" + id+"/"+((MainActivity)context).lang, deviceHash);
                    } else {
                        response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/club/get_detail/" + id+"/"+((MainActivity)context).lang, deviceHash);
                    }


                    System.out.println(response);
	    	   		JSONObject json = new JSONObject(response);
	    	   		JSONObject jsonDetail = json.getJSONObject("result");

	    	   		zipUrl= jsonDetail.getString("file");		
	    	        download(zipUrl, newDir);
	        	} else isUnReached=true;
	        	
			// zistovanie ci existuje stary priecinok
	        } 	else isDownloaded=true;        
			
		} catch (Exception e) {
			((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error5));
	   		System.out.println("from detail jet: "+e);

	   	} //**************************************************************magnus**************************************************************************
        } else {
        	try {   
        		//kontrola ci existuje magazin priecinok
        		File magazinDir = new File(((MainActivity)context).getExternalCacheDir ().toString()  + "/"+Constants.STORAGE_FILE+"/articles/3-"+model.getMagazinID()+
        				"-"+((MainActivity)getActivity()).lang+"-"+model.getLastChange());
    	        if(!magazinDir.isDirectory()) {
    	        	Functions.deleteOldDirectory("3-"+model.getMagazinID(),context);
    	        	magazinDir.mkdirs();   	            	         	           	        			
    	        }
    	      //koniec kontroly ci existuje magazin priecinok
    	   		zipUrl= model.getZipUrl();
    	   		
    	   		newDir="/"+Constants.STORAGE_FILE+"/articles/3-"+model.getMagazinID()+"-"+((MainActivity)getActivity()).lang+"-"+model.getLastChange()+"/"+
    	   		model.getMagnusArticleId();
    	   		File currentArticle = new File(((MainActivity)context).getExternalCacheDir ().toString()  + newDir);
    	   		File currentArticleIndex = new File(((MainActivity)context).getExternalCacheDir ().toString()  + newDir+"/index.html");
    	   		
    	        if(!(currentArticle.isDirectory() && currentArticleIndex.isFile() )) {
    	        	if (((MainActivity)getActivity()).isNetworkAvailable()){
    	        		if (zipUrl.equals("")){
    	    	   			response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/archive/get_article/" + model.getId(), deviceHash);
    	    		   		JSONObject json = new JSONObject(response);
    	    		   		JSONObject jsonDetail = json.getJSONObject("result");
    	    		   		//System.out.println(json);
    	    		   		zipUrl= jsonDetail.getString("zip");
    	    	   		}
    	        	download(zipUrl, newDir);
    	        	} else isUnReached=true;
    		    } else isDownloaded=true;  	        
    			
    		} catch (Exception e) {
    			((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error5));
    	   		System.out.println("from get_article"+e);
    	   		File forDelete = new File(((MainActivity)context).getExternalCacheDir ().toString()  + newDir+"/index.html");
    			forDelete.delete();
    	   	}
        	
        	
        }
        //*****************************************************koniec magnusu******************************************************************************       
        
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		
		//nastavenie parametrov pre sipky doprava a dolava
		/*RelativeLayout.LayoutParams next_article_params = (LayoutParams) next_article.getLayoutParams();
		RelativeLayout.LayoutParams prev_article_params = (LayoutParams) prev_article.getLayoutParams();
		next_article_params.width=screenWidth/arrow_koef;
		next_article_params.height=screenWidth/arrow_koef;
		next_article_params.setMargins(0, 0, screenWidth/arrow_koef, 0);
		prev_article_params.width=screenWidth/arrow_koef;
		prev_article_params.height=screenWidth/arrow_koef;
		prev_article_params.setMargins(screenWidth/arrow_koef, 0, 0, 0);
		next_article.setVisibility(View.INVISIBLE);
		prev_article.setVisibility(View.INVISIBLE);	 */
		
		LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		rel.addView(myWebView,params);		
		
		//zistujeme ci sa clanok nachadza medzi oblubenymi
		 List<String> list = ((MainActivity)context).getFavourites();
		 int indexFav = ((MainActivity)context).isIdinList(list, model.getId(),model.getSource());

		 File f = new File(((MainActivity)context).getExternalCacheDir ().toString()  + "/"+Constants.STORAGE_FILE+"/articles/system/settings.js");
         if(f.isFile()){
        	 if (indexFav<0){
        		 new EditSettingsFile(4,"0",context);
        	 } else {       	  
        		 new EditSettingsFile(4,"1",context);
        	 }
         } else {
        	File directoryForDelete = new File(((MainActivity)context).getExternalCacheDir ().toString() +"/"+Constants.STORAGE_FILE+"/articles/system");		   				
     		Functions.deleteDirectory(directoryForDelete);
     		((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error14));
         }
       //ak je clanok stiahnuty tak sa nacita obsah a nastavia odporucane clanky 
       if (isDownloaded){
    	   loadUrl();
           if (!source.equals("2")){
               handleRelated();
           }

       } else if (isUnReached){
    	   loadUrl();
       }
        
       
        return rootView;
    }

	 //stiahne clanok
	 private void download(String zipUrl, String newDir){
         ((MainActivity)context).article_task = new LoadArticle(context,this).execute(new String[] { zipUrl, newDir});
	 }
	


	//nacita obsah clanku
	public void loadUrl(){
		 File currentArticleIndex = new File(((MainActivity)context).getExternalCacheDir ().toString() + newDir+"/index.html");
		 this.spinner.setVisibility(View.INVISIBLE);
	         if (!currentArticleIndex.isFile()) {
	        	 ((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error14));
	         } else {
	        //nastavi sa webClient, ktory sleduje zmeny url adresy vo webView na zaklade cinnosti uzavatela	 
	         myWebView.setWebViewClient(new WebC(context,model,spinner,rel, myWebView,overlay));
                 // System.out.println("file:///" + currentArticleIndex.getAbsolutePath());
	         myWebView.loadUrl("file:///" + currentArticleIndex.getAbsolutePath());
	      //   myWebView.loadDataWithBaseURL("file:///android_asset/","file:///" + currentArticleIndex.getAbsolutePath(),"text/html","utf-8",null);
	        
	         }
	}
	//vytvaraju sa v pravom menu jednotlive polozky odporucanych clankov. Pre magnus clanky sa zobrazuje navyse okno magnus magazina daneho clanku
	public void handleRelated (){
		 //*****************************************************related***************************************************************************************
		//System.out.println(model.);
        ((MainActivity)getActivity()).right_frames.removeAllViews();
        ((MainActivity)getActivity()).right_frames_magnus.removeAllViews();
		related_items = Functions.getRelated(model,context);
	        if (related_items!=null){

	        for (i=0;i<related_items.size();i++){
	        	final RightMenuFrame rightFrame = new RightMenuFrame(getActivity(), related_items.get(i),i,false);
	        	 ((MainActivity)getActivity()).right_frames.addView(rightFrame);
	        	 rightFrame.setOnClickListener(new View.OnClickListener()
	             {
	                 @Override
	                 public void onClick(View v)
	                 {
	                	 if ((!((MainActivity)context).logged)&&((related_items.get(rightFrame.getI()).getLocked().equals("1")))){
						 ((MainActivity)context).menuLayout.displayLogin(false,0,0);
	                 } else {  ((MainActivity)getActivity()).currentItems=related_items;
	               	 ((MainActivity)getActivity()).displayDetail(related_items.get(rightFrame.getI()));

	                  }

	                 }
	             });
				}
	        if (source.equals("3")){
		        final RightMenuFrame rightFrame = new RightMenuFrame(getActivity(), model,0,true);
		        ((MainActivity)getActivity()).right_frames_magnus.addView(rightFrame);
	        }
	        }
	      //*****************************************************end related***************************************************************************************
	}
	
	public CustomWebView getMyWebView() { 
		return myWebView;
	}
	
	
}
