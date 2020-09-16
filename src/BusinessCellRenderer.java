import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Luis Estevez
 */
public class BusinessCellRenderer extends JLabel implements ListCellRenderer {
  private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

  public BusinessCellRenderer() {
    setOpaque(true);
    setFont(new java.awt.Font("Arial", 0, 12));
  }

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
    Business entry = (Business) value;
    setText("<html><body style='width: 200px'><br>" + entry.getName() + "<br>Stars: " + entry.getStars() + "<br>" + entry.getCity() + ", " + entry.getState() + "</html>");
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