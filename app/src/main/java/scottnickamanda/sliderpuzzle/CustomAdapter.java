
package scottnickamanda.sliderpuzzle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom wrote adapter to allow Piece class files to be visually shown in
 * a grid layout.
 *
 * @author Scott Wilson
 */

public class CustomAdapter extends BaseAdapter {

    /** context that will later be inflated */
    private Context context;

    /** the pieces that the adapter will display */
    private Piece[] pieces;

    /**
     * Default constructor
     * Saves parameters for future use
     *
     * @param context context resource from which this class was called
     * @param board the current game board
     */
    public CustomAdapter(Context context, GameBoard board) {
        this.context = context;
        pieces = board.getPieces();
    }

    /**
     * Produces a displayable view for the display element that uses this
     * CustomAdapter
     *
     * @param pos the position of the piece
     * @param convertView a view that will be reused if present
     * @param parent the group in which this view belongs to
     * @return the created view
     */
    public View getView(int pos, View convertView, ViewGroup parent) {

        //Inflate context
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //If we are able to reuse the convertView we will, otherwise we will create a new view
        if (convertView == null) {
            //Create a new view
            View customView = inflater.inflate(R.layout.pieces, null);
            //Add a TextView with parameters defined in xml
            TextView tv = (TextView) customView.findViewById(R.id.textView1);
            //Add an ImageView with parameters defined in xml
            ImageView iv = (ImageView) customView.findViewById(R.id.imageView1);

            //Set the text of the TextView
            tv.setText(pieces[pos].toString());
            //Set the image of the ImageView
            iv.setImageBitmap(pieces[pos].getImage());

            //Return the View now that it has all elements attached to it
            return customView;
        }
        //There is an existing view that we may recycle, use it
        else {
            //Add a TextView with parameters defined in xml
            TextView tv = (TextView) convertView.findViewById(R.id.textView1);
            //Add an ImageView with parameters defined in xml
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);

            //Set the text of the TextView
            tv.setText(pieces[pos].toString());
            //Set the image of the ImageView
            iv.setImageBitmap(pieces[pos].getImage());
        }
        //Return the View now that it has all elements attached to it
        return convertView;

    }

    /**
     * A count of the number of pieces involved in this view
     *
     * @return the number of pieces
     */
    public int getCount() {
        return pieces.length;
    }

    /**
     * This method does nothing, it has to be here because we extended BaseAdapter
     */
    public Object getItem(int position) {
        return null;
    }

    /**
     * This method does nothing, it has to be here because we extended BaseAdapter
     */
    public long getItemId(int position) {
        return 0;
    }
}