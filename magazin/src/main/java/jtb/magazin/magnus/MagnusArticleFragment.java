package jtb.magazin.magnus;


import java.util.ArrayList;

import jtb.magazin.adapter.JetPagerAdapter;
import jtb.magazin.store.MagnusArticleStore;
import jtb.magazin.Constants;
import jtb.magazin.MagnusFragment;
import jtb.magazin.MainActivity;
import jtb.magazin.R; 

import org.lucasr.twowayview.TwoWayView;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

//zobrazuje grid s clankami pre vybrany magazin a bocne spodne menu sa naplni polozkami ktore tiez predstavuju clanky
@SuppressLint("ValidFragment")
public class MagnusArticleFragment extends Fragment {
	TwoWayView twitterList;
	ViewPager pager;
	Context context;
	String magazinId;
	MagnusArticleStore mas;
	JetPagerAdapter adapter=null;
	float screenWidth;
	float screenHeight;
	 RelativeLayout bottom_menu;
	String magazinLastChange;
	public JetPagerAdapter getAdapter() {
		return adapter;
	}
	
	public MagnusArticleFragment(Context context, String magazinId, String magazinLastChange){
		this.context=context;
		this.magazinId=magazinId;
		this.magazinLastChange=magazinLastChange;
	}


	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 		
        View rootView = inflater.inflate(R.layout.fragment_magnus_article, container, false);
        //System.out.println(magazinId);
        mas = new MagnusArticleStore(context, magazinId);
        MagnusFragment mf = (MagnusFragment) getParentFragment();

		screenWidth = ((MainActivity)context).screenWidth;
		screenHeight = ((MainActivity)context).screenHeight;
		float dx=screenWidth/Constants.WIDTH_KOEF;
		 int fragmentHeight = (int) (screenHeight*84/100);
	        int fragmentWidth = (int) (screenWidth*4/5);
	        
	        int m = (int) (screenWidth/Constants.MAGNUS_ARTICLE_MARGIN_WIDTH_KOEF);
	        
