package jtb.magazin.UI;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

public class CustomDrawerLayout extends DrawerLayout {

public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
}

public CustomDrawerLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
}

public CustomDrawerLayout(Context context) {
    super(context);
}

private View currentDrawerView;



@Override
public boolean onInterceptTouchEvent(MotionEvent event) {
    boolean result = super.onInterceptTouchEvent(event);

    if (currentDrawerView != null && isDrawerOpen(currentDrawerView)) {
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) currentDrawerView.getLayoutParams();
        

        if (layoutParams.gravity == Gravity.START) {
            if ((event.getX() > layoutParams.width) && (event.getX() < layoutParams.width*2)) {
                result = false;
            } else result=super.onInterceptTouchEvent(event);
        }
      //  System.out.println(layoutParams.width);
       /* else if (layoutParams.gravity == Gravity.END) {
            if (event.getY() > currentDrawerView.getX() + currentDrawerView.getWidth()) {
                result = false;
            }
        }*/
        // ......
        //return false;
    };
    return result;
}

public void setDrawerViewWithoutIntercepting(View view) {
    this.currentDrawerView = view;
}
}