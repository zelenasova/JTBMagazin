package jtb.magazin.magnus;

import jtb.magazin.MainActivity;
import jtb.magazin.model.ArticleModel;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class MyMagnusPaint extends View  implements GestureDetector.OnGestureListener {
	
	private ArticleModel model;
	private int defaultTriangleSize;
	private int triangleSize;
	private MagnusArticleControlsLayer controls;	
	private GestureDetectorCompat gDetector;
	private ImageView img;
	MagnusArticleFrame parent;
	private float xFrame;
	private float yFrame;
	private float koef=0.5f;
	int imgMargin;
	int imgWidth;
	boolean isOpen = false;
	boolean isOpening = false;
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
	
	
	public MyMagnusPaint(Context context, MagnusArticleControlsLayer controls, int actualHeight, ArticleModel model) {
			super(context);
			//gDetector spracovava kliky
			gDetector = new GestureDetectorCompat(context, this);
			this.model=model;
			this.controls = controls;
		 	DisplayMetrics metrics = getResources().getDisplayMetrics();
	    	float screenHeight= metrics.widthPixels;
	    	triangleSize=0;   	
	    	this.actualHeight=actualHeight;
	    	this.context=context;
	    	this.id=model.getId();
	    	this.type=model.getCategoryID();
	    	
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
			if (controls.getVisibility() == View.VISIBLE) {
				controls.setTriangleSize(triangleSize);
			}
			               
		}
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	//pri prekreslovani sa postupne odkryva a zvacsuje obdlznik
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);	   
	    canvas.save(); 
	    Paint paint = new Paint();	      
	    paint.setColor(android.graphics.Color.parseColor("#3A4F59"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(3);
        canvas.drawRect(0, 0, this.getWidth(), triangleSize, paint);    
	    canvas.restore();
		
	}
	 
	
	@Override
    public boolean onTouchEvent(MotionEvent me) {
		parent = (MagnusArticleFrame) getParent(); 
		xFrame=parent.getActualWidth();
		yFrame=parent.getActualHeight();
		switch (me.getAction()) {
			case MotionEvent.ACTION_DOWN:	
				if(((MainActivity)context).openMagnusRoller!=null){
				//	((MainActivity)context).openMagnusRoller.controlsLayer.setVisibility(View.INVISIBLE);
					((MainActivity)context).openMagnusRoller.viewPaint.isOpen=false;
					((MainActivity)context).openMagnusRoller.viewPaint.setTriangleSize(0);
				}
				if (isOpen){
					ObjectAnimator triangleAnimator = ObjectAnimator.ofInt(this, "triangleSize", 0);
					triangleAnimator.addListener(new AnimatorListener() {
						@Override
						public void onAnimationEnd(Animator animation) {
						//	MyPaint.this.setLayoutParams(new FrameLayout.LayoutParams(defaultTriangleSize, defaultTriangleSize));
							MyMagnusPaint.this.controls.setVisibility(View.INVISIBLE);
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

				}
				break;
			case MotionEvent.ACTION_UP:
				

						if ((!isOpen)){
							 if ((!(SystemClock.elapsedRealtime() - mLastClickTime < 1000))){
								 if ((!((MainActivity)context).logged)&&(model.getLocked().equals("1"))){
									 ((MainActivity)context).menuLayout.displayLogin(false,0,0);
								 } else ((MainActivity)MyMagnusPaint.this.context).displayDetail(model);
								 
						        }
						        mLastClickTime = SystemClock.elapsedRealtime();							
						}break;	
					}						
				
				
		
       	
		return gDetector.onTouchEvent(me);
		//return true;
    }
	 

	@Override
	//pri dlhom stlaceni sa odkryje vrstva kde je moznost pridat clanok medzi ulozene
	public void onLongPress(MotionEvent e) {
		isOpen = true;
		this.controls.setVisibility(View.VISIBLE);
		ObjectAnimator triangleAnimator = ObjectAnimator.ofInt(this, "triangleSize",
				parent.getActualHeight());
		triangleAnimator.setDuration(500);
		triangleAnimator.start();
		((MainActivity)context).openMagnusRoller=(MagnusArticleFrame) this.getParent();
		
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
