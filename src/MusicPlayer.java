import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MusicPlayer {
    private Song currentSong;

    //use jlayer library to create an AdvancedPlayer obj to play music
    private AdvancedPlayer advancedPlayer;

    //constructor
    public MusicPlayer() {

    }

    public void loadSong(Song song) {
        currentSong = song;

        //play the current song if not null
        if (currentSong != null) {
            playCurrentSong();
        }
    }

    public void playCurrentSong() {
        try {
            //read mp3 audio data
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            //create new AdvancedPlayer
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);

            //start music
            startMusicThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMusicThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //play music
                    advancedPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
