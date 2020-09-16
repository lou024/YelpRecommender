import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GraphFrame extends JFrame{
    private JPanel panel;
    private JList<Business> businessList;
    private JList<Business> linkedBusinessesList;
    private JLabel linkedBusinesses;
    private JLabel businesses;
    private JLabel current;

    private Graph g;

    public GraphFrame(Graph g){
        super("Graph");
        this.g = g;

        businessList.setModel(new javax.swing.AbstractListModel<Business>() {
            Business[] l = g.getGraph();
            public int getSize() { return l.length; }
            public Business getElementAt(int i) { return l[i]; }
        });
        businessList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                businessSelected(businessList.getSelectedValue());
            }
        });
        businessList.setCellRenderer(new BusinessCellRenderer());

        linkedBusinessesList.setModel(new javax.swing.AbstractListModel<Business>() {
            Business[] l = g.getGraph()[0].getClosest();
            public int getSize() { return l.length; }
            public Business getElementAt(int i) { return l[i]; }
        });
        linkedBusinessesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                businessSelected(linkedBusinessesList.getSelectedValue());
            }
        });

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

    private void businessSelected(Business b){
        linkedBusinessesList.setModel(new javax.swing.AbstractListModel<Business>() {
            Business[] l = b.getClosest();
            public int getSize() { return l.length; }
            public Business getElementAt(int i) { return l[i]; }
        });
        current.setText("Current: " + b.getName());
    }
}
