package com.example.muzikcalar;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView songName;
    TextView total;
    ListView listview;

    ImageButton play;
    ImageButton previous;
    ImageButton next;

    int currentMusic = 0;

    ArrayList<String> Musiclist = new ArrayList<>();
    MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssignComponents();
        currentMusic = 0;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.Play: {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                } else {
                    if(mediaPlayer.getCurrentPosition() == 0){
                        mediaPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/" +  Musiclist.get(currentMusic)));
                        mediaPlayer.start();
                        total.setText(getDuration());
                        songName.setText(Musiclist.get(currentMusic));
                    }else{
                        mediaPlayer.start();
                    }
                    play.setImageResource(R.drawable.pause);
                }
                break;
            }
            case R.id.Previous:{
                currentMusic = (currentMusic == 0) ? (Musiclist.size() -1) : (currentMusic -1);
                StartSelectedMusic(currentMusic);
                break;
            }
            case R.id.Next: {
                currentMusic = (currentMusic == Musiclist.size() -1) ? 0 : (currentMusic+1);
                StartSelectedMusic(currentMusic);
                break;
            }
        }
    }
    public void StartSelectedMusic(int Index){

        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();

        mediaPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/" +  Musiclist.get(Index)));
        mediaPlayer.start();
        songName.setText(Musiclist.get(Index));
        total.setText(getDuration());
        play.setImageResource(R.drawable.pause);
    }
    public void AssignComponents() {

        listview = findViewById(R.id.listView);


        Field[] fields = R.raw.class.getFields();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Musiclist);

        for (Field field : fields) //Nude loop :{
            Musiclist.add(field.getName());

        listview.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        mediaPlayer = MediaPlayer.create(this, Uri.parse("android.resource://" + getPackageName() + "/raw/" +  Musiclist.get(0)));

        songName = findViewById(R.id.SongName);
        total = findViewById(R.id.Total);

        play = findViewById(R.id.Play);
        previous = findViewById(R.id.Previous);
        next = findViewById(R.id.Next);

        play.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                StartSelectedMusic(position);
            }});
    }

    public String getDuration(){
        int minitues;
        int diff;

        String min;
        String sec;

        minitues =  (mediaPlayer.getDuration() / 1000) / 60;
        diff =  (mediaPlayer.getDuration() / 1000) % 60;

        min = (minitues < 10) ? ("0" + String.valueOf(minitues)) : String.valueOf(minitues);
        sec = (diff < 10) ? ("0" + String.valueOf(diff)) : String.valueOf(diff);

        return min + ":" + sec;
    }
}