from flask import Flask, render_template, request
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

# UPLOAD YOUR OWN API_KEY
os.environ['OPENAI_API_KEY'] = ''
client = OpenAI()


@app.route('/', methods=['GET'])
def index(error=''):
    return render_template('index.html', error=error)

@app.route('/feedback', methods=['GET'])
def feedback():
    return render_template('feedback.html', code='paste student\'s implementation here', gpt_response='click \'generate feedback\' to get a response')

@app.route('/uploads', methods=['POST']) 
def uploads():
    print('inside uploads')

    # Clear all previous java files in folder
    delete_all_files()

    # Get inputted files
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
       name = test_file.filename
       if name:
            test_file.save(os.path.join(UPLOAD_FOLDER, name))

    for solution_file in solution_files:
        name = solution_file.filename
        if name:
            if 'Node' not in name and 'Entry' not in name:
                name = name[:-5] + 'Solution.txt'
            solution_file.save(os.path.join(UPLOAD_FOLDER, name))

    return feedback()

@app.route('/compile', methods=['POST'])
def compile():
    print('inside compile')

    # Define the file path
    student_filename = get_file('Solution.txt')[:-12] + '.java'
    create_and_write_to_file(student_filename, request.form['student_impl'])

    # compile student code 
    try:
        compile_process = subprocess.run(['javac', student_filename], cwd=UPLOAD_FOLDER, capture_output=True, text=True)
        if compile_process.returncode != 0:
            print("student code failed to compile")
            response = 'student code did not compile\n' + compile_process.stderr
            return render_template('feedback.html', code=request.form['student_impl'], gpt_response=response)
        else:
            print("student code compiled")
            response = 'student code compiled'
            return render_template('feedback.html', code=request.form['student_impl'], gpt_response=response)
    
    except Exception as e:
        return render_template('feedback.html', code=request.form['student_impl'], gpt_response=str(e))

@app.route('/run', methods=['GET'])
def run():
    # Compile all files in folder
    compilaton_output = compile_all_files()

    # Run the compiled Java program
    test_filename = get_file('Test.java')
    print('Test name:', test_filename)
    try:
        run_process = subprocess.run(['java', '-cp', f'.:{JUNIT_JAR}:{HAMCREST_JAR}', 'Main', test_filename[:-4]], 
                                     cwd=UPLOAD_FOLDER, capture_output=True, text=True)
        if run_process.returncode == 0:
            response = 'tests successfully ran, continue to generating feedback'
            print(response)
            return response
        else:
            response = 'issue with running tests...'
            print(run_process.stderr)
            return response + '\n' + run_process.stderr
    except Exception as e:
        print(str(e))
        return str(e) 

@app.route('/response', methods=['GET'])
def response():
    print('inside response')

    # get file names
    solution_filename = get_file('Solution.txt')
    test_filename = get_file('Test.java')
    student_filename = solution_filename[:-12] + '.java'


    # get file contents
    solution = read_file_to_string(solution_filename)
    tests = read_file_to_string(test_filename)
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


    # result = "pretend this is a long ass chat response to avoid using my gpt cash"
    # print(result)

    return result

def get_all_files():
    all_files = glob.glob(os.path.join(UPLOAD_FOLDER, '*.java'))
    all_files += glob.glob(os.path.join(UPLOAD_FOLDER, '*.txt'))
    all_files += glob.glob(os.path.join(UPLOAD_FOLDER, '*.class'))
    return all_files

def delete_all_files():
    print('delete_all_files')
    all_files = get_all_files()
    for file in all_files: 
        if os.path.exists(file) and 'Main' not in file:
            os.remove(file)
    
def get_file(keyword):
    print('get_file:', keyword)
    all_files = get_all_files()
    for file in all_files:
        i = 0
        if keyword in file:
            return file
        
    return 'file not found'

def create_and_write_to_file(filename, content):
    # Define the file path
    filepath = os.path.join(UPLOAD_FOLDER, filename)
    
    # Write the code to a .java file
    with open(filepath, 'w') as file:
        print('writing to', filename)
        file.write(content)

def compile_all_files():
    print("compile_all_files")
    try:
        # Compile all files
        all_files = glob.glob(os.path.join(UPLOAD_FOLDER, '*.java'))
        compile_process = subprocess.run(['javac', '-cp', f'.:{JUNIT_JAR}:{HAMCREST_JAR}'] + all_files, 
                                         cwd=UPLOAD_FOLDER, capture_output=True, text=True)
        
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
    str = f""" 
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
    return str[:16385]

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')