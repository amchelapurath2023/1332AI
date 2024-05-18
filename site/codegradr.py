from flask import Flask, render_template, request, redirect, url_for
import openai
import os
import subprocess

app = Flask(__name__)
app.static_folder = 'static'
app.template_folder ='templates'
UPLOAD_FOLDER = os.path.abspath(os.path.join(os.path.dirname(__file__), 'uploads'))

@app.route('/')
def index(error=''):
    return render_template('index.html', error=error)

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
            solution_file.save(os.path.join(UPLOAD_FOLDER, solution_file.filename))

    return redirect(url_for('feedback'))

def compile_and_run_java_files(student, test):
    print("compile_and_run_java_files")
    print(student)
    print(test)
    try:
        # Compile all Java files in the directory
        compile_process = subprocess.run(['javac', student], cwd=UPLOAD_FOLDER, capture_output=True, text=True)

        if compile_process.returncode != 0:
            print('fail here')
            return f"Compilation failed:\n{compile_process.stderr}"

        # Run the test files
        run_process = subprocess.run(['java', test], cwd=UPLOAD_FOLDER, capture_output=True, text=True)

        if run_process.returncode != 0:
            return f"Execution failed:\n{run_process.stderr}"

        return run_process.stdout

    except Exception as e:
        return str(e)

def read_file_to_string(file_path):
    try:
        with open(file_path, 'r') as file:
            file_contents = file.read()
        return file_contents
    except Exception as e:
        return str(e)
    

@app.route('/feedback')
def feedback():
    files = os.listdir(UPLOAD_FOLDER)
    student_filename = ""
    test_filename = ""
    for file in files:
        if "Test" not in file and "Node" not in file:
            student_filename = file
        if "Test.java" in file:
            test_filename = file
        if student_filename and test_filename:
            break

    output = compile_and_run_java_files(student_filename, test_filename)
    print(output)

    path = UPLOAD_FOLDER + "/" + student_filename
    student_implementation = read_file_to_string(path)
    return render_template('feedback.html', code=student_implementation)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')