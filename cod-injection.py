import re
from flask import Flask, request, jsonify

app = Flask(__name__)

# Define a regex pattern to allow only numbers, basic math operators (+-*/%^), and parentheses.
ALLOWED_PATTERN = re.compile(r'^[\d\+\-\*/\%\^\(\)\s]+$')

def sanitize_input(user_input):
    # Strip out unwanted characters.
    if not ALLOWED_PATTERN.match(user_input):
        return None  # Return None if the input contains disallowed characters.

    # Optionally, you can also remove certain dangerous words (keywords like exec, eval, import).
    forbidden_keywords = ['exec', 'eval', 'import', 'open', 'os', 'subprocess', '__']
    if any(keyword in user_input for keyword in forbidden_keywords):
        return None
    
    return user_input

@app.route('/evaluate', methods=["POST"])
def evaluate_code():
    user_input = request.get_json().get('code', '')

    sanitized_input = sanitize_input(user_input)
    if sanitized_input is None:
        return jsonify({"error": "Invalid input, contains forbidden characters or keywords."}), 400
    
    try:
        # Safe evaluation of mathematical expressions
        result = eval(sanitized_input)  # eval here is safe because input is sanitized
        return jsonify({"result": result})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
