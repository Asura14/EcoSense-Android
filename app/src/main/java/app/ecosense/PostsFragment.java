package app.ecosense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import app.ecosense.cards.FeedCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
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

        ArrayList<Card> cards = new ArrayList<Card>();

        // TODO: add cards dynamically
        FeedCard card = new FeedCard(getContext());
        card.setTitle("This is the title");
        card.setTeaser("This is the teaser");
        card.setAuthor("Author");
        card.setImageUrl("https://pbs.twimg.com/profile_images/378800000856151767/c9cda4e0452dcd6d66a70a33bb970d9b.jpeg");
        card.setDate("10 Set.");


        FeedCard card1 = new FeedCard(getContext());
        card1.setTitle("This is the title 1");
        card1.setTeaser("This is the teaser 1");
        card1.setAuthor("Author 2");
        card1.setImageUrl("http://www.altcoinfever.com/wp-content/uploads/2014/02/pot.png");
        card1.setDate("20 Set.");

        FeedCard card2 = new FeedCard(getContext());
        card2.setTitle("This is the title 2");
        card2.setTeaser("This is the teaser 2");
        card2.setAuthor("Author 2");
        card2.setImageUrl("http://41.media.tumblr.com/b72cff4e0b89f9e08ef0ab2f6ce67fa6/tumblr_nr67uoBubF1twhxf0o1_500.png");
        card2.setDate("21 Set.");

        cards.add(card);
        cards.add(card1);
        cards.add(card2);

        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) rootView.findViewById(R.id.feed_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }


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

