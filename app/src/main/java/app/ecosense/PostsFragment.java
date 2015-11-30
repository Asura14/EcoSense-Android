package app.ecosense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.CardViewNative;

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
            //TODO get posts from EcoSense to display
            selectedPost.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        /*
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
                        R.id.list_item_posts_title, // The ID of the textview
                        postListArray);


        // Get a reference to the ListView, and attach this adapter to it
        ListView listView = (ListView) rootView.findViewById(R.id.listview_posts_title);
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
        */

        //Create a Card
        Card card = new Card(getContext());

        //Create a CardHeader
        CardHeader header = new CardHeader(getContext());
        header.setTitle("MELO");
        //Add Header to card
        card.addCardHeader(header);

        //Set card in the cardView
        CardView cardView = (CardView) rootView.findViewById(R.id.teste);
        cardView.setCard(card);


        return rootView;
    }

    private void updatePosts() {
        FetchPost posts = new FetchPost();
        //TODO get posts from Ecosense
        //posts.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updatePosts();
    }

    public class FetchPost extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";

            return null;
        }

        private String[] getDataFromJson(String postJsonStr, int numDays) throws JSONException {
            JSONObject postJason = new JSONObject(postJsonStr);
            JSONArray postsArray = postJason.getJSONArray("title");

            int numberOfPosts = 10;
            String[] resultStrings = new String[numberOfPosts];
            for(int i = 0; i < postsArray.length(); i++) {
                String title;
                String description;

                // Get the JSON object representing the post
                JSONObject post = postsArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that to read "1400356800" as
                // "this saturday"
                title = post.getString("title");

                // description is in a child array
                description = post.getString("description");

                resultStrings[i] = title + " - " + description;
            }
            return resultStrings;

        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                postsAdapter.clear();
                for(String postString : result) {
                    postsAdapter.add(postString);
                }
                // New data from server
            }
        }
    }


}

