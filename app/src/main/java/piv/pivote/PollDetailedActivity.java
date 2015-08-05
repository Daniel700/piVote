package piv.pivote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Daniel on 02.08.2015.
 */
public class PollDetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Poll p = (Poll) getIntent().getSerializableExtra("Poll");
        Toast.makeText(getApplicationContext(), p.getQuestion(), Toast.LENGTH_LONG).show();

    }
}
