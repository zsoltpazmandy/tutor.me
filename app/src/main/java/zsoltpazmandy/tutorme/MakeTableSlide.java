package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MakeTableSlide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_table_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView topHint = (TextView) findViewById(R.id.tableSlideTag);

        setUpTable();
    }

    public void setUpTable(){
        TextView row1col1 = (TextView) findViewById(R.id.tableSlideRow1Col1);
        row1col1.setMaxWidth(row1col1.getWidth());

        TextView row1col2 = (TextView) findViewById(R.id.tableSlideRow1Col2);
        row1col2.setMaxWidth(row1col2.getWidth());

        TextView row2col1 = (TextView) findViewById(R.id.tableSlideRow2Col1);
        row2col1.setMaxWidth(row2col1.getWidth());

        TextView row2col2 = (TextView) findViewById(R.id.tableSlideRow2Col2);
        row2col2.setMaxWidth(row2col2.getWidth());

        TextView row3col1 = (TextView) findViewById(R.id.tableSlideRow3Col1);
        row3col1.setMaxWidth(row3col1.getWidth());

        TextView row3col2 = (TextView) findViewById(R.id.tableSlideRow3Col2);
        row3col2.setMaxWidth(row3col2.getWidth());

        TextView row4col1 = (TextView) findViewById(R.id.tableSlideRow4Col1);
        row4col1.setMaxWidth(row4col1.getWidth());

        TextView row4col2 = (TextView) findViewById(R.id.tableSlideRow4Col2);
        row4col2.setMaxWidth(row4col2.getWidth());

        TextView row5col1 = (TextView) findViewById(R.id.tableSlideRow5Col1);
        row5col1.setMaxWidth(row5col1.getWidth());

        TextView row5col2 = (TextView) findViewById(R.id.tableSlideRow5Col2);
        row5col2.setMaxWidth(row5col2.getWidth());

        TextView row6col1 = (TextView) findViewById(R.id.tableSlideRow6Col1);
        row6col1.setMaxWidth(row6col1.getWidth());

        TextView row6col2 = (TextView) findViewById(R.id.tableSlideRow6Col2);
        row6col2.setMaxWidth(row6col2.getWidth());

        TextView row7col1 = (TextView) findViewById(R.id.tableSlideRow7Col1);
        row7col1.setMaxWidth(row7col1.getWidth());

        TextView row7col2 = (TextView) findViewById(R.id.tableSlideRow7Col2);
        row7col2.setMaxWidth(row7col2.getWidth());

        TextView row8col1 = (TextView) findViewById(R.id.tableSlideRow8Col1);
        row8col1.setMaxWidth(row8col1.getWidth());

        TextView row8col2 = (TextView) findViewById(R.id.tableSlideRow8Col2);
        row8col2.setMaxWidth(row8col2.getWidth());

        TextView row9col1 = (TextView) findViewById(R.id.tableSlideRow9Col1);
        row9col1.setMaxWidth(row9col1.getWidth());

        TextView row9col2 = (TextView) findViewById(R.id.tableSlideRow9Col2);
        row9col2.setMaxWidth(row9col2.getWidth());

        TextView row10col1 = (TextView) findViewById(R.id.tableSlideRow10Col1);
        row10col1.setMaxWidth(row10col1.getWidth());

        TextView row10col2 = (TextView) findViewById(R.id.tableSlideRow10Col2);
        row10col2.setMaxWidth(row10col2.getWidth());
    }

}
