package jtb.magazin;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;

import jtb.magazin.model.ArticleModel;

//staticke funkcie ktore sa vyuzivaju v celej aplikacii
//-deleteDirectory(File path)
//-sha1Hash(String toHash)
//-bytesToHex(final byte[] hash)
//-addInt(String [] series, String newInt)
//-CopyAssets(AssetManager assetManager, String cacheDir)
//-copyFile(InputStream in, OutputStream out)
//-removeInt(String[] original, int position)
//-isIntInArray(String[] pole, String element)

public final class Functions {
	
	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	public static String sha1Hash(String toHash)
	{
	    String hash = null;
	    try
	    {
	        MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
	        byte[] bytes = toHash.getBytes("UTF-8");
	        digest.update(bytes, 0, bytes.length);
	        bytes = digest.digest();

	        // This is ~55x faster than looping and String.formating()
	        hash = bytesToHex( bytes );
	    }
	    catch( NoSuchAlgorithmException e )
	    {
	        e.printStackTrace();
	    }
	    catch( UnsupportedEncodingException e )
	    {
	        e.printStackTrace();
	    }
	    return hash;
	}

    public static final void deleteOldDirectory(String id,Context context){

        File directory = new File(((MainActivity)context).getExternalCacheDir ().toString() +"/"+Constants.STORAGE_FILE+"/articles");
        File[] contents = directory.listFiles();
        // the directory file is not really a directory..
        if (contents == null) {	}
        // Folder is empty
        else if (contents.length == 0) {}
        // Folder contains files
        else {
            String forDelete = "";
            for (File file : contents) {
                if (file.getName().startsWith(id+"-")) {
                    forDelete=file.getName();
                    System.out.println("Deleted file: "+file.getName());
                }
            }
            if (!(forDelete.equals(""))){
                File directoryForDelete = new File(((MainActivity)context).getExternalCacheDir ().toString() +"/"+Constants.STORAGE_FILE+"/articles/"+forDelete);
                //	System.out.println(((MainActivity)context).getExternalCacheDir ().toString() +"/"+Constants.STORAGE_FILE+"/articles/"+forDelete);
                Functions.deleteDirectory(directoryForDelete);
            }
        }
    }

	public static  String bytesToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	public static String[] addInt(String [] series, String newInt){
	    //create a new array with extra index
		String[] newSeries = new String[series.length + 1];

	    //copy the integers from series to newSeries    
	    for (int i = 0; i < series.length; i++){
	        newSeries[i] = series[i];
	    }
	//add the new integer to the last index     
	    newSeries[newSeries.length - 1] = newInt;


	    return newSeries;

	     }
	
	 public static void CopyAssets(AssetManager assetManager, String cacheDir) {
         
         String[] files = null;
         try {
             files = assetManager.list("Files");
         } catch (IOException e) {
           //  Log.e("tag", e.getMessage());
         }
  
         String filename ="system.zip";
             System.out.println("File name => "+filename);
             InputStream in = null;
             OutputStream out = null;
             try {
               in = assetManager.open(filename);   // if files resides inside the "Files" directory itself
               out = new FileOutputStream(cacheDir +"/"+Constants.STORAGE_FILE+"/articles/system/" + filename);
               copyFile(in, out);
               in.close();
               in = null;
               out.flush();
               out.close();
               out = null;
             } catch(Exception e) {
                // Log.e("tag", e.getMessage());
             }
         }
    
     private static void copyFile(InputStream in, OutputStream out) throws IOException {
         byte[] buffer = new byte[1024];
         int read;
         while((read = in.read(buffer)) != -1){
           out.write(buffer, 0, read);
         }
     }


	
	
	public static String[] removeInt(String[] original, int position){
	    String[] n = new String [original.length - 1];
	    System.arraycopy(original, 0, n, 0, position );
	    System.arraycopy(original, position+1, n, position, original.length - position-1);
	    return n;
	}
	
	public static int isIntInArray(String[] pole, String element){
		for (int i=0;i<pole.length;i++){
			if (pole[i].equals(element)) return i;
		}
		return -1;
	}

