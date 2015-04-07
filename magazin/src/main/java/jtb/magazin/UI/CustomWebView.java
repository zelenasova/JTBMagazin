package jtb.magazin.UI;


import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import java.util.List;

import jtb.magazin.MainActivity;
import jtb.magazin.model.ArticleModel;

//webView predstavuje detail clanku, do tohto webView sa nacita obsah html,css a javascript. WebView je potrebne rozsirit kvoli
//osetreniu klikov
public class CustomWebView extends WebView implements OnGestureListener {

	Context context;
	Boolean isBottomFirst=false;
	private GestureDetector gestureDetector;
	float startX;
	float startY;
	Boolean enableHorizontal=false;
	Boolean startScrolling = true;
	int statusBarHeight;
	String [] related;
	boolean isLaunch=false;
	String id;
	//ImageView next_article;
	//ImageView prev_article;
	int screenWidth;
	int previousPos;
	int nextPos;
	int maxPos;
	String source;
	ArticleModel model;
	int height;
	int webViewHeight;
	int treshold;
	int treshold2;
	boolean loading_complet=false;
	RelativeLayout rel;
	boolean isEndInTouch = false;
    int screenHeight;
    int activePage;
    OverScroller scroller;
    int action_down_scrollY=0;

	//OverScroller scroller;
	
   
    //***********************************magnus konstruktor*****************************************
    public CustomWebView(Context context, ArticleModel model,RelativeLayout rel) {
		super(context);
		this.context=context;
		this.source=model.getSource();
		this.id=model.getId();
		//this.next_article=next_article;
    	//this.prev_article=prev_article;
		init();
		this.setVerticalScrollBarEnabled(false);
		previousPos=model.getPos()-1;
		nextPos=model.getPos()+1;
		maxPos=((MainActivity)context).currentItems.size()-1;
		this.model=model;
		this.rel=rel;
		treshold2=(int) dipToPixels(context, 50);
        scroller=new OverScroller(context);
	
		  
	//	scroller = new OverScroller(context);
		
	}
    
    public void init() {
        gestureDetector = new GestureDetector(this.getContext(), this);
		this.statusBarHeight=((MainActivity)context).getStatusBarHeight();
		this.screenWidth=(int) ((MainActivity)context).screenWidth;
        this.screenHeight=(int) ((MainActivity)context).screenHeight;
		treshold=(int) (5000/((MainActivity)context).screenWidth);
    }
    
