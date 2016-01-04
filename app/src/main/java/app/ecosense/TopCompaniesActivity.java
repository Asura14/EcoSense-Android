package app.ecosense;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import app.ecosense.models.Company;
import app.ecosense.web.Api;
import app.ecosense.web.DownloadImageTask;

public class TopCompaniesActivity  extends AppCompatActivity {

    public ArrayList<Company> companiesArray = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setContentView(R.layout.activity_top_companies);
        new FetchCompanies(this).execute();
    }

    public class FetchCompanies extends AsyncTask<String, Void, JSONArray> {

        Activity parent;

        public FetchCompanies(Activity activity) {
            this.parent = activity;
        }

        @Override
        protected void onPostExecute(JSONArray companies) {
            if(companies != null) {
                for (int i = 0; i < companies.length(); i++) {
                    Company newCompany = new Company();
                    try {
                        JSONObject company = companies.getJSONObject(i);
                        newCompany.setAvatar(company.getString("image_url"));
                        newCompany.setName(company.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    companiesArray.add(newCompany);
                }
            }


            ListView listView = (ListView) parent.findViewById(R.id.companies);
            listView.setAdapter(new ListAdapter(parent, R.layout.activity_top_companies, companiesArray, parent));

        }

        @Override
        protected JSONArray doInBackground(String... uri) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            JSONArray companies = null;
            String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString("TOKEN", "token not found");

            try {
                url = new URL(Api.URL_PATH + "api/companies/top");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Bearer " + token);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                companies = getJSONFromInputStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return companies;
        }

        public JSONArray getJSONFromInputStream(InputStream in) throws IOException, JSONException {

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            return new JSONArray(responseStrBuilder.toString());
        }
    }

    public class ListAdapter extends ArrayAdapter<Company> {
        private Activity activity;

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<Company> items, Activity activity) {
            super(context, resource, items);
            this.activity = activity;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.fragment_top_companies, null);
            }

            Company p = getItem(position);

            if (p != null) {
                TextView name = (TextView) v.findViewById(R.id.company_name);
                if(p.getAvatar() != null) {
                   new DownloadImageTask((ImageView) v.findViewById(R.id.avatar), this.activity).execute(p.getAvatar());
                }
                if (name != null) {
                    name.setText(p.getName());
                }
            }

            return v;
        }

    }
}
