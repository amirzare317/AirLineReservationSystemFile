public class FlightInfo {
    private String flightId;
    private String origin;
    private String destination;
    private String date;
    private String time;
    private int price;
    private int seats;
    private boolean isAllow = true;

    public void setAllow(boolean allow) {
        isAllow = allow;
    }

    public boolean isAllow() {
        return isAllow;
    }


    public FlightInfo(String flightId, String origin, String destination, String date, String time, int price, int seats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
        this.seats = seats;
    }


    public FlightInfo() {
    }

    public String getFlightId() {
        return flightId;
    }

}
