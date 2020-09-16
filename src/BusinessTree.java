import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BusinessTree implements Serializable{
    private final RedBlackTree bt;
    private ArrayList<Business> list;
    private final KMeans kmeans;
    private final Graph g;
    
    private int size;
            
    public BusinessTree(RedBlackTree bt, KMeans kmeans){
        this.bt = bt;
        list = bt.toArrayList();
        this.size = bt.count();
        this.kmeans = kmeans;
        setClosest();
        this.g = new Graph();
        createGraph();
    }
    
    public Graph getG(){
        return g;
    }
    
    private void createGraph(){
        for(Business b : list){
            for(Business x : b.getClosest())g.addEdge(b, x);
        }
    }
    
    public Business[] getCluster(Business b){
        return kmeans.getCluster(b);
    }
    
    public Business[] getAlphabetical(){
        Business[] out = (Business[])bt.toArrayList().toArray();
        Arrays.sort(out, new Comparator<Business>(){
            @Override
            public int compare(Business b1, Business b2){
                return b1.getName().compareTo(b2.getName());
            }
        });
        return out;
    }
    
    private boolean contains(ArrayList<Integer> takenIndexes, int ind){
        for(int i = 0; i < takenIndexes.size(); i++)if(takenIndexes.get(i) == ind)return true;
        return false;
    }
    
    public ArrayList<Business> getList(){
        return list;
    }
    
    public RedBlackTree getTree(){
        return bt;
    }
    
    public Business[] getArray(){
        return bt.toArray();
    }
    
    public int size(){
        return bt.count();
    }
    
    private void setClosest(){
        for(Business b1 : list){
            Business[] closest = new Business[4];
            double c0 = Double.MAX_VALUE, c1 = Double.MAX_VALUE,
                    c2 = Double.MAX_VALUE, c3 = Double.MAX_VALUE;
            for(Business b2 : list){
                if(b1 != b2){
                    
                    double temp = Haversine(b1.getLat(), b1.getLong(), b2.getLat(), b2.getLong());
                    if(temp < c0){
                        //if slot is taken push back the current business
                        //to next slot. Keeping array organized
                        if(closest[0] != null){
                            if(closest[1] != null){
                                if(closest[2] != null){
                                    c3 = c2;
                                    closest[3] = closest[2];
                                }
                                c2 = c1;
                                closest[2] = closest[1];
                            }
                            c1 = c0;
                            closest[1] = closest[0];
                        }
                        
                        c0 = temp;
                        closest[0] = b2;
                    }else if(temp < c1){
                        if(closest[1] != null){
                            if(closest[2] != null){
                                closest[3] = closest[2];
                                c3 = c2;
                            }
                            c2 = c1;
                            closest[2] = closest[1];
                        }
                        c1 = temp;
                        closest[1] = b2;
                    }else if(temp < c2){
                        if(closest[2] != null){
                            c3 = c2;
                            closest[3] = closest[2];
                        }
                        c2 = temp;
                        closest[2] = b2;
                    }else if(temp < c3){
                        c3 = temp;
                        closest[3] = b2;
                    }
                    
                }
            }
            b1.setClosest(closest);
        }
    }
    
    private double Haversine(double lat1, double long1, double lat2, double long2){
        //distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);
        
        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        
        double a = Math.pow(Math.sin(dLat/2), 2) + Math.pow(Math.sin(dLong/2), 2) *
                Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        
        return 6371 * c;
    }
    
    //Calculates the similarity (a number within 0 and 1) between two strings.
    public static double similarity(String str1, String str2) {
        String longer = str1, shorter = str2;
        if(str1.length() < str2.length()){ // longer should always have greater length
            longer = str2; 
            shorter = str1;
        }
        int longerLength = longer.length();
        if(longerLength == 0){
            return 1.0; // both strings are zero length
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    // Example implementation of the Levenshtein Edit Distance
    // http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2){
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for(int i = 0; i <= s1.length(); i++){
            int lastValue = i;
            for(int j = 0; j <= s2.length(); j++){
                if (i == 0)costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))newValue = Math.min(Math.min(newValue, lastValue),costs[j]) + 1;
                            costs[j - 1] = lastValue;
                            lastValue = newValue;
                    }
                }
            }
            if(i > 0)costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
    
}
