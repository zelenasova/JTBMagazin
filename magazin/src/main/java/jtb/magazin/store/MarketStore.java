package jtb.magazin.store;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import jtb.magazin.CustomHttpClient;
import jtb.magazin.Functions;
import jtb.magazin.MainActivity;
import jtb.magazin.model.Forex;
import jtb.magazin.model.Indexy;
import jtb.magazin.model.Kurzy;
import jtb.magazin.model.Xetra;

//prvy frame v informacnom servise je market, tu ziskavame zo servera data
public class MarketStore {

    String response="";
    public ArrayList<Indexy> indexes = new ArrayList<Indexy>();
    public ArrayList<Xetra> xetras = new ArrayList<Xetra>();
    public ArrayList<Forex> forexes = new ArrayList<Forex>();
    public ArrayList<Kurzy> rates = new ArrayList<Kurzy>();
    public boolean error = false;

    public MarketStore(Context context) {


        String url = "http://194.50.215.137/api/common/get_market_indexes";
        String forHash = Functions.sha1Hash(url + ((MainActivity) (context)).authHash);
        File cacheFile = new File(((MainActivity) context).getExternalCacheDir().toString() + "/cache/market.txt");
        JSONObject json = null;
        try {
            try {
                if (!((MainActivity) (context)).isNetworkAvailable()) {
                    if (cacheFile.isFile()) {
                        String fromFile = ((MainActivity) (context)).readFromFile(cacheFile);
                        json = new JSONObject(fromFile);
                    } else error = true;
                } else {
                    response = CustomHttpClient.executeHttpGet(url, ((MainActivity) (context)).authHash);
                    json = new JSONObject(response);
                    //	System.out.println(jsonAuth);
                    String jsonString = json.toString();
                    //	System.out.println(jsonString);
                    ((MainActivity) (context)).writeToFile(cacheFile, jsonString);
                }
            } catch (Exception e) {
                //error.setText(R.string.connection);
                System.out.println("z Market1: " + e);
                if (cacheFile.isFile()) {
                    String fromFile = ((MainActivity) (context)).readFromFile(cacheFile);
                    json = new JSONObject(fromFile);
                } else error = true;
            }


            if (json != null) {
                JSONObject market = json.getJSONObject("result");
                //System.out.println(json);


                JSONArray indexy = market.getJSONArray("indexy");
                for (int i = 0; i < indexy.length(); i++) {
                    JSONArray item = indexy.getJSONArray(i);
                    String titul = item.getString(0);
                    String kurz = item.getString(1);
                    String zmena = item.getString(2);
                    Indexy idx = new Indexy();
                    idx.setTitul(titul);
                    idx.setKurz(kurz);
                    idx.setZmena(zmena);
                    indexes.add(idx);
                }

                JSONArray xetra = market.getJSONArray("spad");
                for (int i = 0; i < xetra.length(); i++) {
                    JSONArray item = xetra.getJSONArray(i);
                    String nazov = item.getString(0);
                    String kurz = item.getString(1);
                    String zmena = item.getString(2);
                    Xetra x = new Xetra();
                    x.setNazov(nazov);
                    x.setKurz(kurz);
                    x.setZmena(zmena);
                    xetras.add(x);
                }

                JSONArray forex = market.getJSONArray("forex");
                for (int i = 0; i < forex.length(); i++) {
                    JSONArray item = forex.getJSONArray(i);
                    String mena = item.getString(0);
                    String pocet = item.getString(1);
                    String cena = item.getString(2);
                    Forex f = new Forex();
                    f.setMena(mena);
                    f.setPocet(pocet);
                    f.setCena(cena);
                    forexes.add(f);
                }

                JSONArray kurzy = market.getJSONArray("kurzy");
                for (int i = 0; i < kurzy.length(); i++) {
                    JSONArray item = kurzy.getJSONArray(i);
                    String mena = item.getString(0);
                    String kurz = item.getString(1);
                    Kurzy k = new Kurzy();
                    k.setMena(mena);
                    k.setKurz(kurz);
                    rates.add(k);
                }

            }
        } catch (Exception e) {
            //error.setText(R.string.connection);
            System.out.println("z Market2: " + e);

        }
    }
}
