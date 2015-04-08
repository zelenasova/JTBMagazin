package jtb.magazin.UI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

//ak sa uzivatel pokusi vytiahnut lave menu tak je nastavena ochrana zona pre ViePager aby zastavila scroll na okraji
public class MyViewPager extends ViewPager {
	
	public boolean scrollingEnabled = true;
	int screenWidth;
	int screenHeight;

    public MyViewPager(Context context, AttributeSet attrs) { 
        super(context, attrs);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
    }
    
  
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	//if (scrollingEnabled)
    		//super.onInterceptTouchEvent(ev);
    	
    	return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        System.out.println(ev.getX());
    	if (ev.getX()<screenWidth/10) return false;
    	else return super.onTouchEvent(ev);

    	
    	//return super.onTouchEvent(ev);
    }
}
