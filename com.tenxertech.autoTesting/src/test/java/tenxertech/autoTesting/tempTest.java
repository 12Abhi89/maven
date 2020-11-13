package tenxertech.autoTesting;

import java.time.LocalTime;
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

	

	@Test()
	public void Test()
	{
//		FirefoxBinary fbinary=new FirefoxBinary();
//		fbinary.addCommandLineOptions("--headless");
//		WebDriverManager.firefoxdriver().setup();
//		FirefoxOptions fo=new FirefoxOptions();
//		fo.setBinary(fbinary);
//		WebDriver driver=new FirefoxDriver();
//		
//		driver.get("http://google.com");
		
		int start=java.time.LocalTime.now().toSecondOfDay();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int end=java.time.LocalTime.now().toSecondOfDay();
		
		System.out.println("rr:"+(end-start));
		
//		super.testCaseName="test1";
//		super.takeShot(true);
		System.out.println("\n===============end Test=====================\n");
			
	}

}
