import re
import ast
import operator as op
from flask import Flask, request, jsonify
import logging

app = Flask(__name__)

# Allow digits, basic math operators, parentheses, spaces, dot and scientific e/E, and caret (^)
ALLOWED_PATTERN = re.compile(r'^[0-9+\-*/%\^\(\)\s\.eE]+$')

# Operators we permit
_ALLOWED_OPERATORS = {
    ast.Add: op.add,
    ast.Sub: op.sub,
    ast.Mult: op.mul,
    ast.Div: op.truediv,
    ast.FloorDiv: op.floordiv,
    ast.Mod: op.mod,
    ast.Pow: op.pow,
    ast.USub: op.neg,
    ast.UAdd: op.pos,
    ast.BitXor: op.xor,    # keep if you want '^' to behave as XOR (see note below)
}

def sanitize_input(user_input):
    # Basic pattern check first
    if not isinstance(user_input, str) or not ALLOWED_PATTERN.match(user_input):
        return None

    # Block dangerous keywords (extra protection; unlikely to pass the regex anyway)
    forbidden_keywords = ['exec', 'eval', 'import', 'open', 'os', 'subprocess', '__']
    if any(keyword in user_input for keyword in forbidden_keywords):
        return None

    # Normalize caret '^' to Python power operator '**' if you intended caret for power.
    # NOTE: In Python, '^' is bitwise XOR. If you want ^ to be XOR keep this line commented out.
    normalized = user_input.replace('^', '**')

    return normalized

def safe_eval_expr(expr: str):
    """
    Very small evaluator that accepts numeric literals, unary ops and binary arithmetic.
    Raises ValueError for disallowed constructs.
    """
    try:
        parsed = ast.parse(expr, mode='eval')
    except SyntaxError as e:
        raise ValueError("Syntax error in expression") from e

    # Disallow any nodes that aren't explicitly allowed
    for node in ast.walk(parsed):
        # disallowed node types
        if isinstance(node, (ast.Import, ast.ImportFrom, ast.Global, ast.Nonlocal,
                             ast.Lambda, ast.FunctionDef, ast.ClassDef, ast.AsyncFunctionDef,
                             ast.With, ast.Try, ast.Yield, ast.YieldFrom, ast.Await,
                             ast.Call, ast.Attribute, ast.Subscript, ast.List, ast.Dict, ast.Set,
                             ast.ListComp, ast.SetComp, ast.DictComp, ast.GeneratorExp)):
            raise ValueError(f"Disallowed expression component: {type(node).__name__}")

        # disallow names (variables) — only numeric constants allowed
        if isinstance(node, ast.Name):
            raise ValueError(f"Use of names is not allowed: {node.id}")

    def _eval(node):
        if isinstance(node, ast.Expression):
            return _eval(node.body)

        if isinstance(node, ast.Constant):  # Python 3.8+
            if isinstance(node.value, (int, float)):
                return node.value
            raise ValueError("Only numeric constants are allowed")

        # for older Python: ast.Num
        if isinstance(node, ast.Num):
            return node.n

        if isinstance(node, ast.BinOp):
            left = _eval(node.left)
            right = _eval(node.right)
            op_type = type(node.op)
            if op_type in _ALLOWED_OPERATORS:
                return _ALLOWED_OPERATORS[op_type](left, right)
            raise ValueError(f"Operator {op_type.__name__} not allowed")

        if isinstance(node, ast.UnaryOp):
            operand = _eval(node.operand)
            op_type = type(node.op)
            if op_type in _ALLOWED_OPERATORS:
                return _ALLOWED_OPERATORS[op_type](operand)
            raise ValueError(f"Unary operator {op_type.__name__} not allowed")

        raise ValueError(f"Unsupported expression part: {type(node).__name__}")

    return _eval(parsed)

@app.route('/evaluate', methods=["POST"])
def evaluate_code():
    user_input = request.get_json().get('code', '')

    sanitized_input = sanitize_input(user_input)
    if sanitized_input is None:
        return jsonify({"error": "Invalid input, contains forbidden characters or keywords."}), 400

    try:
        # Evaluate the expression using the safe AST evaluator (no eval/exec)
        result = safe_eval_expr(sanitized_input)
        return jsonify({"result": result})
    except ValueError as e:
        logging.exception("Error evaluating user input")
        return jsonify({"error": str(e)}), 400
    except ZeroDivisionError:
        logging.exception("Division by zero evaluating user input")
        return jsonify({"error": "Division by zero"}), 400
    except Exception:
        logging.exception("Error evaluating user input")
        return jsonify({"error": "Internal server error"}), 500

if __name__ == '__main__':
    app.run()
