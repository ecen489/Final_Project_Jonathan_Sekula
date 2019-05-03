package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Bio extends Fragment {

    TextView bio;
    ImageButton camera;

    FirebaseDatabase database;
    DatabaseReference root_reference;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    String player;

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

        database = FirebaseDatabase.getInstance();
        root_reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getActivity().getIntent();
        player = intent.getStringExtra("player");

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VideoActivity.class);
                intent.putExtra("player", player);
                startActivity(intent);
            }
        });

        ReadDatabase();

    }

    public void ReadDatabase() {
        user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference player_reference = root_reference.child(player + "/Bio/");
            player_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String bio_string = "";
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        bio_string = bio_string + snapshot.getValue() + "\n";
                    }
                    bio.setText(bio_string);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
