from bs4 import BeautifulSoup
import os
java = "java --version"
maven = "mvn --version"

print("\n============================================")
if os.system(java):
    print("java is not installed")
print("\n============================================")

if os.system(maven):
    print("maven is not installed")
print("\n============================================")

if os.system("git --version"):
    print("maven is not installed")
print("\n============================================")
os.system("mkdir temp")
os.system("dir")
isFile = os.path.isfile("temp")
print(isFile)

file = ["SolarBatteryCharger.xml", "SolarBatteryChargerDeviceTest.xml", "AS104.xml", "CN274.xml", "AllTestCase.xml"]
k = 1
for i in file:
    print(k, ": ", i)
    k += 1
data=int(input("Enter file number to select Test Case :"))


new_file = open("C:/Users/Abhi/Tenxer/AutoTesting/com.tenxertech.autoTesting/testng.xml", "w")
with open("C:/Users/Abhi/Tenxer/AutoTesting/com.tenxertech.autoTesting/TestNG_Files/"+file[data-1]+"", "r") as f:
    new_file.write(f.read())

new_file.close()


run = int(input("\nDo you want to run [yes=1 / no=0] :"))
if run == 1:
    os.chdir('C:/Users/Abhi/Tenxer/AutoTesting/com.tenxertech.autoTesting/')
    os.system('mvn clean install')
else:
    exit()

