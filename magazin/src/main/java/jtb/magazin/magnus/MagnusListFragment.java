package jtb.magazin.magnus;

 import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.UI.CustomHorizontalScrollView;
import jtb.magazin.store.MagnusListStore;

@SuppressLint("ValidFragment")

//tato trieda je child Fragment k zakladnemu fragmentu ktory predstavuje trieda MagnusFragment
//v MagnusFragment sa vytvara hlavne okno ktore tvory lave menu a samotny obsah ktory tvori FrameLayout do ktoreho sa vkladaju Child Fragmenty 
//ako je tento a fragment pre zobrazenie clankov jedneho magazinu - MagnusArticleFragment
//
public class MagnusListFragment extends android.support.v4.app.Fragment {

	Context context;	
	float screenWidth;
	float screenHeight;
	MagnusListStore magnusList;
	
	public MagnusListFragment(Context context, MagnusListStore magnusList){
		this.context=context;
		this.magnusList=magnusList;
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 
		screenWidth = ((MainActivity)context).screenWidth;
		screenHeight = ((MainActivity)context).screenHeight;
		float dx =  screenWidth/Constants.WIDTH_KOEF;
		//layout tvori jednoduchy srcollbar v ktorom je linearLayout magnus_list, do ktoreho sa popridavaju vsetky magaziny
        View rootView = inflater.inflate(R.layout.fragment_magnus_list, container, false);
        LinearLayout wrapper = (LinearLayout) rootView.findViewById(R.id.magnus_list);
        
        float fragmentHeight = screenHeight*84/100;
        float fragmentWidth = screenWidth*4/5;

        float m = screenWidth/Constants.MAGNUS_MARGIN_WIDTH_KOEF;    
        //vyska a sirka jedneho magazinu bez margins
        float h = (fragmentHeight-m)/2;
        float w= h/330*260;
        int itemWrapperWidth = (int) (fragmentWidth/3);
        int rightMargin=itemWrapperWidth-(int) w-(int) m;
        
        CustomHorizontalScrollView  customScrollView = (CustomHorizontalScrollView) rootView.findViewById(R.id.customScrollView);
        customScrollView.setFeatureItems(itemWrapperWidth);
      //  wrapper.setPadding((int) m, 0, 0, 0);
    //    System.out.println(magnusList.getItem(0).);
      //  rozlisujeme ci mame parny pocet magazinov a pridavame magaziny po stlpcoch, najnovsi je prvy
       if((magnusList.getCount()%2)==0){
        	
	        for(int i =0;i<=((magnusList.getCount()+1)/2)-1;i++){
		        LinearLayout column = new LinearLayout(context);
		        column.setOrientation(LinearLayout.VERTICAL);
		        LinearLayout.LayoutParams column_params = new LinearLayout.LayoutParams((int) w,LinearLayout.LayoutParams.MATCH_PARENT);
		        column.setLayoutParams(column_params);
		        column_params.setMargins((int) m, 0, (int) rightMargin, 0);
		        LinearLayout.LayoutParams frame_params1 = new LinearLayout.LayoutParams((int)w,(int)h);
		        LinearLayout.LayoutParams frame_params2 = new LinearLayout.LayoutParams((int)w,(int)h);
		        frame_params1.setMargins(0, 0, 0, (int) m);
		        if ((i*2)<magnusList.getCount()) column.addView(new MagnusListFrame(context,magnusList.getItem(i*2),this,w,h),frame_params1); 
		        if ((i*2+1)<magnusList.getCount()) column.addView(new MagnusListFrame(context,magnusList.getItem(i*2+1),this,w,h),frame_params2);
		        wrapper.addView(column);

            }
	        
        } else { //ak je neparny pocet      
        		 for(int i =0;i<=((magnusList.getCount()+1)/2)-2;i++){  
        	        LinearLayout column = new LinearLayout(context);
        	        column.setOrientation(LinearLayout.VERTICAL);
        	        LinearLayout.LayoutParams column_params = new LinearLayout.LayoutParams((int) w,LinearLayout.LayoutParams.MATCH_PARENT);
        	        column.setLayoutParams(column_params);
        	        column_params.setMargins((int) m, 0, (int) rightMargin, 0);
        	        LinearLayout.LayoutParams frame_params1 = new LinearLayout.LayoutParams((int)w,(int)h);
        	        LinearLayout.LayoutParams frame_params2 = new LinearLayout.LayoutParams((int)w,(int)h);
        	        frame_params1.setMargins(0, 0, 0, (int) m);
        	        if ((i*2+0)<magnusList.getCount()) column.addView(new MagnusListFrame(context,magnusList.getItem(i*2+0),this,w,h),frame_params1);
        	        if ((i*2+1)<magnusList.getCount()) column.addView(new MagnusListFrame(context,magnusList.getItem(i*2+1),this,w,h),frame_params2);       	               
        	        wrapper.addView(column);      
        	        }
        	LinearLayout column = new LinearLayout(context);
 	        column.setOrientation(LinearLayout.VERTICAL);
 	        LinearLayout.LayoutParams column_params = new LinearLayout.LayoutParams((int) w,LinearLayout.LayoutParams.MATCH_PARENT);
 	        column.setLayoutParams(column_params);
 	        column_params.setMargins(0, 0, (int) (w/2+2*dx), 0);
 	        LinearLayout.LayoutParams frame_params1 = new LinearLayout.LayoutParams((int)w,(int)h);
 	        frame_params1.setMargins(0, 0, 0, (int) m);
 	        column.addView(new MagnusListFrame(context,magnusList.getItem(magnusList.getCount()-1),this,w,h),frame_params1);     
 	        wrapper.addView(column);      
        }
        
        
        rootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// drop the view
				destroyFragment();
			}
		});
	
        return rootView;
    }
	
	private void destroyFragment() {
		getChildFragmentManager().beginTransaction().hide(this).commit();
	}
	
	
}
