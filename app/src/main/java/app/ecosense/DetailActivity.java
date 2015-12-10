package app.ecosense;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.ecosense.models.Post;
import app.ecosense.web.DownloadImageTask;

public class DetailActivity extends AppCompatActivity {



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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public ArrayList<Post> postsFromEcosense;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.getStringExtra("Title") != null) {
                String postID = intent.getStringExtra("Title");
                ((TextView) rootView.findViewById(R.id.post_title)).setText(postID);
                ((TextView) rootView.findViewById(R.id.post_description)).setText("Hello boys");
                ((ImageView) rootView.findViewById(R.id.post_image)).setImageResource(R.mipmap.ic_launcher);
            } else if (intent != null) {
                Post post = (Post) intent.getSerializableExtra("post");
                ((TextView) rootView.findViewById(R.id.post_title)).setText(post.getTitle());
                ((TextView) rootView.findViewById(R.id.post_description)).setText(post.getDescription());
                if(post.getImage() != null) {
                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.post_image)).execute(post.getImage());
                }
            }


            final Button button = (Button) rootView.findViewById(R.id.like_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TO DO add like action

                }
            });

            return rootView;
        }
    }
}