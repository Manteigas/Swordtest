package testsword.swordtest.models;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import testsword.swordtest.utils.Utils;

/**
 * Created by André Manteigas on 28/09/2017.
 */

public class PatientLoader extends AsyncTaskLoader<List<Patient>> {
    private static final String LOG_TAG = PatientLoader.class.getName();
    private String mUrl;
    private Context context;

    public PatientLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Patient> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return Utils.makeServerRequest(mUrl, context);
    }
}
