package com.link.newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.newsfeed.model.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleDetails extends AppCompatActivity {
    ImageView DetailsArticleImage;
    TextView DetailsArticleTitle, DetailsArticleAuthor, DetailsArticleDescription, DetailsArticlePublishedAt;
    Button OpenWebsite;
    private Article mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_details);
        DetailsArticleImage = findViewById(R.id.details_article_image);
        DetailsArticleTitle = findViewById(R.id.details_article_title);
        DetailsArticleAuthor = findViewById(R.id.details_article_author);
        DetailsArticleDescription = findViewById(R.id.details_article_description);
        DetailsArticlePublishedAt = findViewById(R.id.details_article_publishedAt);
        OpenWebsite = findViewById(R.id.open_website);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mArticle = extras.getParcelable("Article");

            Picasso.with(this).load(mArticle.getUrlToImage()).into(DetailsArticleImage);
            DetailsArticleTitle.setText(mArticle.getTitle());
            String Author = "By " + mArticle.getAuthor();
            DetailsArticleAuthor.setText(Author);
            DetailsArticleDescription.setText(mArticle.getDescription());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
            try {
                Date date = dateFormat.parse(mArticle.getPublishedAt());
                dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                DetailsArticlePublishedAt.setText(dateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            OpenWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(mArticle.getUrl()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });


        }


    }
}
