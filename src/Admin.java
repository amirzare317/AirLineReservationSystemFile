import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Scanner;

public class Admin extends Files {
    Scanner input = new Scanner(System.in);
    String string = "";
    String str = "";
    int count = 0;

    /*
     * Since we just need to execute table of flights once, this function is called in constructor.
     */
    public Admin() throws IOException {
        tableOfFlights();
    }

    /**
     * Show the Menu of Admin.
     */
    public void showAdminMenu(){
        System.out.println("===========================");
        System.out.println("    Admin MENU OPTIONS");
        System.out.println("===========================");
        System.out.println(". . . . . . . . . . . . . .");
        System.out.println("    <1> Add");
        System.out.println("    <2> Update");
        System.out.println("    <3> Remove");
        System.out.println("    <4> Flight schedules");
        System.out.println("    <0> Sign out");
    }

    /**
     * Just to show the admin menu after each action.
     */
    public void showAdminMenuAgain() throws IOException {
        System.out.println("Press any key to continue...");
        string = input.next();
        System.out.print("\033[H\033[2J");
        showAdminMenu();
    }

    /**
     * When the menu showed, by entering any button which is designed here, the program will start to run.
     */
    public void options() throws IOException {
        int number = 10;
        while (number != 0) {
            number = input.nextInt();
            switch (number) {
                case 1:
                    System.out.println("Adding...");
                    add();
                    showAdminMenuAgain();
                    break;
                case 2:
                    System.out.println("Updating...");
                    System.out.println("Enter the flight ID:");
                    string = input.next();
                    update(string);
                    showAdminMenuAgain();
                    break;
                case 3:
                    System.out.println("Deleting...");
                    System.out.println("Enter the flight ID:");
                    string = input.next();
                    delete(string);
                    showAdminMenuAgain();
                    break;
                case 4:
                    showTable();
                    showAdminMenuAgain();
                    break;
                case 0:
                    break;

                default:
                    System.out.println("Wrong input!!!");
                    break;
            }
        }
    }

    // Array of flights. In this airline you can have only 15 flights.
    public FlightInfo[] flights = new FlightInfo[15];

    /**
     * In this method our default flights are initialized.
     */
    public void tableOfFlights() throws IOException {

//        rfileFlights.setLength(0);
//        Note that the first boolean value is for search and the second boolean value is for allowing to canceling or updating for admin.
//         Info of 1st flight
//        rfileFlights.writeChars(fixToWrite("WX-12"));
//        rfileFlights.writeChars(fixToWrite("Yazd"));
//        rfileFlights.writeChars(fixToWrite("Tehran"));
//        rfileFlights.writeChars(fixToWrite("1401-12-10"));
//        rfileFlights.writeChars(fixToWrite("12:30"));
//        rfileFlights.writeInt(700_000);
//        rfileFlights.writeInt(51);
//        rfileFlights.writeBoolean(false); // 1byte
//        rfileFlights.writeBoolean(false);
//
//        // Info of 2nd flight
//        rfileFlights.writeChars(fixToWrite("WZ-15"));
//        rfileFlights.writeChars(fixToWrite("Mashhad"));
//        rfileFlights.writeChars(fixToWrite("Ahvaz"));
//        rfileFlights.writeChars(fixToWrite("1401-12-11"));
//        rfileFlights.writeChars(fixToWrite("08:00"));
//        rfileFlights.writeInt(900_000);
//        rfileFlights.writeInt(245);
//        rfileFlights.writeBoolean(false); // 1byte
//        rfileFlights.writeBoolean(false);
//
//        // Info of 3rd flight
//        rfileFlights.writeChars(fixToWrite("BG-22")); //30
//        rfileFlights.writeChars(fixToWrite("Shiraz")); //30
//        rfileFlights.writeChars(fixToWrite("Tabriz")); //30
//        rfileFlights.writeChars(fixToWrite("1401-12-12")); //30
//        rfileFlights.writeChars(fixToWrite("22:30")); //30
//        rfileFlights.writeInt(1_100_000); //4
//        rfileFlights.writeInt(12); //4
//        rfileFlights.writeBoolean(false); // 1byte
//        rfileFlights.writeBoolean(false);

    }


