package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatsActivity extends AppCompatActivity {

    ImageView headshot;
    GridLayout grid;

    String player;
    String position;

    String[] headings = {};
    String[] heading_qb = {"Year", "Team", "Cmp", "Att", "Cmp%", "Yds", "TD", "INT"};
    String[] heading_rb = {"Year", "Team", "Rush", "Yds", "YPA", "TD", "Rec", "Yds2", "YPR", "TD2"};
    String[] heading_wr = {"Year", "Team", "Tgt", "Rec", "Yds", "YPR", "TD"};

    FirebaseDatabase database;
    DatabaseReference root_reference;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        headshot = findViewById(R.id.headshot);
        grid = findViewById(R.id.grid);

        Intent intent = getIntent();
        player = intent.getStringExtra("player");
        position = intent.getStringExtra("position");

        database = FirebaseDatabase.getInstance();
        root_reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        switch(player) {
            case "Troy Aikman":
                headshot.setBackgroundResource(R.drawable.troy_aikman);
                ReadDatabase();
                break;

            case "Tony Romo":
                headshot.setBackgroundResource(R.drawable.tony_romo);
                ReadDatabase();
                break;

            case "Roger Staubach":
                headshot.setBackgroundResource(R.drawable.roger_staubach);
                ReadDatabase();
                break;

            case "Tony Dorsett":
                headshot.setBackgroundResource(R.drawable.tony_dorsett);
                ReadDatabase();
                break;

            case "Emmit Smith":
                headshot.setBackgroundResource(R.drawable.emitt_smith);
                ReadDatabase();
                break;

            case "Herschel Walker":
                headshot.setBackgroundResource(R.drawable.herschel_walker);
                ReadDatabase();
                break;

            case "Dez Bryant":
                headshot.setBackgroundResource(R.drawable.dez_bryant);
                ReadDatabase();
                break;

            case "Michael Irvin":
                headshot.setBackgroundResource(R.drawable.michael_irvin);
                ReadDatabase();
                break;

            case "Terrell Owens":
                headshot.setBackgroundResource(R.drawable.terrell_owens);
                ReadDatabase();
                break;

            case "Jason Witten":
                headshot.setBackgroundResource(R.drawable.jason_witten);
                ReadDatabase();
                break;
            default:
                headshot.setBackgroundResource(R.drawable.photo_unavailable);
                ReadDatabase();
                break;
        }
    }

    public void ReadDatabase() {
        user = mAuth.getCurrentUser();

        if (user != null) {
            DatabaseReference player_reference = root_reference.child(player + "/Stats/");
            switch(position) {
                case "QB":
                    headings = heading_qb;
                    break;
                case "RB":
                    headings = heading_rb;
                    break;
                case "WR":
                    headings = heading_wr;
                    break;
                case "TE":
                    headings = heading_wr;
                    break;
            }

            grid.setColumnCount(headings.length);
            grid.setOrientation(GridLayout.VERTICAL);

            for (int i = 0; i < headings.length; ++i) {
                DatabaseReference stat_reference = player_reference.child(headings[i] + "/");
                stat_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        grid.setRowCount((int) dataSnapshot.getChildrenCount()+1);

                        int heading_position = ArrayPosition(headings, String.valueOf(dataSnapshot.getKey()));
                        TextView stat = new TextView(getApplicationContext());
                        stat.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        stat.setText(headings[heading_position]);
                        stat.setTextColor(Color.parseColor("#000000"));
                        stat.setPadding(14, 14, 14, 14);
                        stat.setTextSize(13);
                        stat.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        stat.setGravity(Gravity.CENTER);
                        grid.addView(stat);


                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            stat = new TextView(getApplicationContext());
                            stat.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            stat.setText(String.valueOf(snapshot.getValue()));
                            stat.setTextColor(Color.parseColor("#000000"));
                            stat.setPadding(14, 14, 14, 14);
                            stat.setTextSize(13);
                            stat.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                            stat.setGravity(Gravity.CENTER);
                            grid.addView(stat);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
        else {
            Toast.makeText(this, "Please Login...", Toast.LENGTH_SHORT).show();
        }
    }

    public int ArrayPosition(String[] array, String string) {
        int pos = -1;
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(string))
                pos = i;
        }

        return pos;
    }

}
