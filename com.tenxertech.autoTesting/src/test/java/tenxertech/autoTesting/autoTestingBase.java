package tenxertech.autoTesting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
	public static String EvmSelectingPage="Tenxer - Login";
	public HashMap<String,Float>  SystemData;//to store system data String is for key float is for value
	
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
		
		
		ngDriver.waitForAngularRequestsToFinish();
		wait.until(ExpectedConditions.titleIs(EvmSelectingPage));

	}
	
	public void destroy()
	{
		driver.close();
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
	public void destroy(ITestResult result,String path)
	{
		

		if(ITestResult.FAILURE == result.getStatus())
		{
			screenshot(path);
		}
	
		driver.close();
	}
	
	
	
	public void closePopUp() throws InterruptedException
	 {
		//Switch to Eva chat bot
				try {
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
				}catch(Exception e)
				{
					throw new RuntimeException("waited 60sec to switch to boat frame",e);
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
				Thread.sleep(3000);
				driver.findElement(By.xpath(EvaPath)).click();
				
				//switch to parrent frame
				driver.switchTo().parentFrame();
				
				//input configure value
				List<WebElement> temp=driver.findElements(ByAngular.model("tnxmodel"));
						
						
						
				if(!(driver.findElements(By.xpath("//button[@class='wmClose notop']")).size() >0))
				{
					temp.get(0).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='wmClose notop']")));
					//Minimizes user guide pop up
					driver.findElement(By.xpath("//button[@class='wmClose notop']")).click();
				}
				else
				{
					try {
						//Waits Till User guide close button is available
						wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='wmClose notop']")));
						//Minimizes user guide pop up
						driver.findElement(By.xpath("//button[@class='wmClose notop']")).click();
						
						}catch(Exception e)
						{
							throw new RuntimeException("waited 60sec to load user guide popup",e);
						}
				}
				
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
		
		
		public void pressButton(String Button)
		{
			int icounter=0;
			//driver.findElement(By.xpath(configButton)).click();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(ByAngular.buttonText(Button)))).click();//submit()
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			wait.until(ExpectedConditions.elementToBeClickable(ByAngular.buttonText("X"))).click();//closes popup
			
			String DeviceState=driver.findElement(By.xpath("//*[@id=\"navbar6\"]/ul[3]/li[2]")).getText();
			
			if(!(DeviceState.contains("Connected")))
			{
				while(!(DeviceState.contains("Connected")))
				{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".glassc-conn"))).click();//it will click on connect at top bar
					System.out.println("\n=>con");
					//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					wait.until(ExpectedConditions.elementToBeClickable(ByAngular.buttonText("X"))).click();
					System.out.println("\n=>x");
//					if(driver.findElement(ByAngular.buttonText("X")).isDisplayed())
//					{
//						
//						driver.findElement(ByAngular.buttonText("X")).click();
//						
//					}
//					

					//this will close the warnig popup
					
//					System.out.println("\n=>"+DeviceState+"|");
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					
					DeviceState=driver.findElement(By.xpath("//*[@id=\"navbar6\"]/ul[3]/li[2]")).getText();
				}
				
				driver.findElement(ByAngular.buttonText(Button)).click();//sub
				System.out.println("sub");
				try {
					System.out.println("BYes");
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("toast-container"), "YES"));//wait until popup window shows yes and cancel
					driver.findElement(By.xpath(".//button[@ng-click=\"sendPriority()\" and @class=\"btn btn-success\"]")).click();//YES is pressed from the popup appears after pressing discharge button
					
					//wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(ByAngular.buttonText("YES")))).click();
					System.out.println("Ayes");
					}catch(Exception e)
					{
						throw new RuntimeException("Yes button didnt click");
					}
				
				System.out.println("endif");
			}
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
}
