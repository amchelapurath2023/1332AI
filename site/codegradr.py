from flask import Flask, render_template, request
import openai

app = Flask(__name__)

app.static_folder = 'static'
app.template_folder ='templates'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/uploads', methods=['POST']) 
def uploads():
    # Check if the POST request has the file part
    if 'test_files' not in request.files or 'solution_files' not in request.files:
        return 'No file part'

    test_files = request.files.getlist('test_files')
    solution_files = request.files.getlist('solution_files')

    # Check if user selected files
    if len(test_files) == 0 or len(solution_files) == 0:
        return 'No files selected'

    # Save the uploaded files
    for test_file in test_files:
        # Save or process test_file
        test_file.save("path/to/save/test_file")

    for solution_file in solution_files:
        # Save or process solution_file
        solution_file.save("path/to/save/solution_file")

    return 'success'

@app.route('/feedback')
def feedback():
    return render_template('feedback.html')

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')