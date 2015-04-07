package jtb.magazin.UI;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;

//prve okienko pri zobrazeni oblubenych clankov.
@SuppressLint("ViewConstructor")
public class FavoritesFrame extends FrameLayout {
	
	Context context;
	
	public FavoritesFrame(Context context, int pocet) {
		super(context);
		this.setBackgroundColor(Color.parseColor("#d5d5d5"));
		this.context=context;
		
		
		 LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		 mInflater.inflate(R.layout.search_frame, this);
		 
		 DisplayMetrics metrics = context.getResources().getDisplayMetrics();		  	
			int m=(int) (metrics.widthPixels/Constants.MARGIN_WIDTH_KOEF*2);
		 
		 LinearLayout lin = (LinearLayout) findViewById(R.id.search_frame);
		 lin.setPadding(0, 0, 3*m, 0);
		 ImageView search_img = (ImageView) findViewById(R.id.search_img);
		 LinearLayout.LayoutParams search_img_Params = (android.widget.LinearLayout.LayoutParams) search_img.getLayoutParams();
		 search_img_Params.width=5*m;
		 search_img_Params.height=4*m;


		ImageView imgDate = new ImageView(context);
		imgDate.setImageResource(R.drawable.date);

		TextView text1 = (TextView) findViewById(R.id.vysledky);
		text1.setText(Html.fromHtml(((MainActivity)context).getString(R.string.favorites)));
		text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size17));
		TextView text2 = (TextView) findViewById(R.id.hladane);
		
		//osetrenie sklonovania
		if (pocet==0){
			text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clankov));
		} else if (pocet==1){
			text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clanok));
		} else if ((pocet>1)&&pocet<5){
			text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clanky));
		} else text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clankov));
		
		text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
		text1.setTypeface(((MainActivity) context).fedraSansAltProBold);
		text2.setTypeface(((MainActivity) context).fedraSansAltProLight);
		
	}
	
	

	
}
