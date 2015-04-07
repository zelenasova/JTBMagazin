package jtb.magazin;

import jtb.magazin.R; 

import java.io.File;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//zobrazi sa tutorial ako vrstva ponad pomocou nacitania tutorialu do webView
@SuppressLint({ "ValidFragment", "SetJavaScriptEnabled" })
public class TutorialFragment extends Fragment {		
	
	Context context;
  
    public TutorialFragment(Context context) { 
		
    	this.context=context;
	}
    

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        
        RelativeLayout  tutorial = (RelativeLayout) rootView.findViewById(R.id.tutorial);
		//tutorial.setClickable(true);
		
        double w= ((MainActivity) context).screenWidth;
		double dx = (int) (w/Constants.WIDTH_KOEF);
		
		//zatvorenie tutorialu
		ImageView close = new ImageView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(60*dx),(int)(60*dx));
		close.setLayoutParams(params);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		close.setBackgroundResource(R.drawable.close_wot);
		params.setMargins(0, (int)(20*dx), (int)(20*dx), 0);
		tutorial.addView(close);
		
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {							
				getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(0,R.animator.translate_x ).remove(TutorialFragment.this).commit();
				new Handler().postDelayed(openDrawerRunnable(), 1200);
			}
		});
		
        
        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (((MainActivity)context).lang.equals("sk")) {
        	//webView.loadUrl("file:///android_asset/tutorial/index.html");
            File tut_file = new File(((MainActivity)context).getExternalCacheDir ().toString() + "/"+Constants.STORAGE_FILE+"/articles/tutorial/Tutorial_sk/index.html");
            webView.loadUrl("file:///" + tut_file.getAbsolutePath());
        } else {
            File tut_file = new File(((MainActivity)context).getExternalCacheDir ().toString() + "/"+Constants.STORAGE_FILE+"/articles/tutorial/Tutorial_cs/index.html");
            webView.loadUrl("file:///" + tut_file.getAbsolutePath());
        }
	
        return rootView;
    }
	

	//po zatvoreni sa vysunie lave menu
		private Runnable openDrawerRunnable() {
		    return new Runnable() {

		        @Override
		        public void run() {
		        	((MainActivity)context).getmDrawerLayout().openDrawer(((MainActivity)context).mDrawerLinear);
		        }
		    };
		}
	
}
