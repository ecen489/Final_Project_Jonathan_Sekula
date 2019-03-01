package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    ImageView headshot;
    GridLayout grid;

    String player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        headshot = findViewById(R.id.headshot);
        grid = findViewById(R.id.grid);

        Intent intent = getIntent();
        player = intent.getStringExtra("player");

        String text;

        switch(player) {
            case "Troy Aikman":
                headshot.setBackgroundResource(R.drawable.troy_aikman);

                text = ReadTxtFile(R.raw.troy_aikman_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Tony Romo":
                headshot.setBackgroundResource(R.drawable.tony_romo);

                text = ReadTxtFile(R.raw.tony_romo_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Roger Staubach":
                headshot.setBackgroundResource(R.drawable.roger_staubach);

                text = ReadTxtFile(R.raw.roger_staubach_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Tony Dorsett":
                headshot.setBackgroundResource(R.drawable.tony_dorsett);

                text = ReadTxtFile(R.raw.tony_dorsett_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Emmitt Smith":
                headshot.setBackgroundResource(R.drawable.emitt_smith);

                text = ReadTxtFile(R.raw.emmitt_smith_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Herschel Walker":
                headshot.setBackgroundResource(R.drawable.herschel_walker);

                text = ReadTxtFile(R.raw.herschel_walker_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Dez Bryant":
                headshot.setBackgroundResource(R.drawable.dez_bryant);

                text = ReadTxtFile(R.raw.dez_bryant_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Michael Irvin":
                headshot.setBackgroundResource(R.drawable.michael_irvin);

                text = ReadTxtFile(R.raw.michael_irvin_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Terrell Owens":
                headshot.setBackgroundResource(R.drawable.terrell_owens);

                text = ReadTxtFile(R.raw.terrell_owens_stats);
                CreateGrid(text, GetHeadings(text));
                break;

            case "Jason Witten":
                headshot.setBackgroundResource(R.drawable.jason_witten);

                text = ReadTxtFile(R.raw.jason_witten_stats);
                CreateGrid(text, GetHeadings(text));
                break;
            default:
                break;
        }
    }

    public String ReadTxtFile(int id) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(id)));
        String text = "";

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                text = text + line + "\n";
            }
        } catch(IOException e) {return null;}

        return text;

    }

    public ArrayList<String> GetHeadings(String text) {

        BufferedReader reader = new BufferedReader(new StringReader(text));
        ArrayList<String> headings = new ArrayList<>();

        try {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains("->")) {
                    headings.add(line.substring(2, line.length()));
                }

            }
        } catch(IOException e) {return null;}

        return headings;
    }

    public void CreateGrid(String text, ArrayList<String> headings) {

        BufferedReader reader = new BufferedReader(new StringReader(text));

        ArrayList<ArrayList<String>> stats = new ArrayList<>();
        ArrayList<String> column;
        String line;

        try {
            reader.readLine();
            for (int i = 0; i < headings.size(); ++i) {
                column = new ArrayList<>();
                column.add(headings.get(i));

                while ((line = reader.readLine()) != null) {

                    if (i < headings.size() - 1) {
                        if (line.contains(headings.get(i + 1))) break;
                        else column.add(line);
                    }
                    else column.add(line);
                }

                stats.add(column);
            }
        } catch (IOException e) {return;}

        grid.setColumnCount(stats.size());
        grid.setRowCount(stats.get(0).size());

        ArrayList<ArrayList<String>> stats_transpose = Transpose(stats);

        for (int i = 0; i < stats_transpose.size(); ++i) {
            for (int j = 0; j < stats_transpose.get(0).size(); ++j) {
                TextView stat = new TextView(this);
                stat.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                stat.setText(stats_transpose.get(i).get(j));
                stat.setPadding(14, 14, 14, 14);
                stat.setTextSize(13);
                stat.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                stat.setGravity(Gravity.CENTER);
                grid.addView(stat);
            }
        }

    }

    public ArrayList<ArrayList<String>> Transpose(ArrayList<ArrayList<String>> stats) {
        ArrayList<ArrayList<String>> transpose = new ArrayList<>();
        ArrayList<String> column;

        for (int i = 0; i < stats.get(0).size(); ++i) {
            column = new ArrayList<>();
            for (int j = 0; j < stats.size(); ++j) {
                column.add(stats.get(j).get(i));
            }
            transpose.add(column);
        }

        return transpose;
    }

    public void VideoClick(View view) {
        Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
        intent.putExtra("player", player);
        startActivity(intent);
    }
}
