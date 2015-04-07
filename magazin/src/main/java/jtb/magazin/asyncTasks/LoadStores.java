package jtb.magazin.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.store.ArticleStore;
import jtb.magazin.store.MagnusListStore;
import jtb.magazin.store.MarketStore;
import jtb.magazin.store.TwitterStore;

/**
 * Created by zelenasova on 9.3.2015.
 */
// v osobitnom vlakne nacita vsetky potrebne story
public class LoadStores extends AsyncTask<String, String, String> {

    private Context context;

    public LoadStores(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("som v LoadStores");
        //spinner.setVisibility(View.VISIBLE);

    }



    @Override
    protected String doInBackground(String... urls) {
        ((MainActivity)context).updateAuth();
        if (isCancelled()) {
            return null;
        }
        ((MainActivity)context).updateLatest();
        if (isCancelled()) {
            return null;
        }

        if (((MainActivity)context).currentError) publishProgress();
        ((MainActivity)context).currentError = false;
        //System.out.println("idem nacitat twitter store");
        if (isCancelled()) {
            return null;
        }
        ((MainActivity)context).twitterStore = new TwitterStore(context);
        if (isCancelled()) {
            return null;
        }
        //System.out.println("idem nacitat jet store");
        ((MainActivity)context).storeJet = new ArticleStore(context, "0");
        if (isCancelled()) {
            return null;
        }
        //System.out.println("idem nacitat servis store");
        ((MainActivity)context).storeServis = new ArticleStore(context, "1");
        if (isCancelled()) {
            return null;
        }
        ((MainActivity)context).storeKlub = new ArticleStore(context, "klub");
        if (isCancelled()) {
            return null;
        }
        //System.out.println("idem nacitat market store");
        ((MainActivity)context).marketStore = new MarketStore(context);
        if (isCancelled()) {
            return null;
        }
        //System.out.println("idem nacitat magnusList store");
        ((MainActivity)context).magnusList = new MagnusListStore(context);
        if (isCancelled()) {
            return null;
        }

        String json = ((MainActivity)context).gson.toJson(((MainActivity)context).twitterStore);
        ((MainActivity)context).prefsEditor.putString("savedTwitterStore", json);
        String json2 = ((MainActivity)context).gson.toJson(((MainActivity)context).storeJet);
        ((MainActivity)context).prefsEditor.putString("savedStoreJet", json2);
        String json3 = ((MainActivity)context).gson.toJson(((MainActivity)context).storeServis);
        ((MainActivity)context).prefsEditor.putString("savedStoreServis", json3);
        String json4 = ((MainActivity)context).gson.toJson(((MainActivity)context).storeKlub);
        ((MainActivity)context).prefsEditor.putString("savedStoreKlub", json4);
        ((MainActivity)context).prefsEditor.commit();
			if (( ((MainActivity)context).twitterStore.error) || ( ((MainActivity)context).storeJet.error)|| ( ((MainActivity)context).storeJet.getBasicError) || ( ((MainActivity)context).currentError)|| ( ((MainActivity)context).storeServis.error)
                    || ( ((MainActivity)context).storeKlub.error)|| ( ((MainActivity)context).storeKlub.getBasicError) ||
                    ( ((MainActivity)context).marketStore.error)|| ( ((MainActivity)context).magnusList.error))
        	publishProgress();
        return urls[2];
    }

    protected void onProgressUpdate(String... progress) {

        ((MainActivity)context).errorMessage(context.getString(R.string.error3), context.getString(R.string.error6));
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            ((MainActivity)context).updateAppSettings();
            ((MainActivity)context).menuLayout.updateMenuItems();
            System.out.println("nacitel som vsetky story");

            if ((!((MainActivity)context).fromResume)) {
                if (!((MainActivity)context).completLoadStore) {
                    ((MainActivity)context).createHomeFragment(((MainActivity) context).storeJet, false);
                    ((MainActivity)context).currentItems = ((MainActivity)context).storeJet.getItems();
                    ((MainActivity)context).gridCurrentItems=((MainActivity)context).storeJet.getItems();
                    ((MainActivity)context).fadeOUT();
                    ((MainActivity)context).completLoadStore = true;
                }
            } else {
                //zistujeme ci bol zmeneny jazyk, ak ano, tak hodime do zakladnehp gridu ako po stlaceni menu
                if (result.equals("language")){
                    ((MainActivity)context).displayView(1, "", null);
                    ((MainActivity)context).dialog_lin.setVisibility(View.INVISIBLE);
                    ((MainActivity)context).dialog.setVisibility(View.VISIBLE);
                } else {
                    ((MainActivity)context).updateView();
                }

                ((MainActivity)context).fromResume = false;
            }
        } catch (Exception e) {
            System.out.println("Exception from LoadStores: "+e);
        }



        // mDrawerLayout.openDrawer(mDrawerLinear);
    }
}
