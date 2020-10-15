package solar_Battery_Charger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;

public class SolarBatteryChargerTest {
	

	protected WebDriver driver;
	protected JavascriptExecutor jsDriver;
	protected NgWebDriver ngDriver;
	protected String pageurl = "https://renesas.evmlabs.com/#!/";
	protected WebDriverWait wait;
	public static String LoginPage="Tenxer - Login";
	public static String EvmSelectingPage="Tenxer -";
	public static String SBCEvmLanPage="Tenxer - ISL81601-US011REFZ- Solar Battery Charger";
	
	//Setup the driver settings
	@BeforeMethod
	public void setup() 
	{
		//System.setProperty("webdriver.gecko.driver","C:\\Users\\Abhi\\Tenxer\\AutoTesting\\Selenium_Jar_Files\\geckodriver.exe");
		//driver=new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Abhi\\Tenxer\\AutoTesting\\Selenium_Jar_Files\\chromedriver.exe");
		driver = new ChromeDriver();
		jsDriver=(JavascriptExecutor) driver;
		ngDriver=new NgWebDriver(jsDriver);
		wait = new WebDriverWait(driver,60);
		
		
		driver.get(pageurl);
		driver.manage().window().maximize();
		ngDriver.waitForAngularRequestsToFinish();
		//Login Page
		driver.findElement(ByAngular.model("username")).sendKeys("abhishek@tenxertech.com");
		driver.findElement(ByAngular.model("password")).sendKeys("4KSVHCgxc6p7dV2");
		driver.findElement(ByAngular.buttonText("Login")).click();
		ngDriver.waitForAngularRequestsToFinish();
		Assert.assertEquals(driver.getTitle(), EvmSelectingPage,"Home page is not loaded");
	}
	
	//Close the driver
	@AfterMethod
	public void destroy()
	{
		driver.close();
	}
	
