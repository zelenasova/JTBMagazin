package jtb.magazin.magnus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jtb.magazin.Constants;
import jtb.magazin.MagnusFragment;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.MagnusListModel;

// tato trieda predstavuje jeden magazin, ktory tvoria vrstvy nad sebou. Na spodku je obrazok ktory tvori tien, nad nim obrazok ktory tahame z modelu,
// dalej ciernobiely obrazok, ktory sa objavi az po kliku na magazin a na vrchu je nadpis a cislo magazinu
@SuppressLint("ViewConstructor")
public class MagnusListFrame extends FrameLayout {
	DisplayImageOptions options;
	ImageLoader imageLoader;
	Context context;
	ImageView img;
	ImageView img2;
	ImageView shadow;
	private int actualWidth;
	DisplayMetrics metrics;
	public MagnusListModel magnusListModel;
	MagnusListFragment magnusListFragment;
	boolean open=false;
	boolean img2Loaded = false;
	TextView title;
	TextView subTitle;
	
	public int getActualWidth() {
		return actualWidth;
	}
	
	private int actualHeight;
	public int getActualHeight() {
		return actualHeight;
	}
	
	public MagnusListFrame(Context context, MagnusListModel magnusListModel, MagnusListFragment magnusListFragment, float w, float h) {
		super(context);
		this.setBackgroundColor(Color.parseColor("#cccccc"));
		this.context=context;
		this.magnusListModel=magnusListModel;
		metrics = getResources().getDisplayMetrics();
		img = new ImageView(context);
		img2 = new ImageView(context);
		shadow = new ImageView(context);
		imageLoader=((MainActivity)context).imageLoader;
		options=((MainActivity)context).options;
		this.magnusListFragment=magnusListFragment;
		
		final ProgressBar spinner = new ProgressBar(context);
		spinner.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
		RelativeLayout.LayoutParams spinner_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams spinner_lin_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		final LinearLayout spinner_lin = new LinearLayout(context);
		spinner_lin.setBackgroundColor(Color.WHITE);
		spinner_lin.setGravity(Gravity.CENTER);
		spinner.setLayoutParams(spinner_params);
		spinner_lin.setLayoutParams(spinner_lin_params);
		spinner_lin.addView(spinner);
		
		
		
		//nacitava sa zakladny obrazok pre magazin
		imageLoader.displayImage(magnusListModel.getCover1(), img, options, new SimpleImageLoadingListener() {
 			@Override
 			public void onLoadingStarted(String imageUri, View view) {
 				spinner_lin.setVisibility(View.VISIBLE);
 			}
 			
 			@Override
 			public  void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
 				spinner_lin.setVisibility(View.GONE);
 			}
 		});
		shadow.setBackgroundResource(R.drawable.cover_bg);
		
		
		
		RelativeLayout rel = new RelativeLayout(context);
		RelativeLayout.LayoutParams relPar = new RelativeLayout.LayoutParams((int) (w/523*505),(int) (h/668*632));
		RelativeLayout.LayoutParams titlePar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams subTitlePar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rel.setLayoutParams(relPar);
		title = new TextView(context);
		subTitle = new TextView(context);
		title.setText(magnusListModel.getTitle());
		subTitle.setText(magnusListModel.getSubTitle());
    	title.setTypeface(((MainActivity) context).giorgioSansWebBold);
    	subTitle.setTypeface(((MainActivity) context).giorgioSansWebBold);
    	title.setTextColor(Color.WHITE);
    	subTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size30));
    	subTitle.setTextColor(Color.WHITE);
		title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size60));
		float mw = metrics.widthPixels;
		float dx = mw/Constants.WIDTH_KOEF;
		title.setShadowLayer(dx*2, dx, dx, Color.BLACK);
		title.setPadding((int)(10*dx), 0, 0, (int)(5*dx));
		subTitle.setShadowLayer(dx*2, dx, dx, Color.BLACK);
		subTitle.setPadding((int)(10*dx), 0, 0, (int)(5*dx));
		title.setLayoutParams(titlePar);
		subTitle.setLayoutParams(subTitlePar);
		subTitlePar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		titlePar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		title.setVisibility(View.INVISIBLE);
		subTitle.setVisibility(View.INVISIBLE);
		img.setScaleType(ScaleType.FIT_XY);
		img2.setScaleType(ScaleType.FIT_XY);
		shadow.setScaleType(ScaleType.FIT_XY);
		img2.setVisibility(View.INVISIBLE);
		this.setBackgroundColor(Color.parseColor("#60cccccc"));
		
		// rel uz je trosku zmenseny aby pasoval do vyrezu na pozadi ktory tvori obrazok s tienom
		this.addView(shadow);
		this.addView(spinner_lin);
		rel.addView(img);
		rel.addView(img2);	
		rel.addView(title);
		rel.addView(subTitle);
		this.addView(rel,relPar);
		
		//System.out.println(magnusListModel.getCover1());
		//System.out.println(magnusListModel.getCover2());
		//su dve mozne stavy ktore predstavuje premenna open
		this.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//ak uz je nejaky magazin otvoreny zatvori sa
				if (((MainActivity)MagnusListFrame.this.context).openMagnusListFrame !=null){
					((MainActivity)MagnusListFrame.this.context).openMagnusListFrame.img2.setVisibility(View.INVISIBLE);
					((MainActivity)MagnusListFrame.this.context).openMagnusListFrame.title.setVisibility(View.INVISIBLE);
					((MainActivity)MagnusListFrame.this.context).openMagnusListFrame.subTitle.setVisibility(View.INVISIBLE);
					
				}
				
				if (open){
					try {
						// vo fragmente MagnusFragment su funkcie na komunikaciu medzi fragmentmi, vola sa displayMagazinObsah() ktora 
						//prida dalsi fragment nad listFragment.
						MagnusFragment mf = (MagnusFragment) MagnusListFrame.this.magnusListFragment.getParentFragment();
						mf.displayMagazinObsah(MagnusListFrame.this.magnusListModel.getId(),MagnusListFrame.this.magnusListModel.getLastChange(),
						MagnusListFrame.this.magnusListModel.getTitle(),MagnusListFrame.this.magnusListModel.getSubTitle());		
						open = false;
						((MainActivity)MagnusListFrame.this.context).openMagnusListFrame.open=false;
						//((MainActivity)MagnusListFrame.this.context).openMagnusListFrame =null;
						//img2.setVisibility(View.INVISIBLE);
					} catch (ClassCastException e) {
                        System.out.println(e);
					} 
				} else {
					//prvykrat sa klikne na magazin, zobrazi sa ciernobiely obrazok a texty, a zaznamena sa do MainActivity ze je otvoreny magazin
					img2.setVisibility(View.VISIBLE);
					if (!img2Loaded){
					imageLoader.displayImage(MagnusListFrame.this.magnusListModel.getCover2(), img2, options, new SimpleImageLoadingListener() {});
					img2Loaded=true;
					}
					title.setVisibility(View.VISIBLE);
					subTitle.setVisibility(View.VISIBLE);	
					if (((MainActivity)MagnusListFrame.this.context).openMagnusListFrame !=null){
					((MainActivity)MagnusListFrame.this.context).openMagnusListFrame.open=false;
					}
					open=true;
					((MainActivity)MagnusListFrame.this.context).openMagnusListFrame=MagnusListFrame.this;
				}
				// drop the view
				
			}
		});
	

	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);		
		actualWidth = MeasureSpec.getSize(widthMeasureSpec);
		actualHeight = MeasureSpec.getSize(heightMeasureSpec);
	}
	
	

	
}