    /**
     * The details of all fights will be printed by calling this method.
     */
    public void showTable() throws IOException {
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s\n", "FlightId", "Origin", "Destination", "Date", "Time", "Price", "Seats");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15d %-15d\n", fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), rfileFlights.readInt(), rfileFlights.readInt());
            rfileFlights.readBoolean();
            rfileFlights.readBoolean();
            System.out.println(".....................................................................................................");
            count++;
        }
    }


    /**
     * Admin is able to add a new flight.
     * Hint: Admin is allowed to add flights up to 15.
     */
    public void add() throws IOException {

        count = (int) (rfileFlights.length() / 160);
        rfileFlights.seek(count * 160L);
        System.out.println("Enter FlightID:");
        rfileFlights.writeChars(fixToWrite(input.next()));

        System.out.println("Enter Origin:");
        rfileFlights.writeChars(fixToWrite(input.next()));

        System.out.println("Enter Destination:");
        rfileFlights.writeChars(fixToWrite(input.next()));

        System.out.println("Enter Date:");
        rfileFlights.writeChars(fixToWrite(input.next()));

        System.out.println("Enter Time:");
        rfileFlights.writeChars(fixToWrite(input.next()));

        System.out.println("Enter Price:");
        rfileFlights.writeInt(input.nextInt());

        System.out.println("Enter Free Seats:");
        rfileFlights.writeInt(input.nextInt());

        rfileFlights.writeBoolean(false);
        rfileFlights.writeBoolean(false);
        count++;
    }

    /**
     * Admin is able to delete the intended flight.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     *               Then, the value of that string will be null, so that it can't be recognized by search engines.
     */
    public void delete(String string) throws IOException {
        int countArray2 = 0;
        int flag = 0;
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek(countArray2 * 160L);
            if (fixToRead(rfileFlights).equals(string)) {
                int index = 0;
                index = (int) ((rfileFlights.getFilePointer() - 30) / 160);
                int len = (int) (rfileFlights.length() / 160);
                rfileFlights.seek((countArray2 + 1) * 160L - 2);

                if (rfileFlights.readBoolean()) {
                    System.out.println("Some people has registered this flight.\nYou can't delete this flight!");
                    flag = 1;
                    break;
                }
                shiftArray(index, len);
                rfileFlights.setLength((len - 1) * 160L);
                flag = 1;
                //todo

                System.out.println("Flight deleted successfully");
            }
            countArray2++;
        }
        if (flag == 0) {
            System.out.println("This flight ID is not valid");
        }
    }

    public void shiftArray(int index, int len) throws IOException {
        String flightID = "";
        String origin = "";
        String destination = "";
        String date = "";
        String time = "";
        int price;
        int seats;
        boolean state1 = false;
        boolean state2 = false;
        int fixedIndex = index;
        for (int i = 0; i < (len - fixedIndex) - 1; i++) {
            rfileFlights.seek((index + 1) * 160L);
            flightID = fixToRead(rfileFlights); //30

            origin = fixToRead(rfileFlights); //30

            destination = fixToRead(rfileFlights);//30

            date = fixToRead(rfileFlights);//30

            time = fixToRead(rfileFlights);//30

            price = rfileFlights.readInt();//4

            seats = rfileFlights.readInt();//4

            state1 = rfileFlights.readBoolean();//1

            state2 = rfileFlights.readBoolean();//1

            rfileFlights.seek((index) * 160L);
            rfileFlights.writeChars(fixToWrite(flightID));

            rfileFlights.writeChars(fixToWrite(origin));

            rfileFlights.writeChars(fixToWrite(destination));

            rfileFlights.writeChars(fixToWrite(date));

            rfileFlights.writeChars(fixToWrite(time));

            rfileFlights.writeInt(price);

            rfileFlights.writeInt(seats);

            rfileFlights.writeBoolean(state1);

            rfileFlights.writeBoolean(state2);
            index++;
        }


    }


    /**
     * Admin is able to update any feature of flights except FlightID which is unique.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     *               So that you have accesses to any details of intended flight.
     */
    public void update(String string) throws IOException {
        int flag = 0;
        int countArray = 0;
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek(countArray * 160L);
            if (fixToRead(rfileFlights).equals(string)) {
                flag = 1;
                rfileFlights.seek((countArray + 1) * 160L - 2);
                if (rfileFlights.readBoolean()) {
                    System.out.println("Some people has registered this flight.\nYou can't delete this flight!");
                    break;
                }

                updateOrigin();

                updateDestination();

                updateDate();

                updateTime();

                updatePrice();

                updateSeats();
                System.out.println("Flight updated successfully");
            }
            countArray++;
        }
        if (flag == 0) {
            System.out.println("This flight ID is not valid");
        }
    }

    /**
     * Update the seats of each flight.
     */
    private void updateSeats() throws IOException {
        System.out.println("Change Seats (Press N to escape)");
        str = input.next();
        if (!str.equalsIgnoreCase("N")) {
            rfileFlights.seek(rfileFlights.getFilePointer() + 98);
            rfileFlights.writeChars(fixToWrite(str));
        }
    }

    /**
     * Update the price of each flight.
     */
    private void updatePrice() throws IOException {
        System.out.println("Change Price (Press N to escape)");
        str = input.next();
        if (!str.equalsIgnoreCase("N")) {
            rfileFlights.seek(rfileFlights.getFilePointer() + 94);
            rfileFlights.writeChars(fixToWrite(str));
        }
    }

    /**
     * Update the time of each flight.
     */
    private void updateTime() throws IOException {
        System.out.println("Change Time (Press N to escape)");
        str = input.next();
        if (!str.equalsIgnoreCase("N")) {
            rfileFlights.seek(rfileFlights.getFilePointer() + 90);
            rfileFlights.writeChars(fixToWrite(str));
        }
    }

    /**
     * Update the date of each flight.
     */
    private void updateDate() throws IOException {
        System.out.println("Change Date (Press N to escape)");
        str = input.next();
        if (!str.equalsIgnoreCase("N")) {
            rfileFlights.seek(rfileFlights.getFilePointer() + 60);
            rfileFlights.writeChars(fixToWrite(str));
        }
    }

    /**
     * Update the destination of each flight.
     */
    private void updateDestination() throws IOException {
        System.out.println("Change Destination (Press N to escape)");
        str = input.next();
        if (!str.equalsIgnoreCase("N")) {
            rfileFlights.seek(rfileFlights.getFilePointer() + 30);
            rfileFlights.writeChars(fixToWrite(str));
        }
    }

    /**
     * Update the origin of each flight.
     */
    private void updateOrigin() throws IOException {
        System.out.println("Change Origin (Press N to escape)");
        str = input.next();
        if (!str.equalsIgnoreCase("N")) {
            rfileFlights.seek(rfileFlights.getFilePointer());
            rfileFlights.writeChars(fixToWrite(str));

        }
    }
}