   /* @Override
	public void loadUrl(String url)
	{
		System.out.println("+++++WebView loadUrl:" +url);
		super.loadUrl(url);
	
	}
	@Override
	public void  postUrl  (String  url, byte[] postData)
	{
		System.out.println("+++++++WebView postUrl:" +url);
		super.postUrl(url, postData);
	}*/

    
   
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("unused")
    private void setLeftMargin(int value) {
        RelativeLayout.LayoutParams par = (android.widget.RelativeLayout.LayoutParams)this.getLayoutParams();
        par.leftMargin=value;
        par.setMarginStart(value);
     //  this.requestLayout();
    }
//zistujeme ci sa dosledkom srolovania dosiahol dolny okraj webView, ak sa dosiahol druhykrat, vysunie sa prave menu
	@Override  
	public void  onScrollChanged (int l, int t, int oldl, int oldt){  
	    if (loading_complet){
            if(this.getScrollY() + webViewHeight >= height){
                //System.out.println("THE END reached");
                isBottomFirst=!isBottomFirst;
                if (!isBottomFirst) {
                    ((MainActivity) context).getmDrawerLayout().openDrawer(Gravity.END);
                }
            }
        }

	}
	//rozlisujeme horizontalny a vertikalny pohyb prstom. Ak sa hybeme horizontalne, presuva sa na dalsi clanok
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		//na zaciatku treba vypocitat vysku celeho webView

		
		if(!((MainActivity) context).isGallery){
		 switch(event.getAction()){
         case MotionEvent.ACTION_DOWN:
        	 startX=event.getRawX();
        	 startY=event.getRawY();
             //System.out.println("ACTION DOWN getScrollY()"+getScrollY());
             action_down_scrollY=getScrollY();
        	 isEndInTouch=false;
        //	 System.out.println("scrollY:"+this.getScrollY());
        	// System.out.println("webViewHeight:"+webViewHeight);
        //	 System.out.println("height:"+height);
          break;
         case MotionEvent.ACTION_POINTER_DOWN:

          break;
         case MotionEvent.ACTION_MOVE:
        	 
        	 //System.out.println("som v action move");
        	 
        	 if (startScrolling) {
        		 float diffX = Math.abs(event.getRawX() - startX);
        		 float diffY = Math.abs(event.getRawY() - startY);
        		 if (diffX > diffY + treshold) {
        			 setVerticalScrollBarEnabled(false);
        			 setHorizontalScrolling();
        			 setEnabled(false);
        			// System.out.println("tu malk by som byt len raz");
        		 }
        		 if (diffX > 0 || diffY > 0) {
        			 startScrolling = false;
        		 }
        	 }
        	// if (event.getRawX() > (event.getRawY())){
             //horizontalnmy pohyb
        	if (enableHorizontal) {
        		rel.setX(event.getRawX()-startX);
        		event.setLocation(event.getX(), startY-statusBarHeight);
        	
        		//System.out.println("diff: "+(event.getRawX()-startX));
        	//ak sa posunie s prstom o 1/10 obrazovky objavi sa sipka
	        	/*if ((event.getRawX()-startX)>(screenWidth/10)) {
	        		if (previousPos>=0) {
	        		next_article.setVisibility(View.VISIBLE);
	        		}
	        	}*/
        	
	        	/*if ((event.getRawX()-startX)<-(screenWidth/10)) {
	        		if (nextPos<=maxPos) {
	        			prev_article.setVisibility(View.VISIBLE);
	        		}
	        	}*/
	        	
        	//ak sa posunie prstom o 1/4 obrazovky tak sa spusta dalsi clanok
	        	if ((event.getRawX()-startX)>(screenWidth/4)) {
	        		if ((previousPos>=0) && !isLaunch) {
	        			ArticleModel previous = ((MainActivity)context).currentItems.get(previousPos);
	        			if ((previous.getLocked().equals("1"))&&(!((MainActivity)context).logged)) {
	        				((MainActivity)context).menuLayout.displayLogin(true,model.getPos(),previousPos);
	        			} else {
	        				((MainActivity)context).displayDetail(previous);
	        			}
	        			
	        			isLaunch=true;
	        		}
	        	}
	        	if ((event.getRawX()-startX)<-(screenWidth/4)) {
	        		
	        		if ((nextPos<=maxPos) && !isLaunch ){
	        			ArticleModel next = ((MainActivity)context).currentItems.get(nextPos);
	        			if ((next.getLocked().equals("1"))&&(!((MainActivity)context).logged)){
	        				((MainActivity)context).menuLayout.displayLogin(true,model.getPos(),nextPos);
	        			} else {
	        		 	((MainActivity)context).displayDetail(next);
	        			}
	        			
	        			isLaunch=true;
	        			
	        		}
	        	}
        	}
        	break;
         case MotionEvent.ACTION_UP:
             //System.out.println("som v action up");
        	 startScrolling=true;
        	 enableHorizontal=false;
        	// Toast.makeText(context, getUrl(), Toast.LENGTH_LONG).show(); 
        	 //vraciame sa do povodnej pozicie
        	 rel.setX(0);
        	 //prev_article.setVisibility(View.INVISIBLE);
        	 //next_article.setVisibility(View.INVISIBLE);

             if (gestureDetector.onTouchEvent(event)) {
                return true;
             }
             else {
                 int scrollY = getScrollY();
                 activePage = ((scrollY + (screenHeight/2))/screenHeight);
                 int scrollTo = activePage*screenHeight;
                 ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "scrollY", scrollY, scrollTo).setDuration(500);
                 objectAnimator.setInterpolator(new DecelerateInterpolator());
                 objectAnimator.start();
                 //scrollTo(0, scrollTo);


             }
        	
        	 break;
        	// setVerticalScrolling();

         case MotionEvent.ACTION_POINTER_UP:
        	
          break;
         }  
		}
        return (gestureDetector.onTouchEvent(event) || super.onTouchEvent(event));

    }


	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(((MainActivity) context).isGallery){
            return  false;
        }
        int maxY = height - screenHeight;
        int startY = getScrollY();
        scroller.fling(0, startY, 0, (int) -(velocityY/3*2), 0, 0, 0, maxY,0, 0);
        int finalY = scroller.getFinalY();
        activePage = ((finalY + (screenHeight/2))/screenHeight);
        int scrollTo = activePage*screenHeight;
        //System.out.println("onFling getScrollY():"+startY);
        //System.out.println("final Y"+finalY);
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "scrollY", startY, scrollTo).setDuration(700);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		//System.out.println("onscroll");
		//System.out.println(computeVerticalScrollOffset());
		//  System.out.println(e2.getX()-e1.getX());
		 // if(enableHorizontal){ this.setX(e2.getRawX()-e1.getRawX());
		  
		//  }
		   
	
		//System.out.println("koniec scrollu");
		 return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub	
	}
	
	public void slideAnimationOff() {
		  TranslateAnimation animation = new TranslateAnimation(
				  Animation.RELATIVE_TO_PARENT, 0f,
				  Animation.RELATIVE_TO_PARENT, 1f,
				  Animation.RELATIVE_TO_SELF, 0f,
				  Animation.RELATIVE_TO_SELF, 0f);
		  animation.setDuration(3000);
		  this.startAnimation(animation);
		}
	

	
	public ArticleModel getMagnusModel(String id){
		List<ArticleModel> list = ((MainActivity)context).magnusArticleItems;
		int i =0;
		for (ArticleModel item : list) {
			if(item.getMagnusArticleId().equals(id)) {
				 return list.get(i);
			} i++;
		}
		return ((MainActivity)context).magnusArticleItems.get(0);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	} 
	
	public void setHorizontalScrolling() {
			enableHorizontal=true;
			setVerticalScrollBarEnabled(false);		
	}
	
	public void setVerticalScrolling() {
		enableHorizontal=false;
		this.setVerticalScrollBarEnabled(true);		
}
	public static float dipToPixels(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}

    public void setWebviewHeight (){
            height = (int) Math.floor(this.getContentHeight() * this.getScale());
            webViewHeight = this.getMeasuredHeight();
            loading_complet=true;
        //System.out.println(height);
        //System.out.println(screenHeight);
    }

}
