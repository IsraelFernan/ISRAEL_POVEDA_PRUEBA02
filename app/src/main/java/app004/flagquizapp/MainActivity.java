package app004.flagquizapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.XmlRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private String usuario;
    private SharedPreferences referencias;
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    private boolean deviceIsPhone = true;
    private boolean preferencesChanged = true;
    private MainActivityFragment quizFragment;
    private LogicaDePreguntas quizViewModel;
    private OnSharedPreferenceChangeListener preferencesChangeListener;

    private void setSharedPreferences() {
        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, app004.flagquizapp.R.xml.preferences, false);
        // Register a listener for shared preferences changes
        Bundle extras = getIntent().getExtras();
        this.usuario = extras.getString("usuario");
        this.referencias = getSharedPreferences(this.usuario, Context.MODE_PRIVATE);
        this.referencias.getString("configuracion","");
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    private void screenSetUp() {
        if (getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            deviceIsPhone = false;
        }
        if (deviceIsPhone) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quizViewModel = ViewModelProviders.of(this).get(LogicaDePreguntas.class);
        this.preferencesChangeListener = new ConfigChangeListener(this);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        this.usuario = extras.getString("usuario");
        if(this.usuario != null){
            this.setSharedPreferences();
            this.screenSetUp();
            Log.d("referencias",String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getAll()));
            this.referencias = getSharedPreferences(this.usuario, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = referencias.edit();
            editor.putString("configuracion",String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getAll()));
            editor.commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {
            this.quizFragment = (MainActivityFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.quizFragment);
            this.quizViewModel.setGuessRows(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(CHOICES, null));
            this.quizViewModel.setRegionsSet(PreferenceManager.getDefaultSharedPreferences(this)
                    .getStringSet(REGIONS, null));

            this.quizFragment.resetQuiz();

            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(app004.flagquizapp.R.menu.menu_main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        preferencesIntent.putExtra("usuario",this.usuario);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    public int getScreenSize() {
        return getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public MainActivityFragment getQuizFragment() {
        return this.quizFragment;
    }

    public LogicaDePreguntas getQuizViewModel() {
        return quizViewModel;
    }

    public static String getCHOICES() {
        return CHOICES;
    }

    public static String getREGIONS() {
        return REGIONS;
    }

    public void setPreferencesChanged(boolean preferencesChanged) {
        this.preferencesChanged = preferencesChanged;
    }


}
