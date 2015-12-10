package app.ecosense;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

import app.ecosense.cards.FeedCard;
import app.ecosense.models.Comment;
import app.ecosense.models.Post;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

public class PostsFragment extends Fragment implements CardView.OnClickListener {

    private CardArrayRecyclerViewAdapter mCardArrayAdapter;
    public ArrayList<Post> postsFromEcosense = new ArrayList<>();
    public CardRecyclerView mRecyclerView;
    public ArrayList<Card> cards = new ArrayList<>();
    public Card.OnCardClickListener mCardListener;

    public PostsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        new FetchPosts().execute();
    }

    @Override
    public void onClick(View v) {
        Activity postActivity = new Activity();
        Intent postIntent = new Intent(postActivity, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, v.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.posts_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);

        mRecyclerView = (CardRecyclerView) rootView.findViewById(R.id.feed_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public class FetchPosts extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected void onPostExecute(JSONArray posts) {
            if(posts != null) {
                for(int i = 0; i < posts.length(); i++) {
                    try {
                        Post newPost = new Post();
                        JSONObject post = posts.getJSONObject(i);
                        newPost.setAuthor(post.getString("name"));
                        newPost.setDescription(post.getString("content"));
                        newPost.setPostDate(post.getString("updated_at"));
                        newPost.setTeaser(post.getString("teaser"));
                        newPost.setTitle(post.getString("title"));
                        newPost.setImage(post.getString("image_url"));
                        JSONArray commentsJSON = post.getJSONArray("comments");
                        ArrayList<Comment> commentsList = new ArrayList<>();
                        for(int j = 0; j < commentsJSON.length(); j++) {
                            JSONObject comment = commentsJSON.getJSONObject(j);
                            Comment newComment = new Comment(comment.getString("name"),
                                    comment.getString("created_at"),
                                    comment.getString("content"));
                            commentsList.add(newComment);
                        }
                        newPost.setComments(commentsList);
                        postsFromEcosense.add(newPost);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Add cards
            for(int i = 0; i < postsFromEcosense.size() && i < 30; i++) {
                FeedCard card = new FeedCard(getContext());
                card.setTitle(postsFromEcosense.get(i).getTitle());
                card.setTeaser(postsFromEcosense.get(i).getTeaser());
                card.setAuthor(postsFromEcosense.get(i).getAuthor());
                card.setImageUrl(postsFromEcosense.get(i).getImage());
                card.setDate(postsFromEcosense.get(i).getPostDate());
                card.setId(String.valueOf(i));
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Post post = postsFromEcosense.get(Integer.parseInt(card.getId()));
                        Log.d("POST: ", post.getTitle());
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("post", post);
                        startActivity(intent);
                    }
                });
                cards.add(card);
            }

            mRecyclerView.setAdapter(new CardArrayRecyclerViewAdapter(getActivity(), cards));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        protected JSONArray doInBackground(String... uri) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            JSONArray posts = null;
            try {
                url = new URL("http://soaring-llama-1432.vagrantshare.com/api/posts");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                posts = getJSONFromInputStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return posts;
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

}

