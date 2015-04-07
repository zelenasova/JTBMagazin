package jtb.magazin.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import jtb.magazin.Constants;
import jtb.magazin.CustomHttpClient;
import jtb.magazin.Functions;
import jtb.magazin.MainActivity;

/**
 * Created by zelenasova on 9.3.2015.
 */
// stahuje system.zip potrebny pre zobrazenie detailov clanku
public class LoadSystem extends AsyncTask<String, String, String> {

    private Context context;
    String source;
    String versionLastChange;

    public LoadSystem(Context context, String versionLastChange)
    {
        this.context = context;
        this.versionLastChange=versionLastChange;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivity)context).dialog_lin.setVisibility(View.VISIBLE);
        ((MainActivity)context).dialog.setProgress(0);


    }

    @Override
    protected String doInBackground(String... urls) {
        String zipUrl = urls[0];
        source = urls[1];
        int count;
        try {
                /*Log.w("jeden",zipUrl);
				//URL url = new URL(zipUrl);
                URL url = new URL("https://194.50.215.138/tutorial.zip");
				URLConnection conection = url.openConnection();
				conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),8192);*/

            //HttpClient client = CustomHttpClient.getHttpsClient(new DefaultHttpClient());
            HttpClient client = CustomHttpClient.getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(zipUrl));
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream input = entity.getContent();
            long lenghtOfFile = entity.getContentLength();

            File newDirectory = new File(((MainActivity)context).getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/"+source+"/");
            if (!newDirectory.isDirectory()) newDirectory.mkdirs();
            File file = new File(((MainActivity)context).getExternalCacheDir().toString() + "/"+ Constants.STORAGE_FILE + "/articles/"+source+"/"+source+".zip");
            System.out.println(file.toString());
            OutputStream output = new FileOutputStream(file);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    output.flush();
                    output.close();
                    input.close();
                    return null;
                }

                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                System.out.println("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
                // SystemClock.sleep(20);
            }
            output.flush();
            output.close();
            input.close();
            CustomHttpClient.unpackZip(((MainActivity)context).getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/"+source+"/",source+".zip");
            File forDelete = new File(((MainActivity)context).getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/"+source+"/",source+".zip");
            forDelete.delete();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            File directoryForDelete = new File(((MainActivity)context).getExternalCacheDir().toString()+ "/"+ Constants.STORAGE_FILE+ "/articles/"+source);
            Functions.deleteDirectory(directoryForDelete);
        }

        return null;
    }

    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        ((MainActivity)context).dialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            ((MainActivity)context).prefs.edit().putString("versionSystem", versionLastChange).commit();
            ((MainActivity)context).dialog_lin.setVisibility(View.INVISIBLE);
            System.out.println("nacital som cely system");
        } catch (Exception e) {
            System.out.println("Exception from LoadSystem: "+e);
        }
    }

    @Override
    protected void onCancelled(){
        System.out.println("uzatvaram LoadSystem");
        System.out.println("vymazavam priecinky systemu");
        File forDelete = new File(((MainActivity)context).getExternalCacheDir().toString() + "/" + Constants.STORAGE_FILE + "/articles/"+source+"/",source+".zip");
        forDelete.delete();
        File directoryForDelete = new File(((MainActivity)context).getExternalCacheDir().toString()+ "/"+ Constants.STORAGE_FILE+ "/articles/"+source);
        Functions.deleteDirectory(directoryForDelete);

    }
}