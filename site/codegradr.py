from flask import Flask, render_template, request, redirect, url_for
import openai
import os

app = Flask(__name__)

app.static_folder = 'static'
app.template_folder ='templates'
UPLOAD_FOLDER = os.path.abspath(os.path.join(os.path.dirname(__file__), 'uploads'))

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/uploads', methods=['POST']) 
def uploads():
    print('inside uploads')
    # Check if the POST request has the file part
    if 'test_files' not in request.files or 'solution_files' not in request.files:
        return 'No file part'

    test_files = request.files.getlist('test_files')
    solution_files = request.files.getlist('solution_files')

    # Check if user selected files
    if len(test_files) == 0 or len(solution_files) == 0:
        print("no files")
        return 'No files selected'

    # Save the uploaded files
    for test_file in test_files:
       if test_file.filename:  # Only save if a filename is provided
            print(test_file.filename)
            test_file.save(os.path.join(UPLOAD_FOLDER, test_file.filename))
            # test_file.save(os.path.join(UPLOAD_FOLDER, test_file.filename))

    for solution_file in solution_files:
        if solution_file.filename:  # Only save if a filename is provided
            print(solution_file.filename)
            test_file.save(os.path.join(UPLOAD_FOLDER, solution_file.filename))

    return redirect(url_for('feedback'))


@app.route('/feedback')
def feedback():
    return render_template('feedback.html')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')