
package scottnickamanda.sliderpuzzle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


public class CustomAdapter extends BaseAdapter {

    private Context context;
    private String[] number;
    //private int[] imageIds;

    CustomAdapter(Context context, String[] number,int[] imageIds) {
        this.context = context;
        this.number = number;
        //this.imageIds = imageIds;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        GridView grid = (GridView)parent;

        int size = grid.getRequestedColumnWidth();

        TextView text = new TextView(context);
        text.setLayoutParams(new GridView.LayoutParams(size, size));


        return text;
    }
    @Override
    public int getCount() {
        return number.length;
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

