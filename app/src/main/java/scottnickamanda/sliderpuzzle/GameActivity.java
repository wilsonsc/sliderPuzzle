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

    /** The custom adapter used to display the pieces in the GridView */
    CustomAdapter adapter;

    GameBoard board;
    Bitmap image;


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

        //Retrieves information about the physical size of the users
        //device
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Store the width and height of the device
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        //Sets the physical dimensions of the device
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
        if (extras.getInt("imageID") != 0 ) {
            image = BitmapFactory.decodeResource(getResources(),
                    extras.getInt("imageID"));
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
                    ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();

                    //Check if a game is in progress
                    if (board.getGameProgress())

                        //Check to see if the user won
                        if (board.checkIfWon()) {
                            //Tell the user they won
                            Toast.makeText(getApplicationContext(),
                                    "You win!", Toast.LENGTH_SHORT)
                                    .show();
                            //Refresh adapter to hide numbers
                            ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();
                        }
                }
            }
        });
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
        Bitmap greyImage = BitmapFactory.decodeResource(getResources(), R.drawable.greyimage);
        Bitmap greyImageResized = Bitmap.createScaledBitmap(greyImage, pieceSize, pieceSize, true);

        //Assign it to the array
        images[gridSize*gridSize-1] = Bitmap.createBitmap
                (greyImageResized);
        return images;
    }


}