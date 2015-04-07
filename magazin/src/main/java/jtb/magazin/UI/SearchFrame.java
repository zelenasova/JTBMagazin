package jtb.magazin.UI;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
//prve okienko pri zobrazeni vyhladavania.
@SuppressLint("ViewConstructor")
public class SearchFrame extends FrameLayout {
	
	Context context;
	
	public SearchFrame(Context context, int pocet) {
		super(context);
		this.setBackgroundColor(Color.parseColor("#d5d5d5"));
		this.context=context;
		
		
		 LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		 View convertView = mInflater.inflate(R.layout.search_frame, null);
		 
		 DisplayMetrics metrics = context.getResources().getDisplayMetrics();		  	
			int m=(int) (metrics.widthPixels/Constants.MARGIN_WIDTH_KOEF*2);
		 
		 LinearLayout lin = (LinearLayout) convertView.findViewById(R.id.search_frame);
		 lin.setPadding(0, 0, 3*m, 0);
		 ImageView search_img = (ImageView) convertView.findViewById(R.id.search_img);
		 LinearLayout.LayoutParams search_img_Params = (android.widget.LinearLayout.LayoutParams) search_img.getLayoutParams();
		 search_img_Params.width=5*m;
		 search_img_Params.height=4*m;
		 
		/* ImageView search_icon_big = (ImageView) convertView.findViewById(R.id.search_icon_big);
		 LinearLayout.LayoutParams search_icon_big_Params = (android.widget.LinearLayout.LayoutParams) search_icon_big.getLayoutParams();
		 search_icon_big_Params.width=4*m;
		 search_icon_big_Params.height=4*m;
		 search_icon_big_Params.setMargins(0, 2*m, m, 0);*/
		           
		
		           
		LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		RelativeLayout rel = new RelativeLayout(context);
		RelativeLayout rel2 = new RelativeLayout(context); 
		
		ImageView imgDate = new ImageView(context);
		imgDate.setImageResource(R.drawable.date);
		
		float w = metrics.widthPixels/15;
		TextView text1 = (TextView) convertView.findViewById(R.id.vysledky);
		TextView text1b = (TextView) convertView.findViewById(R.id.vysledky2);
		text1.setText(((MainActivity)context).getString(R.string.vysledky_vyhladavania));
		text1b.setText(((MainActivity)context).getString(R.string.vysledky_vyhladavania2));
		text1b.setVisibility(View.VISIBLE);
		text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size17));
		text1b.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size17));
		//text1.setLineSpacing(0, (float) 1.1);
		TextView text2 = (TextView) convertView.findViewById(R.id.hladane);
		
		if (pocet==0){
			text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clankov));
		} else if (pocet==1){
			text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clanok));
		} else if ((pocet>1)&&pocet<5){
			text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clanky));
		} else text2.setText(pocet+" "+((MainActivity)context).getString(R.string.clankov));
		
		text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));

		text1.setTypeface(((MainActivity)context).fedraSansAltProBold);
		text1b.setTypeface(((MainActivity)context).fedraSansAltProLight);
		text2.setTypeface(((MainActivity)context).fedraSansAltProLight);
	//	lin.setLayoutParams(linPar);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
	
		

		this.addView(lin);
		
	}
	
	

	
}
