package jtb.magazin;

import java.util.List; 

import jtb.magazin.UI.ArticleThumbnailFrame;
import jtb.magazin.adapter.ReadLaterPagerAdapter;

import jtb.magazin.R; 

import org.lucasr.twowayview.TwoWayView;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

@SuppressLint("ValidFragment")
public class FavouritesFragment extends Fragment {
	TwoWayView twitterList;
	ViewPager pager;
	List<String> list;
	Context context;
	public FavouritesFragment(){}
	
	public FavouritesFragment(Context context,List<String> list){
		this.list=list;
 		this.context=context;
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {       

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        View content = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentHeight=content.getHeight();
       //System.out.println(contentHeight);
        DisplayMetrics metrics = getResources().getDisplayMetrics(); 
        float mf= metrics.widthPixels;
		int m=(int) (mf/Constants.MARGIN_WIDTH_KOEF);
        pager = (ViewPager)rootView.findViewById(R.id.fav_pager);
        //nastavi sa adapter
		pager.setAdapter(new ReadLaterPagerAdapter(getActivity(),((MainActivity)getActivity()).options,
				((MainActivity)getActivity()).imageLoader,list,true));
		pager.setCurrentItem(0);
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
