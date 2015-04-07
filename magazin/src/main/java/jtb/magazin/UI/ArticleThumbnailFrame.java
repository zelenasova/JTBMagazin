package jtb.magazin.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import jtb.magazin.Constants;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.ArticleModel;

//predstavuje jeden frame v hlavnom gride
//mame nad sebou vrstvy - obrazok, datum, perex (ktory je standardne skryty),nadpis, vrstva MyMagnusPaint kde su osetrene kliky, a vrstva MagnusArticleControlsLayer kde sa zobrazuje
//moznost pridat clanok medzi ulozene ak sa dlho podrzi prst na clanok
@SuppressLint("ViewConstructor")
public class ArticleThumbnailFrame extends FrameLayout {
    DisplayImageOptions options;
    ImageLoader imageLoader;
    ArticleModel model;
    public LinearLayout black;
    Context context;
    ImageView img;
    ImageView cb;
    public TextView text2;
    RelativeLayout rel;
    int konstantaPadding = 110;
    public MyPaint viewPaint;
    private int actualHeight;

    public int getActualHeight() {
        return actualHeight;
    }

    public ArticleThumbnailFrame(Context context) {
        super(context);
        this.setBackgroundColor(Color.parseColor("#60cccccc"));

    }

    public ArticleThumbnailFrame(Context context,boolean loader) {
        super(context);
        this.setBackgroundColor(Color.parseColor("#60cccccc"));
        final ProgressBar spinner = new ProgressBar(context);
        spinner.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
        float dx = ((MainActivity) context).screenWidth / Constants.WIDTH_KOEF;
        RelativeLayout.LayoutParams spinner_params = new RelativeLayout.LayoutParams((int) (15 * dx), (int) (15 * dx));
        RelativeLayout rel = new RelativeLayout(context);
        this.addView(rel);
        spinner_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        spinner.setLayoutParams(spinner_params);
        rel.addView(spinner);
    }

