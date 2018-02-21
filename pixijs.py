from flask import Flask, render_template, request

app = Flask(__name__)


@app.route('/')
def hello_world():
    return render_template('index.html')


@app.route('/canvas')
def canvas():
    ctx = {
        'lineStyle': request.args.get('lineStyle') or 0,
        'rect': request.args.get('rect') or 0,
        'static': request.args.get('static') or 0
    }
    return render_template('canvas.html', **ctx)


@app.route('/pixi')
def pixi():
    ctx = {
        'lineStyle': request.args.get('lineStyle') or 0,
        'rect': request.args.get('rect') or 0,
        'static': request.args.get('static') or 0,
        'forceCanvas': request.args.get('forceCanvas') or 0
    }
    return render_template('pixi.html', **ctx)


@app.route('/svg')
def svg():
    ctx = {
        'lineStyle': request.args.get('lineStyle') or 0,
        'rect': request.args.get('rect') or 0,
        'static': request.args.get('static') or 0
    }
    return render_template('svgdyn.html', **ctx)

if __name__ == '__main__':
    app.run(debug=True)
