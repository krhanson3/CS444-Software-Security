import re
import os
import subprocess

from flask import Flask, request
app = Flask(__name__)


@app.route("/command1")
def command_injection1():
    files = request.args.get('files', '')
    # Don't let files be `; rm -rf /`
    # Split files on comma, only allow safe filenames (no metacharacters, no slashes)
 Error
Uncontrolled command line
This command line depends on a .
Code that passes user input directly to exec, eval, or some other library routine that executes a command, allows the user to execute malicious code.

    file_list = [f for f in files.split(',') if re.match(r'^[\w.-]+$', f)]
    if not file_list:
        return "No valid filenames specified", 400
    # Use subprocess.run, avoid shell, pass filenames as arguments
    subprocess.run(['ls'] + file_list)
    # Don't let files be `; rm -rf /`
    subprocess.Popen("ls " + files, shell=True) # $result=BAD


@app.route("/path-exists-not-sanitizer")
def path_exists_not_sanitizer():
    """os.path.exists is not a sanitizer

    This small example is inspired by real world code. Initially, it seems like a good
    sanitizer. However, if you are able to create files, you can make the
    `os.path.exists` check succeed, and still be able to run commands. An example is
    using the filename `not-there || echo pwned`.
    """
       base_path = '/server/static/listings'  # Change as appropriate for your app
    if os.path.exists(path):	    user_path = request.args.get('path', '')
