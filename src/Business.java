
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Business implements Comparable<Business>, Serializable{

    private String business_id, name, address, city, state;
    private double latitude, longitude;
    private ArrayList<String> attributes;
    private Map<String, Integer> mappedAttributes;
    private Business[] closest;
    double stars;
    int reviewCount;

    private ArrayList<Review> reviews = new ArrayList<Review>();
    private FrequencyTable ft = new FrequencyTable();

    //arraylist holding reviews to list
    public Business(String business_id, String name, String address, String city,
                    String state, double latitude, double longitude, double stars){
        this.business_id = business_id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stars = stars;
        this.reviewCount = 0;
        this.attributes = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public String getBusinessID(){
        return business_id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState(){
        return state;
    }
    
    public double getLat(){
        return latitude;
    }
    
    public double getLong(){
        return longitude;
    }
    
    public double getStars(){
        return stars;
    }
    
    public void addMappedAttributes(Map<String, Integer> mappedAttributes){
        this.mappedAttributes = mappedAttributes;
    }
    
    public void addAttributes(String[] categories){
        for(String attribute : categories){
            attributes.add(attribute.strip());
        }
    }
    
    public ArrayList<String> getAttributes(){
        return attributes;
    }
    
    public boolean addReview(Review r){
        if(r.getBusinessID().equals(business_id)){
            reviews.add(r);
            ft.addReview(r);
            reviewCount++;
            return true;
        }
        return false;
    }

    public int getReviewCount(){
        return reviewCount;
    }
    
    public FrequencyTable getFT(){
        return ft;
    }
    
    public Business compareNames(Business business, ArrayList<Business> bl){
        for(Business b : bl){
            if(similarity(b.getName(), business.getName()) >= .75){
                return b;
            }
        }
        for(Business b : bl){
            if(similarity(b.getName(), business.getName()) >= .5){
                return b;
            }
        }
        for(Business b : bl){
            if(similarity(b.getName(), business.getName()) >= .25){
                return b;
            }
        }
        for(Business b : bl){
            if(similarity(b.getName(), business.getName()) >= .15){
                return b;
            }
        }
        
        return bl.get(0);
    }
    
    public Business getSimStars(Business b, Business[] bl){
        Business out = b;
        ArrayList<Business> outArr = new ArrayList<>();
        for(int i = 0; i < bl.length; i++){
            if(bl[i] != b){
                double diff = b.getStars() - bl[i].getStars();
                //diff = (diff & 0x7FFFFFFF);
                if(diff >= -1 && diff <= 1){
                    outArr.add(bl[i]);
                }
            }
        }
        return compareNames(b, outArr);
    }
    
    public Business getSim(Business[] bl){
        if(reviewCount == 0)return getSimStars(this, bl);
        Business out = this;
        double high = 0;
        for(int i = 0; i < bl.length; i++){
            double temp = ft.compareTo(bl[i].getFT());
            if(this != bl[i] && temp > high){
                high = temp;
                out = bl[i];
            }
        }
        return out;
    }
    
    public Review getReview(String review_id){
        for(Review r : reviews){
            if(r != null && r.getReviewID().equals(review_id))return r;
        }
        return null;
    }
    
    public ArrayList<Review> getReviews(){
        return reviews;
    }
    
    public String toString(){
        return name + "; Location: " + address + ", " + city + ", " + state;
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
    
    public void setClosest(Business[] closest){
        this.closest = closest;
    }
    
    public Business[] getClosest(){
        return closest;
    }

    @Override
    public int compareTo(Business b) {
        return this.business_id.compareTo(b.getBusinessID());
    }
    
}
