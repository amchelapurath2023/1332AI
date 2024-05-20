import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * Your implementation of a LinearProbingHashMap.
 *
 * @author Yash Vagal
 * @version 1.0
 * @userid yvagal3
 * @GTID 903379154
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public LinearProbingHashMap(int initialCapacity) {
        table = new LinearProbingMapEntry[initialCapacity];
        size = 0;
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. Be
     * careful to consider the differences between integer and double
     * division when calculating load factor. Additionally, be very careful to
     * use the correct types when comparing with LF. Comparing types with
     * different precisions may result in unexpected rounding errors.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value is null");
        }
        if ((size + 1.0) / table.length > MAX_LOAD_FACTOR) {
            resizeBackingTable(table.length * 2 + 1);
        }

        int startIndex = Math.abs(key.hashCode() % table.length);
        int remIndex = -1;
        for (int i = 0; i < table.length; i++) {
            if (table[startIndex] == null || (table[startIndex].isRemoved()
                    && table[startIndex].getKey().equals(key))) {
                if (remIndex >= 0) {
                    table[remIndex] = new LinearProbingMapEntry<>(key, value);
                } else {
                    table[startIndex] = new LinearProbingMapEntry<>(key, value);
                }
                size++;
                return null;
            } else if (table[startIndex].isRemoved() && remIndex == -1) {
                remIndex = startIndex;
            } else if (table[startIndex].getKey().equals(key)) {
                V oldKeyVal = table[startIndex].getValue();
                table[startIndex].setValue(value);
                return oldKeyVal;
            } else {
                startIndex = (startIndex + 1) % table.length;
            }
        }
        table[remIndex] = new LinearProbingMapEntry<>(key, value);
        size++;
        return null;
    }

    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key you entered is null");
        }
        int startIndex = Math.abs(key.hashCode() % table.length);
        for (int i = 0; i < table.length; i++) {
            if (table[startIndex] == null || (table[startIndex].isRemoved()
                    && table[startIndex].getKey().equals(key))) {
                throw new NoSuchElementException("key is not in the map");
            } else if (table[startIndex].getKey().equals(key)) {
                table[startIndex].setRemoved(true);
                size--;
                return table[startIndex].getValue();
            } else {
                startIndex = (startIndex + 1) % table.length;
            }
        }
        throw new NoSuchElementException("key is not in the map");
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        for (int i = 0; i < table.length; i++) {
            if (table[index] == null) {
                throw new NoSuchElementException("Key is not in the map");
            } else if (table[index].getKey().equals(key)) {
                if (table[index].isRemoved()) {
                    throw new NoSuchElementException("Key is not in the map");
                }
                return table[index].getValue();
            }
            index = (index + 1) % table.length;
        }
        throw new NoSuchElementException("Key is not in the map");
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                return !table[index].isRemoved();
            }
            index = (index + 1) % table.length;
        }
        return false;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (LinearProbingMapEntry<K, V> entry : table) {
            if (entry != null && !entry.isRemoved()) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        List<V> list = new ArrayList<>();
        for (LinearProbingMapEntry<K, V> entry : table) {
            if (entry != null && !entry.isRemoved()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed. 
     * You should NOT copy over removed elements to the resized backing table.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("length is less than the number of items in the hash map");
        }
        LinearProbingMapEntry<K, V>[] temp = new LinearProbingMapEntry[length];
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                int newIndex = Math.abs(table[i].getKey().hashCode() % length);
                for (int iter = 0; iter < length; iter++) {
                    if (temp[newIndex] == null) {
                        temp[newIndex] = table[i];
                        break;
                    } else {
                        newIndex = (newIndex + 1) % length;
                    }
                }
                count++;
                if (count == size) {
                    break;
                }
            }
        }
        table = temp;
    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {
        table = new LinearProbingMapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public LinearProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}