    //ziska odporucane clanky zo servera
    public static ArrayList<ArticleModel> getRelated(ArticleModel model, Context context) {
        String response="";
        ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
        ArrayList<String> ids = new ArrayList<String>();
        HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
        Gson gson = new Gson();
        try {
            String url = "http://194.50.215.137/api/common/get_related_articles/"+model.getId()+"/"+model.getSource()+"/"+((MainActivity)context).lang;
            String forHash = Functions.sha1Hash(url+((MainActivity)context).authHash);
            JSONObject json;
            //ak nie je pripojenie k internetu pozrie sa ci uz nie su ulozene v cache
            if (!((MainActivity)(context)).isNetworkAvailable()){
                String fromFile = ((MainActivity)(context)).readFromFile(new File(((MainActivity)context).getExternalCacheDir ().toString() +
                        "/cache/related_"+forHash+".txt"));
                Type type = new TypeToken<ArrayList<ArticleModel>>(){}.getType();
                items = gson.fromJson(fromFile, type);
                return items;


            } else {
                response = CustomHttpClient.executeHttpGet(url, ((MainActivity)context).authHash);
                json = new JSONObject(response);

                JSONArray jsonIds = json.getJSONArray("result");

                for (int i = 0; i < jsonIds.length(); i++) {
                    JSONObject c = jsonIds.getJSONObject(i);
                    String id = c.getString("id");
                    String locked = c.getString("locked");
                    String source = c.getString("source");
                    String lastChange = c.getString("lastChange");
                    //	System.out.println(c);
                    ArticleModel model2 = new ArticleModel();
                    model2.setLocked(locked);
                    model2.setSource(source);
                    model2.setId(id);
                    model2.setLastChange(lastChange);
                    model2.setPos(i);
                    items.add(model2);
                    hashmap.put(id, i);
                    ids.add(source+"-"+id);
                }
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                for (int i=0; i<ids.size(); i++){
                    //	System.out.println("z funkcie get related: "+ids.get(i));
                    postParameters.add(new BasicNameValuePair("id["+i+"]",ids.get(i)));
                }
                response = CustomHttpClient.executeHttpPost("http://194.50.215.137/api/common/get_basic/" + ((MainActivity) (context)).lang, ((MainActivity)context).authHash, postParameters);
                JSONObject json2 = new JSONObject(response);
                JSONArray jsonBasic = json2.getJSONArray("result");
                for (int i = 0; i < jsonBasic.length(); i++) {
                    JSONObject jsonArticle = jsonBasic.getJSONObject(i);
                    String title=jsonArticle.getString("title");
                    String perex=jsonArticle.getString("perex");
                    String thumbURL=jsonArticle.getString("thumbURL");
                    String categoryID=jsonArticle.getString("categoryID");
                    String publishDate=jsonArticle.getString("publishDate");

                    int j = (Integer) hashmap.get(jsonArticle.getString("id"));
                    items.get(j).setTitle(title);
                    items.get(j).setDatePublish(publishDate);
                    items.get(j).setPerex(perex);
                    items.get(j).setImageUrl(thumbURL);
                    items.get(j).setCategoryID(categoryID);

                    if (items.get(j).getSource().equals("3")){
                        //	System.out.println(jsonArticle);
                        String articleID = jsonArticle.getString("articleID");
                        String magazinId = jsonArticle.getString("magazineID");
                        items.get(j).setMagazinID(magazinId);
                        items.get(j).setMagnusArticleId(articleID);
                        items.get(j).setZipUrl("");
                    }
                }
                //ak sa podarlo stiahnut, ulozi sa do cache
                String jsonItems = gson.toJson(items);
                ((MainActivity)(context)).writeToFile(new File(((MainActivity)context).getExternalCacheDir ().toString() +"/cache/related_"+forHash+".txt"),jsonItems);
            }
        } catch (Exception e) {
            System.out.println("from getRelated: "+e);
            //	((MainActivity)context).errorMessage("Pripojenie so serverom", "Nepodarilo sa nastavi� s�visiace �l�nky");
        }
        return items;
    }
	
}