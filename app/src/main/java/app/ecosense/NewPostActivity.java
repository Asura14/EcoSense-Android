package app.ecosense;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.ecosense.models.Post;
import app.ecosense.web.Api;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    public void createPost(Post post, Location location) {
        new CreatePostTask(post,location, this).execute();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        EditText mPostTitle;
        EditText mPostTeaser;
        EditText mPostDescription;
        Button mSendPostButton;

        String postTitle;
        String postTeaser;
        String postDescription;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            LocationManager mLocation = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            final Location currentLoc = mLocation.getLastKnownLocation(mLocation.getAllProviders().get(0));

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());


            View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);


            mPostTitle = (EditText)rootView.findViewById(R.id.new_post_title);
            mPostTitle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    postTitle = mPostTitle.getText().toString();
                }
            });

            mPostTeaser = (EditText)rootView.findViewById(R.id.new_post_teaser);
            mPostTeaser.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    postTeaser = mPostTeaser.getText().toString();
                }
            });

            mPostDescription = (EditText)rootView.findViewById(R.id.new_post_content);
            mPostDescription.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    postDescription = mPostDescription.getText().toString();
                }
            });

            mSendPostButton = (Button) rootView.findViewById(R.id.send_post_button);
            mSendPostButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Post post = new Post();
                    post.setTitle(String.valueOf(mPostTitle.getText()));
                    post.setTeaser(String.valueOf(mPostTeaser.getText()));
                    post.setDescription(String.valueOf(mPostDescription.getText()));
                    ((NewPostActivity) getActivity()).createPost(post, currentLoc);
                }
            });

            return rootView;
        }
    }

    public class CreatePostTask extends AsyncTask<Void, Void, Boolean> {
        Post post;
        Location location;
        Activity activity;
        public CreatePostTask(Post post, Location location, Activity activity) {
            this.post = post;
            this.location = location;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(Api.URL_PATH + "api/posts");
                String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getString("TOKEN", "token not found");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "Bearer " + token);

                JSONObject cred = new JSONObject();
                cred.put("title",this.post.getTitle());
                cred.put("teaser",this.post.getTeaser());
                cred.put("content",this.post.getDescription());
                cred.put("latitude", this.location.getLatitude());
                cred.put("longitude", this.location.getLongitude());
                cred.put("ecological_issue", true);
                cred.put("company_id", 0);

                OutputStream writer = urlConnection.getOutputStream();
                writer.write(cred.toString().getBytes("UTF-8"));
                writer.close();
                writer.flush();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("Response: ", String.valueOf(urlConnection.getResponseCode()));
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                Log.d("ES HERE", total.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Toast.makeText(activity.getApplicationContext(), "Post sent", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }
}
