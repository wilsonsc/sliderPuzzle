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

    /** Application Shared Preferences and file name*/
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
        if (extras.getInt("boardSize") != 0){
            //Apply the user's selection
            board = new GameBoard(extras.getInt("boardSize"));

        }

        //Otherwise sets GameBoard size to 3
        else
            board = new GameBoard(3);

        //Initializes the image to be used in this puzzle
        if (extras.getInt("imageID") != 0 && extras.getInt("imageID") != 1) {
            image = BitmapFactory.decodeResource(getResources(),
                    extras.getInt("imageID"));
        }
        else if (extras.getByteArray("customImage") != null){
            byte[] imageArray = extras.getByteArray("customImage");
            image = BitmapFactory.decodeByteArray(imageArray, 0,
                   imageArray.length);
        }
        else {
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
        grid.setColumnWidth(width/board.getColumns());

        //Set the custom adapter with this context and the array of pieces
        adapter = new CustomAdapter(this, board);
        //Set this adapter to the grid
        grid.setAdapter(adapter);

        totalSeconds = 0;
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //Initialize timer based off parameters declared in xml
                        timerText = (TextView) findViewById(R.id.timerText);

                        if (seconds == 60) {
                            seconds = 0;
                            minutes = minutes + 1;
                        }
                        if (seconds > 10) {
                            timerText.setText("Time: "+String.valueOf(minutes) + ":" + String.valueOf(seconds));
                        } else {
                            timerText.setText("Time: "+String.valueOf(minutes) + ":0" + String.valueOf(seconds));
                        }

                        seconds += 1;
                        totalSeconds += 1;
                    }
                });
            }
        }, 0, 1000);

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
                        }
                }
            }
        });
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        int instanceSeconds = getTotalSeconds();
//        savedInstanceState.putInt("totalSeconds",instanceSeconds);
//        int moves = board.getMoveCounter();
//        savedInstanceState.putInt("moves",moves);
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        totalSeconds = savedInstanceState.getInt("totalSeconds");
//        int moves = savedInstanceState.getInt("moves");
//        moveCount.setText("Moves made: " + moves);
//    }

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
     *         false Allows normal menu processing to proceed
     ******************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.reset:
                resetBoard();
                return true;
            case R.id.returnToMenu:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void resetBoard() {

        board.newGame();
        board.shuffleBoard();
        moveCount.setText("Moves made: " +
                board.getMoveCounter());
        totalSeconds = 0;
        minutes = 0;
        seconds = 0;
        ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();
    }

    private void setHighScore() {
        int gameTime = getTotalSeconds();
        String boardSize = board.getBoardSize()+"x"+board.getBoardSize();

        if(gameTime > 0) {

            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            String dateOutput = dateForm.format(new Date());
            String scores = gamePrefs.getString(boardSize, "");
            if(scores.length()>0){

                List<TimerScore> scoreStrings = new ArrayList<>();
                String[] allScores = scores.split("\\|");

                for(String s : allScores){
                    String[] parts = s.split(" - ");
                    scoreStrings.add(new TimerScore(parts[0], Integer.parseInt(parts[1])));
                }

                TimerScore newScore = new TimerScore(dateOutput, gameTime);
                scoreStrings.add(newScore);

                Collections.sort(scoreStrings);

                StringBuilder scoreBuild = new StringBuilder("");
                for(int i=0; i<scoreStrings.size(); i++){
                    if(i>=10) break;//only want ten
                    if(i>0) scoreBuild.append("|");//pipe separate the score strings
                    scoreBuild.append(scoreStrings.get(i).getScoreText());
                }

                //write to prefs
                scoreEdit.putString(boardSize, scoreBuild.toString());
                scoreEdit.apply();

            }
            else{
                scoreEdit.putString(boardSize, ""+dateOutput+" - "+gameTime);
                scoreEdit.apply();
            }

        }
    }

    /*******************************************************************
     * Chops the image to be used for the game up into smaller pieces
     *
     * @param picture the picture in which is to be chopped up
     * @param gridSize size of the grid the game will be played on
     * @param pieceSize the physical dimensions in pixels of the pieces
     * @return an array containing the chopped up pieces
     ******************************************************************/
    Bitmap[] splitBitmap(Bitmap picture,int gridSize, int pieceSize) {

        //Create a bitmap image based upon the original image
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture,
                pieceSize*gridSize, pieceSize*gridSize, true);

        //Create the array in which the chopped images will be saved in
        Bitmap[] images = new Bitmap[gridSize*gridSize+1];

        //Iterate and create images row by row
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                images[row*gridSize+column] = Bitmap.createBitmap
                        (scaledBitmap, column * pieceSize,
                                row * pieceSize, pieceSize, pieceSize);
            }
        }
        images[gridSize*gridSize] = images[gridSize*gridSize-1];

        //Create a bitmap image for the blank piece and resize
        Bitmap greyImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.greyimage);
        Bitmap greyImageResized = Bitmap.createScaledBitmap(greyImage,
                pieceSize, pieceSize, true);

        //Assign it to the array
        images[gridSize*gridSize-1] = Bitmap.createBitmap
                (greyImageResized);
        return images;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }
}