import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONDataPuller {

    private static RedBlackTree businesses;
    private static int businessCount;
    private static ArrayList<String> allCategories;
    private static ArrayList<Business> tempList;

    public JSONDataPuller(){
        businesses = new RedBlackTree();
        allCategories = new ArrayList<>();
        tempList = new ArrayList<>();
        run();
    }
    
    private static void run(){

        String businessJSON = "./yelp_dataset/business.json";
        String reviewJSON = "./yelp_dataset/review.json";

        buildBussinessList(businessJSON);
        addReviewsToBusinesses(reviewJSON);
        addToBTree();
    }
    
    private static void addToBTree(){
        for(Business b : tempList)businesses.insert(b);
    }

    private static void buildBussinessList(String businessJSON){

        try{
            BufferedReader br = new BufferedReader(new FileReader(businessJSON));
            String line = "";
            String[] JSONinfo;
            int n = 0;
            while((line = br.readLine()) != null && n < 10000){
                //getting rid of curly brackets in beginning/end of JSONline
                //making parsing a bit simpler
                //splitting by commas unless commas are within quotations
                JSONinfo = line.substring(1, line.length()-1).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                //grabs unparsed categories for businesses
                //adding it to ArrayList<String> to keep track of all possible categories
                String[] categories = line.substring(line.indexOf("categories"), line.indexOf(",\"hours")).split(":");
                
                Business b = parseBusiness(JSONinfo, categories[1]);
                tempList.add(b);
                n++;
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static Business parseBusiness(String[] JSONinfo, String categories){
        /*
        * Sample JSON Array
        * ind: 0    business_id":"1SWheh84yJXfytovILXOAQ"
        * ind: 1    "name":"Arizona Biltmore Golf Club"
        * ind: 2    "address":"2818 E Camino Acequia Drive"
        * ind: 3    "city":"Phoenix"
        * ind: 4    "state":"AZ"
        * ind: 5    "postal_code":"85016"
        * ind: 6    "latitude":33.5221425
        * ind: 7    "longitude":-112.0184807
        * ind: 8    "stars":3.0
        * ind: 9    "review_count":5
        * ind: 10   "is_open":0
        * ind: 11   "attributes":{"GoodForKids":"False"}
        * ind: 12   "categories":"Golf
        * Active Life"
        * "hours":null
         */
        String business_id = "";
        String name = "";
        String address = "";
        String city = "";
        String state = "";
        double latitude = 0.0, longitude = 0.0;
        double stars = 0;
        for(int i = 0; i < 9; i++){
            
            if(i == 0){
                String[] temp = JSONinfo[i].split(":");
                business_id = temp[1].substring(1, temp[1].length()-1);
            }else if(i == 1){
                String[] temp = JSONinfo[i].split(":");
                name = temp[1].substring(1, temp[1].length()-1);
            }else if(i == 2){
                String[] temp = JSONinfo[i].split(":");
                address = temp[1].substring(1, temp[1].length()-1);
            }else if(i == 3){
                String[] temp = JSONinfo[i].split(":");
                city = temp[1].substring(1, temp[1].length()-1);
            }else if(i == 4){
                String[] temp = JSONinfo[i].split(":");
                state = temp[1].substring(1, temp[1].length()-1);
            }else if(i == 6){
                String[] temp = JSONinfo[i].split(":");
                latitude = Double.parseDouble(temp[1]);
            }else if(i == 7){
                String[] temp = JSONinfo[i].split(":");
                longitude = Double.parseDouble(temp[1]);
            }else if(i == 8){
                String[] temp = JSONinfo[i].split(":");
                stars = Double.parseDouble(temp[1]);
            }

        }

        Business b = new Business(business_id, name, address, city, state, latitude, longitude, stars);
        if(!categories.equals("null")){
            String[] temp = categories.substring(1, categories.length() - 1).split(",");
            b.addAttributes(temp);
            addToAllCategories(temp);
        }
        return b;

    }
    
    private static void addToAllCategories(String[] categories){
        for(String str : categories){
            if(!allCategories.contains(str.strip()))allCategories.add(str.strip());
        }
    }
    
    public static ArrayList<String> getCategories(){
        return allCategories;
    }

    public static void addReviewsToBusinesses(String reviewJSON){

        try{
            BufferedReader br = new BufferedReader(new FileReader(reviewJSON));
            String line = "";
            String[] JSONinfo;
            int n = 0;
            while((line = br.readLine()) != null && n < 30000){
                //getting rid of curly brackets in beginning/end of JSONline
                //making parsing a bit simpler
                //splitting by commas unless commas are within quotations
                JSONinfo = line.substring(1, line.length()-1).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if(parseReview(JSONinfo))n++;

            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private static boolean parseReview(String[] JSONinfo){
        String review_id = "", review = "", business_id = "", user_id = "";
        double stars = 0;

        if(JSONinfo.length == 1)JSONinfo = JSONinfo[0].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for(int i = 0; i < JSONinfo.length; i++){
            String[] temp = JSONinfo[i].split(":");

            if(JSONinfo[i].startsWith("\"review_id\":"))review_id = temp[1].substring(1, temp[1].length()-1);
            else if(JSONinfo[i].startsWith("\"business_id\":"))business_id = temp[1].substring(1, temp[1].length()-1);
            else if(JSONinfo[i].startsWith("\"stars\":"))stars = Double.parseDouble(temp[1]);
            else if(JSONinfo[i].startsWith("\"text\":")){
                review = temp[1];
                while(!JSONinfo[i].endsWith("\"")){
                    review = review.concat(JSONinfo[i+1]);
                    i++;
                }

            }
        }
        
        Review r = new Review(review_id, review, business_id, user_id, stars);
        for(Business b : tempList){
            if(b.getBusinessID().equals(r.getBusinessID())){
                b.addReview(r);
                return true;
            }
        }
        return false;
    }

    public static RedBlackTree getBusinesses(){
        return businesses;
    }

    private static boolean contains(String busID){
        return businesses.containsByID(busID);
    }

    private static Business getBusiness(String business_id){
        return businesses.getBusiness(business_id);
    }

    public static void printBusinesses(){
        businesses.print();
    }
}
