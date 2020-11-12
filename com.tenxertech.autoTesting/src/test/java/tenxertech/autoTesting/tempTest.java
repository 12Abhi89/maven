package tenxertech.autoTesting;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;

import io.github.bonigarcia.wdm.WebDriverManager;

public class tempTest extends autoTestingBase {

	
	@BeforeTest
	public void setup()
	{
		//super.setup();
	}
	@AfterTest
	public void destroy()
	{
//		String path=super.screenshotPath+super.testCaseName+".png";
//		super.screenshot(path);
		driver.quit();
	}
	@Test()
	public void Test()
	{
		FirefoxBinary fbinary=new FirefoxBinary();
		fbinary.addCommandLineOptions("--headless");
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions fo=new FirefoxOptions();
		fo.setBinary(fbinary);
		WebDriver driver=new FirefoxDriver();
		
		driver.get("http://google.com");
		
//		super.testCaseName="test1";
//		super.takeShot(true);
		System.out.println("\n===============end Test=====================\n"+driver.getTitle());
			
	}
//	
//	@Test(priority=2)
//	public void Test2()
//	{
//		System.out.println("===============end Test=====================");
//	}
}
