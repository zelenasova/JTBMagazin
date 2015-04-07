package jtb.magazin.store;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jtb.magazin.CustomHttpClient;
import jtb.magazin.MainActivity;
import jtb.magazin.model.TwitterModel;

//ziskanie dat pre twitter je jednoduchy, staci raz zavolat server a posle vsetky data naraz
public class TwitterStore {

    private ArrayList<TwitterModel> items = new ArrayList<TwitterModel>();
    public boolean error = false;

    @SuppressLint("SimpleDateFormat")
    public TwitterStore(Context context) {

        String response = "";
        String deviceHash = ((MainActivity) (context)).authHash;
        try {
            response = CustomHttpClient.executeHttpGet("http://194.50.215.137/api/twitter/get/", deviceHash);
            //System.out.println(responseGet.get(0));
            JSONObject jsonFull = new JSONObject(response);
            JSONArray json = jsonFull.getJSONArray("result");

            //System.out.println(json);
            for (int i = 0; i < json.length(); i++) {
                JSONObject c = json.getJSONObject(i);
                String author = c.getString("author");
                String publishDate = c.getString("publishDate");
                String content = c.getString("content");
                String twitterID = c.getString("id");

                String imageUrl = c.getString("authorThumb");
                String retweetedBy = "";
                if (!(c.isNull("retweetedBy"))) {
                    retweetedBy = c.getString("retweetedBy");
                    //System.out.println(retweetedBy);
                }
                TwitterModel model = new TwitterModel();
                model.setTitle("Twitter" + i);
                model.setId(twitterID);
                model.setImageUrl(imageUrl);
                model.setRetweetedBy(retweetedBy);
                //String newstring = new SimpleDateFormat("hh:mm - d MMM yy").format(date);
                model.setDatePublish(publishDate);
                model.setAutor(author);
                model.setTitle(content);
                model.setUrl("");
                items.add(model);
            }
            //System.out.println(json);


        } catch (Exception e) {
            error = true;
            System.out.println("TwitterStore:" + e);
        }
    }

    public int getCount() {

        return items.size();

    }

    public TwitterModel getItem(int index) {

        return items.get(index);
    }
}
