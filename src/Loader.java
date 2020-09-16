import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author lou
 */
public class Loader {
    BusinessTree BL = null;
    
    public boolean save(BusinessTree bl){
        try{
            FileOutputStream fout = new FileOutputStream("bl.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(bl);
            oos.flush();
            oos.close();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean load(){
        try{
            FileInputStream fis = new FileInputStream("bl.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            BL = (BusinessTree) ois.readObject();
            ois.close();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    public BusinessTree getBL(){
        return BL;
    }
}
