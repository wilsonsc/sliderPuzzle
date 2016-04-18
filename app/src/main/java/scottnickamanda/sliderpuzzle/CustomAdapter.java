
package scottnickamanda.sliderpuzzle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/***********************************************************************
 * Custom wrote adapter to allow Piece class files to be visually shown
 * in a grid layout.
 *
 * @author Scott Wilson
 **********************************************************************/

public class CustomAdapter extends BaseAdapter {

    /** context that will later be inflated */
    private Context context;

    /** the pieces that the adapter will display */
    private Piece[] pieces;

    /** Integer representing whether TextView is visible or not */
    private int visibleFlag = 1;

    /*******************************************************************
     * Default constructor
     * Saves parameters for future use
     *
     * @param context context resource from which this class was called
     * @param board the current game board
     ******************************************************************/
    public CustomAdapter(Context context, GameBoard board) {

        this.context = context;
        pieces = board.getPieces();
    }

    /*******************************************************************
     * Sets flag and updates view to change text visibility
     *
     * @param flag int representing whether text is visible or not
     ******************************************************************/
    public void updateView(int flag) {
        visibleFlag = flag;
        notifyDataSetChanged();
    }

    /*******************************************************************
     * Produces a displayable view for the display element that uses
     * this CustomAdapter
     *
     * @param pos the position of the piece
     * @param convertView a view that will be reused if present
     * @param parent the group in which this view belongs to
     * @return the created view
     ******************************************************************/
    public View getView(int pos, View convertView, ViewGroup parent) {

        //Inflates context
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Creates a new view if an existing view is not available
        if (convertView == null) {

            //Create a new view
            View customView = inflater.inflate(R.layout.pieces, null);

            //Adds TextView and ImageView with parameters defined in xml
            TextView tv = (TextView) customView.findViewById
                    (R.id.textView1);
            ImageView iv = (ImageView) customView.findViewById
                    (R.id.imageView1);

            //Sets the text of the TextView and image of the ImageView
            tv.setText(pieces[pos].toString());
            iv.setImageBitmap(pieces[pos].getImage());

            // if flag is 1, set text visible
            if (visibleFlag == 1) {
                tv.setVisibility(View.VISIBLE);
            }

            // if flag is 0, set text to invisible
            if (visibleFlag == 0) {
                tv.setVisibility(View.INVISIBLE);
            }

            //Return the View now that it has all elements attached
            return customView;
        }

        //Otherwise recycles existing view
        else {
            //Adds TextView and ImageView with parameters defined in xml
            TextView tv = (TextView) convertView.findViewById
                    (R.id.textView1);
            ImageView iv = (ImageView) convertView.findViewById
                    (R.id.imageView1);

            if (visibleFlag == 1) {
                tv.setVisibility(View.VISIBLE);
            }
            if (visibleFlag == 0) {
                tv.setVisibility(View.INVISIBLE);
            }

            //Sets the text of the TextView and image of the ImageView
            tv.setText(pieces[pos].toString());
            iv.setImageBitmap(pieces[pos].getImage());
        }
        //Return the View now that it has all elements attached to it
        return convertView;

    }

    /*******************************************************************
     * A count of the number of pieces involved in this view
     *
     * @return the number of pieces
     ******************************************************************/
    public int getCount() {
        return pieces.length;
    }

    /*******************************************************************
     * This method does nothing, it is needed because we extended
     * BaseAdapter
     ******************************************************************/
    public Object getItem(int position) {
        return null;
    }

    /*******************************************************************
     * This method does nothing, it is needed because we extended
     * BaseAdapter
     ******************************************************************/
    public long getItemId(int position) {
        return 0;
    }
}