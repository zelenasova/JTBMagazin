package jtb.magazin.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

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
import jtb.magazin.MainActivity;

/**
 * Created by zelenasova on 9.3.2015.
 */
// v osobitnom vlakne nacita vsetky potrebne story
public class LoadArticleOnBackground extends AsyncTask<String, String, String> {

    private Context context;
    String newDir;

    public LoadArticleOnBackground(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urls) {
        //***************************************nove***************************
        String zipUrl = urls[0];
        newDir = urls[1];
        String zipFile;
        int count;
        try {
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
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            System.out.println("Error from LoadArticlaOnBackground: " + e.getMessage());
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

    protected void onProgressUpdate(String... progress) {
    }

    @Override
    protected void onPostExecute(String result) {
        //((MainActivity)context).dialog_lin.setVisibility(View.INVISIBLE);
    }


}
