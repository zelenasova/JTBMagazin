package jtb.magazin.UI;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//osetruju sa kliky na ramcek v gride
@SuppressLint("ViewConstructor")
public class MyPaint extends View  implements GestureDetector.OnGestureListener {
	
	private ArticleModel model;
	private int defaultTriangleSize;
	private int triangleSize;
	private ArticleControlsLayer controlsLayer;
	private GestureDetectorCompat gDetector;
	private ImageView img;
	ArticleThumbnailFrame parent;
	private float yFrame;
	private float koef=0.5f;
	int imgMargin;
	int imgWidth;
	boolean isOpen = false;
	boolean isOpening = false;
	TextView text1;
	TextView text2;
	public TextView text3;
	int h;
	int text3Height;
	public boolean isVisiblePerex=false;
	boolean isClosing = false;
	int actualHeight;
	Context context;
	String id;
	int bottomPadding;
	private long mLastClickTime = 0;
	String type;
	LinearLayout black;
	boolean locked;
	boolean initialPerex = false;
	final float dx;
	final float dy;
	final float mw;


	public MyPaint(Context context, int imgMargin, int imgWidth,TextView text1,TextView text2,
			LinearLayout black, int actualHeight,ArticleModel model,int bottomPadding) {
			super(context);
			gDetector = new GestureDetectorCompat(context, this);
			this.model=model;
		 	DisplayMetrics metrics = getResources().getDisplayMetrics();
	    	float screenHeight= metrics.widthPixels;
	    	//this.defaultTriangleSize= (int)(screenHeight/64);
	    	this.defaultTriangleSize= (int)(screenHeight/64);
	    	triangleSize=0;
	    	this.black=black;
	    	this.imgMargin=imgMargin;
	    	this.imgWidth=imgWidth;
	    	this.text2=text2;
	    	this.text1=text1;
	    	this.actualHeight=actualHeight;
	    	this.context=context;
	    	this.id=model.getId();
	    	this.bottomPadding=bottomPadding;
	    	this.type=model.getCategoryID();
	    	this.locked=(!((MainActivity)context).logged)&&(model.getLocked().equals("1"));
	    	mw= ((MainActivity)context).screenWidth;
	    	final float mh= ((MainActivity)context).screenHeight;
	    	dx =  mw/Constants.WIDTH_KOEF;
	    	dy =  mh/Constants.WIDTH_KOEF;
	    	
	    //	this.setLayoutParams( new FrameLayout.LayoutParams(defaultTriangleSize, defaultTriangleSize));
	    	this.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
	    	
	}
	
	public int getTriangleSize() {
		return triangleSize;
	}
	
