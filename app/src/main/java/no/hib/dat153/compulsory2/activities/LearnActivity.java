package no.hib.dat153.compulsory2.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import no.hib.dat153.compulsory2.R;
import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.ApplicationUtils;

/**
 * Learning mode, presents a sequence of images and asks the user
 * the name of the person on the current image.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class LearnActivity extends AppCompatActivity {
    /**
     * Database operations.
     */
    private ApplicationDatabase myDB;

    /**
     * Persons in the database.
     */
    private ArrayList<Person> list;

    /**
     * Amount of persons
     */
    private int total;

    /**
     * The amount of answers the user has submitted.
     */
    private int current;

    /**
     * The amount of correct answers the user has submitted.
     */
    private int correct = 0;

    /**
     * The answer the user is submitting.
     */
    private TextView input;

    /**
     * View showing the user's score.
     */
    private TextView score;

    /**
     * View showing how many images there is left to show.
     */
    private TextView imagesLeft;

    /**
     * The current image to be shown.
     */
    private ImageView image;

    /**
     * The current person
     */
    private Person person;

    /**
     * Renders the view, shuffles the list of persons and shows the
     * first person in the shuffled list.
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);


        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        total = list.size();
        if(total <= 0) {
            Intent resumeMain = new Intent(LearnActivity.this, MainActivity.class);
            resumeMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(resumeMain);
            return;
        } else {
            ApplicationUtils.shuffle(list);
            current = 0;

            imagesLeft = (TextView) findViewById(R.id.imagesLeft);
            imagesLeft.setText(total + "");

            score = (TextView) findViewById(R.id.learnScore);
            score.setText(correct + "");

            person = list.get(current);

            image = (ImageView) findViewById(R.id.imageView);
            Uri uri = Uri.parse(person.getUriString());
            try {
                InputStream stream = ApplicationUtils.fetchImage(getApplicationContext(), uri);
                image.setImageBitmap(BitmapFactory.decodeStream(stream));
            } catch (Exception e) {
                throw new Error(e);
            }
            input = (EditText) findViewById(R.id.learnEditText);
        }
    }

    /**
     * Computes score and proceeds by either viewing the next image,
     * or invoking ScoreActivity to view results.
     * @param view The button which was clicked.
     */
    public void onClick(View view) {

        person = list.get(current);
        ++current;

        if(person.getName().equalsIgnoreCase(input.getText().toString())) {
            correct++;
        }
        if(view.getId() == R.id.showScore) {
            total = current - 1;
            startScore();
        } else if(current >= total) {
            startScore();
        } else {
            score.setText(correct + "");
            imagesLeft.setText((total - current) + "");
            input.setText("");

            person = list.get(current);

            Uri uri = Uri.parse(person.getUriString());
            try {
                InputStream stream = ApplicationUtils.fetchImage(getApplicationContext(), uri);
                image.setImageBitmap(BitmapFactory.decodeStream(stream));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * Starts the ScoreActivity and provides the intent with
     * variables to be used (correct and total submissions).
     */
    public void startScore() {
        Intent intent = new Intent(LearnActivity.this, ScoreActivity.class);
        intent.putExtra(ScoreActivity.CORRECT, correct);
        intent.putExtra(ScoreActivity.TOTAL, total);
        startActivity(intent);
    }

    public void backToMainScreen(View view) {
        Intent intent = new Intent(LearnActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
