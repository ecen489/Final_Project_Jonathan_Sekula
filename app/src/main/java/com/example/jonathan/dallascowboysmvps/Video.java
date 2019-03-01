package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;


public class Video extends Fragment {

    VideoView videoView;

    public Video() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = view.findViewById(R.id.video_view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        String player = intent.getStringExtra("player");

        String fileName = "";

        switch (player) {
            case "Troy Aikman":
                fileName = "troy_aikman_highlights";
                break;
            case "Tony Romo":
                fileName = "tony_romo_highlights";
                break;
            case "Roger Staubach":
                fileName = "roger_staubach_highlights";
                break;
            case "Tony Dorsett":
                fileName = "tony_dorsett_highlights";
                break;
            case "Emmitt Smith":
                fileName = "emmitt_smith_highlights";
                break;
            case "Herschel Walker":
                fileName = "herschel_walker_highlights";
                break;
            case "Dez Bryant":
                fileName = "dez_bryant_highlights";
                break;
            case "Michael Irvin":
                fileName = "michael_irvin_highlights";
                break;
            case "Terrell Owens":
                fileName = "terrell_owens_highlights";
                break;
            case "Jason Witten":
                fileName = "jason_witten_highlights";
            default:
                break;
        }

        String filePath = "android.resource://" + getActivity().getPackageName() + "/raw/" + fileName;

        videoView.setVideoURI(Uri.parse(filePath));

        videoView.setMediaController(new MediaController(getContext()));

        int pos = 0;
        if (savedInstanceState != null)
            pos = savedInstanceState.getInt("pos");

        videoView.seekTo(pos);
        videoView.start();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView.isPlaying())
            outState.putInt("pos", videoView.getCurrentPosition());
    }

}
