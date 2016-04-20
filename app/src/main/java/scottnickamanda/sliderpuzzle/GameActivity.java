package scottnickamanda.sliderpuzzle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/***********************************************************************
 * Main activity class for the Slider Puzzle game
 * Displays pieces on a grid format, requires user interaction, has move
 * counts
 *
 * @author Scott Wilson
 **********************************************************************/
public class GameActivity extends AppCompatActivity {

    /** GridView to display the individual pieces in */
    GridView grid;

    /** TextView to display the number of moves a player has made */
    TextView moveCount;

    /** TextView to display the time player has spent on game */
    TextView timerText;
    Timer t;
    private int seconds = 0;
    private int minutes = 0;
    private int totalSeconds;

    /** The custom adapter used to display the pieces in the GridView */
    CustomAdapter adapter;

    GameBoard board;
    Bitmap image;

    /** Boolean representing if text on grid is visible */
    Boolean isVisible;

    /** Application Shared Preferences and file name */
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "SliderPuzzleFile";

    /*******************************************************************
     * First called when the activity is started
     * Creates the GridView board layout
     * Initializes all elements for the user to begin playing
     *
     * @param savedInstanceState currently unused parameter
     ******************************************************************/
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.game);

        // Instantiate the Shared Preferences object
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);

        //Retrieves information about the physical size of the users
        //device
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Store the width and height of the device
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        //Check if user has selected a custom board size
        Bundle extras = getIntent().getExtras();
        if (extras.getInt("boardSize") != 0) {
            //Apply the user's selection
            board = new GameBoard(extras.getInt("boardSize"));

        }

        //Otherwise sets GameBoard size to 3
        else
            board = new GameBoard(3);

        //Initializes the image to be used in this puzzle
        if (extras.getInt("imageID") != 0 && extras.getInt("imageID")
                != 1) {
            image = BitmapFactory.decodeResource(getResources(),
                    extras.getInt("imageID"));
        } else if (extras.getByteArray("customImage") != null) {
            byte[] imageArray = extras.getByteArray("customImage");
            image = BitmapFactory.decodeByteArray(imageArray, 0,
                    imageArray.length);
        } else {
            image = BitmapFactory.decodeResource(getResources(),
                    R.drawable.cat);
        }

        //Checks to see which dimension of the users device is smaller
        if (width > height)

            //Width is greater, create pieces the size of the height
            board.setImages(splitBitmap(image, board.getColumns(),
                    height / board.getColumns()));
        else
            //Height is greater, create pieces the size of the width
            board.setImages(splitBitmap(image, board.getColumns(),
                    width / board.getColumns()));

        //Initialize grid based off parameters declared in xml
        grid = (GridView) findViewById(R.id.gridView);

        //Initialize move counter based off parameters declared in xml
        moveCount = (TextView) findViewById(R.id.movesMade);

        //Start a new game
        board.newGame();

        //Shuffle the pieces on the board
        board.shuffleBoard();

        //Set size of the columns in GridView based off users device
        //dimensions
        grid.setColumnWidth(width / board.getColumns());

        //Set custom adapter with this context and the array of pieces
        adapter = new CustomAdapter(this, board);
        //Set this adapter to the grid
        grid.setAdapter(adapter);

        // initialize timer
        beginTimer();
