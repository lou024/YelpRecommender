import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author lou
 */
public class KMeans implements Serializable{
    private final Random random = new Random();
    private final ArrayList<Business> bl;
    private final ArrayList<String> allAttributes;
    private final Map<Centroid, List<Record>> clusters;
    
    public KMeans(RedBlackTree bt, ArrayList<String> allAttributes){
        this.bl = bt.toArrayList();
        this.allAttributes = allAttributes;
        System.out.println("Clustering...");
        this.clusters = init();
        System.out.println("Clustering complete!");
    }
    
    public ArrayList<Business> getBL(){
        return bl;
    }
    
    private Business[] toArray(List<Record> records){
        Business[] businesses = new Business[records.size()];
        for(int i = 0; i < records.size(); i++){
            businesses[i] = records.get(i).getBusiness();
        }
        return businesses;
    }
    public Business[] getCluster(Business b){
        for(List<Record> records : clusters.values()){
            for(Record r : records){
                if(r.getBusiness().equals(b))return toArray(records);
            }
        }
        //return empty array if business is not clustered
        //which is bad
        Business[] temp = {};
        return temp;
    }
    
    public Map<Centroid, List<Record>> init(){
        List<Record> records = buildDataset(bl);
        Map<Centroid, List<Record>> clusters = fit(records, 10, new HammingDistance(), 75);
        return clusters;
    }
    
    private List<Record> buildDataset(List<Business> buisinesses){
        List<Record> records = new ArrayList<>();
        
        for(Business b : buisinesses){
            ArrayList<String> attributes = b.getAttributes();
            Map<String, Integer> map = new HashMap<>();
            for(String attribute : allAttributes){
                if(attributes.contains(attribute))map.put(attribute, 1);
                else map.put(attribute, 0);
            }
            b.addMappedAttributes(map);
            records.add(new Record(b, map));
        }
        return records;
    }
    
    private List<Centroid> randomCentroids(List<Record> records, int k){
        
        List<Centroid> centroids = new ArrayList<>();
        
        for(int i = 0; i < k; i++){
            Map<String, Integer> coordinates = records.get(random.nextInt(records.size())).getAttributes();
            centroids.add(new Centroid(coordinates));
        }
        return centroids;
    }
    
    private Centroid nearestCentroid(Record record, List<Centroid> centroids, Distance distance){
        int minDistance = Integer.MAX_VALUE;
        Centroid nearest = null;
        
        for(Centroid c : centroids){
            int currDistance = distance.calculate(record.getAttributes(), c.getCoordinates());
            if(currDistance < minDistance){
                minDistance = currDistance;
                nearest = c;
            }
        }
        return nearest;
    }
    
    private void assignToCluster(Map<Centroid, List<Record>> clusters,
            Record record, Centroid centroid){
        clusters.compute(centroid, (key, list) -> {
            if(list == null)list = new ArrayList<>();
            list.add(record);
            return list;
        });
    }
    
    private List<Centroid> relocateCentroids(Map<Centroid, List<Record>> clusters){
        List<Centroid> centroids = new ArrayList<>();
        for(List<Record> records : clusters.values()){
            Map<String, Integer> coordinates = records.get(random.nextInt(records.size())).getAttributes();
            centroids.add(new Centroid(coordinates));
        }
        return centroids;
    }
    
    public Map<Centroid, List<Record>> fit(List<Record> records, int k,
            Distance d, int maxIterations){
        
        List<Centroid> centroids = randomCentroids(records, k);
        Map<Centroid, List<Record>> clusters = new HashMap<>();
        Map<Centroid, List<Record>> lastState = new HashMap<>();
        
        //iterate for a pre-defined number of times
        for(int i = 0; i < maxIterations; i++){
            boolean isLast = (i == maxIterations - 1);
            
            //find nearest centroid for each record
            for(Record record : records){
                Centroid nearest = nearestCentroid(record, centroids, d);
                assignToCluster(clusters, record, nearest);
                
            }
            
            //if assignments haven't changed since last iteration then terminate
            boolean end = isLast || clusters.equals(lastState);
            lastState = clusters;
            if(end)break;
            
            //end of each iteration we rearrange centroids
            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }
        return lastState;
    }
    
}
