import java.util.*;

public class NaiveDisjointSet<T> {
    HashMap<T, T> parentMap = new HashMap<>();
    private HashMap<T,Integer> height = new HashMap<>();


    void add(T element) {
        parentMap.put(element, element);
        height.put(element,0);
    }
    T find(T a) {
        if (parentMap.get(a).equals(a)) {
            return a;
        }
        else{
         parentMap.put(a,find(parentMap.get(a)));
         return parentMap.get(a);
        }
    }

    void union(T a, T b) {
        if (!find(a).equals(find(b))){
            if (height.get(find(a)).equals(height.get(find(b)))) {
                parentMap.put(find(a), find(b));
                height.put(find(a), height.get(find(a)) + 1);
            } else if (height.get(find(a)) < height.get(find(b))) {
                parentMap.put(find(a), find(b));
            } else{
                parentMap.put(find(b), find(a));
            }

        }
    }


}
