import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MusicPlayer extends PlaybackListener {
    //this will be used to update isPaused more synchronous
    private static final Object playSignal = new Object();

    //reference to updating the gui
    private MusicPlayerGUI musicPlayerGUI;

    private Song currentSong;
    public Song getCurrentSong() {return currentSong;}

    //use jlayer library to create an AdvancedPlayer obj to play music
    private AdvancedPlayer advancedPlayer;

    //pause boolean
    private boolean isPaused;

    //used for pausing and resuming
    private int currentFrame;
    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }

    //track how many milliseconds has passed since playing the song (for slider)
    private int currentTimeInMilli;
    public void setCurrentTimeInMilli(int timeInMilli) {
        currentTimeInMilli = timeInMilli;
    }

    //constructor
    public MusicPlayer(MusicPlayerGUI musicPlayerGUI) {
        this.musicPlayerGUI = musicPlayerGUI;
    }

    public void loadSong(Song song) {
        currentSong = song;

        //play the current song if not null
        if (currentSong != null) {
            playCurrentSong();
        }
    }

    public void pauseSong() {
        if (advancedPlayer != null) {
            isPaused = true;

            //stop the player
            stopSong();
        }
    }

    public void stopSong() {
        if (advancedPlayer != null) {
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    public void playCurrentSong() {
        if (currentSong == null) return;

        try {
            //read mp3 audio data
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            //create new AdvancedPlayer
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);

            //start music
            startMusicThread();

            //start playback slider thread
            startPlaybackSliderThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMusicThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isPaused) {
                        synchronized (playSignal) {
                            //update isPaused flag
                            isPaused = false;

                            //notify the other thread
                            playSignal.notify();
                        }

                        //resume music from last frame
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                    } else {
                        //play music from beginning
                        advancedPlayer.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //thread for handling slider update
    private void startPlaybackSliderThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isPaused) {
                    try {
                        synchronized (playSignal) {
                            playSignal.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //System.out.println("isPaused: " + isPaused);

                while (!isPaused) {
                    try {
                        //increment current time in milli
                        currentTimeInMilli++;

                        //calculate into frame value
                        int calculatedFrame = (int) ((double) currentTimeInMilli * 2.08 * currentSong.getFrameRatePerMilliseconds());

                        //update the gui
                        musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                        Thread.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        //this method gets called in the beginning of song
        System.out.println("Playback started");
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        //this method gets called when song is finished or closed
        System.out.println("Playback finished");

        //System.out.println("Stopped at " + evt.getFrame());

        if (isPaused) {
            currentFrame += (int) ((double) evt.getFrame() * currentSong.getFrameRatePerMilliseconds());
        }
    }
}
