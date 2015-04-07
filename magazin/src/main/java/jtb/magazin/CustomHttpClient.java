package jtb.magazin;

import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//nachadzaju sa funkcie pre komunikaciu so serverom
public class CustomHttpClient {
	/** The time it takes for our client to timeout */
    public static final int HTTP_TIMEOUT = 20 * 1000; // milliseconds

    /** Single instance of our HttpClient */
    private static HttpClient mHttpClient;

    public static HttpClient getHttpsClient(HttpClient client) {
        try{
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    public static String executeHttpsPost(String url,String auth,ArrayList<NameValuePair> postParameters) throws URISyntaxException {
        String resutString="";
        StringBuilder builder = new StringBuilder();
        HttpClient client = getHttpsClient(new DefaultHttpClient());
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        try {
            HttpPost request = new HttpPost(url);
            request.setHeader("Authorization","Bearer "+auth);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            System.out.println("pred execute");
            HttpResponse response = client.execute(request);
            System.out.println("po execute");
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entityResponse = response.getEntity();
                InputStream content = entityResponse.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line=null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line+"\n");
                }
                reader.close();
                resutString=builder.toString();

            } else {

            }
        } catch (ConnectTimeoutException e) {
            Log.w("Connection Tome Out", e);
        } catch (ClientProtocolException e) {
            Log.w("ClientProtocolException", e);
        } catch (SocketException e) {
            Log.w("SocketException", e);
        } catch (IOException e) {
            Log.w("IOException", e);
        }
        return resutString;
    }

    public static String executeHttpsGet(String url,String auth) throws URISyntaxException {
        String resutString="";
        StringBuilder builder = new StringBuilder();
        HttpClient client = getHttpsClient(new DefaultHttpClient());
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        try {

            HttpGet request = new HttpGet();
            request.setHeader("Authorization","Bearer "+auth);
            request.setURI(new URI(url));

            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entityResponse = response.getEntity();
                InputStream content = entityResponse.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line=null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line+"\n");
                }
                reader.close();
                resutString=builder.toString();

            } else {

            }
        } catch (ConnectTimeoutException e) {
            System.out.println("Connection Tome Out:"+ e);
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException:"+ e);
        } catch (SocketException e) {
            System.out.println("SocketException:"+ e);
        } catch (IOException e) {
            System.out.println("IOException:"+e);
        }
        return resutString;
    }
    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     */
    public static HttpClient getHttpClient() {

            HttpClient mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);

        return mHttpClient;
    }


    public static boolean unpackZip(String path, String zipname)
    {       
         InputStream is;
         ZipInputStream zis;
         try 
         {
             String filename;
             is = new FileInputStream(path + zipname);
             zis = new ZipInputStream(new BufferedInputStream(is));          
             ZipEntry ze;
             byte[] buffer = new byte[1024];
             int count;

             while ((ze = zis.getNextEntry()) != null) 
             {
                 // zapis do souboru
                 filename = ze.getName();
                 if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                 }
                 FileOutputStream fout = new FileOutputStream(path + filename);
                 // cteni zipu a zapis
                 while ((count = zis.read(buffer)) != -1) 
                 {
                     fout.write(buffer, 0, count);             
                 }
                 fout.close();               
                 zis.closeEntry();
             }

             zis.close();
         } 
         catch(IOException e)
         {
             e.printStackTrace();
             return false;
         }

        return true;
    }
    
    
    public static void downloadFile(String url,String newDir) throws Exception {       
       

            try {
            	HttpClient client = getHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                 //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
      
                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File newDirectory = new File(Environment.getExternalStorageDirectory() + newDir);
                if(!newDirectory.isDirectory()) newDirectory.mkdirs();
                File file = new File(Environment.getExternalStorageDirectory() + newDir+"/article.zip");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                }
                //close the output stream when done
                fileOutput.close();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
   
    
    
    
    public static String executeHttpPost(String url, String auth, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        String resutString;
        StringBuilder builder = new StringBuilder();
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            request.setHeader("Authorization","Bearer "+auth);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"cp1250"));

            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line+"\n");
            }
            in.close();

            resutString=builder.toString();
            return resutString;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String executeHttpGet(String url,String auth) throws Exception {
        BufferedReader in = null;
        String resutString;
        StringBuilder builder = new StringBuilder();
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setHeader("Authorization","Bearer "+auth);
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line+"\n");
            }
            in.close();
            resutString=builder.toString();
            return resutString;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