    public ArticleThumbnailFrame(final Context context,  final ArticleModel model, int frameHeight) {
        super(context);
        // TODO Auto-generated constructor stub
        this.options = ((MainActivity) context).options;
        this.context = context;
        this.imageLoader = ((MainActivity) context).imageLoader;
        this.model = model;
        this.actualHeight = frameHeight;
        img = new ImageView(context);
        cb = new ImageView(context);
        this.context = context;
        this.model = model;
        this.setBackgroundColor(Color.parseColor("#60cccccc"));

        float mh = ((MainActivity) context).screenHeight;
        final float mw = ((MainActivity) context).screenWidth;

        final int imgWidth = (int) (mw / 64);
        int imgWidth2 = (int) (mw / 64);
        final int imgMargin = (int) (mw / 64 / 2);
        final float dx = mw / Constants.WIDTH_KOEF;
        final float dy = mh / Constants.WIDTH_KOEF;


        final ProgressBar spinner = new ProgressBar(context);
        spinner.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));

        //imageLoader automaticky uklada obrazky do cache, ak last change je iny ako ulozeny vtedy sa cache obrazok musi vymazat
        if (model.getImageUrl() != null) {
            /*HashMap<String, String> urlsMap = ((MainActivity) context).urlsMap;
            if (urlsMap.containsKey(model.getImageUrl())) {
                String savedLastChange = urlsMap.get(model.getImageUrl());

                if (!(savedLastChange.equals(model.getLastChange()))) {
                    System.out.println(model.getImageUrl());
                    System.out.println(savedLastChange);
                    System.out.println(model.getLastChange());
                    File cachedImage = DiscCacheUtils.findInCache(model.getImageUrl(), ImageLoader.getInstance().getDiscCache());
                    if (cachedImage != null) {
                        if (cachedImage.exists()) {
                            cachedImage.delete();
                            System.out.println("vymazavam obrazok z cache: " + cachedImage.toString());
                        }
                    }
                    urlsMap.put(model.getImageUrl(), model.getLastChange());
                    ((MainActivity) context).setImagesLastChange(urlsMap);
                    ((MainActivity) context).urlsMap = urlsMap;
                }
            }  else {
                urlsMap.put(model.getImageUrl(), ArticleThumbnailFrame.this.model.getLastChange());
                ((MainActivity) context).setImagesLastChange(urlsMap);
            }*/
            spinner.setVisibility(View.INVISIBLE);
            imageLoader.displayImage(model.getImageUrl(), img, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    if ((!((MainActivity) context).logged) && (model.getLocked().equals("1"))) {
                        cb.setBackground(new BitmapDrawable(convertColorIntoBlackAndWhiteImage(loadedImage)));
                    }
                    spinner.setVisibility(View.GONE);
                }
            });

        }
        //text1 - datum
        //text2 - nadpis
        //text3 - perex
        //lin obsahuje datum a pripadne lock obrazok ak je clanok zamknuty a uzivatel nie je prihlaseny
        //rel obsahuje lin,text2 a text3

        rel = new RelativeLayout(context);
        RelativeLayout rel2 = new RelativeLayout(context);
        LinearLayout lin = new LinearLayout(context);
        final int text1Height = (int) (30 * dy);
        LinearLayout.LayoutParams linPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, text1Height);
        lin.setLayoutParams(linPar);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams spinner_params = new RelativeLayout.LayoutParams((int) (15 * dx), (int) (15 * dx));
        LinearLayout spinner_lin = new LinearLayout(context);
        spinner_lin.setGravity(Gravity.RIGHT);
        spinner.setLayoutParams(spinner_params);
        spinner_lin.addView(spinner);
        //	RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(w, 50);
        rel.setPadding((int) mw / 70, 0, (int) mw / konstantaPadding, (int) mw / konstantaPadding);
        TextView text1 = new TextView(context);
        text2 = new TextView(context);

        text1.setId(R.id.text1);
        text2.setId(R.id.text2);
        this.setBackgroundColor(android.graphics.Color.parseColor("#5b707b"));

        //params2.addRule(RelativeLayout.ABOVE, text3.getId());
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params1.addRule(RelativeLayout.ABOVE, text2.getId());
        rel.addView(lin, params1);
        rel2.setBackgroundColor(Color.WHITE);
        lin.setOrientation(LinearLayout.HORIZONTAL);

        rel.addView(text2, params2);


        text1.setTypeface(((MainActivity) context).openSansRegular);
        text2.setTypeface(((MainActivity) context).fedraSansAltProBold);
        if (model.getDatePublish() != null) {
            text1.setText(model.getDatePublish());
            text1.setPadding((int) (10 * dx), 0, (int) (10 * dx), 0);
            text1.setGravity(Gravity.CENTER);
            text1.setBackgroundColor(Color.parseColor("#90000000"));
            text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size9));
            text1.setTextColor(Color.WHITE);
        }
        if (model.getPrice()!= null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat format = new DecimalFormat("#,###", symbols);
            String count = format.format(Integer.parseInt(model.getPrice()));
            text1.setText(count+" bonov");
            text1.setPadding((int) (10 * dx), 0, (int) (10 * dx), 0);
            text1.setGravity(Gravity.CENTER);
            text1.setBackgroundColor(Color.parseColor("#90000000"));
            text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size9));
            text1.setTextColor(Color.WHITE);
        }
        if (model.getTitle()!=null){
            String title = model.getTitle();
            title = title.replace("\\n"," ");
            text2.setText(title);
        }

        text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.size15));
        text2.setShadowLayer(dx * 2, dx, dx, Color.BLACK);
        text2.setTextColor(Color.WHITE);
        text2.setMaxLines(3);
        img.setScaleType(ScaleType.CENTER_CROP);

        black = new LinearLayout(context);
        black.setBackgroundColor(Color.parseColor("#80000000"));
        black.setVisibility(View.INVISIBLE);

        this.setBackgroundColor(Color.parseColor("#60cccccc"));
        // *******************************************************lock clanok*************************************************
        //ak je zamknuty clanok tak obrazok je ciernobiely
        /*if ((!((MainActivity) context).logged) && (model.getLocked().equals("1"))) {
            if (!(model.getSource().equals("3"))) {
                ImageView lock = new ImageView(context);
                LinearLayout.LayoutParams lockPar = new LinearLayout.LayoutParams(text1Height, text1Height);
                lockPar.setMargins(0, 0, (int) (5 * dx), 0);
                lock.setImageDrawable(getResources().getDrawable(R.drawable.locked));
                lock.setLayoutParams(lockPar);
                lin.addView(lock);
            }
            this.addView(cb);
        } else {
            this.addView(img);
        }*/
        if ((!((MainActivity) context).logged) && (model.getLocked().equals("1"))) {
            //if (!(model.getSource().equals("3"))) {
                ImageView lock = new ImageView(context);
                LinearLayout.LayoutParams lockPar = new LinearLayout.LayoutParams(text1Height, text1Height);
                lockPar.setMargins(0, 0, (int) (5 * dx), 0);
                lock.setImageDrawable(getResources().getDrawable(R.drawable.locked));
                lock.setLayoutParams(lockPar);
                lin.addView(lock);
            //}
            this.addView(cb);
        } else {
            this.addView(img);
        }
        if (model.getSource().equals("3")) {
            ImageView magnus = new ImageView(context);
            LinearLayout.LayoutParams magnusPar = new LinearLayout.LayoutParams(text1Height, text1Height);
            magnusPar.setMargins(0, 0, (int) (5 * dx), 0);
            magnus.setImageDrawable(getResources().getDrawable(R.drawable.magnus_article_icon));
            magnus.setLayoutParams(magnusPar);
            lin.addView(magnus);
        }
        if ((model.getDatePublish() != null)||(model.getPrice() != null)) lin.addView(text1, linPar);
        this.addView(spinner_lin);
        this.addView(black);
        this.addView(rel, params1);

        //osetruje kliky na clanok
        viewPaint = new MyPaint(context, imgWidth, imgMargin, text1, text2, black, actualHeight, model, (int) mw / konstantaPadding);
        this.addView(viewPaint);

    }


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
