import java.io.IOException;
import java.io.RandomAccessFile;

public class User {
    private String userName;
    private String password;
    private int charge;
    private String bookedTickets;

    Passenger userInfo;
    Admin admin;

    public void write(RandomAccessFile rfile, String userName, String password) throws IOException {
        rfile.writeChars(admin.fixToWrite(userName));
        rfile.writeChars(admin.fixToWrite(password));
    }

    public String getBookedTickets() {
        return bookedTickets;
    }

    public void setBookedTickets(String bookedTickets) {
        this.bookedTickets = bookedTickets;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Check if the user is admin or not.
     * @param userName is the username of the user.
     * @param password is the password of the user.
     * @return If userName and password both equal to admin it returns true, otherwise it returns false.
     */
    public boolean isAdmin(String userName, String password) {

        if (userName.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
