package tenxertech.autoTesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class autoTestingBase {
	

	//-----------------------------------------
	static String username = "1289prakash"; // Your username
    static String authkey = "32YV5Rf7cVghW2yEUlzCUaT7qxIuC5lyuxZ9Wl6juPUbJD2gpq"; 
    //-----------------------------------------
	protected WebDriver driver;
	protected JavascriptExecutor jsDriver;
	protected NgWebDriver ngDriver;
	protected String pageurl = "https://renesas.evmlabs.com/#!/";
	protected WebDriverWait wait;
	public static String LoginPage="Tenxer - Login";
	public static String EvmSelectingPage="Tenxer - Login";
	public HashMap<String,Float>  SystemData;//to store system data String is for key float is for value
	protected boolean screenShotTaken=false;
	protected String testCaseName;
	protected String status="No";
	protected String screenshotPath="com.tenxertech.autoTesting/screenshot/";
	
	public void setup()
	{
		System.out.println("===========================================");
		WebDriverManager.chromedriver().setup();
//		driver=new FirefoxDriver();
		driver=new ChromeDriver();
		//---------------------lambda Test-----------------------
		
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability("build", "Tenxer");
//		capabilities.setCapability("name", "AutoTesting");
//		capabilities.setCapability("platform", "Windows 10");
//		capabilities.setCapability("browserName", "Chrome");
//		capabilities.setCapability("version","86.0");
//		capabilities.setCapability("resolution","1920x1080");
//		
//		try {//https://1289prakash:32YV5Rf7cVghW2yEUlzCUaT7qxIuC5lyuxZ9Wl6juPUbJD2gpq@hub.lambdatest.com/wd/hub
//    		driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.lambdatest.com/wd/hub"), capabilities);
//        } catch (MalformedURLException e) {
//            System.out.println("Invalid grid URL");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        
		//--------------------------------------------
		jsDriver=(JavascriptExecutor) driver;
		ngDriver=new NgWebDriver(jsDriver);
		wait = new WebDriverWait(driver,60);
		
		driver.get(pageurl);
		driver.manage().window().maximize();
		ngDriver.waitForAngularRequestsToFinish();
		
		Assert.assertEquals(driver.getTitle(), LoginPage);
		
		//Login Page
		driver.findElement(ByAngular.model("username")).sendKeys("abhishek@tenxertech.com");
		driver.findElement(ByAngular.model("password")).sendKeys("4KSVHCgxc6p7dV2");
		driver.findElement(ByAngular.buttonText("Login")).click();
		
		
		ngDriver.waitForAngularRequestsToFinish();
		wait.until(ExpectedConditions.titleIs(EvmSelectingPage));

	}
	
	public void destroy()
	{
		//driver.close();
//		if (driver != null) {
//            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit(); //really important statement for preventing your test execution from a timeout.
        //}
	}
	
	public void screenshot(String path)
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
	public void takeShot(boolean value)
	{
		//String path=screenshotPath+testCaseName+".png";
		String path="..\\com.tenxertech.autoTesting\\screenshot\\";
		System.out.println("\npath"+path);
		if(value)
		{
		screenshot(path);
		screenShotTaken=value;
		}
		else
		{
			File shot = new File(path);
			shot.delete();
			screenShotTaken=value;
		}
	}

	
	public void destroy(ITestResult result,String path)
	{
		

		if(ITestResult.FAILURE == result.getStatus() && (!screenShotTaken))
		{
			screenshot(path);
		}
	
		driver.close();
	}
	
	public boolean liveStream(String buttonName)
	{
		driver.switchTo().frame(0);
		int bit=0;
		//int total=driver.findElements(By.xpath("html/body/a/img")).size();
		try {
		System.out.println("=$"+driver.findElement(By.id("registernow")).getText());
		wait.until(ExpectedConditions.elementToBeClickable(By.id("register"))).click();
		//driver.findElement(By.id("register")).click();
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("curbitrate1")));
		
		String KBits="";
		String[] kb;
		
		int counter=0;
		System.out.println("BW");
		while(bit<=100)
		{
			KBits=driver.findElement(By.id("curbitrate1")).getText();
			if(KBits.isEmpty())
			{
				continue;
			}
			System.out.println("Kbits;"+KBits);
			kb=KBits.split(" ");
			System.out.println("kb0:"+kb[0]);
			System.out.println("kb1"+kb[1]);
			bit=Integer.parseInt(kb[0]);
			System.out.println("Wbit:"+bit);
			try {
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("\nSplit Skipped");
			}
			counter++;
			if(counter>=60)
			{
				bit=0;
				break;
			}
			
		}
		System.out.println("bit="+bit);
		driver.switchTo().parentFrame();
		Actions actions = new Actions(driver);
		//WebElement close=driver.findElement(By.xpath("//*[@id=\"video-live\"]/div/div[1]/div[2]/button[3]"));
		WebElement close=driver.findElement(ByAngular.buttonText(buttonName));
		actions.moveToElement(close).perform();
		}catch(Exception e)
		{
			Assert.assertFalse(true,"Live Streaming Error :\n"+e);
		}
		//curbitrate1
		if(bit>100)
			return true;
		else
			return false;
	}
	
	public void eva()
	{
		//Switch to Eva chat bot
		try {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
		}catch(Exception e)
		{
			throw new RuntimeException("waited 60sec to switch to eva bot frame",e);
		}
		String EvaPath="//html/body/div/div/div/nav/div/button[@class='min-max-toggle btn btn--icon' and @aria-label='minimize chat window toggle']";
		try {
		//waits to load Eva Chat bot
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(EvaPath)));
		}catch(Exception e)
		{
			throw new RuntimeException("waited 60sec to load content of Eva chat bot",e);
		}
		
		//minimizes Eva Chat bot
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver.findElement(By.xpath(EvaPath)).click();
		
		//switch to parrent frame
		driver.switchTo().parentFrame();
	}
	public void closePopUp()
	 {
		
				eva();
				//input configure value
				//List<WebElement> temp=driver.findElements(ByAngular.model("tnxmodel"));
						
				try {
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='wmClose notop']"))).click();
				}catch(Exception e)
				{
					throw new RuntimeException("user guide is not apreared");
				}
