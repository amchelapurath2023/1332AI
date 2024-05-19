from flask import Flask, render_template, request, redirect, url_for
from openai import OpenAI
import os
import subprocess
import glob

# Flask 
app = Flask(__name__)
app.static_folder = 'static'
app.template_folder ='templates'
# getting file names
UPLOAD_FOLDER = os.path.abspath(os.path.join(os.path.dirname(__file__), 'uploads'))
JUNIT_JAR = os.path.join(UPLOAD_FOLDER, 'junit-4.13.2.jar')
HAMCREST_JAR = os.path.join(UPLOAD_FOLDER, 'hamcrest-core-1.3.jar')
all_files = glob.glob(os.path.join(UPLOAD_FOLDER, '*.java'))
solution_filename = ""
test_filename = ""
for file in all_files:
    if "Test" not in file and "Node" not in file:
        solution_filename = file
    if "Test.java" in file:
        test_filename = file
    if solution_filename and test_filename:
        break

# UPLOAD YOUR OWN API_KEY
# 'sk-proj-56uZpDkjJXOi9rTGR3jvT3BlbkFJUFiAiNBe2iwWklWH7vqF'
os.environ['OPENAI_API_KEY'] = 'sk-proj-56uZpDkjJXOi9rTGR3jvT3BlbkFJUFiAiNBe2iwWklWH7vqF'
client = OpenAI()

@app.route('/uploads', methods=['POST']) 
def uploads():
    print('inside uploads')
    # Check if the POST request has the file part
    if 'test_files' not in request.files or 'solution_files' not in request.files:
        return 'No file part'

    test_files = request.files.getlist('test_files')
    solution_files = request.files.getlist('solution_files')

    # Check if user selected files
    if test_files[0].filename == '' and solution_files[0].filename == '':
        error = 'You did not upload any files.'
        return index(error=error)
    elif test_files[0].filename == '':
        error = 'You forgot to upload test files.'
        return index(error=error)
    elif solution_files[0].filename == '':
        error = 'You forgot to upload the student\'s solution.'
        return index(error=error)
    
    # Save the uploaded files
    for test_file in test_files:
       if test_file.filename:  # Only save if a filename is provided
            test_file.save(os.path.join(UPLOAD_FOLDER, test_file.filename))

    for solution_file in solution_files:
        if solution_file.filename:  # Only save if a filename is provided
            newName = solution_file.filename[:-5] + 'Solution.java'
            solution_file.save(os.path.join(UPLOAD_FOLDER, newName))

    return redirect(url_for('feedback'))

@app.route('/compile', methods=['POST'])
def compile():
    print('inside compile')

    # Define the file path
    student_filename = solution_filename[:-13] + '.java'
    filepath = os.path.join(UPLOAD_FOLDER, student_filename)
    
    # Write the code to a .java file
    with open(filepath, 'w') as file:
        print('writing to', student_filename)
        file.write(request.form['student_impl'])

    # compile student code 
    try:
        compile_process = subprocess.run(['javac', student_filename], cwd=UPLOAD_FOLDER, capture_output=True, text=True)
        if compile_process.returncode != 0:
            print("student code did failed to compile")
            response = 'student code did not compile\n' + compile_process.stderr
            return render_template('feedback.html', code=request.form['student_impl'], gpt_response=response)
        else:
            print("student code compiled")
            response = 'student code compiled, continue to generating feedback'
            return render_template('feedback.html', code=request.form['student_impl'], gpt_response=response)
    
    except Exception as e:
        return render_template('feedback.html', code=request.form['student_impl'], gpt_response=str(e))

@app.route('/run', methods=['POST'])
def run():
    # run junits, idk how to do this part 
    return 0

@app.route('/response', methods=['GET'])
def response():
    print('inside response')
    # output = compile_and_run_java_files(solution_filename, test_filename)

    solution = read_file_to_string(solution_filename)
    tests = read_file_to_string(test_filename)
    student_filename = solution_filename[:-13] + '.java'
    student = read_file_to_string(student_filename)

    prompt = generate_prompt(solution, tests, student)
    
    response = client.chat.completions.create(messages=[
            {
                "role": "user",
                "content": prompt,
            }, 
            {
                "role": "system", 
                "content": "Play the role of a coding instructor. Speak in second person (using 'you')."
            }
        ],
        model="gpt-3.5-turbo",
    )

    result = response.choices[0].message.content

    return render_template('feedback.html', code=student, gpt_response=result)

def compile_and_run_java_files(code, test):
    print("compile_and_run_java_files")
    try:
        # Compile all files
        compile_process = subprocess.run(['javac', '-cp', f'.:{JUNIT_JAR}:{HAMCREST_JAR}'] + all_files, cwd=UPLOAD_FOLDER,
                                        capture_output=True, text=True)
        
        if compile_process.returncode != 0:
            return f"Compilation failed:\n{compile_process.stderr}"
        else:
            print("all code compiles")
            return 'all code compiles'

    except Exception as e:
        return str(e)

def read_file_to_string(file_path):
    try:
        with open(file_path, 'r') as file:
            file_contents = file.read()
        return file_contents
    except Exception as e:
        return str(e)

def generate_prompt(solution, tests, student):
    return f""" 
    Here is the correct implementation:
    {solution}
    
    Here are the test cases we are grading them on. These are JUnit test cases. Assume the Util file is implemented. 
    The testing file is completely correct. Use the content of the test cases to understand and 'grade' the student's implementation:
    {tests}

    Here is the student's implemention:
    {student}
    
    Please explain in detail where the student went wrong and how they can correct their mistakes. Point
    to lines in their code where the errors occur in a succinct way. Comparing and contrasting with the 
    solution code would be very helpful. Using bullet points is helpful, too.
    """

@app.route('/', methods=['GET'])
def index(error=''):
    return render_template('index.html', error=error)

@app.route('/feedback', methods=['GET'])
def feedback():
    return render_template('feedback.html', code='paste student\'s implementation here', gpt_response='click \'generate feedback\' to get a response')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')