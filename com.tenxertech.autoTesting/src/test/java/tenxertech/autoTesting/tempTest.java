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
		//super.setup();
		// super.LandingPage(1);
	}
	@Test(priority=1)
	public void Test()
	{
		
		System.out.println("===============end Test=====================");
			
	}
	
	@Test(priority=2)
	public void Test2()
	{
		System.out.println("===============end Test=====================");
	}
}
