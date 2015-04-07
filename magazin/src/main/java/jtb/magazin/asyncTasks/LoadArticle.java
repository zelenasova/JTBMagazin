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

import jtb.magazin.CustomHttpClient;
import jtb.magazin.DetailFragment;
import jtb.magazin.Functions;
import jtb.magazin.MainActivity;
import jtb.magazin.R;

/**
 * Created by zelenasova on 9.3.2015.
 */
// v osobitnom vlakne nacita vsetky potrebne story
public class LoadArticle extends AsyncTask<String, String, String> {

    private Context context;
    private DetailFragment detail;
    boolean error=false;
    String newDir;

    public LoadArticle(Context context, DetailFragment detail) {
        this.detail = detail;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivity) context).dialog_lin.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... urls) {
        //***************************************nove***************************
        String zipUrl = urls[0];
        newDir = urls[1];
        String zipFile;
        int count;
        try {
                            /*URL url = new URL(zipUrl);
			                URLConnection conection = url.openConnection();
			                conection.connect();
			                // getting file length
			                int lenghtOfFile = conection.getContentLength();

			                // input stream to read file - with 8k buffer
			                InputStream input = new BufferedInputStream(url.openStream(), 8192);*/

            //HttpClient client = CustomHttpClient.getHttpsClient(new DefaultHttpClient());
            HttpClient client = CustomHttpClient.getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(zipUrl));
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream input = entity.getContent();
            long lenghtOfFile = entity.getContentLength();

            // Output stream to write file
            File newDirectory = new File(((MainActivity) context).getExternalCacheDir().toString() + newDir);
            if (!newDirectory.isDirectory()) newDirectory.mkdirs();
            File file = new File(((MainActivity) context).getExternalCacheDir().toString() + newDir + "/article.zip");
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
                
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 50) / lenghtOfFile));
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            System.out.println("Error from LoadArtcile: " + e.getMessage());
            error=true;
            zipFile = ((MainActivity) context).getExternalCacheDir().toString() + newDir + "/";
            File forDelete = new File(zipFile + "article.zip");
            forDelete.delete();
        }

        zipFile = ((MainActivity) context).getExternalCacheDir().toString() + newDir + "/";
        CustomHttpClient.unpackZip(zipFile, "article.zip");
        File forDelete = new File(zipFile + "article.zip");
        forDelete.delete();
        //  DetailFragment.this.myWebView.loadUrl(urls[0]);

        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        ((MainActivity) context).dialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        //((MainActivity)context).dialog_lin.setVisibility(View.INVISIBLE);
        try {
            if (error){
                ((MainActivity)context).spinner.setVisibility(View.INVISIBLE);
                ((MainActivity)context).dialog_lin.setVisibility(View.INVISIBLE);
                ((MainActivity)context).errorMessage(((MainActivity)context).getString(R.string.error3),((MainActivity)context).getString(R.string.error14));
                return;
            }
            detail.loadUrl();
            detail.handleRelated();
        } catch (Exception e) {
            System.out.println("Exception from LoadArticle: "+e);
        }


    }

    @Override
    protected void onCancelled(){
        System.out.println("uzatvaram LoadArticle");
        System.out.println("vymazavam article");
        String zipFile = ((MainActivity) context).getExternalCacheDir().toString() + newDir + "/";
        File forDelete = new File(zipFile + "article.zip");
        forDelete.delete();
        File directoryForDelete = new File(((MainActivity) context).getExternalCacheDir().toString() + newDir);
        Functions.deleteDirectory(directoryForDelete);

    }
}
