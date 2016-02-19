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

public class MainActivity extends AppCompatActivity {
    static int currentSize;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        ArrayAdapter<CharSequence> gameSizes = ArrayAdapter.createFromResource(this,
                    R.array.possibleGameSizes, android.R.layout.simple_spinner_item);

        String[] sizes = new String[] {"3x3", "4x4", "5x5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,
                    android.R.layout.simple_spinner_item, sizes);
        Spinner gameSize = (Spinner) findViewById(R.id.selectSize);
        gameSize.setAdapter(adapter);
        int currentSize = 3;
        gameSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSize(position+3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button launchGame = (Button) findViewById(R.id.newGame);
        launchGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("boardSize",getSize());
                startActivity(intent);

            }
        });}

        static int getSize() {
            return currentSize;
        }
        static void setSize(int size) {
        currentSize = size;
        }

}



