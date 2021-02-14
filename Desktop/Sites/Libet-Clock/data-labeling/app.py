# from flask import Flask, render_template, redirect, url_for, request, jsonify
# from flask import make_response
# app = Flask(__name__)


# @app.route('/')
# def home_page():
#     example_embed = 'This string is from python'
#     return render_template('index.html', embed=example_embed)


# @app.route('/test', methods=['GET', 'POST'])
# def test():
#     print('testing')
#     if request.method == 'POST':
#         message = {'greeting': 'Hello from Flask!'}
#         return jsonify(message)

#         # GET request
#     if request.method == 'GET':
#         message = {'greeting': 'Hello from Flask!'}
#         return jsonify(message)  # serialize and use JSON headers


# if __name__ == "__main__":
#     app.run(debug=True)
