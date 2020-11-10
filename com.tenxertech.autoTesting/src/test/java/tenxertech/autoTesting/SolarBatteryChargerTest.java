package tenxertech.autoTesting;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;

public class SolarBatteryChargerTest extends SolarBatteryChargerBase{
	
	protected String testCaseName="SolarBatteryChargerTest()";
	//Setup the driver settings
	@BeforeTest
	@org.testng.annotations.Parameters(value={"browser","version","platform"})
	public void initialize() throws InterruptedException 
	{
		super.setup();
		super.LandingPage(0);
		super.closePopUp();
	}
	
	@AfterMethod
	public void screenshot(ITestResult result)
	{
		if(ITestResult.FAILURE == result.getStatus())
		{
		String path="../screenshot/"+testCaseName+".png";
		super.screenshot(path);
		}
	}
	
	@AfterTest
	public void destroy()
	{
		
		super.destroy();
	}
	
	@Test
	public void SolarBatteryChargerLiveStreamTest() throws InterruptedException
	{
		//super.PressConfigButton();
		super.testCaseName="SolarBatteryChargerTestLiveStream";	
		driver.findElement(ByAngular.buttonText(super.submitButton));
		if(!super.liveStream(super.submitButton))
		{
			Assert.assertFalse(true,"Solar battery charger live stream is noy working");
		}
	}
 
	//Testing Solar Battery Charger
	@Test(dataProvider="ConfigData")
	public void SolarBatteryChargerConfigTest(String UserInputVoltage,String UserInputCurrent,String UserInputIrradiance ,String UserInputTemp) throws Exception {
		
		System.out.println("====================SolarBatteryCharger Test Start=======================");
		
		testCaseName="SolarBatteryChargerTest("+ UserInputVoltage + "," + UserInputCurrent + "," + UserInputIrradiance + "," + UserInputTemp+")";	
		
		
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
		super.PressConfigButton();
		super.configurButtonCheck();
		//End of configure button----------------------------------------------------------------------------------------------------------
		try {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]")));
		}catch(Exception e)
		{
			throw new RuntimeException("waited for 60sec for MPPT On button to be available",e);
		}
		//This list contains MPPT button and Battery Discharge button
		List<WebElement> MPPTandBattery=driver.findElements(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]"));
		
		//MPPT On--------------------------------------------------------------------------
		Thread.sleep(10000);//charge the battery for 10sec
		//MPPT Button will be pressed
		MPPTandBattery.get(0).click();
		ngDriver.waitForAngularRequestsToFinish();
		
		MPPTOn();
		
		//End MPPT On--------------------------------------------------------------------------------------
		Thread.sleep(10000);//charge the battery for 10sec
		
		//Battery Discharge ---------------------------------------------------
		//Press Battery Discharge Button
		jsDriver.executeScript("arguments[0].scrollIntoView();",MPPTandBattery.get(1));	
		MPPTandBattery.get(1).click();//On Battery Discharge button
		
		int DischargeWait=dischargeBatteryOn();//this function checks discharge on conditions
		if(DischargeWait==1)//if function returns 1 then battery voltage is more than 12 and can use discharge button to off discharge process else wait until automatically turns off
		{
			MPPTandBattery.get(1).click();//off Battery Discharge button
			try {
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("toast-container"), "YES"));//wait until popup window shows yes and cancel
			driver.findElement(By.xpath(".//button[@ng-click=\"sendPriority()\" and @class=\"btn btn-success\"]")).click();//YES is pressed from the popup appears after pressing discharge button
			}catch(Exception e)
			{
				throw new RuntimeException("After Discharge Button is OFF waited 60sec for popup which show YES and Cancel");
			}
			dischargeBatteryOff();
			
		}
		else
		{
			dischargeBatteryOff();
		}
		System.out.println("====================SolarBatteryCharger Test Start=======================");
		
	}
	
	
	@DataProvider(name="ConfigData")
	public Object[][] InputData() throws IOException
	{
		Object[][] data=super.dataProvider("ConfigureData", 0);
		return data;
	}




}
