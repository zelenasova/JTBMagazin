package jtb.magazin.UI;


	import android.animation.ObjectAnimator;
    import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
    import android.view.animation.DecelerateInterpolator;
    import android.widget.HorizontalScrollView;
import android.widget.Scroller;

public class CustomHorizontalScrollView extends HorizontalScrollView implements GestureDetector.OnGestureListener {


		private GestureDetector mGestureDetector;
		private int mActiveFeature = 0;
		Context context;
		int featureWidth;
		int count = 6;
		private Scroller mScroller;


		public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
            this.context=context;
		}

		public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
			super(context, attrs);
            this.context=context;
		}

		public CustomHorizontalScrollView(Context context) {
			super(context);
			this.context=context;
			mScroller = new Scroller(context);
		}

		public void setFeatureItems(final int featureWidth){
            mScroller = new Scroller(context);
			this.featureWidth=featureWidth;
	 		/*setOnTouchListener(new View.OnTouchListener() {
	 			@Override
	 			public boolean onTouch(View v, MotionEvent event) {
	 				//If the user swipes
	 				if (mGestureDetector.onTouchEvent(event)) {
	 					return true;
	 				}
	 				else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
	 					int scrollX = getScrollX();
	 					mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
	 					int scrollTo = mActiveFeature*featureWidth;
	 					smoothScrollTo(scrollTo, 0);
	 					return CustomHorizontalScrollView.super.onTouchEvent(event);
	 				}
	 				else{
	 					return false;
	 				}
	 			}

				
	 		});*/
	 		mGestureDetector = new GestureDetector(this.getContext(), this);
	 	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //na zaciatku treba vypocitat vysku celeho webView



            switch(event.getAction()){
                case MotionEvent.ACTION_UP:
                    System.out.println("som v action up");

                    if (mGestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    else {
                        int scrollX = getScrollX();
                        System.out.println("scrollX");
                        System.out.println("featureWidth:"+featureWidth);
                        mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
                        System.out.println("mActiveFeature:"+mActiveFeature);
                        int scrollTo = mActiveFeature*featureWidth;
                        //smoothScrollTo(scrollTo, 0);
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "scrollX", scrollX, scrollTo).setDuration(300);
                        objectAnimator.setInterpolator(new DecelerateInterpolator());
                        objectAnimator.start();
                        return true;
                    }


                // setVerticalScrolling();

                case MotionEvent.ACTION_POINTER_UP:

                    break;
            }

    return (mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event));

    }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
            int maxX = featureWidth*100;
            int startX = getScrollX();
            mScroller.fling(startX, 0, (int) -(velocityX), 0, 0, maxX, 0, 0);
            int finalX = mScroller.getFinalX();
            int activePage = ((finalX + (featureWidth/2))/featureWidth);
            int scrollTo = activePage*featureWidth;
            //System.out.println("onFling getScrollY():"+startY);
            //System.out.println("final X"+finalX);
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "scrollX", startX, scrollTo).setDuration(400);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.start();
            return true;
        }

    }
