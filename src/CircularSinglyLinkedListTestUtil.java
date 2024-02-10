import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Grading util for CircularSinglyLinkedList.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */
class CircularSinglyLinkedListTestUtil {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private TestObject[] expected;
    private CircularSinglyLinkedList<TestObject> actual;
    private static Map<String, List<String>> persistentChecks;

    static {
        persistentChecks = new HashMap<>();
        persistentChecks.put("addAtIndex_size", null);
        persistentChecks.put("addToFront_size", null);
        persistentChecks.put("addToBack_size", null);
        persistentChecks.put("removeAtIndex_returnValue", null);
        persistentChecks.put("removeAtIndex_size", null);
        persistentChecks.put("removeFromFront_returnValue", null);
        persistentChecks.put("removeFromFront_size", null);
        persistentChecks.put("removeFromBack_returnValue", null);
        persistentChecks.put("removeFromBack_size", null);
        persistentChecks.put("get_unmodified", null);
        persistentChecks.put("isEmpty_unmodified", null);

        // Spring 2020
        persistentChecks.put("removeLastOccurrence_returnValue", null);
        persistentChecks.put("removeLastOccurrence_returnReference", null);
        persistentChecks.put("removeLastOccurrence_equals", null);
        persistentChecks.put("removeLastOccurrence_size", null);
        persistentChecks.put("toArray_unmodified", null);
    }

    //*************************************************************************
    //************************    AUXILIARY METHODS    ************************
    //*************************************************************************

    // HELPER METHODS

