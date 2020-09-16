import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author lou
 */
public class Graph implements Serializable{

    private class LinkedList implements Serializable{
        class Node implements Serializable{
            Business key;
            Node next;
            
            Node(Business key){
                this.key = key;
            }
            
            Node getNext(){
                return next;
            }
            
            
        }
        
        Node head;
        Node current;
        int count = 0;
        
        void add(Business b){
            Node curr = head;
            while(curr != null)curr = curr.next;
            curr = new Node(b);
            count++;
        }
        
        boolean contains(Business b){
            Node curr = head;
            while(curr != null){
                if(curr.key == b)return true;
                curr = curr.next;
            }
            return false;
        }
        
        int size(){
            return count;
        }
        
        Business getCurr(){
            if(current == null)current = head;
            return current.key;
        }
        
        boolean hasNext(){
            return (current.next != null);
        }
        
        Business getNext(){
            return current.next.key;
        }
        
    }
    
    private final Map<Business, LinkedList> map = new HashMap<>();
    
    
    public void addVertex(Business b){
        LinkedList ll = new LinkedList();
        //for(Business c : b.getClosest())ll.add(c);
        map.put(b, ll);
    }
    
    public void addEdge(Business src, Business des){
        if(!map.containsKey(src))addVertex(src);
        if(!map.containsKey(des))addVertex(des);
        
        map.get(src).add(des);
        
        boolean bidirectional = false;
        for(Business b : des.getClosest()){
            if(b == src)bidirectional = true;
        }
        if(bidirectional)map.get(des).add(src);
    }
    
    public int getVertexCount(){
        return map.keySet().size();
    }
    
    public void getEdgesCount(boolean bidirectional){
        int count = 0;
        for(Business b : map.keySet()){
            count += map.get(b).size();
        }if(bidirectional)count = count / 2;
        
    }
    
    public boolean hasVertex(Business b){
        return map.containsKey(b);
    }
    
    public boolean hasEdge(Business src, Business des){
        return map.get(src).contains(des);
    }
    
    public Business[] getGraph(){
        Business[] out = new Business[map.keySet().size()];
        int i = 0;
        for(Business b : map.keySet()){
            out[i++] = b;
        }
        return out;
    }
    
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        for(Business b : map.keySet()){
            str.append(b.toString() + ": ");
            LinkedList ll = map.get(b);
            Business e = ll.getCurr();
            while(ll.hasNext()){
                str.append(e.toString() + ", ");
                e = map.get(b).getNext();
            }
            str.append("\n");
        }
        
        return str.toString();
    }
}
