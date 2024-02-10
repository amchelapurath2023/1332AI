/**
 * Your implementation of a CircularSinglyLinkedList without a tail pointer.
 *
 * @author David Wang
 * @version 2.0
 * @userid dwang388
 * @GTID 90XXXXXXX
 *
 * Collaborators: Indira Tatikola
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */

 import java.util.NoSuchElementException;

 public class CircularSinglyLinkedList_IMPL<T> {
 
     /*
      * Do not add new instance variables or modify existing ones.
      */
     private CircularSinglyLinkedListNode<T> head;
     private int size;
 
     /*
      * Do not add a constructor.
      */
 
     /**
      * Adds the data to the specified index.
      *
      * Must be O(1) for indices 0 and size and O(n) for all other cases.
      *
      * @param index the index at which to add the new data
      * @param data  the data to add at the specified index
      * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
      * @throws java.lang.IllegalArgumentException  if data is null
      */
     public void addAtIndex(int index, T data) {
         if (index < 0 || index > size) {
             throw new IndexOutOfBoundsException("Index out of bounds");
         }
         if (data == null) {
             throw new IllegalArgumentException("Data null");
         }
         if (index == 0) {
             addToFront(data);
         } else if (index == size) {
             addToBack(data);
         } else {
             /* STEPS:
             1. iterate to node prior to where we want to add
             2. create new node with new data, set as next node
             3. increment size
              */
             CircularSinglyLinkedListNode<T> cur = head;
             // feel like its more intuitive for bounds to be from 0 to index - 1, but it's okay
             for (int i = 1; i < index; i++) {
                 cur = cur.getNext();
             }
             cur.setNext(new CircularSinglyLinkedListNode<>(data, cur
                     .getNext()));
             size++;
         }
     }
 
     /**
      * Adds the data to the front of the list.
      *
      * Must be O(1).
      *
      * @param data the data to add to the front of the list
      * @throws java.lang.IllegalArgumentException if data is null
      */
     public void addToFront(T data) {
         if (data == null) {
             throw new IllegalArgumentException("Data null");
         }
         // EDGE CASE: empty list
         if (size == 0) {
             head = new CircularSinglyLinkedListNode<>(data);
             head.setNext(head);
         } else {
             /* STEPS:
             1. create copy of head node
             2. replace head data with new data
             3. set next node to copy of previous head
             4. increment size
             */
             CircularSinglyLinkedListNode<T> node =
                     new CircularSinglyLinkedListNode<>(head.getData(), head
                             .getNext());
             head.setData(data);
             head.setNext(node);
         }
         size++;
     }
 
     /**
      * Adds the data to the back of the list.
      *
      * Must be O(1).
      *
      * @param data the data to add to the back of the list
      * @throws java.lang.IllegalArgumentException if data is null
      */
     public void addToBack(T data) {
         if (data == null) {
             throw new IllegalArgumentException("Data null");
         }
         addToFront(data);
         head = head.getNext();
     }
 
     /**
      * Removes and returns the data at the specified index.
      *
      * Must be O(1) for index 0 and O(n) for all other cases.
      *
      * @param index the index of the data to remove
      * @return the data formerly located at the specified index
      * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
      */
     public T removeAtIndex(int index) {
         if (index < 0 || index >= size) {
             throw new IndexOutOfBoundsException("Index out of bounds");
         }
         if (index == 0) {
             return removeFromFront();
         } else if (index == size - 1) {
             return removeFromBack();
         } else {
             /* STEPS:
             1. iterate to node prior to where we want to add
             2. save data to be removed
             3. reset next pointer to node after removed node
             4. decrement size
             5. return removed data
             */
             CircularSinglyLinkedListNode<T> cur = head;
             for (int i = 0; i < index - 1; i++) {
                 cur = cur.getNext();
             }
             T result = cur.getNext().getData();
             cur.setNext(cur.getNext().getNext());
             --size;
             return result;
         }
     }
 
     /**
      * Removes and returns the first data of the list.
      *
      * Must be O(1).
      *
      * @return the data formerly located at the front of the list
      * @throws java.util.NoSuchElementException if the list is empty
      */
     public T removeFromFront() {
         if (size == 0) {
             throw new NoSuchElementException("list empty");
         }
         T result = head.getData();
         // EDGE CASE: one element list
         if (--size == 0) {
             head = null;
         } else {
             head.setData(head.getNext().getData());
             head.setNext(head.getNext().getNext());
         }
         return result;
 
     }
 
     /**
      * Removes and returns the last data of the list.
      *
      * Must be O(n).
      *
      * @return the data formerly located at the back of the list
      * @throws java.util.NoSuchElementException if the list is empty
      */
     public T removeFromBack() {
         if (size == 0) {
             throw new NoSuchElementException("list empty");
         }
         T result;
         // EDGE CASE: one element list
         if (--size == 0) {
             result = head.getData();
             head = null;
         } else {
             CircularSinglyLinkedListNode<T> cur = head;
             /* STEPS:
             1. iterate to one prior to the last node (size - 1 bc size was decremented in line 192)
             2. save data to be removed
             3. remove next node
              */
             for (int i = 0; i < size - 1; i++) {
                 cur = cur.getNext();
             }
             result = cur.getNext().getData();
             cur.setNext(cur.getNext().getNext());
         }
         return result;
 
     }
 
     /**
      * Returns the data at the specified index.
      *
      * Should be O(1) for index 0 and O(n) for all other cases.
      *
      * @param index the index of the data to get
      * @return the data stored at the index in the list
      * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
      */
     public T get(int index) {
         if (index < 0 || index >= size) {
             throw new IndexOutOfBoundsException("Index out of bounds");
         }
         CircularSinglyLinkedListNode<T> curr = head;
         for (int i = 0; i < index; i++) {
             curr = curr.getNext();
         }
         return curr.getData();
     }
 
     /**
      * Returns whether or not the list is empty.
      *
      * Must be O(1).
      *
      * @return true if empty, false otherwise
      */
     public boolean isEmpty() {
         return size == 0;
     }
 
     /**
      * Clears the list.
      *
      * Clears all data and resets the size.
      *
      * Must be O(1).
      */
     public void clear() {
         head = null;
         size = 0;
     }
 
     /**
      * Removes and returns the last copy of the given data from the list.
      *
      * Do not return the same data that was passed in. Return the data that
      * was stored in the list.
      *
      * Must be O(n).
      *
      * @param data the data to be removed from the list
      * @return the data that was removed
      * @throws java.lang.IllegalArgumentException if data is null
      * @throws java.util.NoSuchElementException   if data is not found
      */
     public T removeLastOccurrence(T data) {
         if (data == null) {
             throw new IllegalArgumentException("Data is null");
         }
         /* STEPS:
         1. iterate through entire list, saving reference to nodes prior to node containing 'data'
         2. removal process with final saved reference
          */
         // Step 1 //
         CircularSinglyLinkedListNode<T> cur = head;
         CircularSinglyLinkedListNode<T> resultNode = null;
         for (int i = 1; i < size; i++, cur = cur.getNext()) {
             if (data.equals(cur.getNext().getData())) {
                 resultNode = cur;
             }
         }
         // EDGE CASE: if head is the last occurrence
         if (resultNode == null && head != null && data.equals(head.getData())) {
             resultNode = cur;
         }
         // EDGE CASE: data not found or list is empty (same as 'data not found')
         if (resultNode == null || head == null) {
             throw new java.util.NoSuchElementException("Data must be in the "
                     + "list");
         }
         // Step 2 //
         T result = resultNode.getNext().getData();
         if (--size == 0) {
             head = null;
         } else {
             if (head.equals(resultNode.getNext())) {
                 head = resultNode.getNext().getNext();
             }
             resultNode.setNext(resultNode.getNext().getNext());
         }
         return result;
     }
 
     /**
      * Returns an array representation of the linked list.
      *
      * Must be O(n) for all cases.
      *
      * @return the array of length size holding all of the data (not the
      * nodes) in the list in the same order
      */
     public T[] toArray() {
         T[] array = (T[]) new Object[size];
         CircularSinglyLinkedListNode<T> curr = head;
         for (int i = 0; i < size; i++) {
             array[i] = curr.getData();
             curr = curr.getNext();
         }
 
         return array;
     }
 
     /**
      * Returns the head node of the list.
      *
      * For grading purposes only. You shouldn't need to use this method since
      * you have direct access to the variable.
      *
      * @return the node at the head of the list
      */
     public CircularSinglyLinkedListNode<T> getHead() {
         // DO NOT MODIFY!
         return head;
     }
 
     /**
      * Returns the size of the list.
      *
      * For grading purposes only. You shouldn't need to use this method since
      * you have direct access to the variable.
      *
      * @return the size of the list
      */
     public int size() {
         // DO NOT MODIFY!
         return size;
     }
 }