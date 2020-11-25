import os
import shutil

if os.path.isdir("maven"):
    os.chdir("maven")
    if os.path.isdir("screenshot"):
        shutil.rmtree('screenshot')
    else:
        os.mkdir("screenshot")

    os.system("python index.py")