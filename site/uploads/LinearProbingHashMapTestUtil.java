import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Grading util for LinearProbingHashMap.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */
class LinearProbingHashMapTestUtil {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private LinearProbingMapEntry<TestObject, String>[] expected;
    private LinearProbingHashMap<TestObject, String> actual;
    private static Map<String, List<String>> persistentChecks;

    static {
        persistentChecks = new HashMap<>();
        persistentChecks.put("put_return", null);
        persistentChecks.put("put_size", null);
        persistentChecks.put("put_equals", null);
        persistentChecks.put("remove_return", null);
        persistentChecks.put("remove_equals", null);
        persistentChecks.put("remove_size", null);
        persistentChecks.put("get_equals", null);
        persistentChecks.put("get_unmodified", null);
        persistentChecks.put("containsKey_equals", null);
        persistentChecks.put("containsKey_unmodified", null);
        persistentChecks.put("keySet_unmodified", null);
        persistentChecks.put("values_unmodified", null);
        persistentChecks.put("resizeBackingTable_size", null);
    }

    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    // HELPER METHODS

    /**
     * Prints the non-null entries of the persistent check map
     */
    @SuppressWarnings("unused")
    static void printPersistentChecks() {
        for (String key : persistentChecks.keySet()) {
            List<String> value = persistentChecks.get(key);
            if (value != null && value.size() != 0) {
                System.out.println("Key: " + key);
                System.out.println("Value: " + persistentChecks.get(key));
            }
        }
    }

    /**
     * Checks the persistent check map given a key
     *
     * @param key the key to check
     */
    static void checkPersistentCheck(String key) {
        if (!persistentChecks.containsKey(key)) {
            throw new IllegalArgumentException("Key should already be in "
                    + "persistent check map");
        }
        List<String> value = persistentChecks.get(key);
        assertNotNull(key + " persistent check was never tested by any method",
                value);
        assertTrue(key + " persistent check was failed by the following "
                + "tests: " + value, value.isEmpty());
    }

