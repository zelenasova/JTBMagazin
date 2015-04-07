package jtb.magazin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import jtb.magazin.magnus.MagnusArticleFragment;
import jtb.magazin.magnus.MagnusListFragment;
import jtb.magazin.model.MagnusListModel;
import jtb.magazin.store.MagnusListStore;

//magnus fragment obsahuje lave menu a hlavnu cast, ktory tvori frameLayout a do ktoreho sa nacitava najprv zoznam magazinov, a po vybere magazinu
//vrstva ponad kde sa zobrazia clanky zvoleneho magazinu. Child fragmenty komunikuju stale s touto triedou, kde sa vytvara zakladny zoznam, aj clanky
//zvoleneho magazinu

@SuppressLint({ "ValidFragment", "SetJavaScriptEnabled" })
public class MagnusFragment extends Fragment {		
	
    SharedPreferences prefs;
    Context context;
    int mFrameHeight;
    RelativeLayout mFrame;
    public RelativeLayout bottom_menu;
    TextView noble;
    TextView back;
    TextView magazinId;
    ImageView imageM;
    ImageView back_img;
    float screenWidth;
	float screenHeight;
	LinearLayout wrapper_article;
	LinearLayout back_wrapper;
	FragmentManager ft;
	android.support.v4.app.FragmentTransaction transaction;
	private MagnusListStore magnusList;
	float m;
	boolean fromRelated = false;
	MagnusListModel model;
  
    public MagnusFragment(SharedPreferences prefs,Context context) { 
		this.prefs=prefs;
		this.context=context;
	}
    
    public MagnusFragment(SharedPreferences prefs,Context context,MagnusListModel model, boolean fromRelated) { 
    	this(prefs,context);
		this.fromRelated=fromRelated;
		this.model=model;				
	}
    

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_magnus, container, false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		m = screenWidth/Constants.MAGNUS_MARGIN_WIDTH_KOEF;
		//obrazok velke M v menu
        imageM = (ImageView) rootView.findViewById(R.id.imageM);
        mFrame = (RelativeLayout) rootView.findViewById(R.id.magnus_menu1);
        
        //do spodneho menu sa pri vybere magazinu naplnia polozky clankami
        bottom_menu = (RelativeLayout) rootView.findViewById(R.id.magnus_menu3);
        wrapper_article = (LinearLayout) rootView.findViewById(R.id.magnus_menu_article);
        wrapper_article.setClickable(true);
        wrapper_article.setPadding((int)(screenWidth/35), 0, (int) (screenWidth/70), 0);
        
        //v hornej casti sa zobrazuje text a obrazok back, po stlaceni sa da dostat na zoznam magazinov
        back = (TextView) rootView.findViewById(R.id.magnus_back);
        back_wrapper = (LinearLayout) rootView.findViewById(R.id.magnus_back_wrapper);
        back_img = (ImageView) rootView.findViewById(R.id.magnus_back_img);
        back.setTypeface(((MainActivity) context).giorgioSansWebRegular);
        back.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size22));
        back.setText(getResources().getString(R.string.spat));
        back_img.getLayoutParams().height=(int) (1.25*m);
        back_img.getLayoutParams().width=(int) (1.25*m/44*28);
        back.setPadding((int) (0.5*m), 0, 0, 0);
        
        //text v menu
        noble = (TextView) rootView.findViewById(R.id.noble);
        noble.setText(Html.fromHtml(getString(R.string.noble)));
        noble.setTypeface(((MainActivity) context).giorgioSansWebRegular);
        noble.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size20));
      //cislo zvoleneho magazinu v menu
        magazinId = (TextView) rootView.findViewById(R.id.magnus_menu2);
        magazinId.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size80));
        magazinId.setTypeface(((MainActivity) context).giorgioSansWebBold);
        
        
        

        ViewTreeObserver vto =mFrame.getViewTreeObserver();
		 vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		        @Override 
		        public void onGlobalLayout() { 		         
		        	MagnusFragment.this.mFrameHeight=mFrame.getHeight();		 
		            mFrame.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		            
		        } 
		    }); 
		 
		 ViewTreeObserver vto2 =noble.getViewTreeObserver();
		 vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		        @Override 
		        public void onGlobalLayout() { 		         
		        	int nobleHeight = noble.getHeight();		 
		        	 RelativeLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,200);
		        	 layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		        	 layoutParams.width=(mFrameHeight-nobleHeight-nobleHeight/8)/16*10;
	                 layoutParams.height = mFrameHeight-nobleHeight-nobleHeight/8;
	                 imageM.setLayoutParams(layoutParams);
	                 noble.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		            
		        } 
		    }); 
		 
		 
		 magnusList =  ((MainActivity)context).magnusList;
		 
		 ft = getChildFragmentManager();
		 //zobrazi zoznam magazinov
		 displayMagazinList();
		
		 back_wrapper.setVisibility(View.INVISIBLE);
		 ((MainActivity)context).spinner.setVisibility(View.INVISIBLE);
		 back_wrapper.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeMagazinObsah();
				}
			});
		 if (fromRelated) {
			 displayMagazinObsah(model.getId(), model.getLastChange(), model.getTitle(), model.getSubTitle());
			 fromRelated=false;
		 }
	
        return rootView;
    }
	//ak je zvoleny magazin tak touto funkciou sa dostaneme spat na zoznam magazinov
	public void removeMagazinObsah(){
		((MainActivity)context).actualView=1;
		if (((MainActivity)context).obsah!=null){
			//noble.setText(Html.fromHtml(getString(R.string.noble_archiv)));
			magazinId.setText("");
			bottom_menu.removeAllViews();
			bottom_menu.setVisibility(View.INVISIBLE);
			back_wrapper.setVisibility(View.INVISIBLE);
		transaction = ft.beginTransaction();
		transaction.remove(((MainActivity)context).obsah);
		transaction.commit();
            ((MainActivity)context).obsah=null;
            System.out.println("fargment obsah: "+((MainActivity)context).obsah);
		if (((MainActivity)context).detail==null){
		((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,((MainActivity)context).getmDrawerRight());
		}
		}
	}
	
	//pre zobrazenie clankov magazina sa vola nasledujuca funkkcia
	public void displayMagazinObsah(String id, String magazinLastChange, String title, String subTitle){
		((MainActivity)context).actualView=2;
		((MainActivity)context).actualMagazinID=id;
		((MainActivity)context).actualMagazinTitle=title;
		((MainActivity)context).actualMagazinSubTitle=subTitle;
		((MainActivity)context).actualMagazinLastChange=magazinLastChange;
        ((MainActivity)context).obsah = new MagnusArticleFragment(context,id,magazinLastChange);
		magazinId.setText(title);
	//	noble.setText(Html.fromHtml(getString(R.string.noble)));
		transaction = ft.beginTransaction();
		transaction.add(R.id.nested_fragment, ((MainActivity)context).obsah);
		transaction.commit();
		bottom_menu.setVisibility(View.VISIBLE);
		back.setText(getResources().getString(R.string.spat));
		back_wrapper.setVisibility(View.VISIBLE);
		//((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,((MainActivity)context).getmDrawerRight());
	}
	 //zobrazi zoznam magazinov
	public void displayMagazinList(){
        System.out.println("fargment obsah: "+((MainActivity)context).obsah);
		android.support.v4.app.Fragment list = new MagnusListFragment(context,magnusList);
		transaction = ft.beginTransaction();
		transaction.add(R.id.nested_fragment, list);
		transaction.commit();
		//((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,((MainActivity)context).getmDrawerRight());
		
	}
	
	
}
