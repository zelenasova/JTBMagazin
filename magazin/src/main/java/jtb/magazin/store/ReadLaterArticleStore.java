package jtb.magazin.store;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jtb.magazin.CustomHttpClient;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;

//spolocna trieda pre ulozene a oblubene clanky, list sa ziskava z preferences
public class ReadLaterArticleStore {

    private ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
    String deviceHash;

    @SuppressLint("SimpleDateFormat")
    public ReadLaterArticleStore(List<String> list, Context context) {


        //***********************************************************spracovanie JSONU*******************************************************************
        deviceHash = ((MainActivity) (context)).authHash;

        String response = "";
        ArrayList<String> ids = new ArrayList<String>();


        if (list.size() > 0) {

            try {

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                    postParameters.add(new BasicNameValuePair("id[" + i + "]", list.get(i)));
                }

                String url = "http://194.50.215.137/api/common/get_basic/" + ((MainActivity) (context)).lang;
                JSONObject json = null;

                if (!((MainActivity) (context)).isNetworkAvailable()) {
                    Gson gson = new Gson();
                    File directory = new File(((MainActivity) context).getExternalCacheDir().toString() + "/cache/articles");
                    File[] contents = directory.listFiles();
                    // the directory file is not really a directory..
                    if (contents == null) {
                    }
                    // Folder is empty
                    else if (contents.length == 0) {
                    }
                    // Folder contains files
                    else {
                        for (int i = 0; i < list.size(); i++) {
                            //System.out.println(list.get(i)+"-");
                            boolean found = false;
                            for (File file : contents) {
                                if (file.getName().startsWith(list.get(i) + "-" + ((MainActivity) context).lang)) {
                                    found = true;
                                    String fromFile = ((MainActivity) (context)).readFromFile(file);
                                    ArticleModel item = gson.fromJson(fromFile, ArticleModel.class);
                                    items.add(item);
                                }
                            }
                            if (!found)
                                ((MainActivity) context).errorMessage(((MainActivity) context).getString(R.string.error3),
                                        ((MainActivity) context).getString(R.string.error6));
                        }
                    }
                    //System.out.println(jsonAuth);
                } else {
                    response = CustomHttpClient.executeHttpPost(url, deviceHash, postParameters);
                    json = new JSONObject(response);

                }


                //System.out.println(list.size());
                //jsonBasic.getJSONObject("0");
                //System.out.println(json);
                if (json != null) {
                    JSONArray jsonBasic = json.getJSONArray("result");
                    // prvy frame vyhladavanie

                    for (int i = 0; i < jsonBasic.length(); i++) {
                        JSONObject jsonArticle = jsonBasic.getJSONObject(i);
                        //System.out.println(jsonArticle);
                        String title = jsonArticle.getString("title");
                        String perex = jsonArticle.getString("perex");
                        String thumbURL = jsonArticle.getString("thumbURL");
                        String id = jsonArticle.getString("id");
                        String source = jsonArticle.getString("source");

                        String lastChange = jsonArticle.getString("lastChange");


                        ArticleModel model = new ArticleModel();
                        model.setId(id);
                        model.setTitle(title);

                        model.setPerex(perex);
                        model.setPos(i);
                        model.setImageUrl(thumbURL);
                        model.setSource(source);

                        model.setLastChange(lastChange);
                        String publishDate;
                        String price;
                        String locked;
                        if (source.equals("2")){
                            price = jsonArticle.getString("price");
                            model.setPrice(price);
                            model.setLocked("0");
                        } else {
                            publishDate=jsonArticle.getString("publishDate");
                            model.setDatePublish(publishDate);
                            locked = jsonArticle.getString("locked");
                            model.setLocked(locked);
                        }

                        if (source.equals("3")) {
                            String articleID = jsonArticle.getString("articleID");
                            String magazinId = jsonArticle.getString("magazineID");
                            model.setMagazinID(magazinId);
                            model.setMagnusArticleId(articleID);
                            model.setZipUrl("");
                        }

                        items.add(model);

                    }


                    // 	System.out.println(contacts);
                    // 	System.out.println(id);
                    // else
                    //error.setText(R.string.wrong);
                }
                ((MainActivity) context).currentItems = items;
                ((MainActivity) context).gridCurrentItems = items;
            } catch (Exception e) {
                ((MainActivity) context).errorMessage(((MainActivity) context).getString(R.string.error3), ((MainActivity) context).getString(R.string.error6));
                System.out.println("ReadLaterArticleStore:" + e);
            }
        }

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
