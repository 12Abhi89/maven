package tenxertech.autoTesting;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class AS104_Test extends autoTestingBase {
	
	protected String Button="START";
	protected int LoadSetPos=0;
	protected List<WebElement> config;//this contains confi1,config2,config3,in rush
	protected List<WebElement> configDropDown;//from this we access drop down
	protected List<WebElement> start;//all 4 start button
	protected int config_1=0;
	protected int config_2=1;
	protected int config_3=2;
	protected int In_Rush=3;
	protected String[] key= {"Load current","Battery current","Load voltage","Battery Voltage","Load power"};
	protected String testCaseName="AS104_Test";
	protected int powerset=0;
	
	
	@BeforeTest
	public void setup()
	{
		
		super.setup();
		super.LandingPage(1);
		
		//super.closePopUp(Button);
		
		config=driver.findElements(By.id("home-tab"));//contains 4 config's
		for(WebElement a: config)
		System.out.println("\n->"+a.getText());
		
		configDropDown=driver.findElements(ByAngular.model("tnxmodel"));//contains dropdown
		//List<WebElement> start=driver.findElements(By.xpath("//*[contains(text(),'START')]"));//contains 4 start button
		start=driver.findElements(ByAngular.buttonText(Button));//contains 4 strat buttons

	}
	

	@AfterMethod
	public void screenshot(ITestResult result)
	{
		if(ITestResult.FAILURE == result.getStatus())
		{
		String path="../com.tenxertech.autoTesting/target/surefire-reports/screenshot/"+testCaseName+".png";
		super.screenshot(path);
		super.status="Yes";
		}
	}
	
	@AfterTest
	public void destroy()
	{
		
		super.destroy();
	}
	
	public void Check(String LoadVoltage)
	{
		//wait until top bar shows ready
		int k=0;
		while(!(super.TopBarStatus()==1))
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			k++;
			if(k>60)
			{
				Assert.assertFalse(true,"During condition check waited 60 sec for top bar to show READY");
			}
		}
		
		String a=LoadVoltage;
		String[] b=a.split(" ");
		float loadVol=Float.parseFloat(b[0].strip());
		System.out.println("\n>"+LoadVoltage+" :"+loadVol);
	
	
		SystemData=super.SystemStatus();
		
		float ActualLoadVol=SystemData.get(key[2]);
		
		float max=loadVol+2;
		float min=loadVol-2;
		float roundedLoadVol=(float) Math.ceil(ActualLoadVol);
		System.out.println("ladvol"+ActualLoadVol);
		
		if(!(roundedLoadVol<=max && roundedLoadVol >=min))
		{
			Assert.assertFalse(true, "Voltage Load in System status is "+ActualLoadVol+" Expected value is near "+ loadVol+" V |");
		}
		
		
		//console check
		//String[] console=super.Console();
		List<String> console=Arrays.asList(super.Console());
		int i=0;
		for(String j:console)
		{
			System.out.println("\n>"+i+"|"+j+"|");
			i++;
		}
		int lastIndex=console.lastIndexOf("Load set");
		if(lastIndex>LoadSetPos)
			LoadSetPos=lastIndex;
		else
			Assert.assertFalse(true,"Console is not diplaying Load Set for"+loadVol+"V |");
		
		System.out.println("\n=>"+console.get(lastIndex-2));
		System.out.println("\nloadset"+LoadSetPos);
		
		
		
	}
	
	public void InRushCheck(int battery,String power)
	{
		int BatVol[]= {8,7,11,15};

		List<String> console=Arrays.asList(super.Console());
		
		int k=powerset;
		while(!((k=console.lastIndexOf("Output Power set to 55W"))>powerset))
		{
			console=Arrays.asList(super.Console());
		}

		powerset=k;
//		while(!console.contains("Output Power set to 55W"))
//		{
//
//			console=Arrays.asList(super.Console());
//
//		}
		System.out.println("=========================================="+powerset);
		for(String i:console)
		{
			System.out.println("\n"+i);
		}

//		SystemData=super.SystemStatus();
//		float ActualLoadVol=SystemData.get(key[3]);
//		
//		float max=BatVol[battery]+1;
//		float min=BatVol[battery]-1;
//		float roundedLoadVol=(float) Math.ceil(ActualLoadVol);
//		System.out.println("ladvol"+ActualLoadVol);
//		
//		if(!(roundedLoadVol<=max && roundedLoadVol >=min))
//		{
//			Assert.assertFalse(true, "For in Rush configuration Battery Voltage in System status is "+ActualLoadVol+" Expected value is near "+ battery+"="+BatVol[battery]+" V |");
//		}
		console=null;
		
	}
	public void Start()
	{
		//String path="//button[@ng-class=\"getComToArr(data.class)\" and @class=\"btn btn-primary btn-element ng-binding fat-btn\" and @title=\"\" and @type=\"submit\"]";
	    super.pressButton(Button);
	}

	public void batteryStartButton(int bat)
	{
		wait.until(ExpectedConditions.elementToBeClickable(start.get(3))).click();
		try {
			if(driver.findElement(By.id("toast-container")).isDisplayed())
			{
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("toast-container"), "YES"));//wait until popup window shows yes and cancel
			driver.findElement(By.xpath(".//button[@ng-click=\"sendPriority()\" and @class=\"btn btn-success\"]")).click();//YES is pressed from the popup appears after pressing discharge button
			}
			//wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(ByAngular.buttonText("YES")))).click();
			}catch(Exception e)
			{
				System.out.println("yes");
			}
	}
	
	public void configuration(int selectedConfig,String[][] inputs)
	{
		config.get(selectedConfig).click();
		
		for(String[] i:inputs)
		{
		configDropDown.get(Integer.parseInt(i[0])).click();
		Select Config1LoadVoltage=new Select(configDropDown.get(Integer.parseInt(i[0])));
		Config1LoadVoltage.selectByVisibleText(i[1]);
		}
	}
	@Test(dataProvider="configOneData")
	public void AS104_Config_1_Test(String LoadVoltage,String LoadCurrent) throws InterruptedException
	{
		testCaseName="AS104_Config_1_Test("+LoadVoltage+","+LoadCurrent+")";
		System.out.println("=====================Config 1|"+testCaseName+"|=============================");
		//config.get(config_1).click();
		String[][] config_1_inputs= {{"0","20 V"},{"1","2.1 A"}};
		configuration(config_1,config_1_inputs);
		Start();
		Check("20 V");
		System.out.println("=====================End Config 1=============================");
	}
		
	@Test(dataProvider="configTwoData")
	public void AS104_Config_2_Test(String LoadVoltage,String LoadCurrent) throws InterruptedException
	{
		testCaseName="AS104_Config_2_Test("+LoadVoltage+","+LoadCurrent+")";
		System.out.println("=====================Config 2|"+testCaseName+"|=============================");
		String[][] config_2_inputs= {{"2","24 V"},{"3","1.6 A"}};
		configuration(config_2,config_2_inputs);

		wait.until(ExpectedConditions.elementToBeClickable(start.get(1))).click();
		
		//pressStart();
		Check("24 V");
		System.out.println("=====================End Config 2=============================");
	}


	@Test(dataProvider="configThreeData")
	public void AS104_Config_3_Test(String LoadVoltage,String LoadCurrent) throws InterruptedException
	{
		testCaseName="AS104_Config_3_Test("+LoadVoltage+","+LoadCurrent+")";
		System.out.println("=====================Config 3|"+testCaseName+"|=============================");
		//Config 3
		
		String[][] config_3_inputs= {{"4","32 V"},{"5","1.4 A"}};
		configuration(config_3,config_3_inputs);
		
		
		//driver.findElement(ByAngular.buttonText(SubmitButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(start.get(2))).click();
		Check("32 V");
		System.out.println("=====================End Config 3=============================");
	}
	
	@Test(dataProvider="inRushData")
	public void AS104_In_Rush_Test(String LoadVoltage,String LoadPower,String LoadDuration) throws InterruptedException
	{
		testCaseName="AS104_In_Rush_Test("+LoadVoltage+","+LoadPower+","+LoadDuration+")";
		System.out.println("=====================In Rush|"+testCaseName+"|=============================");
		
		//In Rush
		String[][] In_Rush_inputs= {{"12","10"},{"6","32 V"},{"7","55 W"}};
		configuration(In_Rush,In_Rush_inputs);
		
		jsDriver.executeScript("arguments[0].click();", configDropDown.get(8));//0 is selected
//		wait.until(ExpectedConditions.elementToBeClickable(start.get(3))).click();
		batteryStartButton(3);
		InRushCheck(0,"55 W");
		Thread.sleep(5000);

		jsDriver.executeScript("arguments[0].click();", configDropDown.get(9));//2 is selected
		batteryStartButton(3);
		InRushCheck(1,"55 W");
		Thread.sleep(5000);
		
		jsDriver.executeScript("arguments[0].click();", configDropDown.get(10));//3 is selected
		batteryStartButton(3);
		InRushCheck(2,"55 W");
		Thread.sleep(5000);

		jsDriver.executeScript("arguments[0].click();", configDropDown.get(11));//4 is selected
		batteryStartButton(3);
		InRushCheck(3,"55 W");
		Thread.sleep(5000);
		

		
		
		System.out.println("=====================End Config 4=============================");
		
	}
	@Test
	public void AS104LiveStreamTest()
	{
		super.testCaseName="AS104_In_Rush_LiveStream_ Test";
		if(!super.liveStream(Button))
		{
			Assert.assertFalse(true,"AS104 live stream error");
		}
	}
	
	@DataProvider(name="configOneData")
	public Object[][] config_1_data() throws IOException
	{
		
		Object[][] data=super.dataProvider("AS104", 0);
		return data;
		
	}
	
	@DataProvider(name="configTwoData")
	public Object[][] config_2_data() throws IOException
	{
		
		Object[][] data=super.dataProvider("AS104", 1);
		return data;
		
	}
	
	@DataProvider(name="configThreeData")
	public Object[][] config_3_data() throws IOException
	{
		
		Object[][] data=super.dataProvider("AS104", 2);
		return data;
		
	}
	@DataProvider(name="inRushData")
	public Object[][] In_Rush_data() throws IOException
	{
		
		Object[][] data=super.dataProvider("AS104", 3);
		return data;
		
	}

}
