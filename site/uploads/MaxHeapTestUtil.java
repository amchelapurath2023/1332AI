import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Grading util for MinHeap.
 *
 * DO NOT SHARE WITH STUDENTS.
 *
 * @author David Wang
 */
public class MaxHeapTestUtil {

    //*************************************************************************
    //***********************    INSTANCE VARIABLES    ************************
    //*************************************************************************

    private TestObject[] expected;
    private MaxHeap<TestObject> actual;
    private static Map<String, List<String>> persistentChecks;

    static {
        persistentChecks = new HashMap<>();
        persistentChecks.put("buildHeap_size", null);
        persistentChecks.put("buildHeap_compareTo", null);
        persistentChecks.put("buildHeap_capacity", null);
        persistentChecks.put("add_size", null);
        persistentChecks.put("add_compareTo", null);
        persistentChecks.put("remove_returnValue", null);
        persistentChecks.put("remove_nullSpot", null);
        persistentChecks.put("remove_size", null);
        persistentChecks.put("remove_compareTo", null);
        persistentChecks.put("getMax_unmodified", null);
        persistentChecks.put("isEmpty_unmodified", null);
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
     * @param methodName the name of the method being tests
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
     * Converts the expected and actual structures to a string
     *
     * @return the stringified version of the structures
     */
    private String getContent() {
        if (expected == null) {
            throw new IllegalStateException("Expected cannot be null");
        } else if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        Object[] backingArray = actual.getBackingArray();
        if (backingArray == null) {
            throw new IllegalArgumentException("Backing array cannot be null");
        }
        String result = "Expected: " + Arrays.toString(expected);
        result += "\n";
        result += "Actual: " + Arrays.toString(backingArray);
        return result;
    }

    /**
     * Checks to see if the array contains any null
     *
     * @param arr the array to check
     * @return true if the array contains a null element, false otherwise
     */
    private static boolean containsNull(Object[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        for (Object o : arr) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    // SETUP METHODS

    /**
     * Sets the fields of the MnHeap using reflection to avoid
     * dependencies in unit testing.
     *
     * @param backingArray the new backingArray
     * @param size         the new size
     */
    private void setMinHeap(TestObject[] backingArray, int size) {
        actual = new MaxHeap<>();
        try {
            Field field = MaxHeap.class.getDeclaredField("backingArray");
            field.setAccessible(true);
            field.set(actual, backingArray);

            field = MaxHeap.class.getDeclaredField("size");
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
     * Creates and sets the actual variables to a MinHeap of consecutive
     * integers.
     *
     * @param data      the data to be stored
     * @param capacity  the capacity of the expected and actual arrays
     * @param useSignum whether the compareTo should cap to -1, 0, 1 or not
     */
    @SuppressWarnings("SameParameterValue")
    private void createMinHeap(int[] data, int capacity, boolean useSignum) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        } else if (capacity < data.length + 1) {
            throw new IllegalArgumentException("Capacity cannot be less than "
                    + data.length + 1);
        }
        expected = new TestObject[capacity];
        TestObject[] arr = new TestObject[capacity];
        for (int i = 0; i < data.length; i++) {
            expected[i + 1] = new TestObject(data[i], useSignum);
            arr[i + 1] = expected[i + 1];
        }
        setMinHeap(arr, data.length);
    }

    /**
     * Creates and sets the expected variable to an array of consecutive
     * integers.
     *
     * Creates and sets the actual variables to an MinHeap of consecutive
     * integers.
     *
     * @param expectedData     the data to be stored in the expected array
     * @param actualData       the data to be stored in the actual array
     * @param expectedCapacity the capacity of the expected array
     * @param actualCapacity   the capacity of the actual array
     * @param useSignum        whether the compareTo should cap to -1, 0, 1
     *                         or not
     */
    private void createMinHeapDifferent(int[] expectedData, int[] actualData,
                                        int expectedCapacity,
                                        int actualCapacity,
                                        boolean useSignum) {
        if (expectedData == null) {
            throw new IllegalArgumentException("Expected data cannot be null");
        } else if (actualData == null) {
            throw new IllegalArgumentException("Actual data cannot be null");
        } else if (expectedCapacity < expectedData.length + 1) {
            throw new IllegalArgumentException("Expected capacity cannot be "
                    + "< " + (expectedData.length + 1));
        } else if (actualCapacity < actualData.length + 1) {
            throw new IllegalArgumentException("Actual capacity cannot be "
                    + "< " + (actualData.length + 1));
        }
        Map<Integer, TestObject> mapping = new HashMap<>();
        expected = new TestObject[expectedCapacity];
        TestObject[] arr = new TestObject[actualCapacity];
        if (actualData.length > expectedData.length) {
            for (int i = 0; i < actualData.length; i++) {
                arr[i + 1] = new TestObject(actualData[i], useSignum);
                mapping.put(actualData[i], arr[i + 1]);
            }
            if (mapping.size() != actualData.length) {
                throw new IllegalArgumentException("Cannot have duplicate "
                        + "data in actual data");
            }
            for (int i = 0; i < expectedData.length; i++) {
                if (!mapping.containsKey(expectedData[i])) {
                    throw new IllegalArgumentException("Expected data cannot "
                            + "contain elements not in actual data when expected "
                            + "data's length is less than actual data's");
                }
                expected[i + 1] = mapping.get(expectedData[i]);
            }
        } else {
            for (int i = 0; i < expectedData.length; i++) {
                expected[i + 1] = new TestObject(expectedData[i], useSignum);
                mapping.put(expectedData[i], expected[i + 1]);
            }
            if (mapping.size() != expectedData.length) {
                throw new IllegalArgumentException("Cannot have duplicate "
                        + "data in expected data");
            }
            for (int i = 0; i < actualData.length; i++) {
                if (!mapping.containsKey(actualData[i])) {
                    throw new IllegalArgumentException("Actual data cannot "
                            + "contain elements not in expected data when actual "
                            + "data's length is less than expected data's");
                }
                arr[i + 1] = mapping.get(actualData[i]);
            }
        }
        setMinHeap(arr, actualData.length);
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
     * Verifies that the capacity is as expected.
     *
     * If used with null as the method name, it does the standard assertion.
     * If used with an actual method name, it will perform the assertion and
     * mark the corresponding error in the persistent check map.
     *
     * @param methodName the name of the assignment method or null if just
     *                   the assertion is wanted
     * @param capacity   the expected capacity
     */
    private void assertCapacityEquals(String methodName, int capacity) {
        if (expected == null) {
            throw new IllegalStateException("Expected cannot be null");
        } else if (actual == null) {
            throw new IllegalStateException("Actual cannot be null");
        }
        Object[] backingArray = actual.getBackingArray();
        if (backingArray == null) {
            throw new IllegalStateException("Backing array cannot be null");
        }
        if (methodName == null) {
            assertEquals("Unexpected backing array length", capacity,
                    backingArray.length);
        } else {
            String checkName = methodName + "_capacity";
            computeIfAbsent(checkName);
            if (capacity != backingArray.length) {
                persistentChecks.get(checkName).add(persistentCheckLine(
                        methodName));
            }
        }
    }


    /**
     * Verifies that the specified index has a null rather than another object.
     *
     * If used with null as the method name, it does the standard assertion.
     * If used with an actual method name, it will perform the assertion and
     * mark the corresponding error in the persistent check map.
     *
     * @param methodName the name of the assignment method or null if just
     *                   the assertion is wanted
     * @param index      the index to check for null
     */
    private void assertNullSpot(String methodName, int index) {
        String content = "\n" + getContent() + "\n";
        String message = "\nExpected null at index: " + index + "\n";
        Object[] backingArray = actual.getBackingArray();
        if (index < 0 || index >= backingArray.length) {
            throw new IllegalArgumentException("Index cannot be outside the "
                    + "range [0, " + backingArray.length + ")");
        }
        if (methodName == null) {
            assertNull("Unexpected backing array content" + content + message,
                    backingArray[index]);
        } else {
            String checkName = methodName + "_nullSpot";
            computeIfAbsent(checkName);
            if (backingArray[index] != null) {
                persistentChecks.get(checkName)
                        .add(persistentCheckLine(methodName) + message);
            }
        }
    }

    /**
     * Verifies that the actual backing array is the same as the expected
     * backing array.
     *
     * If used with null as the method name, it does the standard assertion.
     * If used with an actual method name, it will perform the assertion and
     * mark the corresponding error in the persistent check map.
     *
     * @param methodName the name of the assignment method or null if just
     *                   the assertion is wanted
     */
    private void assertStructureEquals(String methodName) {
        String content = "\n" + getContent() + "\n";
        Object[] backingArray = actual.getBackingArray();
        List<TestObject> filtered = new ArrayList<>();
        for (TestObject obj : expected) {
            if (obj != null) {
                filtered.add(obj);
            }
        }
        if (backingArray.length > 0) {
            assertNullSpot(methodName, 0);
        }
        for (int i = filtered.size() + 1; i < backingArray.length; i++) {
            assertNullSpot(methodName, i);
        }
        if (backingArray.length > 0) {
            assertTrue("Backing array isn't large enough to hold the data.",
                    backingArray.length > filtered.size());
        }
        for (int i = 0; i < filtered.size(); i++) {
            assertEquals("Unexpected backing array content" + content,
                    filtered.get(i), backingArray[i + 1]);
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
     * Verifies that the backing array and size have not changed.
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
            assertStructureEquals(null);
        } else {
            String content = "\n" + getContent() + "\n";
            String checkName = methodName + "_unmodified";
            computeIfAbsent(checkName);
            if ((expectedSize != actual.size()) || !Arrays.equals(expected,
                    actual.getBackingArray())) {
                persistentChecks.get(checkName)
                        .add(persistentCheckLine(methodName) + content);
            }
        }
    }

    // EXCEPTION METHODS

    /**
     * Tests buildHeap with null data
     */
    void buildHeapNullData() {
        try {
            actual = new MaxHeap<>(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            throw e;
        }
    }

    /**
     * Tests constructor with null data included in list
     *
     * @param toAdd the numbers to add into the actual array
     */
    void buildHeapIncludeNullData(Integer... toAdd) {
        if (!containsNull(toAdd)) {
            throw new RuntimeException("toAdd should contain null");
        }
        try {
            ArrayList<TestObject> data = new ArrayList<>();
            for (Integer num : toAdd) {
                data.add(num == null ? null : new TestObject(num, true));
            }
            actual = new MaxHeap<>(data);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            throw e;
        }
    }

    /**
     * Tests add with null data
     *
     * @param data     the data to be stored
     * @param capacity the capacity of the expected and actual arrays
     */
    @SuppressWarnings("SameParameterValue")
    void addNullData(int[] data, int capacity) {
        createMinHeap(data, capacity, true);
        try {
            actual.add(null);
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertUnmodified(null, data.length);
            throw e;
        }
    }

    /**
     * Tests remove with empty heap
     *
     * @param capacity the capacity of the expected array
     */
    @SuppressWarnings("SameParameterValue")
    void removeEmpty(int capacity) {
        createMinHeap(new int[0], capacity, true);
        try {
            actual.remove();
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElementException.class, e.getClass());
            assertUnmodified(null, 0);
            throw e;
        }
    }

    /**
     * Tests getMax with empty heap
     *
     * @param capacity the capacity of the expected array
     */
    @SuppressWarnings("SameParameterValue")
    void getMaxEmpty(int capacity) {
        createMinHeap(new int[0], capacity, true);
        try {
            actual.getMax();
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElementException.class, e.getClass());
            assertUnmodified(null, 0);
            throw e;
        }
    }

    // RUNNER METHODS

    /**
     * Runner for the constructor tests to reduce code redundancy.
     */
    void constructorRunner() {
        actual = new MaxHeap<>();
        assertEquals(0, actual.size());
        assertArrayEquals(new Object[MaxHeap.INITIAL_CAPACITY],
                actual.getBackingArray());
    }

    /**
     * Runner for the buildHeap tests to reduce code redundancy.
     *
     * @param expectedData the data to be stored in the expected array
     * @param actualData   the data to be stored in the actual array
     */
    void buildHeapRunner(int[] expectedData, int[] actualData) {
        if (expectedData == null) {
            throw new IllegalArgumentException("Expected data cannot be null");
        } else if (actualData == null) {
            throw new IllegalArgumentException("Actual data cannot be null");
        }

        // Signum test
        String methodName = "buildHeap";
        createMinHeapDifferent(expectedData, new int[0],
                expectedData.length * 2 + 1, actualData.length * 2 + 1, true);
        ArrayList<TestObject> data = new ArrayList<>();
        for (int num : actualData) {
            data.add(new TestObject(num, true));
        }
        actual = new MaxHeap<>(data);
        assertCapacityEquals(methodName, actualData.length * 2 + 1);
        assertStructureEquals(null);
        assertSizeEquals(methodName, actualData.length);

        // Compare to test
        String checkName = methodName + "_compareTo";
        computeIfAbsent(checkName);
        createMinHeapDifferent(expectedData, new int[0],
                expectedData.length * 2 + 1, actualData.length * 2 + 1, false);
        data = new ArrayList<>();
        for (int num : actualData) {
            data.add(new TestObject(num, false));
        }
        try {
            actual = new MaxHeap<>(data);
            assertStructureEquals(null);
        } catch (AssertionError | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the add tests to reduce code redundancy.
     *
     * @param expectedData     the data to be stored in the expected array
     * @param actualData       the data to be stored in the actual array
     * @param expectedCapacity the capacity of the expected array
     * @param actualCapacity   the capacity of the actual array
     * @param toAdd            the numbers to add into the actual array
     */
    void addRunner(int[] expectedData, int[] actualData,
                   int expectedCapacity, int actualCapacity,
                   int... toAdd) {
        // Signum test
        String methodName = "add";
        createMinHeapDifferent(expectedData, actualData, expectedCapacity,
                actualCapacity, true);
        for (int data : toAdd) {
            actual.add(new TestObject(data, true));
        }
        assertCapacityEquals(null, expectedCapacity);
        assertStructureEquals(null);
        assertSizeEquals(methodName, expectedData.length);

        // Compare to test
        String checkName = methodName + "_compareTo";
        computeIfAbsent(checkName);
        createMinHeapDifferent(expectedData, actualData, expectedCapacity,
                actualCapacity, false);
        try {
            for (int data : toAdd) {
                actual.add(new TestObject(data, true));
            }
            assertStructureEquals(null);
        } catch (AssertionError | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the remove tests to reduce code redundancy.
     *
     * @param expectedData     the data to be stored in the expected array
     * @param actualData       the data to be stored in the actual array
     * @param expectedCapacity the capacity of the expected array
     * @param actualCapacity   the capacity of the actual array
     * @param toRemoveIndices  the indices of the numbers to remove from
     *                         the actual array
     */
    @SuppressWarnings("SameParameterValue")
    void removeRunner(int[] expectedData, int[] actualData,
                      int expectedCapacity, int actualCapacity,
                      int... toRemoveIndices) {
        // Signum test
        String methodName = "remove";
        createMinHeapDifferent(expectedData, actualData, expectedCapacity,
                actualCapacity, true);
        TestObject[] toRemoveData = new TestObject[toRemoveIndices.length];
        for (int i = 0; i < toRemoveIndices.length; i++) {
            toRemoveData[i] = actual.getBackingArray()[i + 1];
        }
        for (int i = 0; i < toRemoveIndices.length; i++) {
            assertReturnValueEquals(methodName, toRemoveData[i],
                    actual.remove());
        }
        assertCapacityEquals(null, expectedCapacity);
        assertStructureEquals(methodName);
        assertSizeEquals(methodName, expectedData.length);

        // Compare to test
        String checkName = methodName + "_compareTo";
        computeIfAbsent(checkName);
        createMinHeapDifferent(expectedData, actualData, expectedCapacity,
                actualCapacity, false);
        toRemoveData = new TestObject[toRemoveIndices.length];
        for (int i = 0; i < toRemoveIndices.length; i++) {
            toRemoveData[i] = actual.getBackingArray()[i + 1];
        }
        try {
            for (int i = 0; i < toRemoveIndices.length; i++) {
                actual.remove();
            }
            assertStructureEquals(methodName);
        } catch (AssertionError | NullPointerException e) {
            persistentChecks.get(checkName)
                    .add(persistentCheckLine(methodName));
        }
    }

    /**
     * Runner for the getMax tests to reduce code redundancy.
     *
     * @param data     the data to be stored
     * @param capacity the capacity of the expected and actual arrays
     */
    @SuppressWarnings("SameParameterValue")
    void getMaxRunner(int[] data, int capacity) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        } else if (data.length == 0) {
            throw new IllegalArgumentException("Length of data cannot be 0");
        }
        String methodName = "getMax";
        createMinHeap(data, capacity, true);
        TestObject min = actual.getBackingArray()[1];
        assertReturnValueEquals(null, min, actual.getMax());
        assertUnmodified(methodName, data.length);
    }

    /**
     * Runner for the isEmpty tests to reduce code redundancy.
     *
     * @param data     the data to be stored
     * @param capacity the capacity of the expected and actual arrays
     */
    @SuppressWarnings("SameParameterValue")
    void isEmptyRunner(int[] data, int capacity) {
        String methodName = "isEmpty";
        createMinHeap(data, capacity, true);
        assertEquals("Unexpected isEmpty return value", data.length
                == 0, actual.isEmpty());
        assertUnmodified(methodName, data.length);
    }

    /**
     * Runner for the clear tests to reduce code redundancy.
     *
     * @param data             the data to be stored
     * @param expectedCapacity the capacity of the expected array
     * @param actualCapacity   the capacity of the actual array
     * @param checkSize        whether to check only if the size is reset (if
     *                         true)
     *                         or if the structure is cleared (if false)
     */
    @SuppressWarnings("SameParameterValue")
    void clearRunner(int[] data, int expectedCapacity, int actualCapacity,
                     boolean checkSize) {
        createMinHeapDifferent(new int[0], data, expectedCapacity,
                actualCapacity, true);
        actual.clear();
        if (!checkSize) {
            assertCapacityEquals(null, expectedCapacity);
            assertStructureEquals(null);
        } else {
            assertSizeEquals(null, 0);
        }
    }


    //*************************************************************************
    //************************   AUXILIARY CLASSES    *************************
    //*************************************************************************

    private static class TestObject implements Comparable<TestObject> {

        private int num;
        private boolean useSignum;

        /**
         * Creates a new TestObject
         *
         * @param num       the number encapsulated by the object
         * @param useSignum whether the compareTo should cap to -1, 0, 1 or not
         */
        TestObject(int num, boolean useSignum) {
            this.num = num;
            this.useSignum = useSignum;
        }

        @Override
        public int compareTo(TestObject obj) {
            if (useSignum && obj.useSignum) {
                return Integer.signum(num - obj.num);
            } else {
                return num - obj.num;
            }
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