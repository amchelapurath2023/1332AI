package src;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.NoSuchElementException;

/**
 * Grading JUnits for CircularSinglyLinkedList.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CircularSinglyLinkedListTest {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private static final long TIMEOUT = 1000;
    private static int points;
    private static CircularSinglyLinkedListTestUtil util;

    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    @Before
    public void setup() {
        util = new CircularSinglyLinkedListTestUtil();
    }

    @AfterClass
    public static void printResults() {
        System.out.println("Total Points: " + points);
    }

    //*************************************************************************
    //**********************    addAtIndex (10 points)   **********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void addAtIndex01NegativeIndex() {
        try {
            /*
                EMPTY

                (Attempt) Adding -1
             */
            util.addAtIndexNegativeIndex(0, -1);
        } catch (IndexOutOfBoundsException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Adding -2
                 */
                util.addAtIndexNegativeIndex(5, -2);
            } catch (IndexOutOfBoundsException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void addAtIndex02LargeIndex() {
        try {
            /*
                EMPTY

                (Attempt) Adding at Index 1
             */
            util.addAtIndexLargeIndex(0, 1);
        } catch (IndexOutOfBoundsException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Adding at Index 6
                 */
                util.addAtIndexLargeIndex(5, 6);
            } catch (IndexOutOfBoundsException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void addAtIndex03NullDataException() {
        try {
            /*
                EMPTY

                (Attempt) Adding null
             */
            util.addAtIndexNullData(0, 0);
        } catch (IllegalArgumentException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Adding null
                 */
                util.addAtIndexNullData(5, 1);
            } catch (IllegalArgumentException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addAtIndex04Empty() {
        /*
            EMPTY

            =[Adding 0]=>

            0 -> head
         */
        util.addAtIndexRunner(1, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addAtIndex05OneElement() {
        /*
            1 -> head

            =[Adding 0]=>

            0 -> 1 -> head
         */
        util.addAtIndexRunner(2, 0);

        /*
            0 -> head

            =[Adding 1]=>

            0 -> 1 -> head
         */
        util.addAtIndexRunner(2, 1);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addAtIndex06FrontAndCloserToFront() {
        /*
            1 -> 2 -> 3 -> 4 -> head

            =[Adding 0]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addAtIndexRunner(5, 0);

        /*
            0 -> 2 -> 3 -> 4 -> head

            =[Adding 1]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addAtIndexRunner(5, 1);

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void addAtIndex07Middle() {
        /*
            0 -> 1 -> 3 -> 4 -> head

            =[Adding 2]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addAtIndexRunner(5, 2);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addAtIndex08BackAndCloserToBack() {
        /*
            0 -> 1 -> 2 -> 3 -> head

            =[Adding 4]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addAtIndexRunner(5, 4);

        /*
            0 -> 1 -> 2 -> 4 -> head

            =[Adding 3]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addAtIndexRunner(5, 3);

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addAtIndex09Size() {
        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("addAtIndex_size");
        points += 1;
    }

    //*************************************************************************
    //***********************   addToFront (5 points)   ***********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void addToFront01NullData() {
        try {
            /*
                EMPTY

                (Attempt) Adding null
             */
            util.addToFrontNullData(0);
        } catch (IllegalArgumentException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Adding null
                 */
                util.addToFrontNullData(4);
            } catch (IllegalArgumentException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToFront02Empty() {
        /*
            EMPTY

            =[Adding 0]=>

            0 -> head
         */
        util.addToFrontRunner(1, 1);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToFront03OneElement() {
        /*
            1

            =[Adding 0]=>

            0 -> 1 -> head
         */
        util.addToFrontRunner(2, 1);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToFront04General() {
        /*
            2 -> 3 -> 4 -> head

            =[Adding 1, 0]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addToFrontRunner(5, 2);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToFront05Size() {
        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("addToFront_size");
        points += 1;
    }

    //*************************************************************************
    //***********************    addToBack (5 points)   ***********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void addToBack01NullData() {
        try {
            /*
                EMPTY

                (Attempt) Adding null
             */
            util.addToBackNullData(0);
        } catch (IllegalArgumentException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Adding null
                 */
                util.addToBackNullData(5);
            } catch (IllegalArgumentException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToBack02Empty() {
        /*
            EMPTY

            =[Adding 0]=>

            0 -> head
         */
        util.addToBackRunner(1, 1);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToBack03OneElement() {
        /*
            0 -> head

            =[Adding 1]=>

            0 -> 1 -> head
         */
        util.addToBackRunner(2, 1);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToBack04General() {
        /*
            0 -> 1 -> 2 -> head

            =[Adding 3, 4]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.addToBackRunner(5, 2);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void addToBack05Size() {
        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("addToBack_size");
        points += 1;
    }

    //*************************************************************************
    //*********************   removeAtIndex (10 points)   *********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void removeAtIndex01NegativeIndex() {
        try {
            /*
                EMPTY

                (Attempt) Removing -1
             */
            util.removeAtIndexNegativeIndex(0, -1);
        } catch (IndexOutOfBoundsException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Removing -2
                 */
                util.removeAtIndexNegativeIndex(5, -2);
            } catch (IndexOutOfBoundsException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void removeAtIndex02LargeIndex() {
        try {
            /*
                EMPTY

                (Attempt) Removing 1
             */
            util.removeAtIndexLargeIndex(0, 1);
        } catch (IndexOutOfBoundsException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Removing 5
                 */
                util.removeAtIndexLargeIndex(5, 5);
            } catch (IndexOutOfBoundsException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex03OneElement() {
        /*
            0 -> head

            =[Removing 0]=>

            EMPTY
         */
        util.removeAtIndexRunner(1, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex04Front() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> 5 -> head

            =[Removing 0]=>

            1 -> 2 -> 3 -> 4 -> 5 -> head
         */
        util.removeAtIndexRunner(6, 0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex05CloserToFront() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> 5 -> head

            =[Removing 1]=>

            0 -> 2 -> 3 -> 4 -> 5 -> head
         */
        util.removeAtIndexRunner(6, 1);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex06Middle() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head

            =[Removing 2]=>

            0 -> 1 -> 3 -> 4 -> head
         */
        util.removeAtIndexRunner(5, 2);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex07Back() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> 5 -> head

            =[Removing 5]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.removeAtIndexRunner(6, 5);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex08CloserToBack() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> 5 -> head

            =[Removing 4]=>

            0 -> 1 -> 2 -> 3 -> 5 -> head
         */
        util.removeAtIndexRunner(6, 4);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeAtIndex09ReturnValueAndSize() {
        // Whether or not the correct value was returned
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeAtIndex_returnValue");

        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeAtIndex_size");

        points += 1;
    }

    //*************************************************************************
    //********************    removeFromFront (5 points)   ********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void removeFromFront01Empty() {
        try {
            /*
                EMPTY

                (Attempt) Removing
             */
            util.removeFromFrontEmpty();
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeFromFront02OneElement() {
        /*
            0 -> head

            =[Removing 0]=>

            EMPTY
         */
        util.removeFromFrontRunner(1, 1);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void removeFromFront03General() {
        /*
            0 -> 1 head

            =[Removing 0]=>

            1 -> head
         */
        util.removeFromFrontRunner(2, 1);

        /*
            0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> head

            =[Removing 0, 1]=>

            3 -> 4 -> 5 -> 6 -> head
         */
        util.removeFromFrontRunner(7, 2);

        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeFromFront04ReturnValueAndSize() {
        // Whether or not the correct value was returned
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeFromFront_returnValue");

        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeFromFront_size");

        points += 1;
    }

    //*************************************************************************
    //*********************   removeFromBack (5 points)   *********************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void removeFromBack01Empty() {
        try {
            /*
                EMPTY

                (Attempt) Removing
             */
            util.removeFromBackEmpty();
        } catch (NoSuchElementException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeFromBack02OneElement() {
        /*
            0 -> head

            =[Removing 0]=>

            EMPTY
         */
        util.removeFromBackRunner(1, 1);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void removeFromBack03General() {
        /*
            0 -> 1 head

            =[Removing 1]=>

            0 -> head
         */
        util.removeFromBackRunner(2, 1);

        /*
            0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> head

            =[Removing 6, 5]=>

            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.removeFromBackRunner(7, 2);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeFromBack04SizeAndReturnValue() {
        // Whether or not the correct value was returned
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeFromBack_returnValue");

        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeFromBack_size");

        points += 1;
    }

    //*************************************************************************
    //**************************   get (10 points)   **************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void get01NegativeIndex() {
        try {
            /*
                EMPTY

                (Attempt) Getting -1
             */
            util.getNegativeIndex(0, -1);
        } catch (IndexOutOfBoundsException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Getting -2
                 */
                util.getNegativeIndex(5, -2);
            } catch (IndexOutOfBoundsException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void get02LargeIndex() {
        try {
            /*
                EMPTY

                (Attempt) Getting 1
             */
            util.getLargeIndex(0, 1);
        } catch (IndexOutOfBoundsException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Getting 5
                 */
                util.getLargeIndex(5, 5);
            } catch (IndexOutOfBoundsException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void get03OneElement() {
        /*
            0 -> head

            Getting 0
         */
        util.getRunner(1, 0);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void get04Front() {
        /*
            0 -> 1 -> 2 -> 3 -> 4

            Getting 0
         */
        util.getRunner(5, 0);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void get05Middle() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head

            Getting 2
         */
        util.getRunner(5, 2);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void get06Back() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head

            Getting 4
         */
        util.getRunner(5, 3);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void get07Unmodified() {
        // Whether or not the structure remained unchanged
        CircularSinglyLinkedListTestUtil.checkPersistentCheck("get_unmodified");
        points += 1;
    }

    //*************************************************************************
    //************************    isEmpty (4 points)   ************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty01Empty() {
        /*
            EMPTY
         */
        util.isEmptyRunner(0);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty02OneElement() {
        /*
            0 -> head
         */
        util.isEmptyRunner(1);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty03General() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.isEmptyRunner(5);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void isEmpty04Unmodified() {
        // Whether or not the structure remained unchanged
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("isEmpty_unmodified");
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
         */
        util.clearRunner(0, true);
        util.clearRunner(0, false);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void clear02Size() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        // Only checks that the size gets reset
        util.clearRunner(5, true);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void clear03Structure() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        // Only checks that the structure gets cleared
        util.clearRunner(5, false);
        points += 2;
    }

    //*************************************************************************
    //*****************    removeLastOccurrence (10 points)   *****************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void removeLastOccurrence01NullData() {
        try {
            /*
                0 -> 1 -> 2 -> 3 -> 4 -> head

                (Attempt) Removing null
             */
            util.removeLastOccurrenceNullData(5);
        } catch (IllegalArgumentException e) {
            points += 1;
            throw e;
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void removeLastOccurrence02ElementNotFound() {
        try {
            /*
                EMPTY

                (Attempt) Removing -1
             */
            util.removeLastOccurrenceElementNotFound(0, -1);
        } catch (NoSuchElementException e1) {
            try {
                /*
                    0 -> 1 -> 2 -> 3 -> 4 -> head

                    (Attempt) Removing 5
                 */
                util.removeLastOccurrenceElementNotFound(5, 5);
            } catch (NoSuchElementException e2) {
                points += 1;
                throw e2;
            }
        }
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence03OneElementAndTwoElements() {
        int[] numbers = new int[]{0};

        /*
            0 -> head

            =[Removing 0]=>

            EMPTY
         */
        util.removeLastOccurrenceRunner(numbers, 0);

        numbers = new int[]{0, 1};

        /*
            0 -> 1 -> head

            =[Removing 0]=>

            1 -> head
         */
        util.removeLastOccurrenceRunner(numbers, 0);

        /*
            0 -> 1 -> head

            =[Removing 1]=>

            0 -> head
         */
        util.removeLastOccurrenceRunner(numbers, 0);

        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence04OneCopy() {
        int[] numbers = new int[]{0, 1, 2, 3, 4};

        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head

            =[Removing 0]=>

            1 -> 2 -> 3 -> 4 -> head
         */
        util.removeLastOccurrenceRunner(numbers, 0);

        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head

            =[Removing 1 (Index 1)]=>

            0 -> 2 -> 3 -> 4 -> head
         */
        util.removeLastOccurrenceRunner(numbers, 1);

        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head

            =[Removing 4 (Index 4)]=>

            0 -> 1 -> 2 -> 3 -> head
         */
        util.removeLastOccurrenceRunner(numbers, 4);

        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence05TwoCopiesInHeadAndTail() {
        /*
            0 -> 1 -> 2 -> 3 -> 0 -> head

            =[Removing 0 (Index 4)]=>

            0 -> 1 -> 2 -> 3 -> head
         */
        util.removeLastOccurrenceRunner(new int[]{0, 1, 2, 3, 0}, 4);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence06TwoCopiesInHeadAndMiddle() {
        /*
            0 -> 1 -> 2 -> 0 -> 3 -> head

            =[Removing 0 (Index 3)]=>

            0 -> 1 -> 2 -> 3 -> head
         */
        util.removeLastOccurrenceRunner(new int[]{0, 1, 2, 0, 3}, 3);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence07TwoCopiesInMiddle() {
        /*
            0 -> 1 -> 2 -> 1 -> 3 -> head

            =[Removing 1 (Index 3)]=>

            0 -> 1 -> 2 -> 3 -> head
         */
        util.removeLastOccurrenceRunner(new int[]{0, 1, 2, 1, 3}, 3);
        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence08ReturnValueAndReturnReference() {
        // Whether or not the correct value was returned
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeLastOccurrence_returnValue");

        // Whether or not the correct reference was returned
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeLastOccurrence_returnReference");

        points += 1;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void removeLastOccurrence09EqualsAndSize() {
        // Whether or not equals was used instead of ==
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeLastOccurrence_equals");

        // Whether or not size was updated correctly
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("removeLastOccurrence_size");
        points += 1;
    }

    //*************************************************************************
    //************************    toArray (6 points)   ************************
    //*************************************************************************

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void toArray01Empty() {
        /*
            EMPTY
         */
        util.toArrayRunner(0);
        points += 1;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void toArray02OneElement() {
        /*
            0 -> head
         */
        util.toArrayRunner(1);
        points += 2;
    }

    // 2 point(s)
    @Test(timeout = TIMEOUT)
    public void toArray03General() {
        /*
            0 -> 1 -> 2 -> 3 -> 4 -> head
         */
        util.toArrayRunner(5);
        points += 2;
    }

    // 1 point(s)
    @Test(timeout = TIMEOUT)
    public void toArray04Unmodified() {
        // Whether or not the structure remained unchanged
        CircularSinglyLinkedListTestUtil
            .checkPersistentCheck("toArray_unmodified");
        points += 1;
    }
}