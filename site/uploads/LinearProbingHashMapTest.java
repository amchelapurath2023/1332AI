import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.NoSuchElementException;

/**
 * Grading JUnits for ArrayList.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LinearProbingHashMapTest {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private static final long TIMEOUT = 1000;
    private static int points;
    private static LinearProbingHashMapTestUtil util;

    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    @Before
    public void setup() {
        util = new LinearProbingHashMapTestUtil();
    }

    @AfterClass
    public static void printResults() {
        System.out.println("Total Points: " + points);
    }

    //*************************************************************************
    //**********************    constructor (3 points)   **********************
    //*************************************************************************

    // 3 point(s)
    @Test(timeout = TIMEOUT)
    public void constructor01General() {
        util.constructorRunner(17);
        points += 3;
    }

    //*************************************************************************
    //**************************   put (17 points)   **************************
    //*************************************************************************

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void put01NullDataException() {
        try {
            /*
                [_, _, _, _, _, _, _, _, _, _, _, _, _]
             */
            util.putNullData("");
        } catch (IllegalArgumentException e1) {
            try {
                /*
                    [0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]
                 */
                util.putNullData("[0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]");
            } catch (IllegalArgumentException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put02Empty() {
        /*
            [_, _, _, _, _, _, _, _, _, _, _, _, _]

            =[Adding (5, 5)]=>

            [null, null, null, null, null, (5, 5), null, null, null, null,
            null, null, null]
         */
        util.putRunner("[_, _, _, _, _, 5, _, _, _, _, _, _, _]", "[_, _, _, "
                        + "_, _, _, _, _, _, _, _, _, _]", new String[] {"5"},
                new Integer[] {5});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put03NegativeHash() {
        /*
            [_, (-1, -1), _, _, _, _, (-6, -6), _, (-8, -8), _, _, _,
             (-25, -25)]

            =[Adding (-23, -23), (MIN, MIN)]=>

            [_, (-1, -1), _, _, _, _, (-6, -6), _, (-8, -8), _, (-23, -23),
            (MIN, MIN), (-25, -25)]
         */
        util.putRunner("[_, -1, _, _, _, _, -6, _, -8, _, -23, MIN, -25]",
                "[_, -1, _, _, _, _, -6, _, -8, _, _, _, -25]", new String[]
                        {"-23", "MIN"}, new Integer[] {10, 11});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put04ProbingNoDEL() {
        /*
            [_, (1, 1), _, _, _, _, (6, 6), _, (8, 8), _, _, (11, 11), (24, 25)]

             =[Adding (19, 19), (24, 24)]=>

             [_, (1, 1), _, _, _, _, (6, 6), (19, 19), (8, 8), _, _, (11, 11),
              (24, 24)]
         */
        util.putRunner("[_, 1, _, _, _, _, 6, 19, 8, _, _, 11, 24]", "[_, 1, "
                + "_, _, _, _, 6, _, 8, _, _, 11, (24,23)]", new String[] {
                "19", "24"}, new Integer[] {7, 12});
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void put05ProbingDEL() {
        /*
            [_, (1, 1), x(14, 14), x(27, 26), (40, 40), _, (6, 6), _, (8, 8),
             _, _, x(11, 11), (24, 23)

            =[Adding (53, 53), (27, 27), (24, 24)]=>

            [_, (1, 1), (53, 53), (27, 27), (40, 40), _, (6, 6), _, (8, 8),
            _, _, x(11, 11), (24, 24)]
         */
        util.putRunner("[_, 1, 53, 27, 40, _, 6, _, 8, _, _, x11, 24]",
                "[_, 1, x14, x(27,26), 40, _, 6, _, 8, _, _, x11, (24,23)]",
                new String[] {"53", "27", "24"}, new Integer[] {2, 3, 12});
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void put06ProbingWraparound() {
        /*
            [(37, 37), (50, 49), _, _, _, _, _, _, _, _, _, (11, 11), (24, 24)]

            =[Adding (63, 63), (50, 50)]=>

            [(37, 37), (50, 50), (63, 63), _, _, _, _, _, _, _, _, (11, 11),
            (24, 24)]
         */
        util.putRunner("[37, 50, 63, _, _, _, _, _, _, _, _, 11, 24]", "[37, "
                        + "(50,49), _, _, _, _, _, _, _, _, _, 11, 24]",
                new String[] {"63", "50"}, new Integer[] {2, 1});

        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]

            =[Adding (40, 40)]=>

            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, (40, 40), _, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.putRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, _, _, _, _, 40, _, _, _, "
                        + "_, _, _, _, _, _, _, _, _, _]", "[0, 1, 2, 3, 4, 5, 6, 7, 8, "
                        + "_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]",
                new String[] {"40"}, new Integer[] {13});

        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put07NoNulls() {
        /*
            [x(0, 0), x(1, 1), x(2, 2), x(3, 3), x(4, 4), x(5, 5), x(6, 6), x
            (7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]

            =[Adding (15, 15)]=>

            [x(0, 0), x(1, 1), (15, 15), x(3, 3), x(4, 4), x(5, 5), x(6, 6),
            x(7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]
         */
        util.putRunner("[x0, x1, 15, x3, x4, x5, x6, x7, x8, x9, x10, x11, "
                + "x12]", "[x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, "
                + "x12]", new String[] {"15"}, new Integer[] {2});

        /*
            [x(0, 0), x(1, 1), x(2, 2), x(3, 3), x(4, 4), (53, 53), x(6, 6), x
            (7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]

            =[Adding (53, 53)]=>

            [x(0, 0), x(1, 1), x(2, 2), x(3, 3), x(4, 4), (53, 53), x(6, 6),
            x(7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]
         */
        util.putRunner("[x0, x1, x2, x3, x4, 53, x6, x7, x8, x9, x10, x11, "
                + "x12]", "[x0, x1, x2, x3, x4, 53, x6, x7, x8, x9, x10, x11, "
                + "x12]", new String[] {"53"}, new Integer[] {5});

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put08DelKey() {
        /*
            [x(0, 0), _, _, _, _, _, _, _, _, _, _, _, _]

            =[Adding (0, 0)]=>

            [(0, 0), _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.putRunner("[0, _, _, _, _, _, _, _, _, _, _, _, _]", "[x0, _, _,"
                        + " _, _, _, _, _, _, _, _, _, _]", new String[] {"0"},
                new Integer[] {0});

        /*
            [(0, 0), x(13, 13), _, _, _, _, _, _, _, _, _, _, _]

            =[Adding (13, 13)]=>

            [(0, 0), (13, 13), _, _, _, _, _, _, _, _, _, _, _]
         */
        util.putRunner("[0, 13, _, _, _, _, _, _, _, _, _, _, _]", "[0, x13, _,"
                        + " _, _, _, _, _, _, _, _, _, _]", new String[] {"13"},
                new Integer[] {1});

        /*
            [x(0, 0), x(13, 13), _, _, _, _, _, _, _, _, _, _, _]

            =[Adding (13, 13)]=>

            [(13, 13), x(13, 13), _, _, _, _, _, _, _, _, _, _, _]
         */
        util.putRunner("[13, x13, _, _, _, _, _, _, _, _, _, _, _]", "[x0, "
                        + "x13, _,  _, _, _, _, _, _, _, _, _, _]", new String[] {"13"},
                new Integer[] {0});

        /*
            [(0, 0), x(13, 13), x(26, 26), _, (4, 4), (5, 5), _, _, _, _, _, _, _]

            =[Adding (26, 26)]=>

            [(0, 0), (26, 26), x(26, 26), _, (4, 4), (5, 5), _, _, _, _, _, _, _]
         */
        util.putRunner("[0, 26, x26, _, 4, 5, _, _, _, _, _, _, _]",
                "[0, x13, x26, _, 4, 5, _, _, _, _, _, _, _]",
                new String[] {"26"}, new Integer[] {1});

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put09AddToLFNoResize() {
        util.putRunner("[0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]", "[0, 1, 2, "
                        + "3, 4, 5, 6, _, _, _, _, _, _]", new String[] {"7"},
                new Integer[] {7});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put10ResizeSimple() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            _, _, _, _, _]

            =[Adding (8, 8)]=>

            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.putRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, _, _, _, _, _, _, _, _, "
                + "_, _, _, _, _, _, _, _, _, _]", "[0, 1, 2, 3, 4, 5, 6, 7, _, "
                + "_, _, _, _]", new String[] {"8"}, new Integer[] {null});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put11ResizeRehashing() {
        util.putRunner("[26, 1, _, 3, _, 5, _, _, _, _, _, _, _, 13, _, 15, "
                        + "_, 17, _, 19, _, _, _, _, _, _, 53]", "[13, 53, 15, 3, 17, "
                        + "5, 19, 26, _, _, _, _, _]", new String[] {"1"},
                new Integer[] {null});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put12ResizeDELFlags() {
        /*
            [(0, 0), (1, 1), x(2, 2), (3, 3), x(4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), (9, 9), _, _, _]

            =[Adding (10, 10)]=>

            [(0, 0), (1, 1), _, (3, 3), _, (5, 5), (6, 6), (7, 7), (8, 8),
            (9, 9), (10, 10), _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.putRunner("[0, 1, _, 3, _, 5, 6, 7, 8, 9, 10, _, _, _, _, _, _, "
                        + "_, _, _, _, _, _, _, _, _, _]", "[0, 1, x2,"
                        + " 3, x4, 5, 6, 7, 8, 9, _, _, _]", new String[] {"(10,10)"},
                new Integer[] {null});
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void put13ResizeTwice() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), (9, 9), (10, 10), (11, 11), (12, 12), (13, 13), (14, 14),
            (15, 15), (16, 16), (17, 17), _, _, _, _, _, _, _, _, _]

            =[Adding (18, 18)]=>

            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), (9, 9), (10, 10), (11, 11), (12, 12), (13, 13), (14, 14),
             (15, 15), (16, 16), (17, 17), (18, 18), _, _, _, _, _, _, _, _,
             _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _,
             _, _, _, _, _, _, _]
         */
        util.putRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, "
                        + "15, 16, 17, 18, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, "
                        + "_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]",
                "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, "
                        + "17, _, _, _, _, _, _, _, _, _]", new String[] {"18"},
                new Integer[] {null});
        points += 1;
    }

    @Test(timeout = TIMEOUT)
    public void put14Return() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("put_return");
        points += 1;
    }

    @Test(timeout = TIMEOUT)
    public void put15EqualsAndSize() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("put_size");
        LinearProbingHashMapTestUtil.checkPersistentCheck("put_equals");
        points += 1;
    }

    //*************************************************************************
    //************************    remove (10 points)   ************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void remove01NullDataException() {
        try {
            /*
                [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7,
                7), _, _, _, _, _]

                (Attempt) Removing null
             */
            util.removeNullData("[0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]");
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void remove02NoSuchElementExceptionEmpty() {
        try {
            /*
                [_, _, _, _, _, _, _, _, _, _, _, _, _]

                (Attempt) Removing 0
             */
            util.removeNoSuchElement("[_, _, _, _, _, _, _, _, _, _, _, _, "
                    + "_]", 0);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void remove03NoSuchElementExceptionFoundKeyRemoved() {
        try {
            /*
                [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8,
                 8), _, _, x(11, 11), (24, 24)]

                 (Attempt) Removing 27
             */
            util.removeNoSuchElement("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, "
                    + "x11, 24]", 27);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void remove04NoSuchElementExceptionNoNulls() {
        try {
            /*
                [x(0, 0), x(1, 1), x(2, 2), x(3, 3), x(4, 4), x(5, 5), x(6,
                6), x(7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]

                (Attempt) Removing 13
             */
            util.removeNoSuchElement("[x0, x1, x2, x3, x4, x5, x6, x7, x8, "
                    + "x9, x10, x11, x12]", 13);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void remove05NoSuchElementProbingDELNotInMap() {
        try {
            /*
                [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _,
                (8, 8), _, _, x(11, 11), (24, 24)]

                (Attempt) Removing 37
             */
            util.removeNoSuchElement("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, "
                    + "x11, 24]", 37);
        } catch (NoSuchElementException e1) {
            try {
                /*
                    [(37, 37), x(50, 50), _, _, _, _, _, _, _, _, _,
                    (11,11), (24, 24)]

                    (Attempt) Removing 63
                 */
                util.removeNoSuchElement("[37, x50, _, _, _, _, _, _, _, _, "
                        + "_, 11, 24]", 63);
            } catch (NoSuchElementException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove06NegativeHash() {
        /*
            [_, (-1, -1), _, _, _, _, (-6, -6), _, (-8, -8), _, _,
            (MIN, MIN), (-25, -25)]

            =[Removing MIN, -6]=>

            [_, (-1, -1), _, _, _, _, x(-6, -6), _, (-8, -8), _, _,
            x(MIN, MIN), (-25, -25)]
         */
        util.removeRunner("[_, -1, _, _, _, _, x-6, _, -8, _, _, xMIN, -25]",
                "[_, -1, _, _, _, _, -6, _, -8, _, _, MIN, -25]", 11, 6);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove07ProbingNoDELWraparound() {
        /*
            [(37, 37), (50, 50), _, _, _, _, _, _, _, _, _, (11, 11), (24, 24)]

            =[Removing 50]=>

            [(37, 37), x(50, 50), _, _, _, _, _, _, _, _, _, (11, 11), (24, 24)]
         */
        util.removeRunner("[37, x50, _, _, _, _, _, _, _, _, _, 11, 24]",
                "[37, 50, _, _, _, _, _, _, _, _, _, 11, 24]", 1);

        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, (40, 40), _, _, _, _, _, _, _, _, _, _, _, _, _]

            =[Removing 40]=>

            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, x(40, 40), _, _, _, _, _, _, _, _, _, _, _,
            _, _]
         */
        util.removeRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, _, _, _, _, x40, _, _, "
                + "_, _, _, _, _, _, _, _, _, _, _]", "[0, 1, 2, 3, 4, 5, 6, 7, "
                + "8, _, _, _, _, 40, _, _, _, _, _, _, _, _, _, _, _, _, _]", 13);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove08ProbingDELInMap() {
        /*
            [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8, 8),
            _, _, x(11, 11), (24, 24)]

            =[Removing 40]=>

            [_, (1, 1), x(14, 14), x(27, 27), x(40, 40), _, (6, 6), _, (8, 8),
            _, _, x(11, 11), (24, 24)
         */
        util.removeRunner("[_, 1, x14, x27, x40, _, 6, _, 8, _, _, x11, 24]",
                "[_, 1, x14, x27, 40, _, 6, _, 8, _, _, x11, 24]", 4);

        /*
            [_, (1, 1), x(14, 14), x(27, 27), x(40, 40), (53, 53), x(6, 6), _,
            x(8, 8), _, _, x(11, 11), _]

            =[Removing 53]=>

            [_, (1, 1), x(14, 14), x(27, 27), x(40, 40), x(53, 53), x(6, 6), _,
            x(8, 8), _, _, x(11, 11), _]
         */
        util.removeRunner("[_, 1, x14, x27, x40, x53, x6, _, x8, _, _, x11, _]",
                "[_, 1, x14, x27, x40, 53, x6, _, x8, _, _, x11, _]", 5);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove09ReturnAndEquals() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("remove_return");
        LinearProbingHashMapTestUtil.checkPersistentCheck("remove_equals");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void remove10Size() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("remove_size");
        points += 1;
    }

    //*************************************************************************
    //**************************   get (10 points)   **************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void get01NullDataException() {
        try {
            /*
                [0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]

                (Attempt) Getting null
             */
            util.getNullData("[0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]");
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void get02NoSuchElementExceptionEmpty() {
        try {
            /*
                [_, _, _, _, _, _, _, _, _, _, _, _, _]
             */
            util.getNoSuchElement("[_, _, _, _, _, _, _, _, _, _, _, _, _]", 0);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void get03NegativeHash() {
        /*
            [_, (-1, -1), _, _, _, _, (-6, -6), _, (-8, -8), _, _, (MIN, MIN),
            (-25, -25)]

            Getting MIN, -6
         */
        util.getRunner("[_, -1, _, _, _, _, -6, _, -8, _, _, MIN, -25]",
                11, 6);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void get04ProbingNoDELWraparound() {
        /*
            [(37, 37), (50, 50), _, _, _, _, _, _, _, _, _, (11, 11), (24, 24)]

            Getting 37
         */
        try {
            util.getRunner("[37, 50, _, _, _, _, _, _, _, _, _, 11, 24]", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, (40, 40), _, _, _, _, _, _, _, _, _, _, _, _, _]

            Getting 40
        */
        try {
            util.getRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, _, _, _, _, 40, _, _,"
                    + " _, _, _, _, _, _, _, _, _, _, _]", 13);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            /*
                [(37, 37), (50, 50), _, _, _, _, _, _, _, _, _, (11, 11),
                (24, 24)]
                (Attempt) Getting 63
             */
            util.getNoSuchElement("[37, 50, _, _, _, _, _, _, _, _, _, 11, "
                    + "24]", 63);
        } catch (NoSuchElementException e1) {
            try {
                /*
                    [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7,
                    7), (8, 8), _, _, _, _, (40, 40), _, _, _, _, _, _, _, _, _,
                    _, _, _, _]

                    (Attempt) Getting 13
                 */
                util.getNoSuchElement("[0, 1, 2, 3, 4, 5, 6, 7, 8, _, _, "
                                + "_, _, 40, _, _, _, _, _, _, _, _, _, _, _, _, _]",
                        13);
            } catch (NoSuchElementException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void get05ProbingDELInMap() {
        /*
            [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8, 8),
             _, _, x(11, 11), (24, 24)

             Getting 40
         */
        util.getRunner("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, x11, 24]", 4);

        /*
            [_, (1, 1), x(14, 14), x(27, 27), x(40, 40), (53, 53), x(6, 6), _,
            x(8, 8), _, _, x(11, 11), _]

            Getting 53
         */
        util.getRunner("[_, 1, x14, x27, x40, 53, x6, _, x8, _, _, x11, _]", 5);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void get06FoundKeyRemoved() {
        try {
            util.getNoSuchElement("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, x11, "
                    + "24]", 27);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void get07NoNulls() {
        try {
            /*
                [x(0, 0), x(1, 1), x(2, 2), x(3, 3), x(4, 4), x(5, 5), x(6,
                6), x(7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]

                (Attempt) Getting 13
             */
            util.getNoSuchElement("[x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, "
                    + "x10, x11, x12]", 13);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void get08ProbingDELNotInMap() {
        try {
            /*
                [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8,
                 8), _, _, x(11, 11), (24, 24)]

                 (Attempt) Getting 37
             */
            util.getNoSuchElement("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, "
                    + "x11, 24]", 37);
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void get09Equals() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("get_equals");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void get10Unmodified() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("get_unmodified");
        points += 1;
    }

    //*************************************************************************
    //***********************    contains (10 points)   ***********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void containsKey01NullDataException() {
        try {
            util.containsNullData("[0, 1, 2, 3, 4, 5, 6, 7, _, _, _, _, _]");
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey02Empty() {
        /*
            [_, _, _, _, _, _, _, _, _, _, _, _, _]

            Checking 0
         */
        util.containsKeyRunner("[_, _, _, _, _, _, _, _, _, _, _, _, _]", 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey03NegativeHash() {
        /*
            [_, (-1, -1), _, _, _, _, (-6, -6), _, (-8, -8), _, _, (MIN, MIN)
            , (-25, -25)]

            Checking MIN, -6
         */
        util.containsKeyRunner("[_, -1, _, _, _, _, -6, _, -8, _, _, MIN, "
                + "-25]", Integer.MIN_VALUE, -6);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey04ProbingNoDELWraparound() {
        /*
            [(37, 37), (50, 50), _, _, _, _, _, _, _, _, _, (11, 11), (24, 24)]

            Checking 50, 63
         */
        util.containsKeyRunner("[37, 50, _, _, _, _, _, _, _, _, _, 11, 24]",
                50, 63);

        /*
            (0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), _, _, _, _, (40, 40), _, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.containsKeyRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, _, _, _, _, 40, "
                + "_, _, _, _, _, _, _, _, _, _, _, _, _]", 40);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey05ProbingDELInMap() {
        /*
            [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8, 8),
             _, _, x(11, 11), (24, 24)]

             Checking 40
         */
        util.containsKeyRunner("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, x11, "
                + "24]", 40);

        /*
            [_, (1, 1), x(14, 14), x(27, 27), x(40, 40), (53, 53), x(6, 6), _,
            x(8, 8), _, _, x(11, 11), _]

            Checking 53
         */
        util.containsKeyRunner("[_, 1, x14, x27, x40, 53, x6, _, x8, _, _, x11, _]",
                53);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey06FoundKeyRemoved() {
        /*
            [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8, 8),
             _, _, x(11, 11), (24, 24)]

             Checking 27
         */
        util.containsKeyRunner("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, x11, "
                + "24]", 27);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey07NoNulls() {
        /*
            [x(0, 0), x(1, 1), x(2, 2), x(3, 3), x(4, 4), x(5, 5), x(6, 6), x
            (7, 7), x(8, 8), x(9, 9), x(10, 10), x(11, 11), x(12, 12)]

            Checking 13
         */
        util.containsKeyRunner("[x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10,"
                + " x11, x12]", 13);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey08ProbingDELNotInMap() {
        /*
            [_, (1, 1), x(14, 14), x(27, 27), (40, 40), _, (6, 6), _, (8, 8),
             _, _, x(11, 11), (24, 24)]

             Checking 37
         */
        util.containsKeyRunner("[_, 1, x14, x27, 40, _, 6, _, 8, _, _, x11, "
                + "24]", 37);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey09Equals() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("containsKey_equals");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void containsKey10Unmodified() {
        LinearProbingHashMapTestUtil.checkPersistentCheck(
                "containsKey_unmodified");
        points += 1;
    }

    //*************************************************************************
    //*************************   keySet (5 points)   *************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void keySet01Empty() {
        /*
            [_, _, _, _, _, _, _, _, _, _, _, _, _]

            keys: {}
         */
        util.keySetRunner("[_, _, _, _, _, _, _, _, _, _, _, _, _]");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void keySet02NoDEL() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), (9, 9), (10, 10), (11, 11), (12, 12), (13, 13), (14, 14),
             (15, 15), (16, 16), (17, 17), _, _, _, _, _, _, _, _, _]

             keys: {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
              17}
         */
        util.keySetRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,"
                + " 15, 16, 17, _, _, _, _, _, _, _, _, _]");
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void keySet03WithDEL() {
        /*
            [(0, 0), (1, 1), x(2, 2), (3, 3), x(4, 4), (5, 5), (6, 6), (7, 7)
            , (8, 8), (9, 9), _, _, _,]

            keys: {0, 1, 3, 5, 6, 7, 8, 9}
         */
        util.keySetRunner("[0, 1, x2, 3, x4, 5, 6, 7, 8, 9, _, _, _]");
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void keySet04Unmodified() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("keySet_unmodified");
        points += 1;
    }

    //*************************************************************************
    //*************************   values (5 points)   *************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void values01Empty() {
        /*
            [_, _, _, _, _, _, _, _, _, _, _, _, _]

            values: []
         */
        util.valuesRunner("[_, _, _, _, _, _, _, _, _, _, _, _, _]");
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void values02NoDEL() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7),
            (8, 8), (9, 9), (10, 10), (11, 11), (12, 12), (13, 13), (14, 14),
             (15, 15), (16, 16), (17, 17), _, _, _, _, _, _, _, _, _]

             values: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
              17]
         */
        util.valuesRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,"
                + " 15, 16, 17, _, _, _, _, _, _, _, _, _]");
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void values03WithDEL() {
        /*
            [(0, 0), (1, 1), x(2, 2), (3, 3), x(4, 4), (5, 5), (6, 6), (7, 7)
            , (8, 8), (9, 9), _, _, _,]

            values: [0, 1, 3, 5, 6, 7, 8, 9]
         */
        util.valuesRunner("[0, 1, x2, 3, x4, 5, 6, 7, 8, 9, _, _, _]");

        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void values04Unmodified() {
        LinearProbingHashMapTestUtil.checkPersistentCheck("values_unmodified");
        points += 1;
    }

    //*************************************************************************
    //******************    resizeBackingTable (10 points)   ******************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void resizeBackingTable01CapacityException() {
        try {
            /*
                [_, _, _, _, _, _, _, _, _, _, _, _, _]
            */
            util.resizeBackingTableNegativeLength("[_, _, _, _, _, _, _, _, "
                    + "_, _, _, _, _]", -1);
        } catch (IllegalArgumentException e1) {
            try {
                /*
                    (0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6),
                    (7, 7), _, _, _, _, _]
                 */
                util.resizeBackingTableSmallLength("[0, 1, 2, 3, 4, 5, 6, 7, "
                        + "_, _, _, _, _]", 7);
            } catch (IllegalArgumentException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable02MinCapacity() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), _, _, _,
             _, _, _]

             =[Resizing to 7]=>

            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6)]
         */
        util.resizeBackingTableRunner("[0, 1, 2, 3, 4, 5, 6]", "[0, 1, 2, 3, "
                + "4, 5, 6, _, _, _, _, _, _]", 7);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable03SmallerGeneral() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), _, _, _,
             _, _, _]

             =[Resizing to 9]=>

             [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), _, _]
         */
        util.resizeBackingTableRunner("0, 1, 2, 3, 4, 5, 6, _, _]", "[0, 1, "
                + "2, 3, 4, 5, 6, _, _, _, _, _, _]", 9);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable04LargerSimple() {
        /*
            [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), _, _, _,
             _, _, _]

             =[Resizing to 14]=>

             [(0, 0), (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), _, _,
             _, _, _, _, _]
         */
        util.resizeBackingTableRunner("[0, 1, 2, 3, 4, 5, 6, _, _, _, _, _, "
                + "_, _]", "[0, 1, 2, 3, 4, 5, 6, _, _, _, _, _, _]", 14);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable05Rehashing() {
        /*
            [_, (-1, -1), _, _, _, _, (-6, -6), _, (-8, -8), _, _, (MIN, MIN),
            (-25, -25)]

            =[Resizing to 14]=>

            [_, (-1, -1), (MIN, MIN), _, _, _, (-6, -6), _, (-8, -8), _, _,
            (-25, -25), _, _]
         */
        util.resizeBackingTableRunner("[_, -1, MIN, _, _, _, -6, _, -8, _, _,"
                        + " -25, _, _]", "[_, -1, _, _, _, _, -6, _, -8, _, _, MIN, -25]",
                14);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable06RehashingProbing() {
        /*
            [_, (1, 1), _, _, _, _, (6, 6), _, (8, 8), _, _, (11, 11), (24, 24)]

            =[Resizing to 5]=>

            [(24, 24), (1, 1), (6, 6), (8, 8), (11, 11)]
         */
        util.resizeBackingTableRunner("[24, 1, 6, 8, 11]", "[_, 1, _, _, _, "
                + "_, 6, _, 8, _, _, 11, 24]", 5);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable07DELEntries() {
        /*
            [(0, 0), (1, 1), x(2, 2), (3, 3), x(4, 4), (5, 5), (6, 6), (7, 7)
            , (8, 8), (9, 9), _, _, _]

            =[Resizing to 14]=>

            [(0, 0), (1, 1), _, (3, 3), _, (5, 5), (6, 6), (7, 7), (8, 8),
            (9, 9), _, _, _, _]
         */
        util.resizeBackingTableRunner("[0, 1, _, 3, _, 5, 6, 7, 8, 9, _, _, "
                + "_, _]", "[0, 1, x2, 3, x4, 5, 6, 7, 8, 9, _, _, _]", 14);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void resizeBackingTable08Size() {
        LinearProbingHashMapTestUtil.checkPersistentCheck(
                "resizeBackingTable_size");
        points += 1;
    }

    //*************************************************************************
    //*************************    clear (5 points)   *************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void clear01Empty() {
        /*
            [_, _, _, _, _, _, _, _, _, _, _, _, _]

            =[Clearing]=>

            [_, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.clearRunner("[_, _, _, _, _, _, _, _, _, _, _, _, _]", false);
        util.clearRunner("[_, _, _, _, _, _, _, _, _, _, _, _, _]", true);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void clear02Size() {
        /*
            [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, _,
             _, _, _, _, _, _, _, _]

            =[Clearing]=>

            [_, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.clearRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,"
                + " 15, 16, 17, _, _, _, _, _, _, _, _, _]", true);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void clear03Table() {
        /*
            [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, _,
             _, _, _, _, _, _, _, _]

            =[Clearing]=>

            [_, _, _, _, _, _, _, _, _, _, _, _, _]
         */
        util.clearRunner("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,"
                + " 15, 16, 17, _, _, _, _, _, _, _, _, _]", false);
        points += 2;
    }

}