    /**
     * Checks the persistent check map given a key
     *
     * @param key the key to check
     */
    static void checkPersistentCheck(String key) {
        if (!persistentChecks.containsKey(key)) {
            throw new IllegalArgumentException("Key must already be in "
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
     * @param methodName the name of the method being tested
     * @return the test name and line number if it exists
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
        throw new IllegalStateException("Line number was not found");
    }

    /**
     * Adds a new empty list to the persistentChecks map if one does not
     * exist already
     *
     * @param key the key to check the map
     */
    @SuppressWarnings("all")
    private static void computeIfAbsent(String key) {
        if (!persistentChecks.containsKey(key)) {
            throw new IllegalArgumentException("Key must already be in "
                + "persistent check map");
        }
        if (persistentChecks.get(key) == null) {
            persistentChecks.put(key, new LinkedList<>());
        }
    }

    /**
     * Converts the actual linked list to a string
     *
     * @return the stringified version of the list
     */
    private String linkedListToString() {
        if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        Set<CircularSinglyLinkedListNode<TestObject>> set = new HashSet<>();
        StringBuilder result = new StringBuilder("[");
        CircularSinglyLinkedListNode<TestObject> head = actual.getHead();
        if (head != null) {
            result.append(head.getData());
            if (head.getNext() != head) {
                result.append(", ");
            }
            for (CircularSinglyLinkedListNode<TestObject> cur =
                 head.getNext(); cur != head; cur = cur.getNext()) {
                if (cur == null) {
                    throw new IllegalArgumentException("Cannot contain null "
                        + "in the list");
                }
                if (set.contains(cur)) {
                    return "Cannot stringify list since there is an infinite "
                        + "loop";
                }
                set.add(cur);
                result.append(cur.getData());
                if (cur.getNext() != head) {
                    result.append(", ");
                }
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Converts the expected and actual structures to a string
     *
     * @return the stringified version of the structures
     */
    private String getContent() {
        if (expected == null) {
            throw new IllegalStateException("Expected cannot be null");
        }
        String result = "Expected: " + Arrays.toString(expected);
        result += "\n";
        result += "Actual: " + linkedListToString();
        return result;
    }

    /**
     * Gets the data from the linked list at the index specified
     *
     * @param index the index of the data
     * @return the data from the linked list at that index
     */
    private TestObject getData(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be < 0");
        } else if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        CircularSinglyLinkedListNode<TestObject> cur = actual.getHead();
        int i = 0;
        while (i < index && cur != null) {
            cur = cur.getNext();
            i++;
        }
        if (cur == null) {
            throw new IllegalArgumentException("Linked list ended before "
                + "reaching index");
        }
        return cur.getData();
    }

    /**
     * Creates an int array from 0 to end
     *
     * @param end the last number to include (exclusive, not
     *            inclusive)
     * @return the int array
     */
    private static int[] intArray(int end) {
        if (end < 0) {
            throw new IllegalArgumentException("End cannot be < 0");
        }
        int[] arr = new int[end];
        for (int i = 0; i < end; i++) {
            arr[i] = i;
        }
        return arr;
    }

    /**
     * Reverse sorts an int array
     *
     * @param arr the int array to reverse sort
     */
    private static void reverseSortIntArray(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        Arrays.sort(arr);
        for (int left = 0, right = arr.length - 1; left < right; left++,
            right--) {
            int temp = arr[right];
            arr[right] = arr[left];
            arr[left] = temp;
        }
    }

    // SETUP METHODS

    /**
     * Sets the fields of the CircularSinglyLinkedList using reflection to avoid
     * dependencies in unit testing.
     *
     * @param head the new head
     * @param size the new size
     */
    private void setLinkedList(CircularSinglyLinkedListNode<TestObject> head,
                               int size) {
        actual = new CircularSinglyLinkedList<>();
        try {
            Field field = CircularSinglyLinkedList.class
                .getDeclaredField("head");
            field.setAccessible(true);
            field.set(actual, head);

            field = CircularSinglyLinkedList.class.getDeclaredField("size");
            field.setAccessible(true);
            field.set(actual, size);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and sets the expected variable to an array of consecutive
     * integers.
     *
     * Creates and sets the actual variable to a list of consecutive integers.
     *
     * @param data the data to be stored
     */
    private void createCustomLinkedList(int[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        expected = new TestObject[data.length];
        CircularSinglyLinkedListNode<TestObject> head;
        if (data.length == 0) {
            head = null;
        } else {
            expected[0] = new TestObject(data[0]);
            head = new CircularSinglyLinkedListNode<>(expected[0]);
            CircularSinglyLinkedListNode<TestObject> tail = head;
            for (int i = 1; i < data.length; i++) {
                expected[i] = new TestObject(data[i]);
                CircularSinglyLinkedListNode<TestObject> node =
                    new CircularSinglyLinkedListNode<>(expected[i]);
                tail.setNext(node);
                tail = node;
            }
            tail.setNext(head);
        }
        setLinkedList(head, data.length);
    }

    /**
     * Creates and sets the expected variable to hold an array with the
     * missing numbers omitted.
     *
     * Creates and sets the actual variable to hold a list as usual.
     *
     * @param data              the data to be stored
     * @param missingIndicesSet the indices to omit
     */
    private void createCustomLinkedListExpectedMissing(
        int[] data, Set<Integer> missingIndicesSet
    ) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        } else if (missingIndicesSet == null) {
            throw new IllegalArgumentException("Missing indices set cannot be "
                + "null");
        } else if (missingIndicesSet.size() > data.length) {
            throw new IllegalArgumentException("Number of missing numbers "
                + "cannot be > " + data.length);
        }
        int expectedCapacity = data.length - missingIndicesSet.size();
        expected = new TestObject[expectedCapacity];
        CircularSinglyLinkedListNode<TestObject> head;
        if (data.length == 0) {
            head = null;
        } else {
            head = new CircularSinglyLinkedListNode<>(new TestObject(data[0]));
            CircularSinglyLinkedListNode<TestObject> tail = head;
            int j = 0;
            if (!missingIndicesSet.contains(0)) {
                expected[j++] = head.getData();
            }
            for (int i = 1; i < data.length; i++) {
                CircularSinglyLinkedListNode<TestObject> node =
                    new CircularSinglyLinkedListNode<>(new TestObject(data[i]),
                        tail);
                tail.setNext(node);
                tail = node;
                if (!missingIndicesSet.contains(i)) {
                    expected[j++] = node.getData();
                }
            }
            tail.setNext(head);
        }
        setLinkedList(head, data.length);
    }

    /**
     * Creates and sets the expected variable to hold an array as usual.
     *
     * Creates and sets the actual variable to hold a list with the missing
     * numbers omitted.
     *
     * @param data              the data to be stored
     * @param missingIndicesSet the indices to omit
     */
    private void createCustomLinkedListActualMissing(
        int[] data, Set<Integer> missingIndicesSet
    ) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        } else if (missingIndicesSet == null) {
            throw new IllegalArgumentException("Missing indices set cannot be "
                + "null");
        } else if (missingIndicesSet.size() > data.length) {
            throw new IllegalArgumentException("Number of missing indices "
                + "cannot be > " + data.length);
        }
        expected = new TestObject[data.length];
        CircularSinglyLinkedListNode<TestObject> head;
        if (data.length == 0) {
            head = null;
        } else {
            int j = 0;
            while (j < data.length && missingIndicesSet.contains(j)) {
                expected[j] = new TestObject(data[j]);
                j++;
            }
            if (j < data.length) {
                expected[j] = new TestObject(data[j]);
                head = new CircularSinglyLinkedListNode<>(expected[j++]);
                CircularSinglyLinkedListNode<TestObject> tail = head;
                for (int i = j; i < data.length; i++) {
                    expected[i] = new TestObject(data[i]);
                    if (!missingIndicesSet.contains(i)) {
                        CircularSinglyLinkedListNode<TestObject> node =
                            new CircularSinglyLinkedListNode<>(expected[i],
                                tail);
                        tail.setNext(node);
                        tail = node;
                    }
                }
                tail.setNext(head);
            } else {
                head = null;
            }
        }
        setLinkedList(head, data.length - missingIndicesSet.size());
    }

    /**
     * Creates and sets the expected variable to a sorted array of
     * consecutive integers.
     *
     * Creates and sets the actual variable to a sorted list of consecutive
     * integers.
     *
     * For example:
     * createLinkedList(5) gives us [0, 1, 2, 3, 4].
     *
     * @param end the last number of the interval (exclusive, not inclusive)
     */
    private void createLinkedList(int end) {
        int[] data = intArray(end);
        createCustomLinkedList(data);
    }

    /**
     * Creates and sets the expected variable to hold a sorted array with the
     * missing numbers omitted.
     *
     * Creates and sets the actual variable to hold a sorted list as usual.
     *
     * For example:
     * createLinkedListExpectedMissing(4, {0, 2}) gives us [1, 3] for the
     * expected and [0, 1, 2, 3] for the actual.
     *
     * @param end        the last number to include (exclusive, not inclusive)
     * @param missingArr the numbers to omit
     */
    private void createLinkedListExpectedMissing(int end, int[] missingArr) {
        if (missingArr == null) {
            throw new IllegalArgumentException("Missing array cannot be null");
        }
        Set<Integer> missingSet = new HashSet<>();
        for (int index : missingArr) {
            if (index < 0 || index >= end) {
                throw new IllegalArgumentException("Deleted data cannot be "
                    + "outside the range [0, " + end + ")");
            } else if (missingSet.contains(index)) {
                throw new IllegalArgumentException("Deleted data cannot "
                    + "contain duplicate data");
            }
            missingSet.add(index);
        }
        int[] data = intArray(end);
        createCustomLinkedListExpectedMissing(data, missingSet);
    }

    /**
     * Creates and sets the expected variable to hold a sorted array as usual.
     *
     * Creates and sets the actual variable to hold a sorted list with the
     * missing numbers omitted.
     *
     * For example:
     * createLinkedListActualMissing(4, {0, 2}) gives us [0, 1, 2, 3] for the
     * expected and [1, 3] for the actual.
     *
     * @param end        the last number to include (exclusive, not inclusive)
     * @param missingArr the numbers to omit
     */
    private void createLinkedListActualMissing(int end, int[] missingArr) {
        if (missingArr == null) {
            throw new IllegalArgumentException("Missing array cannot be null");
        }
        Set<Integer> missingSet = new HashSet<>();
        for (int index : missingArr) {
            if (index < 0 || index >= end) {
                throw new IllegalArgumentException("Deleted data cannot be "
                    + "outside the range [0, " + end + ")");
            } else if (missingSet.contains(index)) {
                throw new IllegalArgumentException("Deleted data cannot "
                    + "contain duplicate data");
            }
            missingSet.add(index);
        }
        int[] data = intArray(end);
        createCustomLinkedListActualMissing(data, missingSet);
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
    private static <T> void assertReturnValueEquals(String methodName,
                                                    T expectedReturn,
                                                    T actualReturn) {
        String message = "\nExpected: " + expectedReturn + "\nActual: "
            + actualReturn + "\n";
        if (methodName == null) {
            assertEquals("Unexpected return value" + message, expectedReturn,
                actualReturn);
        } else {
            String checkName = methodName + "_returnValue";
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
     * Verifies that the actual structure is the same as the expected structure.
     */
    private void assertStructureEquals() {
        String content = "\n" + getContent() + "\n";
        if (expected.length == 0) {
            assertNull("Unexpected head in an empty list" + content,
                actual.getHead());
        } else {
            CircularSinglyLinkedListNode<TestObject> cur = actual.getHead();
            for (int i = 0; i < expected.length; i++, cur = cur.getNext()) {
                assertNotNull("Unexpected null node" + content, cur);
                assertSame("Unexpected linked list content" + content,
                    expected[i], cur.getData());
            }
            assertSame("Unexpected tail next pointer", actual.getHead(), cur);
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
            computeIfAbsent(checkName);
            if (expectedSize != size) {
                persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName) + message);
            }
        }
    }

    /**
     * Verifies that the structure and size have not changed.
     *
     * It will perform the assertion and mark the corresponding error in the
     * persistent check map.
     *
     * @param methodName   the name of the assignment method
     * @param expectedSize the expected size of the list
     */
    private void assertUnmodified(String methodName, int expectedSize) {
        if (methodName == null) {
            assertSizeEquals(null, expectedSize);
            assertStructureEquals();
        } else {
            String content = "\n" + getContent() + "\n";
            String checkName = methodName + "_unmodified";
            computeIfAbsent(checkName);
            if (expectedSize == actual.size()) {
                try {
                    assertStructureEquals();
                } catch (AssertionError e) {
                    persistentChecks.get(checkName)
                        .add(persistentCheckLine(methodName) + content);
                }
            } else {
                persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName) + content);
            }
        }
    }

    // EXCEPTION METHODS

    /**
     * Tests addAtIndex with negative indices
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the negative index to try to add to
     */
    void addAtIndexNegativeIndex(int end, int index) {
        if (index >= 0) {
            throw new IllegalArgumentException("Index cannot be >= 0");
        }
        createLinkedList(end);
        try {
            actual.addAtIndex(index, new TestObject(index));
        } catch (IndexOutOfBoundsException e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests addAtIndex with large indices
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the large index to try to add to
     */
    void addAtIndexLargeIndex(int end, int index) {
        if (index <= end) {
            throw new IllegalArgumentException("Index cannot be <= " + end);
        }
        createLinkedList(end);
        try {
            actual.addAtIndex(index, new TestObject(index));
        } catch (IndexOutOfBoundsException e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests addAtIndex with null data
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the index to try to add to
     */
    void addAtIndexNullData(int end, int index) {
        try {
            createLinkedList(end);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        try {
            actual.addAtIndex(index, null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests addToFront with null data
     *
     * @param end the last number to include (exclusive, not inclusive)
     */
    void addToFrontNullData(int end) {
        try {
            createLinkedList(end);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        try {
            actual.addToFront(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests addToBack with null data
     *
     * @param end the last number to include (exclusive, not inclusive)
     */
    void addToBackNullData(int end) {
        try {
            createLinkedList(end);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        try {
            actual.addToBack(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests removeAtIndex with negative indices
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the large index to try to add to
     */
    void removeAtIndexNegativeIndex(int end, int index) {
        if (index >= 0) {
            throw new IllegalArgumentException("Index cannot be >= 0");
        }
        createLinkedList(end);
        try {
            actual.removeAtIndex(index);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests removeAtIndex with large indices
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the large index to try to add to
     */
    void removeAtIndexLargeIndex(int end, int index) {
        if (index < end) {
            throw new IllegalArgumentException("Index cannot be < " + end);
        }
        createLinkedList(end);
        try {
            actual.removeAtIndex(index);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests removeFromFront with empty list
     */
    void removeFromFrontEmpty() {
        createLinkedList(0);
        try {
            actual.removeFromFront();
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElementException.class, e.getClass());
            assertUnmodified(null, 0);
            throw e;
        }
    }

    /**
     * Tests removeFromBack with empty list
     */
    void removeFromBackEmpty() {
        createLinkedList(0);
        try {
            actual.removeFromBack();
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElementException.class, e.getClass());
            assertUnmodified(null, 0);
            throw e;
        }
    }

    /**
     * Tests get with negative indices
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the negative index to try to add to
     */
    void getNegativeIndex(int end, int index) {
        if (index >= 0) {
            throw new IllegalArgumentException("Index cannot be >= 0");
        }
        createLinkedList(end);
        try {
            actual.get(index);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
            assertUnmodified("get", end);
            throw e;
        }
    }

    /**
     * Tests get with large indices
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param index the negative index to try to add to
     */
    void getLargeIndex(int end, int index) {
        if (index < end) {
            throw new IllegalArgumentException("Index cannot be < " + end);
        }
        createLinkedList(end);
        try {
            actual.get(index);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
            assertUnmodified("get", end);
            throw e;
        }
    }

    // RUNNER METHODS

    /**
     * Runner for the addAtIndex tests to reduce code redundancy.
     *
     * @param end   the last number to include (exclusive, not inclusive)
     * @param toAdd the numbers to add into the actual array
     */
    void addAtIndexRunner(int end, int... toAdd) {
        String methodName = "addAtIndex";
        Arrays.sort(toAdd);
        createLinkedListActualMissing(end, toAdd);
        for (int index : toAdd) {
            actual.addAtIndex(index, expected[index]);
        }
        assertStructureEquals();
        assertSizeEquals(methodName, end);
    }

    /**
     * Runner for the addToFront tests to reduce code redundancy.
     *
     * @param end        the last number to include (exclusive, not inclusive)
     * @param iterations the number of times to call the method
     */
    @SuppressWarnings("SameParameterValue")
    void addToFrontRunner(int end, int iterations) {
        if (iterations < 0 || iterations > end) {
            throw new IllegalArgumentException("Iterations cannot be outside "
                + "the range [0, " + end + ")");
        }
        String methodName = "addToFront";
        int[] toAdd = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            toAdd[i] = iterations - i - 1;
        }
        createLinkedListActualMissing(end, toAdd);
        for (int index : toAdd) {
            actual.addToFront(expected[index]);
        }
        assertStructureEquals();
        assertSizeEquals(methodName, end);
    }

    /**
     * Runner for the addToBack tests to reduce code redundancy.
     *
     * @param end        the last number to include (exclusive, not inclusive)
     * @param iterations the number of times to call the method
     */
    @SuppressWarnings("SameParameterValue")
    void addToBackRunner(int end, int iterations) {
        if (iterations < 0 || iterations > end) {
            throw new IllegalArgumentException("Iterations cannot be outside "
                + "the range [0, " + end + ")");
        }
        String methodName = "addToBack";
        int[] toAdd = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            toAdd[i] = end - iterations + i;
        }
        createLinkedListActualMissing(end, toAdd);
        for (int index : toAdd) {
            actual.addToBack(expected[index]);
        }
        assertStructureEquals();
        assertSizeEquals(methodName, end);
    }

    /**
     * Runner for the removeAtIndex tests to reduce code redundancy.
     *
     * @param end      the last number to include (exclusive, not inclusive)
     * @param toRemove the numbers to remove from the actual array
     */
    void removeAtIndexRunner(int end, int... toRemove) {
        String methodName = "removeAtIndex";
        reverseSortIntArray(toRemove);
        createLinkedListExpectedMissing(end, toRemove);
        for (int index : toRemove) {
            TestObject data = getData(index);
            assertReturnValueEquals(methodName, data, actual
                .removeAtIndex(index));
        }
        assertStructureEquals();
        assertSizeEquals(methodName, end - toRemove.length);
    }

    /**
     * Runner for the removeFromFront tests to reduce code redundancy.
     *
     * @param end        the last number to include (exclusive, not inclusive)
     * @param iterations the number of times to call the method
     */
    @SuppressWarnings("SameParameterValue")
    void removeFromFrontRunner(int end, int iterations) {
        if (iterations < 0 || iterations > end) {
            throw new IllegalArgumentException("Iterations cannot be outside "
                + "the range [0, " + end + "]");
        }
        String methodName = "removeFromFront";
        int[] toRemove = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            toRemove[i] = i;
        }
        createLinkedListExpectedMissing(end, toRemove);
        for (int i = 0; i < iterations; i++) {
            TestObject data = getData(0);
            assertReturnValueEquals(methodName, data, actual.removeFromFront());
        }
        assertStructureEquals();
        assertSizeEquals(methodName, end - iterations);
    }

    /**
     * Runner for the removeFromBack tests to reduce code redundancy.
     *
     * @param end        the last number to include (exclusive, not inclusive)
     * @param iterations the number of times to call addToBack
     */
    @SuppressWarnings("SameParameterValue")
    void removeFromBackRunner(int end, int iterations) {
        if (iterations < 0 || iterations > end) {
            throw new IllegalArgumentException("Iterations cannot be outside "
                + "the range [0, " + end + "]");
        }
        String methodName = "removeFromBack";
        int[] toRemove = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            toRemove[i] = end - i - 1;
        }
        createLinkedListExpectedMissing(end, toRemove);
        for (int index : toRemove) {
            TestObject data = getData(index);
            assertReturnValueEquals(methodName, data, actual.removeFromBack());
        }
        assertStructureEquals();
        assertSizeEquals(methodName, end - iterations);
    }

    /**
     * Runner for the get tests to reduce code redundancy.
     *
     * @param end     the last number to include (exclusive, not inclusive)
     * @param toCheck the numbers to check in the actual array
     */
    void getRunner(int end, int... toCheck) {
        String methodName = "get";
        createLinkedList(end);
        for (int index : toCheck) {
            if (index < 0 || index >= end) {
                throw new IllegalArgumentException("Removed data cannot be "
                    + "outside the range [0, " + end + ")");
            }
            assertReturnValueEquals(null, expected[index], actual.get(index));
        }
        assertUnmodified(methodName, end);
    }

    /**
     * Runner for the isEmpty tests to reduce code redundancy.
     *
     * @param end the last number to include (exclusive, not inclusive)
     */
    void isEmptyRunner(int end) {
        String methodName = "isEmpty";
        createLinkedList(end);
        assertReturnValueEquals(null, end == 0, actual.isEmpty());
        assertUnmodified(methodName, end);
    }

    /**
     * Runner for the clear tests to reduce code redundancy.
     *
     * @param end      the last number to include (exclusive, not inclusive)
     * @param sizeOnly whether to check only if the size is reset(if true) or
     *                 if the structure is cleared (if false)
     */
    void clearRunner(int end, boolean sizeOnly) {
        createLinkedList(end);
        expected = new TestObject[0];
        actual.clear();
        if (!sizeOnly) {
            assertStructureEquals();
        } else {
            assertSizeEquals(null, 0);
        }
    }

    //*************************************************************************
    //***********************    SPRING 2020 METHODS    ***********************
    //*************************************************************************

    /**
     * Verifies that the actual return value is the same as the expected return
     * value.
     *
     * @param expectedReturn the expected return value
     * @param actualReturn   the actual return value
     * @param <T>            the type of object to compare
     */
    private static <T> void assertReturnReferenceEquals(T expectedReturn,
                                                        T actualReturn) {
        assertSame("Unexpected return reference", expectedReturn,
            actualReturn);
    }

    /**
     * Tests removeLastOccurrence with null data
     *
     * @param end the last number of the interval (exclusive, not inclusive)
     */
    @SuppressWarnings("SameParameterValue")
    void removeLastOccurrenceNullData(int end) {
        if (end <= 0) {
            throw new RuntimeException("end cannot be <= 0");
        }
        try {
            createLinkedList(end);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        try {
            actual.removeLastOccurrence(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Tests removeLastOccurrence with data not in list
     *
     * @param end  the last number of the interval (exclusive, not inclusive)
     * @param data the number to remove from the actual array
     */
    void removeLastOccurrenceElementNotFound(int end, int data) {
        if (data >= 0 && data < end) {
            throw new IllegalArgumentException("Data cannnot be in the range "
                + "[0, " + end + ")");
        }
        createLinkedList(end);
        try {
            actual.removeLastOccurrence(new TestObject(data));
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElementException.class, e.getClass());
            assertUnmodified(null, end);
            throw e;
        }
    }

    /**
     * Runner for the removeLastOccurrence tests to reduce code redundancy.
     *
     * @param numbers         the numbers to be stored in the expected and
     *                        actual variables
     * @param toRemoveIndices the indices in the actual array for what we
     *                        want to remove
     */
    void removeLastOccurrenceRunner(int[] numbers, int... toRemoveIndices) {
        if (numbers == null) {
            throw new IllegalArgumentException("Numbers cannot be null");
        }
        String methodName = "removeLastOccurrence";
        reverseSortIntArray(toRemoveIndices);
        Set<Integer> missingIndicesSet = new HashSet<>();
        for (int index : toRemoveIndices) {
            if (index < 0 || index >= numbers.length) {
                throw new IllegalArgumentException("Deleted numbers cannot be "
                    + "outside the range [0, " + numbers.length + ")");
            } else if (missingIndicesSet.contains(index)) {
                throw new IllegalArgumentException("Deleted indices cannot "
                    + "contain duplicate index");
            }
            missingIndicesSet.add(index);
        }
        createCustomLinkedListExpectedMissing(numbers, missingIndicesSet);
        TestObject[] data = new TestObject[toRemoveIndices.length];
        for (int i = 0; i < toRemoveIndices.length; i++) {
            data[i] = getData(toRemoveIndices[i]);
        }
        for (TestObject aData : data) {
            assertReturnValueEquals(methodName, aData, actual
                .removeLastOccurrence(aData));
        }
        assertStructureEquals();
        assertSizeEquals(methodName, numbers.length - toRemoveIndices.length);

        // Return reference test
        String checkName = methodName + "_returnReference";
        computeIfAbsent(checkName);
        createCustomLinkedListExpectedMissing(numbers, missingIndicesSet);
        data = new TestObject[toRemoveIndices.length];
        for (int i = 0; i < toRemoveIndices.length; i++) {
            data[i] = getData(toRemoveIndices[i]);
        }
        try {
            for (TestObject aData : data) {
                assertReturnReferenceEquals(aData,
                    actual.removeLastOccurrence(aData));
            }
            assertStructureEquals();
        } catch (AssertionError | NoSuchElementException
            | NullPointerException e) {
            persistentChecks.get(checkName)
                .add(persistentCheckLine(methodName));
        }

        // Equals test
        checkName = methodName + "_equals";
        computeIfAbsent(checkName);
        createCustomLinkedListExpectedMissing(numbers, missingIndicesSet);
        data = new TestObject[toRemoveIndices.length];
        for (int i = 0; i < toRemoveIndices.length; i++) {
            data[i] = getData(toRemoveIndices[i]);
        }
        try {
            for (TestObject aData : data) {
                assertReturnReferenceEquals(aData,
                    actual.removeLastOccurrence(new TestObject(aData.num)));
            }
            assertStructureEquals();
        } catch (AssertionError | NoSuchElementException
            | NullPointerException e) {
            persistentChecks.get(checkName)
                .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the removeLastOccurrence tests to reduce code redundancy.
     *
     * @param end the last number to include (exclusive, not inclusive)
     */
    void toArrayRunner(int end) {
        String methodName = "toArray";
        createLinkedList(end);
        assertArrayEquals(expected, actual.toArray());
        assertUnmodified(methodName, end);
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
            return Objects.hash(num);
        }

        @Override
        public String toString() {
            return "TestObject{"
                + "num=" + num
                + '}';
        }
    }
}
