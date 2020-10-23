package tenxertech.autoTesting;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class autoTestingBase {
	


	protected WebDriver driver;
	protected JavascriptExecutor jsDriver;
	protected NgWebDriver ngDriver;
	protected String pageurl = "https://renesas.evmlabs.com/#!/";
	protected WebDriverWait wait;
	public static String LoginPage="Tenxer - Login";
	public static String EvmSelectingPage="Tenxer -";
	
	
	public void setup()
	{
		System.out.println("===========================================");
		WebDriverManager.chromedriver().setup();
		//driver=new FirefoxDriver();
		driver=new ChromeDriver();
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
		wait.until(ExpectedConditions.titleIs(EvmSelectingPage));
		//ngDriver.waitForAngularRequestsToFinish();
		//wait.until(ExpectedConditions.titleIs(EvmSelectingPage));
		//Assert.wait.until(ExpectedConditions.titleContains(EvmSelectingPage));
		//Assert.assertEquals(driver.getTitle(), EvmSelectingPage,"Home page is not loaded");
	}
	
	public void destroy()
	{
		driver.close();
	}
	
	public void destroy(ITestResult result,String path)
	{
		

		if(ITestResult.FAILURE == result.getStatus())
		{
			System.setProperty("org.uncommons.reportng.escape-output", "false");

			
			File file = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
			//String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
			
			try {
				
				
			FileUtils.copyFile(file, new File(path),true);
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		
		}
	
		driver.close();
	}
	
	

}
