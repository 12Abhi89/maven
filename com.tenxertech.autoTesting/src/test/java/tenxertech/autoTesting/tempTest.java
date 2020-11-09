package tenxertech.autoTesting;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.ByAngular;

public class tempTest extends autoTestingBase {

	
	@BeforeTest
	public void setup()
	{
		super.setup();
		super.LandingPage(1);
	}
	@Test(priority=1)
	public void Test()
	{
		super.closePopUp("START");
		
		driver.findElement(ByAngular.buttonText("START")).click();
		
		List<String> c=Arrays.asList(super.Console());
		
		while(!(c.contains("Load set")))
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c=Arrays.asList(super.Console());
			System.out.println("\n>"+c.get(c.size()-1));
		}
		System.out.println("===============end Test=====================");
			
	}
	
	@Test(priority=2)
	public void Test2()
	{
		if(!super.liveStream("START"))
			Assert.assertFalse(true,"not streaming");
		System.out.println("===================end Test2==========================");
	}
}
