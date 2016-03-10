package scottnickamanda.sliderpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.GridView;

/**
 * An activity where the user can select an image to use in the game
 */
public class SelectImageActivity extends AppCompatActivity {

    /** GridView to display the individual pieces in */
    GridView listView;

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
        listView = (GridView) findViewById(R.id.imageSelect);
        listView.setAdapter(new ImageAdapter(this, width));

    }
}
