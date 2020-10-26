package tenxertech.autoTesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;

public class SolarBatteryChargerTest extends SolarBatteryChargerBase{
	
	//Setup the driver settings
	@BeforeMethod
	public void initialize() 
	{
		super.setup();
	}
	
	//Close the driver
	@AfterMethod
	public void destroySBC(ITestResult result)
	{
		//String path="../maven.selenium.testng/target/surefire-reports/screenshot/"+inputvoc+"_"+inputioc+"_"+inputirr+"_"+inputtemp+".png";
		String path="../com.tenxertech.autoTesting/target/surefire-reports/screenshot/"+inputvoc+"_"+inputioc+"_"+inputirr+"_"+inputtemp+".png";
		super.destroy(result,path);
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
		
		inputvoc=UserInputVoltage;
		inputioc=UserInputCurrent;
		inputirr=UserInputIrradiance;
		inputtemp=UserInputTemp;
		
		super.renesasLandingPage();
		
		super.closePopUp();
		
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
		System.out.println("\n-----------------------------------------\n");
		
	}
	
	
	@DataProvider(name="ConfigData")
	public Object[][] InputData() throws IOException
	{
		File ConfigData=new File("../com.tenxertech.autoTesting/TestingData/ConfigureData.xlsx");
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
