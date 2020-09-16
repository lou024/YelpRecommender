import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Luis Estevez
 * 
 *   Properties of Red-Black Tree: 
 *1. Each node is either red or black.
 *2. The root is black. This rule is sometimes omitted. Since the root can always be changed from red to black, but not necessarily vice versa, this rule has little effect on analysis.
 *3. All leaves (NIL) are black.
 *4. If a node is red, then both its children are black.
 *5. Every path from a given node to any of its descendant NIL nodes goes through the same number of black nodes.
 * 
 *
 */
public class RedBlackTree implements Serializable{
    
    private class Node implements Serializable{
        Business key;
        Node left, right, parent;
        boolean red;
        
        Node(Business key){
            this.key = key;
        }
        
        Node getP(){
            return this.parent;
        }
        
        Node getU(){
            if(this.parent == null)return null;
            Node p = this.parent;
            Node g = p.getP();
            if(g == null)return null;
            
            if(g.left == p)return g.right;
            else return g.left;
        }
        
        Node getGP(){
            if(parent == null)return null;
            return this.parent.getP();
        }
    }
    
    private Node root;
    private int count = 0;
    
    public RedBlackTree(){
        root = null;
    }
    
    public RedBlackTree(Business key){
        root = new Node(key);
        root.red = false;
    }
    
    public int count(){
        return count;
    }
    
    private Node insert(Node curr, Node parent, Node n){
        if(curr == null){
            n.parent = parent;
            n.red = (parent != null);
            count++;
            
            return n;
        }
        
        if(curr.key.compareTo(n.key) >= 0){
            curr.left = insert(curr.left, curr, n);
        }else{
            curr.right = insert(curr.right, curr, n);
        }
        return curr;
    }
    
    public void insert(Business key){
        Node n = new Node(key);
        insert(root, null, n);
        repairTree(n);
        root = n;
        while(root.parent != null)root = root.parent;
    }
    
    private void repairTree(Node k){
        if(k == null)return;
        Node p = k.getP();
        //case 1: parent is black or null; no need to check
        //case 2: parent is red
        if(p != null && p.red){
            Node g = k.getGP();
            Node u = k.getU();
            
            //case 2a: s is black or null
            if(u == null || !u.red){
                if(g != null && g.left != null && g.left == p){
                    if(p.left == k){
                        if(g.parent != null && g.parent.right == g)g.parent.right = p;
                        else if(g.parent != null)g.parent.left = p;
                        
                        p.parent = g.parent;
                        g.parent = p;
                        
                        g.left = p.right;
                        if(g.left != null)g.left.parent = g;
                        p.right = g;
                        
                        g.red = true;
                        p.red = false;
                        //repairTree(p.parent);
                    }else{
                        if(g.parent != null && g.parent.right == g)g.parent.right = k;
                        else if(g.parent != null)g.parent.left = k;
                        
                        k.parent = g.parent;
                        g.parent = k;
                        p.parent = k;
                        
                        p.right = null;
                        g.left = null;
                        
                        k.right = g;
                        k.left = p;
                        
                        k.red = false;
                        g.red = true;
                        //repairTree(k.parent);
                    }
                //g.right must be equal to p
                }else if(g != null){
                    if(p.right != null && p.right == k){
                        if(g.parent != null && g.parent.right == g)g.parent.right = p;
                        else if(g.parent != null)g.parent.left = p;
                        
                        p.parent = g.parent;
                        g.parent = p;
                        
                        g.right = p.left;
                        if(g.right != null)g.right.parent = g;
                        p.left = g;
                        
                        g.red = true;
                        p.red = false;
                        //repairTree(p.parent);
                    }else{
                        if(g.parent != null && g.parent.right == g)g.parent.right = k;
                        else if(g.parent != null)g.parent.left = k;
                        
                        k.parent = g.parent;
                        p.parent = k;
                        g.parent = k;
                        
                        p.left = null;
                        g.right = null;
                        
                        k.right = p;
                        k.left = g;
                        //repairTree(k.parent);
                    }
                }
                //s cant be null so no need to check
                //case 2b: u.red == true
            }else{
                p.red = false;
                u.red = false;
                if(root != null && root != g){
                    g.red = true;
                }
            }
            
        }
    }
    
    private boolean contains(Node curr, Business key){
        if(curr == null)return false;
        if(curr.key == key)return true;
        if(contains(curr.left, key))return true;
        return contains(curr.right, key);
    }
    
    public boolean contains(Business key){
        return contains(root, key);
    }
    
    private boolean containsByID(Node curr, String busID){
        if(curr == null)return false;
        int comp = curr.key.getBusinessID().compareTo(busID);
        if(comp == 0)return true;
        if(comp < 0)return containsByID(curr.left, busID);
        return containsByID(curr.right, busID);
    }
    
    public boolean containsByID(String busID){
        return containsByID(root, busID);
    }
    
    private Business getBusiness(Node curr, String busID){
        if(curr != null){
            int comp = curr.key.getBusinessID().compareTo(busID);
            if(comp < 0)return getBusiness(curr.left, busID);
            if(comp == 0)return curr.key;
            return getBusiness(curr.right, busID);
        }
        return null;
    }
    
    public Business getBusiness(String busID){
        return getBusiness(root, busID);
    }
    
    public boolean addReview(Review r){
        Business b = getBusiness(r.getBusinessID());
        if(b == null)return false;
        b.addReview(r);
        return true;
    }
    
    private void print(Node curr){
        if(curr != null){
            print(curr.left);
            System.out.println(curr.key.getName());
            System.out.println(curr.key.getAddress());
            System.out.println(curr.key.getCity() + ", " + curr.key.getState() + "\n");
            print(curr.right);
        }
    }
    
    public void print(){
        System.out.println("Root: " + root.key.toString() + "\n");
        print(root);
    }
    
    public ArrayList<Business> toArrayList(){
        ArrayList<Business> out = new ArrayList<>();
        addToList(out, root);
        return out;
    }
    
    private void addToList(ArrayList<Business> list, Node curr){
        if(curr != null){
            addToList(list, curr.left);
            list.add(curr.key);
            addToList(list, curr.right);
        }
    }
    
    public Business[] toArray(){
        ArrayList<Business> temp = toArrayList();
        Business[] out = new Business[temp.size()];
        int i = 0;
        for(Business b : temp){
            out[i++] = b;
        }
        return out;
        
    }
    
}
