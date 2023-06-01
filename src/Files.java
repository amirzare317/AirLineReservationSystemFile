import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Files {
    protected RandomAccessFile rfileFlights;
    protected RandomAccessFile rfileUsers;
    protected RandomAccessFile rfileTickets;

    {
        try {
            rfileFlights = new RandomAccessFile("Flights.dat", "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }
    private final int SIZE = 15;
    protected String fixToWrite(String str){
        while (str.length() < SIZE) {
            str += " ";
        }
        return str.substring(0, SIZE);
    }

    protected String fixToRead(RandomAccessFile randomAccessFile) throws IOException {
        String str = "";
        for (int i = 0; i < SIZE; i++) {
            str += randomAccessFile.readChar();
        }
        return str.trim();
    }
}

