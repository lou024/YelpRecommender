import java.io.Serializable;

public class FrequencyTable implements Comparable<FrequencyTable>, Serializable{
    //Node class to store hashcodes
    //and keeps track of the frequency
    //Node may have 'next' only if a collision happens,
    //in that case the new Node will be added as 'next'
    private static class Node implements Serializable{
        String key;
        int frequency;
        Node next;

        Node(String key, Node next){
            this.key = key;
            this.next = next;
            this.frequency = 1;
        }

        public void incrementFreq(){
            frequency++;
        }
    }

    private Node[] Table;
    private int size = 8;
    private int count = 0;

    public FrequencyTable(){
        this.Table = new Node[size];
    }

    public boolean contains(String key){
        if(count == 0)return false;
        int h = hasher(key);
        int i = h & (Table.length - 1);
        if(Table[i] != null && key.equals(Table[i].key))return true;
        return false;
    }

    private int hasher(String entry){
        int hash = 0;
        for(int i = 0; i < entry.length(); i++){
            hash += entry.charAt(i);
            hash += hash << 17;
            hash ^= hash >> 9;

        }
        hash += hash << 15;
        hash ^= hash >> 9;
        hash += hash << 19;
        return (hash & 0x7FFFFFFF);
    }
    
    public int getSize(){
        return size;
    }

    private void resize(){
        Node[] old = Table;
        size = nextPwrOf2(size);
        Table = new Node[size];
        count = 0;
        for(int i = 0; i < old.length; i++){
            for(Node curr = old[i]; curr != null; curr = curr.next){
                add(curr.key);
            }
        }
    }
    
    public void addReview(Review r){
        for(String wrd : r.getReview().split(" ")){
            String temp = wrd.replaceAll("[^a-zA-Z]", "");
            add(temp);
        }
        
    }

    private boolean add(String word){
        //return false to check for collision
        //add key to table
        int index = (hasher(word) & Table.length - 1);
        if(contains(word)){
            Table[index].incrementFreq();
            return true;
        }
        if(Table[index] == null){
            Table[index] = new Node(word, null);
            count++;
            if(loadFactor() >= .75)resize();
            return true;
        }else{
            Node curr = Table[index];
            while(curr.next != null){
                curr = curr.next;
            }
            curr.next = new Node(word, null);
            count++;
            if(loadFactor() >= .75)resize();
            return false;
        }
    }

    private double loadFactor(){
        //get Collision probability
        return (double)count/size;
    }

    //returns next integer of power 2
    private int nextPwrOf2(int n){
        //decrement n to handle when n itself is pwr of 2
        //n = n - 1;

        while((n & n - 1) != 0){
            n = n & n - 1;
        }
        return n << 1;
    }
    
    @Override
    public int compareTo(FrequencyTable ft) {
        int contain = 0;
        for(int i = 0; i < Table.length; i++){
            for(Node curr = Table[i]; curr != null; curr = curr.next){
                if(ft.contains(curr.key))contain = contain + 1;
            }
        }
        return contain;
        
    }

    //Prints each element in Table with it's location,
    //Key, and Frequency
    public String toString(){
        String out = "Load Factor: " + loadFactor() + "\nSize: " + size + "\nCount: " + count;
        for(int i = 0; i < Table.length; i++){
            if(Table[i] != null){
                for(Node curr = Table[i]; curr != null; curr = curr.next){
                    System.out.println("Index: " + i + " Key: " + curr.key + " Freq: " + curr.frequency);
                }
            }
        }
        return out;
    }
}