//        t = new Timer();

        // total seconds of users game, used for saving high score
        totalSeconds = 0;

        isVisible = true;

        /***************************************************************
         * A click listener for the grid
         * When user clicks on a valid piece it will move appropriately
         **************************************************************/
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Defines what happens when the user clicks on a given piece
             *
             * @param parent the parent view of the view in which the listener exists
             * @param v the view in which the listener exists
             * @param position the position of the piece clicked
             * @param id the id of the piece clicked
             */
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                //Check if the user clicked on a valid move position
                if (board.checkMove(position)) {
                    //Move the pieces appropriately
                    board.movePieces(position);
                    moveCount.setText("Moves made: " +
                            board.getMoveCounter());
                    //Notify adapter that data has changed and redraw
                    ((BaseAdapter) grid.getAdapter())
                            .notifyDataSetChanged();

                    //Check if a game is in progress
                    if (board.getGameProgress())

                        //Check to see if the user won
                        if (board.checkIfWon()) {
                            //Tell the user they won
                            Toast.makeText(getApplicationContext(),
                                    "You win!", Toast.LENGTH_SHORT)
                                    .show();
                            //Refresh adapter to hide numbers
                            ((BaseAdapter) grid.getAdapter())
                                    .notifyDataSetChanged();
                            t.cancel();
                            setHighScore();
                            t.purge();
                        }
                }
            }
        });
    }

    /*******************************************************************
     * Overrides method in Menu class. Initializes the contents of the
     * Game Activity's standard options menu.
     *
     * @param menu The options menu in which the items are placed
     * @return true Must return true for the menu to be displayed
     ******************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gameactivity, menu);
        return true;
    }

    /*******************************************************************
     * Overrides method in MenuItem class. Called whenever an item in
     * the Game Activity menu is selected.
     *
     * @param item The menu item that was selected
     * @return true  Consume menu processing here
     * false Allows normal menu processing to proceed
     ******************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // performs tasks depending on menu item selected
        switch (item.getItemId()) {
            case R.id.reset:
                resetBoard();
                return true;
            case R.id.returnToMenu:
                board = null;
                t.cancel();
                t.purge();
                finish();
                return true;
            case R.id.numberVisibility:
                if(board.getGameProgress()) {
                    // if numbers are visible
                    if (isVisible) {
                        // make invisible
                        adapter.updateView(0);
                        isVisible = false;
                    } else {
                        // otherwise make visible
                        adapter.updateView(1);
                        isVisible = true;
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*******************************************************************
     * Begins a new game. Shuffles board, resets timer and move counter.
     ******************************************************************/
    private void resetBoard() {

        totalSeconds = 0;
        minutes = 0;
        seconds = 0;
        if(!board.getGameProgress()) {
            beginTimer();
        }
        board.newGame();
        board.shuffleBoard();
        moveCount.setText("Moves made: " +
                board.getMoveCounter());
        ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();
    }

    /*******************************************************************
     * Saves player's high score based on board size. Saves game time as
     * total seconds along with board size and date. Saves all these as
     * a string into array lists, sorted by board size.
     ******************************************************************/
    private void setHighScore() {

        // sets game time to total seconds of players game
        int gameTime = getTotalSeconds();

        // string used for sorting high scores array lists
        String boardSize = board.getBoardSize() + "x"
                + board.getBoardSize();

        // if the game is a valid game
        if (gameTime > 0) {

            // initializes a shared preferences editor that will
            // store the string
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();

            // sets date format to US
            DateFormat dateForm = new SimpleDateFormat
                    ("dd MMMM yyyy", Locale.US);

            // sets this format to date output to be used
            String dateOutput = dateForm.format(new Date());

            // pulls previously saved array list of selected board size
            String scores = gamePrefs.getString(boardSize, "");

            // if previously saved scores exist
            if (scores.length() > 0) {

                // instantiates new array list and array for sorting
                List<TimerScore> scoreStrings = new ArrayList<>();
                String[] allScores = scores.split("\\|");

                // adds previously saved scores into scoreStrings
                for (String s : allScores) {
                    String[] parts = s.split(" - ");
                    scoreStrings.add(new TimerScore(parts[0],
                            Integer.parseInt(parts[1])));
                }

                // creates a new saved score from user's game
                TimerScore newScore = new TimerScore(dateOutput,
                        gameTime);

                // adds new score to array list of all scores
                scoreStrings.add(newScore);

                // sorts array list of all scores
                Collections.sort(scoreStrings);

                // initialize new scorebuilder
                StringBuilder scoreBuild = new StringBuilder("");

                // cuts array list of saved scores to the top ten scores
                for (int i = 0; i < scoreStrings.size(); i++) {

                    //only want ten top scores
                    if (i >= 10) break;

                    //pipe separates the score strings
                    if (i > 0) scoreBuild.append("|");

                    // builds new high score string for saving
                    scoreBuild.append(scoreStrings.get(i)
                            .getScoreText());
                }

                //write top ten high scores to prefs
                scoreEdit.putString(boardSize, scoreBuild.toString());
                scoreEdit.apply();
            }

            // else if no saved scores exist
            else {

                // write users score to prefs
                scoreEdit.putString(boardSize, "" + dateOutput + " - "
                        + gameTime);
                scoreEdit.apply();
            }

        }
    }

    /*******************************************************************
     * Chops the image to be used for the game up into smaller pieces
     *
     * @param picture   the picture in which is to be chopped up
     * @param gridSize  size of the grid the game will be played on
     * @param pieceSize the physical dimensions in pixels of the pieces
     * @return an array containing the chopped up pieces
     ******************************************************************/
    Bitmap[] splitBitmap(Bitmap picture, int gridSize, int pieceSize) {

        //Create a bitmap image based upon the original image
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture,
                pieceSize * gridSize, pieceSize * gridSize, true);

        //Create the array in which the chopped images will be saved in
        Bitmap[] images = new Bitmap[gridSize * gridSize + 1];

        //Iterate and create images row by row
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                images[row * gridSize + column] = Bitmap.createBitmap
                        (scaledBitmap, column * pieceSize,
                                row * pieceSize, pieceSize, pieceSize);

            }
        }
        images[gridSize * gridSize] = images[gridSize * gridSize - 1];

        //Create a bitmap image for the blank piece and resize
        Bitmap greyImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.greyimage);
        Bitmap greyImageResized = Bitmap.createScaledBitmap(greyImage,
                pieceSize, pieceSize, true);

        //Assign it to the array
        images[gridSize * gridSize - 1] = Bitmap.createBitmap
                (greyImageResized);

        greyImage.recycle();
        greyImageResized.recycle();
        scaledBitmap.recycle();
        return images;
    }

    /*******************************************************************
     * Timer that starts at time zero and adds a second every 1000
     * milliseconds.  Updates timer text every second.
     ******************************************************************/
    public void beginTimer() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //Initialize timer from xml parameters
                        timerText = (TextView) findViewById
                                (R.id.timerText);

                        // when seconds is 60
                        if (seconds == 60) {

                            // set seconds to 0, add minute
                            seconds = 0;
                            minutes = minutes + 1;
                        }

                        // format text depending on seconds
                        if (seconds > 9) {
                            timerText.setText("Time: " + minutes + ":"
                                    + seconds);
                        } else {
                            timerText.setText("Time: " + minutes + ":0"
                                    + seconds);
                        }

                        // add 1 second to timer
                        // add 1 second to total seconds
                        seconds += 1;
                        totalSeconds += 1;
                    }
                });
            }
        }, 0, 1000);
    }

    /*******************************************************************
     * Getter method for number of seconds
     *
     * @return value representing the number of seconds
     ******************************************************************/
    public int getTotalSeconds() {
        return totalSeconds;
    }
}
