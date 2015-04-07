package jtb.magazin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

public class EditSettingsFile {
	
	
	//upravuje sa konfiguracny subor settings.js
	 public EditSettingsFile(int settings_id, String value,Context context) {
	    
        

        File f = new File(((MainActivity)context).getExternalCacheDir ().toString()  + "/"+Constants.STORAGE_FILE+"/articles/system/settings.js");
        FileInputStream fs = null;
        InputStreamReader in = null;
        BufferedReader br = null;
        
        StringBuffer sb = new StringBuffer();
        
        String textinLine;
        
        try {
             fs = new FileInputStream(f);
             in = new InputStreamReader(fs);
             br = new BufferedReader(in);
            
            while(true)
            {
                textinLine=br.readLine();
                if(textinLine==null)
                    break;
                sb.append(textinLine);
            }
            
            switch (settings_id) {
            	
            case 1: 
            	
              String ch_language = "languageCode";
              int cnt1 = sb.indexOf(ch_language);
              sb.replace(cnt1+ch_language.length()+4,cnt1+ch_language.length()+6,value);
              break;
              
            case 2:
              
              String ch_size = "fontSize";
              int cnt2 = sb.indexOf(ch_size);
              sb.replace(cnt2+ch_size.length()+4,cnt2+ch_size.length()+5,value);
              break;
              
            case 3:
              
              String ch_color = "bgColor";
              int cnt3 = sb.indexOf(ch_color);
              sb.replace(cnt3+ch_color.length()+3,cnt3+ch_color.length()+4,value);
              break;
              
            case 4:
              
              String ch_favorite = "isFavourite";
              int cnt4 = sb.indexOf(ch_favorite);
              sb.replace(cnt4+ch_favorite.length()+3,cnt4+ch_favorite.length()+4,value);
              break;
              
            }
              
          

              fs.close();
              in.close();
              br.close();

            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
            
            try{
                FileWriter fstream = new FileWriter(f);
                BufferedWriter outobj = new BufferedWriter(fstream);
                outobj.write(sb.toString());
                outobj.close();
                
            }catch (Exception e){
              System.err.println("Error: " + e.getMessage());
            }
    }
}