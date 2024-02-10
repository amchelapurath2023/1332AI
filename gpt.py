import subprocess

def test_student_code(student_code_file, solution_code_file, junit_test_file):
    # Compile student's code
    compile_command = f"javac CircularSinglyLinkedList.java"
    subprocess.run(compile_command, shell=True)

    # Compile solution code
    compile_command = f"javac CircularSinglyLinkedList_IMPL.java"
    subprocess.run(compile_command, shell=True)

    # Compile JUnit tests
    compile_command = f"javac -cp .:CircularSinglyLinkedListTest CircularSinglyLinkedListTest"
    subprocess.run(compile_command, shell=True)

    junit_test_file = "CircularSinglyLinkedListTest.java"

    # Run JUnit tests
    run_tests_command = f"java -cp .:{junit_test_file.split('.')[0]} org.junit.runner.JUnitCore {junit_test_file.split('.')[0]}"
    result = subprocess.run(run_tests_command, shell=True, capture_output=True, text=True)

    print("Test Results:")
    print(result.stdout)

if __name__ == "__main__":
    # Paths to files
    student_code_file = "student_arraylist.java"
    solution_code_file = "solution_arraylist.java"
    junit_test_file = "ArrayListTest.java"

    # Test the student's code
    test_student_code(student_code_file, solution_code_file, junit_test_file)
