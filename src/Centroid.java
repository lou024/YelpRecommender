import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author lou
 */
public class Centroid implements Serializable{
    private final Map<String, Integer> coordinates;
    
    public Centroid(Map<String, Integer> coordinates){
        this.coordinates = coordinates;
    }
    
    public Map<String, Integer> getCoordinates(){
        return coordinates;
    }
}
