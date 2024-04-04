import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class Song {
    private String songTitle;
    private String songArtist;
    private String songLength;
    private String filePath;

    public Song(String filePath) {
        this.filePath = filePath;

        try {
            //use jaudiotagger library to create an audio file obj to read mp3
            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            //read through the metadata of audio file
            Tag tag = audioFile.getTag();
            if (tag != null) {
                songTitle = tag.getFirst(FieldKey.TITLE);
                songArtist = tag.getFirst(FieldKey.ARTIST);
            } else {
                //could not read any information
                songTitle = "N/A";
                songArtist = "N/A";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getter
    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongLength() {
        return songLength;
    }

    public String getFilePath() {
        return filePath;
    }
}
