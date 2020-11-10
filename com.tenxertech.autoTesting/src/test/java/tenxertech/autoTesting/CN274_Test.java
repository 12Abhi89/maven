package tenxertech.autoTesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CN274_Test extends autoTestingBase{
	
	private static int CHARGE_TIME_100=540;//480 sec to charge + 60sec offset
	private static int CHARGE_TIME_200=450;//7.5min Maximum Charging time
	private static int CHARGE_TIME_300=485;//425+60 34
	private static int CHARGE_TIME_400=505;//445+60 35//1000=94x5 and 8
	private static int CHARGE_TIME_1000=470;
	protected String Button="DISCHARGE";

	@BeforeTest
	public void initialize() throws InterruptedException 
	{
		super.setup();
		super.LandingPage(2);
		//super.closePopUp(Button);
	}
	
	@AfterMethod
	public void screenshot(ITestResult result)
	{
		if(ITestResult.FAILURE == result.getStatus())
		{
		String path="../com.tenxertech.autoTesting/target/surefire-reports/screenshot/"+super.testCaseName+".png";
		super.screenshot(path);
		}
	}
	
	@AfterTest
	public void destroy()
	{
		super.destroy();
	}
	
	@Test
	public void CN274_LiveStream()
	{
		super.testCaseName="CN274_LiveStream";
		if(!super.liveStream(Button))
		{
			Assert.assertFalse(true,"CN274 live stream is not displaying");
		}
	}

	public void chargingCheck()
	{
		
	}
	
	public void dischargingCheck()
	{
		
	}
	@Test(dataProvider="CN274Data")
	public void CN274_Auto_Test(String LoadCurrent)
	{
		super.testCaseName="CN274_Test("+LoadCurrent+")";
		System.out.println("===================="+testCaseName+" Start=======================");
		
		//input configure value
		List<WebElement> dropdown=driver.findElements(ByAngular.model("tnxmodel"));
				
		//Selects input from Voc dropdown
		dropdown.get(0).click();
		Select Voc=new Select(dropdown.get(0));
		Voc.selectByVisibleText(LoadCurrent);
		
		super.pressButton(Button);
		//driver.findElement(By.xpath("//*[@id=\"stepformcontainer\"]/div[4]")).click();
		
		int Charging=0;
		//String[] console=super.Console();
		List<String> console = Arrays.asList(super.Console());
		while(!(console.contains("Capcitor fully charged")))
		{
			//condition1
			//TopBarStatus=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")).getText();
			//Assert.assertEquals(TopBarStatus,". In progress","During Capacitor Charging Top Bar not showing In Progress |");
			if(!(super.TopBarStatus()==0))
			{
				Assert.assertFalse(true,"During Capacitor Charging Top Bar not showing In Progress |");
			}
			//===========
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			console = Arrays.asList(super.Console());
			Charging++;
			//System.out.println("\nc>"+Charging);
			if(!(Charging<CHARGE_TIME_1000+60))
			{
				Assert.assertFalse(true, "Capasitor charging time "+Charging+" exceeds "+CHARGE_TIME_1000+"seconds which is maximum");
			}
		}
		System.out.println("\nTotal Charging time Seconds="+Charging);
		
		
		
		
		
		SystemData=super.SystemStatus();
		String[] key={"Capacitor (max 2.5V) ","Input Current","Output Current","Input Voltage","Output Voltage","Boost Voltage","LDO Voltage"};
		
		///condition1
//		TopBarStatus=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")).getText();
//		Assert.assertEquals(TopBarStatus,". Ready","During Capacitor DisCharging Top Bar not showing Ready |");
		
		int discharging=0;
		while(SystemData.get(key[4]) > 0.0)
		{
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SystemData.clear();
			SystemData=super.SystemStatus();
			discharging++;
			//System.out.println("\nd>"+discharging);
			
			
			if(!(super.TopBarStatus()==1))
			{
				Assert.assertFalse(true,"During discharge top bar not showing Ready");
			}
		}
		
		
		System.out.println("\nTotal DisCharging time in Seconds="+discharging);
		
		
		System.out.println("\n====================CN274 End ===============================");
	}
	
	
	@DataProvider(name="CN274Data")
	public Object[][] InputData() throws IOException
	{
		Object[][] data=super.dataProvider("CN274Data", 0);
		return data;
	}
}
