package jtb.magazin.magnus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
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
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;
//trieda predstavuje jeden clanok v ramci jedneho magazinu
// mame nad sebou vrstvy - obrazok, nadpis, vrstva MyMagnusPaint kde su osetrene kliky, a vrstva MagnusArticleControlsLayer kde sa zobrazuje
//moznost pridat clanok medzi ulozene ak sa dlho podrzi prst na clanok
@SuppressLint("ViewConstructor")
public class MagnusArticleFrame extends FrameLayout {
	DisplayImageOptions options;
	ImageLoader imageLoader;
	Context context;
	ImageView img;
	private int actualWidth;
	MyMagnusPaint viewPaint;
	DisplayMetrics metrics;
	ImageView cb;
	ArticleModel magnusArticleModel;
	 String magazinLastChange;
	 MagnusArticleControlsLayer controlsLayer;
	 private int actualHeight;
	
	
	
	public MagnusArticleFrame(final Context context, final ArticleModel magnusArticleModel, String magazinLastChange) {
		super(context);
		this.setBackgroundColor(Color.parseColor("#cccccc"));
		this.context=context;
		this.magnusArticleModel=magnusArticleModel;
		metrics = getResources().getDisplayMetrics();
		img = new ImageView(context);
		imageLoader=((MainActivity)context).imageLoader;
		options=((MainActivity)context).options;
		this.magazinLastChange=magazinLastChange;
		//ciernobiely obrazok
		cb=new ImageView(context);
		
		
		
		final ProgressBar spinner = new ProgressBar(context);
		spinner.setVisibility(View.INVISIBLE);
		imageLoader.displayImage(magnusArticleModel.getImageUrl(), img, options, new SimpleImageLoadingListener() {
 			@Override
 			public void onLoadingStarted(String imageUri, View view) {
 				spinner.setVisibility(View.VISIBLE);
 			}
 			/*@Override
 			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
 				String message = null;
 				switch (failReason.getType()) {
 					case IO_ERROR:
 						message = "Input/Output error";
 						break;
 					case DECODING_ERROR:
 						message = "Image can't be decoded";
 						break;
 					case NETWORK_DENIED:
 						message = "Downloads are denied";
 						break;
 					case OUT_OF_MEMORY:
 						message = "Out Of Memory error";
 						break;
 					case UNKNOWN:
 						message = "Unknown error";
 						break;
 				}
 				
 				
 				
 			//	Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
 				//spinner.setVisibility(View.GONE);
 			}*/
 			@Override
 			public  void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
 				//ak je clanok zamknuty tak sa obrazok zmeni na ciernobiely
 				if ((!((MainActivity)context).logged)&&(magnusArticleModel.getLocked().equals("1"))){
 					cb.setBackground(new BitmapDrawable(convertColorIntoBlackAndWhiteImage(loadedImage)));	
 				}
 				spinner.setVisibility(View.GONE);
 			}
 		});
		int p = metrics.widthPixels/Constants.MAGNUS_ARTICLE_MARGIN_WIDTH_KOEF;
		RelativeLayout.LayoutParams spinner_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout spinner_lin = new LinearLayout(context);
		spinner_lin.setGravity(Gravity.CENTER);
		spinner.setLayoutParams(spinner_params);
		spinner_lin.addView(spinner);
		float mw=metrics.widthPixels;
		float dx = mw/Constants.WIDTH_KOEF;
		
		LinearLayout lin = new LinearLayout(context);
		LinearLayout.LayoutParams linPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		//System.out.println(actualHeight);
		lin.setLayoutParams(linPar);
		lin.setGravity(Gravity.BOTTOM);
		TextView title = new TextView(context);
		String string = magnusArticleModel.getTitle();
		//System.out.println(string);
		String newString=string.replace("\\n", "<br />");
		title.setText(Html.fromHtml(newString));
		title.setTextColor(Color.WHITE);
		title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size21));
		title.setShadowLayer(dx*2, dx, dx, Color.BLACK);
		title.setPadding(2*p, 0, 0, p);
    	title.setTypeface(((MainActivity) context).giorgioSansWebBold);
		lin.addView(title);
		img.setScaleType(ScaleType.CENTER_CROP);
			
		this.setBackgroundColor(Color.parseColor("#60cccccc"));
		if ((!((MainActivity)context).logged)&&(magnusArticleModel.getLocked().equals("1"))){
			this.addView(cb);
		} else {
			this.addView(img);			
		}
		this.addView(lin,linPar);
		//ak je zamknuty clanok a uzivatel nie je prihlaseny zobrazi sa obrazok lock
		if (magnusArticleModel.getLocked().equals("1")){
			ImageView locked = new ImageView(context);
			locked.setBackgroundResource(R.drawable.magnus_lock);
			LinearLayout lin_lock = new LinearLayout(context);
			LinearLayout.LayoutParams lin_lock_par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			lin_lock.setLayoutParams(lin_lock_par);
			lin_lock.setGravity(Gravity.RIGHT);		
			LinearLayout.LayoutParams locked_par = new LinearLayout.LayoutParams((int)(18*dx), (int)(27*dx));
			locked_par.setMargins(0, (int)(10*dx), (int)(10*dx), 0);
			lin_lock.addView(locked,locked_par);
			this.addView(lin_lock);
		}
		
		//vrstva na vrchu kde je moznost pridat clanok medzi ulozene
		controlsLayer = new MagnusArticleControlsLayer(context,magnusArticleModel);
		controlsLayer.setVisibility(View.INVISIBLE);
		
		//obsluhuje kliky
		viewPaint = new MyMagnusPaint(context, controlsLayer,actualHeight,magnusArticleModel);
		this.addView(viewPaint);
		this.addView(controlsLayer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		
		this.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
			}
		});
	

	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);		
		actualWidth = MeasureSpec.getSize(widthMeasureSpec);
		actualHeight = MeasureSpec.getSize(heightMeasureSpec);
	}
	
	public int getActualWidth() {
		return actualWidth;
	}
	
	
	public int getActualHeight() {
		return actualHeight;
	}
	
	//konvertuje obrazok na ciernobiely
	private Bitmap convertColorIntoBlackAndWhiteImage(Bitmap orginalBitmap) {
	    ColorMatrix colorMatrix = new ColorMatrix();
	    colorMatrix.setSaturation(0);

	    ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
	            colorMatrix);

	    Bitmap blackAndWhiteBitmap = orginalBitmap.copy(
	            Bitmap.Config.ARGB_8888, true);

	    Paint paint = new Paint();
	    paint.setColorFilter(colorMatrixFilter);

	    Canvas canvas = new Canvas(blackAndWhiteBitmap);
	    canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);

	    return blackAndWhiteBitmap;
	}
	

	
}
