package jtb.magazin;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jtb.magazin.UI.CustomWebView;
import jtb.magazin.UI.WorldOfTags;
import jtb.magazin.model.ArticleModel;

//pozoruje zmeny url adresy vo webView a obsluhuje ich
public class WebC extends WebViewClient {
    Context context;
    String id;
    LinearLayout spinner;
    String source;
    ProgressDialog pDialog;
    MyTimerTask myTask;
    Timer myTimer;
    final Handler handler;
    RelativeLayout rel;
    CustomWebView myWebView;
    int timerProgress;
    int currentProgress;
    boolean actualFavorites = false;
    FrameLayout overlay;
    ArticleModel model;

    public WebC(Context context, ArticleModel model, LinearLayout spinner, RelativeLayout rel, CustomWebView myWebView, FrameLayout overlay) {
        //	System.out.println("som v detaile-1");
        this.context = context;
        this.id = model.getId();
        this.model=model;
        this.spinner = spinner;
        this.source = model.getSource();
        myTask = new MyTimerTask();
        myTimer = new Timer();
        handler = new Handler();
        this.rel = rel;
        this.myWebView = myWebView;
        this.overlay=overlay;
        // System.out.println("som v detaile0");


    }

    @Override
    public void onPageFinished(WebView view, String url) {

        //Toast.makeText(context, "uz som nacital: "+view.getUrl(), Toast.LENGTH_SHORT).show();
        //System.out.println("onPageFinished:"+url);
        int index = url.indexOf("#");
        if (index > 0) {
            String end = url.substring(index, url.length());
            System.out.println(end);


            //ak sa klikne na oblubene tak sa bud prida clanok medzi oblubene, alebo ak je tak sa odstrani zo zoznamu
            if (end.equals("#togglefavourite")) {
                List<String> list = ((MainActivity) context).getFavourites();
                int indexFav = ((MainActivity) context).isIdinList(list, id, source);
                if (indexFav < 0) {
                    list.add(source + "-" + id);
                    ((MainActivity) context).saveFavouritesList(list);
                    if(source.equals("3")){
                        Gson gson = new Gson();
                        String jsonItem = gson.toJson(model);
                        //ak sme ziskali data pre dany clanok ulozime si do suboru pre pripad vypadnutia internetu
                        File cacheFile = new File(context.getExternalCacheDir ().toString() +"/cache/articles/"+model.getSource()+
                                "-"+model.getId()+"-"+((MainActivity)(context)).lang+"-"+model.getLastChange()+".txt");
                        ((MainActivity)(context)).writeToFile(cacheFile,jsonItem);
                    }
                } else {

                    for (Iterator<String> iter = list.listIterator(); iter.hasNext(); ) {
                        String a = iter.next();
                        if (a.equals(source + "-" + id)) {
                            iter.remove();
                        }
                    }

                    ((MainActivity) context).saveFavouritesList(list);
                }
                if (((MainActivity) context).actualView == 4) {
                    ((MainActivity) context).updateFavorites = true;
                }

            }

            //ak sa kompletne nacita, zastavi sa casovac a zmizne loader
            if (end.equals("#ready")) {
                //((MainActivity)context).displayView(9, "", null);
                //	spinner.setVisibility(View.GONE);
                myWebView.setWebviewHeight();
                overlay.setClickable(false);
                myTimer.cancel();
                ((MainActivity) context).dialog_lin.setVisibility(View.INVISIBLE);
            }
            //po stisku spat sa odtsrani detail clanku
            if (end.equals("#back")) {
                if (((MainActivity) context).updateFavorites)
                    ((MainActivity) context).spinner.setVisibility(View.VISIBLE);
                ((MainActivity) context).displayView(9, "", null);
            }
            //indikuje ze sa nachadzame v galerii, cim sa zabranuje prechod na dalsi clanok v CustomWebView
            if (end.equals("#galleryvisible")) {
                ((MainActivity) context).isGallery = true;
            }
            if (end.equals("#galleryhidden")) {
                ((MainActivity) context).isGallery = false;
            }
            //po kliku na krystal suvislosti spusti sa Trieda WorldOfTags
            if (end.equals("#worldoftags")) {
                new WorldOfTags(context, id, source, myWebView);
                ((MainActivity) context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ((MainActivity) context).mDrawerLinear);
                ((MainActivity) context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ((MainActivity) context).mDrawerRight);
            }
            //na zaciatku nacitavania je stav 50% lebo prvych 50% je stahovanie clanku
            if (end.equals("#recalc")) {
                ((MainActivity) context).dialog.setProgress(50);
                ((MainActivity) context).dialog_lin.setVisibility(View.VISIBLE);

                //((MainActivity)context).createDialog(pDialog, ((MainActivity)context).getString(R.string.dialog_load));
            }

            if (end.startsWith("#progress")) {

                int index2 = end.indexOf("-");
                if (index2 > 0) {
                    String progress = end.substring(index2 + 1, end.length());
                    int progressValue = Integer.parseInt(progress);
                    ((MainActivity) context).dialog.setProgress(50 + (progressValue / 2));
                    if (progressValue>30) {
                        myWebView.setVisibility(View.VISIBLE);
                        ((MainActivity) context).dialog_lin.setVisibility(View.INVISIBLE);
                    }
                    //ak sa progres posunul casovac sa resetne
                    if (progressValue != timerProgress) {
                        myTimer.cancel();
                        timerProgress = progressValue;
                        myTimer = new Timer();
                        myTimer.schedule(new MyTimerTask(), 10000, 10000);
                    }

                }
            }
        }
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            handler.post(new Runnable() {
                @SuppressWarnings("unchecked")
                public void run() {
                    try {
                        System.out.println("som v maTimerTask");
                        stopWebView();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }
                }
            });

        }
    }


    public void stopWebView() {
        myTimer.cancel();
        rel.removeView(myWebView);
        myWebView.stopLoading();
        //myWebView.removeView;
        myWebView.setWebViewClient(null);
        myWebView.setWebChromeClient(null);
        //	WebStorage.getInstance().deleteAllData();
        myWebView.destroy();
        ((MainActivity) context).dialog_lin.setVisibility(View.INVISIBLE);
        //myWebView=null;
        ((MainActivity) context).displayView(9, "", null);
        ((MainActivity) context).errorMessage(((MainActivity) context).getString(R.string.error3), ((MainActivity) context).getString(R.string.error5));

    }

    //ked sa zacne nacitavat stranka, spusta sa timer na 10s a pozoruje sa ci sa zmenil progress nacitavania, ak casovac dojde do konca tak
    //sa zastavi webView, ak nastane zmena opat sa spusti casovac na 10s
    @Override
    public void onPageStarted(WebView view, String url, Bitmap facIcon) {
        //System.out.println("onPageStarted:"+url);
        try {
            timerProgress = 0;
            myTimer.schedule(myTask, 10000, 10000);
        } catch (Exception e){
            System.out.println("Exception from OnPageStarted:"+e);
        }


        //


        //System.out.println("nacitavam "+id);
        //spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        System.out.println(description);

        super.onReceivedError(view, errorCode, description, failingUrl);
        // Toast.makeText(((MainActivity)context).getBaseContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //System.out.println("shouldOverrideUrlLoading:"+url);
        //po stisku spat sa odtsrani detail clanku
        if (url.startsWith("messenger")) {
            System.out.println("som v #messenger");
            int index2 = url.indexOf(":");
            if (index2 > 0) {

                String id = url.substring(index2 + 1, url.length());
                int index3 = id.indexOf(":");
                if (index3>0) id= id.substring(0, index3);
                System.out.println(id);
                ArticleModel model = getLinkModel(id, "1");
                if (model != null) {
                    ArrayList<ArticleModel> items = new ArrayList<ArticleModel>();
                    items.add(model);
                    ((MainActivity) context).displayDetail(model);
                    ((MainActivity) context).currentItems = items;
                    ((MainActivity) context).spinner.setVisibility(View.VISIBLE);
                }
                return true;
            }

        }
        int index = url.lastIndexOf(".");
        if (index > 0) {
            String end = url.substring(index, url.length());
            if (end.equals(".pdf")){
                url = url.substring(url.lastIndexOf("articles"), url.length());
                File file = new File(((MainActivity)context).getExternalCacheDir ().toString()  + "/"+Constants.STORAGE_FILE+"/"+url);


                if (file.exists()) {
                    System.out.println("existujem");
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        context.startActivity(intent);
                    }
                    catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"No Application Available to View PDF", Toast.LENGTH_SHORT).show();
                    }
                } else System.out.println("neexistujem");


            } else {
                Intent i = new Intent(context, WebViewActivity.class);
                i.putExtra("url", url);
                context.startActivity(i);
            }
            return true;
        } else {
            return false;
        }

    }

    private ArticleModel getLinkModel(String id, String source) {
        ArticleModel model = null;
        try {
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("id[0]", source + "-" + id));

            String response = CustomHttpClient.executeHttpPost("http://194.50.215.137/api/common/get_basic/" +
                    ((MainActivity) (context)).lang, ((MainActivity) context).authHash, postParameters);
            JSONObject json = new JSONObject(response);
            JSONArray jsonBasic = json.getJSONArray("result");
            System.out.println(jsonBasic);
            for (int i = 0; i < jsonBasic.length(); i++) {

                JSONObject jsonArticle = jsonBasic.getJSONObject(i);

                String locked = jsonArticle.getString("locked");
                String lastChange = jsonArticle.getString("lastChange");
                String title = jsonArticle.getString("title");
                String perex = jsonArticle.getString("perex");
                String thumbURL = jsonArticle.getString("thumbURL");
                String categoryID = jsonArticle.getString("categoryID");
                String publishDate = jsonArticle.getString("publishDate");
                model = new ArticleModel();
                model.setPos(0);
                model.setLocked(locked);
                model.setSource("1");
                model.setId(id);
                model.setLastChange(lastChange);
                model.setTitle(title);
                model.setDatePublish(publishDate);
                model.setPerex(perex);
                model.setImageUrl(thumbURL);
                model.setCategoryID(categoryID);

            }

        } catch (Exception e) {
            System.out.println("from getLinkArticle: " + e);
            	//((MainActivity)context).errorMessage("Pripojenie so serverom", "Nepodarilo sa nastavi� s�visiace �l�nky");
        }
        return model;

    }
}