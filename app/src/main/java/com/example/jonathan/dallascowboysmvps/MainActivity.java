package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        players = findViewById(R.id.players);
        players.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                Bundle bundle = new Bundle();

                switch (i) {
                    case 0:
                        intent.putExtra("player", "Troy Aikman");
                        intent.putExtra("position", "QB");
                        bundle.putString("player", "Troy Aikman");
                        break;
                    case 1:
                        intent.putExtra("player", "Tony Romo");
                        intent.putExtra("position", "QB");
                        break;
                    case 2:
                        intent.putExtra("player", "Roger Staubach");
                        intent.putExtra("position", "QB");
                        break;
                    case 3:
                        intent.putExtra("player", "Tony Dorsett");
                        intent.putExtra("position", "RB");
                        break;
                    case 4:
                        intent.putExtra("player", "Emmitt Smith");
                        intent.putExtra("position", "RB");
                        break;
                    case 5:
                        intent.putExtra("player", "Herschel Walker");
                        intent.putExtra("position", "RB");
                        break;
                    case 6:
                        intent.putExtra("player", "Dez Bryant");
                        intent.putExtra("position", "WR");
                        break;
                    case 7:
                        intent.putExtra("player", "Michael Irvin");
                        intent.putExtra("position", "WR");
                        break;
                    case 8:
                        intent.putExtra("player", "Terrell Owens");
                        intent.putExtra("position", "WR");
                        break;
                    case 9:
                        intent.putExtra("player", "Jason Witten");
                        intent.putExtra("position", "TE");
                        break;
                }

                Bio fragment = new Bio();
                fragment.setArguments(bundle);
                startActivity(intent);
            }
        });
    }

    public void LogoClick(View view) {

        String message = "";

        switch(view.getId()) {
            case R.id.helmet:
                message = "The Dallas Cowboys play in AT&T Stadium, aka Jerry World, in Arlington" +
                        "Texas.\nIt seats 80,000 fans (4th highest in NFL), and contains a screen" +
                        " that extends from 20-yard line to 20-yard line (29th largest in the world).";
                break;
            case R.id.football:
                message = "The Dallas Cowboys joined the NFL in 1960. They are currently the most" +
                        " valued team in the NFL and are known as America's Team.";
                break;
            case R.id.trophy:
                message = "The Dallas Cowboys have won 5 Super Bowls in the years 1972, 1978, " +
                        "1993, 1994, and 1996.";
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setTitle("Fun Facts");
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
