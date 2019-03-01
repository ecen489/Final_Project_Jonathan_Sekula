package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Bio extends Fragment {

    TextView bio;
    ImageButton camera;

    public Bio() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bio, container, false);
        bio = view.findViewById(R.id.bio);
        camera = view.findViewById(R.id.camera);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        Intent intent = getActivity().getIntent();
        final String player = intent.getStringExtra("player");

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VideoActivity.class);
                intent.putExtra("player", player);
                startActivity(intent);
            }
        });

        switch(player) {
            case "Troy Aikman":
                bio.setText(ReadTxtFile(R.raw.troy_aikman_bio));
                break;

            case "Tony Romo":
                bio.setText(ReadTxtFile(R.raw.tony_romo_bio));
                break;

            case "Roger Staubach":
                bio.setText(ReadTxtFile(R.raw.roger_staubach_bio));
                break;

            case "Tony Dorsett":
                bio.setText(ReadTxtFile(R.raw.tony_dorsett_bio));
                break;

            case "Emmitt Smith":
                bio.setText(ReadTxtFile(R.raw.emmitt_smith_bio));
                break;

            case "Herschel Walker":
                bio.setText(ReadTxtFile(R.raw.herschel_walker_bio));
                break;

            case "Dez Bryant":
                bio.setText(ReadTxtFile(R.raw.dez_bryant_bio));
                break;

            case "Michael Irvin":
                bio.setText(ReadTxtFile(R.raw.michael_irvin_bio));
                break;

            case "Terrell Owens":
                bio.setText(ReadTxtFile(R.raw.terrell_owens_bio));
                break;

            case "Jason Witten":
                bio.setText(ReadTxtFile(R.raw.jason_witten_bio));
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

}
