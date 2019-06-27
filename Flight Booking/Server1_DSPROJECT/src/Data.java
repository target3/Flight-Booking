/*
 *  IoKog
 *  target3
 */
import java.io.Serializable;
import java.rmi.RemoteException;

public class Data implements Serializable {

    private String from, to, arrival, departure, passengers, time, flightno, cost;
    private Integer numseats;
    private Integer money;

    public Data(String from, String to, String arrival, String departure, String passengers) throws RemoteException {

        this.from = from;
        this.to = to;
        this.arrival = arrival;
        this.departure = departure;
        this.passengers = passengers;

    }

    public String getTime() {
        return time;
    }

    public String getFlightno() {
        return flightno;
    }

    public String getCost() {
        return cost;
    }

    public Data(String from, String to, String arrival, String departure, String time, String cost, String flightno, Integer numseats) {
        this.from = from;
        this.to = to;
        this.arrival = arrival;
        this.departure = departure;
        this.time = time;
        this.cost = cost;
        this.flightno = flightno;
        this.numseats = numseats;
    }

    public Integer getNumseats() {
        return numseats;
    }

    public String getFrom() {
        return from;
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

    public String getPassengers() {
        return passengers;
    }
    public String toString() {
        return ("<html>Flight No. : " + this.flightno + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Departure Time : " + this.time + "<br>"
                + "From : " + this.from + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To : " + this.to + "<br>"
                + "Departure Date : " + this.arrival + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Return Date : " + this.departure + "<br>"
                + "Available seats : " + this.numseats
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost (per person): " + this.cost + "<br>"
                + "------------------------------------------------------------------------------<br></html>");
    }

}
