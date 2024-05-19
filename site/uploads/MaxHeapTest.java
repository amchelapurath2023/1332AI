import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.NoSuchElementException;

/**
 * Grading JUnits for MaxHeap.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MaxHeapTest {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private static final long TIMEOUT = 1000;
    private static int points;
    private static MaxHeapTestUtil util;

    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    @Before
    public void setup() {
        util = new MaxHeapTestUtil();
    }

    @AfterClass
    public static void printResults() {
        System.out.println("Total Points: " + points);
    }

    //*************************************************************************
    //***********************   constructor (1 point)   ***********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void constructor01General() {
        util.constructorRunner();
        points += 1;
    }

    //*************************************************************************
    //***********************   buildHeap (19 points)   ***********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void buildHeap01NullData() {
        try {
            /*
                EMPTY

                (Attempt) Adding null
             */
            util.buildHeapNullData();
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void buildHeap02IncludeNullData() {
        try {
            /*
                EMPTY

                (Attempt) Adding 1, 2, null, 3
             */
            util.buildHeapIncludeNullData(1, 2, null, 3);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap03EmptyList() {
        /*
            EMPTY

            ==>

            EMPTY
         */
        util.buildHeapRunner(new int[]{}, new int[]{});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap04OneElement() {
        /*
            0

            ==>

            0
         */
        util.buildHeapRunner(new int[]{0}, new int[]{0});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap05NoHeapify() {
        /*
                 5
               /   \
              4     3
             / \   /
            2   1 0

            ==>

                 5
               /   \
              4     3
             / \   /
            2   1 0
        */
        util.buildHeapRunner(new int[]{5, 4, 3, 2, 1, 0},
                new int[]{5, 4, 3, 2, 1, 0});
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap06OneDeepHeapify() {
        /*
                 4
               /   \
              5     0
             / \   /
            3   2 1

            ==>

                 5
               /   \
              4     1
             / \   /
            3   2 0
        */
        util.buildHeapRunner(new int[]{5, 4, 1, 3, 2, 0},
                new int[]{4, 5, 0, 3, 2, 1});
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap07RootFullHeapify() {
        /*
                 0
               /   \
              5     4
             / \   /
            3   2 1

            ==>

                 5
               /   \
              3     4
             / \   /
            0   2 1
        */
        util.buildHeapRunner(new int[]{5, 3, 4, 0, 2, 1},
                new int[]{0, 5, 4, 3, 2, 1});
        points += 2;
    }

    // 3 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap08MiddleLevelsFullHeapify() {
        /*
                   8
                 /   \
                0     6
               / \   / \
              5   4 3   2
             / \
            1   7

            ==>

                   8
                 /   \
                7     6
               / \   / \
              5   4 3   2
             / \
            1   0
        */
        util.buildHeapRunner(new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0},
                new int[]{8, 0, 6, 5, 4, 3, 2, 1, 7});
        points += 3;
    }

    // 3 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap09AllHeapify() {
        /*
                   0
                 /   \
                1     2
               / \   / \
              3   4 5   6
             / \
            7   8

            ==>

                   8
                 /   \
                7     6
               / \   / \
              3   4 5   2
             / \
            1   0
        */
        util.buildHeapRunner(new int[]{8, 7, 6, 3, 4, 5, 2, 1, 0},
                new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        points += 3;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap10Size() {
        // Whether or not size was updated correctly
        MaxHeapTestUtil.checkPersistentCheck("buildHeap_size");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap11CompareTo() {
        // Whether or not compare to was used correctly
        MaxHeapTestUtil.checkPersistentCheck("buildHeap_compareTo");
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void buildHeap12Capacity() {
        // Whether or not the capacity was correct
        MaxHeapTestUtil.checkPersistentCheck("buildHeap_capacity");
        points += 2;
    }

    //*************************************************************************
    //**************************   add (20 points)   **************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void add01NullData() {
        /*
            EMPTY

            (Attempt) Adding null
         */
        try {
            util.addNullData(new int[0], 13);
        } catch (IllegalArgumentException e1) {
            try {
                /*
                       2
                     /   \
                    1     0

                    (Attempt) Adding null
                 */
                util.addNullData(new int[]{2, 1, 0}, 13);
            } catch (IllegalArgumentException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void add02Empty() {
        /*
            EMPTY

            =[Adding 0]=>

            0
         */
        util.addRunner(new int[]{0}, new int[0], 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void add03OneElement() {
        /*
            1

            =[Adding 0]=>

              1
             /
            0
        */
        util.addRunner(new int[]{1, 0}, new int[]{1}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void add04NoHeapify() {
        /*
                 6
               /   \
              5     4
             / \   /
            3   2 1

            =[Adding 0]=>

                 6
               /   \
              5     4
             / \   / \
            3   2 1   0
        */
        util.addRunner(new int[]{6, 5, 4, 3, 2, 1, 0},
                new int[]{6, 5, 4, 3, 2, 1}, 13, 13, 0);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add05FullHeapify() {
        /*
                   8
                 /   \
                7     6
               / \   / \
              5   4 3   2
             / \
            1   0

            =[Adding 9]=>

                   9
                 /   \
                8     6
               / \   / \
              5   7 3   2
             / \ /
            1  0 4
        */
        util.addRunner(new int[]{9, 8, 6, 5, 7, 3, 2, 1, 0, 4}, new int[]
                {8, 7, 6, 5, 4, 3, 2, 1, 0}, 13, 13, 9);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add06EarlyStop() {
        /*
                 6
               /   \
              4     3
             / \   /
            2   1 0

            =[Adding 5]=>

                 6
               /   \
              4     5
             / \   / \
            2   1 0   3
        */
        util.addRunner(new int[]{6, 4, 5, 2, 1, 0, 3},
                new int[]{6, 4, 3, 2, 1, 0}, 13, 13, 5);

        /*
               3
             /   \
            1     0

            =[Adding 2]=>

                 3
               /   \
              2     0
             /
            1
        */
        util.addRunner(new int[]{3, 2, 0, 1},
                new int[]{3, 1, 0}, 13, 13, 2);

        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add07FullNoResize() {
        /*
                      11
                   /       \
                 10          9
               /   \       /   \
              8     7     6     5
             / \   / \
            4   3 2   1

            =[Adding 0]=>

                      11
                   /       \
                 10          9
               /   \       /   \
              8     7     6     5
             / \   / \   /
            4   3 2   1 0
        */
        util.addRunner(new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
                new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, 13, 13, 0);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add08ResizeNoHeapify() {
        /*
                      12
                   /       \
                 11         10
               /   \       /   \
              9     8     7     6
             / \   / \   /
            5   4 3   2 1

            =[Adding 0]=>

                       12
                   /       \
                 11         10
               /   \       /   \
              9     8     7     6
             / \   / \   / \
            5   4 3   2 1   0
        */
        util.addRunner(new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
                new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, 26, 13, 0);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add09ResizeFullSwaps() {
        /*
                      11
                   /       \
                 10          9
               /   \       /   \
              8     7     6     5
             / \   / \   /
            4   3 2   1 0

            =[Adding 12]=>

                      12
                   /       \
                10          11
               /   \       /   \
              8     7     9     5
             / \   / \   / \
            4   3 2   1 0   6
        */
        util.addRunner(new int[]{12, 10, 11, 8, 7, 9, 5, 4, 3, 2, 1, 0, 6},
                new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, 26, 13, 12);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add10ResizeStopEarly() {
        /*
                      12
                   /       \
                 10          9
               /   \       /   \
              8     7     6     5
             / \   / \   /
            4   3 2   1 0

            =[Adding 11]=>

                      12
                   /       \
                 10         11
               /   \       /   \
              8     7     9     5
             / \   / \   / \
            4   3 2   1 0   6
        */
        util.addRunner(new int[]{12, 10, 11, 8, 7, 9, 5, 4, 3, 2, 1, 0, 6},
                new int[]{12, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, 26, 13, 11);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void add11ResizeTwice() {
        // Heap: [_, 25, 24, 23, 22, ..., 1] -> [_, 25, 24, ..., 0] + [_]x25
        // Not drawing it out, but basically no swaps, just capacity 26
        // array becomes capacity 52.

        // Adding 0
        int[] expectedData = new int[26];
        int[] actualData = new int[25];
        for (int i = 25; i > 0; --i) {
            expectedData[25 - i] = i;
            actualData[25 - i] = i;
        }
        expectedData[25] = 0;
        util.addRunner(expectedData, actualData, 52, 26, 0);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void add12Size() {
        // Whether or not size was updated correctly
        MaxHeapTestUtil.checkPersistentCheck("add_size");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void add13CompareTo() {
        // Whether or not compare to was used correctly
        MaxHeapTestUtil.checkPersistentCheck("add_compareTo");
        points += 1;
    }

    //*************************************************************************
    //************************    remove (20 points)   ************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void remove01Empty() {
        try {
            /*
                EMPTY

                (Attempt) Removing
             */
            util.removeEmpty(13);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove02OneElement() {
        /*
            0

            =[Removing 0]=>

            EMPTY
         */
        util.removeRunner(new int[0], new int[]{0}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove03NoHeapify() {
        /*
              2
             / \
            0   1

            =[Removing 2]=>

              1
             /
            0
        */
        util.removeRunner(new int[]{1, 0}, new int[]{2, 0, 1}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove04LeftFullHeapifyTwoChildEnd() {
        /*
                 6
               /   \
              5     4
             / \   / \
            3   2 1   0

            =[Removing 6]=>

                 5
               /   \
              3     4
             / \   /
            0   2 1
        */
        util.removeRunner(new int[]{5, 3, 4, 0, 2, 1},
                new int[]{6, 5, 4, 3, 2, 1, 0}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove05LeftFullHeapifyOneChildEnd() {
        /*
                   8
                 /   \
                7     6
               / \   / \
              5   4 3   2
             / \
            1   0

            =[Removing 8]=>

                   7
                 /   \
                5     6
               / \   / \
              1   4 3   2
             /
            0
        */
        util.removeRunner(new int[]{7, 5, 6, 1, 4, 3, 2, 0},
                new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove06LeftEarlyStopTwoChildEnd() {
        /*
                 6
               /   \
              5     4
             / \   / \
            0   2 1   3

            =[Removing 6]=>

                 5
               /   \
              3     4
             / \   /
            0   2 1
        */
        util.removeRunner(new int[]{5, 3, 4, 0, 2, 1},
                new int[]{6, 5, 4, 0, 2, 1, 3}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove07LeftEarlyStopOneChildEnd() {
        /*
                   8
                 /   \
                7     6
               / \   / \
              5   4 3   2
             / \
            0   1

            =[Removing 8]=>

                   7
                 /   \
                5     6
               / \   / \
              1   4 3   2
             /
            0
        */
        util.removeRunner(new int[]{7, 5, 6, 1, 4, 3, 2, 0},
                new int[]{8, 7, 6, 5, 4, 3, 2, 0, 1}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove08RightFullHeapify() {
        /*
                   8
                 /   \
                6     7
               / \   / \
              2   3 4   5
             / \
            0   1

            =[Removing 8]=>

                   7
                 /   \
                6     5
               / \   / \
              2   3 4   1
             /
            0
        */
        util.removeRunner(new int[]{7, 6, 5, 2, 3, 4, 1, 0},
                new int[]{8, 6, 7, 2, 3, 4, 5, 0, 1}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove09RightEarlyStopTwoChildEnd() {
        /*
                   8
                 /   \
                6     7
               / \   / \
              4   5 0   1
             / \
            2   3

            =[Removing 8]=>

                   7
                 /   \
                6     3
               / \   / \
              4   5 0   1
             /
            2
        */
        util.removeRunner(new int[]{7, 6, 3, 4, 5, 0, 1, 2},
                new int[]{8, 6, 7, 4, 5, 0, 1, 2, 3}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove10RightEarlyStopOneChildEnd() {
        /*
                   8
                 /   \
                6     7
               / \   / \
              4   5 0   1

            =[Removing 8]=>

                   7
                 /   \
                6     1
               / \   /
              4   5 0
        */
        util.removeRunner(new int[]{7, 6, 1, 4, 5, 0},
                new int[]{8, 6, 7, 4, 5, 0, 1}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove11MixedFullSwapsTwoChild() {
        /*
                   9
                 /   \
                8     7
               / \   / \
              6   5 4   3
             / \ /
            1  2 0

            =[Removing 9]=>

                   8
                 /   \
                6     7
               / \   / \
              2   5 4   3
             / \
            1   0
        */
        util.removeRunner(new int[]{8, 6, 7, 2, 5, 4, 3, 1, 0},
                new int[]{9, 8, 7, 6, 5, 4, 3, 1, 2, 0}, 13, 13, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove12MixedFullSwapsOneChild() {
        /*
                  10
                 /   \
                9     8
               / \   / \
              6   7 5   3
             / \ / \
            4  2 1  0

            =[Remove 10]=>

                   9
                 /   \
                7     8
               / \   / \
              6   1 5   3
             / \ /
            4  2 0
        */
        util.removeRunner(new int[]{9, 7, 8, 6, 1, 5, 3, 4, 2, 0},
                new int[]{10, 9, 8, 6, 7, 5, 3, 4, 2, 1, 0}, 13, 13, 0);
        points += 1;
    }


    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove13MixedEarlyStopLeftEnd() {
        /*
                 6
               /   \
              5     4
             / \   / \
            1   2 0   3

            =[Removing 6]=>

                 5
               /   \
              3     4
             / \   /
            1   2 0
        */
        util.removeRunner(new int[]{5, 3, 4, 1, 2, 0},
                new int[]{6, 5, 4, 1, 2, 0, 3}, 13, 13, 0);
        points += 1;
    }


    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove14MixedEarlyStopRightEnd() {
        /*
                 6
               /   \
              4     5
             / \   / \
            1   2 0   3

            =[Removing 6]=>

                 5
               /   \
              4     3
             / \   /
            1   2 0
        */
        util.removeRunner(new int[]{5, 4, 3, 1, 2, 0},
                new int[]{6, 4, 5, 1, 2, 0, 3}, 13, 13, 0);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void remove15General() {
        /*
                 6
               /   \
              3     5
             / \   /
            2   1 4

            =[Removing 6]=>

                 5
               /   \
              3     4
             / \
            2   1
         */
        util.removeRunner(new int[]{5, 3, 4, 2, 1},
                new int[]{6, 3, 5, 2, 1, 4}, 13, 13, 0);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove16ReturnValue() {
        // Whether or not the correct value was returned
        MaxHeapTestUtil.checkPersistentCheck("remove_returnValue");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove17NullSpot() {
        MaxHeapTestUtil.checkPersistentCheck("remove_nullSpot");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove18Size() {
        // Whether or not size was updated correctly
        MaxHeapTestUtil.checkPersistentCheck("remove_size");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove19CompareTo() {
        // Whether or not compare to was used correctly
        MaxHeapTestUtil.checkPersistentCheck("remove_compareTo");
        points += 1;
    }

    //*************************************************************************
    //*************************   getMax (5 points)   *************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void getMax01Empty() {
        try {
            /*
                EMPTY
             */
            util.getMaxEmpty(13);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void getMax02OneElement() {
        /*
            0

            Getting 0
         */
        util.getMaxRunner(new int[]{0}, 13);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void getMax03General() {
        /*
              2
             / \
            1   0

            Getting 2
        */
        util.getMaxRunner(new int[]{2, 1, 0}, 13);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void getMax04Unmodified() {
        // Whether or not the structure remained unchanged
        MaxHeapTestUtil.checkPersistentCheck("getMax_unmodified");
        points += 1;
    }

    //*************************************************************************
    //************************    isEmpty (5 points)   ************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty01Empty() {
        util.isEmptyRunner(new int[0], 13);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty02OneElement() {
        /*
            0
         */
        util.isEmptyRunner(new int[]{0}, 13);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty03General() {
        /*
              2
             / \
            1   0
        */
        util.isEmptyRunner(new int[]{2, 1, 0}, 13);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty04Unmodified() {
        // Whether or not the structure remained unchanged
        MaxHeapTestUtil.checkPersistentCheck("isEmpty_unmodified");
        points += 1;
    }

    //*************************************************************************
    //*************************    clear (5 points)   *************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void clear01Empty() {
        /*
            EMPTY

            =[Clear]=>

            EMPTY
         */
        util.clearRunner(new int[0], 13, 13, true);
        util.clearRunner(new int[0], 13, 13, false);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void clear02OneElement() {
        /*
            0

            =[Clear]=>

            EMPTY
         */
        util.clearRunner(new int[]{0}, 13, 13, true);
        util.clearRunner(new int[]{0}, 13, 13, false);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void clear03SizeCheckOnly() {
        /*
              2
             / \
            1   0

            =[Clear]=>

            EMPTY
        */
        util.clearRunner(new int[]{2, 1, 0}, 13, 13, true);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void clear04ArrayCheckOnly() {
        /*
              2
             / \
            1   0

            =[Clear]=>

            EMPTY
        */
        util.clearRunner(new int[]{2, 1, 0}, 13, 13, false);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void clear05LargerArray() {
        /*
              2
             / \
            1   0

            =[Clear]=>

            EMPTY
        */
        util.clearRunner(new int[]{2, 1, 0}, 13, 26, false);
        points += 1;
    }
}