package jtb.magazin.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import jtb.magazin.MainActivity;
import jtb.magazin.R;
import jtb.magazin.store.SearchArticleStore;

/**
 * Created by zelenasova on 9.3.2015.
 */
// v osobitnom vlakne nacita vsetky potrebne story
public class LoadSearchStore extends AsyncTask<Void, Void, Void> {

    private Context context;

    public LoadSearchStore(Context context) {
        this.context = context;
    }

    SearchArticleStore store;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivity) context).spinner.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... urls) {
        store = new SearchArticleStore(((MainActivity) context).actualSearchText, context);
        if (store.error)
            publishProgress();
        return null;
    }

    protected void onProgressUpdate(Void... progress) {
        ((MainActivity) context).errorMessage(context.getString(R.string.error3), context.getString(R.string.error6));
    }

    @Override
    protected void onPostExecute(Void result) {

        try {
            ((MainActivity) context).menuLayout.updateMenuItems();
            ((MainActivity) context).createSearchFragment(store);
            ((MainActivity) context).spinner.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            System.out.println("Exception from LoadSearchStore: "+e);
        }
    }
}

