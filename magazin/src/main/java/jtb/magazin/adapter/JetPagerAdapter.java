package jtb.magazin.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import jtb.magazin.store.ArticleStore;
import jtb.magazin.store.GetBasic;

//adapter ktory na zaklade dat (store) naplni obsah ViewPagera

public class JetPagerAdapter extends PagerAdapter {
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
    int row = 3;
    ArrayList<GridLayout.LayoutParams> params;
    GridLayout gridBasic;
    int iBasic;
    int posBasic;
    public int loadedStoreItem;
    ViewPager pager;

    public JetPagerAdapter(Context context, DisplayImageOptions options, ImageLoader imageLoader, ArticleStore store,ViewPager pager) {
        this.pager=pager;
        this.store = store;
        loadedStoreItem=store.getNextIndex();
        this.options = options;
        mContext = context;
        this.imageLoader = imageLoader;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mf = metrics.widthPixels;
        m = (int) (mf / Constants.MARGIN_WIDTH_KOEF);
        float width = (float) ((metrics.widthPixels) - 6 * m);
        float Contentheight = ((MainActivity) context).screenHeight / 4 * 3;
        float height = Contentheight - 4 * m;
        //vyska a sirka jedneho ramceka
        rowHeight = (int) (height / 3);
        rowWidth = (int) (width / 5);
        //kvoli nepresnostiam pri zaokruhlovani pri prevode float na int posledny stlpec a riadok musi vyplnit zvysny priestor.
        rowLastWidth = (int) (mf - 6 * m - 4 * rowWidth);
        rowLastHeight = (int) (height - 2 * rowHeight);


        //vytvara sa mriezka s 8 okienkami
        //************************************************************clanok1***********************************************************
        GridLayout.LayoutParams param1 = new GridLayout.LayoutParams();
        param1.setMargins(m, m, m, m);
        param1.columnSpec = GridLayout.spec(0, 2);
        param1.rowSpec = GridLayout.spec(0, 2);
        param1.width = rowWidth * 2 + m;
        param1.height = rowHeight * 2 + m;

        //************************************************************clanok2*******************************************************
        GridLayout.LayoutParams param2 = new GridLayout.LayoutParams();
        param2.setMargins(0, m, m, m);
        param2.width = rowWidth * 2 + m;
        param2.height = rowHeight;
        param2.columnSpec = GridLayout.spec(2, 2);
        param2.rowSpec = GridLayout.spec(0);
        //************************************************************clanok3*******************************************************
        GridLayout.LayoutParams param3 = new GridLayout.LayoutParams();
        param3.height = LayoutParams.WRAP_CONTENT;
        param3.width = LayoutParams.WRAP_CONTENT;
        param3.setMargins(0, m, m, m);
        param3.width = rowLastWidth;
        param3.height = rowHeight;
        param3.columnSpec = GridLayout.spec(4);
        param3.rowSpec = GridLayout.spec(0);

        //************************************************************clanok4****************************************************************

        GridLayout.LayoutParams param4 = new GridLayout.LayoutParams();
        param4.setMargins(0, 0, m, m);
        param4.width = rowWidth;
        param4.height = rowHeight;
        param4.columnSpec = GridLayout.spec(2);
        param4.rowSpec = GridLayout.spec(1);
        //************************************************************clanok5************************************************************

        GridLayout.LayoutParams param5 = new GridLayout.LayoutParams();
        param5.setMargins(0, 0, m, m);
        param5.width = rowWidth + rowLastWidth + m;
        param5.height = rowHeight;
        param5.columnSpec = GridLayout.spec(3, 2);
        param5.rowSpec = GridLayout.spec(1);
        //************************************************************clanok6************************************************************

        GridLayout.LayoutParams param6 = new GridLayout.LayoutParams();
        param6.setMargins(m, 0, m, m);
        param6.width = rowWidth * 3 + 2 * m;
        param6.height = rowLastHeight;
        param6.columnSpec = GridLayout.spec(0, 3);
        param6.rowSpec = GridLayout.spec(2);
        //************************************************************clanok7************************************************************

        GridLayout.LayoutParams param7 = new GridLayout.LayoutParams();
        param7.setMargins(0, 0, m, m);
        param7.width = rowWidth;
        param7.height = rowLastHeight;
        param7.columnSpec = GridLayout.spec(3);
        param7.rowSpec = GridLayout.spec(2);
        //************************************************************clanok8************************************************************

        GridLayout.LayoutParams param8 = new GridLayout.LayoutParams();
        param8.setMargins(0, 0, m, m);
        param8.width = rowLastWidth;
        param8.height = rowLastHeight;
        param8.columnSpec = GridLayout.spec(4);
        param8.rowSpec = GridLayout.spec(2);

        params = new ArrayList<GridLayout.LayoutParams>();
        params.add(param1);
        params.add(param2);
        params.add(param3);
        params.add(param4);
        params.add(param5);
        params.add(param6);
        params.add(param7);
        params.add(param8);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //System.out.println("ViewGroup_childs:"+container.getChildCount());
        //System.out.println("container:"+container);
        //System.out.println("destroy:"+position);
        container.removeView((View) object);
    }

    //kvoli refreshu musi funkcia vratit position_none, inak by zostali v pamati data a nedalo by sa aktualizovat obsah ViewPagera
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return (store.getCount() + 7) / 8;
    }

    public void setData(ArticleStore store) {
        this.store = store;
        this.loadedStoreItem=store.getNextIndex();
    }

    //jedena strana ViewPagera
    @Override
    public Object instantiateItem(ViewGroup view, int pos) {

        //System.out.println("vytvaram page:"+pos);
        GridLayout g = new GridLayout(mContext);
        g.setColumnCount(column);
        g.setRowCount(row);
        for (int i = 0; i < 8; i++) {
            FrameLayout f1 = null;
            if ((pos * 8 + i) < store.getCount()) {
                if (store.getItem(pos * 8 + i).getImageUrl() == null) {
                    f1 = new ArticleThumbnailFrame(mContext,true);
                    f1.setLayoutParams(params.get(i));
                    g.addView(f1);
                    if ((pos*8+i)==loadedStoreItem){
                        System.out.println("nacitavam dalsich 64 storov");
                        loadedStoreItem+=Constants.LOADED_ARTICLES;
                        /*gridBasic=g;
                        iBasic=i;
                        posBasic=pos;*/
                        // System.out.println("volam dalsich 32 framov");
                        LoadBasic task = new LoadBasic();
                        task.execute(new String[] { "", "", "" });
                    }

                } else {


                    f1 = new ArticleThumbnailFrame(mContext, store.getItem(pos * 8 + i), params.get(i).height);
                    f1.setLayoutParams(params.get(i));
                    g.addView(f1);
                }


            } else {
                // System.out.println("vytvaram prazdny frame");
                f1 = new ArticleThumbnailFrame(mContext);
                f1.setLayoutParams(params.get(i));
                g.addView(f1);
            }
            //System.out.println(g.getChildCount());

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
            GetBasic.getBasic(mContext, store.getNextIndex(), Constants.LOADED_ARTICLES, store.getItems(), store.getIds(), store.getHashmap(), store,"0");
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