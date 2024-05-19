import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MaxHeap.
 *
 * @author Indira Tatikola
 * @version 1.0
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
     * The backingArray should have capacity 2n + 1 where n is the
     * number of data in the passed in ArrayList (not INITIAL_CAPACITY).
     * Index 0 should remain empty, indices 1 to n should contain the data in
     * proper order, and the rest of the indices should be empty.
     *
     * Consider how to most efficiently determine if the list contains null data.
     * 
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MaxHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The inputted ArrayList is null.");
        }
        size = data.size();
        backingArray = (T[]) new Comparable[size * 2 + 1];
        for (int i = 1; i <= size; i++) {
            if (data.get(i - 1) == null) {
                throw new IllegalArgumentException("An inputted data is null.");
            }
            backingArray[i] = data.get(i - 1);
        }
        buildHeap();
    }

    /**
     * helper method for instantiating a MaxHeap in O(n) time
     */
    private void buildHeap() {
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
            throw new IllegalArgumentException("The inputted data is null.");
        }
        if (size + 1 == backingArray.length) {
            T[] newArray = (T[]) new Comparable[2 * backingArray.length];
            for (int i = 1; i <= size; i++) {
                newArray[i] = backingArray[i];
            }
            backingArray = newArray;
        }

        backingArray[++size] = data;
        upHeap(size);
    }

    /**
     * recursive upHeap algorithm
     * @param i index from where to start
     */
    private void upHeap(int i) {
        if (i != 1) {
            T child = backingArray[i];
            T parent = backingArray[i / 2];
            if (child.compareTo(parent) > 0) {
                backingArray[i / 2] = child;
                backingArray[i] = parent;
                upHeap(i / 2);
            }
        }
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
            throw new NoSuchElementException("The heap is empty.");
        }
        T removed = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size--] = null;
        downHeap(1);
        return removed;
    }

    /**
     * private recursive helper method for downHeap algorithm
     * @param i index from where to start downHeap
     */
    private void downHeap(int i) {
        if (i <= size / 2) {
            T parent = backingArray[i];
            T greaterChild = null;
            int greaterChildIndex = 2 * i;
            if (2 * i + 1 <= size && backingArray[2 * i + 1].compareTo(backingArray[2 * i]) > 0) {
                greaterChild = backingArray[2 * i + 1];
                greaterChildIndex++;
            } else {
                greaterChild = backingArray[2 * i];
            }

            if (greaterChild.compareTo(parent) > 0) {
                System.out.println("SWAPPED");
                System.out.println("Parent: " + parent + "\tChild: " + greaterChild);
                backingArray[greaterChildIndex] = parent;
                backingArray[i] = greaterChild;
                downHeap(greaterChildIndex);
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
            throw new NoSuchElementException("The heap is empty.");
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
