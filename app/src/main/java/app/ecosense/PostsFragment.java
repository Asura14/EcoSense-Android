package app.ecosense;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import app.ecosense.cards.FeedCard;
import app.ecosense.models.Post;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Created by Luis on 16/11/2015.
 */
public class PostsFragment extends Fragment implements CardView.OnClickListener {

    private CardArrayRecyclerViewAdapter mCardArrayAdapter;
    public View.OnClickListener mListener;

    public PostsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onClick(View v) {
        Intent postIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, v.toString());
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
        card.setImageUrl("http://www.247inktoner.com/blog/image.axd?picture=%2F2013%2F02%2Fecofriendly21.jpg");
        card.setDate("10 Set.");


        FeedCard card1 = new FeedCard(getContext());
        card1.setTitle("This is the title 1");
        card1.setTeaser("This is the teaser 1");
        card1.setAuthor("Author 2");
        card1.setImageUrl("http://www.247inktoner.com/blog/image.axd?picture=%2F2013%2F02%2Fecofriendly21.jpg");
        card1.setDate("20 Set.");

        FeedCard card2 = new FeedCard(getContext());
        card2.setTitle("This is the title 2");
        card2.setTeaser("This is the teaser 2");
        card2.setAuthor("Author 2");
        card2.setImageUrl("http://www.247inktoner.com/blog/image.axd?picture=%2F2013%2F02%2Fecofriendly21.jpg");
        card2.setDate("21 Set.");

        cards.add(card);
        cards.add(card1);
        cards.add(card2);

        mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);

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
        protected String[] doInBackground(String... uri) {


            return null;
        }


    }


}

