package app.ecosense.cards;


import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.ecosense.R;
import app.ecosense.web.DownloadImageTask;
import it.gmariotti.cardslib.library.internal.Card;

public class FeedCard extends Card {

    protected TextView mTitle;
    protected TextView mTeaser;
    protected TextView mAuthorAndDate;
    protected ImageView mImage;


    private String title;
    private String teaser;
    private String author;
    private String imageUrl;


    public FeedCard(Context context) {
        this(context, R.layout.feed_card);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public FeedCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setTitle(String title){
        this.title=title;
    }
    public void setTeaser(String teaser){
        this.teaser=teaser;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public void setImageUrl(String url){
        this.imageUrl = url;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_title);
        mTeaser = (TextView) parent.findViewById(R.id.card_teaser);
        mAuthorAndDate = (TextView) parent.findViewById(R.id.card_author);
        mImage = (ImageView) parent.findViewById(R.id.card_image);

        android.view.ViewGroup.LayoutParams layoutParams = mImage.getLayoutParams();
        layoutParams.width = 350;
        layoutParams.height = 350;
        mImage.setLayoutParams(layoutParams);


        if (mTitle!=null)
            mTitle.setText(this.title);

        if (mTeaser != null)
            mTeaser.setText(this.teaser);

        if (mAuthorAndDate != null)
            mAuthorAndDate.setText(this.author);

        if(imageUrl != null)
            new DownloadImageTask(mImage).execute(imageUrl);
    }
}