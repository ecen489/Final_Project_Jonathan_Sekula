package com.example.jonathan.dallascowboysmvps;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.Manifest;

public class MainActivity extends AppCompatActivity {

    ListView players;
    ArrayList<String> players_list = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference root_reference;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    String player;
    String position;
    String text="\n";

    String currentPhotoPath;

    static final int CAMERA_REQUEST_CODE = 10;
    static final int REQUEST_TAKE_PHOTO = 1;

    Bitmap bitmap;

    String[] headings = {};
    String[] heading_qb = {"Year", "Team", "Cmp", "Att", "Cmp%", "Yds", "TD", "INT"};
    String[] heading_rb = {"Year", "Team", "Rush", "Yds", "YPA", "TD", "Rec", "Yds2", "YPR", "TD2"};
    String[] heading_wr = {"Year", "Team", "Tgt", "Rec", "Yds", "YPR", "TD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        root_reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            root_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        players_list.add(snapshot.getKey());
                    }
                    String[] players_array = new String[players_list.size()];
                    players_list.toArray(players_array);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, players_array) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            TextView textView = view.findViewById(android.R.id.text1);

                            textView.setTextColor(Color.BLACK);

                            return view;
                        }
                    };
                    players.setAdapter(arrayAdapter);
                    players_list.clear();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        players = findViewById(R.id.players);
        players.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                DatabaseReference player_reference = root_reference.child(players.getItemAtPosition(i).toString() + "/Position/");
                player_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                        intent.putExtra("player", players.getItemAtPosition(i).toString());
                        intent.putExtra("position", String.valueOf(dataSnapshot.getValue()));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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

    @TargetApi(Build.VERSION_CODES.M)
    public void AddNew(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Player Name");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Name");

        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        player = input.getText().toString();

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                        alertDialog2.setTitle("Player Position");
                        String[] positions = {"QB", "RB", "WR", "TE"};
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, positions);
                        final Spinner spinner = new Spinner(MainActivity.this);
                        spinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        spinner.setAdapter(arrayAdapter);
                        alertDialog2.setView(spinner);

                        alertDialog2.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        position = spinner.getSelectedItem().toString();
                                        root_reference.child(player).child("Position").setValue(position);
                                        dialog.cancel();

                                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                            dispatchTakePictureIntent();
                                        }
                                        else {
                                            String[] permissionRequested = {Manifest.permission.CAMERA};
                                            requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);
                                        }
                                    }
                                });

                        alertDialog2.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog2.show();
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider2",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            setPic();
            runTextRecognition();
        }
    }

    private void setPic() {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;
        bmOptions.inScaled = false;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    }

    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        recognizer.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            Log.d("tag", "new block");
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    Log.d("tag", elements.get(k).getText());
                    text = text + elements.get(k).getText() + "\n";
                }
            }
        }

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

        DatabaseReference stats_reference = root_reference.child(player).child("Stats");
        DatabaseReference child_reference = stats_reference.child("Year");

        int counter = 13;

        String[] lines = text.split(System.getProperty("line.separator"));
        for (int i = 1; i < lines.length; ++i) {
            int pos = ArrayPosition(headings, lines[i]);
            if (pos != -1) {
                if (counter < 13) {
                    while (counter < 13) {
                        child_reference.push().setValue("N/A");
                        ++counter;
                    }
                }
                child_reference = stats_reference.child(lines[i]);
                counter = 0;
            }
            else {
                child_reference.push().setValue(lines[i]);
                ++counter;
            }
        }

        if (counter < 13) {
            while (counter < 13) {
                child_reference.push().setValue("N/A");
                ++counter;
            }
        }

        text="\n";

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Notification")
                .setContentText("Database Insertion Successful")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.baseline_notification_important_black_18dp);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);

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
