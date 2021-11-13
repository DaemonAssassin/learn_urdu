package beast.mateenmehmood.learnurdu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView numbers, colors, family, phrases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting appropriate TextView
        numbers = findViewById(R.id.numbers);
        colors = findViewById(R.id.colors);
        family = findViewById(R.id.family);
        phrases = findViewById(R.id.phrases);

        //setting onClickListener to NumbersActivity
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                //Create a new intent to open the {@link NumbersActivity}
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);
                //starting new activity
                startActivity(numbersIntent);
            }
        });
        //setting onClickListener to ColorsActivity
        colors.setOnClickListener(view -> {
            Intent colorsIntent = new Intent(getApplicationContext(), ColorsActivity.class);
            startActivity(colorsIntent);
        });
        //setting onClickListener to FamilyActivity
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent familyIntent = new Intent(getApplicationContext(), FamilyActivity.class);
                startActivity(familyIntent);
            }
        });
        //setting onClickListener to PhrasesActivity
        phrases.setOnClickListener(view -> {
            Intent phrasesIntent = new Intent(MainActivity.this, PhraseActivity.class);
            startActivity(phrasesIntent);
        });
    }
}