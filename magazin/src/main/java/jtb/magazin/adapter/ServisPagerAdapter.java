package jtb.magazin.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.UI.ArticleThumbnailFrame;
import jtb.magazin.UI.MarketFrame;
import jtb.magazin.store.ArticleStore;
import jtb.magazin.store.GetBasic;

//adapter ktory na zaklade dat (store) naplni obsah ViewPagera

public class ServisPagerAdapter extends PagerAdapter {
    ArticleStore store;
    DisplayImageOptions options;
    private Context mContext;
    ImageLoader imageLoader;
    float mf;
    int m;
    int rowHeight;
    int rowWidth;
    int rowLastWidth;
    int rowLastHeight;
    int column = 5;
    int row = 4;
    ArrayList<GridLayout.LayoutParams> params;
    public int loadedStoreItem;

    public ServisPagerAdapter(Context context,DisplayImageOptions options,ImageLoader imageLoader,ArticleStore store) {
        loadedStoreItem=store.getNextIndex();
        this.store=store;
        this.options=options;
        mContext = context;
        this.imageLoader=imageLoader;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mf= metrics.widthPixels;
        m=(int) (mf/Constants.MARGIN_WIDTH_KOEF);
        float width = (float)((metrics.widthPixels)-6*m);
        float Contentheight = ((MainActivity)context).screenHeight;
        float height = Contentheight-5*m;
        //vyska a sirka jedneho ramceka
        rowHeight = (int)(height/4);
        rowWidth = (int)(width/5);
        //kvoli nepresnostiam pri zaokruhlovani pri prevode float na int posledny stlpec a riadok musi vyplnit zvysny priestor.
        rowLastWidth = (int) (mf-6*m-4*rowWidth);
        rowLastHeight = (int) (height-3*rowHeight);

        //vytvara sa mriezka s 8 okienkami
        //************************************************************clanok1***********************************************************
        GridLayout.LayoutParams param1 =new GridLayout.LayoutParams();
        param1.setMargins(m, m, m, m);
        param1.columnSpec = GridLayout.spec(0,2);
        param1.rowSpec = GridLayout.spec(0,2);
        param1.width = rowWidth*2+m;
        param1.height=rowHeight*2+m;

        //************************************************************clanok2*******************************************************
        GridLayout.LayoutParams param2 =new GridLayout.LayoutParams();
        param2.setMargins(0, m, m, m);
        param2.width = rowWidth*2+m;
        param2.height=rowHeight;
        param2.columnSpec = GridLayout.spec(2,2);
        param2.rowSpec = GridLayout.spec(0);
        //************************************************************clanok3*******************************************************
        GridLayout.LayoutParams param3 =new GridLayout.LayoutParams();
        param3.height = LayoutParams.WRAP_CONTENT;
        param3.width = LayoutParams.WRAP_CONTENT;
        param3.setMargins(0, m, m, m);
        param3.width = rowLastWidth;
        param3.height=rowHeight;
        param3.columnSpec = GridLayout.spec(4);
        param3.rowSpec = GridLayout.spec(0);

        //************************************************************clanok4****************************************************************

        GridLayout.LayoutParams param4 =new GridLayout.LayoutParams();
        param4.setMargins(0, 0, m, m);
        param4.width = rowWidth;
        param4.height=rowHeight;
        param4.columnSpec = GridLayout.spec(2);
        param4.rowSpec = GridLayout.spec(1);
        //************************************************************clanok5************************************************************

        GridLayout.LayoutParams param5 =new GridLayout.LayoutParams();
        param5.setMargins(0, 0, m, m);
        param5.width = rowWidth+rowLastWidth+m;
        param5.height=rowHeight;
        param5.columnSpec = GridLayout.spec(3,2);
        param5.rowSpec = GridLayout.spec(1);
        //************************************************************clanok6************************************************************

        GridLayout.LayoutParams param6 =new GridLayout.LayoutParams();
        param6.setMargins(m, 0, m, m);
        param6.width = rowWidth*3+2*m;
        param6.height=rowHeight;
        param6.columnSpec = GridLayout.spec(0,3);
        param6.rowSpec = GridLayout.spec(2);
        //************************************************************clanok7************************************************************

        GridLayout.LayoutParams param7 =new GridLayout.LayoutParams();
        param7.setMargins(0, 0, m, m);
        param7.width = rowWidth;
        param7.height=rowHeight;
        param7.columnSpec = GridLayout.spec(3);
        param7.rowSpec = GridLayout.spec(2);
        //************************************************************clanok8************************************************************

        GridLayout.LayoutParams param8 =new GridLayout.LayoutParams();
        param8.setMargins(0, 0, m, m);
        param8.width = rowLastWidth;
        param8.height=rowHeight;
        param8.columnSpec = GridLayout.spec(4);
        param8.rowSpec = GridLayout.spec(2);
        //************************************************************clanok9************************************************************

        GridLayout.LayoutParams param9 =new GridLayout.LayoutParams();
        param9.setMargins(m, 0, m, m);
        param9.width = rowWidth;
        param9.height=rowLastHeight;
        param9.columnSpec = GridLayout.spec(0);
        param9.rowSpec = GridLayout.spec(3);
        //************************************************************clanok10************************************************************

        GridLayout.LayoutParams param10 =new GridLayout.LayoutParams();
        param10.setMargins(0, 0, m, m);
        param10.width = 2*rowWidth+m;
        param10.height=rowLastHeight;
        param10.columnSpec = GridLayout.spec(1,2);
        param10.rowSpec = GridLayout.spec(3);
        //************************************************************clanok11************************************************************

        GridLayout.LayoutParams param11 =new GridLayout.LayoutParams();
        param11.setMargins(0, 0, m, m);
        param11.width = rowWidth;
        param11.height=rowLastHeight;
        param11.columnSpec = GridLayout.spec(3);
        param11.rowSpec = GridLayout.spec(3);
        //************************************************************clanok12************************************************************

        GridLayout.LayoutParams param12 =new GridLayout.LayoutParams();
        param12.setMargins(0, 0, m, m);
        param12.width = rowLastWidth;
        param12.height=rowLastHeight;
        param12.columnSpec = GridLayout.spec(4);
        param12.rowSpec = GridLayout.spec(3);


        params = new ArrayList<GridLayout.LayoutParams>();
        params.add(param1);params.add(param2);params.add(param3);params.add(param4);
        params.add(param5);params.add(param6);params.add(param7);params.add(param8);
        params.add(param9);params.add(param10);params.add(param11);params.add(param12);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    //kvoli refreshu musi funkcia vratit position_none, inak by zostali v pamati data a nedalo by sa aktualizovat obsah ViewPagera
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        //ak sa zobrazuje Servis tak prve okienko je market, preto pri vypocte poctu stran ViewPagera musime to zohladnit
        return (store.getCount() +1+ 11) / 12;
    }

