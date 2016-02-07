package scottnickamanda.sliderpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    GridView grid;
    int gameSize = 9;
    int columns = 3;
    int width = 3;
    boolean gameInProgress;
    int blankPiece = gameSize-1;

    Piece[] pieces;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        newGame();

        grid = (GridView) findViewById(R.id.gridView);


        ArrayAdapter<Piece> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pieces);

        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //Toast.makeText(getApplicationContext(), String.valueOf(blankPiece),
                        //Toast.LENGTH_SHORT).show();;
                if (checkMove(position)) {
                    movePieces(position);
                }
                ((BaseAdapter) grid.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    void newGame() {
        pieces = new Piece[gameSize];
        for (int i = 0; i < gameSize - 1; i++)

        {
            pieces[i] = new Piece(i);
        }
            pieces[gameSize-1] = new Piece(-1);
        gameInProgress = true;
    }

    //Check to see if this is a valid move, checks 4 directions
    boolean checkMove(int pieceNumber) {
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
        int temp = pieces[pieceNumber].getNumber() - 1;
        pieces[pieceNumber].setNumber(-1);
        pieces[blankPiece].setNumber(temp);
        blankPiece = pieceNumber;
    }
}
