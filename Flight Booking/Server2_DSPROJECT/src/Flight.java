/*
 *  IoKog
 *  target3
 */
public class Flight {

    private String from, to, time, arrival, departure, cost, flightno;
    private Integer seats;

    public Flight() {
    }

    public Flight(String from, String to, String arrival, String departure, String time, String cost, Integer seats, String flightno) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.arrival = arrival;
        this.departure = departure;
        this.cost = cost;
        this.seats = seats;
        this.flightno = flightno;
    }

    public void setSeats(Integer seats) {
        this.seats = this.seats - seats;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public String getCost() {
        return cost;
    }

    public Integer getSeats() {
        return seats;
    }

    public String getFlightno() {
        return flightno;
    }

    public String toString() {
        return from + " " + to + " " + arrival + " " + departure + " " + time + " " + cost + " " + flightno + " " + Integer.toString(seats) + " ";
    }

}
