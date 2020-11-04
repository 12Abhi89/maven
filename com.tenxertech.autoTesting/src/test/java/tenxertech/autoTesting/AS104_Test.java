package tenxertech.autoTesting;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;

public class AS104_Test extends autoTestingBase {
	
	protected String Button="START";
	protected int LoadSetPos=0;
	
	@BeforeMethod
	public void setup()
	{
		
		super.setup();
		super.LandingPage(1);
	}
	
	@AfterMethod
	public void destroy()
	{
		super.destroy();
	}
	
	public void Check(String LoadVoltage)
	{
		//wait until top bar shows ready
		while(!(super.TopBarStatus()==1))
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String a=LoadVoltage;
		String[] b=a.split(" ");
		float loadVol=Float.parseFloat(b[0].strip());
		System.out.println("\n>"+LoadVoltage+" :"+loadVol);
	
	
		SystemData=super.SystemStatus();
		String[] key= {"Load current","Battery current","Load voltage","Battery Voltage","Load power"};
		
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
	
	public void InRushCheck(int battery)
	{
		int BatVol[]= {8,7,11,15};
		
		List<String> console=Arrays.asList(super.Console());
		while(!console.contains("Output Power set to 55W"))
		{
			console.clear();
			console=Arrays.asList(super.Console());
		}
		SystemData=super.SystemStatus();
		String[] key= {"Load current","Battery current","Load voltage","Battery Voltage","Load power"};
		
		float ActualLoadVol=SystemData.get(key[3]);
		
		float max=BatVol[battery]+1;
		float min=BatVol[battery]-1;
		float roundedLoadVol=(float) Math.ceil(ActualLoadVol);
		System.out.println("ladvol"+ActualLoadVol);
		
		if(!(roundedLoadVol<=max && roundedLoadVol >=min))
		{
			Assert.assertFalse(true, "For in Rush configuration Battery Voltage in System status is "+ActualLoadVol+" Expected value is near "+ BatVol+" V |");
		}
		
	}
	public void pressStart()
	{
		//String path="//button[@ng-class=\"getComToArr(data.class)\" and @class=\"btn btn-primary btn-element ng-binding fat-btn\" and @title=\"\" and @type=\"submit\"]";
	    super.pressButton(Button);
	}

	@Test
	public void AS104test() throws InterruptedException
	{
		try {
			super.closePopUp();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<WebElement> config=driver.findElements(By.id("home-tab"));//contains 4 config's
		for(WebElement a: config)
		System.out.println("\n->"+a.getText());
		
		List<WebElement> configDropDown=driver.findElements(ByAngular.model("tnxmodel"));//contains dropdown
		Thread.sleep(3000);
		//List<WebElement> start=driver.findElements(By.xpath("//*[contains(text(),'START')]"));//contains 4 start button
		List<WebElement> start=driver.findElements(ByAngular.buttonText(Button));
		
		
		//Config 1
		config.get(0).click();
		
		//config 1 loadVoltage
		configDropDown.get(0).click();
		Select Config1LoadVoltage=new Select(configDropDown.get(0));
		Config1LoadVoltage.selectByVisibleText("20 V");
		
		//config 1 loadCurrent
		
		configDropDown.get(1).click();
		Select Config1LoadCurrent=new Select(configDropDown.get(1));
		Config1LoadCurrent.selectByVisibleText("2.1 A");
		
		//jsDriver.executeScript("arguments[0].click();", start);
		//driver.findElement(ByAngular.buttonText(SubmitButton)).submit();
		
		//wait.until(ExpectedConditions.elementToBeClickable(ByAngular.buttonText("X")));
		
		//System.out.println("\nsfjkj:"+driver.findElement(By.xpath("//div[@class=\"toast toast-warning\" and @ng-click=\"tapToast()\"]")).getText());
		System.out.println("b\n");
		pressStart();
		System.out.println("A\n");
		Check("20 V");
		System.out.println("=====================End Config 1=============================");

		//Config 2
		config.get(1).click();
				
		//config 2 loadVoltage
		configDropDown.get(2).click();
		Select Config2LoadVoltage=new Select(configDropDown.get(2));
		Config2LoadVoltage.selectByVisibleText("24 V");
				
		//config 2 loadCurrent
				
		configDropDown.get(3).click();
		Select Config2LoadCurrent=new Select(configDropDown.get(3));
		Config2LoadCurrent.selectByVisibleText("1.6 A");
		
		//driver.findElement(ByAngular.buttonText(SubmitButton)).clear();
		//driver.findElement(ByAngular.buttonText(SubmitButton)).click();
		//driver.findElement(By.xpath(".//button[@ng-class=\"getComToArr(data.class)\" and class=\"btn btn-primary btn-element ng-binding fat-btn\"]")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(start.get(1))).click();
		
		//pressStart();
		Check("24 V");
		
		System.out.println("=====================End Config 2=============================");

		//Config 3
		config.get(2).click();
		
		
		//config 3 loadVoltage
		configDropDown.get(4).click();
		Select Config3LoadVoltage=new Select(configDropDown.get(4));
		Config3LoadVoltage.selectByVisibleText("32 V");
						
		//config 3 loadCurrent
						
		configDropDown.get(5).click();
		Select Config3LoadCurrent=new Select(configDropDown.get(5));
		Config3LoadCurrent.selectByVisibleText("1.4 A");
		
		//driver.findElement(ByAngular.buttonText(SubmitButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(start.get(2))).click();
//		pressStart();
		Check("32 V");
		System.out.println("=====================End Config 3=============================");
		
		//In Rush
		config.get(3).click();
		
		//config 4 loadCurrent
		
		configDropDown.get(12).click();
		Select InRushDuration=new Select(configDropDown.get(12));
		InRushDuration.selectByVisibleText("10");
		
		//config 4 loadVoltage
		configDropDown.get(6).click();
		Select InRushLoadVoltage=new Select(configDropDown.get(6));
		InRushLoadVoltage.selectByVisibleText("32 V");
						
		//config 4 loadCurrent
						
		configDropDown.get(7).click();
		Select InRushLoadCurrent=new Select(configDropDown.get(7));
		InRushLoadCurrent.selectByVisibleText("55 W");
		
		
		jsDriver.executeScript("arguments[0].click();", configDropDown.get(8));//0 is selected
		
//		jsDriver.executeScript("arguments[0].click();", configDropDown.get(9));//2 is selected
//		
//		jsDriver.executeScript("arguments[0].click();", configDropDown.get(10));//3 is selected
//		
//		jsDriver.executeScript("arguments[0].click();", configDropDown.get(11));//4 is selected
		
		//driver.findElement(ByAngular.buttonText(SubmitButton)).submit();
		wait.until(ExpectedConditions.elementToBeClickable(start.get(3))).click();
		//pressStart();
		InRushCheck(0);
		
		System.out.println("=====================End Config 4=============================");
		
		//Start button is clicked
		//driver.findElement(By.xpath(".//button[@ng-class=\"getComToArr(data.class)\" and class=\"btn btn-primary btn-element ng-binding fat-btn\"]")).click();

		System.out.println("==========================End AS104==========================");
	}

}
