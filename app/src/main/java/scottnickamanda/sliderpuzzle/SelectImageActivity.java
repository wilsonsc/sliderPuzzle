package scottnickamanda.sliderpuzzle;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/***********************************************************************
 * An activity where the user can select an image to use in the game
 **********************************************************************/
public class SelectImageActivity extends AppCompatActivity {

    /** GridView to display the individual pieces in */
    GridView imageGrid;
    long selected;
    String pathToFile;
    int LOAD_CUSTOM_IMAGE = 25;
    ImageAdapter adapter;

    /*******************************************************************
     * First called when the activity is started
     * Creates a grid to display the images
     * Listeners attached the grid to record what the user selected
     *
     * @param savedInstanceState currently unused parameter
     ******************************************************************/
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectimage);

        //Retrieves information about the physical size of the device
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Store the width of the device
        final int width = dm.widthPixels;

        //Create the listView
        imageGrid = (GridView) findViewById(R.id.imageSelect);
        adapter = new ImageAdapter(this, width);
        imageGrid.setAdapter(adapter);

        imageGrid.setOnItemClickListener
                (new AdapterView.OnItemClickListener() {
            /***********************************************************
             * Defines what happens when the user clicks on a given
             * piece
             *
             * @param parent the parent view of the view in which the
             *               listener exists
             * @param v the view in which the listener exists
             * @param position the position of the piece clicked
             * @param id the id of the piece clicked
             **********************************************************/
            public void onItemClick(AdapterView parent, View v,
                                    int position, long id) {
                adapter.getItem(position);

                //Check if the user clicked on a valid move position
                selected = adapter.getItemId(position);
                Intent intent = new Intent();

                //If the user selected a custom image, add to the intent
                intent.putExtra("imageID", selected);

                //Set the result to ok
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button importImage = (Button) findViewById(R.id.customImage);

        //Create a listener for the button
        importImage.setOnClickListener(new View.OnClickListener() {

            /*********************************************************
             * Launches image select activity when user clicks button
             *
             * @param v the view of the button
             *********************************************************/
            public void onClick(View v) {

                Intent intent =new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //Start the activity with the request code corresponding
                //to an custom image request
                startActivityForResult(intent, LOAD_CUSTOM_IMAGE);

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == LOAD_CUSTOM_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the image
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pathToFile = cursor.getString(columnIndex);
                cursor.close();
                Bitmap customImage = BitmapFactory.decodeFile(pathToFile);
                Intent intent = new Intent();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                customImage.compress(Bitmap.CompressFormat.PNG, 100, os);
                intent.putExtra("image", os.toByteArray());

                //Free up resources
                os.close();
                customImage.recycle();

                //Set the result to ok
                setResult(RESULT_OK, intent);
                finish();

            } else {
                Toast.makeText(this, "No Image selected",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
}