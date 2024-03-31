from flask import Flask, render_template

app = Flask(__name__)

app.static_folder = 'static'
app.template_folder ='templates'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/feedback')
def feedback():
    return render_template('feedback.html')

if __name__ == '__main__':
    app.run(host='0.0.0.0')