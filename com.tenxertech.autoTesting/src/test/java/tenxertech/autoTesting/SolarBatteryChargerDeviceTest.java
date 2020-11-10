package tenxertech.autoTesting;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;

public class SolarBatteryChargerDeviceTest extends SolarBatteryChargerBase {
	
	@BeforeTest
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
		String path="../screenshot/SolarBatteryChargerDeviceCheck.png";
		super.screenshot(path);
		}
	}
	
	@AfterTest
	public void destroy()
	{
		
		super.destroy();
	}
	@Test
	public void SolarBatteryChargerDeviceCheckLiveStreamTest() throws InterruptedException
	{
		super.testCaseName="SolarBatteryChargerDeviceCheckLiveStreamTest";
		//super.PressConfigButton();
		driver.findElement(ByAngular.buttonText(super.submitButton));
		if(!super.liveStream(super.submitButton))
		{
			Assert.assertFalse(true,"Solar battery charger live stream is noy working");
		}
	}
	@Test()
	public void SolarBatteryChargerDeviceCheck() throws InterruptedException
	{
		System.out.println("====================SolarBatteryChargerDevice Test Start=======================");
		
		try
		{
			super.PressConfigButton();//on
		}catch(Exception e)
		{
			throw new RuntimeException("Configuartion problem"+e);
		}
		
		String[] consoleData;
		int i=0;
		while(true)
		{
		consoleData=super.Console();
		System.out.println("\n=="+consoleData[(consoleData.length)-1]);
		if((consoleData[(consoleData.length)-1]).contains("collecting data"))
			break;
		Thread.sleep(1000);
		i++;
		if(i>60)
			Assert.assertFalse(true,"Waited 60sec for console to show 'collecting data'");
		}
		
		
		try {
			super.takeShot(true);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".glassc-conn"))).click();//off
			super.takeShot(false);
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Assert.assertFalse(true,"after configuration console showed collecting data but can't disconnect the device");
		}
		try {
		super.PressConfigButton();//on
		super.takeShot(true);
		super.configurButtonCheck();
		super.takeShot(false);
		System.out.println("config done");
		}catch(Exception e)
		{
			throw new RuntimeException("After Disconnecting for config button device is not reconnecting |"+e);
		}
		//This list contains MPPT button and Battery Discharge button
		List<WebElement> MPPTandBattery=driver.findElements(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]"));
		//MPPT On--------------------------------------------------------------------------
		Thread.sleep(5000);//charge the battery for 10sec
		//MPPT Button will be pressed
		MPPTandBattery.get(0).click();
		ngDriver.waitForAngularRequestsToFinish();
		Thread.sleep(3000);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//OFF
		Thread.sleep(5000);
		try
		{
		super.PressConfigButton();//on
		super.takeShot(true);
		super.configurButtonCheck();
		super.takeShot(false);
		System.out.println("MPPT done");
		}catch(Exception e)
		{
			throw new RuntimeException("After Disconnecting for MPPT On button device is not reconnecting |"+e);
		}
		MPPTandBattery.get(1).click();//start discharge
		ngDriver.waitForAngularRequestsToFinish();
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//OFF
				
		Thread.sleep(5000);
		try
		{
		super.PressConfigButton();//on
		super.takeShot(true);
		super.configurButtonCheck();
		super.takeShot(false);
		System.out.println("dis done");
		}catch(Exception e)
		{
			throw new RuntimeException("After Disconnecting for Discharge button device is not reconnecting |"+e);
		}	
		System.out.println("====================SolarBatteryChargerDevice Test End=======================");
	}
}
