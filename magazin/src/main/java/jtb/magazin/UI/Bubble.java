package jtb.magazin.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

//zobrazuje bublinku v markete
public class Bubble extends View {
	
	int w;
	int h;
	int offset;
	int dx;
	int dv;
    Paint paint;
    Path wallpath;
	
    public Bubble(Context context, int w, int h,int offset, int dx, int dv) {
        super(context);
        this.w=w;
        this.h=h;
        this.offset=offset;
        this.dx=dx;
        this.dv=dv;
        paint = new Paint();
        wallpath = new Path();
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        paint.setColor(android.graphics.Color.parseColor("#ededed"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth((float) 4.0);
   
     /*   Path wallpath = new Path();
        wallpath.reset(); // only needed when reusing this path for a new build
        wallpath.moveTo(0, 0); // used for first point
        wallpath.lineTo(w, 0);
        wallpath.lineTo(w, h);
        wallpath.lineTo(0, h);
        wallpath.lineTo(0,0); // there is a setLastPoint action but i found it not to work as expected
*/        

        wallpath.reset(); // only needed when reusing this path for a new build
        wallpath.moveTo(0+2, dv); // used for first point
        wallpath.lineTo(offset, dv);
        wallpath.lineTo(offset+dx/2, 0);
        wallpath.lineTo(offset+dx, dv);
        wallpath.lineTo(w-2, dv);
        wallpath.lineTo(w-2, h+dv);
        wallpath.lineTo(0+2, h+dv);
        wallpath.lineTo(0+2,dv); // there is a setLastPoint action but i found it not to work as expected

        canvas.drawPath(wallpath, paint);
        
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(android.graphics.Color.parseColor("#e2e2e2"));
        canvas.drawPath(wallpath, paint);
    }
}
