package jtb.magazin.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.Forex;
import jtb.magazin.model.Indexy;
import jtb.magazin.model.Kurzy;
import jtb.magazin.model.Xetra;
import jtb.magazin.store.MarketStore;

// V informacnom servise prve okienko je market
//tento frame tvori menu + styri na sebe vrstvy - indexy, xetra, forex, kurzy. Implicitne sa zobrazi Indexy. Ostatne vrstvy su
//invisible, po kliku na nejaku polozku menu sa vybrana polozka zobrazi a ostatne budu neviditelne
@SuppressLint("ViewConstructor")
public class MarketFrame extends FrameLayout {

	Context context;
	TextView text1;
	TextView text2;
	TextView text3;
	TextView text4;
	TextView text5;
	TextView text6;
	TextView text7;
	ArrayList<Indexy> indexes;
	ArrayList<Xetra> xetras;
	ArrayList<Forex> forexes;
	ArrayList<Kurzy> rates;
	
	public MarketFrame(final Context context, int w, int h, MarketStore marketStore) {
		super(context);
		this.indexes=marketStore.indexes;
		this.xetras=marketStore.xetras;
		this.forexes=marketStore.forexes;
		this.rates=marketStore.rates;
		// TODO Auto-generated constructor stub
		System.out.println("som v markete");
		this.context=context;	
		float mw =((MainActivity)context).screenWidth;
		float mh=((MainActivity)context).screenHeight;
		final float dx = (int)(mw/Constants.WIDTH_KOEF);
		final float dy = (int)(mh/Constants.HEIGHT_KOEF);
		final int itemHeight = h/10;
		LinearLayout pomWrapper = new LinearLayout(context);
		pomWrapper.setGravity(Gravity.CENTER);
		this.addView(pomWrapper);
		pomWrapper.setBackgroundResource(R.drawable.border);
		pomWrapper.setPadding(0, h/16, 0, 0);

		// vyska = 50*dx(padding celeho frame) + itemHeight(horne menu) + 5*dx + itemHeight+25*dx
		//************************************************************tvorba menu****************************************************************************	
		
		final int wrapperWidth=w*8/10;
		LinearLayout wrapper = new LinearLayout(context);
		LinearLayout.LayoutParams wrapperPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		wrapper.setLayoutParams(wrapperPar);
		wrapper.setOrientation(LinearLayout.VERTICAL);
		pomWrapper.addView(wrapper);
		
		
		int itemMargin=(int)(10*dx);
		int itemWidth=(wrapperWidth-3*itemMargin)/4;
		
		//horne menu
		LinearLayout topMenu = new LinearLayout(context);
		LinearLayout.LayoutParams topMenuPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		topMenu.setLayoutParams(topMenuPar);
		topMenuPar.setMargins(0, 0, 0, (int)(5*dx));
		wrapper.addView(topMenu);
		
		text1 = new TextView(context);		
		text1.setText("Indexy");
		text1.setBackgroundColor(Color.parseColor("#cecece"));
		text1.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams text1Par = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		text1.setLayoutParams(text1Par);
		text1.setTextColor(Color.parseColor("#8a8a8a"));		
		text1Par.setMargins(0, 0, itemMargin, 0);
		topMenu.addView(text1);
				
		text2 = new TextView(context);
		text2.setText("Xetra");
		text2.setBackgroundResource(R.drawable.border);
		text2.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams text2Par = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		text2.setLayoutParams(text2Par);
		text2.setTextColor(Color.parseColor("#8a8a8a"));
		text2Par.setMargins(0, 0, itemMargin, 0);
		topMenu.addView(text2);
		
		text3 = new TextView(context);
		text3.setText("Forex");
		text3.setBackgroundResource(R.drawable.border);
		text3.setGravity(Gravity.CENTER);
		text3.setTextColor(Color.parseColor("#8a8a8a"));
		LinearLayout.LayoutParams text3Par = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		text3.setLayoutParams(text3Par);
		text3Par.setMargins(0, 0, itemMargin, 0);
		topMenu.addView(text3);
		
		text4 = new TextView(context);
		text4.setHeight(itemHeight);
		text4.setWidth(itemWidth);
		text4.setText("Kurzy");
		text4.setTextColor(Color.parseColor("#8a8a8a"));
		text4.setBackgroundResource(R.drawable.border);
		text4.setGravity(Gravity.CENTER);
		topMenu.addView(text4);
				
		//************************************************************koniec tvorba menu*********************************************************************
		
		//*******************************************topBar*********************************************************************************************
		final RelativeLayout topBarWrapper = new RelativeLayout(context);
		RelativeLayout.LayoutParams topBarWrapperPar = new RelativeLayout.LayoutParams(wrapperWidth, itemHeight+(int)(30*dx));
		topBarWrapper.setGravity(Gravity.BOTTOM);
		topBarWrapper.setLayoutParams(topBarWrapperPar);
		wrapper.addView(topBarWrapper);
		int dx2=(int)(20*dx);
		int dv2=(int)(15*dx);
		//pre kazdu polozku menu je pod menu vytvoreny obdlznik so sipkou, tento objekt predstavuje trieda Bubble
		final Bubble bubble1 = new Bubble(context,wrapperWidth, itemHeight,itemWidth/2-dx2/2,dx2,dv2);
		LayoutParams bublePar = new LayoutParams(wrapperWidth, itemHeight+(int)(25*dx));
		bubble1.setLayoutParams(bublePar);
		topBarWrapper.addView(bubble1);
		
		final Bubble bubble2 = new Bubble(context,wrapperWidth, itemHeight,itemWidth+itemMargin+itemWidth/2-dx2/2,dx2,dv2);
		bubble2.setLayoutParams(bublePar);
		final Bubble bubble3 = new Bubble(context,wrapperWidth, itemHeight,2*itemWidth+2*itemMargin+itemWidth/2-dx2/2,dx2,dv2);
		bubble3.setLayoutParams(bublePar);
		final Bubble bubble4 = new Bubble(context,wrapperWidth, itemHeight,3*itemWidth+3*itemMargin+itemWidth/2-dx2/2,dx2,dv2);
		bubble4.setLayoutParams(bublePar);
		
		LinearLayout topBar = new LinearLayout(context);
		LinearLayout.LayoutParams topBarPar = new LinearLayout.LayoutParams(wrapperWidth, itemHeight+(int)(25*dx));
		topBar.setLayoutParams(topBarPar);
		topBar.setGravity(Gravity.BOTTOM);
		topBarWrapper.addView(topBar);
		
		
		text5 = new TextView(context);		
		text5.setText("Titul");
		text5.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams text5Par = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		text5.setLayoutParams(text5Par);
		text5.setTextColor(Color.parseColor("#818181"));
		text5Par.setMargins(0, 0, 2*itemMargin+itemWidth, 0);
		topBar.addView(text5);
		
		text6 = new TextView(context);		
		text6.setText("Kurz");
		text6.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams text6Par = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		text6.setLayoutParams(text6Par);
		text6.setTextColor(Color.parseColor("#818181"));
		text6Par.setMargins(0, 0, itemMargin, 0);
		topBar.addView(text6);
		
		text7 = new TextView(context);		
		text7.setText("Zmena");
		text7.setGravity(Gravity.LEFT);
		text7.setTextColor(Color.parseColor("#818181"));
		LinearLayout.LayoutParams text7Par = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		text7.setLayoutParams(text7Par);
		topBar.addView(text7);
		text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text6.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text7.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size14));
		text1.setTypeface(((MainActivity) context).openSansRegular);text2.setTypeface(((MainActivity) context).openSansRegular);text3.setTypeface(((MainActivity) context).openSansRegular);
        text4.setTypeface(((MainActivity) context).openSansRegular);text5.setTypeface(((MainActivity) context).openSansRegular);text6.setTypeface(((MainActivity) context).openSansRegular);
        text7.setTypeface(((MainActivity) context).openSansRegular);
      //*******************************************end topBar*********************************************************************************************
		
	
		
		FrameLayout frame = new FrameLayout(context);
		LayoutParams fp = new LayoutParams(wrapperWidth, LayoutParams.WRAP_CONTENT);
		frame.setLayoutParams(fp);
		wrapper.addView(frame);
		int rowHeight = itemHeight;
		//***************************************************Indexy******************************************************************************
		final LinearLayout indexLin = new LinearLayout(context);
		LinearLayout.LayoutParams indexPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		indexLin.setLayoutParams(indexPar);
		indexLin.setOrientation(LinearLayout.VERTICAL);
		indexLin.setBackgroundColor(Color.parseColor("#fafafa"));
		frame.addView(indexLin);
		for (int i=0;i<6;i++){
			LinearLayout row = new LinearLayout(context);
			LinearLayout.LayoutParams rowPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(rowPar);
			indexLin.addView(row);
			TextView textI1 = new TextView(context);		
			textI1.setText((i+1)+"");
			textI1.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams textI1Par = new LinearLayout.LayoutParams(itemWidth/3, rowHeight);
			textI1.setLayoutParams(textI1Par);
			textI1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI1.setTextColor(Color.parseColor("#b6b6b6"));
			row.addView(textI1);
			
			if (indexes.size()!=0){
			
			TextView textI2 = new TextView(context);
			textI2.setText(indexes.get(i).getTitul());
			textI2.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			LinearLayout.LayoutParams textI2Par = new LinearLayout.LayoutParams(itemWidth*5/3+2*itemMargin, rowHeight);
			textI2.setLayoutParams(textI2Par);
			textI2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI2.setTextColor(Color.parseColor("#222222"));
			row.addView(textI2);
			
			TextView textI3 = new TextView(context);
			textI3.setText(indexes.get(i).getKurz());
			textI3.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			LinearLayout.LayoutParams textI3Par = new LinearLayout.LayoutParams(itemWidth+itemMargin, rowHeight);
			textI3.setLayoutParams(textI3Par);
			textI3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI3.setTextColor(Color.parseColor("#222222"));
			row.addView(textI3);
			
			TextView textI4 = new TextView(context);
			textI4.setText(indexes.get(i).getZmena());
			textI4.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			LinearLayout.LayoutParams textI4Par = new LinearLayout.LayoutParams(itemWidth*2/3, rowHeight);
			textI4.setLayoutParams(textI4Par);
			textI4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI4.setTextColor(Color.parseColor("#222222"));
			row.addView(textI4);
			
			textI1.setTypeface(((MainActivity) context).openSansRegular);textI2.setTypeface(((MainActivity) context).openSansRegular);textI3.setTypeface(((MainActivity) context).openSansRegular);
            textI4.setTypeface(((MainActivity) context).openSansRegular);
			
			LinearLayout imgLin = new LinearLayout(context);
			imgLin.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams imgLinPar = new LinearLayout.LayoutParams(itemWidth/3, rowHeight);
			imgLin.setLayoutParams(imgLinPar);
			row.addView(imgLin);
			
			ImageView img = new ImageView(context);
			LinearLayout.LayoutParams imgPar = new LinearLayout.LayoutParams(itemHeight/3, itemHeight/2);
			img.setLayoutParams(imgPar);
			if (indexes.get(i).getZmena().startsWith("-")){
				img.setBackgroundResource(R.drawable.rate_down);
				} else img.setBackgroundResource(R.drawable.rate_up);
			imgLin.addView(img);
			
			}
			
		}
		
		
		//*********************************************Xetra**********************************************************************************
		final LinearLayout xetraLin = new LinearLayout(context);
		LinearLayout.LayoutParams xetraPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		xetraLin.setLayoutParams(xetraPar);
		xetraLin.setOrientation(LinearLayout.VERTICAL);
		xetraLin.setBackgroundColor(Color.parseColor("#fafafa"));
		frame.addView(xetraLin);
		for (int i=0;i<6;i++){
			LinearLayout row = new LinearLayout(context);
			LinearLayout.LayoutParams rowPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(rowPar);
			xetraLin.addView(row);
			TextView textI1 = new TextView(context);		
			textI1.setText((i+1)+"");
			textI1.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams textI1Par = new LinearLayout.LayoutParams(itemWidth/3, rowHeight);
			textI1.setLayoutParams(textI1Par);
			textI1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI1.setTextColor(Color.parseColor("#b6b6b6"));
			row.addView(textI1);
			
			if (xetras.size()!=0){
			
			TextView textI2 = new TextView(context);
			textI2.setText(xetras.get(i).getNazov());
			textI2.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			LinearLayout.LayoutParams textI2Par = new LinearLayout.LayoutParams(itemWidth*5/3+2*itemMargin, rowHeight);
			textI2.setLayoutParams(textI2Par);
			textI2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI2.setTextColor(Color.parseColor("#222222"));
			row.addView(textI2);
			
			TextView textI3 = new TextView(context);
			textI3.setText(xetras.get(i).getKurz());
			textI3.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			LinearLayout.LayoutParams textI3Par = new LinearLayout.LayoutParams(itemWidth+itemMargin, rowHeight);
			textI3.setLayoutParams(textI3Par);
			textI3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI3.setTextColor(Color.parseColor("#222222"));
			row.addView(textI3);
			
			TextView textI4 = new TextView(context);
			textI4.setText(xetras.get(i).getZmena());
			textI4.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			LinearLayout.LayoutParams textI4Par = new LinearLayout.LayoutParams(itemWidth*2/3, rowHeight);
			textI4.setLayoutParams(textI4Par);
			textI4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
			textI4.setTextColor(Color.parseColor("#222222"));
			row.addView(textI4);
			
			textI1.setTypeface(((MainActivity) context).openSansRegular);textI2.setTypeface(((MainActivity) context).openSansRegular);
            textI3.setTypeface(((MainActivity) context).openSansRegular);textI4.setTypeface(((MainActivity) context).openSansRegular);
			
			LinearLayout imgLin = new LinearLayout(context);
			imgLin.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams imgLinPar = new LinearLayout.LayoutParams(itemWidth/3, rowHeight);
			imgLin.setLayoutParams(imgLinPar);
			row.addView(imgLin);
			
			ImageView img = new ImageView(context);
			LinearLayout.LayoutParams imgPar = new LinearLayout.LayoutParams(itemHeight/3, itemHeight/2);
			img.setLayoutParams(imgPar);
			if (xetras.get(i).getZmena().startsWith("-")){
			img.setBackgroundResource(R.drawable.rate_down);
			} else img.setBackgroundResource(R.drawable.rate_up);
			imgLin.addView(img);
			
			}
			
		}
		
		//*********************************************Forex**********************************************************************************
				final LinearLayout forexLin = new LinearLayout(context);
				LinearLayout.LayoutParams forexPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
				forexLin.setLayoutParams(forexPar);
				forexLin.setOrientation(LinearLayout.VERTICAL);
				forexLin.setBackgroundColor(Color.parseColor("#fafafa"));
				frame.addView(forexLin);
				for (int i=0;i<6;i++){
					LinearLayout row = new LinearLayout(context);
					LinearLayout.LayoutParams rowPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
					row.setLayoutParams(rowPar);
					forexLin.addView(row);
					TextView textI1 = new TextView(context);		
					textI1.setText((i+1)+"");
					textI1.setGravity(Gravity.CENTER);
					LinearLayout.LayoutParams textI1Par = new LinearLayout.LayoutParams(itemWidth/3, rowHeight);
					textI1.setLayoutParams(textI1Par);
					textI1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI1.setTextColor(Color.parseColor("#b6b6b6"));
					row.addView(textI1);
					
					if (forexes.size()!=0){
					
					TextView textI2 = new TextView(context);
					textI2.setText(forexes.get(i).getMena());
					textI2.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					LinearLayout.LayoutParams textI2Par = new LinearLayout.LayoutParams(itemWidth*5/3+2*itemMargin, rowHeight);
					textI2.setLayoutParams(textI2Par);
					textI2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI2.setTextColor(Color.parseColor("#222222"));
					row.addView(textI2);
					
					TextView textI3 = new TextView(context);
					textI3.setText(forexes.get(i).getPocet());
					textI3.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					LinearLayout.LayoutParams textI3Par = new LinearLayout.LayoutParams(itemWidth+itemMargin, rowHeight);
					textI3.setLayoutParams(textI3Par);
					textI3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI3.setTextColor(Color.parseColor("#222222"));
					row.addView(textI3);
					
					TextView textI4 = new TextView(context);
					textI4.setText(forexes.get(i).getCena());
					textI4.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					LinearLayout.LayoutParams textI4Par = new LinearLayout.LayoutParams(itemWidth*2/3, rowHeight);
					textI4.setLayoutParams(textI4Par);
					textI4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI4.setTextColor(Color.parseColor("#222222"));
					row.addView(textI4);
					
					textI1.setTypeface(((MainActivity) context).openSansRegular);textI2.setTypeface(((MainActivity) context).openSansRegular);
                    textI3.setTypeface(((MainActivity) context).openSansRegular);textI4.setTypeface(((MainActivity) context).openSansRegular);
					
					
					}
				}
				
				//*********************************************Kurzy**********************************************************************************
				final LinearLayout kurzyLin = new LinearLayout(context);
				LinearLayout.LayoutParams kurzyPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
				kurzyLin.setLayoutParams(kurzyPar);
				kurzyLin.setOrientation(LinearLayout.VERTICAL);
				kurzyLin.setBackgroundColor(Color.parseColor("#fafafa"));
				frame.addView(kurzyLin);
				for (int i=0;i<6;i++){
					LinearLayout row = new LinearLayout(context);
					LinearLayout.LayoutParams rowPar = new LinearLayout.LayoutParams(wrapperWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
					row.setLayoutParams(rowPar);
					kurzyLin.addView(row);
					TextView textI1 = new TextView(context);		
					textI1.setText((i+1)+"");
					textI1.setGravity(Gravity.CENTER);
					LinearLayout.LayoutParams textI1Par = new LinearLayout.LayoutParams(itemWidth/3, rowHeight);
					textI1.setLayoutParams(textI1Par);
					textI1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI1.setTextColor(Color.parseColor("#b6b6b6"));
					row.addView(textI1);
					
					if (rates.size()!=0){
					
					TextView textI2 = new TextView(context);
					textI2.setText(rates.get(i).getMena());
					textI2.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					LinearLayout.LayoutParams textI2Par = new LinearLayout.LayoutParams(itemWidth*5/3+2*itemMargin, rowHeight);
					textI2.setLayoutParams(textI2Par);
					textI2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI2.setTextColor(Color.parseColor("#222222"));
					row.addView(textI2);
					
					TextView textI3 = new TextView(context);
					textI3.setText(rates.get(i).getKurz());
					textI3.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
					LinearLayout.LayoutParams textI3Par = new LinearLayout.LayoutParams(itemWidth+itemMargin, rowHeight);
					textI3.setLayoutParams(textI3Par);
					textI3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size12));
					textI3.setTextColor(Color.parseColor("#222222"));
					row.addView(textI3);					
					
					textI1.setTypeface(((MainActivity) context).openSansRegular);textI2.setTypeface(((MainActivity) context).openSansRegular);textI3.setTypeface(((MainActivity) context).openSansRegular);
					}
				}
		
				xetraLin.setVisibility(View.INVISIBLE);
            	forexLin.setVisibility(View.INVISIBLE);
            	kurzyLin.setVisibility(View.INVISIBLE);
		text1.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {            
            	text1.setBackgroundColor(Color.parseColor("#cecece"));
            	text2.setBackgroundResource(R.drawable.border);
            	text3.setBackgroundResource(R.drawable.border);
            	text4.setBackgroundResource(R.drawable.border);
            	indexLin.setVisibility(View.VISIBLE);
            	xetraLin.setVisibility(View.INVISIBLE);
            	forexLin.setVisibility(View.INVISIBLE);
            	kurzyLin.setVisibility(View.INVISIBLE);
            	text5.setText("Titul");
            	text6.setText("Kurz");
            	text7.setText(getResources().getString(R.string.change));
            	topBarWrapper.removeViewAt(0);
        		topBarWrapper.addView(bubble1, 0);
            }
        });
		
		text2.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {            
            	text2.setBackgroundColor(Color.parseColor("#cecece"));
            	text1.setBackgroundResource(R.drawable.border);
            	text3.setBackgroundResource(R.drawable.border);
            	text4.setBackgroundResource(R.drawable.border);
            	indexLin.setVisibility(View.INVISIBLE);
            	xetraLin.setVisibility(View.VISIBLE);
            	forexLin.setVisibility(View.INVISIBLE);
            	kurzyLin.setVisibility(View.INVISIBLE);
                text5.setText(getResources().getString(R.string.title));
            	text6.setText("Kurz");
            	text7.setText(getResources().getString(R.string.change));
            	topBarWrapper.removeViewAt(0);
        		topBarWrapper.addView(bubble2, 0);
            }
        });
		
		text3.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {            
            	text3.setBackgroundColor(Color.parseColor("#cecece"));
            	text1.setBackgroundResource(R.drawable.border);
            	text2.setBackgroundResource(R.drawable.border);
            	text4.setBackgroundResource(R.drawable.border);
            	indexLin.setVisibility(View.INVISIBLE);
            	xetraLin.setVisibility(View.INVISIBLE);
            	forexLin.setVisibility(View.VISIBLE);
            	kurzyLin.setVisibility(View.INVISIBLE);
            	text5.setText("Mena");
            	text6.setText("Počet");
            	text7.setText("Cena");
            	topBarWrapper.removeViewAt(0);
        		topBarWrapper.addView(bubble3, 0);
            }
        });
		
		text4.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {            
            	text4.setBackgroundColor(Color.parseColor("#cecece"));
            	text1.setBackgroundResource(R.drawable.border);
            	text2.setBackgroundResource(R.drawable.border);
            	text3.setBackgroundResource(R.drawable.border);
            	indexLin.setVisibility(View.INVISIBLE);
            	xetraLin.setVisibility(View.INVISIBLE);
            	forexLin.setVisibility(View.INVISIBLE);
            	kurzyLin.setVisibility(View.VISIBLE);
            	text5.setText("Mena");
            	text6.setText("Kurz");
            	text7.setText("");           	
        		topBarWrapper.removeViewAt(0);
        		topBarWrapper.addView(bubble4, 0);
            }
        });
		
	
		
		
	
		
		
		
	//	this.addView(cornerImg);
	}
	
	


	
	
	
	
	
	
	
	
	
	
}
