package scottnickamanda.sliderpuzzle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main activity class for the Slider Puzzle game
 * Displays pieces on a grid format, requires user interaction, has move counts
 *
 * @author Scott Wilson
 */
public class GameActivity extends AppCompatActivity {

    /* GridView to display the individual pieces in*/
    GridView grid;
    /* TextView to display the number of moves a player has made*/
    TextView moveCount;
    /* The number of pieces on the current puzzle, default value initialized*/
    int gameSize = 9;
    /* The number of columns on the current puzzle, default value initialized*/
    int columns = 3;
    /* True if the game is currently in progress, false if the game has not started or has ended*/
    boolean gameInProgress;
    /* A representation of the index of the "blank" piece*/
    int blankPiece = gameSize-1;
    /* The number of moves the player has made*/
    int moveCounter = 0;
    /* An array to hold all of the pieces that the puzzle is made of*/
    Piece[] pieces;
    /* The image that is being used in the current game*/
    Bitmap image;
    /* An array holding the individual chopped up image pieces*/
    Bitmap[] choppedImage;
    /* The custom adapter that will be used to display the pieces in the GridView*/
    CustomAdapter adapter;


    /**
     * First called when the activity is started
     * Creates the GridView board layout
     * Initializes all elements for the user to begin playing
     *
     * @param savedInstanceState currently unused parameter
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        //Check if the user has selected a custom board size from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Apply the user's selection
            columns = extras.getInt("boardSize");
            gameSize = columns * columns;
            blankPiece = gameSize-1;
        }
        //Retrieve information about the physical size of the users device
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //Store the width and height of the device
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        //Initialize the image to be used in this puzzle
        image = BitmapFactory.decodeResource(getResources(), R.mipmap.catpicture);

        //Check to see which dimension of the users device is smaller
        if (width > height)
            //Width is greater, create pieces the size of the height
            choppedImage = splitBitmap(image, columns, height / columns);
        else
            //Height is greater, create pieces the size of the width
            choppedImage = splitBitmap(image, columns, width / columns);

        //Initialize grid based off parameters declared in xml
        grid = (GridView) findViewById(R.id.gridView);
        //Initialize move counter based off parameters declared in xml
        moveCount = (TextView) findViewById(R.id.movesMade);
        //Start a new game
        newGame();
        //Shuffle the pieces on the board
        shuffleBoard();
        //Set the size of the columns in the GridView based off users device dimensions
        grid.setColumnWidth(width/columns);
        //Set the custom adapter with this context and the array of pieces
        adapter = new CustomAdapter(this, pieces);
        //Set this adapter to the grid
        grid.setAdapter(adapter);

        /**
         * A click listener for the grid
         * When user clicks on a valid piece it will move appropriately
         */
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
                if (checkMove(position)) {
                    //Move the pieces appropriately
                    movePieces(position);
                    //Increase the move counter and update the view
                    moveCounter++;
                    moveCount.setText("Moves made: " + moveCounter);
                    //Notify the adapter that data has changed, it will redraw
                    ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();
                    //Check if a game is in progress
                    if (gameInProgress)
                        //Check to see if the user won
                        if (checkIfWon()) {
                            //Tell the user they won
                            Toast.makeText(getApplicationContext(), "You win!",
                                    Toast.LENGTH_SHORT).show();
                            //Game is over, set variable appropriately
                            gameInProgress = false;
                        }
                }
            }
        });
    }

    /**
     * User is ready to play a new game, create the game with the appropriate size
     */
    void newGame() {
        //Declare pieces array size based off current game size
        pieces = new Piece[gameSize];
        //Initialize pieces and declare text and image
        for (int i = 0; i < gameSize - 1; i++) {
            pieces[i] = new Piece(i, choppedImage[i]);
        }
        //Final piece does not get an image and gets a number that will not be displayed
        //-1 indicates the piece is to not be shown to the user
        pieces[gameSize-1] = new Piece(-1);
        //Begin the game
        gameInProgress = true;
    }

    /**
     * Checks to see if this is a valid move
     * If the piece that was clicked on is adjacent to the blank piece, it is
     */

    boolean checkMove(int pieceNumber) {
        //Game must be in progress to be a valid move
        if (!gameInProgress)
            return false;
        //Must be clicking on a piece that is not the blank piece
        if (pieceNumber == blankPiece)
            return false;
        //Check to see if piece is adjacent to the blank piece
        if (pieceNumber + columns == blankPiece ||
                pieceNumber - columns == blankPiece ||
                (pieceNumber + 1 == blankPiece || pieceNumber - 1 == blankPiece) &&
                        pieceNumber / columns == blankPiece / columns)
            return true;
        return false;
    }

    /**
     * Swaps the values of the two pieces
     * @param pieceNumber the index of the piece that was clicked
     */
    public void movePieces(int pieceNumber) {
        //Make sure the piece exists
        if (pieceNumber >= gameSize || pieceNumber < 0)
            return;
        //Sets the blank piece to the values which were in the piece the user clicked
        pieces[blankPiece].setNumber(pieces[pieceNumber].getNumber() - 1);
        pieces[blankPiece].setImage(pieces[pieceNumber].getImage());
        //Sets the piece the user clicked to the values of the blank piece
        pieces[pieceNumber].setNumber(-1);
        pieces[pieceNumber].setImage(choppedImage[gameSize-1]);
        //Locally save the index of the blank piece
        blankPiece = pieceNumber;

    }

    /**
     * Chops the image to be used for the game up into smaller pieces
     *
     * @param picture the picture in which is to be chopped up
     * @param gridSize the size of the grid in which the game will be played on
     * @param pieceSize the physical dimensions in pixels of the chopped pieces
     * @return an array containing the chopped up pieces
     */
    public Bitmap[] splitBitmap(Bitmap picture,int gridSize, int pieceSize) {
        //Create a bitmap image based upon the original image that is able to be chopped
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, pieceSize*gridSize,
                    pieceSize*gridSize, true);
        //Create the array in which the chopped images will be saved in
        Bitmap[] images = new Bitmap[gridSize*gridSize];

        //Iterate and create images row by row
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                images[row*gridSize+column] = Bitmap.createBitmap(scaledBitmap, column *
                    pieceSize, row * pieceSize, pieceSize, pieceSize);
            }
        }
        //Create a blank image for the blank piece
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        //Assign it to the array
        images[gridSize*gridSize - 1] = Bitmap.createBitmap(pieceSize, pieceSize, conf);

        return images;
    }

    /**
     * Shuffles the board for the user
     * Simulates a random "roll" from 1-4 (0-3)
     * Each value of the roll corresponds to a direction in which we attempt to brute force move
     * the piece to.
     * This will be repeated equal to the number of tiles squared * 2 times
     * ****This implementation ensures the game will ALWAYS have a valid solution****
     * A pure random shuffling would occur in unsolvable puzzles
     */
    public void shuffleBoard() {
        /* The value of the random roll*/
        int roll;
        for (int i = 0; i < gameSize*gameSize*2; i++) {
            //Roll
            roll = (int) (Math.random() * 4);
            //Force move based off what was rolled
            switch (roll) {
                case 0:
                    if (checkMove(blankPiece - columns)) {
                        movePieces(blankPiece - columns);
                    }
                    break;
                case 1:
                    if (checkMove(blankPiece + columns)) {
                        movePieces(blankPiece + columns);
                    }
                    break;
                case 2:
                    if (checkMove(blankPiece - 1)) {
                        movePieces(blankPiece - 1);
                    }
                    break;
                case 3:
                    if (checkMove(blankPiece + 1)) {
                        movePieces(blankPiece + 1);
                    }
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * Checks to see if the board is in a state which is won
     * @return true if the game is won, false otherwise
     */
    public boolean checkIfWon() {
        //Check bottom right corner for validity
        if (blankPiece != gameSize-1)
            return false;
        //Check pieces, starting with upper left for validity
        for (int i = 0; i < gameSize - 1; i++) {
            if (pieces[i].getNumber() != i+1)
                return false;
        }
        return true;
    }
}