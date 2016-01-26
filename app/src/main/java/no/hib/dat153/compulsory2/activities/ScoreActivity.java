package no.hib.dat153.compulsory2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import no.hib.dat153.compulsory2.R;

/**
 * Result of the users performance in learning mode.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class ScoreActivity extends AppCompatActivity {

    /**
     * Key representing the amount of correct answers submitted by the user.
     */
    public static final String CORRECT = "no.hib.dat153.compulsory2.activities.CORRECT";

    /**
     * Key representing the total amount of answers submitted by the user.
     */
    public static final String TOTAL = "no.hib.dat153.compulsory2.activities.TOTAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Bundle data = getIntent().getExtras();
        int correct = data.getInt(CORRECT);
        int total = data.getInt(TOTAL);

        TextView headingView = (TextView) findViewById(R.id.scoreResultHeading);
        TextView scoreView = (TextView) findViewById(R.id.scoreResultText);

        String resultHeadingText;
        String [] dividedText = getResources().getStringArray(R.array.scoreActivity);
        if(correct != total || total == 0)
            resultHeadingText = dividedText[0];
        else
            resultHeadingText = dividedText[1];

        String resultScoreText = dividedText[2] + correct + dividedText[3]
                + total + dividedText[4];

        headingView.setText(resultHeadingText);
        scoreView.setText(resultScoreText);
    }

    /**
     * Pressing this button will bring the MainActivity to the foreground.
     * @param view The item which was clicked.
     */
    public void onClick(View view) {
        Intent resumeMain = new Intent(ScoreActivity.this, MainActivity.class);
        resumeMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(resumeMain);
    }
}
