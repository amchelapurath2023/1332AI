import subprocess

# import openai

def test_student_code(junit_test_file):
    # # Compile student's code
    # compile_command = f"javac {student_code_file}"
    # subprocess.run(compile_command, shell=True)

    # # Compile solution code
    # compile_command = f"javac {solution_code_file}"
    # subprocess.run(compile_command, shell=True)

    # Compile
    compile_command = "javac -cp ../lib/junit-4.13.2.jar *.java"
    # compile_command = f"javac -cp .:/Users/anshul/1332AI/lib/junit-4.13.2.jar {junit_test_file}"
    subprocess.run(compile_command, shell=True)

    # Run JUnit tests
    run_tests_command = f"java -cp ../lib/*:. org.junit.runner.JUnitCore {junit_test_file.split('.')[0]}"
    result = subprocess.run(run_tests_command, shell=True, capture_output=True, text=True)

    # def generate_comments(junit_output):
    # # Prompt for GPT-3.5
    #     prompt = f"Based on the JUnit output:\n\"{junit_output}\", provide feedback for improving the code:"

    #     # Call OpenAI's GPT-3.5 API
    #     response = openai.Completion.create(
    #         engine="text-davinci-003",  # or any other preferred model
    #         prompt=prompt,
    #         max_tokens=150,
    #         n=1,
    #         stop=None,
    #         temperature=0.5,
    #         api_key="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"
    #     )

    #     # Extract generated comments from API response
    #     comments = response.choices[0].text.strip()
    #     return comments

    print("Test Results:")
    print(result.stdout)

if __name__ == "__main__":
    # Paths to files
    # student_code_file = "/Users/anshul/1332AI/src/CircularSinglyLinkedList.java"
    # solution_code_file = "/Users/anshul/1332AI/src/CircularSinglyLinkedList_IMPL.java"
    junit_test_file = "CircularSinglyLinkedListTest.java"

    # Test the student's code
    test_student_code(junit_test_file)
    
