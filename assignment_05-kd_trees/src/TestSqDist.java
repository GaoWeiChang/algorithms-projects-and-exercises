public class TestSqDist {

    public static void main(String[] args) {
        double[] p1 = {-1, 1};
        double[] p2 = {1, -1};
        double result1 = KDTree.sqDist(p1, p2);
        System.out.println("Test 1: sqDist(p1, p2) = " + result1);
        assert Math.abs(result1 - 8.0) < 0.001 : "Expected: 8.0, Got: " + result1;
        
        double[] p3 = {0, 0};
        double[] p4 = {3, 4};
        double result2 = KDTree.sqDist(p3, p4);
        System.out.println("Test 2: sqDist(p3, p4) = " + result2);
        assert Math.abs(result2 - 25.0) < 0.001 : "Expected: 25.0, Got: " + result2;
        
        double[] p5 = {5, 7, 9};
        double result3 = KDTree.sqDist(p5, p5);
        System.out.println("Test 3: sqDist(p5, p5) = " + result3);
        assert Math.abs(result3 - 0.0) < 0.001 : "Expected: 0.0, Got: " + result3;
        
        double[] p6 = {1, 2, 3};
        double[] p7 = {4, 5, 6};
        double result4 = KDTree.sqDist(p6, p7);
        double expected4 = 9 + 9 + 9; // (4-1)² + (5-2)² + (6-3)²
        System.out.println("Test 4: sqDist(p6, p7) = " + result4);
        assert Math.abs(result4 - expected4) < 0.001 : "Expected: 27.0, Got: " + result4;
        
        double[] p8 = {5};
        double[] p9 = {10};
        double result5 = KDTree.sqDist(p8, p9);
        System.out.println("Test 5: sqDist(p8, p9) = " + result5);
        assert Math.abs(result5 - 25.0) < 0.001 : "Expected: 25.0, Got: " + result5;
        
        double[] p10 = {1, 2, 3, 4, 5};
        double[] p11 = {6, 7, 8, 9, 10};
        double result6 = KDTree.sqDist(p10, p11);
        double expected6 = 25 + 25 + 25 + 25 + 25; // (6-1)² + (7-2)² + (8-3)² + (9-4)² + (10-5)²
        System.out.println("Test 6: sqDist(p10, p11) = " + result6);
        assert Math.abs(result6 - expected6) < 0.001 : "Expected: 125.0, Got: " + result6;
        
        double[] p12 = {};
        double[] p13 = {};
        double result7 = KDTree.sqDist(p12, p13);
        System.out.println("Test 7: sqDist(p12, p13) = " + result7);
        assert Math.abs(result7 - 0.0) < 0.001 : "Expected: 0.0, Got: " + result7;
        
        System.out.println("[OK]");
    }
}