/*
 *  IoKog
 *  target3
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server1_DSPROJECT extends UnicastRemoteObject implements DataInterf {

    private List<Data> list;

    protected Server1_DSPROJECT() throws RemoteException {
        super();
    }

    protected Server1_DSPROJECT(List<Data> list) throws RemoteException {
        super();
    }

    @Override
    public String IDfinder(String id, String pass) throws RemoteException {
        Socket sock;
        try {
            // Σύνδεση με τον Server2
            //Connection with Server2
            sock = new Socket("localhost", 5555);
            BufferedReader instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            // Στέλνει στον server2 το αίτημα για την κράτηση, τον αριθμό πτήσης και τους επιβάτες που είχε βάλει 
            // απο την αναζήτηση ο client προηγουμένως.
            //Sents to Server2 request for the reservation, the flight number and passengers that client had previously searched
            outstream.write("KRATISI" + "-" + id + "-" + pass);
            outstream.newLine();
            outstream.flush();
            //Αφού ο server κάνει τους ελέγχους γυρνάει DONE αν μπόρεσε να γίνει η κράτηση ή FALSE αν δεν έγινε
            //if reservation is complete server returns DONE and False if it can't complete
            String strin = instream.readLine();
            String[] dedomena = strin.split(" ");
            // Αν έγινε η κράτηση και μειώθηκαν οι θέσεις για την συγκεκριμένη πτήση
            // τότε επιστρέφει στον client τα στοιχεία τις κράτησης. (215 γραμμή κώδικα στο Gui_Client)
            //if reservation is complete and number of seats decreased then returns to client
            //reservation's data (215 line at Gui_Client)
            if (dedomena[0].equals("DONE")) {
                instream.close();
                outstream.close();
                return "DONE" + " " + dedomena[1] + " " + dedomena[2] + " " + dedomena[3] + " " + dedomena[4] + " " + dedomena[5] + " " + dedomena[6];
            } else {
                // Αλλιώς γυρνάει στον χρήστη WRONG ώστε να του εμφανιστεί κατάλληλο μήνυμα οτι δεν μπόρεσε να γίνει η κράτηση
                instream.close();
                outstream.close();
                return "WRONG";
            }
        } catch (IOException ex) {
            Logger.getLogger(Server1_DSPROJECT.class.getName()).log(Level.SEVERE, null, ex);
            return "WRONG";
        }
    }

    // Μέθοδος για την αναζήτηση που καλεί ο client με όρισμα το αντικείμενο που έχει δημιουργήσει.
    //Search method that client uses
    @Override
    public List<Data> showdata(Data d1) throws RemoteException {
        list = new ArrayList<>();
        Socket sock;
        try {
            // κάνει την σύνδεση με τον Server_2
            //connection with Server_2
            sock = new Socket("localhost", 5555);
            BufferedReader instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            // Στέλνει στον server_2 τα στοιχεία του αντικειμένου
            //Sends to server_2 data from object
            outstream.write(d1.getFrom() + "-" + d1.getTo() + "-" + d1.getArrival() + "-" + d1.getDeparture() + "-" + d1.getPassengers());
            outstream.newLine();
            outstream.flush();
            // Διαβάζει αυτά που στέλνει ο server_2.
            //Reads those data that server_2 sends
            String strin = instream.readLine();
            String[] dedomena = strin.split(" ");
            // O server_2 στέλνει ένα string αυτό το string περιέχει πολλά στοιχεία για την ακρίβεια το πρώτο στοιχεία είναι αν βρέθηκαν ή όχι πτήσεις
            // αν δεν βρέθηκαν τότε επιστρέφει μια κενή λίστα στον client 
            // αν βρέθηκαν τότε τα περνάει σε μια λίστα. Για την δημιουργεία του αντικειμένου υπάρχουν 7 ορίσματα. Οπότε έχουμε δημιουργήσει μια επανάληψη
            // ανά 7. Ώστε να μπορέσουν να δημιουργήθουν παραπάνω απο ένα αντικείμενα.
            //Server_2 sends a sting . This string contains lots of data . The first is if flights have been found
            //if not returns an empty list to client
            //if found , add them into a list. to create the object there are 7 definitions. So we have made a loop of 7.
            //To create more than one object
            if (dedomena[0].equals("FALSE")) {
                return list;
            } else {
                // for ανα 7. Έχουμε δημιουργήσει ένα νέο constructor με 7 ορίσματα για τα νέα δεδομένα που παίρνει απο τον server2
                // χρειαζόμαστε αυτό τον αλγόριθμο γιατί ο server2 μπορεί να γυρίσει παραπάνω απο μια πτήσεις.
                //We have made a new constructor with 7 definitions for new data that takes from server2
                //We need this cause server2 can return more than one flight
                for (int i = 0; i < dedomena.length; i += 8) {
                    list.add(new Data(dedomena[i], dedomena[i + 1], dedomena[i + 2], dedomena[i + 3], dedomena[i + 4], dedomena[i + 5], dedomena[i + 6], Integer.parseInt(dedomena[i + 7])));
                }
                // νέο rebind με την λίστα.
                //new rebind with the list
                Naming.rebind("//localhost/S", new Server1_DSPROJECT(list));
                instream.close();
                outstream.close();
                //επιστρέφει την γεμάτη λίστα στον server.
                //returns full list to server
                return list;
            }
        } catch (IOException ex) {
            Logger.getLogger(Server1_DSPROJECT.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }

    public static void main(String[] args) {
        try {
            // δημιουργεί το αντικείμενο και κάνει super()
            //creation of object 
            Server1_DSPROJECT data = new Server1_DSPROJECT();
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
            Naming.bind("//localhost/S", data);
            System.out.println("Server up and running....");
        } catch (Exception ex) {
            System.out.println("Server not connected: " + ex);
        }
    }

}
