package jtb.magazin;


import jtb.magazin.UI.ArticleThumbnailFrame; 
import jtb.magazin.adapter.KeywordsPagerAdapter;
import jtb.magazin.adapter.SearchPagerAdapter;
import jtb.magazin.store.SearchArticleStore;

import org.lucasr.twowayview.TwoWayView;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

//zobrazenie vysledkov vyhladavania/vyhladavania podla keywords po kliku na text v krystali suvislosti
//na zaklade parametra isKeywordID
@SuppressLint("ValidFragment")
public class SearchFragment extends Fragment {
	TwoWayView twitterList;
	ViewPager pager;
	String searchText;
	String keywordID;
	boolean isKeywordID=false;
	Context context;
	SearchArticleStore store;
	public SearchFragment(){}
	
	public SearchFragment(Context context, SearchArticleStore store){
		this.context=context;
		this.store=store;
	}
	
	public SearchFragment(Context context, String keywordID, boolean isKeywordID){
		this.keywordID=keywordID;
		this.isKeywordID=isKeywordID;
		this.context=context;
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {       
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        

        float mf= ((MainActivity)context).screenWidth;
		int m=(int) (mf/Constants.MARGIN_WIDTH_KOEF);
        pager = (ViewPager)rootView.findViewById(R.id.fav_pager);
        //z krystalu suvislosti
        if (isKeywordID){     	
     		pager.setAdapter(new KeywordsPagerAdapter((MainActivity)getActivity(),keywordID));
     		pager.setCurrentItem(0);
        	
        } else {
        //vysledky vyhladavania
		pager.setAdapter(new SearchPagerAdapter(context,((MainActivity)getActivity()).options,((MainActivity)getActivity()).imageLoader,store));
		pager.setCurrentItem(0);
        }
        pager.setPageMargin(-m);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			//ak sa prejde na dalsiu stranku tak clanok ktory ma vysunuty perex sa musi dat do povodneho stavu 
	         @Override
	         public void onPageSelected(int position)
	         {
	        	 ArticleThumbnailFrame openFrame=((MainActivity)context).openFrame;
	        	 if (openFrame!=null){
	        	openFrame.viewPaint.text3.setVisibility(View.GONE);
	        	openFrame.black.setVisibility(View.GONE);
	        	openFrame.viewPaint.isVisiblePerex=false;
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				openFrame.text2.setLayoutParams(params2);
	        	 }
	         }

	         @Override
	         public void onPageScrolled(int arg0, float arg1, int arg2) {}
	         @Override
	         public void onPageScrollStateChanged(int arg0) {}
	      });
		((MainActivity)context).spinner.setVisibility(View.INVISIBLE); 
        return rootView;
    }
	
	
}
