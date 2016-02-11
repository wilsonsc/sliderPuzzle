
package scottnickamanda.sliderpuzzle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends BaseAdapter {

    private Context context;
    private Piece[] pieces;

    public CustomAdapter(Context context, Piece[] pieces) {
        this.context = context;
        this.pieces = pieces;

    }

    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.pieces, null);
        TextView tv = (TextView) customView.findViewById(R.id.textView1);
        ImageView iv = (ImageView) customView.findViewById(R.id.imageView1);

        tv.setText(pieces[pos].toString());
        iv.setImageBitmap(pieces[pos].getImage());
        //iv.setScaleType(ImageView.ScaleType.FIT_END);

        //int size = grid.getRequestedColumnWidth();

        //TextView text = new TextView(context);


        return customView;
    }
    @Override
    public int getCount() {
        return pieces.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
