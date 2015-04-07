 package jtb.magazin.UI;

	import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import jtb.magazin.MainActivity;
import jtb.magazin.model.WOTModel;
	//obsluhujeme zobrazenie 3dModelu do 2D a obsluhujeme rotaciu krystalu na zaklade dotykov uzivatela
	public class WOTController extends FrameLayout  implements GestureDetector.OnGestureListener {
		
		String deviceHash;
		final RelativeLayout world;
		private ArrayList<WOTModel> items;
		double w;
		double h;
		double angleX;
		double angleY;
		double deltaX=0.2;
		double deltaY=0.7;
		double angleRX;
		double angleRY;
		double startX;
		double startY;
		double trashHold;
        int i=0;
        int count;
		Context context;
		private GestureDetectorCompat gDetector;
		ArrayList<DrawLine> lines;
        RelativeLayout world_wrapper;
        RelativeLayout overlay;
        Point2D   point;
        Point3D pointAfterRotation;
        Point3D newPoint = new Point3D();
        Point2D result;
        double xx, yy, zz;

		public WOTController(Context context, ArrayList<WOTModel> items,ArrayList<DrawLine> lines, final RelativeLayout world, RelativeLayout world_wrapper,RelativeLayout overlay) {
			super(context);
			this.items=items;
			this.lines=lines;
			this.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
			w= ((MainActivity) context).screenWidth;
			h= ((MainActivity) context).screenHeight;
			//System.out.println(w);
			//System.out.println(h);
			trashHold =(int) dipToPixels(context, 10);
			this.world=world;
            this.overlay=overlay;
			this.world_wrapper=world_wrapper;
			this.context=context;
		
			gDetector = new GestureDetectorCompat(context, this);
			//najprv pre vsetky textViews a ciary vypocitame suradnice v 2D. Zaciatocnu poziciu 3D sme ziskali od servera, pomocou convert3Dto2D
			//ziskavame suradnice v 2D a nasledne mozme vykreslit
            count = items.size();

			 for (final WOTModel item : items) {
                 i++;
				//final Point2D point = convert3DTo2D(new Point3D(item.x,item.y,item.z));
				item.text.setTextSize(40);
				final float koef=(float) ((item.z + 80) / 160 * 0.7 + 0.3);
				
				/*item.params.setMargins((int)(point.x-item.getWidth()/2), (int)(point.y-item.getHeight()/2), 0, 0);
				item.text.setScaleX(koef);
			    item.text.setScaleY(koef);
			    item.text.setAlpha(koef);*/
				
				//potrebujeme najst stred textView
			    ViewTreeObserver vto =item.text.getViewTreeObserver();
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
			        @Override 
			        public void onGlobalLayout() {
                        System.out.println("som v TreeObserver");
			        	int width = item.text.getWidth();
			        	int height = item.text.getHeight();
			        	item.width=width;
			        	item.height=height;
			        	//item.params.leftMargin=(int) (point.x-width*koef/2);
						//item.params.topMargin=(int) (point.y-height*koef/2);
						updateOneText(deltaX,deltaY,item);


			        	item.text.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        System.out.println("koncim s TreeObserver");
                        //item.text.setVisibility(VISIBLE);
                        //item.text.setA
			        } 
			    }); 
			    
			}
            world.setVisibility(View.VISIBLE);
		}

        public void updateOneText(double angleRX, double angleRY,WOTModel item){

            for ( int i = 0; i < lines.size(); i++ ) {
                lines.get(i).invalidate();
            }


                //  System.out.println("pred rotaciou"+items.get(a).x);
                pointAfterRotation = rotate3DPoint(new Point3D(item.x,item.y,item.z), angleRX, angleRY,0);
                //System.out.println("po rotacii"+pointAfterRotation.x);
                float koef = (float) ((pointAfterRotation.z + 80) / 160 * 0.7 + 0.3);
                float lineKoef = (float) ((pointAfterRotation.z + 80) / 160 * 0.2 + 0.1);
                //System.out.println(lineKoef);
                //System.out.println(items.get(a).getHeight());
                point = convert3DTo2D(pointAfterRotation);
                item.actualKoef=lineKoef;
                item.actualKoefHeight=koef;
                item.actualX=(int) point.x;
                item.actualY=(int) point.y;
                item.text.setX((float)(point.x-item.width/2));
                item.text.setY((float)(point.y-item.height/2));
                item.text.setScaleX(koef);
                item.text.setScaleY(koef);
                item.text.setAlpha(koef);
                // items.get(a).text.setVisibility(View.VISIBLE);

            System.out.println("koncim s update Texts");


        }
		
		//na zaklade rotacie krystalu aktualizujeme pozicie jednotlivych textViews
		public void updateTexts(double angleRX, double angleRY){

            for ( DrawLine line:lines ) {
                line.invalidate();
            }
            //System.out.println(items.size());
            for (WOTModel item : items){
                pointAfterRotation = rotate3DPoint(new Point3D(item.x,item.y,item.z), angleRX, angleRY,0);
			    float koef = (float) ((pointAfterRotation.z + 80) / 160 * 0.7 + 0.3);
			    float lineKoef = (float) ((pointAfterRotation.z + 80) / 160 * 0.2 + 0.1);
			    point = convert3DTo2D(pointAfterRotation);
                item.actualKoef=lineKoef;
                item.actualKoefHeight=koef;
                item.actualX=(int) point.x;
                item.actualY=(int) point.y;
                item.text.setX((float)(point.x-item.width/2));
                item.text.setY((float)(point.y-item.height/2));
                item.text.setScaleX(koef);
                item.text.setScaleY(koef);
                item.text.setAlpha(koef);
			  }
		}
		
		//prepocet na 2D suradnice
		Point2D convert3DTo2D (Point3D point) {
			point.z = point.z-160;
			result = new Point2D(w / 2 - (h/2.5) * point.x / (point.z == 0 ? 1 : point.z), h / 2 - ((h/2.5)) * point.y / (point.z == 0 ? 1 : point.z));
			return result;
		}
		
		//rotuje krystal v 3D priestore
		 Point3D rotate3DPoint (Point3D point, double angleX, double angleY, double angleZ){


			  // X axis
			  // y' = y*cos q - z*sin q
			  // z' = y*sin q + z*cos q
			  // x' = x
			  yy = point.y * Math.cos(angleX) - point.z * Math.sin(angleX);
			  zz = point.y * Math.sin(angleX) + point.z * Math.cos(angleX);
			  xx = point.x;
 
			  point.x = xx;
			  point.y = yy;
			  point.z = zz;
			  	  
			  // Y axis
			  //z' = z*cos q - x*sin q
			  //x' = z*sin q + x*cos q
			  //y' = y
			  zz = point.z * Math.cos(angleY) - point.x * Math.sin(angleY);
			  xx = point.z * Math.sin(angleY) + point.x * Math.cos(angleY);
			  yy = point.y;			  
			  
			  point.x = xx;
			  point.y = yy;
			  point.z = zz;			  
			  
			  // Z axis
			  //x' = x*cos q - y*sin q
			  //y' = x*sin q + y*cos q
			  //z' = z
			  xx = point.x * Math.cos(angleZ) - point.y * Math.sin(angleZ);
			  yy = point.x * Math.sin(angleZ) + point.y * Math.cos(angleZ);
			  zz = point.z;			  
			  
			  point.x = xx;
			  point.y = yy;
			  point.z = zz;			  
			  
			  newPoint.x = point.x;
			  newPoint.y = point.y;
			  newPoint.z = point.z;			 
			 
			 return newPoint;
		 }
		
		 class Point2D {
			 
			 double x;
			 double y;
			 
			 public Point2D(double x, double y){
				 this.x=x;
				 this.y=y;
			 }
		 }
		 
		 class Point3D {
			 double x;
			 double y;
			 double z;
			 
			 public Point3D(){		
			 }
			 
			 public Point3D(double x, double y, double z){
				 this.x=x;
				 this.y=y;
				 this.z=z;
			 }
		 }
		 
		 @Override
		    public boolean onTouchEvent(MotionEvent me) {
				
				switch (me.getAction()) {
					case MotionEvent.ACTION_DOWN:					
						startX = me.getX();
						startY = me.getY();
					//	System.out.println(startX);
						break;
						
					case MotionEvent.ACTION_MOVE:
						double trashholdX = Math.abs(startX-me.getX());
						double trashholdY = Math.abs(startY-me.getY());
						
						
						//System.out.println(me.x);
						//System.out.println(me.x);
						if ((trashholdX<trashHold) && (trashholdY<trashHold)) break;
						//potrebujeme ziskat uhol o ktory sa rotuje krystal
						angleX = (-(startX - me.getX()));
						angleY = (-(startY - me.getY()));
						
						angleRX= (Math.PI / 180 * (angleY %360));
						angleRY= (Math.PI / 180 * (angleX %360));
                        //System.out.println("aktualizujem");
						updateTexts(angleRX+deltaX,angleRY+deltaY);
						//System.out.println("koncim s aktualizaciou");
						break;
						
					case MotionEvent.ACTION_UP:
						break;
		       
				}
				return gDetector.onTouchEvent(me);
				//return true;
		    }

		@Override
		public boolean onDown(MotionEvent e) {
			
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			//System.out.println(distanceX);
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		//ak sa jednoducho klikne na textView tak sa zobrazia clanky ktore suvisia s danym keyword
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			//System.out.println("som v onSingleTapUp");
			float x = e.getX();
			float y = e.getY();
			
			//musime zistit ktore textView je na vrchu, lebo moze nastat situacia ze textViews sa prekryvaju.
			WOTModel kandidat = null;
			double z=-1000;
			 for ( int a = 0; a < items.size(); a++ ) {
				  
					Point2D   point;
					Point3D pointAfterRotation;
						  
					pointAfterRotation = rotate3DPoint(new Point3D(items.get(a).x,items.get(a).y,items.get(a).z), angleRX+deltaX, angleRY+deltaY,0);
					float koef = (float) ((pointAfterRotation.z + 80) / 160 * 0.7 + 0.3);
						 
					point = convert3DTo2D(pointAfterRotation);	
					float startX = (float) (point.x-items.get(a).width/2*koef);
					float endX = (float) (point.x+items.get(a).width/2*koef);
					float startY = (float) (point.y-items.get(a).height/2*koef);
					float endY = (float) (point.y+items.get(a).height/2*koef);
	
					if (((x>startX)&(x<endX)) && ((y>startY)&(y<endY))) {
						if (pointAfterRotation.z>z){
							kandidat = items.get(a);
							z= pointAfterRotation.z;
						}
					}
				}
			 if (kandidat !=null){
				 world.setVisibility(View.INVISIBLE);
				 world_wrapper.setVisibility(View.INVISIBLE);
				world.removeAllViews();
				//spustime hladanie vysledkov pre dane keyword
				((MainActivity)context).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ((MainActivity)context).mDrawerLinear);
				((MainActivity)context).displayKeywords(kandidat.keywordID);
			 }
			return true;
		}
		
		public static float dipToPixels(Context context, float dipValue) {
		    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
		}
		
	}
