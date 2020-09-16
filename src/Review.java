import java.io.Serializable;

public class Review implements Serializable{

    // Column order:
    // 1)funny 2)useful 3)review_id 4)review 5)business_id 6)stars
    private String review_id, review, business_id, user_id;
    private double stars;

    public Review(String review_id, String review, String business_id, String user_id, double stars){
        this.review_id = review_id;
        this.review = review;
        this.business_id = business_id;
        this.user_id = user_id;
        this.stars = stars;
    }

    public String getReview(){
        return review;
    }
    public String getBusinessID() {
        return business_id;
    }
    
    public double getStars(){
        return stars;
    }

    public String toString(){
        return "business id: " + business_id + "\nreview id: " + review_id + "\nreview: " + review;
    }

    public String getReviewID(){
        return review_id;
    }
}
