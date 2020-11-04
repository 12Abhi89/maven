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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CN274_Test extends autoTestingBase{
	
	private static int CHARGE_TIME_200=450;//7.5min Maximum Charging time

	@BeforeMethod
	public void setup()
	{
		super.setup();
		super.LandingPage(2);
	}
	
	@AfterMethod
	public void destroy()
	{
		super.destroy();
	}
	
	@Test(dataProvider="CN274Data")
	public void CN274_Auto_Test(String LoadCurrent)
	{
		try {
			super.closePopUp();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//input configure value
		List<WebElement> dropdown=driver.findElements(ByAngular.model("tnxmodel"));
				
		//Selects input from Voc dropdown
		dropdown.get(0).click();
		Select Voc=new Select(dropdown.get(0));
		Voc.selectByVisibleText(LoadCurrent);
		
		driver.findElement(By.xpath("//*[@id=\"stepformcontainer\"]/div[4]")).click();
		
		int Charging=0;
		String TopBarStatus;
		//String[] console=super.Console();
		List<String> console = Arrays.asList(super.Console());
		while(!(console.contains("Capcitor fully charged")))
		{
			//condition1
			TopBarStatus=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")).getText();
			Assert.assertEquals(TopBarStatus,". In progress","During Capacitor Charging Top Bar not showing In Progress |");
			//===========
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			console = Arrays.asList(super.Console());
			Charging++;
		}
		System.out.println("\nChargingSeconds"+Charging);
		
//		if(!(Charging<CHARGE_TIME))
//		{
//			Assert.assertFalse(true, "Capasitor charging time exceeds 7.5min which is maximum");
//		}
		
		
		SystemData=super.SystemStatus();
		String[] key={"Capacitor (max 2.5V) ","Input Current","Output Current","Input Voltage","Output Voltage","Boost Voltage","LDO Voltage"};
		System.out.println("-----------\n"+SystemData.get(key[4]));
		
		///condition1
//		TopBarStatus=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")).getText();
//		Assert.assertEquals(TopBarStatus,". Ready","During Capacitor DisCharging Top Bar not showing Ready |");
		
		int discharging=0;
		while(SystemData.get(key[4]) > 0)
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
		}
		
		
		System.out.println("\nFinalDisChargingSeconds"+discharging);
		
		
		System.out.println("\n====================CN274 End ===============================");
	}
	
	
	@DataProvider(name="CN274Data")
	public Object[][] InputData() throws IOException
	{
		File ConfigData=new File("../com.tenxertech.autoTesting/TestingData/CN274Data.xlsx");
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
