import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Luis Estevez
 */
public class ReviewCellRenderer extends JLabel implements ListCellRenderer {
  private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

  public ReviewCellRenderer() {
    setOpaque(true);
    setFont(new java.awt.Font("Arial", 0, 12));
    
  }

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
    Review entry = (Review) value;
    setText("<html><body style='width: 175px'><br>Review: " + entry.getReview() + "<br>Stars: " + entry.getStars() + "<br></html>");
    if (isSelected) {
      setBackground(HIGHLIGHT_COLOR);
      setForeground(Color.white);
    } else {
      setBackground(Color.white);
      setForeground(Color.black);
    }
    return this;
  }
}
