package app.ecosense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import app.ecosense.models.Post;

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

        EditText mPostTitle;
        EditText mPostTeaser;
        EditText mPostDescription;
        Button mSendPostButton;

        String postTitle;
        String postTeaser;
        String postDescription;

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                    //TODO Send to EcoSense

                    Toast.makeText(getContext(),"Post sent", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });

            return rootView;
        }
    }


}
