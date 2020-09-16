import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JFrame{
    private JPanel panel;
    private JList<Business> clusteredEnt;
    private JList<Business> businesses;
    private JList<Business> closestEnt;
    private JTextArea similarEnt;
    private JList<Review> reviews;
    private JLabel businessLabel;
    private JLabel current;
    private JLabel clusterLabel;
    private JLabel reviewLabel;
    private JLabel closestLabel;
    private JLabel similarLabel;

    static BusinessTree bt = null;
    KMeans km = null;
    final Loader l = new Loader();
    /*boolean LoadSuccess allows me to save only when a load fails
    i.e. file doesn't exist then create and save*/
    static boolean LoadSuccess = false;
    public Main(){
        super("Yelp Recommender");
        System.out.println("Loading...");
        //if load fails
        if(!l.load()){
            System.out.println("Load Unsuccessful.\nReading JSON...");
            LoadSuccess = false;
            JSONDataPuller json = new JSONDataPuller();
            km = new KMeans(json.getBusinesses(), json.getCategories());
            System.out.println("Building Business List...");
            bt = new BusinessTree(json.getBusinesses(), km);
            System.out.println("Building Complete.");
        }else{
            LoadSuccess = true;
            System.out.println("Load successful!");
            bt = l.getBL();
        }

        WindowListener wndCloser = new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                if(!LoadSuccess){
                    System.out.println("Saving...");
                    if(!l.save(bt))System.out.println("Save Unsuccesful!");
                    else System.out.println("Save Successful!");

                }
                System.exit(0);
            }
        };
        addWindowListener(wndCloser);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

        businesses.setModel(new javax.swing.AbstractListModel<Business>() {
            Business[] b = bt.getArray();
            public int getSize() { return b.length; }
            public Business getElementAt(int i) { return b[i]; }
        });
        businesses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                businessSelected(businesses.getSelectedValue());
            }
        });
        businesses.setVisibleRowCount(10);
        businesses.setCellRenderer(new BusinessCellRenderer());

        closestEnt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        closestEnt.setModel(new javax.swing.AbstractListModel<Business>() {
            Business[] l = bt.getArray()[0].getClosest();
            public int getSize() { return l.length; }
            public Business getElementAt(int i) { return l[i]; }
        });
        closestEnt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closestSelected(closestEnt.getSelectedValue());
            }
        });
        closestEnt.setCellRenderer(new BusinessCellRenderer());
        closestEnt.setVisibleRowCount(10);

        clusteredEnt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clusteredEnt.setModel(new javax.swing.AbstractListModel<Business>() {
            Business[] l = bt.getCluster(bt.getArray()[0]);
            public int getSize() { return l.length; }
            public Business getElementAt(int i) { return l[i]; }
        });
        clusteredEnt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clusteredSelected(clusteredEnt.getSelectedValue());
            }
        });
        clusteredEnt.setCellRenderer(new BusinessCellRenderer());
        clusteredEnt.setVisibleRowCount(5);


        reviews.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reviews.setModel(new javax.swing.AbstractListModel<Review>() {
            Business b = bt.getArray()[0];
            ArrayList<Review> r = b.getReviews();
            public int getSize() { return r.size(); }
            public Review getElementAt(int i) { return r.get(i); }
        });
        reviews.setCellRenderer(new ReviewCellRenderer());
        reviews.setVisibleRowCount(10);

        current.setText(bt.getArray()[0].getName());
        Business sim = bt.getArray()[0].getSim(bt.getArray());
        similarEnt.setText(sim.getName() + "\n" + sim.getAddress() + "\n" + sim.getCity() + ", " + sim.getState() + "\nStars:" + sim.getStars());
        similarEnt.setEditable(false);
        setContentPane(panel);
        // get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // get 2/3 of the height, and 2/3 of the width
        int height = screenSize.height * 2 / 3;
        int width = screenSize.width * 2 / 3;

        // set the jframe height and width
        setPreferredSize(new Dimension(width, height));
        pack();
    }

    public void businessSelected(Business b){
        reviews.setModel(new javax.swing.AbstractListModel<Review>() {
            ArrayList<Review> r = b.getReviews();
            public int getSize() { return r.size(); }
            public Review getElementAt(int i) { return r.get(i); }
        });
        clusteredEnt.setModel(new javax.swing.AbstractListModel<Business>(){
            Business[] l = bt.getCluster(b);
            public int getSize(){ return l.length; }
            public Business getElementAt(int i){ return l[i]; }

        });
        closestEnt.setModel(new javax.swing.AbstractListModel<Business>(){
            Business[] l = b.getClosest();
            public int getSize(){ return l.length; }
            public Business getElementAt(int i){ return l[i]; }

        });

        current.setText("Current: " + b.getName());
        Business sim = b.getSim(bt.getArray());
        similarEnt.setText(sim.getName() + "\n" + sim.getAddress() + "\n" +
                sim.getCity() + ", " + sim.getState() + "\nStars:" + sim.getStars());
    }

    public void clusteredSelected(Business b){
        reviews.setModel(new javax.swing.AbstractListModel<Review>() {
            ArrayList<Review> r = b.getReviews();
            public int getSize() { return r.size(); }
            public Review getElementAt(int i) { return r.get(i); }
        });
        clusteredEnt.setModel(new javax.swing.AbstractListModel<Business>(){
            Business[] l = bt.getCluster(b);
            public int getSize(){ return l.length; }
            public Business getElementAt(int i){ return l[i]; }

        });
        closestEnt.setModel(new javax.swing.AbstractListModel<Business>(){
            Business[] l = b.getClosest();
            public int getSize(){ return l.length; }
            public Business getElementAt(int i){ return l[i]; }

        });
        current.setText("Current: " + b.getName());
        Business sim = b.getSim(bt.getArray());
        similarEnt.setText(sim.getName() + "\n" + sim.getAddress() + "\n" +
                sim.getCity() + ", " + sim.getState() + "\nStars:" + sim.getStars());
    }

    public void closestSelected(Business b){
        reviews.setModel(new javax.swing.AbstractListModel<Review>() {
            ArrayList<Review> r = b.getReviews();
            public int getSize() { return r.size(); }
            public Review getElementAt(int i) { return r.get(i); }
        });
        clusteredEnt.setModel(new javax.swing.AbstractListModel<Business>(){
            Business[] l = bt.getCluster(b);
            public int getSize(){ return l.length; }
            public Business getElementAt(int i){ return l[i]; }

        });
        closestEnt.setModel(new javax.swing.AbstractListModel<Business>(){
            Business[] l = b.getClosest();
            public int getSize(){ return l.length; }
            public Business getElementAt(int i){ return l[i]; }
        });
        current.setText("Current: " + b.getName());
        Business sim = b.getSim(bt.getArray());
        similarEnt.setText(sim.getName() + "\n" + sim.getAddress() + "\n" +
                sim.getCity() + ", " + sim.getState() + "\nStars:" + sim.getStars());
    }

    public static void main(String[] args){
        new Main().setVisible(true);
        new GraphFrame(bt.getG()).setVisible(true);
    }
}
