from bs4 import BeautifulSoup
import os


# Reading the data inside the xml
# file to a variable under the name
# data
file=["SolarBatteryCharger.xml", "SolarBatteryChargerDeviceTest.xml", "AS104.xml", "CN274.xml", "AllTestCase.xml"]
k=1
for i in file:
    print(k, ": ", i)
    k+=1
data=int(input("Enter file number to select Test Case :"))


new_file = open("C:/Users/Abhi/Tenxer/AutoTesting/com.tenxertech.autoTesting/testng.xml", "w")
with open("C:/Users/Abhi/Tenxer/AutoTesting/com.tenxertech.autoTesting/TestNG_Files/"+file[data-1]+"", "r") as f:
    new_file.write(f.read())

new_file.close()
temp= open("C:/Users/Abhi/Tenxer/AutoTesting/temp.py", "r")
for i in temp:
    print(i, end="")

os.chdir('C:/Users/Abhi/Tenxer/AutoTesting/com.tenxertech.autoTesting/')
os.system('mvn clean install')


