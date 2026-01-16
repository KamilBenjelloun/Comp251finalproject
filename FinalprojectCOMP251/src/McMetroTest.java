import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class McMetroTest {

    @Test
    void testMaxPassengers1() {
        // Buildings
        BuildingID b1 = new BuildingID(1);
        BuildingID b2 = new BuildingID(2);
        BuildingID b3 = new BuildingID(3);
        BuildingID b4 = new BuildingID(4);
        BuildingID b5 = new BuildingID(5);
        BuildingID b6 = new BuildingID(6);

        Building[] buildings = new Building[]{
                new Building(b1, 10),
                new Building(b2, 10),
                new Building(b3, 10),
                new Building(b4, 10),
                new Building(b5, 10),
                new Building(b6, 10)
        };

        // Tracks
        Track[] tracks = new Track[]{
                new Track(new TrackID(1), b1, b2, 5, 3),
                new Track(new TrackID(2), b1, b4, 5, 4),
                new Track(new TrackID(3), b2, b4, 5, 2),
                new Track(new TrackID(4), b2, b3, 5, 1),
                new Track(new TrackID(5), b4, b3, 5, 4),
                new Track(new TrackID(6), b4, b5, 5, 2),
                new Track(new TrackID(7), b3, b6, 5, 3),
                new Track(new TrackID(8), b5, b6, 5, 2)
        };
        McMetro metro = new McMetro(tracks, buildings);
        int maxPassengers = metro.maxPassengers(b1, b6);
        assertEquals(5, maxPassengers);
    }

    @Test
    void testBestMetro() {
        // Buildings
        BuildingID b1 = new BuildingID(1);
        BuildingID b2 = new BuildingID(2);
        BuildingID b3 = new BuildingID(3);
        BuildingID b4 = new BuildingID(4);
        BuildingID b5 = new BuildingID(5);
        BuildingID b6 = new BuildingID(6);

        Building[] buildings = new Building[]{
                new Building(b1, 10),
                new Building(b2, 10),
                new Building(b3, 10),
                new Building(b4, 10),
                new Building(b5, 10),
                new Building(b6, 10)
        };

        // Tracks
        TrackID t1 = new TrackID(1);
        TrackID t2 = new TrackID(2);
        TrackID t3 = new TrackID(3);
        TrackID t4 = new TrackID(4);
        TrackID t5 = new TrackID(5);
        TrackID t6 = new TrackID(6);
        TrackID t7 = new TrackID(7);
        TrackID t8 = new TrackID(8);
        Track[] tracks = new Track[]{
                new Track(t1, b1, b2, 8, 5),
                new Track(t2, b1, b4, 7, 5),
                new Track(t3, b2, b4, 6, 5),
                new Track(t4, b2, b3, 1, 5),
                new Track(t5, b4, b3, 4, 5),
                new Track(t6, b4, b5, 3, 5),
                new Track(t7, b3, b6, 2, 5),
                new Track(t8, b5, b6, 5, 5)
        };
        McMetro metro = new McMetro(tracks, buildings);
        TrackID[] bms = metro.bestMetroSystem();
        TrackID[] expected = new TrackID[]{t4, t7, t5, t8, t2};
        String result1 = "";
        String result2 = "";
        for (int i=0; i<bms.length; i++){
            result1 += "["+bms[i]+"], ";
        }
        for (int i=0; i<expected.length; i++){
            result2 += "["+expected[i]+"], ";
        }
        System.out.println("Expected: "+result2);
        System.out.println("Actual: "+result1);
        assertArrayEquals(expected, bms);
    }

    @Test
    void testSearchForPassengers1() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis"};
        String[] expected = {"Al", "Alex", "Ally"};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("al");

        //printing arrays to compare
        String actualResult ="";
        for(int i=0; i<found.size(); i++){
            actualResult += found.get(i)+", ";
        }
        String expectedResult ="";
        for(int i=0; i<expected.length; i++){
            expectedResult += expected[i]+", ";
        }
        System.out.println("Extepted: " + expectedResult);
        System.out.println("Actual: " + actualResult);

        assertArrayEquals(expected, found.toArray(new String[0]));
    }
    @Test
    void testSearchForPassengers2() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis",};
        String[] expected = {"Bob", "Bobbert", "Bobby"};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("bob");

        //printing arrays to compare
        String actualResult ="";
        for(int i=0; i<found.size(); i++){
            actualResult += found.get(i)+", ";
        }
        String expectedResult ="";
        for(int i=0; i<expected.length; i++){
            expectedResult += expected[i]+", ";
        }
        System.out.println("Extepted: " + expectedResult);
        System.out.println("Actual: " + actualResult);

        assertArrayEquals(expected, found.toArray(new String[0]));

    }
    @Test
    void testSearchForPassengers3() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis",};
        String[] expected = {"Davis", "David", "Davie"};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("dav");

        //printing arrays to compare
        String actualResult ="";
        for(int i=0; i<found.size(); i++){
            actualResult += found.get(i)+", ";
        }
        String expectedResult ="";
        for(int i=0; i<expected.length; i++){
            expectedResult += expected[i]+", ";
        }
        System.out.println("Extepted: " + expectedResult);
        System.out.println("Actual: " + actualResult);

        assertArrayEquals(expected, found.toArray(new String[0]));
    }
    @Test
    void testSearchForPassengers4() {
        McMetro mcMetro = new McMetro(new Track[0], new Building[0]);
        String[] passengers = {
                "Alex", "Bob", "Ally", "al", "Bobby","bObbert", "David",
                "Davie", "Davis",};
        String[] expected = {};
        mcMetro.addPassengers(passengers);

        ArrayList<String> found = mcMetro.searchForPassengers("col");

        //printing arrays to compare
        String actualResult ="";
        for(int i=0; i<found.size(); i++){
            actualResult += found.get(i)+", ";
        }
        String expectedResult ="";
        for(int i=0; i<expected.length; i++){
            expectedResult += expected[i]+", ";
        }
        System.out.println("Extepted: " + expectedResult);
        System.out.println("Actual: " + actualResult);

        assertArrayEquals(expected, found.toArray(new String[0]));
    }

    @Test
    void testHireTicketCheckers1() {
        int[][] schedule = new int[][]{
                {1,2}, {2,3}, {3,4}, {1,3},
                {1,4}, {3,6}, {4,7}, {4,5},
                {5,6}, {4,8}, {2,4}, {6,7}
        };

        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(6, toHire);
    }

    @Test
    void testHireTicketCheckers2(){
        int[][] schedule = new int[][]{
                {0,2}, {1,3}, {2,5}, {4,6},
                {5,9}, {6,9}, {8,10}
        };

        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(3, toHire);
    }
    @Test
    void testHireTicketCheckers3(){
        int[][] schedule = new int[][]{{1,3}, {1,3}, {1,3}};
        int toHire = McMetro.hireTicketCheckers(schedule);
        assertEquals(1, toHire);
    }
}