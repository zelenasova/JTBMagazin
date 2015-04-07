package jtb.magazin.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import jtb.magazin.Constants;
import jtb.magazin.CustomHttpClient;
import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.model.WOTModel;

public class WorldOfTags {

    String deviceHash;

    RelativeLayout bg;
    RelativeLayout world_overlay;
    private ArrayList<WOTModel> items;
    private ArrayList<DrawLine> lines;
    double w;
    double h;
    Context context;
    private static final float BITMAP_SCALE = 0.3f;
    private static final float BLUR_RADIUS = 25f;
    JSONObject json = null;
    JSONArray jsonBasic;
    ImageView close;
    LinearLayout lin_close;

    RenderScript rs;

    //trieda predstavuje hlavny layout krystal suvislosti
    //Snazime sa dostat 3D model krystalu do 2D a zobrazit na obrazovke. Krystal sa da rotovat a tento vypocet stale prebieha
    public WorldOfTags(Context context, String id, String source, WebView myWebView) {
        this.context = context;
        rs = RenderScript.create(context);
        //rozprestiera sa na celej sobrazovke
        w = ((MainActivity) context).screenWidth;
        h = ((MainActivity) context).screenHeight;

        //items predstavuju textViews a lines ciary
        items = new ArrayList<WOTModel>();
        lines = new ArrayList<DrawLine>();

        deviceHash = ((MainActivity) (context)).authHash;
        //hlavny layout je world ktory sa nachadza v activity_main

        bg = (RelativeLayout) ((MainActivity) context).findViewById(R.id.world_of_bg);
        world_overlay = (RelativeLayout) ((MainActivity) context).findViewById(R.id.world_of_tags_overlay);
        ((MainActivity) context).world_wrapper.setVisibility(View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT>17){
            Bitmap original = screenShot(myWebView);
            BitmapDrawable background = new BitmapDrawable(blur(context, original));
            background.setAlpha(220);
            bg.setBackground(background);
        }

        /*final Allocation input = Allocation.createFromBitmap(rs, original);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(25f);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(original);
		BitmapDrawable background = new BitmapDrawable(original);*/





        ((MainActivity) context).world.setVisibility(View.INVISIBLE);
        ((MainActivity) context).world.setClickable(true);


        float dx = (int) (w / Constants.WIDTH_KOEF);
        close = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (40 * dx), (int) (40 * dx));
        close.setLayoutParams(params);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        close.setBackgroundResource(R.drawable.close_wot);
        params.setMargins(0, (int) (20 * dx), (int) (20 * dx), 0);

        lin_close = new LinearLayout(context);
        RelativeLayout.LayoutParams lin_params = new RelativeLayout.LayoutParams((int) (100 * dx), (int) (100 * dx));
        lin_close.setLayoutParams(lin_params);
        lin_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lin_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);


        //pri zatvoreni sa odomknu lave a prave menu
        lin_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) WorldOfTags.this.context).removeWOT();
            }
        });
        world_overlay.addView(close);
        world_overlay.addView(lin_close);


        LoadKeywords task = new LoadKeywords();
        task.execute(new String[]{id, source});




    }

    public class LoadKeywords extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String id = urls[0];
            String source = urls[1];
            String response = "";

            try {
                //najprv musime skontaktovat server pre ziskanie keywords pre dany clanok
                String url = "http://194.50.215.137/api/common/get_similar_by_keywords/" + id + "/" + source + "/" + ((MainActivity) (context)).lang;
                File cacheFile = new File(((MainActivity) context).getExternalCacheDir().toString() + "/cache/wot.txt");


                if (!((MainActivity) (context)).isNetworkAvailable()) {
                    if (cacheFile.isFile()) {
                        String fromFile = ((MainActivity) (context)).readFromFile(cacheFile);
                        json = new JSONObject(fromFile);
                    } else
                        ((MainActivity) context).errorMessage(context.getString(R.string.error3), context.getString(R.string.error6));
                    //System.out.println(jsonAuth);
                } else {
                    response = CustomHttpClient.executeHttpGet(url, deviceHash);
                    json = new JSONObject(response);
                    //	System.out.println(json);
                    String jsonString = json.toString();
                    //	System.out.println(jsonString);
                    ((MainActivity) (context)).writeToFile(cacheFile, jsonString);
                }


            } catch (Exception e) {
                //error.setText(R.string.connection);
                System.out.println("from LoadKeywords doInBackground" + e);
                //((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error6));
            }
            return "";
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String result) {

            if (json != null) {

                try {
                    JSONArray jsonBasic = json.getJSONArray("result");
                    //vytvarame objekty typu WOTModel, nastavime parametre textView a pridame model do items
                    for (int i = 0; i < jsonBasic.length(); i++) {
                        JSONObject item = jsonBasic.getJSONObject(i);
                        String keyword = item.getString("keyword");
                        String keywordID = item.getString("keywordID");
                        JSONObject position = item.getJSONObject("position");
                        double x = Double.parseDouble(position.getString("x")) * 80;
                        double y = Double.parseDouble(position.getString("y")) * 80;
                        double z = Double.parseDouble(position.getString("z")) * 80;
                        WOTModel w = new WOTModel(context);
                        w.keyword=keyword;
                        w.keywordID=keywordID;
                        w.x=x;
                        w.y=y;
                        w.z=z;
                        w.text.setX(-800);
                        w.text.setY(-400);
                        w.text.setText(keyword);

                        w.text.setTypeface(((MainActivity) context).fedraSansAltProBook);
                        if (((MainActivity) context).articleWhiteBackground){
                            w.text.setTextColor(Color.BLACK);
                        } else {
                            w.text.setTextColor(Color.WHITE);
                        }




                        items.add(w);
                        ((MainActivity) context).world.addView(w.text);

                    }
                    //vytvarame ciary medzi textViews

                    for (int i = 0; i < items.size(); i++) {
                        for (int j = i + 1; j < items.size(); j++) {
                            DrawLine line = new DrawLine(context, items.get(i), items.get(j));
                            lines.add(line);
                            ((MainActivity) context).world.addView(line);
                        }
                    }

                    WOTController controller = new WOTController(context, items, lines, ((MainActivity) context).world, ((MainActivity) context).world_wrapper,world_overlay);
                    ((MainActivity) context).world.addView(controller);

                    System.out.println("worldVisible");



                } catch (Exception e) {
                    //error.setText(R.string.connection);
                    System.out.println("from LoadKeywords onPostExecute" + e);
                    //((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error6));
                }


            }

        }
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {


        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];

        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }


        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap blur(Context ctx, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

}