    public void setData(ArticleStore store) {
        this.store = store;
        this.loadedStoreItem=store.getNextIndex();
    }

    //jedena strana ViewPagera
    @Override
    public Object instantiateItem(ViewGroup view, int pos) {

        //System.out.println("aktual pos: "+pos);
        GridLayout g = new GridLayout(mContext);
        g.setColumnCount(column);
        g.setRowCount(row);

        int k =0;
        if (pos==0) {
            // prvy frame v servise je Market

            FrameLayout f1;
            //ak sa este nevytvoril market frame vytvori sa a ulozi sa do MainActivity do premennej marketFrame
            if (((MainActivity)mContext).marketFrame==null){
                System.out.println("idem vytvorit new marketFrame");
                f1 = new MarketFrame(mContext,rowWidth*2+m,rowHeight*2+m,((MainActivity)mContext).marketStore);
                ((MainActivity)mContext).marketFrame=f1;
            } else {
                if (((MainActivity)mContext).marketFrame.getParent() != null)
                    ((GridLayout)((MainActivity)mContext).marketFrame.getParent()).removeView(((MainActivity)mContext).marketFrame);
                f1=((MainActivity)mContext).marketFrame;
            }
            f1.setLayoutParams (params.get(0));
            g.addView(f1);
            k=1;
        }
        for(int i=k;i<12;i++){

            FrameLayout f1=null;
            if ((pos*12+i) < (store.getCount()+1)){
                //zakladne informacie ako obrazok, title, perex sa stahuju z internetu postupne preto ak sa obrazok je null znamena
                //ze treba doplnit store s dalsimi datami to je dalsich Constants.LOADED_ARTICLES clankov
                if (store.getItem(pos*12+i-1).getImageUrl()== null) {
                    f1 = new ArticleThumbnailFrame(mContext,true);
                    f1.setLayoutParams(params.get(i));
                    g.addView(f1);
                    if ((pos*12+i-1)==loadedStoreItem){
                        System.out.println("nacitavam dalsich 64 storov");
                        loadedStoreItem+=Constants.LOADED_ARTICLES;
                        /*gridBasic=g;
                        iBasic=i;
                        posBasic=pos;*/
                        // System.out.println("volam dalsich 32 framov");
                        LoadBasic task = new LoadBasic();
                        task.execute(new String[] { "", "", "" });
                    }
                }
                //vytvori sa jedno okienko gridu
                f1 = new ArticleThumbnailFrame(mContext, store.getItem(pos*12+i-1),params.get(i).height);

                f1.setLayoutParams (params.get(i));
                g.addView(f1);
            } else  {
                f1 = new ArticleThumbnailFrame(mContext);

                f1.setLayoutParams (params.get(i));
                g.addView(f1);
            }

        }


        view.addView(g);
        return g;


    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public class LoadBasic extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            GetBasic.getBasic(mContext, store.getNextIndex(), Constants.LOADED_ARTICLES, store.getItems(), store.getIds(), store.getHashmap(), store,"1");
            store.setNextIndex(store.getNextIndex() + Constants.LOADED_ARTICLES);
            return "";
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                notifyDataSetChanged();
                if (store.getBasicError) ((MainActivity)mContext).errorMessage(mContext.getString(R.string.error3), mContext.getString(R.string.error6));
            } catch (Exception e) {
                System.out.println("Exception from LoadBasic: "+e);
            }

        }
    }


} 