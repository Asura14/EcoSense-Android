package app.ecosense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Luis on 16/11/2015.
 */
public class PostsFragment extends Fragment {

    private ArrayAdapter<String> postsAdapter;

    public PostsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.posts_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchPost selectedPost = new FetchPost();
            //TO TO Execute Post Activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        String[] postsList = {
                "Post1 es here",
                "Post2 es here",
                "Post3 es here",
                "Post4 es here",
                "Post5 es here",
                "Post6 es here",
                "Post7 es here",
                "Post8 es here",
                "Post9 es here",
                "Post10 es here",
        };

        final List<String> postListArray = new ArrayList<String>(Arrays.asList(postsList));

        postsAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_posts, // The name of the layout ID
                        R.id.list_item_posts_textview, // The ID of the textview
                        postListArray);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_posts);
        listView.setAdapter(postsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String posts = postsAdapter.getItem(position);
                Intent postIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, posts);
                startActivity(postIntent);
            }
        });

        return rootView;
    }

    public class FetchPost {

        private String[] getDataFromJson(String postJsonStr, int numDays) throws JSONException {
            JSONObject postJason = new JSONObject(postJsonStr);
            JSONArray postsArray = postJason.getJSONArray("title");

            int numberOfPosts = 10;
            String[] resultStrings = new String[numberOfPosts];
            for(int i = 0; i < postsArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String title;
                String description;

                // Get the JSON object representing the post
                JSONObject post = postsArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                title = post.getString("title");

                // description is in a child array called "weather", which is 1 element long.
                description = post.getString("description");

                resultStrings[i] = title + " - " + description;
            }
            return resultStrings;

        }
    }
}

