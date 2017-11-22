package com.example.esteban.yambagrupo4;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class StatusFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = "StatusActivity";
    EditText editStatus;
    Button buttonTweet;
    Twitter twitter;
    TextView textCount;
    SharedPreferences prefs;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

// Find views!
        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        buttonTweet.setOnClickListener(this);
        textCount = (TextView) view.findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editStatus.addTextChangedListener(this);

        return view;
    }

    public void onClick(View v) {
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked");
        ProgressDialog progress = new ProgressDialog(this.getActivity());
        progress.setTitle(getResources().getString(R.string.loading_title));
        progress.setMessage(getResources().getString(R.string.loading_text));
        new PostTask(progress, this).execute(status);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable statusText) {
        int count = 140 - statusText.length();
        textCount.setText(Integer.toString(count));
        textCount.setTextColor(Color.GREEN);
        if (count < 10)
            textCount.setTextColor(Color.YELLOW);
        if (count < 0)
            textCount.setTextColor(Color.RED);

    }

    private final class PostTask extends AsyncTask<String, Void, String> {

        ProgressDialog progress;
        StatusFragment fragment;

        public PostTask(ProgressDialog progress, StatusFragment fragment) {
            this.progress = progress;
            this.fragment = fragment;
        }

        public void onPreExecute() {
            progress.show();
//aquí se puede colocar código a ejecutarse previo
//a la operación
        }

        // Llamada al empezar
        @Override
        protected String doInBackground(String... params) {
            String accesstoken = prefs.getString("accesstoken", "917697166705070080-QPm4gaY3N4lwrTpITLNSyrqJpBTNhkl");
            String accesstokensecret = prefs.getString("accesstokensecret", "graIxnxd2mG97n7QyKsAnf9CXQ8XIW7XkFRwdeYNMdGR1");
            // Comprobar si el nombre de usuario o el password están vacíos.
            // Si lo están, indicarlo mediante un Toast y redirigir al usuario a Settings
            /*if (TextUtils.isEmpty(accesstoken) || TextUtils.isEmpty(accesstokensecret)) {
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                return "Por favor, actualiza tu nombre de usuario y tu contraseña";
            }*/
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey("9b7zS9MZiwLEkyKvsbt4LXQhK")
                    .setOAuthConsumerSecret("b2T3kEWGZUZA8VdF8uIzG9Wl2WCGCCUjdJIhWSpP7iUYCmhVCH")
                    .setOAuthAccessToken(accesstoken)
                    .setOAuthAccessTokenSecret(accesstokensecret);
            TwitterFactory factory = new TwitterFactory(builder.build());
            twitter = factory.getInstance();
            try {
                twitter.updateStatus(params[0]);
                return getResources().getString(R.string.tweet_ok);
            } catch (TwitterException e) {
                Log.e(TAG, "Fallo en el envío");
                e.printStackTrace();
                if (e.getStatusCode() == -1)
                    return getResources().getString(R.string.tweet_noconex);
                if (e.getStatusCode() == 403)
                    return getResources().getString(R.string.tweet_repeat);
                else
                    return getResources().getString(R.string.tweet_fail);
            }

        }

        // Llamada cuando la actividad en background ha terminado
        @Override
        protected void onPostExecute(String result) {
            // Accion al completar la actualizacion del estado
            progress.dismiss();
            super.onPostExecute(result);
            Snackbar.make(StatusFragment.this.getView(), result, Snackbar.LENGTH_LONG).show();
        }
    }
}