    /**
     * Returns the test name and line number that the persistent check failed at
     * for the given method name.
     *
     * @param methodName the name of the method being tests
     * @return the test name and line number if it exists
     * @throws java.lang.IllegalArgumentException if the methodName is null
     * @throws java.lang.IllegalStateException    if no line number was found
     */
    private static String persistentCheckLine(String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name cannot be null");
        }
        StackTraceElement[] calls = (new Throwable()).getStackTrace();
        for (StackTraceElement call : calls) {
            if (call.getClassName().endsWith("Test")
                    && call.getMethodName().startsWith(methodName)
                    && call.getMethodName().charAt(methodName.length()) >= '0'
                    && call.getMethodName().charAt(methodName.length()) <= '9') {
                return "(" + call.getMethodName() + ", "
                        + call.getLineNumber() + ")";
            }
        }
        throw new IllegalStateException("No line number was found.");
    }

    /**
     * Adds a new empty list to the persistentChecks map if one does not
     * exist already
     *
     * @param key the key to check the map
     */
    @SuppressWarnings("all")
    private static void computeIfAbsent(String key) {
        if (persistentChecks.get(key) == null) {
            persistentChecks.put(key, new LinkedList<>());
        }
    }

    /**
     * Converts the expected and actual structures to a string
     *
     * @return the stringified version of the structures
     */
    private String getContent() {
        String result = "Expected: " + Arrays.toString(expected);
        result += "\n";
        result += "Actual: " + tableToString();
        return result;
    }

    /**
     * Turns a table into a string
     *
     * @return the stingified version of the actual table
     */
    private String tableToString() {
        StringBuilder result = new StringBuilder("[");
        for (LinearProbingMapEntry<TestObject, String> entry
                : actual.getTable()) {
            if (entry == null) {
                result.append("null");
            } else {
                String key = entry.getKey() == null ? "null"
                        : entry.getKey().toString();
                String value = entry.getValue() == null ? "null"
                        : entry.getValue();
                result.append(String.format("(%s, %s)", key, value));
            }
            result.append(", ");
        }
        result.setLength(result.length() - 2);
        result.append("]");
        return result.toString();
    }

    /**
     * Turns a table string into a map entry
     *
     * @param segment the stringified map entry
     * @return the map entry
     */
    private static LinearProbingMapEntry<TestObject, String> mapEntryFromString(
            String segment) {
        if (segment.equals("_")) {
            return null;
        } else {
            boolean removed;
            LinearProbingMapEntry<TestObject, String> result;
            if (segment.startsWith("x")) {
                removed = true;
                segment = segment.replaceAll("^x", "").trim();
            } else {
                removed = false;
            }
            if (segment.contains(",")) {
                segment = segment.trim().replaceAll("^\\(|\\)$", "")
                        .trim();
                String[] entry = segment.split(",");
                int key;
                switch (entry[0]) {
                    case "MAX":
                        key = Integer.MAX_VALUE;
                        break;
                    case "MIN":
                        key = Integer.MIN_VALUE;
                        break;
                    default:
                        key = Integer.parseInt(entry[0]);
                }
                String value = entry[1];
                result =
                        new LinearProbingMapEntry<>(new TestObject(key),
                                value);
            } else {
                int data;
                switch (segment) {
                    case "MAX":
                        data = Integer.MAX_VALUE;
                        break;
                    case "MIN":
                        data = Integer.MIN_VALUE;
                        break;
                    default:
                        data = Integer.parseInt(segment);
                }
                result = new LinearProbingMapEntry<>(new
                        TestObject(data), segment);
            }
            result.setRemoved(removed);
            return result;
        }
    }

    /**
     * Turns a table string into a table
     *
     * @param table the string representing the table
     * @return the table that resulted from the string and the size
     */
    @SuppressWarnings("unchecked")
    private static Pair<LinearProbingMapEntry<TestObject, String>[], Integer>
    tableFromString(String table) {
        if (table == null) {
            throw new IllegalArgumentException("Table string cannot be null");
        }
        table = table.trim().replaceAll("^\\[|]$", "")
                .trim();
        LinearProbingMapEntry<TestObject, String>[] result;
        int size;
        if (table.length() == 0) {
            result = (LinearProbingMapEntry<TestObject, String>[])
                    new LinearProbingMapEntry[0];
            size = 0;
        } else {
            size = 0;
            String[] segments = table.split(", ");
            result = (LinearProbingMapEntry<TestObject, String>[])
                    new LinearProbingMapEntry[segments.length];
            for (int i = 0; i < segments.length; i++) {
                String segment = segments[i].trim();
                LinearProbingMapEntry<TestObject, String> entry =
                        mapEntryFromString(segment);
                result[i] = entry;
                if (entry != null && !entry.isRemoved()) {
                    size++;
                }
            }
        }
        return new Pair<>(result, size);
    }

    // SETUP METHODS

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    LinearProbingHashMapTestUtil() {
        expected = (LinearProbingMapEntry<TestObject, String>[])
                new LinearProbingMapEntry[LinearProbingHashMap.INITIAL_CAPACITY];
        actual = new LinearProbingHashMap<>();
    }

    /**
     * Sets the fields of the LinearProbingHashMap using reflection to avoid
     * dependencies in unit testing.
     *
     * @param table the new table
     * @param size  the new size
     */
    private void setHashMap(LinearProbingMapEntry<TestObject, String>[] table,
                            int size) {
        if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        try {
            Field field = LinearProbingHashMap.class.getDeclaredField("table");
            field.setAccessible(true);
            field.set(actual, table);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            Field field = LinearProbingHashMap.class.getDeclaredField("size");
            field.setAccessible(true);
            field.set(actual, size);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and sets the expected variable to an array of map entries.
     *
     * Creates and sets the actual variable to a map represented by the string.
     *
     * @param table the string representing the table
     */
    private void createLinearProbingHashMap(String table) {
        expected = tableFromString(table).getKey();
        Pair<LinearProbingMapEntry<TestObject, String>[], Integer> result =
                tableFromString(table);
        setHashMap(result.getKey(), result.getValue());
    }

    /**
     * Creates and sets the expected variable to the expected array of map
     * entries.
     *
     * Creates and sets the actual variable to a map represented by the actual
     * string.
     *
     * @param expectedMap string representing the expected map
     * @param actualMap   string representing the actual map
     */
    private void createLinearProbingHashMapDifferent(String expectedMap,
                                                     String actualMap) {
        expected = tableFromString(expectedMap).getKey();
        Pair<LinearProbingMapEntry<TestObject, String>[], Integer> result =
                tableFromString(actualMap);
        setHashMap(result.getKey(), result.getValue());
    }

    // ASSERT METHODS

    /**
     * Verifies that the actual return value is the same as the expected
     * return value.
     *
     * If used with null as the method name, it does the standard assertion.
     * If used with an actual method name, it will perform the assertion and
     * mark the corresponding error in the persistent check map.
     *
     * @param methodName     the name of the assignment method or null if just
     *                       the assertion is wanted
     * @param expectedReturn the expected return value
     * @param actualReturn   the actual return value
     * @param <T>            the type of object to compare
     */
    private static <T> void assertReturnEquals(String methodName,
                                               T expectedReturn,
                                               T actualReturn) {
        String message = "\nExpected: " + expectedReturn + "\nActual: "
                + actualReturn + "\n";
        if (methodName == null) {
            assertEquals("Unexpected return value" + message, expectedReturn,
                    actualReturn);
        } else {
            String checkName = methodName + "_return";
            if (!persistentChecks.containsKey(checkName)) {
                throw new IllegalArgumentException("Method name should "
                        + "already be in persistent check map");
            }
            computeIfAbsent(checkName);
            if ((expectedReturn == null && actualReturn != null)
                    || (expectedReturn != null && !expectedReturn
                    .equals(actualReturn))) {
                persistentChecks.get(checkName)
                        .add(persistentCheckLine(methodName) + message);
            }
        }
    }

    /**
     * Verifies that the actual table is the same as the expected table.
     */
    private void assertTableEquals() {
        if (expected == null) {
            throw new IllegalStateException("Expected cannot be null");
        } else if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        LinearProbingMapEntry<TestObject, String>[] table = actual.getTable();
        if (table == null) {
            throw new IllegalStateException("Table cannot be null");
        }
        String content = "\n" + getContent() + "\n";
        assertEquals("Unexpected table length", expected.length,
                table.length);
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] == null) {
                assertNull("Unexpected non-null index: " + i + content,
                        table[i]);
            } else if (expected[i].isRemoved()) {
                assertNotNull("Unexpected null index: " + i + content,
                        table[i]);
                assertTrue("Unexpected non-removed index: " + i + content,
                        table[i].isRemoved());
            } else {
                assertNotNull("Unexpected null index: " + i + content,
                        table[i]);
                assertFalse("Unexpected removed index: " + i + content,
                        table[i].isRemoved());
                assertEquals("Unexpected table content" + content,
                        expected[i], table[i]);
            }
        }
    }

    /**
     * Verifies that the actual size is the same as the expected size.
     *
     * If used with null as the method name, it does the standard assertion.
     * If used with an actual method name, it will perform the assertion and
     * mark the corresponding error in the persistent check map.
     *
     * @param methodName   the name of the assignment method or null if just
     *                     the assertion is wanted
     * @param expectedSize the expected size
     */
    private void assertSizeEquals(String methodName, int expectedSize) {
        if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        int size = actual.size();
        String message = "\nExpected: " + expectedSize + "\nActual: "
                + size + "\n";
        if (methodName == null) {
            assertEquals("Unexpected size" + message, expectedSize, size);
        } else {
            String checkName = methodName + "_size";
            if (!persistentChecks.containsKey(checkName)) {
                throw new IllegalArgumentException("Method name should "
                        + "already be in persistent check map");
            }
            computeIfAbsent(checkName);
            if (expectedSize != size) {
                persistentChecks.get(checkName)
                        .add(persistentCheckLine(methodName) + message);
            }
        }
    }

    /**
     * Verifies that the table and size have not changed.
     *
     * It will perform the assertion and mark the corresponding error in the
     * persistent check map.
     *
     * @param methodName   the name of the assignment method
     * @param expectedSize the expected size of the list
     */
    private void assertUnmodified(String methodName, int expectedSize) {
        if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        if (methodName == null) {
            assertEquals(expectedSize, actual.size());
            assertArrayEquals(expected, actual.getTable());
        } else {
            String content = "\n" + getContent() + "\n";
            String checkName = methodName + "_unmodified";
            if (!persistentChecks.containsKey(checkName)) {
                throw new IllegalArgumentException("Method name should "
                        + "already be in persistent check map");
            }
            computeIfAbsent(checkName);
            if ((expectedSize != actual.size()) || !Arrays.equals(expected,
                    actual.getTable())) {
                persistentChecks.get(checkName)
                        .add(persistentCheckLine(methodName) + content);
            }
        }
    }

    // EXCEPTION METHODS

    /**
     * Tests put with null key / value
     *
     * @param table the string representing the table
     */
    void putNullData(String table) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        try {
            actual.put(null, "");
        } catch (IllegalArgumentException e1) {
            assertEquals(IllegalArgumentException.class, e1.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            try {
                actual.put(new TestObject(0), null);
            } catch (IllegalArgumentException e2) {
                assertEquals(IllegalArgumentException.class, e2.getClass());

                // Unmodified structure check
                assertUnmodified(null, size);

                throw e2;
            }
        }
    }

    /**
     * Tests remove with null data
     *
     * @param table the string representing the table
     */
    @SuppressWarnings("SameParameterValue")
    void removeNullData(String table) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        if (size == 0) {
            throw new RuntimeException("Hash map cannot be empty");
        }
        try {
            actual.remove(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            throw e;
        }
    }

    /**
     * Tests remove with data not in map
     *
     * @param table the string representing the table
     * @param data the data to find in the tree
     */
    void removeNoSuchElement(String table, int data) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        String methodName = "remove";
        int size = actual.size();
        try {
            actual.remove(new TestObject(data));
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElementException.class, e.getClass());

            assertTableEquals();
            assertSizeEquals(methodName, size);

            throw e;
        }
    }

    /**
     * Tests get with null data
     *
     * @param table the string representing the table
     */
    @SuppressWarnings("SameParameterValue")
    void getNullData(String table) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        if (size == 0) {
            throw new RuntimeException("Hash map cannot be empty");
        }
        try {
            actual.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            throw e;
        }
    }

    /**
     * Tests get with data not in map
     *
     * @param table the string representing the table
     * @param data the data to find in the tree
     */
    void getNoSuchElement(String table, int data) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        try {
            actual.get(new TestObject(data));
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            throw e;
        }
    }

    /**
     * Tests contains with null data
     *
     * @param table the string representing the table
     */
    @SuppressWarnings("SameParameterValue")
    void containsNullData(String table) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        if (size == 0) {
            throw new RuntimeException("Hash map cannot be empty");
        }
        try {
            actual.containsKey(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            throw e;
        }
    }

    /**
     * Tests resizeBackingTable with negative length
     *
     * @param table the string representing the table
     * @param length the length to resize the backing table to
     */
    @SuppressWarnings("SameParameterValue")
    void resizeBackingTableNegativeLength(String table, int length) {
        if (length >= 0) {
            throw new RuntimeException("Length should be negative");
        }
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        try {
            actual.resizeBackingTable(length);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            throw e;
        }
    }

    /**
     * Tests resizeBackingTable with small length
     *
     * @param table the string representing the table
     * @param length the length to resize the backing table to
     */
    @SuppressWarnings("SameParameterValue")
    void resizeBackingTableSmallLength(String table, int length) {
        try {
            createLinearProbingHashMap(table);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        int size = actual.size();
        if (length >= size) {
            throw new RuntimeException("Length should be smaller than " + size);
        }
        try {
            actual.resizeBackingTable(length);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());

            // Unmodified structure check
            assertUnmodified(null, size);

            throw e;
        }
    }

    // RUNNER METHODS

    /**
     * Runner for the constructor tests to reduce code redundancy
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    @SuppressWarnings({"SameParameterValue", "unchecked"})
    void constructorRunner(int initialCapacity) {
        actual = new LinearProbingHashMap<>(initialCapacity);
        expected = (LinearProbingMapEntry<TestObject, String>[])
                new LinearProbingMapEntry[initialCapacity];
        assertTableEquals();
        assertSizeEquals(null, 0);
    }

    /**
     * Runner for the put tests to reduce code redundancy.
     *
     * @param expectedMap  string representing the expected map
     * @param actualMap    string representing the actual map
     * @param toAdd        the numbers to add into the actual map
     * @param toAddIndices the indices to add the numbers in the actual map
     */
    void putRunner(String expectedMap, String actualMap, String[] toAdd,
                   Integer[] toAddIndices) {
        if (toAdd == null) {
            throw new IllegalArgumentException("To add cannot be null");
        } else if (toAddIndices == null) {
            throw new IllegalArgumentException("To add indices cannot be null");
        } else if (toAdd.length != toAddIndices.length) {
            throw new IllegalArgumentException("To add array and to add "
                    + "indices array cannot have different lengths");
        }

        // Testing with reference equal keys
        String methodName = "put";
        createLinearProbingHashMapDifferent(expectedMap, actualMap);
        int expectedSize = actual.size();
        for (int i = 0; i < toAdd.length; i++) {
            LinearProbingMapEntry<TestObject, String> paramEntry =
                    mapEntryFromString(toAdd[i]);
            LinearProbingMapEntry<TestObject, String> entry =
                    toAddIndices[i] == null ? null : actual
                            .getTable()[toAddIndices[i]];
            String retValue;
            TestObject key = paramEntry.getKey();
            String value = paramEntry.getValue();
            if (entry != null && !entry.isRemoved()) {
                retValue = entry.getValue();
                key = entry.getKey();
            } else {
                retValue = null;
                expectedSize++;
            }
            assertReturnEquals(methodName, retValue, actual.put(key, value));
        }
        assertTableEquals();
        assertSizeEquals(methodName, expectedSize);

        // Testing with value equal keys
        String checkName = methodName + "_equals";
        computeIfAbsent(checkName);
        createLinearProbingHashMapDifferent(expectedMap, actualMap);
        try {
            for (int i = 0; i < toAdd.length; i++) {
                LinearProbingMapEntry<TestObject, String> paramEntry =
                        mapEntryFromString(toAdd[i]);
                TestObject key = paramEntry.getKey();
                String value = paramEntry.getValue();
                actual.put(key, value);
            }
            assertTableEquals();
        } catch (AssertionError | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the remove tests to reduce code redundancy.
     *
     * @param expectedMap     string representing the expected map
     * @param actualMap       string representing the actual map
     * @param toRemoveIndices the indices of the numbers to remove in the
     *                        actual map
     */
    void removeRunner(String expectedMap, String actualMap,
                      int... toRemoveIndices) {
        // Testing with reference equal keys
        String methodName = "remove";
        createLinearProbingHashMapDifferent(expectedMap, actualMap);
        LinearProbingMapEntry<TestObject, String>[] table = actual.getTable();
        int expectedSize = actual.size() - toRemoveIndices.length;
        for (int index : toRemoveIndices) {
            if (index < 0 || index >= table.length) {
                throw new IllegalArgumentException("Deleted index cannot be "
                        + "outside the range [0, " + table.length + ")");
            }
            assertReturnEquals(methodName, table[index].getValue(),
                    actual.remove(table[index].getKey()));
        }
        assertTableEquals();
        assertSizeEquals(methodName, expectedSize);

        // Testing with value equal keys
        String checkName = methodName + "_equals";
        computeIfAbsent(checkName);
        createLinearProbingHashMapDifferent(expectedMap, actualMap);
        table = actual.getTable();
        try {
            for (int index : toRemoveIndices) {
                actual.remove(new TestObject(table[index].getKey().num));
            }
            assertTableEquals();
        } catch (AssertionError | NoSuchElementException
                 | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the get tests to reduce code redundancy.
     *
     * @param table  the string representing the table
     * @param toGetIndices the indices of the numbers to get
     */
    void getRunner(String table, int... toGetIndices) {
        // Testing with reference equal keys
        String methodName = "get";
        createLinearProbingHashMap(table);
        LinearProbingMapEntry<TestObject, String>[] actualTable = actual
                .getTable();
        int expectedSize = actual.size();
        for (int index : toGetIndices) {
            if (index < 0 || index >= actualTable.length) {
                throw new IllegalArgumentException("Deleted index cannot be "
                        + "outside the range [0, " + actualTable.length + ")");
            }
            assertReturnEquals(null, actualTable[index].getValue(),
                    actual.get(actualTable[index].getKey()));
        }
        assertUnmodified(methodName, expectedSize);

        // Testing with value equal keys
        String checkName = methodName + "_equals";
        computeIfAbsent(checkName);
        createLinearProbingHashMap(table);
        actualTable = actual.getTable();
        try {
            for (int index : toGetIndices) {
                TestObject key = new TestObject(expected[index].getKey().num);
                assertReturnEquals(null, actualTable[index].getValue(),
                        actual.get(key));
            }
        } catch (AssertionError | NoSuchElementException
                 | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the containsKey tests to reduce code redundancy.
     *
     * @param table the string representing the table
     * @param toCheck     the numbers to check
     */
    void containsKeyRunner(String table, int... toCheck) {
        // Testing with reference equal keys
        String methodName = "containsKey";
        createLinearProbingHashMap(table);
        Integer[] toCheckIndices = new Integer[toCheck.length];
        for (int i = 0; i < toCheck.length; i++) {
            for (int j = 0; j < expected.length; j++) {
                LinearProbingMapEntry<TestObject, String> entry = expected[j];
                if (entry != null && entry.getKey().num == toCheck[i]
                        && !entry.isRemoved()) {
                    toCheckIndices[i] = j;
                }
            }
        }
        LinearProbingMapEntry<TestObject, String>[] actualTable = actual
                .getTable();
        int expectedSize = actual.size();
        for (int i = 0; i < toCheck.length; i++) {
            TestObject key = toCheckIndices[i] == null
                    ? new TestObject(toCheck[i])
                    : actualTable[toCheckIndices[i]].getKey();
            assertReturnEquals(null, toCheckIndices[i] != null,
                    actual.containsKey(key));
        }
        assertUnmodified(methodName, expectedSize);

        // Testing with value equal keys
        String checkName = methodName + "_equals";
        computeIfAbsent(checkName);
        createLinearProbingHashMap(table);
        try {
            for (int i = 0; i < toCheck.length; i++) {
                TestObject key = new TestObject(toCheck[i]);
                assertReturnEquals(null, toCheckIndices[i] != null,
                        actual.containsKey(key));
            }
        } catch (AssertionError | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the keySet tests to reduce code redundancy.
     *
     * @param table the string representing the table
     */
    void keySetRunner(String table) {
        String methodName = "keySet";
        createLinearProbingHashMap(table);
        int expectedSize = actual.size();
        Set<TestObject> set = new HashSet<>();
        for (LinearProbingMapEntry<TestObject, String> entry : expected) {
            if (entry != null && !entry.isRemoved()) {
                set.add(entry.getKey());
            }
        }
        assertEquals(methodName, set, actual.keySet());
        assertUnmodified(methodName, expectedSize);
    }

    /**
     * Runner for the values tests to reduce code redundancy.
     *
     * @param table the string representing the table
     */
    void valuesRunner(String table) {
        String methodName = "values";
        createLinearProbingHashMap(table);
        int expectedSize = actual.size();
        List<String> list = new LinkedList<>();
        for (LinearProbingMapEntry<TestObject, String> entry : expected) {
            if (entry != null && !entry.isRemoved()) {
                list.add(entry.getValue());
            }
        }
        assertEquals(methodName, list, actual.values());
        assertUnmodified(methodName, expectedSize);
    }

    /**
     * Runner for the resizeBackingTable tests to reduce code redundancy.
     *
     * @param expectedMap string representing the expected map
     * @param actualMap   string representing the actual map
     * @param length      new length of the backing table
     */
    void resizeBackingTableRunner(String expectedMap, String actualMap,
                                  int length) {
        String methodName = "resizeBackingTable";
        createLinearProbingHashMapDifferent(expectedMap, actualMap);
        int expectedSize = actual.size();
        actual.resizeBackingTable(length);
        assertTableEquals();
        assertSizeEquals(methodName, expectedSize);
    }

    /**
     * Runner for the clear tests to reduce code redundancy.
     *
     * @param table string representing the actual map
     * @param sizeOnly whether to check only if the size is reset(if true) or
     *                 if the structure is cleared (if false)
     */
    void clearRunner(String table, boolean sizeOnly) {
        createLinearProbingHashMapDifferent("[_, _, _, _, _, _, _, _, _, _, "
                + "_, _, _]", table);
        actual.clear();
        if (!sizeOnly) {
            assertTableEquals();
        } else {
            assertSizeEquals(null, 0);
        }
    }

    //*************************************************************************
    //************************   AUXILIARY CLASSES    *************************
    //*************************************************************************

    private static class TestObject {

        private int num;

        /**
         * Creates a new TestObject
         *
         * @param num the number encapsulated by the object
         */
        TestObject(int num) {
            this.num = num;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TestObject that = (TestObject) o;
            return num == that.num;
        }

        @Override
        public int hashCode() {
            return num;
        }

        @Override
        public String toString() {
            return "TestObject{"
                    + "num=" + num
                    + '}';
        }
    }

    private static class Pair<K, V> {

        private K key;
        private V value;

        /**
         * Creates a new Pair
         *
         * @param key the key for the pair
         * @param value the value for the pair
         */
        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Gets the key
         *
         * @return the key
         */
        private K getKey() {
            return key;
        }

        /**
         * Gets the value
         *
         * @return the value
         */
        private V getValue() {
            return value;
        }
    }
}