package scottnickamanda.sliderpuzzle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/***********************************************************************
 * A custom image adapter to display images in a ListView
 **********************************************************************/
public class ImageAdapter extends BaseAdapter{

    /** context that will later be inflated */
    private Context mContext;
    /** size in which the images will be*/
    int size;

    /*******************************************************************
     * Default constructor
     * Saves width to determine size of images
     *
     * @param context context resource from which this class was called
     * @param width the physical width of the users device
     ******************************************************************/
    public ImageAdapter(Context context, int width) {
        mContext = context;
        size = width / 2;
    }

    /*******************************************************************
     * A count of how many images are currently to be displayed
     * @return the number of images
     ******************************************************************/
    public int getCount() {
        return mThumbIds.length;
    }

    /*******************************************************************
     * This method does nothing, needed because we extended BaseAdapter
     ******************************************************************/
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    /*******************************************************************
     * Allows access of the ID of a selected image
     *
     * @param position the position in the adapter corresponding to the
     *                 image
     * @return the numerical id of the image
     ******************************************************************/
    public long getItemId(int position) {
        return mThumbIds[position];
    }

    //Create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            //If it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ListView.LayoutParams
                    (size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(10, 10, 10, 10);
        }
        else {

            //Image is recycled, set imageView to it
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    //References to our images
    private Integer[] mThumbIds = {
            R.drawable.cat,
            R.drawable.monkey,
            R.drawable.lemurs,
            R.drawable.tiger,
            R.drawable.palousefalls,
            R.drawable.prismaticsprings
    };
}
