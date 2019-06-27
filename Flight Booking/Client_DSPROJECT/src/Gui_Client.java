/*
 *  IoKog
 *  target3
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class Gui_Client extends JFrame implements ActionListener {

    private Date arrivalDate, departureDate;
    private JButton search, kratisi, back;
    private JTextField fromfield, tofield, arrivalfield, departurefield, epivatesfield, kratisifield;
    private JLabel from_label, to_label, arrival_label, departure_label, epivates_label, kratisi_label;
    private String from, to, arrival, departure, passengers;
    List<Data> list;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Long difference;
    private int dates = 0;
    JFrame frame;
    private static DataInterf look_up;

    // Αρχικό Gui στον Client όπου θα εμφανίζονται τα πεδία για την αναζήτηση πτήσης
    //Gui for flight search
    public Gui_Client() {
        super("Available Flight Search");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        /*
         * Παρακάτω καθώς φτιάχνουμε τα label και τα fields βλέπουμε οτι έχουμε προκαθωρίσει εμείς κάποιες τιμές που θα έχουν τα πεδία μας
	 * αυτά δεν χρειαζόταν. Για τα συγκεκριμένα στοιχεία έχουμε ορίσει στον server_2  τρεία δρομολόγια σε ξεχωριστές ώρες με διαφορετική 
	 * τιμή και διαφορετικές διαθέσιμες πτήσης ώστε να μην χρειάζεται να πληκτρολογούμε κάθε φορά τα ίδια για τους ελέγχους. Ετσι το αφήσαμε 
	 * προκαθορισμένα αυτά τα στοιχεία μήπως βοηθήσουν και εσάς.
         */
        from_label = new JLabel("From : ");
        from_label.setBounds(135, 20, 50, 25);
        fromfield = new JTextField(10);
        fromfield.setBounds(180, 20, 200, 25);
        fromfield.setText("Athens");

        to_label = new JLabel("To : ");
        to_label.setBounds(130, 70, 55, 25);
        tofield = new JTextField(10);
        tofield.setBounds(180, 70, 200, 25);
        tofield.setText("London");

        arrival_label = new JLabel("Departure Date : ");
        arrival_label.setBounds(45, 120, 150, 25);
        arrivalfield = new JTextField(10);
        arrivalfield.setText("30/5/2019");
        arrivalfield.setBounds(180, 120, 200, 25);

        departure_label = new JLabel("Return Date : ");
        departure_label.setBounds(50, 170, 125, 25);
        departurefield = new JTextField(10);
        departurefield.setText("2/6/2019");
        departurefield.setBounds(180, 170, 200, 25);

        epivates_label = new JLabel("Passenger No. : ");
        epivates_label.setBounds(55, 220, 125, 25);

        epivatesfield = new JTextField(10);
        epivatesfield.setBounds(180, 220, 200, 25);
        epivatesfield.setText("2");

        search = new JButton("Flight Search");
        search.addActionListener(this);
        search.setBounds(120, 270, 200, 50);

        Container pane = getContentPane();
        pane.setLayout(null);
        pane.add(fromfield);
        pane.add(from_label);
        pane.add(tofield);
        pane.add(to_label);
        pane.add(epivates_label);
        pane.add(epivatesfield);
        pane.add(departure_label);
        pane.add(departurefield);
        pane.add(arrival_label);
        pane.add(arrivalfield);
        pane.add(search);
        setContentPane(pane);
    }

    //Gui για την εμφάνιση τον αποτελεσμάτων βάση της αναζήτησης
    public void Gui_Emfanisi() {
        frame = new JFrame();
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        kratisi_label = new JLabel("Choose Flight(Flight Id) : ");
        kratisi_label.setBounds(80, 480, 250, 25);
        kratisifield = new JTextField(10);
        kratisifield.setBounds(300, 480, 200, 25);

        //Μέθοδος για την εμφάνιση της λίστας που έλαβε από τον server_2
        //Method to show list from Server_2
        DefaultListModel<String> l1 = new DefaultListModel<>();
        list.forEach(x -> {
            l1.addElement(x.toString());
        });

        JList<String> list = new JList<>(l1);
        list.setBounds(50, 20, 500, 400);
        frame.add(list);

        frame.setLayout(null);
        frame.setVisible(true);
        kratisi = new JButton("Reservation");
        kratisi.addActionListener(this);
        kratisi.setBounds(80, 520, 200, 50);

        back = new JButton("Back");
        back.addActionListener(this);
        back.setBounds(320, 520, 200, 50);

        frame.setLayout(null);
        frame.add(kratisi_label);
        frame.add(kratisifield);
        frame.add(back);
        frame.add(kratisi);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        //Για το κουμπί search
        if (source == search) {
            from = fromfield.getText();
            to = tofield.getText();
            arrival = arrivalfield.getText();
            departure = departurefield.getText();
            passengers = epivatesfield.getText();
            try {
                //Εδώ προσπαθούμε να βρούμε την διαφορά που έχουν οι ημερομηνίες που έδωσε ο χρήστης ώστε παρακάτω να ελέγξουμε οτι είναι σωστές
                // και δεν είναι η ημέρα αναχώρησης μετα την ημέρα επιστροφής
                //Here we check so the departure date will not be after arrival date
                arrivalDate = formatter.parse(arrival);
                departureDate = formatter.parse(departure);
                difference = departureDate.getTime() - arrivalDate.getTime();
                dates = (int) (difference / (1000 * 60 * 60 * 24));
            } catch (ParseException ex) {
                Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Ελέγχει ότι κανένα πεδίο δεν είναι άδειο το σύνολο των αριθμών είναι αρνητικό (ημέρα αναχώρησης νωρίτερα απο ημέρα επιστροφής)
            //Check if fields are not empty
            if (dates < 0 || from.equals("") || to.equals("") || passengers.equals("") || arrival.equals("") || departure.equals("")) {
                JOptionPane.showMessageDialog(null, "Wrong Data");
                dispose();
                new Gui_Client();
            } else {
                try {
                    // άμα όλα είναι σωστά τότε δημιουργεί ένα καινούργιο αντικείμενο
                    //if all data are correct creates new object
                    Data d1 = new Data(from, to, arrival, departure, passengers);
                    String url = "//localhost/S";
                    // Γίνετε η σύνδεση με τον rmi server και καλεί την μέθοδο που υλοποιεί ο Server_1 δηλαδή την showdata στέλνοντας έτσι το αντικέιμενο που έφτιαξε στον server_1
                    // O Server_1 κάνοντας τις λειτουργίες του μέσα στη μέθοδο έχει επιστρέψει στον Client μια λίστα με τις πτήσεις σύμφωνα με τα στοιχεία που έδωσε ο χρήστης
                    //connection with rmi server and call showdata method from Server_1
                    //Server_1 returns to Client a list of flights based on client's given data
                    look_up = (DataInterf) Naming.lookup(url);
                    // Η λίστα αποθηκεύεται σε μια global Ματαβλητή ώστε να μπορεί να έχει πρόσβαση η κλάση της κράτησης
                    list = look_up.showdata(d1);
                    // Αν η λίστα είναι κενή τότε σημαίνει οτι ο server_1 σε συνεργασία με τον server_2 δεν βρήκαν κάποια πτήση που να ταιριάζει με τα δεδομένα που έδωσε ο χρήστης
                    // οπότε του εμφανίζεται μήνυνμα ότι δεν υπάρχουν διαθέσιμες πτήσεις αν η λίστα δεν είναι άδεια τότε προχωράει στο παράθυρο για την κράτηση
                    //If list is empty then server_1 in cooperation with server_2 can't find a flight that matches with client's data
                    // client gets a message that there are not available flights but if list is not empty client gets available flights
                    if (!list.isEmpty()) {
                        dispose();
                        Gui_Emfanisi();
                    } else {
                        JOptionPane.showMessageDialog(null, "Not available flights for given data");
                        dispose();
                        new Gui_Client();
                    }
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else if (source == kratisi) {
            Random rand = new Random();
            String url = "//localhost/S";
            try {
                // Αν το πεδίο για την κράτηση δεν είναι κενό που σημαίνει οτι ο χρήστης έδωσε κάποιον κωδικό πτήσης
                if (!kratisifield.equals("")) {
                    look_up = (DataInterf) Naming.lookup(url);
                    // Καλούμε την δεύτερη μέθοδο του Interface την IDfinder με τα δύο ορίσματα που είναι το string για τον αριθμό πτήσης και τους επιβάτες
                    // Τους επιβάτες τους θέλουμε να περνάνε στον server_1 και στην συνέχεια στον server_2 ώστε να γίνει ξανά έλεγχος ότι στην συγκεκριμένη πτήση υπάρχουν 
                    // τόσε διαθέσιμες θέσης. Σε περίπτωση δηλαδή ανταγωνισμού των χρηστών να υπάρχει και ένας ακόμα έλεγχος. Η απάντηση που θα δεχθέι ο χρήστης 
                    // είναι ένα string το οποίο θα έχει πολλές πληροφορίες που θα χρειαστεί να τις διαχωρίσουμε.
                    //Here we call IDfinder method with flight number and number of passengers
                    //First we pass them through server_1 and after to server_2 to check the availability
                    String answer = look_up.IDfinder(kratisifield.getText(), passengers);
                    String[] dedomena = answer.split(" ");
                    // Στη πρώτη θέση έχουμε βάλει να επιστρέφει την λέξη DONE αν μπόρεσε να γίνει η κράτηση απο τον server_2 ή WRONG αν δεν μπόρεσε
                    // Ετσι ο χρήστης θα λάβει ή το μήνυμα με τα στοιχεία της κράτησης ή ένα μήνυμα λάθους
                    //At first position we return Done if reservation is made from server_2 or Wrong for the opposite
                    //So client will get a pop-up with reservation details or an error message
                    if (dedomena[0].equals("DONE")){
                        JOptionPane.showMessageDialog(null, "Reservation made successfully"
                                + "\nFlight No. : " + dedomena[1]
                                + "\nFrom : " + dedomena[2]
                                + "\nTo : " + dedomena[3]
                                + "\nTime : " + dedomena[6]
                                + "\nPassengers No. : " + dedomena[5]
                                + "\nTotal Cost : " + Integer.parseInt(dedomena[4]) * Integer.parseInt(dedomena[5])
                                + "\nGate : " + "A" + rand.nextInt(50));
                        frame.dispose();
                        new Gui_Client();
                    } else {
                        frame.dispose();
                        new Gui_Client();
                        JOptionPane.showMessageDialog(null, "There is some problem with the reservation");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You have to fill the reservation field");
                }
            } catch (NotBoundException ex) {
                Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(Gui_Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (source == back) {
            frame.dispose();
            new Gui_Client();
        }

    }
}
