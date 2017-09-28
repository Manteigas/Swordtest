package testsword.swordtest.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import testsword.swordtest.R;
import testsword.swordtest.adapters.PatientAdapter;
import testsword.swordtest.models.Patient;
import testsword.swordtest.models.PatientLoader;
import testsword.swordtest.utils.Utils;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<List<Patient>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String URL_EXAMPLE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /** UI Elements **/
    private RecyclerView recyclerView;
    private ImageButton ibSettings;
    private ProgressBar progressBar;
    private TextView tvDoctorName;

    private PatientAdapter adapter;
    private List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvDoctorName = (TextView) findViewById(R.id.tvName);
        ibSettings = (ImageButton) findViewById(R.id.ibSettings);

        Typeface avenirMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "AvenirLTStd-Medium.otf");
        tvDoctorName.setTypeface(avenirMedium);

        loadPatientsData();

        patientList = new ArrayList<>();

        if (checkInternetConnection()) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            Toast.makeText(this, getString(R.string.er_nointernetconnection), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new PatientAdapter(this, patientList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final EditText mUrl = (EditText) mView.findViewById(R.id.etUrl);
                Button btSave = (Button) mView.findViewById(R.id.btSave);

                mUrl.setText(Utils.getURLfromSharedPref(MainActivity.this));

                btSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.setURLInSharedPref(MainActivity.this, mUrl.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public Loader<List<Patient>> onCreateLoader(int id, Bundle args) {
        return new PatientLoader(this, URL_EXAMPLE);
    }

    @Override
    public void onLoadFinished(Loader<List<Patient>> loader, List<Patient> data) {
        progressBar.setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
            patientList = data;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Patient>> loader) {
        adapter.clear();
    }

    /** Only for tests - Create the patients list and save it in Shared Pref **/
    private void loadPatientsData() {
        ArrayList<Patient> list = new ArrayList<>();

        list.add(new Patient("Jack Nelson", "04 07 1959", "London", "Asma", 1, "B"));
        list.add(new Patient("Hannah Miller", "26 09 1944", "Paris", "Arthritis", 2, "F"));
        list.add(new Patient("John Douglas", "26 09 1972", "Madrid", "Back Pain", 3, "F"));
        list.add(new Patient("Claire Hughes", "04 07 1977", "Oslo", "Cholesterol", 4, "A"));
        list.add(new Patient("Steven Howard", "26 09 1957", "Rome", "Leukemia", 5, "C"));
        list.add(new Patient("Jackie Scott", "26 09 1952", "Dublin", "Malaria", 6, "A"));
        list.add(new Patient("Florence Melville", "04 07 1944", "Bern", "Diarrhea", 7, "A"));
        list.add(new Patient("Mary Smith", "26 09 1940", "Berlin", "Chickenpox", 8, "A"));
        list.add(new Patient("André Rodrigues", "26 09 1993", "Figueira", "Muscle Pain", 1, "B"));
        list.add(new Patient("Rebelo Sousa", "04 07 1964", "Lisbon", "Fracture", 2, "F"));

        Utils.savePatientsInSharedPref(getApplicationContext(), list);
    }

    /** Check the Internet Connection **/
    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
