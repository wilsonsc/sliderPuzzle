package scottnickamanda.sliderpuzzle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;

/**
 * Displays High Scores
 *
 * Created by amandabuhr on 4/7/16.
 */
public class HighScoresActivity extends Activity {

    private Button okayButton;
    private int currentSize;
    private String[] savedScores;
    private String key;
    private TextView scores;
    private SharedPreferences scorePrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);
        scores = (TextView)findViewById(R.id.highScoresList);
        scorePrefs = getSharedPreferences(GameActivity.GAME_PREFS, 0);
        key = "3x3";
        okayButton = (Button) findViewById(R.id.okBtn);

        setText(key);

        //Retrieves information about the physical size of the users
        //device
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Store the width and height of the device
        final int width = dm.widthPixels;

        /** The possible sizes the user may select for the game*/
        String[] sizes = new String[] {"3x3", "4x4", "5x5"};

        /** An adapter that holds possible user sizes in dropdown box*/
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this,
                android.R.layout.simple_spinner_item, sizes);

        //Sets the dropdown box based off xml defined values
        Spinner gameSize = (Spinner) findViewById(R.id.selectSize);

        //Sets the adapter for the dropdown box
        gameSize.getLayoutParams().width = width / 5;
        gameSize.setAdapter(adapter);
        gameSize.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {

                    /***********************************************************
                     * Set what happens when an item is selected from the
                     * dropdown box
                     * Size of the board is set to the index of the selected
                     * item + 3
                     *
                     * @param parent the parent view
                     * @param view the current view from which this was selected
                     * @param position the position of the selected item
                     * @param id the id of the selected item
                     **********************************************************/
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {

                        //Set the custom size to the index of the item + 3
                        currentSize = (position+3);
                        key=currentSize+"x"+currentSize;
                        setText(key);
                    }

                    /***********************************************************
                     * Must be defined because of extending, does nothing
                     **********************************************************/
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        okayButton.setOnClickListener(new View.OnClickListener() {

            /*********************************************************
             * Launches image select activity when user clicks button
             *
             * @param v the view of the button
             *********************************************************/
            public void onClick(View v) {

                finish();

            }
        });
    }

    private void setText(String key) {
        if(scorePrefs.contains(key)) {
            savedScores = scorePrefs.getString(key, "").split("\\|");

            StringBuilder scoreBuild = new StringBuilder("");
            for (String s : savedScores) {
                String[] parts = s.split(" - ");
                int time = Integer.parseInt(parts[1]);
                int minutes = time / 60;
                int seconds = time % 60;
                String newTime = minutes + ":" + seconds;
                s = parts[0] + "-" + newTime;

                scoreBuild.append(s + "\n");
            }
            scores.setText(scoreBuild.toString());
        } else {
            scores.setText("No Scores Found");
        }
    }
}
