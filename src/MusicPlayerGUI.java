import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class MusicPlayerGUI extends JFrame {
    //color variable
    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private MusicPlayer musicPlayer;

    //allow user to use file explorer
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;

    public MusicPlayerGUI() {
        //add title
        super("Music Player");

        //set size of window
        setSize(400, 600);

        //exit program on close
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //centered window
        setLocationRelativeTo(null);

        //set unresizable window
        setResizable(false);

        setLayout(null);

        //change frame color
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayer(this);
        jFileChooser = new JFileChooser();

        //set default path
        jFileChooser.setCurrentDirectory(new File("src/assets"));

        //filter file to only see mp3 file
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        //load record image
        JLabel songImage = new JLabel(loadImage("src/assets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        //song title
        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        //song artist
        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 20));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        //playback slider
        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth() / 2 - 300 / 2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //when user hold the tick, pause the song
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //when the user drop the tick
                JSlider source = (JSlider) e.getSource();

                //get the frame value from where the user wants to playback to
                int frame = source.getValue();

                //update the current frame
                musicPlayer.setCurrentFrame(frame);

                //update current time in milli
                musicPlayer.setCurrentTimeInMilli((int) (frame / (2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));

                //resume the song
                musicPlayer.playCurrentSong();

                //toggle on pause button and toggle off play button
                enablePauseButtonDisablePlayButton();
            }
        });
        add(playbackSlider);

        //add playback button (next, previous, play)
        addPlaybackBtns();
    }

    private void addToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setBounds(0, 0, getWidth(), 20);

        toolbar.setFloatable(false);

        //dropdown menu
        JMenuBar menuBar = new JMenuBar();
        toolbar.add(menuBar);

        //song menu
        JMenu songMenu = new JMenu("Song");
        menuBar.add(songMenu);

        //load song menu item in song dropdown
        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null) {
                    //create a song obj based on selected file
                    Song song = new Song(selectedFile.getPath());

                    //load song in music player
                    musicPlayer.loadSong(song);

                    //update song title and artist
                    updateSongTitleAndArtist(song);

                    //update playback slider
                    updatePlaybackSlider(song);

                    //toggle on pause button and toggle off play button
                    enablePauseButtonDisablePlayButton();
                }
            }
        });
        songMenu.add(loadSong);

        //playlist menu
        JMenu playlistMenu = new JMenu("Playlist");
        menuBar.add(playlistMenu);

        //adding create playlist menu item in playlist dropdown
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        playlistMenu.add(createPlaylist);

        //create load playlist menu item in playlist dropdown
        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        playlistMenu.add(loadPlaylist);

        add(toolbar);
    }

    private void addPlaybackBtns() {
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth() - 10, 80);
        playbackBtns.setBackground(null);

        //previous button
        JButton prevButton = new JButton(loadImage("src/assets/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        playbackBtns.add(prevButton);

        //play button
        JButton playButton = new JButton(loadImage("src/assets/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //toggle off play button and toggle on pause button
                enablePauseButtonDisablePlayButton();

                //play or resume song
                musicPlayer.playCurrentSong();
            }
        });
        playbackBtns.add(playButton);

        //pause button
        JButton pauseButton = new JButton(loadImage("src/assets/pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //toggle off pause button and toggle on play button
                enablePlayButtonDisablePauseButton();

                //pause the song
                musicPlayer.pauseSong();
            }
        });
        playbackBtns.add(pauseButton);

        //next button
        JButton nextButton = new JButton(loadImage("src/assets/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        playbackBtns.add(nextButton);

        add(playbackBtns);
    }

    //update slider value
    public void setPlaybackSliderValue(int frame) {
        playbackSlider.setValue(frame);
    }

    private void updateSongTitleAndArtist(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    private void updatePlaybackSlider(Song song) {
        //update max count for slider
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        //create song length label
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        //beginning will be 00:00
        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
        labelBeginning.setForeground(Color.WHITE);

        //end will depend on song length
        JLabel labelEnd = new JLabel(song.getSongLength());
        labelEnd.setFont(new Font("Dialog", Font.BOLD, 18));
        labelEnd.setForeground(Color.WHITE);

        labelTable.put(0, labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    private void enablePauseButtonDisablePlayButton() {
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        //turn off play button
        playButton.setVisible(false);
        playButton.setEnabled(false);

        //turn on pause button
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }
    private void enablePlayButtonDisablePauseButton() {
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        //turn on play button
        playButton.setVisible(true);
        playButton.setEnabled(true);

        //turn off pause button
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }


    private ImageIcon loadImage(String imagePath) {
        try {
            //read the image file from the given path
            BufferedImage image = ImageIO.read(new File(imagePath));

            //returns an image icon
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //could not find resource
        return null;
    }
}