		//*******************************************************dolne menu**************************************************************************
	    //mame priestor pre maximalne 5 poloziek
	    int pocetPoloziek = mas.getLinksItems().size();
	    if (pocetPoloziek>0){
	    int divider = (int) (2*dx);
		bottom_menu=mf.bottom_menu;
		int bottomMenuHeight = bottom_menu.getHeight();
		if (bottomMenuHeight==0) bottomMenuHeight= (int) (((MainActivity)context).screenHeight*31080/100000); //84/100*370/1000
		float vyskaPoloziek = bottomMenuHeight-6*divider;
		int vyskaPolozky = ((int) (vyskaPoloziek/5));
		TextView text1 = new TextView(context);TextView text2 = new TextView(context);TextView text3 = new TextView(context);
		TextView text4 = new TextView(context);TextView text5 = new TextView(context);
		RelativeLayout.LayoutParams text1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, vyskaPolozky);
		RelativeLayout.LayoutParams text2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, vyskaPolozky);
		RelativeLayout.LayoutParams text3Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, vyskaPolozky);
		RelativeLayout.LayoutParams text4Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, vyskaPolozky);
		RelativeLayout.LayoutParams text5Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, vyskaPolozky); 
    	text1.setLayoutParams(text1Params);text2.setLayoutParams(text2Params);text3.setLayoutParams(text3Params);
    	text4.setLayoutParams(text4Params);text5.setLayoutParams(text5Params);
    	ArrayList<TextView> texts = new ArrayList<TextView>();
    	texts.add(text1);texts.add(text2);texts.add(text3);texts.add(text4);texts.add(text5);
		text1.setId(2001);text2.setId(2002);text3.setId(2003);text4.setId(2004);text5.setId(2005);
		
		View v1 = new View(context);View v2 = new View(context);View v3 = new View(context);View v4 = new View(context);
		View v5 = new View(context);View v6 = new View(context);
        RelativeLayout.LayoutParams v1Params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, divider);
        RelativeLayout.LayoutParams v2Params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, divider);
        RelativeLayout.LayoutParams v3Params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, divider);
        RelativeLayout.LayoutParams v4Params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, divider);
        RelativeLayout.LayoutParams v5Params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, divider);
        RelativeLayout.LayoutParams v6Params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, divider);
        v1.setBackgroundColor(Color.BLACK);v2.setBackgroundColor(Color.BLACK); v3.setBackgroundColor(Color.BLACK);
        v4.setBackgroundColor(Color.BLACK); v5.setBackgroundColor(Color.BLACK);v6.setBackgroundColor(Color.BLACK);
        v1.setLayoutParams(v1Params);v2.setLayoutParams(v2Params);v3.setLayoutParams(v3Params);v4.setLayoutParams(v4Params);
        v5.setLayoutParams(v5Params);v6.setLayoutParams(v6Params);
        v1.setId(3001);v2.setId(3002);v3.setId(3003);v4.setId(3004);v5.setId(3005);v6.setId(3006);
        ArrayList<View> views = new ArrayList<View>();
        views.add(v2);views.add(v3);views.add(v4);views.add(v5);views.add(v6);
		
        v1Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        text1Params.addRule(RelativeLayout.ABOVE, v1.getId());
        v2Params.addRule(RelativeLayout.ABOVE, text1.getId());
        text2Params.addRule(RelativeLayout.ABOVE, v2.getId());
        v3Params.addRule(RelativeLayout.ABOVE, text2.getId());
        text3Params.addRule(RelativeLayout.ABOVE, v3.getId());
        v4Params.addRule(RelativeLayout.ABOVE, text3.getId());
        text4Params.addRule(RelativeLayout.ABOVE, v4.getId());
        v5Params.addRule(RelativeLayout.ABOVE, text4.getId());
        text5Params.addRule(RelativeLayout.ABOVE, v5.getId());
        v6Params.addRule(RelativeLayout.ABOVE, text5.getId());
		//params2.addRule(RelativeLayout.ABOVE, text3.getId());
        
        bottom_menu.addView(v1);
        for (int i=0;i<pocetPoloziek;i++){
        	//max 5 poloziek
        	if (i>4)break;
        	final int k=pocetPoloziek-1-i;
        	texts.get(i).setText( mas.getLinksItems().get(pocetPoloziek-1-i).getTitle());
        	texts.get(i).setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
        	texts.get(i).setTextColor(Color.BLACK);
        	texts.get(i).setTypeface(((MainActivity) context).giorgioSansWebBold);
        	texts.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size18));
        	
        	bottom_menu.addView(texts.get(i));
        	bottom_menu.addView(views.get(i));
        	// po kliku na menu sa spusti detail clanku
        	texts.get(i).setOnClickListener(new View.OnClickListener() 
            {
                @Override
                public void onClick(View v)
                {	
               if ((!((MainActivity)context).logged)&&(mas.getLinksItems().get(k).getLocked().equals("1"))){
						 ((MainActivity)context).menuLayout.displayLogin(false,0,0);
               } else ((MainActivity)getActivity()).displayDetail(mas.getLinksItems().get(k));
                	//System.out.println(k);
                }
            });
        }
        }	
		
		//*******************************************************koniec dolne menu**************************************************************************
        
	    //grid a naplnenie gridu clankami
		LinearLayout wrapper = (LinearLayout) rootView.findViewById(R.id.magnus_article);
       
       
        int column = 4;
        int row = 3;
        int rowWidth= (fragmentWidth-5*m)/column;
        int rowHeight = (fragmentHeight-2*m)/row;
        int rowLastWidth = (int) (fragmentWidth-5*m-3*rowWidth);
        wrapper.setPadding(m, 0, 0, 0);
        //iterujeme pocet stran
 
        for(int pos =0;pos<((mas.getCount()+11)/12);pos++){
        	
        	GridLayout g = new GridLayout(context);    		
             g.setColumnCount(column);
             g.setRowCount(row);    
            
                 GridLayout.LayoutParams param1 =new GridLayout.LayoutParams();
                 param1.setMargins(0, 0, m, 0);            
                 param1.width = rowWidth;
                 param1.height=rowHeight;    
                
                 GridLayout.LayoutParams param2 =new GridLayout.LayoutParams();     
                 param2.setMargins(0, 0, m, 0);           
                 param2.width = rowWidth;
                 param2.height=rowHeight;
              
                 GridLayout.LayoutParams param3 =new GridLayout.LayoutParams();
                 param3.setMargins(0, 0, m, 0);  
                 param3.width = rowWidth;
                 param3.height=rowHeight;       
                
                 GridLayout.LayoutParams param4 =new GridLayout.LayoutParams();
                 param4.setMargins(0, 0, m, 0); 
                 param4.width = rowLastWidth;
                 param4.height=rowHeight;
                
                 GridLayout.LayoutParams param5 =new GridLayout.LayoutParams();
                 param5.setMargins(0, m, m, 0); 
                 param5.width = rowWidth;
                 param5.height=rowHeight;
                 
                 GridLayout.LayoutParams param6 =new GridLayout.LayoutParams();
                 param6.setMargins(0, m, m, 0);  
                 param6.width = rowWidth;
                 param6.height=rowHeight;
                 
                 GridLayout.LayoutParams param7 =new GridLayout.LayoutParams();
                 param7.setMargins(0, m, m, 0);  
                 param7.width = rowWidth;
                 param7.height=rowHeight;
                 
                 GridLayout.LayoutParams param8 =new GridLayout.LayoutParams();
                 param8.setMargins(0, m, m, 0);  
                 param8.width = rowLastWidth;
                 param8.height=rowHeight;

                 
                 GridLayout.LayoutParams param9 =new GridLayout.LayoutParams();
                 param9.setMargins(0, m, m, 0);  
                 param9.width = rowWidth;
                 param9.height=rowHeight;
                 
                 GridLayout.LayoutParams param10 =new GridLayout.LayoutParams();
                 param10.setMargins(0, m, m, 0);  
                 param10.width = rowWidth;
                 param10.height=rowHeight;
                 
                 GridLayout.LayoutParams param11 =new GridLayout.LayoutParams();
                 param11.setMargins(0, m, m, 0);            
                 param11.width = rowWidth;
                 param11.height=rowHeight;    
                
                 GridLayout.LayoutParams param12 =new GridLayout.LayoutParams();     
                 param12.setMargins(0, m, m, 0);           
                 param12.width = rowLastWidth;
                 param12.height=rowHeight;
              
              
                 
                 ArrayList<GridLayout.LayoutParams> params = new ArrayList<GridLayout.LayoutParams>();
                 params.add(param1);params.add(param2);params.add(param3);params.add(param4);params.add(param5);
                 params.add(param6);params.add(param7);params.add(param8);params.add(param9);params.add(param10);
                 params.add(param11);params.add(param12);
                
                 
                 for(int j=0;j<12;j++){  
                	 FrameLayout f1=null;
                	 
                	 
                	 if ((pos*12+j) < mas.getCount()){
                		 //vytvara sa frame pre jeden clanok
                	 f1 = new MagnusArticleFrame(context,mas.getItem(pos*12+j),magazinLastChange);           	 
                	 f1.setLayoutParams (params.get(j));           	 
                     g.addView(f1);
                	 } /*else  {
                		 f1 = new MagnusArticleFrame(context,mas.getItem(pos*20+j),this);
                	 f1.setLayoutParams (params.get(j));
                	 g.addView(f1);}*/
                	
                }
   
        wrapper.addView(g);
        }
        return rootView;
    }
	
	private void destroyFragment() {
		getChildFragmentManager().beginTransaction().hide(this).commit();
	}
	
	
}
