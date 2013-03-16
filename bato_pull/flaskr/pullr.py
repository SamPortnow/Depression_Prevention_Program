from __future__ import with_statement
from contextlib import closing
import sqlite3
from flask import Flask, request, session, g, redirect, url_for,\
    abort, render_template, flash
import json
import urllib2

DATABASE = '/tmp/flaskr.db'
DEBUG = True
SECRET_KEY = 'development key'
USERNAME = 'admin'
PASSWORD = 'default'

app = Flask(__name__)
app.config.from_object(__name__)

app.config.from_envvar('FLASKR_SETTINGS', silent=True)

render_json = lambda **args: json.dumps(args)


def connect_db():
    return sqlite3.connect(app.config['DATABASE'])

def init_db():
    with closing(connect_db()) as db:
        with app.open_resource('schema.sql') as f:
            db.cursor().executescript(f.read())
        db.commit()

@app.before_request
def before_request():
    g.db = connect_db()

@app.teardown_request
def teardown_request(exception):
    g.db.close()

@app.route('/', methods = ['GET', 'POST'])
def get_em():
        if request.headers['Content-Type'] == 'application/json':
            data = request.json
            for dict_json in data:
                values = dict_json.values()
                g.db.execute('insert into calendar (username, Year, Day, minutes,'
                             'Activity, Feeling, Thought) values (?, ?, ?, ?, ?, ?, ?)',
                    ["Samson", values[0], values[5], values[4], values[2], values[3], values[1]])
                g.db.commit()
            return redirect(url_for('success'))
        else:
            return render_template('layout.html')

@app.route('/game', methods = ['GET', 'POST'])
def get_em_game():
    if request.headers['Content-Type'] == 'application/json':
        data = request.json
        for dict_json in data:
            values = dict_json.values()
            g.db.execute('insert into games (username, Time, RT, Score, GameNumber, Game_Complete, '
                         'Trial, negative_thought, positive_thought, Success) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)',
                ["Samson", values[0], values[1], values[2], values[3], values[4], values[5], values[6],
            values[7], values[8]])
            g.db.commit()
        return redirect(url_for('success_game'))
    else:
        return render_template('layout.html')


@app.route('/success')
def success():
    cur = g.db.execute('select username, Year, Day, minutes, Activity, Feeling, Thought from calendar')
    entries = cur.fetchall()
    return render_template('success.html', entries= entries)

@app.route('/success_game')
def success_game():
    cur = g.db.execute('select username, Time, RT, Score, GameNumber, Game_Complete, '
                       'Trial int, negative_thought, positive_thought, Success from games')
    entries = cur.fetchall()
    return render_template('success.html', entries= entries)

if __name__ == '__main__':
    app.run()
