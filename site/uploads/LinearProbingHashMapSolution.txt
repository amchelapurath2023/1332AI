import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Your implementation of a LinearProbingHashMap.
 *
 * @author Jaemin Cheong
 * @version 1.0
 * @userid jcheong8
 * @GTID 903855109
 * <p>
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 * <p>
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     * <p>
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     * <p>
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     * <p>
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     * <p>
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     * <p>
     * The backing array should have an initial capacity of initialCapacity.
     * <p>
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
     * <p>
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     * <p>
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
     * <p>
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     * <p>
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("The key and value cannot be null.");
        }
        if (((((double) (size + 1)) / ((double) table.length)) > MAX_LOAD_FACTOR)) {
            resizeBackingTable(2 * table.length + 1);
        }
        int recalculatedKey = Math.abs((int) key % table.length);
        int firstDel = -1;
        int increment = 0;
        boolean delFound = false;
        int entiresSeen = 0;
        while (table[Math.abs((recalculatedKey + increment) % table.length)] != null
                && (table[Math.abs((recalculatedKey + increment) % table.length)].getKey() != key
            )
        ) {
            if (table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved() && !delFound) {
                firstDel = Math.abs((recalculatedKey + increment) % table.length);
                delFound = true;
            }
            if (entiresSeen == size) {
                break;
            }
            if (!table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()) {
                entiresSeen++;
            }

            if (increment == table.length) {
                break;
            }
            increment++;
        }

        if (increment == table.length) {
            size++;
            table[firstDel].setValue(value);
            table[firstDel].setKey(key);
            table[firstDel].setRemoved(false);
            return null;
        } else {
            if (table[Math.abs((recalculatedKey + increment) % table.length)] == null && !delFound) {
                LinearProbingMapEntry<K, V> element = new LinearProbingMapEntry<>(key, value);
                size++;
                table[Math.abs((recalculatedKey + increment) % table.length)] = element;
                return null;
            } else if (table[Math.abs((recalculatedKey + increment) % table.length)] != null
                    && table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key)
            ) {
                if (table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()
                        && -1 == firstDel) {
                    table[Math.abs((recalculatedKey + increment) % table.length)].setValue(value);
                    table[Math.abs((recalculatedKey + increment) % table.length)].setKey(key);
                    size++;
                    table[Math.abs((recalculatedKey + increment) % table.length)].setRemoved(false);
                    return null;
                } else if (!table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()) {
                    V save = table[Math.abs((recalculatedKey + increment) % table.length)].getValue();
                    table[Math.abs((recalculatedKey + increment) % table.length)].setValue(value);
                    table[Math.abs((recalculatedKey + increment) % table.length)].setKey(key);
                    return save;
                }
            }
            if (delFound) {
                size++;
                table[firstDel].setValue(value);
                table[firstDel].setKey(key);
                table[firstDel].setRemoved(false);
                return null;
            }
        }
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
            throw new IllegalArgumentException("The key is null.");
        }
        int recalculatedKey = Math.abs((int) key % table.length);
        int increment = 0;
        int entiresSeen = 0;
        while (table[Math.abs((recalculatedKey + increment) % table.length)] != null
                && !table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key)
                && table[Math.abs((recalculatedKey + increment) % table.length)].getValue() != null) {
            if (entiresSeen == size) {
                break;
            }
            if (!table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()) {
                entiresSeen++;
            }
            if (increment == table.length) {
                break;
            }
            increment++;
        }
        if (table[Math.abs((recalculatedKey + increment) % table.length)] == null
                || table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()
        ) {
            throw new NoSuchElementException("The key is already not in the map.");
        }
        V save = table[Math.abs((recalculatedKey + increment) % table.length)].getValue();
        table[Math.abs((recalculatedKey + increment) % table.length)].setRemoved(true);
        size--;
        return save;
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
            throw new IllegalArgumentException("The key cannot be null.");
        }
        int entiresSeen = 0;
        int recalculatedKey = Math.abs((int) key % table.length);
        int increment = 0;
        while (table[Math.abs((recalculatedKey + increment) % table.length)] != null
                && !table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key)
                && table[Math.abs((recalculatedKey + increment) % table.length)].getValue() != null) {
            if (entiresSeen == size) {
                break;
            }
            if (!table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()) {
                entiresSeen++;
            }
            increment++;
            if (increment == table.length) {
                break;
            }
        }
        if (table[Math.abs((recalculatedKey + increment) % table.length)] == null || increment == table.length
                || (entiresSeen == size
                && !table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key))
                || (table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key)
                && table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved())) {
            throw new NoSuchElementException("The key is not in the map.");
        }

        return table[Math.abs((recalculatedKey + increment) % table.length)].getValue();
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
            throw new IllegalArgumentException("Key cannot be null.");
        }
        int recalculatedKey = Math.abs((int) key % table.length);
        int increment = 0;
        int entiresSeen = 0;

        while (table[Math.abs((recalculatedKey + increment) % table.length)] != null
                && !table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key)
                && table[Math.abs((recalculatedKey + increment) % table.length)].getValue() != null) {
            if (entiresSeen == size) {
                break;
            }
            if (!table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()) {
                entiresSeen++;
            }
            increment++;
        }
        if (table[Math.abs((recalculatedKey + increment) % table.length)] == null
                || (entiresSeen == size
                && (!table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key))
                || (table[Math.abs((recalculatedKey + increment) % table.length)].getKey().equals(key)
                && table[Math.abs((recalculatedKey + increment) % table.length)].isRemoved()))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     * <p>
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        HashSet<K> hashSet = new HashSet<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                hashSet.add(table[i].getKey());
            }
        }
        return hashSet;
    }

    /**
     * Returns a List view of the values contained in this map.
     * <p>
     * Use java.util.ArrayList or java.util.LinkedList.
     * <p>
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        ArrayList<V> arrayList = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                arrayList.add(table[i].getValue());
            }
        }
        return arrayList;
    }

    /**
     * Resize the backing table to length.
     * <p>
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     * <p>
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed.
     * You should NOT copy over removed elements to the resized backing table.
     * <p>
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     * <p>
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("The length cannot be smaller than the "
                    + "number of items in the hash map.");
        }
        LinearProbingMapEntry<K, V>[] placeHolder = table;
        table = new LinearProbingMapEntry[length];
        for (int i = 0; i < placeHolder.length; i++) {
            int increment = 0;
            if (placeHolder[i] != null && !placeHolder[i].isRemoved()) {
                int recalculatedKey = Math.abs((int) placeHolder[i].getKey() % length);
                while (table[Math.abs((recalculatedKey + increment) % length)] != null
                        && table[Math.abs((recalculatedKey + increment) % length)].getKey() != placeHolder[i].getKey()
                        && table[Math.abs((recalculatedKey + increment) % length)].getValue() != null) {
                    increment++;
                }

                LinearProbingMapEntry<K, V> element
                        = new LinearProbingMapEntry<>(placeHolder[i].getKey(), placeHolder[i].getValue());
                table[Math.abs((recalculatedKey + increment) % length)] = element;
            }
        }
    }

    /**
     * Clears the map.
     * <p>
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     * <p>
     * Must be O(1).
     */
    public void clear() {
        table = new LinearProbingMapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     * <p>
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
     * <p>
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
