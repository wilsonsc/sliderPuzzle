package scottnickamanda.sliderpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

/**
 * An activity where the user can select an image to use in the game
 */
public class SelectImageActivity extends AppCompatActivity {

    /** GridView to display the individual pieces in */
    GridView imageGrid;
    long selected;
    ImageAdapter adapter;

    /**
     * First called when the activity is started
     * Creates a grid to display the images
     * Listeners attached the grid to record what the user selected
     *
     * @param savedInstanceState currently unused parameter
     */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectimage);
        //Retrieves information about the physical size of the users device
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //Store the width of the device
        final int width = dm.widthPixels;

        //Create the listView
        imageGrid = (GridView) findViewById(R.id.imageSelect);
        adapter = new ImageAdapter(this, width);
        imageGrid.setAdapter(adapter);
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Defines what happens when the user clicks on a given piece
             *
             * @param parent the parent view of the view in which the listener exists
             * @param v the view in which the listener exists
             * @param position the position of the piece clicked
             * @param id the id of the piece clicked
             */
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                adapter.getItem(position);
                //Check if the user clicked on a valid move position
                selected = adapter.getItemId(position);
                Intent intent = new Intent();

                //If the user selected a custom image, add it to the intent
                intent.putExtra("imageID", selected);
                //Set the result to ok
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        /* -MAY USE LATER-
        Button verifySelection = (Button) findViewById(R.id.verifySelection);

        //Create a listener for the button
        verifySelection.setOnClickListener(new View.OnClickListener() {


             * Begins the game when the user clicks the button
             *
             * @param v the view of the button

            public void onClick(View v) {

                Intent intent = new Intent();

                //If the user selected a custom image, add it to the intent
                intent.putExtra("imageID", selected);
                //Set the result to ok
                setResult(RESULT_OK, intent);
                finish();
            }
        });*/
    }
}
