import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author lou
 */
public class Record implements Serializable{
    private final Business b;
    private final Map<String, Integer> attributes;
    
    public Record(Business b, Map<String, Integer> attributes){
        this.b = b;
        this.attributes = attributes;
    }
    
    public Business getBusiness(){
        return b;
    }
    
    public Map<String, Integer> getAttributes(){
        return attributes;
    }
    
}
