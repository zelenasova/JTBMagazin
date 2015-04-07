package jtb.magazin.store;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import jtb.magazin.CustomHttpClient;
import jtb.magazin.Functions;
import jtb.magazin.MainActivity;
import jtb.magazin.model.ArticleModel;

//store sa ziskava na zaklade hladaneho slova, opat ako pri keywords najprv sa ziska zoznam id-ciek a potom sa vola opat server pre
//ziskanie zakladnych dat pre clanky
public class SearchArticleStore {

    private ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
    String deviceHash;
    public boolean error = false;

    @SuppressLint("SimpleDateFormat")
    public SearchArticleStore(String searchText, Context context) {

        //***********************************************************spracovanie JSONU*******************************************************************

        deviceHash = ((MainActivity) (context)).authHash;
        String response = "";
        ArrayList<String> ids = new ArrayList<String>();
        HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
        Gson gson = new Gson();
        try {
            ArrayList<NameValuePair> postParametersSearch = new ArrayList<NameValuePair>();
            postParametersSearch.add(new BasicNameValuePair("text", searchText));

            String url = "http://194.50.215.137/api/common/search/" + ((MainActivity) (context)).lang;
            String forHash = Functions.sha1Hash(url + searchText);
            File cacheFile = new File(((MainActivity) context).getExternalCacheDir().toString() + "/cache/search_" + forHash + ".txt");
            JSONObject json = null;

            if (!((MainActivity) (context)).isNetworkAvailable()) {
                if (cacheFile.isFile()) {
                    String fromFile = ((MainActivity) (context)).readFromFile(cacheFile);
                    Type type = new TypeToken<ArrayList<ArticleModel>>() {
                    }.getType();
                    items = gson.fromJson(fromFile, type);
                } else error = true;
                //System.out.println(jsonAuth);
            } else {
                response = CustomHttpClient.executeHttpPost(url, deviceHash, postParametersSearch);
                json = new JSONObject(response);
                // System.out.println(response);
                JSONArray jsonIds = json.getJSONArray("result");
                //	System.out.println( jsonIds);

                if (jsonIds.length() > 0) {
                    for (int i = 0; i < jsonIds.length(); i++) {
                        JSONObject c = jsonIds.getJSONObject(i);

                        String id = c.getString("id");
                        String locked = c.getString("locked");
                        String source = c.getString("source");
                        String lastChange = c.getString("lastChange");
                        //	System.out.println(c);
                        ArticleModel model = new ArticleModel();
                        model.setLocked(locked);
                        model.setSource(source);
                        model.setId(id);
                        model.setLastChange(lastChange);
                        model.setPos(i);
                        items.add(model);
                        hashmap.put(source + "-" + id, i);
                        ids.add(source + "-" + id);
                    }

                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    for (int i = 0; i < ids.size(); i++) {
                        System.out.println(ids.get(i));
                        postParameters.add(new BasicNameValuePair("id[" + i + "]", ids.get(i)));
                    }

                    System.out.println("tu som");
                    response = CustomHttpClient.executeHttpPost("http://194.50.215.137/api/common/get_basic/" + ((MainActivity) (context)).lang, deviceHash, postParameters);
                    System.out.println(response);
                    JSONObject json2 = new JSONObject(response);
                    System.out.println(json2);
                    JSONArray jsonBasic = json2.getJSONArray("result");
                    //System.out.println(jsonBasic.length());

                    for (int i = 0; i < jsonBasic.length(); i++) {
                        JSONObject jsonArticle = jsonBasic.getJSONObject(i);
                        //System.out.println(items.get(i).getSource());
                        String title = jsonArticle.getString("title");
                        String perex = jsonArticle.getString("perex");
                        String thumbURL = jsonArticle.getString("thumbURL");
                        String categoryID = jsonArticle.getString("categoryID");
                        String publishDate = jsonArticle.getString("publishDate");

                        int j = (Integer) hashmap.get(jsonArticle.getString("source") + "-" + jsonArticle.getString("id"));
                        items.get(j).setTitle(title);
                        items.get(j).setDatePublish(publishDate);
                        items.get(j).setPerex(perex);
                        items.get(j).setImageUrl(thumbURL);
                        items.get(j).setCategoryID(categoryID);

                        if (items.get(j).getSource().equals("3")) {
                            System.out.println(jsonArticle);
                            String articleID = jsonArticle.getString("articleID");
                            String magazinId = jsonArticle.getString("magazineID");
                            items.get(j).setMagazinID(magazinId);
                            items.get(j).setMagnusArticleId(articleID);
                            items.get(j).setZipUrl("");
                        }

                    }
                } //koniec if jsonIds.length()>0
                String jsonItems = gson.toJson(items);
                ((MainActivity) (context)).writeToFile(cacheFile, jsonItems);
            }

        } catch (Exception e) {
            error = true;
            System.out.println("from search article store" + e);
        }
        ((MainActivity) context).currentItems = items;
        ((MainActivity) context).gridCurrentItems = items;

        //***********************************************************koniec spracovania JSONU*******************************************************************
    }

    public int getCount() {

        return items.size();

    }

    public ArticleModel getItem(int index) {

        return items.get(index);
    }


    public ArrayList<ArticleModel> getItems() {
        // TODO Auto-generated method stub
        return null;
    }
}
