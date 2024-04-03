import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MusicPlayerGUI extends JFrame {
    //color variable
    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

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

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        //load record image
        JLabel songImage = new JLabel(loadImage("src/assets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        //song title
        JLabel songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        //song artist
        JLabel songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 20));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);
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
