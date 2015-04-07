package jtb.magazin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.Locale;


//pri spusteni aplikacie sa najprv spusta tato trieda na 4 s sa ukaze obrazovka, objavi sa logo J&T banky a zmizne a spusti sa MainActivity
public class Splash extends Activity {
	ImageView logo;
	int screenWidth;
	int screenHeight;
	String savedLang="";
	SharedPreferences prefs;
    //private final int SPLASH_DISPLAY_LENGHT = 4000;            //cas animacie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
        /*logo = (ImageView) findViewById(R.id.launch_logo);
        logo.getLayoutParams().width=screenWidth/5;
        logo.getLayoutParams().height=screenWidth*14/300;*/
        
        
        /*// animacia
        move();
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // zistujeme jazyk systemu v tablete
            	//String currentLang = Locale.getDefault().getLanguage();
            	
            	// ak jazyk systemu nie je ani slovensky ani cesky tak vybehne menu pre vybratie jazyka (R.layout.select_language)
        	//	if (!((currentLang.equals("sk"))||(currentLang.equals("cs")))){*/
        			
    			savedLang = prefs.getString("savedLang", ""); 
    			if (!((savedLang.equals("sk"))||(savedLang.equals("cs")))){    	   	
        			//logo.setVisibility(View.GONE);
        			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        			LinearLayout language = (LinearLayout) findViewById(R.id.language);
        			View v = inflater.inflate(R.layout.select_language, language,false);
        			language.addView(v);
        			language.setBackgroundColor(Color.parseColor("#CC6A6A6A"));
        			LinearLayout languageWrapper = (LinearLayout) findViewById(R.id.lang);
        			//languageWrapper.setBackgroundColor(Color.parseColor("#000000"));
        			LinearLayout.LayoutParams languageWrapperParams = (LayoutParams) languageWrapper.getLayoutParams();
        			languageWrapperParams.width=(int) (screenWidth/3);
        			languageWrapperParams.height=(int) (screenHeight/5);
        			View sk=languageWrapper.getChildAt(0);
        			View cz=languageWrapper.getChildAt(1);
        			sk.setOnClickListener(new View.OnClickListener() 
        	        {
        	            @Override
        	            public void onClick(View v)
        	            {  // najprv sa nastavi jazyk aplikacie a potom sa prejde na hlavne okno MainActivity 
        	            	prefs.edit().putString("savedLang","sk").commit();
        	            	Locale.setDefault(new Locale("sk"));
        	            	Configuration config = new android.content.res.Configuration();
        	            	config.locale = new Locale("sk");
        	            	getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics()); 	
        	            	 Intent mainIntent = new Intent(Splash.this,MainActivity.class);
        	                 Splash.this.startActivity(mainIntent);
        	                 Splash.this.finish();
        	            }
        	        });
        			cz.setOnClickListener(new View.OnClickListener() 
        	        {
        	            @Override
        	            public void onClick(View v)
        	            {     
        	            	prefs.edit().putString("savedLang","cs").commit();
        	            	Locale.setDefault(new Locale("cs"));
        	            	Configuration config = new android.content.res.Configuration();
        	            	config.locale = new Locale("cs");
        	            	getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics()); 	        
        	            	 Intent mainIntent = new Intent(Splash.this,MainActivity.class);
        	                 Splash.this.startActivity(mainIntent);
        	                 Splash.this.finish();
;
        	            //	lang="cz";
        	            	
        	            }
        	        });
        		} else {
        			
                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
        		}
            }
      /* *//* }, SPLASH_DISPLAY_LENGHT);*//*
    }*/
    
    public void move() {
    	Animation fadeIn = new AlphaAnimation(0, 1);
    	fadeIn.setStartOffset(1000);
    	fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
    	fadeIn.setDuration(1000);

    	Animation fadeOut = new AlphaAnimation(1, 0);
    	fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
    	fadeOut.setStartOffset(3000);
    	fadeOut.setDuration(1000);

    	AnimationSet animation = new AnimationSet(false); //change to false
    	animation.addAnimation(fadeIn);
    	animation.addAnimation(fadeOut);
    	logo.setAnimation(animation);
		logo.startAnimation(animation);
		logo.setVisibility(View.INVISIBLE);
		}
}

