import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Passenger extends Files {
    Scanner input = new Scanner(System.in);

    // By creating this instance, you are able to access to the feature of Admin class.
    Admin infoAdmin;
    // In this array the infos of each passenger will be saved. Note that you can have at most 30 passengers.
    User[] passengerUser = new User[30];
    String string = "";
    String str = "";
    User user;
    // This array is used to show filtering. Note that the maxim length of array is equal to length of flights.
    Boolean[] showFilterItems;

    int userIdentifier;

    // This matrix is created for getting all the information of passengers(likely a local database).
    // Each row is known as a passenger.
    // In each column the data of each passenger including the tickets will be stored.
    private String[][] passengerFlightDetail = new String[15][30];

    // i is the identifier of users.
    int i = 0;

    // lineOrder makes you available to go throw each row of matrix.
    int lineOrder = 0;

    // Because in Java the integers are initialized by zero as default, number is initialized by 10 to avoid interference.
    int number = 10;

    {
        try {
            rfileUsers = new RandomAccessFile("users.dat", "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    {
        try {
            rfileTickets = new RandomAccessFile("tickets.dat", "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    File userFileExist = new File("users.dat");


    /**
     * Show the Menu of Passenger.
     */
    public void showPassengerMenu() {
        System.out.println("==============================");
        System.out.println("    PASSENGER MENU OPTIONS");
        System.out.println("==============================");
        System.out.println(". . . . . . . . . . . . . .");
        System.out.println("    <1> Change password");
        System.out.println("    <2> Search flight tickets");
        System.out.println("    <3> Booking ticket");
        System.out.println("    <4> Ticket cancellation");
        System.out.println("    <5> Booked tickets");
        System.out.println("    <6> Add charge");
        System.out.println("    <7> Show all flights");
        System.out.println("    <0> Sign out");
    }

    /**
     * Just to show the passenger menu after each action.
     */
    public void setShowPassengerMenuAgain() {
        System.out.println("Press any key to continue...");
        string = input.next();
        System.out.print("\033[H\033[2J");
        showPassengerMenu();
    }

    /**
     * When the menu showed, by entering any button which is designed here, the program will start to run.
     */
    public void passengerOption() throws IOException {
        number = 10;
        while (number != 0) {
            number = input.nextInt();
            switch (number) {
                case 1:
                    System.out.println("Changing password...");
                    System.out.println("Enter your new password: ");
                    string = input.next();
                    setPassword(string);
                    setShowPassengerMenuAgain();
                    break;
                case 2:
                    System.out.println("Searching flight tickets...");
                    search();
                    setShowPassengerMenuAgain();
                    break;
                case 3:
                    System.out.println("Booking tickets");
                    System.out.println("Would you like to see flight table once again? Y or N");
                    string = input.next();
                    if (string.equalsIgnoreCase("Y")) {
                        showAllFlights();
                    }
                    System.out.println("Enter your intended flight ID to book it.");
                    string = input.next();
                    if (isEnoughSeat(string)) {
                        if (isEnoughCharge(string)) {
                            bookTickets(string);
                        }
                    }
                    setShowPassengerMenuAgain();
                    break;
                case 4:
                    System.out.println("Cancelling...");
                    System.out.println("This is all the flights you have reserved before...");
                    rfileTickets.seek(0);
                    for (int j = 0; j < rfileTickets.length() / 30; j++) {
                        String str = "";
                        str = fixToRead(rfileTickets);
                        rfileUsers.seek(userIdentifier * 60L + 30);
                        if (str.contains(fixToRead(rfileUsers))) {

                            for (int i = 0; i < rfileFlights.length() / 160; i++) {
                                rfileFlights.seek(i * 160L);
                                if (str.contains(fixToRead(rfileFlights))) {
                                    rfileFlights.seek(i * 160L);
                                    System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15d %-15d\n", fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), rfileFlights.readInt(), rfileFlights.readInt());
                                }
                            }
                        }
                    }

                    System.out.println("---------------------------------------------------------------------------------------------------");
                    System.out.println("These are all of your reservation codes: ");
                    rfileTickets.seek(0);
                    for (int j = 0; j < rfileTickets.length() / 30; j++) {
                        rfileUsers.seek(userIdentifier * 60L + 30);
                        if (fixToRead(rfileTickets).contains(fixToRead(rfileUsers))){
                            rfileTickets.seek(rfileTickets.getFilePointer() - 30);
                            System.out.println(fixToRead(rfileTickets));
                        }
                    }
                    System.out.println("Enter your intended flight ID to cancel it.");
                    str = input.next();
                    System.out.println("Enter your reservation code to cancel it.");
                    string = input.next();
                    ticketCancellation(string);
                    resetCharge(string);
                    resetSeat(string);
                    resetAllow(str);
                    setShowPassengerMenuAgain();
                    break;
                case 5:
                    System.out.println("Booked tickets...");
                    showBookedFlights();
                    setShowPassengerMenuAgain();
                    break;
                case 6:
                    System.out.println("Adding charge...");
                    System.out.println("Enter the amount of money you want to charge");
                    int chargeAmount = input.nextInt();
                    charge(chargeAmount);
                    rfileUsers.seek(rfileUsers.getFilePointer() - 4);
                    System.out.println("Your charge is: " + rfileUsers.readInt());
                    setShowPassengerMenuAgain();
                    break;
                case 7:
                    System.out.println("All flights:");
                    showAllFlights();
                    setShowPassengerMenuAgain();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Wrong input!!!");
                    break;
            }
        }
    }

    public void setPassword(String str) throws IOException {
        rfileUsers.seek(rfileUsers.getFilePointer() - 34);
        rfileUsers.writeChars(fixToWrite(string));
    }


    /**
     * Passenger can book a ticket.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     */
    public void bookTickets(String string) throws IOException {
        int flag = 0;
        int n = 0;
        // If the condition above is true, the ticketID will save to the related index of matrix.
        // The structure of ticketID is: UserPassword + | + FlightID + | + # + Free seats
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek(n * 160L);
            if (fixToRead(rfileFlights).equals(string)) {
                rfileUsers.seek(rfileUsers.getFilePointer() - 34);
                rfileFlights.seek(rfileFlights.getFilePointer() + 124);
                rfileTickets.writeChars(fixToWrite(fixToRead(rfileUsers) + "|" + returnFlightId(string) + "|#" + rfileFlights.readInt()));

            }
            n++;
        }
        System.out.println("Your ticket was successfully reserved");

        // Since a passenger has bought a ticket, so update the number of seats.
        updateSeat(string);
        lineOrder++;
        flag = 1;

    }
//            if (flag == 0) {
//            System.out.println("This flight doesn't exist");
//        }

    public String returnFlightId(String string) throws IOException {
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((i * 160L));
            if (fixToRead(rfileFlights).equals(string)) {
                return string;
            }
        }
        return null;
    }

    /**
     * Passenger can cancel a ticket.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     *               By cancelling, the ticketID will initialize to null.
     */
    public void ticketCancellation(String string) throws IOException {
        int flag = 0;
        int n = 0;
        rfileTickets.seek(0);
        for (int i = 0; i < (rfileTickets.length() / 30); i++) {
            if (rfileTickets.length() <= 30){
                rfileTickets.setLength(0);
            }
            String reservationCode = fixToRead(rfileTickets);
            if (reservationCode.equals(string)){
                for (int j = 0; j < ((rfileTickets.length() / 30) - n) - 1; j++) {
                    String str = fixToRead(rfileTickets);
                }
            }
            flag = 1;
            n++;
        }
        if (flag == 0) {
            System.out.println("You haven't register this flight before.");
        }
    }

    /**
     * This method shows all the flights that has been reserved before.
     * It will search in matrix and if ticketID contains FlightID, will represent the flight info.
     */
    public void showBookedFlights() throws IOException {
        rfileTickets.seek(0);
        for (int j = 0; j < rfileTickets.length() / 30; j++) {
            String str = "";
            str = fixToRead(rfileTickets);
            rfileUsers.seek(userIdentifier * 60L + 30);
            if (str.contains(fixToRead(rfileUsers))) {

                for (int i = 0; i < rfileFlights.length() / 160; i++) {
                    rfileFlights.seek(i * 160L);
                    if (str.contains(fixToRead(rfileFlights))) {
                        rfileFlights.seek(i * 160L);
                        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15d %-15d\n", fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), rfileFlights.readInt(), rfileFlights.readInt());
                    }
                }
            }
        }
    }

    /**
     * To check if passenger have enough cash before buying ticket.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     * @return If passenger have enough charge, it returns true otherwise it return false.
     * This loop repeats until passenger have enough charge to book his/her intended flight.
     */
    public boolean isEnoughCharge(String string) throws IOException {
        int n = 0;
        for (int k = 0; k < rfileFlights.length() / 160; k++) {
            rfileFlights.seek(n * 160L);
            if (fixToRead(rfileFlights).equals(string)) {
                rfileFlights.seek((n * 160L) + 150);
                rfileUsers.seek(60);
                if (rfileUsers.readInt() >= rfileFlights.readInt()) {
                    return true;
                } else {
                    rfileFlights.seek(rfileFlights.getFilePointer() - 4);
                    while (true) {
                        System.out.println("Your charge is not enough -> If you want to charge enter 'Y' to charge your account. At least you need " + rfileFlights.readInt());
                        string = input.next();
                        if (string.equalsIgnoreCase("Y")) {
                            System.out.println("Enter the amount of money you want to charge");
                            int chargeAmount = input.nextInt();
                            charge(chargeAmount);
                            rfileUsers.seek(rfileUsers.getFilePointer() - 4);
                            System.out.println("You added " + rfileUsers.readInt() + " to your wallet.");

                            rfileUsers.seek(rfileUsers.getFilePointer() - 4);
                            rfileFlights.seek(rfileFlights.getFilePointer() - 4);

                            if (rfileUsers.readInt() >= rfileFlights.readInt()) {
                                //Set the new amount of charge
                                rfileUsers.seek(rfileUsers.getFilePointer() - 4);
                                rfileFlights.seek(rfileFlights.getFilePointer() - 4);
                                int newCharge = rfileUsers.readInt() - rfileFlights.readInt();
                                rfileUsers.seek(rfileUsers.getFilePointer() - 4);
                                rfileUsers.writeInt(newCharge);
                                rfileUsers.seek(rfileUsers.getFilePointer() - 4);
                                System.out.println("Now your charge is: " + rfileUsers.readInt());
                                return true;
                            }
                        }
                    }
                }
            }
            n++;
        }
        return false;
    }

    /**
     * If the passenger that has bought a ticket, want to cancel it and that flight doesn't have any passenger, admin is allowed to update or delete that flight.
     *
     * @param str By giving an string to the method, it will search whole matrix to check whether that flight is available or not.
     *            If there wasn't that flight, admin will be able to update or delete that flight.
     */
    public void resetAllow(String str) {
        for (int n = 0; n < 15; n++) {
            for (int m = 0; m < 30; m++) {
                if (passengerFlightDetail[n][m] != null && (!passengerFlightDetail[n][m].contains(str))) {
                    infoAdmin.flights[findFlightIndex(str)].setAllow(true);
                }
            }
        }
    }

    /**
     * This method give the FlightID from passenger and find the index of array which all the flights are stored in.
     *
     * @param str By giving an string to the method, it will search for the FlightID which is equal to the string.
     * @return It would return the index of array.
     */
    public int findFlightIndex(String str) {
        for (int k = 0; k < infoAdmin.flights.length; k++) {
            if (infoAdmin.flights[k] != null && infoAdmin.flights[k].getFlightId().equals(str)) {
                return k;
            }
        }
        return 100;
    }

    /**
     * Is used to check whether there is enough seat or not.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     * @return If there is enough seat, it would return true, otherwise it would return false.
     */
    public boolean isEnoughSeat(String string) throws IOException {
        int n = 0;
        for (int k = 0; k < rfileFlights.length() / 160; k++) {
            rfileFlights.seek((n * 160L) + 154);
            if (rfileFlights.readInt() > 0) {
                return true;
            }
            n++;
        }
        System.out.println("This flight doesn't have empty seat !!!");
        return false;

    }

    /**
     * When passengers buy or cancel a ticket, this method will update the seats.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     */
    public void updateSeat(String string) throws IOException {
        int n = 0;
        for (int k = 0; k < rfileFlights.length() / 160; k++) {
            rfileFlights.seek(n * 160L);
            if (fixToRead(rfileFlights).equals(string)) {
                rfileFlights.seek(rfileFlights.getFilePointer() + 124);
                rfileFlights.writeInt(rfileFlights.readInt() - 1);
            }
            n++;
        }
    }

    /**
     * When the user cancel the ticket, the money of that ticket will return to his/her wallet.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     */
    public void resetCharge(String string) {
        for (int k = 0; k < infoAdmin.flights.length; k++) {
            if (infoAdmin.flights[k] != null && infoAdmin.flights[k].getFlightId().equals(string)) {
                passengerUser[i - 1].setCharge(passengerUser[i - 1].getCharge() + infoAdmin.flights[k].getPrice());
            }
        }
    }

    /**
     * When the user cancel the ticket, seats of that flight will be updated.
     *
     * @param string By giving an string to the method, it will search for the FlightID which is equal to the string.
     */
    public void resetSeat(String string) {
        for (int k = 0; k < infoAdmin.flights.length; k++) {
            if (infoAdmin.flights[k] != null && infoAdmin.flights[k].getFlightId().equals(string)) {
                infoAdmin.flights[k].setSeats(infoAdmin.flights[k].getSeats() + 1);
            }
        }

    }

    /**
     * By calling this method, admin wouldn't be able to update or delete intended flight.
     *
     * @param index is the index of flight in array.
     */
    public void isAllowToChange(int index) {
        infoAdmin.flights[index].setAllow(false);
    }

    /**
     * Each user should register to access the features.
     *
     * @param userName is the username of the passenger.
     * @param password is the password of the passenger.
     */
    public void registration(String userName, String password) throws IOException {

        System.out.print("\033[H\033[2J");

        if (userFileExist.exists()) {
            rfileUsers.seek(rfileUsers.length());

            // User name
            rfileUsers.writeChars(fixToWrite(userName));  //30
            // Password
            rfileUsers.writeChars(fixToWrite(password)); // 30
            // The default charge = 0
            rfileUsers.writeInt(0);
        } else {
            rfileUsers = new RandomAccessFile("users.dat", "rw");
        }

        System.out.println("Congratulation! Your registration was successful");
    }

    public boolean isRegisteredBefore(String userName, String password) throws IOException {
        rfileUsers.seek(0);

        for (int j = 0; j < rfileUsers.length() / 64; j++) {
            if (fixToRead(rfileUsers).equals(userName) && fixToRead(rfileUsers).equals(password)) {
                rfileUsers.readInt();
                userIdentifier = j;
                return true;
            }
        }
        return false;
    }

    /*
     * Since each passenger have its own unique number -> i, to avoid interference we define i.
     * @param password Every passenger has an unique password, so by giving the password, i can be found easily.
     */
//    public void defineI(String password) {
//        for (int k = 0; k < passengerUser.length; k++) {
//            if (passengerUser[k] != null && passengerUser[k].getPassword().equals(password)) {
//                i = k + 1;
//            }
//        }
//    }


    /**
     * Filtering origin of flight.
     * Now the intended section will switch to true.
     */
    public void filterOriginMixed() throws IOException {
        System.out.println("Filtering Origin (Press N to escape)");
        string = input.next();
        int n = 0;
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 30);
            if ((fixToRead(rfileFlights).equals(string)) && (!string.equalsIgnoreCase("N"))) {
                rfileFlights.seek(((n + 1) * 160L) - 1);
                rfileFlights.writeBoolean(true);
            }
            n++;
        }
    }

    /**
     * Filtering Destination of flight.
     * Now the intended section will switch to true.
     */
    public void filterDestinationMixed() throws IOException {
        System.out.println("Filtering Destination (Press N to escape)");
        string = input.next();
        int n = 0;
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 60);
            if ((fixToRead(rfileFlights).equals(string)) && (!string.equalsIgnoreCase("N"))) {
                rfileFlights.seek(((n + 1) * 160L) - 1);
                rfileFlights.writeBoolean(true);
            }
            n++;
        }
    }

    /**
     * Filtering price range.
     * Now the intended section will switch to true.
     */
    public void filterPriceMixed() throws IOException {
        System.out.println("Filtering Price (Press N to escape)");
        int n = 0;
        if (!string.equalsIgnoreCase("N")) {

            System.out.println("============Defining price range============");
            System.out.println("Enter the first range:");
            int x = input.nextInt();
            System.out.println("Enter the second range:");
            int y = input.nextInt();
            for (int i = 0; i < rfileFlights.length() / 160; i++) {
                if (checkValueBetween(x, y, n)) {
                    rfileFlights.seek(((n + 1) + 160) - 1);
                    rfileFlights.writeBoolean(true);
                }
                n++;
            }
        }
    }

    /**
     * If any of index changes to true -> shows that passenger decides to filter flights by these features.
     * This method will print only the true values.
     */
    public void printFilters() throws IOException {
        int n = 0;
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek(((n + 1) * 160L) - 1);
            if (rfileFlights.readBoolean()) {
                printFlight(n);
            }
            n++;
        }
    }

    /**
     * To avid confusing for other passengers, the values of boolean array resets each time.
     */
    public void resetFilters() throws IOException {
        int n = 0;
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n + 1) * 160 - 1);
            rfileFlights.writeBoolean(false);
        }
    }

    /**
     * This method check whether the price is in the range or not.
     *
     * @param x is the first value -> from...
     * @param y is the second value -> up to...
     * @param n is the index array which flights are stored in.
     * @return If the price is between x and y it returns true otherwise it returns false.
     */
    public boolean checkValueBetween(int x, int y, int n) throws IOException {
        rfileFlights.seek((n * 160L) + 150);
        if (rfileFlights.readInt() >= x) {
            rfileFlights.seek(rfileFlights.getFilePointer() - 4);
            if (rfileFlights.readInt() <= y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is used to charge the passenger wallet.
     *
     * @param chargeAmount is the amount of money that would set to passenger's wallet.
     */
    public void charge(int chargeAmount) throws IOException {
        rfileUsers.seek(60);
        rfileUsers.writeInt(chargeAmount);
    }

    /**
     * Passenger can search flights in two ways:
     * First: search by only item like origin, destination etc.
     * Second: search by some item.
     * Note: Passenger can select different items to filter flights.
     */
    public void search() throws IOException {
        System.out.println(".............How do you want to filter flights?.............\n" +
                "   <1> Filter by one item\n" +
                "   <2> Filter by some items\n");
        number = input.nextInt();
        switch (number) {
            case 1:
                System.out.println("Filtering one item...");
                showFilterMenu();
                number = input.nextInt();
                switch (number) {
                    case 1:

                        filterOrigin();
                        break;
                    case 2:

                        filterDestination();
                        break;
                    case 3:

                        filterDate();
                        break;
                    case 4:

                        filterTime();
                        break;
                    case 5:

                        filterPrice();
                }
                break;
            default:
                System.out.println("Incorrect input!!!");
                break;

            case 2:
                System.out.println("Filtering by some items");
                filterOriginMixed();
                filterDestinationMixed();
                filterPriceMixed();
                printFilters();
                resetFilters();
        }
    }

    /**
     * Filtering origin.
     * Passenger can filter only by origin.
     */
    public void filterOrigin() throws IOException {
        System.out.println("Filter by origin:");
        string = input.next();
        int flag = 0;
        int n = 0;
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 30);
            if (fixToRead(rfileFlights).equals(string)) {
                printFlight(n);
                flag = 1;
            }
            n++;
        }
        if (flag == 0) {
            System.out.println("Your intended flight is not available");
        }
    }

    /**
     * Filtering destination.
     * Passenger can filter only by destination.
     */
    public void filterDestination() throws IOException {
        System.out.println("Filter by destination:");
        string = input.next();
        int flag = 0;
        int n = 0;
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 60);
            if (fixToRead(rfileFlights).equals(string)) {
                printFlight(n);
                flag = 1;
            }
            n++;
        }
        if (flag == 0) {
            System.out.println("Your intended flight is not available");
        }
    }

    /**
     * Filtering date.
     * Passenger can filter only by date.
     */
    public void filterDate() throws IOException {
        System.out.println("Filter by date:");
        string = input.next();
        int flag = 0;
        int n = 0;
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 90);
            if (fixToRead(rfileFlights).equals(string)) {
                printFlight(n);
                flag = 1;
            }
            n++;
        }
        if (flag == 0) {
            System.out.println("Your intended flight is not available");
        }
    }

    /**
     * Filtering time.
     * Passenger can filter only by time.
     */
    public void filterTime() throws IOException {
        System.out.println("Filter by time:");
        string = input.next();
        int flag = 0;
        int n = 0;
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 120);
            if (fixToRead(rfileFlights).equals(string)) {
                printFlight(n);
                flag = 1;
            }
            n++;
        }
        if (flag == 0) {
            System.out.println("Your intended flight is not available");
        }
    }

    /**
     * Filtering price.
     * Passenger can filter only by price.
     */
    public void filterPrice() throws IOException {
        System.out.println("Filter by price:");
        string = input.next();
        int flag = 0;
        int n = 0;
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            rfileFlights.seek((n * 160L) + 150);
            if (fixToRead(rfileFlights).equals(string)) {
                printFlight(n);
                flag = 1;
            }
            n++;
        }
        if (flag == 0) {
            System.out.println("Your intended flight is not available");
        }
    }

    /**
     * If passenger decide filter only by one item, this menu will be printed.
     */
    public void showFilterMenu() {
        System.out.println("    <1> Filter by Origin");
        System.out.println("    <2> Filter by Destination");
        System.out.println("    <3> Filter by Date");
        System.out.println("    <4> Filter by Time");
        System.out.println("    <5> Filter by Price");
    }

    /**
     * Only prints the information of specific flight.
     *
     * @param n is the index of flight in array.
     */
    public void printFlight(int n) throws IOException {
        rfileFlights.seek(n * 160L);
        System.out.println(".....................................................................................................");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15d %-15d\n", fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), rfileFlights.readInt(), rfileFlights.readInt());
        rfileFlights.readBoolean();
        rfileFlights.readBoolean();
    }

    /**
     * Show all flights.
     */
    public void showAllFlights() throws IOException {
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s\n", "FlightId", "Origin", "Destination", "Date", "Time", "Price", "Seats");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        rfileFlights.seek(0);
        for (int i = 0; i < rfileFlights.length() / 160; i++) {
            System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15d %-15d\n", fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), fixToRead(rfileFlights), rfileFlights.readInt(), rfileFlights.readInt());
            rfileFlights.readBoolean();
            rfileFlights.readBoolean();
            System.out.println(".....................................................................................................");
        }
    }
}