	//System status data will be displayed
	public HashMap<String, Float> SystemStatus()
	{
		//waits till system status is visible in page
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ByAngular.repeater("value in outputTrans track by $index")));
				
				//prints System status data
				//String obj;
				HashMap<String,Float> SysMap=new HashMap<String,Float>();
				String[] obj,obj2;
				List<WebElement> SystemStatus=driver.findElements(ByAngular.repeater("value in outputTrans track by $index"));
				//Object[][] values= new Object[SystemStatus.size()][2];
				float temp;
				for(WebElement x : SystemStatus)
				{
					
					//values[i]=x.getText().split("\n");
					obj=x.getText().split("\n");
					
					obj2=obj[1].split(" ");
					if(obj2[0].contains("%"))
					{
						//obj2[0].substring(0, (obj2[0].length())-2);
						//System.out.println("sub "+obj2[0].substring(0, (obj2[0].length())-1));
						temp = Float.parseFloat(obj2[0].substring(0, (obj2[0].length())-1));
						
						
					}
					else if(obj2[0].contains("-"))
					{
						temp=-1;
					}
					else
					{
						temp = Float.parseFloat(obj2[0]);
					}
					SysMap.put(obj[0], temp);
				}
				return SysMap;

	}
	
	//returns console data
	public String[] Console()
	{
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByAngular.repeater("op in outputTrans track by $index")));
		List<WebElement> Console=driver.findElements(ByAngular.repeater("op in outputTrans track by $index"));
		String[] con=new String[Console.size()];
		int i=0;
		for(WebElement c:Console)
		{
			con[i]=c.getText().toString();
			i++;
		}
		return con;
	}
	
	//Returns 1 for MPPT On ,0 for MPPT OFF and -1 For Empty
	public int MPPTStatus()
	{
		String MPPT=driver.findElement(By.id("tab_12_21_41")).findElement(By.xpath(".//div[@ng-if=\"value.label\" and @class=\"led-label ng-binding ng-scope\" and @ng-bind-html=\"value.label | newlines\"]")).getText();
		if(MPPT.equals("MPPT ON"))
		{
			return 1;
		}
		else if(MPPT.equals("MPPT OFF"))
		{
			return 0;
		}
		else {
			return -1;
		}
	}
	
	//returns 1 if battery charging is blinking and 0 if its not blinking
	public int BatteryStatus()
	{
		String ExpectedBlink="led blinking";
		String ActualBlink="";
		List<WebElement> batterystats=driver.findElements(By.xpath(".//div[@ng-class=\"pos\" and @class=\"led-container d-flex ng-scope right\"]"));
		String temp="";
		for(WebElement w:batterystats)
		{
			temp=w.getText();
			if(temp.equals("Battery Charging"))
			{
				//".//div[@ng-class=\"pos\"]/div[@ng-class=\"value.position\"]/div[2]"
			ActualBlink=w.findElement(By.xpath(".//div[@ng-class=\"value.position\"]/div[2]")).getAttribute("class");
			}
		}
		
		if(ExpectedBlink.equals(ActualBlink))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	//returns 1 if Discharge status is ON ,0 if Discharge status is OFF and -1 for Discharge status is Empty
	public int DischargeStatus()
	{
		//String ActualState="";
		List<WebElement> batterystats=driver.findElements(By.xpath(".//div[@ng-class=\"pos\" and @class=\"led-container d-flex ng-scope right\"]"));
		String temp="";
		int i=-1;
		for(WebElement w:batterystats)
		{
			temp=w.getText();
			if(temp.equals("ON"))
			{
				i=1;
				break;
				//".//div[@ng-class=\"pos\"]/div[@ng-class=\"value.position\"]/div[2]"
				//ActualState=w.findElement(By.xpath(".//div[@ng-class=\"value.position\"]/div[2]")).getAttribute("class");
			}
			else if(temp.equals("OFF"))
			{
				i=0;
				break;
			}
		}
		return i;
	}
	
	

 public void graph() throws Exception {
	        String file = "https://media.evmlabs.com/janusbase/janus.js";//https://media.evmlabs.com/janusbase/janus
	        String json = readFileAsString(file);
	        System.out.println(json);
	    }
 public static String readFileAsString(String file)throws Exception{
	        return new String(Files.readAllBytes(Paths.get(file)));
	    }

	
	//Testing Solar Battery Charger
	@Test(dataProvider="ConfigData")
	public void configTest(String UserInputVoltage,String UserInputCurrent,String UserInputIrradiance ,String UserInputTemp) throws Exception {
		
		
		//EVM Selecting page
		try {
		wait.until(ExpectedConditions.elementToBeClickable(ByAngular.repeater("form in Forms")));
		}catch(Exception e)
		{
			throw new RuntimeException("waited 60sec in home page for rvm list to load",e);
		}
		driver.findElement(ByAngular.repeater("form in Forms")).findElement(By.className("material-icons")).click();
		ngDriver.waitForAngularRequestsToFinish();
		
		//ISL81601-US011REFZ- Solar Battery Charger Page
		Assert.assertEquals(driver.getTitle(), SBCEvmLanPage,"ISL81601-US011REFZ- Solar Battery Charger Page is not loaded");
		//Switch to Eva chat bot
		try {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
		}catch(Exception e)
		{
			throw new RuntimeException("waited 60sec to switch to boat frame",e);
		}
		String EvaPath="//html/body/div/div/div/nav/div/button[@class='min-max-toggle btn btn--icon' and @aria-label='minimize chat window toggle']";
		try {
		//waits to load Eva Chat bot
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(EvaPath)));
		}catch(Exception e)
		{
			throw new RuntimeException("waited 60sec to load content of Eva chat bot",e);
		}
		
		//minimizes Eva Chat bot
		Thread.sleep(3000);
		driver.findElement(By.xpath(EvaPath)).click();
		
		//switch to parrent frame
		driver.switchTo().parentFrame();
		
		try {
		//Waits Till User guide close button is available
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='wmClose notop']")));
		}catch(Exception e)
		{
			throw new RuntimeException("waited 60sec to load user guide popup",e);
		}
		
		//Minimizes user guide pop up
		driver.findElement(By.xpath("//button[@class='wmClose notop']")).click();
		
		//input configure value
		List<WebElement> dropdown=driver.findElements(ByAngular.model("tnxmodel"));
		
		//Selects input from Voc dropdown
		dropdown.get(0).click();
		Select Voc=new Select(dropdown.get(0));
		Voc.selectByVisibleText(UserInputVoltage);
		
		//Selects input from Isc dropdown
		dropdown.get(1).click();
		Select Isc=new Select(dropdown.get(1));
		Isc.selectByVisibleText(UserInputCurrent);
		
		//Selects input from Irradiance dropdown
		dropdown.get(2).click();
		Select Irradiance=new Select(dropdown.get(2));
		Irradiance.selectByVisibleText(UserInputIrradiance);
		
		
		//Selects input from temperature dropdown
		dropdown.get(3).click();
		Select Temp=new Select(dropdown.get(3));
		Temp.selectByVisibleText(UserInputTemp);
		
		//press the configure button--------------------------------------------------------------
		driver.findElement(By.xpath(".//button[@ng-class=\"getComToArr(data.class)\" and @ng-click=\"submitAll($event,data.allattrib)\" and @class=\"btn btn-primary btn-element  fat-btn\"]")).click();
		
		try {
		//condition 1:waits till top right status bar shows Ready
		wait.until(ExpectedConditions.textToBe(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]"), ". Ready"));
		}catch(Exception e)
		{
			throw new RuntimeException("After configure waited 60sec for top bar to show Ready",e);
		}
		
		try {
		//condition2: waits till system status is visible in page
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ByAngular.repeater("value in outputTrans track by $index")));
		}catch(Exception e)
		{
			throw new RuntimeException("After configure waited 60sec for system status to show values",e);
		}
		//print Rounded values of System status
		HashMap<String,Float>  SystemData;
		SystemData=SystemStatus();
		//key=Vin,Iin,Power_in,Vout,Iout,Power_out,Efficiency,[if discharge is on = Battery V,Battery I]
		String[] key= {"Vin","Iin","Power_in","Vout","Iout","Power_out","Efficiency"};
		for(int i=0;i<key.length;i++)
		{
			System.out.println("====================\n"+key[i]+"="+SystemData.get(key[i]));
		}
		
		
		try {
		//Console data is displayed
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByAngular.repeater("op in outputTrans track by $index")));
		}catch(Exception e)
		{
			throw new RuntimeException("After configure waited 60sec for console data to be displayed",e);	
		}
		String[] console;
		console=Console();
		for(String i:console)
		{
			System.out.println("----------------------------------------\n"+i);
		}
		//condition 3: Console should show "Solar Panel Configuration Complete"
		Assert.assertEquals(console[console.length-3], "Solar Panel Configuration Complete","During Configuring Inputs Console status");
		
		//condition 4:checking Battery status blinking or not
		if(!(BatteryStatus()==1))
		{
			Assert.assertTrue(false, "After configuration Battery Charging status is not blinking");
		}
		
		//condition 5:MPPT Status is OFF
		if(!(MPPTStatus()==0))
		{
			Assert.assertTrue(false,"After Configuration MPPT Status Should be off");
		}
		//End of configure button----------------------------------------------------------------------------------------------------------
		try {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]")));
		}catch(Exception e)
		{
			throw new RuntimeException("waited for 60sec for MPPT On button to be available",e);
		}
		//graph();
		//This list contains MPPT button and Battery Discharge button
		List<WebElement> MPPTandBattery=driver.findElements(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]"));
		
		//MPPT On--------------------------------------------------------------------------
		
		//MPPT Button will be pressed
		MPPTandBattery.get(0).click();
		ngDriver.waitForAngularRequestsToFinish();
		//Condition 1
		try {
		//waits till top right status bar shows Ready
		wait.until(ExpectedConditions.textToBe(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]"), ". Ready"));
		}catch(Exception e)
		{
			throw new RuntimeException("after MPPT On button is pressed waited 60sec for top bar to show Ready");		}
		
		Thread.sleep(5000);//charge the battery
		
		//Condition 2
		//MPPT Button Status
		if(!(MPPTStatus()==1))
		{
			Assert.assertTrue(false,"After MPPT On button pressed MPPT Status Should be ON");
		}
		
		
		//Condition 3
		//MPPT Status in console
		String ExpectedConsoleMPPTStatus="MPPT is ON";
		try {
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByAngular.repeater("op in outputTrans track by $index")));
		}catch(Exception e)
		{
			throw new RuntimeException("After MPPT On button is pressed waited 60sec for console to refresh the content ");
		}
		console=Console();
		Assert.assertEquals(console[console.length-1],ExpectedConsoleMPPTStatus,"After MPPT button is ON Console status ");//Checking is Console Showing MPPT is On
		
		//condition 4:checking Battery status blinking or not
		if(!(BatteryStatus()==1))
		{
			Assert.assertTrue(false,"After MPPT On button pressed Battery Charging should be blinking");
		}
		
		//End MPPT On--------------------------------------------------------------------------------------
		
		//Battery Discharge ---------------------------------------------------

		//wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]")));
		jsDriver.executeScript("arguments[0].scrollIntoView();", MPPTandBattery.get(1));
		
		//Press Battery Discharge Button
		MPPTandBattery.get(1).click();//On Battery Discharge button
		
		ngDriver.waitForAngularRequestsToFinish();
		
		//condition 1:check top right status bar shows "in progrss"

		String ActualBarStatus=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")).getText();
		Assert.assertEquals(ActualBarStatus, ". In progress","Battery Discharge status at top bar");
		
		//Condition 2:expected content console content
		ExpectedConsoleMPPTStatus="Default Battery Discharge upto 10.1. Please click on the button again to stop Discharge process..";
		console=Console();
		//Assert.assertEquals(console[console.length-1],ExpectedConsoleMPPTStatus,"After Battery Discharge");
		if(!console[console.length-1].contains("Default Battery Discharge upto 10.1. Please click on the button again to stop Discharge process"))
		{
			Assert.assertTrue(false, "After discharge in console Discharge message is not displyaing");
		}
		
		Thread.sleep(5000);
		
		//System.out.println(driver.findElement(By.xpath(".//div[@ng-if=\"value.label\" and @style=\"width:160px\" and @class=\"led-label ng-binding ng-scope\" and @ng-bind-html=\"value.label | newlines\"]")).getText());
		SystemData=SystemStatus();
		//key=Vin,Iin,Power_in,Vout,Iout,Power_out,Efficiency,[if discharge is on = Battery V,Battery I]
		String[] DischargeBatterykey= {"Vin","Iin","Power_in","Vout","Iout","Power_out","Efficiency","Battery V","Battery I"};
		
		for(int i=0;i<DischargeBatterykey.length;i++)
		{
			System.out.println("====================\n"+DischargeBatterykey[i]+"="+SystemData.get(DischargeBatterykey[i]));
		}
		
		//condition 3:System status
		if(!(SystemData.get("Iin") <1 && SystemData.get("Power_in")<1 && SystemData.get("Iout") <1 && SystemData.get("Power_out")<1))
		{
			Assert.assertTrue(false, "After Battery Discharge Iin,Power_in,Iout,Power_out in System Status is not less than one");
		}
		//condition 4:checking Battery status blinking or not
		if(!(BatteryStatus()==0))
		{
			Assert.assertTrue(false, "After Battery Discharge: Battery Charging status is blinking but it should not blink");
		}
				
		//condition 5:Discharge status		
		if(!(DischargeStatus()==1))
		{
			Assert.assertTrue(false, "After Battery Discharge: Battery Disharging status is OFF but it should be ON");
		}
		
		//scroll till discharge button is visible
		jsDriver.executeScript("arguments[0].scrollIntoView();", MPPTandBattery.get(1));
		MPPTandBattery.get(1).click();//off Battery Discharge button
		try {
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("toast-container"), "YES"));//wait until popup window shows yes and cancel
		}catch(Exception e)
		{
			throw new RuntimeException("After Discharge Button is OFF waited 60sec for popup which show YES and Cancel");
		}
		driver.findElement(By.xpath(".//button[@ng-click=\"sendPriority()\" and @class=\"btn btn-success\"]")).click();//YES is pressed from the popup appears after pressing discharge button
		
		//-----------------------------------------------------------
		
		//After Discharge is off
		
		System.out.println("\n-----------------------------------------\n");
		
	}
	
	
	@DataProvider(name="ConfigData")
	public Object[][] InputData() throws IOException
	{
		File ConfigData=new File("C:\\Users\\Abhi\\Tenxer\\TestingData\\ConfigureData.xlsx");
		FileInputStream fis=new FileInputStream(ConfigData);
		XSSFWorkbook workbook=new XSSFWorkbook(fis);
		XSSFSheet FirstSheet= workbook.getSheetAt(0);
		
		int row=FirstSheet.getPhysicalNumberOfRows();
		int col =FirstSheet.getRow(0).getPhysicalNumberOfCells();
		
		Object[][] data=new Object[row-1][col];
		for(int i=1;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				data[i-1][j]=FirstSheet.getRow(i).getCell(j).toString();
			}
		}
		//workbook.close();
		return data;
	}


}