//				if(!(driver.findElements(By.xpath("//button[@class='wmClose notop']")).size() >0))
//				{
//					WebElement element = driver.findElement(By.xpath("//button[@class='wmClose notop']"));
//					
//					jsDriver.executeScript("arguments[0].click();", element);
//					//temp.get(0).click();
//					//driver.findElement(ByAngular.buttonText(Button)).click();
//					//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='wmClose notop']")));
//					//Minimizes user guide pop up
//					//driver.findElement(By.xpath("//button[@class='wmClose notop']")).click();
//				}
//				else
//				{
//					try {
//						//Waits Till User guide close button is available
//						wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='wmClose notop']")));
//						//Minimizes user guide pop up
//						driver.findElement(By.xpath("//button[@class='wmClose notop']")).click();
//						
//						}catch(Exception e)
//						{
//							throw new RuntimeException("waited 60sec to load user guide popup",e);
//						}
//				}
				
	 }
	
	//returns console data
	public String[] Console()
	{
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByAngular.repeater("op in outputTrans track by $index")));
		List<WebElement> Console=driver.findElements(ByAngular.repeater("op in outputTrans track by $index"));
		String[] con=new String[Console.size()];
		int i=0;
		for(WebElement c:Console)
		{
			con[i]=c.getText().toString();
			i++;
		}
		return con;
	}
	
	//System status data will be displayed
		
		public HashMap<String, Float> SystemStatus()
				{
					//waits till system status is visible in page
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ByAngular.repeater("value in outputTrans track by $index")));
							
							//prints System status data
							//String obj;
							HashMap<String,Float> SysMap=new HashMap<String,Float>();
							String[] obj,obj2;
							List<WebElement> SystemStatus=driver.findElements(ByAngular.repeater("value in outputTrans track by $index"));
							//Object[][] values= new Object[SystemStatus.size()][2];
							float temp;
							for(WebElement x : SystemStatus)
							{
								
								//values[i]=x.getText().split("\n");
								obj=x.getText().split("\n");
								
								obj2=obj[1].split(" ");
								if(obj2[0].contains("%"))
								{
									//obj2[0].substring(0, (obj2[0].length())-2);
									//System.out.println("sub "+obj2[0].substring(0, (obj2[0].length())-1));
									temp = Float.parseFloat(obj2[0].substring(0, (obj2[0].length())-1));
									
									
								}
								else if(obj2[0].contains("-"))
								{
									temp=-1;
								}
								else
								{
									temp = Float.parseFloat(obj2[0]);
								}
								SysMap.put(obj[0], temp);
							}
							return SysMap;

				}
		//return 1 if topbar shows Ready and return 0 if top bar shows inprogress
		public int TopBarStatus()
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")));
			WebElement TopBar=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]"));
			if(TopBar.getText().contains(". Ready"))
			{
				return 1;
			}
			else if(TopBar.getText().contains(". In progress"))
			{
				return 0;
			}
			else return -1;
		}
		
		public int deviceState()
		{
			String DeviceState=driver.findElement(By.xpath("//*[@id=\"navbar6\"]/ul[3]/li[2]")).getText();
			
			if(DeviceState.contains("Connected"))
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		public void pressButton(String Button)
		{
			System.out.println("takeshot=true");
			takeShot(true);
			//driver.findElement(By.xpath(configButton)).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(ByAngular.buttonText(Button)))).click();//submit()
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println("takeshot=true");
//			takeShot(true);
			System.out.println("done");
			wait.until(ExpectedConditions.elementToBeClickable(ByAngular.buttonText("X"))).click();//closes popup
			
			String DeviceState=driver.findElement(By.xpath("//*[@id=\"navbar6\"]/ul[3]/li[2]")).getText();
			
			if(!(DeviceState.contains("Connected")))
			{
				int timer=0;
				while(!(DeviceState.contains("Connected")))
				{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".glassc-conn"))).click();//it will click on connect at top bar
					//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					wait.until(ExpectedConditions.elementToBeClickable(ByAngular.buttonText("X"))).click();
					
					DeviceState=driver.findElement(By.xpath("//*[@id=\"navbar6\"]/ul[3]/li[2]")).getText();
					timer++;
					if(timer>=40)
					{
						Assert.assertFalse(true,"System is busy for more than 2 min");
					}
				}
				
				driver.findElement(ByAngular.buttonText(Button)).click();//sub
				try {
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("toast-container"), "YES"));//wait until popup window shows yes and cancel
					driver.findElement(By.xpath(".//button[@ng-click=\"sendPriority()\" and @class=\"btn btn-success\"]")).click();//YES is pressed from the popup appears after pressing discharge button
					
					//wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(ByAngular.buttonText("YES")))).click();
					}catch(Exception e)
					{
						throw new RuntimeException("after pressing submit button Yes confiramtion button didnt click");
					}
			}
			takeShot(false);
		}


		public void LandingPage(int pro)
		{
			
					try {
					wait.until(ExpectedConditions.elementToBeClickable(ByAngular.repeater("form in Forms")));
					}catch(Exception e)
					{
						throw new RuntimeException("waited 60sec in home page for rvm list to load",e);
					}
					List<WebElement> projects=driver.findElements(By.className("material-icons"));
					projects.get(pro).click();
					ngDriver.waitForAngularRequestsToFinish();
					
					//ISL81601-US011REFZ- Solar Battery Charger Page
					//Assert.assertEquals(driver.getTitle(), SBCEvmLanPage,"ISL81601-US011REFZ- Solar Battery Charger Page is not loaded");
		}
		
		public Object[][] dataProvider(String fileName,int sheetNumber) throws IOException
		{
			String path="../com.tenxertech.autoTesting/TestingData/"+fileName+".xlsx";
			File ConfigData=new File(path);
			FileInputStream fis=new FileInputStream(ConfigData);
			XSSFWorkbook workbook=new XSSFWorkbook(fis);
			XSSFSheet FirstSheet= workbook.getSheetAt(sheetNumber);
			
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
