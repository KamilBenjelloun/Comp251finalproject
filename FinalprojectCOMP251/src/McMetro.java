import java.util.*;
import java.lang.Math.*;

public class McMetro {
    protected Track[] tracks;
    protected HashMap<BuildingID, Building> buildingTable = new HashMap<>();
    private Queue<BuildingID> queue = new LinkedList<>();
    private HashMap<BuildingID, Boolean> visited = new HashMap<>();
    private HashMap<BuildingID, BuildingID> predecessor = new HashMap<>();
    private HashMap<TrackID, Integer> flows = new HashMap<>();
    private HashMap<TrackID, Boolean> track_available = new HashMap<>();
    private HashMap<BuildingID, HashSet<Track>> adjacencyList = new HashMap<>();
   private Trie tree = new Trie();
    private class Trie_node{
        private Map<Character, Trie_node> children;
        private boolean is_done;
        Trie_node() {
            children = new HashMap<>();
            is_done = false;
        }
    }
    private class Trie {
        Trie_node root;
        Trie(){
            this.root = new Trie_node();
        }
    }
    // You may initialize anything you need in the constructor
    McMetro(Track[] tracks, Building[] buildings) {
        this.tracks = tracks;

        // Populate buildings table
        for (Building building : buildings) {
            buildingTable.putIfAbsent(building.id(), building);
        }
    }
    private void residual_graph () {
        for (Track t : tracks) {
            int flow = flows.getOrDefault(t.id(),0);
            if (flow >= t.capacity()|| t.capacity() == 0){
                track_available.put(t.id(), false);
            }
        }
    }
    private boolean bfs (BuildingID start, BuildingID end) {
        visited.clear();
        predecessor.clear();
        queue.clear();
        for (BuildingID u : buildingTable.keySet()){
            visited.put(u, false);
            predecessor.put(u, null);
        }
        visited.put(start, true);
        queue.add(start);
        while (!queue.isEmpty()){
            BuildingID v = queue.poll();
            for (Track t : adjacencyList.get(v)) {
                if (track_available.get(t.id())) {
                    BuildingID neighbors = t.endBuildingId();
                    if (!visited.get(neighbors)) {
                        visited.put(neighbors, true);
                        predecessor.put(neighbors, v);
                        queue.add(neighbors);
                        if (neighbors.equals(end)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private Track findTrack(BuildingID start, BuildingID end) {
        for (Track t : tracks) {
            if (t.startBuildingId().equals(start) && t.endBuildingId().equals(end)) {
                return t;
            }
        }
        return null;
    }
    // Maximum number of passengers that can be transported from start to end
    int maxPassengers(BuildingID start, BuildingID end) {
        for (Building building : buildingTable.values()) {
            adjacencyList.put(building.id(), new HashSet<>());
        }
        for (Track t : tracks) {
            flows.putIfAbsent(t.id(),0);
            track_available.putIfAbsent(t.id(), t.capacity() > 0);
            adjacencyList.get(t.startBuildingId()).add(t);
        }
        int maxPass = 0;
        if (start.equals(end)) {
            for (Track t : tracks) {
                if (t.startBuildingId().equals(start) && t.endBuildingId().equals(end)) {
                    maxPass = Math.min(t.capacity(), Math.min(buildingTable.get(start).occupants(),buildingTable.get(end).occupants()));
                    break;
                }
            }
            return maxPass;
        }
        while (bfs(start, end)) {
            int Flow = Integer.MAX_VALUE;
            for (BuildingID v = end; !v.equals(start); v = predecessor.get(v)) {
                BuildingID u = predecessor.get(v);
                Track track = findTrack(u, v);
                if (track != null && track.capacity() - flows.get(track.id()) > 0) {
                    Flow = Math.min(Flow, track.capacity()-flows.get(track.id()));
                    int remainingStartOccupants = buildingTable.get(u).occupants();
                    int remainingEndOccupants = buildingTable.get(v).occupants();
                    Flow = Math.min(Flow, Math.min(remainingStartOccupants, remainingEndOccupants));

                }
            }
            if (Flow == 0) {
                break;
            }
            for (BuildingID v = end; !v.equals(start); v = predecessor.get(v)){
                BuildingID u = predecessor.get(v);
                Track forward = findTrack(u, v);
                Track backward = findTrack(v, u);
                if (forward != null) {
                    flows.put(forward.id(), flows.get(forward.id()) + Flow);
                }
                if (backward != null) {
                    flows.put(backward.id(), flows.get(backward.id()) - Flow);
                }
            }
            maxPass += Flow;
            residual_graph();
        }
        return maxPass;
    }

    private int goodness(Track t) {
        int min = Math.min(buildingTable.get(t.startBuildingId()).occupants(), Math.min(buildingTable.get(t.endBuildingId()).occupants(),t.capacity()));
        return min/t.cost();
    }
    // Returns a list of trackIDs that connect to every building maximizing total network capacity taking cost into account
    TrackID[] bestMetroSystem() {
        TrackID[] best = new TrackID[buildingTable.size() - 1];
        PriorityQueue<Track> pq = new PriorityQueue<>((a,b) -> goodness(b)-goodness(a));
        NaiveDisjointSet<BuildingID> disjointset = new NaiveDisjointSet<>();
        for (Track t : tracks) {
            pq.offer(t);
        }
        for (BuildingID buildingId : buildingTable.keySet()) {
            disjointset.add(buildingId);
        }
        int edge = 0;
        while (!pq.isEmpty() && edge < buildingTable.size() - 1) {
            Track current = pq.poll();
            BuildingID start = current.startBuildingId();
            BuildingID end = current.endBuildingId();
            if (!disjointset.find(start).equals(disjointset.find(end))) {
                best[edge] = current.id();
                disjointset.union(start, end);
                edge++;
            }
        }
        return best;
    }

    // Adds a passenger to the system
    void addPassenger(String name) {
        Trie_node current = tree.root;
        String lower = name.toLowerCase();
        for (char letter : lower.toCharArray()) {
            current.children.putIfAbsent(letter, new Trie_node());
            current = current.children.get(letter);
        }
        current.is_done = true;
    }

    // Do not change this
    void addPassengers(String[] names) {
        for (String s : names) {
            addPassenger(s);
        }
    }
    private void dfs(Trie_node node, String current, ArrayList<String> result) {
        if(node.is_done) {
            result.add(current);
        }
        for(char letter : node.children.keySet()) {
            dfs(node.children.get(letter),current+letter, result);
        }
    }

    // Returns all passengers in the system whose names start with firstLetters
    ArrayList<String> searchForPassengers(String firstLetters) {
        ArrayList<String> result = new ArrayList<>();
        Trie_node current = tree.root;
        String lower = firstLetters.toLowerCase();
        for (char letter : lower.toCharArray()) {
            if(!current.children.containsKey(letter)) {
                return result;
            }
            current = current.children.get(letter);
        }
        lower = Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
        dfs(current, lower, result);
        return result;
    }

    // Return how many ticket checkers will be hired
    static int hireTicketCheckers(int[][] schedule) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        for (int[] interval : schedule) {
            pq.offer(interval);
        }
        int counter = 0;
        int end = Integer.MIN_VALUE;
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            if (current[0] >= end) {
                counter++;
                end = current[1];
            }
        }
        return counter;
    }
}
