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
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.getStringExtra("Title") != null) {
                String postID = intent.getStringExtra("Title");
                ((TextView) rootView.findViewById(R.id.post_title)).setText(postID);
                ((TextView) rootView.findViewById(R.id.post_description)).setText("Hello boys");
                ((ImageView) rootView.findViewById(R.id.post_image)).setImageResource(R.mipmap.ic_launcher);
            } else if (intent != null) {
                int postID = intent.getIntExtra("ID", 0);
                ((TextView) rootView.findViewById(R.id.post_title)).setText(postsFromEcosense.get(postID).getTitle());
                ((TextView) rootView.findViewById(R.id.post_description)).setText(postsFromEcosense.get(postID).getDescription());
                ((ImageView) rootView.findViewById(R.id.post_image)).setImageResource(R.mipmap.ic_launcher);
            }
            final Button button = (Button) rootView.findViewById(R.id.like_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TO DO add like action

                }
            });

            return rootView;
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
            }

            @Override
            protected JSONArray doInBackground(String... uri) {

                URL url = null;
                HttpURLConnection urlConnection = null;
                JSONArray posts = null;
                try {
                    url = new URL("http://crispy-cow-7805.vagrantshare.com/api/posts");
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


}