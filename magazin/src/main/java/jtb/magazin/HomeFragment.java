package jtb.magazin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import jtb.magazin.UI.ArticleThumbnailFrame;
import jtb.magazin.adapter.JetPagerAdapter;
import jtb.magazin.adapter.MagnusKlubPagerAdapter;
import jtb.magazin.adapter.ServisPagerAdapter;
import jtb.magazin.adapter.TwitterAdapter;
import jtb.magazin.store.ArticleStore;
import jtb.magazin.store.TwitterStore;

//zakladny fragment ktory je stale na spodnej casti
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    TwoWayView twitterList;
    public ViewPager pager;
    ArticleStore store;
    Context context;
    JetPagerAdapter jetAdapter = null;
    ServisPagerAdapter servisAdapter = null;
    MagnusKlubPagerAdapter klubMagnusAdapter = null;
    boolean isServis;
    TwitterStore twitterStore;
    public LinearLayout twitter_wrapper;

    public JetPagerAdapter getJetAdapter() {
        return jetAdapter;
    }
    public ServisPagerAdapter getServisAdapter() {
        return servisAdapter;
    }
    public MagnusKlubPagerAdapter getMagnusKlubPagerAdapter() {
        return klubMagnusAdapter;
    }

    public HomeFragment() {
    }

    public HomeFragment(ArticleStore store, TwitterStore twitterStore, Context context, boolean isServis) {
        this.store = store;
        this.twitterStore = twitterStore;
        this.context = context;
        this.isServis = isServis;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("vytvaram home fragment");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TwitterAdapter twitterAdapter = new TwitterAdapter(getActivity(), twitterStore);
        twitterList = (TwoWayView) rootView.findViewById(R.id.twitterList);


        //   System.out.println(statusBarHeight);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        float mf = metrics.widthPixels;
        int m = (int) (mf / Constants.MARGIN_WIDTH_KOEF);
        float dx = mf / Constants.WIDTH_KOEF;

        //twitter rozmery
        float itemHeight = ((MainActivity) context).screenHeight / 4 - m;
        int rowWidth = (int) (itemHeight * 50 / 38);
        //oddelenie hlavneho gridu od twittera
        twitter_wrapper = (LinearLayout) rootView.findViewById(R.id.twiter1);
        twitter_wrapper.setPadding(0, 0, 0, m);

        //prve modre okno s logom a textom J&T Banka twitter
        LinearLayout lin2 = (LinearLayout) rootView.findViewById(R.id.twiter2);
        ImageView img = (ImageView) rootView.findViewById(R.id.twiter_img);
        img.getLayoutParams().width = (int) (20 * dx);
        img.getLayoutParams().height = (int) (16.5 * dx);
        TextView text = (TextView) rootView.findViewById(R.id.twitter_feed);
        text.setPadding(0, m, 0, 0);
        text.setText(Html.fromHtml(((MainActivity) context).getString(R.string.twitter_feed)));
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
        text.setTypeface(((MainActivity) context).openSansaLight);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams((int) rowWidth, LayoutParams.MATCH_PARENT);
        param2.setMargins(m, 0, 0, 0);
        lin2.setLayoutParams(param2);

        //nastavenie adaptera pre twitter
        twitterList.setAdapter(twitterAdapter);
        twitterList.setHorizontalScrollBarEnabled(false);

        pager = (ViewPager) rootView.findViewById(R.id.pager);

        // nastavenie adaptera pre hlavny grid
        jetAdapter = new JetPagerAdapter(getActivity(), ((MainActivity) getActivity()).options, ((MainActivity) getActivity()).imageLoader, store,pager);

        //pager.setAdapter(adapter);
        pager.setAdapter(jetAdapter);
        //pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(0);
        pager.setPageMargin(-m); //kvoli tomu ze ked konci strana a zacina dalsia tak pri prechode tam je dvakrat okraj
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            //ak sa prelistuje na dalsiu stranku, vsetky otvorene frames a vysunuty perex ide spat do pociatocneho stavu.
            public void onPageSelected(int position) {
                ((MainActivity) context).actualPos = pager.getCurrentItem();
                ArticleThumbnailFrame openFrame = ((MainActivity) context).openFrame;
                if (openFrame != null) {
                    openFrame.viewPaint.text3.setVisibility(View.GONE);
                    openFrame.black.setVisibility(View.GONE);
                    openFrame.viewPaint.isVisiblePerex = false;
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    openFrame.text2.setLayoutParams(params2);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        ((MainActivity) context).spinner.setVisibility(View.INVISIBLE);

        //ak sa naistaluje aplikacia po prvykrat tak sa spusti tutorial
        if (!((MainActivity) context).visited) {

        } else {
            new Handler().postDelayed(openDrawerRunnable(), 1200);
            //((MainActivity)context).getmDrawerLayout().openDrawer(((MainActivity)context).mDrawerLinear);
        }
        return rootView;
    }

    public void setServisAdaper(ArticleStore store){
        servisAdapter = new ServisPagerAdapter(getActivity(), ((MainActivity) getActivity()).options, ((MainActivity) getActivity()).imageLoader, store);
    }
    public void setMagnusKlubAdaper(ArticleStore store){
        klubMagnusAdapter = new MagnusKlubPagerAdapter(getActivity(), ((MainActivity) getActivity()).options, ((MainActivity) getActivity()).imageLoader, store);
    }

    //po spusteni sa vysunie lave menu
    private Runnable openDrawerRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                ((MainActivity) context).getmDrawerLayout().openDrawer(((MainActivity) context).mDrawerLinear);
            }
        };
    }


}
