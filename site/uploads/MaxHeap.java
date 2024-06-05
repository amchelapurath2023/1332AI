import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MaxHeap.
 *
 * @author Jane Doe
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class MaxHeap<T extends Comparable<? super T>> {

    /*
     * The initial capacity of the MaxHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MaxHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public MaxHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;

    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 (including the empty 0
     * index) where n is the number of data in the passed in ArrayList (not
     * INITIAL_CAPACITY). Index 0 should remain empty, indices 1 to n should
     * contain the data in proper order, and the rest of the indices should
     * be empty.
     *
     * Consider how to most efficiently determine if the list contains null data.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MaxHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("A user can't added a null ArrayList to the MaxHeap! Check again!");
        }
        backingArray = (T[]) new Comparable[data.size() * 2 + 1];
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("You can't add null data from the ArrayList to"
                        + " the MaxHeap! Check again!");
            }
            backingArray[i + 1] = data.get(i);
        }
        size = data.size();
        for (int i = size / 2; i > 0; i--) {
            downHeap(i);
        }

    }

    /**
     * Adds the data to the heap.
     *
     * If sufficient space is not available in the backing array (the backing
     * array is full except for index 0), resize it to double the current
     * length.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("You can't add null data to the MaxHeap. "
                    + "Make sure your data isn't null");
        }
        if (size == backingArray.length - 1) {
            T[] tempBackingArray = (T[]) new Comparable[backingArray.length * 2];
            for (int i = 1; i <= size; i++) {
                tempBackingArray[i] = backingArray[i];
            }
            backingArray = tempBackingArray;
        }
        backingArray[++size] = data;
        int childIndex = size;
        int parentIndex = size / 2;
        while (childIndex > 1 && backingArray[childIndex].compareTo(backingArray[parentIndex]) > 0) {
            swapData(childIndex, parentIndex);
            childIndex = parentIndex;
            parentIndex = parentIndex / 2;
        }
    }

    /**
     * Purpose of the helper method is to swap the data between the first
     * Element and the second Element!
     * It utilises a temp variable to ensure data is properly
     * transfered between the first Element and the second Element!
     * the two elements!
     * @param firstElementIndex the index of the child.
     * @param secondElementIndex the index of the parent
     */
    private void swapData(int firstElementIndex, int secondElementIndex) {
        T temp = backingArray[firstElementIndex];
        backingArray[firstElementIndex] = backingArray[secondElementIndex];
        backingArray[secondElementIndex] = temp;
    }



    /**
     * Removes and returns the root of the heap.
     *
     * Do not shrink the backing array.
     *
     * Replace any unused spots in the array with null.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (size == 0) {
            throw new NoSuchElementException("You can't remove elements from an empty heap! Check again!");
        }
        T temp = backingArray[size];
        T answer = backingArray[1];
        backingArray[1] = temp;
        backingArray[size--] = null;
        int currentSpot = 1;
        downHeap(currentSpot);
        return answer;
    }

    /**
     * The purpose of this helper method is to recurisvely help with the bubble down! The purpose
     * of the downHeap method is to swap the elements if there is a bad relationship
     * between a parent and it's children! If there is no bad relationship with it's children, then it won't
     * recurisvely call the method again!
     * @param currentSpot the purpose of currentSpot is the location of where you currently are as you
     * are traversing down the heap. If you're current spot * 2 > size, we know
     * that the currentSpot is a lead node.
     */
    private void downHeap(int currentSpot) {
        if (2 * currentSpot > size) {
            return;
        }
        int leftChild = 2 * currentSpot;
        if (2 * currentSpot + 1 > size) {
            if (backingArray[leftChild].compareTo(backingArray[currentSpot]) > 0) {
                swapData(2 * currentSpot, currentSpot);
                currentSpot = leftChild;
                downHeap(currentSpot);
            }
        } else {
            int rightChild = 2 * currentSpot + 1;
            T higherPriorityChild = backingArray[leftChild];
            int higherPriorityChildIndex = leftChild;
            if (backingArray[rightChild].compareTo(backingArray[leftChild]) > 0) {
                higherPriorityChild = backingArray[rightChild];
                higherPriorityChildIndex = rightChild;
            }
            if (backingArray[higherPriorityChildIndex].compareTo(backingArray[currentSpot]) > 0) {
                swapData(currentSpot, higherPriorityChildIndex);
                currentSpot = higherPriorityChildIndex;
                downHeap(currentSpot);
            }
        }
    }


    /**
     * Returns the maximum element in the heap.
     *
     * @return the maximum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMax() {
        if (size == 0) {
            throw new NoSuchElementException("You can't get elements from an empty heap! "
                    + "Make sure that your heap isn't empty!");
        }
        return backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}