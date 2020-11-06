package tenxertech.autoTesting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.paulhammant.ngwebdriver.ByAngular;

public class SolarBatteryChargerBase extends autoTestingBase{
	
	public static String SBCEvmLanPage="Tenxer - ISL81601-US011REFZ- Solar Battery Charger";
	public String[] console;
	public HashMap<String,Float>  SystemData;
	public String inputvoc,inputioc,inputirr,inputtemp;//stores input parameters and uses to name the screen shot
	public String submitButton="CONFIGURE";
	public int config=0;
	
	
		//Returns 1 for MPPT On ,0 for MPPT OFF and -1 For Empty
		public int MPPTStatus()
		{
			String MPPT=driver.findElement(By.id("tab_12_21_41")).findElement(By.xpath(".//div[@ng-if=\"value.label\" and @class=\"led-label ng-binding ng-scope\" and @ng-bind-html=\"value.label | newlines\"]")).getText();
			if(MPPT.equals("MPPT ON"))
			{
				return 1;
			}
			else if(MPPT.equals("MPPT OFF"))
			{
				return 0;
			}
			else {
				return -1;
			}
		}
		
		

		//returns 1 if battery charging is blinking and 0 if its not blinking
		public int BatteryStatus()
		{
			String ExpectedBlink="led blinking";
			String ActualBlink="";
			List<WebElement> batterystats=driver.findElements(By.xpath(".//div[@ng-class=\"pos\" and @class=\"led-container d-flex ng-scope right\"]"));
			String temp="";
			for(WebElement w:batterystats)
			{
				temp=w.getText();
				if(temp.equals("Battery Charging"))
				{
					//".//div[@ng-class=\"pos\"]/div[@ng-class=\"value.position\"]/div[2]"
				ActualBlink=w.findElement(By.xpath(".//div[@ng-class=\"value.position\"]/div[2]")).getAttribute("class");
				}
			}
			
			if(ExpectedBlink.equals(ActualBlink))
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		

		
		//returns 1 if Discharge status is ON ,0 if Discharge status is OFF and -1 for Discharge status is Empty
		public int DischargeStatus()
		{
			//String ActualState="";
			List<WebElement> batterystats=driver.findElements(By.xpath(".//div[@ng-class=\"pos\" and @class=\"led-container d-flex ng-scope right\"]"));
			String temp="";
			int i=-1;
			for(WebElement w:batterystats)
			{
				temp=w.getText();
				if(temp.equals("ON"))
				{
					i=1;
					break;
					//".//div[@ng-class=\"pos\"]/div[@ng-class=\"value.position\"]/div[2]"
					//ActualState=w.findElement(By.xpath(".//div[@ng-class=\"value.position\"]/div[2]")).getAttribute("class");
				}
				else if(temp.equals("OFF"))
				{
					i=0;
					break;
				}
			}
			return i;
		}
		
		

		 public void PressConfigButton()
		 {
			 
			//press the configure button--------------------------------------------------------------
			//String configButton=".//button[@ng-class=\"getComToArr(data.class)\" and @ng-click=\"submitAll($event,data.allattrib)\" and @class=\"btn btn-primary btn-element  fat-btn\"]";
			
			super.pressButton(submitButton);
					
		 }
		 
		 

		 public void configurButtonCheck() throws InterruptedException
		 {
			 
			
					try {
					//condition 1:waits till top right status bar shows Ready
						super.takeShot(true);
					wait.until(ExpectedConditions.textToBe(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]"), ". Ready"));
					super.takeShot(false);
					}catch(Exception e)
					{
						throw new RuntimeException("After configure waited 60sec for top bar to show Ready",e);
					}
					
					try {
					//condition2: waits till system status is visible in page
						super.takeShot(true);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ByAngular.repeater("value in outputTrans track by $index")));
					super.takeShot(false);
					}catch(Exception e)
					{
						throw new RuntimeException("After configure waited 60sec for system status to show values",e);
					}
					//print Rounded values of System status
					
					SystemData=SystemStatus();
					//key=Vin,Iin,Power_in,Vout,Iout,Power_out,Efficiency,[if discharge is on = Battery V,Battery I]
					String[] key= {"Vin","Iin","Power_in","Vout","Iout","Power_out","Efficiency"};
					
					while(SystemData.get(key[0])<0)
					{
						Thread.sleep(5000);
						SystemData.clear();
						SystemData=SystemStatus();
					}
					for(int i=0;i<key.length;i++)
					{
						System.out.println("====================\n"+key[i]+"="+SystemData.get(key[i]));
					}
					
					
					try {
					//Console data is displayed
						super.takeShot(true);
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByAngular.repeater("op in outputTrans track by $index")));
					super.takeShot(false);
					}catch(Exception e)
					{
						throw new RuntimeException("After configure waited 60sec for console data to be displayed",e);	
					}
					console=Console();
					//List<String> console=Arrays.asList(Console());
					
					for(String i:console)
					{
						System.out.println("----------------------------------------\n"+i);
					}
					
					//condition 3: Console should show "Solar Panel Configuration Complete"
					Assert.assertEquals(console[console.length-3], "Solar Panel Configuration Complete","During Configuring Inputs Console status");
					
					//condition 4:checking Battery status blinking or not
					if(!(BatteryStatus()==1))
					{
						Assert.assertFalse(true, "After configuration Battery Charging status is not blinking");
					}
					
					//condition 5:MPPT Status is OFF
					if(!(MPPTStatus()==0))
					{
						Assert.assertFalse(true,"After Configuration MPPT Status Should be off |");
					}
					//End of configure button----------------------------------------------------------------------------------------------------------
					
		 }
		 

		 public void MPPTOn() throws InterruptedException
		 {
			//Condition 1
					try {
					//waits till top right status bar shows Ready
						super.takeShot(true);
					wait.until(ExpectedConditions.textToBe(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]"), ". Ready"));
					super.takeShot(false);
					}catch(Exception e)
					{
						throw new RuntimeException("after MPPT On button is pressed waited 60sec for top bar to show Ready");		
					}
					
					
					//Condition 2
					//MPPT Button Status
					int mpptTimer=1;
					while(MPPTStatus()!=1)
					{
						super.takeShot(true);
						Thread.sleep(1000);
						mpptTimer++;
						if(mpptTimer==60)
						{
							Assert.assertFalse(true,"After MPPT On button pressed waited 60sec to show MPPT ON");
						}
						super.takeShot(false);
					}
					
					
					//Condition 3
					//MPPT Status in console
					String ExpectedConsoleMPPTStatus="MPPT is ON";
					try {
						super.takeShot(true);
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ByAngular.repeater("op in outputTrans track by $index")));
					super.takeShot(false);
					}catch(Exception e)
					{
						throw new RuntimeException("After MPPT On button is pressed waited 60sec for console to refresh the content ");
					}
					console=Console();
					Assert.assertEquals(console[console.length-1],ExpectedConsoleMPPTStatus,"After MPPT button is ON Console status ");//Checking is Console Showing MPPT is On
					
					//condition 4:checking Battery status blinking or not
					if(!(BatteryStatus()==1))
					{
						Assert.assertFalse(true,"After MPPT On button pressed Battery Charging should be blinking");
					}
		 }
		 

