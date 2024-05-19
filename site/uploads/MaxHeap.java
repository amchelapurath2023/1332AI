import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MaxHeap.
 *
 * @author Kripa Kannan
 * @version 1.0
 * @userid kkannan40
 * @GTID 903891320
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
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public MaxHeap() {
        size = 0;
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
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
            throw new IllegalArgumentException("Data is null");
        }
        size = data.size();
        backingArray = (T[]) new Comparable[size * 2 + 1];
        for (int i = 1; i <= size; i++) {
            if (data.get(i - 1) == null) {
                throw new IllegalArgumentException("Data is null");
            }
            backingArray[i] = data.get(i - 1);
        }
        for (int i = size / 2; i >= 1; i--) {
            moveDownHeap(i);
        }
    }

    /**
     * Moves an element at the given index up until it is in the correct position.
     * @param index the specified index at which the element is
     */
    private void moveUpHeap(int index) {
        if (index == 1) {
            return;
        }
        int parentIndex = index / 2;
        if (backingArray[index].compareTo(backingArray[parentIndex]) > 0) {
            T temp = backingArray[index];
            backingArray[index] = backingArray[parentIndex];
            backingArray[parentIndex] = temp;
        }
        moveUpHeap(parentIndex);
    }

    /**
     * Moves an element at the given index down until it is in the correct position.
     * @param parent the specified index of the parent element that needs to be moved down the heap
     */
    private void moveDownHeap(int parent) {
        int leftChildIndex = parent * 2;
        int rightChildIndex = parent * 2 + 1;
        if (leftChildIndex > size || (backingArray[leftChildIndex] == null && backingArray[rightChildIndex] == null)) {
            return;
        } else if (backingArray[rightChildIndex] == null) {
            if (backingArray[parent].compareTo(backingArray[leftChildIndex]) < 0) {
                T temp = backingArray[leftChildIndex];
                backingArray[leftChildIndex] = backingArray[parent];
                backingArray[parent] = temp;
            }
        } else {
            int largestChildIndex;
            if (backingArray[rightChildIndex].compareTo(backingArray[leftChildIndex]) < 0) {
                largestChildIndex = leftChildIndex;
            } else {
                largestChildIndex = rightChildIndex;
            }
            if (backingArray[parent].compareTo(backingArray[largestChildIndex]) < 0) {
                T temp = backingArray[largestChildIndex];
                backingArray[largestChildIndex] = backingArray[parent];
                backingArray[parent] = temp;
            }
        }
        moveDownHeap(leftChildIndex);
        moveDownHeap(rightChildIndex);
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
            throw new IllegalArgumentException("Data is null");
        }
        if (size == backingArray.length - 1) {
            T[] temp = (T[]) new Comparable[backingArray.length * 2];
            for (int i = 0; i <= size; i++) {
                temp[i] = backingArray[i];
            }
            backingArray = temp;
        }
        backingArray[size + 1] = data;
        moveUpHeap(size + 1);
        size++;
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
            throw new NoSuchElementException("The heap is empty");
        }
        T remove = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        moveDownHeap(1);
        size--;
        return remove;
    }

    /**
     * Returns the maximum element in the heap.
     *
     * @return the maximum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMax() {
        if (size == 0) {
            throw new NoSuchElementException("The heap is empty");
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
        size = 0;
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
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