import stat
import subprocess
from subprocess import call
import os
import shutil


def version_check():
    jproc = list(subprocess.getoutput("java -version").split(" "))
    mproc = list(subprocess.getoutput("mvn --version").split(" "))
    gproc = list(subprocess.getoutput("git --version").split(" "))

    jver = (jproc[2])[1:3]
    mver=mproc[2].split(".")
    gver = gproc[2].split(".")
    if not int(jver) >= 11:
        return 0
    if not int(mver[0])>=3:
        return 0
    if not int(gver[0])>=1:
        return 0
    return 1


if version_check():
    if os.path.isdir("maven"):
        for root, dirs, files in os.walk("maven"):
            for d in dirs:
                os.chmod(os.path.join(root, d), stat.S_IRWXU)
            for file in files:
                os.chmod(os.path.join(root, file), stat.S_IRWXU)
        shutil.rmtree('maven')
    os.system('git clone --single-branch --branch maven "https://github.com/12Abhi89/maven.git"')
    os.chdir("maven")
    os.mkdir("screenshot")

    file = os.listdir("com.tenxertech.autoTesting/TestNG_Files/")
    k = 1
    for i in file:
        print(k, ": ", i)
        k += 1
    data = int(input("Enter file number to run Test Case :"))

    new_file = open("com.tenxertech.autoTesting/testng.xml", "w")
    with open("com.tenxertech.autoTesting/TestNG_Files/" + file[data - 1] + "", "r") as f:
        new_file.write(f.read())

    new_file.close()

    run = int(input("\nDo you want to run [yes=1 / no=0] :"))
    if run == 1:
        os.system("python index.py")
    else:
        exit()

