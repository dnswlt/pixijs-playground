from flask import Flask, render_template

app = Flask(__name__)


@app.route('/')
def hello_world():
    return render_template('index.html')


@app.route('/canvas')
def canvas():
    return render_template('canvas.html')


@app.route('/pixi')
def pixi():
    return render_template('pixi.html')

if __name__ == '__main__':
    app.run(debug=True)
