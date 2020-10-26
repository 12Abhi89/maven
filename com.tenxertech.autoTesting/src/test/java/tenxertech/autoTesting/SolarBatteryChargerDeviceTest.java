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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SolarBatteryChargerDeviceTest extends SolarBatteryChargerBase {
	
	
	public int connect()
	{
		if(driver.findElement(By.xpath("//*[@id=\"navbar6\"]/ul[3]/li[2]/div/span/span/span[2]")).getText()=="Connected")
			return 1;
		else
			return 0;
	}
	
	
	@BeforeMethod
	public void setup()
	{
		super.setup();
	}
	
	@AfterMethod
	public void destroy(ITestResult result)
	{
		String path="../com.tenxertech.autoTesting/target/surefire-reports/screenshot/deviceCheck.png";
		super.destroy(result, path);
	}
	
	@Test
	public void SolarBatteryChargerDeviceCheck() throws InterruptedException
	{
		System.out.println("====================Device Test Start=======================");
		super.renesasLandingPage();
		try {
			closePopUp();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			super.PressConfigButton();//on
		}catch(Exception e)
		{
			throw new RuntimeException("Configuartion problem");
		}
		
		String[] consoleData;
		while(true)
		{
		consoleData=super.Console();
		System.out.println("\n=="+consoleData[(consoleData.length)-1]);
		if((consoleData[(consoleData.length)-1]).contains("collecting data"))
			break;
		Thread.sleep(1000);
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".glassc-conn"))).click();//off
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		super.PressConfigButton();//on
		super.configurButtonCheck();
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
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//OFF
		Thread.sleep(5000);
		try
		{
		super.PressConfigButton();//on
		super.configurButtonCheck();
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
		super.configurButtonCheck();
		}catch(Exception e)
		{
			throw new RuntimeException("After Disconnecting for Discharge button device is not reconnecting |"+e);
		}	
		System.out.println("====================Device Test End=======================");
	}
}