	public void setTriangleSize(int triangleSize) {
		if (triangleSize != this.triangleSize) {
			this.triangleSize = triangleSize;
			invalidate();	
			//postupne odkryvanie vrstvy nad ramcekom (volba pridat medzi ulozene)
			if (controlsLayer.getVisibility() == View.VISIBLE) {
				controlsLayer.setTriangleSize(triangleSize);
			}
			               
		}
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);	   
	    canvas.save(); 
	    Paint paint = new Paint();	      
	    paint.setColor(android.graphics.Color.parseColor("#5b707b"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(3);
        canvas.drawRect(0, 0, this.getWidth(), triangleSize, paint);    
	    canvas.restore();
		
	}
	 
	
	@Override
    public boolean onTouchEvent(MotionEvent me) {
		if (!locked){

		parent = (ArticleThumbnailFrame) getParent();
		yFrame=parent.getActualHeight();
		switch (me.getAction()) {
			case MotionEvent.ACTION_DOWN:	
				if(((MainActivity)context).openRoller!=null){
					if(((MainActivity)context).openRoller!=this.getParent()) ((MainActivity)context).openRoller.viewPaint.isOpen=false;
					//((MainActivity)context).openRoller.controlsLayer.setVisibility(View.INVISIBLE);
					
					((MainActivity)context).openRoller.viewPaint.setTriangleSize(0);
				}
				//ak uz je otvoreny tak sa zatvori
				if (isOpen){
					ObjectAnimator triangleAnimator = ObjectAnimator.ofInt(this, "triangleSize", 0);
					triangleAnimator.addListener(new AnimatorListener() {
						@Override
						public void onAnimationEnd(Animator animation) {
						//	MyPaint.this.setLayoutParams(new FrameLayout.LayoutParams(defaultTriangleSize, defaultTriangleSize));
							MyPaint.this.controlsLayer.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onAnimationCancel(Animator animation) {
						}

						@Override
						public void onAnimationRepeat(Animator animation) {
						}

						@Override
						public void onAnimationStart(Animator animation) {
						}
						
					});
					triangleAnimator.setDuration(150);
					triangleAnimator.start();
					isClosing = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				

				if (( me.getY() > parent.getActualHeight() / 2)&&(!isOpen)) {
				
			//inicializacia prerexu(text3)	vypocita sa kolko riadkov maximalne moze mat aby sa zmestil do ramceka
			if (!initialPerex)	{
				/*controlsLayer = new ArticleControlsLayer(context,model);
				controlsLayer.setVisibility(View.INVISIBLE);
				parent.addView(controlsLayer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.MATCH_PARENT));*/
				text3 = new TextView(context);
				text3.setId(1002);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				((ArticleThumbnailFrame) this.getParent()).rel.addView(text3,params3);
				text3.setShadowLayer(dx*2, dx, dx, Color.BLACK);
				text3.setText(model.getPerex());
				text3.setTextColor(Color.WHITE);
				text3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size11));
				int textHeight=(int) (actualHeight-text1.getHeight()-text2.getHeight()- mw/parent.konstantaPadding-8*dy);// 4*dy padding + rezerva,
				int lineHeight = text3.getLineHeight();
		        int maxLines = (int) (textHeight / lineHeight);
		        text3.setMaxLines(maxLines);		
		      //  System.out.println(maxLines);
				text3.setOnClickListener(new View.OnClickListener() 
		        {
		            @Override
		            public void onClick(View v)
		            {
		            	((MainActivity)MyPaint.this.context).displayView(2,MyPaint.this.model.getId(),null); 
		            	 
		            }
		        });
				
				
				
				ViewTreeObserver vto2 =text3.getViewTreeObserver();
				 vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
				        @Override 
				        public void onGlobalLayout() { 		        	
				        	text3Height=text3.getHeight();		        	
				            text3.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				            startAnimations();
				          //  text3.setVisibility(View.GONE);
				        } 
				    }); 
				 initialPerex=true;
			}	
						
		//***************************************************************************osetrenie klikov na nazov a perex *******************************	
				if (!isVisiblePerex) {
					ArticleThumbnailFrame openFrame=((MainActivity)context).openFrame;
					if(openFrame!=null) {				
						closePerex(openFrame);
					}
					black.setVisibility(TextView.VISIBLE);
					if (!(text3.getText().equals(""))){
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ABOVE, text3.getId());					
					text2.setLayoutParams(params);
					text3.setVisibility(TextView.VISIBLE);										
					if (initialPerex) startAnimations();
					}
					((MainActivity)context).openFrame=(ArticleThumbnailFrame) this.getParent();
					isVisiblePerex = true;
					} else {
						//if (( me.getY()>this.actualHeight-text3Height-bottomPadding)&&(!isOpen)){
						if ((!isOpen)){
							 if (!(SystemClock.elapsedRealtime() - mLastClickTime < 1000)){
								 ((MainActivity)MyPaint.this.context).displayDetail(model);
								 closePerex((ArticleThumbnailFrame) MyPaint.this.getParent());	
						        }
						        mLastClickTime = SystemClock.elapsedRealtime();
							
						}
					}
		//***************************************************************************osetrenie klikov na nazov a perex ****************************	
					
				} 
				if (isClosing){
					isOpen = false;
					isClosing=false;
				break;}
				isOpening=false;
				break;
		}
       
		} else  ((MainActivity)context).menuLayout.displayLogin(false,0,0);
		
		return gDetector.onTouchEvent(me);
		//return true;
    }
	 

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}
	//pri dlhom stlaceni sa odkryje vrstva kde je moznost pridat clanok medzi ulozene
	@Override
	public void onLongPress(MotionEvent e) {
		if (!locked){
		if (controlsLayer==null){
			controlsLayer = new ArticleControlsLayer(context,model);
			controlsLayer.setVisibility(View.INVISIBLE);
			parent.addView(controlsLayer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		}
		isOpen = true;
		this.controlsLayer.setVisibility(View.VISIBLE);
		ObjectAnimator triangleAnimator = ObjectAnimator.ofInt(this, "triangleSize",parent.getActualHeight());
		triangleAnimator.setDuration(500);
		triangleAnimator.start();
		((MainActivity)context).openRoller=(ArticleThumbnailFrame) this.getParent();
		}
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		
		/*if (!isOpen && (e1.getY() < defaultTriangleSize * 2) ||
			isOpen && (e1.getY() > parent.getActualHeight() - (defaultTriangleSize * 2)))
		{
			if (Math.abs(distanceY) > Math.abs(distanceX) + 1) {
				setTriangleSize((int) (e2.getY()));	
			}
		}*/
		return true;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	
	public void closePerex(ArticleThumbnailFrame frame){
		frame.viewPaint.text3.clearAnimation();
		frame.viewPaint.text2.clearAnimation();
		frame.viewPaint.text3.setVisibility(View.GONE);
		frame.black.setVisibility(View.GONE);
		frame.viewPaint.isVisiblePerex=false;
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		frame.text2.setLayoutParams(params2);
	}
	
	private void startAnimations(){
		if (!model.getPerex().equals("")){
		TranslateAnimation transAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0f,Animation.RELATIVE_TO_PARENT, 0f,
				Animation.ABSOLUTE, text3Height, Animation.RELATIVE_TO_SELF, 0f);
				transAnimation.setDuration(500);
				text2.startAnimation(transAnimation);				
				TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 0f);
				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
				fadeIn.setDuration(300);
				fadeIn.setStartOffset(500);
				text3.startAnimation(fadeIn);
		}
	}
}
