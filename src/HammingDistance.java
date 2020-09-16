import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author lou
 */
public class HammingDistance implements Distance, Serializable{

    @Override
    public int calculate(Map<String, Integer> a1, Map<String, Integer> a2) {
        int temp = 1;
        for(String s : a1.keySet()){
            Integer t1 = a1.get(s), t2 = a2.get(s);
            if(!t1.equals(t2))temp+=1;
        }
        return temp;
    }
    
}
