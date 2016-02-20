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

public class GameActivity extends AppCompatActivity {

    GridView grid;
    TextView moveCount;
    int gameSize = 9;
    int columns = 3;
    boolean gameInProgress;
    int blankPiece = gameSize-1;
    int moveCounter = 0;

    Piece[] pieces;
    Bitmap bm;
    Bitmap[] split;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            columns = extras.getInt("boardSize");
            gameSize = columns * columns;
            blankPiece = gameSize-1;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.catpicture);

        if (width > height)
         split = splitBitmap(bm, columns, height / columns);
        else
        split = splitBitmap(bm, columns, width / columns);
        grid = (GridView) findViewById(R.id.gridView);
        moveCount = (TextView) findViewById(R.id.movesMade);
        newGame();
        shuffleBoard();


        grid.setColumnWidth(width/columns);

        CustomAdapter adapter = new CustomAdapter(this, pieces);


        grid.setAdapter(adapter);





        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
              // Toast.makeText(getApplicationContext(), String.valueOf(w),
                       //Toast.LENGTH_SHORT).show();;
                if (checkMove(position)) {
                    movePieces(position);
                    moveCounter++;
                    moveCount.setText("Moves made: " + moveCounter);
                }
                ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();
                if (blankPiece == gameSize-1 && gameInProgress)
                    if (checkIfWon()) {
                        Toast.makeText(getApplicationContext(), "You win!",
                        Toast.LENGTH_SHORT).show();
                        gameInProgress = false;
                    }
            }
        });
    }

    void newGame() {
        pieces = new Piece[gameSize];
        for (int i = 0; i < gameSize - 1; i++)

        {
            pieces[i] = new Piece(i, split[i]);
        }
            pieces[gameSize-1] = new Piece(-1);
        gameInProgress = true;
    }

    //Check to see if this is a valid move, checks 4 directions
    boolean checkMove(int pieceNumber) {
        if (!gameInProgress)
            return false;
        if (pieceNumber == blankPiece)
            return false;
        if (pieceNumber + columns == blankPiece ||
                pieceNumber - columns == blankPiece ||
                (pieceNumber + 1 == blankPiece || pieceNumber - 1 == blankPiece) &&
                        pieceNumber / columns == blankPiece / columns)
            return true;
        return false;
    }

    //Switches the display of 2 pieces (blank and parameter)
    public void movePieces(int pieceNumber) {
        if (pieceNumber >= gameSize || pieceNumber < 0)
            return;
        pieces[blankPiece].setNumber(pieces[pieceNumber].getNumber() - 1);
        pieces[pieceNumber].setNumber(-1);
        pieces[blankPiece].setImage(pieces[pieceNumber].getImage());
        pieces[pieceNumber].setImage(split[gameSize-1]);
        blankPiece = pieceNumber;

    }

    public Bitmap[] splitBitmap(Bitmap picture,int gridSize, int pieceSize)
    {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, pieceSize*gridSize,
                    pieceSize*gridSize, true);
        Bitmap[] imgs = new Bitmap[gridSize*gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                imgs[row*gridSize+column] = Bitmap.createBitmap(scaledBitmap, column *
                    pieceSize, row * pieceSize, pieceSize, pieceSize);
            }
        }
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        imgs[gridSize*gridSize - 1] = Bitmap.createBitmap(pieceSize, pieceSize, conf);

        return imgs;
    }

    public void shuffleBoard() {
        int roll;
        for (int i = 0; i < gameSize*gameSize*2; i++) {
            roll = (int) (Math.random() * 4);
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

    public boolean checkIfWon() {
        for (int i = 0; i < gameSize - 1; i++) {
            if (pieces[i].getNumber() != i+1)
                return false;
        }
        return true;
    }
}
