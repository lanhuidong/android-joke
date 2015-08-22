package com.nexusy.gemini;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nexusy.gemini.adapter.JokeAdapter;
import com.nexusy.gemini.http.DataParser;
import com.nexusy.gemini.http.GeminiHttpClient;
import com.nexusy.gemini.http.UrlConstants;
import com.nexusy.gemini.model.Joke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Long minId;
    private Long maxId;

    private ListView listView;
    private JokeAdapter adapter;
    private List<Joke> jokes = new ArrayList<>();

    private Dialog loadingDialog;

    private View footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listView = (ListView) findViewById(R.id.joke_list);
        footerView = getLayoutInflater().inflate(R.layout.list_footer, listView, false);
        listView.addFooterView(footerView);

        TextView loadMore = (TextView) findViewById(R.id.load_more);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minId != null) {
                    new QueryOlderJokesTask().execute(minId.toString());
                }
            }
        });

        adapter = new JokeAdapter(MainActivity.this, jokes);
        listView.setAdapter(adapter);

        new QueryOlderJokesTask().execute();

        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxId != null) {
                    new QueryNewerJokesTask().execute(maxId.toString());
                }
            }
        });

    }

    private class QueryOlderJokesTask extends AsyncTask<String, Void, List<Joke>> {
        @Override
        protected void onPreExecute() {
            loadingDialog = new Dialog(MainActivity.this, R.style.dialog);
            loadingDialog.setContentView(R.layout.loading_dialog);
            TextView tv = (TextView) loadingDialog.findViewById(R.id.dialog_content);
            tv.setText(R.string.loading);
            loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
        }

        @Override
        protected List<Joke> doInBackground(String... params) {
            Map<String, String> parameters = new HashMap<>();
            if(params.length > 0){
                parameters.put("id", params[0]);
            }
            String response = GeminiHttpClient.post(UrlConstants.QUERY_OLDER_JOKES, parameters);
            List<Joke> jokes = new DataParser().parseHttpResponse(response);
            if (jokes.isEmpty()) {
                return Collections.emptyList();
            }
            MainActivity.this.jokes.addAll(jokes);
            if (maxId == null) {
                maxId = jokes.get(0).getId() + 1;
            }
            minId = jokes.get(jokes.size() - 1).getId() - 1;
            return jokes;
        }

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            loadingDialog.dismiss();
            if (!jokes.isEmpty()) {
                adapter.notifyDataSetChanged();
            } else {
                listView.removeFooterView(footerView);
            }
        }

    }

    private class QueryNewerJokesTask extends AsyncTask<String, Void, List<Joke>> {
        @Override
        protected void onPreExecute() {
            loadingDialog = new Dialog(MainActivity.this, R.style.dialog);
            loadingDialog.setContentView(R.layout.loading_dialog);
            TextView tv = (TextView) loadingDialog.findViewById(R.id.dialog_content);
            tv.setText(R.string.loading);
            loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
        }

        @Override
        protected List<Joke> doInBackground(String... params) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id", params[0]);
            String response = GeminiHttpClient.post(UrlConstants.QUERY_OLDER_JOKES, parameters);
            List<Joke> jokes = new DataParser().parseHttpResponse(response);
            if (jokes.isEmpty()) {
                return Collections.emptyList();
            }
            MainActivity.this.jokes.addAll(0, jokes);
            maxId = jokes.get(0).getId() + 1;
            return jokes;
        }

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            loadingDialog.dismiss();
            if (!jokes.isEmpty()) {
                adapter.notifyDataSetChanged();
            }
        }

    }

}
