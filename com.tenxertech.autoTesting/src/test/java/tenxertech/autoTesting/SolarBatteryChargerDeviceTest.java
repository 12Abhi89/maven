package tenxertech.autoTesting;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SolarBatteryChargerDeviceTest extends SolarBatteryChargerBase {
	
	@BeforeMethod
	public void setup()
	{
		super.setup();
	}
	
	@AfterMethod
	public void destroy(ITestResult result)
	{
		String path="../target/surefire-reports/screenshot/deviceCheck.png";;
		super.destroy(result,path);
	}
	
	@Test
	public void SolarBatteryChargerDeviceCheck() throws InterruptedException
	{
		System.out.println("===========================================");
		super.renesasLandingPage();
		try {
			closePopUp();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.PressConfigButton();
		
		System.out.println("++++++++++++");
		try {
		driver.findElement(By.xpath("//button[@class=\"toast-close-button ng-scope\" and @ng-click=\"close(true, $event)\"]")).click();
		System.out.println("l");
		}catch(Exception e)
		{
			System.out.println("exceptipn");
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
		System.out.println("after off");
		//driver.findElement(By.xpath("//*[@id=\"video-live\"]/div/div[2]/div/iframe")).getText();
		//wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//on
		super.PressConfigButton();//on
		super.configurButtonCheck();
		
		//This list contains MPPT button and Battery Discharge button
				List<WebElement> MPPTandBattery=driver.findElements(By.xpath("//button[@class=\"btn btn-primary btn-element  fat-btn\" and @ng-click=\"formsubmit($event,data.allattrib)\"]"));
				
				//MPPT On--------------------------------------------------------------------------
				Thread.sleep(10000);//charge the battery for 10sec
				//MPPT Button will be pressed
				MPPTandBattery.get(0).click();
				System.out.println("==================MPPT On=========================");
				ngDriver.waitForAngularRequestsToFinish();
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//OFF
				System.out.println("====================Disconnect=======================");
				Thread.sleep(10000);
				super.PressConfigButton();//on
				System.out.println("=====================connect======================");
				super.configurButtonCheck();
				System.out.println("====================config check=======================");
				
				MPPTandBattery.get(1).click();//start discharge
				System.out.println("==================discharge=========================");
				ngDriver.waitForAngularRequestsToFinish();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//OFF
				
				System.out.println("====================Disconnect=======================");
				Thread.sleep(10000);
				super.PressConfigButton();//on
				System.out.println("=====================connect======================");
				super.configurButtonCheck();
				System.out.println("====================config check=======================");
				MPPTandBattery.get(1).click();//start discharge
				System.out.println("==================discharge=========================");
				ngDriver.waitForAngularRequestsToFinish();
				int DischargeWait=super.dischargeBatteryOn();//this function checks discharge on conditions
				if(DischargeWait==1)//if function returns 1 then battery voltage is more than 12 and can use discharge button to off discharge process else wait until automatically turns off
				{
					 //scroll till discharge button is visible
					//jsDriver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
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
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".glassc-conn"))).click();//OFF
				System.out.println("=======================OFF====================");
				Thread.sleep(10000);
				super.PressConfigButton();//on
				System.out.println("=====================ON======================");
				super.configurButtonCheck();
				
		System.out.println("===========================================");
	}
}
