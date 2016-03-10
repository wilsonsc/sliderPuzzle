package scottnickamanda.sliderpuzzle;

import android.content.Intent;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/***********************************************************************
 * The first screen displayed upon opening the application
 * Allows the user to select the game size before beginning
 *
 * @author Scott Wilson
 **********************************************************************/
public class MainActivity extends AppCompatActivity {

    /** The currently selected custom size of the game, if any*/
    static int currentSize;

    /*******************************************************************
     * Called upon loading the application
     * Load the visual elements for the user
     *
     * @param savedInstanceState no save state currently implemented
     ******************************************************************/
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Set the display based off values defined in xml
        setContentView(R.layout.main);

        /** The possible sizes the user may select for the game*/
        String[] sizes = new String[] {"3x3", "4x4", "5x5"};

        /** An adapter that holds possible user sizes in dropdown box*/
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this,
                    android.R.layout.simple_spinner_item, sizes);

        //Sets the dropdown box based off xml defined values
        Spinner gameSize = (Spinner) findViewById(R.id.selectSize);

        //Sets the adapter for the dropdown box
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
                setSize(position+3);
            }

            /***********************************************************
             * Must be defined because of extending, does nothing
             **********************************************************/
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Create a button based off xml defined values
        Button selectImage = (Button) findViewById(R.id.selectImage);

        //Create a listener for the button
        selectImage.setOnClickListener(new View.OnClickListener() {

            /**
             * Begins the game when the user clicks the button
             *
             * @param v the view of the button
             */
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),
                        SelectImageActivity.class);

                //If the user selected a custom board size,
                //add this as an extra in the intent

                //Begin
                startActivity(intent);
            }
        });

        //Create a button based off xml defined values
        Button launchGame = (Button) findViewById(R.id.newGame);

        //Create a listener for the button
        launchGame.setOnClickListener(new View.OnClickListener() {

            /**
             * Begins the game when the user clicks the button
             *
             * @param v the view of the button
             */
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),
                        GameActivity.class);

                //If the user selected a custom board size,
                //add this as an extra in the intent
                intent.putExtra("boardSize",getSize());

                //Begin
                startActivity(intent);
            }
        });
    }

    /*******************************************************************
     * Helper variable to access the custom board defined user board size
     *
     * @return the currently selected size
     ******************************************************************/
    static int getSize() {

        return currentSize;
    }

    /*******************************************************************
     * Helper variable to set the user's selected custom size
     *
     * @param size the size in which the user wants the board to be
     ******************************************************************/
    static void setSize(int size) {

        currentSize = size;
    }
}