//checking Discharge process. Returns 1 if battery voltage is more than 11. 
//If battery voltage is less than 11 Discharge off will be automatic 
//if voltage is more than 11 then click Discharge OFF to stop discharge	 
		 public int dischargeBatteryOn() throws InterruptedException
		 {
			//load System data
				
					//key=Vin,Iin,Power_in,Vout,Iout,Power_out,Efficiency,[if discharge is on = Battery V,Battery I]
					String[] DischargeBatterykey= {"Vin","Iin","Power_in","Vout","Iout","Power_out","Efficiency","Battery V","Battery I"};
					
					SystemData=SystemStatus();
					int timer=0;
					while(SystemData.get("Battery V")==null)
					{
						super.takeShot(true);
						Thread.sleep(500);
						
						if(timer==120)
						{
							Assert.assertFalse(true, "After Battery Discharge: Waited 60sec to load new system status data");
						}
						SystemData=SystemStatus();
						timer++;
						super.takeShot(false);
					}
					
					for(int i=0;i<DischargeBatterykey.length;i++)
					{
						System.out.println("====================\n"+DischargeBatterykey[i]+"="+SystemData.get(DischargeBatterykey[i]));
					}
					
					
							
					//condition 3:check top right status bar shows "in progrss"

					String ActualBarStatus=driver.findElement(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]")).getText();
					Assert.assertEquals(ActualBarStatus, ". In progress","Battery Discharge status at top bar");
							
					//Condition 4:expected content console content
					//ExpectedConsoleMPPTStatus="Default Battery Discharge upto 10.1. Please click on the button again to stop Discharge process..";
					console=Console();
					//Assert.assertEquals(console[console.length-1],ExpectedConsoleMPPTStatus,"After Battery Discharge");
					//if(!Arrays.asList(Console()).contains("Default Battery Discharge upto 10.1. Please click on the button again to stop Discharge process"))
					String BStats="Default Battery Discharge upto 10.1. Please click on the button again to stop Discharge process";
					if(!(console[console.length-1].contains(BStats) ||  console[console.length-2].contains(BStats) || console[console.length-3].contains(BStats)))
					{
							Assert.assertFalse(true, "After discharge in console Discharge message is not displyaing |");
					}
					//System.out.println(driver.findElement(By.xpath(".//div[@ng-if=\"value.label\" and @style=\"width:160px\" and @class=\"led-label ng-binding ng-scope\" and @ng-bind-html=\"value.label | newlines\"]")).getText());
					
					
					
					//condition 5:checking Battery status blinking or not
					if(!(BatteryStatus()==0))
					{
						Assert.assertFalse(true, "During Battery Discharge: Battery Charging status is blinking but it should not blink");
					}
					//condition 2:Discharge Status should be ON
					if(!(DischargeStatus()==1))
						{
							Assert.assertFalse(true, "After Battery Discharge: Battery Disharging status is OFF but it should be ON");
						}	
					
					//condition 1:System status
					if(!(SystemData.get("Iin") <1 && SystemData.get("Power_in")<1 && SystemData.get("Iout") <1 && SystemData.get("Power_out")<1))
					{
						Assert.assertFalse(true, "After Battery Discharge Iin,Power_in,Iout,Power_out in System Status is not less than one");
					}
					
					if(SystemData.get("Battery V") >= 11)
					{
						return 1;
					}
					else
					{
						return 0;
					}
		 }
		 public void dischargeBatteryOff() throws InterruptedException
		 {
			 int timer=0;
			 
			//Condition 1: Top bar status should be ready
					try {
					//waits till top right status bar shows Ready 
						super.takeShot(true);
					wait.until(ExpectedConditions.textToBe(By.xpath(".//li[@class=\"nav-item\"]/div[@class=\"nav-link active\"]/span[@class=\"ng-scope\"]"), ". Ready"));
					super.takeShot(false);
					}catch(Exception e)
					{
						throw new RuntimeException("after Discharge OFF button is pressed waited 60sec for top bar to show Ready");		
					}
					
			//condition 3:
					timer=0;
					while(BatteryStatus()!=1)
					{
						Thread.sleep(500);
						if(timer==120)
						{
							Assert.assertFalse(true, "After Discharge OFF waited 60sec for Battery blinking Status to be ON But its not |");
						}
						timer++;
					}
			//condition 4:after discharge MPPT should be on
					timer=0;
					while(MPPTStatus()!=1)
					{
						Thread.sleep(500);
						if(timer==120)
						{
							Assert.assertFalse(true, "After Discharge OFF waited 60sec for MPPT Status to be ON But its not. |");
						}
						timer++;
					}
		//condition 2:
				if(!(DischargeStatus()==0))
				{
					Assert.assertFalse(true, "After Discharge OFF Discharge Status Should be OFF But its not |");
				}

		 }
		 
		 


}
