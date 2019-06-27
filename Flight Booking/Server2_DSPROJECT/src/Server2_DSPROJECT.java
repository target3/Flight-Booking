/*
 *  IoKog
 *  target3
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server2_DSPROJECT {

    public static void main(String[] args) {

        try {
            // Δημιουργεία της λίστας με όλες τις πτήσεις
            // a list with flights
            List<Flight> flights = new ArrayList<>();
            flights.add(new Flight("Athens", "London", "30/5/2019", "2/6/2019", "10:30", "260", 10, "A512"));
            flights.add(new Flight("Athens", "London", "30/5/2019", "2/6/2019", "11:30", "280", 8, "A517"));
            flights.add(new Flight("Athens", "London", "30/5/2019", "2/6/2019", "16:30", "200", 5, "A513"));
            flights.add(new Flight("London", "Athens", "31/5/2019", "7/6/2019", "12:30", "200", 12, "A112"));
            flights.add(new Flight("London", "Athens", "31/5/2019", "7/6/2019", "13:30", "210", 21, "A113"));
            flights.add(new Flight("London", "Athens", "31/5/2019", "7/6/2019", "17:30", "250", 18, "A114"));
            flights.add(new Flight("Athens", "Samos", "2/6/2019", "1/7/2019", "11:30", "150", 8, "A312"));
            flights.add(new Flight("Athens", "London", "1/8/2019", "10/8/2019", "18:30", "173", 10, "A514"));
            flights.add(new Flight("London", "Athens", "1/6/2019", "10/6/2019", "22:30", "125", 18, "A530"));
            flights.add(new Flight("Samos", "Athens", "10/7/2019", "12/7/2019", "8:30", "90", 7, "A321"));
            flights.add(new Flight("Athens", "Berlin", "1/7/2019", "1/7/2019", "11:30", "100", 2, "A712"));
            flights.add(new Flight("Athens", "Berlin", "3/7/2019", "4/7/2019", "11:35", "140", 3, "A713"));
            flights.add(new Flight("Berlin", "Athens", "15/7/2019", "20/7/2019", "13:30", "220", 12, "A722"));
            flights.add(new Flight("Berlin", "Athens", "12/7/2019", "28/7/2019", "13:35", "210", 2, "A723"));
            flights.add(new Flight("Chania", "Athens", "1/8/2019", "7/8/2019", "9:30", "100", 5, "A412"));
            flights.add(new Flight("Athens", "Chania", "10/8/2019", "17/8/2019", "20:30", "180", 6, "A422"));
            flights.add(new Flight("Athens", "Chania", "10/8/2019", "17/8/2019", "11:30", "190", 8, "A421"));
            ServerSocket server = new ServerSocket(5555, 50);
            while (true) {
                // Δυο boolean μεταβλητές για να ελέγξουμε παρακάτω οτι καθώς γίνεται η αναζήτηση σε όλη την λίστα
                // βρέθηκε ή όχι πτήση που να ταιριάζει στα στοιχεία του χρήστη ή όχι
                // 2 boolean variables to check that during search to the list if the flight 
                //has been found or not that will match with given from client data
                boolean found, krat;
                Socket sock = server.accept();
                BufferedReader instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                //Διάβασμα του μηνύματος του χρήστη
                //read the message from the client
                String strin = instream.readLine();
                //Χωρίζει το μήνυμα σε έναν πίνακα δηλαδή το κάθε στοιχεία της αναζήτησης σε διαφορετικη θέση στο πίνακα
                //Separate the message into an array 
                String[] dedomena = strin.split("-");
                //Αν το πρώτο στοιχείο είναι KRATISI 
                //if the first element is KRATISI
                if (dedomena[0].equals("KRATISI")) {
                    //αρχικά κάνουμε την μεταβλητή boolean false
                    //first we make the variable boolean false
                    krat = false;
                    // Αναζήτηση σε όλη την λίστα
                    //search the list
                    for (int i = 0; i < flights.size(); i++) {
                        // Έλεγχος για τον αριθμό πτήσης και ότι οι θέσεις είναι μεγαλύτερες οι ίσες απο τον 
                        // αριθμό επιβατών που όρισε ο χρήστης.
                        //Check for the flight number and if number of seats are greater or equal from the
                        //given number of passengers that the client has given
                        if (dedomena[1].equals(flights.get(i).getFlightno()) && Integer.parseInt(dedomena[2]) <= flights.get(i).getSeats()) {
                            // Αν βρεθέι τέτοια πτήση τότε αλλάζει την μεταβλητή boolean 
                            // κάνει ένα "update" τις διαθέσιμες θέσεις για αυτή την πτήση και γυρνάει κάποια στοιχεία στον server1
                            // που θα τα στείλει με την σειρά του στον client (γραμμή κώδικα 215 στο  Gui_Client)
                            //If a flight has been found it changes the variable to true
                            //"updates" available seats for this flight and sends some data to server1
                            //and server1 sends those data to client (215 line at Gui_Client)
                            krat = true;
                            flights.get(i).setSeats(Integer.parseInt(dedomena[2]));
                            // Το πρώτο στοιχείο είναι ένα String για να μπορέσει να καταλάβει ο server1 για το αν η κράτηση
                            //ολοκληρώθηκε με επιτυχία ή όχι
                            //the first element is a string so server1 can understand if reservation has been completed
                            outstream.write("DONE" + " " + flights.get(i).getFlightno() + " " + flights.get(i).getFrom() + " " + flights.get(i).getTo() + " " + flights.get(i).getCost() + " " + dedomena[2] + " " + flights.get(i).getTime() + "\n");
                            outstream.flush();
                        }
                    }
                    // Αν δεν έχει μπεί στην παραπάνω If τότε σημαίνει οτι η boolean Μεταβλητή παραμένει false οπότε γυρνάει αντίστοιχο μήνυμα
                    //variable continues to be false and sends a message
                    if (!krat) {
                        outstream.write("FALSE" + "\n");
                        outstream.flush();
                    }
                // Αν το άιτημα είναι για αναζήτηση
                //if the request is for search
                } else {
                    // ίδια λογική του boolean με προηγουμένος
                    found = false;
                    for (int i = 0; i < flights.size(); i++) {
                        if (dedomena[0].equals(flights.get(i).getFrom())
                                && dedomena[1].equals(flights.get(i).getTo())
                                && dedomena[2].equals(flights.get(i).getArrival())
                                && dedomena[3].equals(flights.get(i).getDeparture())
                                && Integer.parseInt(dedomena[4]) <= flights.get(i).getSeats()) {
                            found = true;
                            // Γυρνάει το toString της κλάσης Flight (σειρά 58 της κλάσης Flight)
                            // δηλαδή όλα τα στοιχεία
                            //returns toString from Flight class(58 line from Flight class)
                            
                            outstream.write(flights.get(i).toString());
                        }
                    }
                    if (!found) {
                        outstream.write("FALSE" + "\n");
                        outstream.flush();
                    }
                    // Στο τέλος στέλνουμε το \n και το καθαρίζουμε το stream για να μπορέσει ο server1 να διαβάσει
                    // παραπάνω απο 1 αντικέιμενα-πτήσεις
                    //at the end we send \n and flush stream so server1 could read more than one objects-flights
                    outstream.write("\n");
                    outstream.flush();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error during I/O");
            ex.getMessage();
            ex.printStackTrace();
        }
    }
}
