package tatoc;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Tatoc_test {
	
	WebDriver driver;
	public  Tatoc_test()
	{
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+File.separator+"chromedriver");
        this.driver = new ChromeDriver();
        driver.get("http://10.0.1.86/tatoc");
	}
	@Test
	public void Basic_page_Test()
	{
		driver.findElement(By.linkText("Basic Course")).click();
		String expected_url="http://10.0.1.86/tatoc/basic/grid/gate";
		Assert.assertEquals(expected_url, driver.getCurrentUrl(), "Whoops.....Navigation Error");
	}
	
	@Test (dependsOnMethods = "Basic_page_Test")
	public void green_Box_Test()
	{
	Assert.assertEquals(this.driver.findElement(By.className("greenbox")).isDisplayed(), true);
	this.driver.findElement(By.className("greenbox")).click();
	String expected_url =("http://10.0.1.86/tatoc/basic/frame/dungeon"); 
	Assert.assertEquals(expected_url, driver.getCurrentUrl(), "Whoops.....Navigation Error");
	}
	
	@Test (dependsOnMethods = "green_Box_Test")
	public void frameDungeon_Test()
	{
	driver.switchTo().frame("main");
	Assert.assertEquals(this.driver.findElement(By.id("answer")).isDisplayed(), true); 
	String box1 = driver.findElement(By.id("answer")).getAttribute("class");
	int n=0;
	while (n==0)
	{
	driver.findElement(By.cssSelector("a")).click();
	driver.switchTo().frame("child"); 
	String box2 = driver.findElement(By.id("answer")).getAttribute("class");
	driver.switchTo().parentFrame();
	if (box1.equals(box2)) 
	{
	driver.findElements(By.cssSelector("a")).get(1).click();
	n=1;
	}
	}
	String expected_Url = "http://10.0.1.86/tatoc/basic/drag";
	Assert.assertEquals(expected_Url, driver.getCurrentUrl(), "Whoops.....Navigation Error");
	}
	
	@Test (dependsOnMethods = "frameDungeon_Test")
	public void drag_and_drop_Test()
	{
		WebElement drop =driver.findElement(By.id("dropbox"));
		WebElement drag =driver.findElement(By.id("dragbox"));
		Actions act= new Actions(driver);
		act.dragAndDrop(drag, drop).build().perform();
		driver.findElement(By.linkText("Proceed")).click();
		String expected_url="http://10.0.1.86/tatoc/basic/windows";
		Assert.assertEquals(expected_url, driver.getCurrentUrl(),"Whoops.....Navigation Error ");
	}
	
	@Test (dependsOnMethods="drag_and_drop_Test")
	public void launch_page_Test()
	{
		driver.findElement(By.partialLinkText("Launch")).click();
		ArrayList windowsList= new ArrayList(driver.getWindowHandles());
		String windows1 =(String)windowsList.get(1);
		driver.switchTo().window(windows1);
		driver.findElement(By.id("name")).sendKeys("ankita");
		driver.findElement(By.id("submit")).click();
		String windows2=(String)windowsList.get(0);
		driver.switchTo().window(windows2);
		driver.findElement(By.linkText("Proceed")).click();
		String expected_url="http://10.0.1.86/tatoc/basic/cookie";
		Assert.assertEquals(expected_url, driver.getCurrentUrl(),"Whoops...Navigation Error");
	}
	
	@Test (dependsOnMethods="launch_page_Test")
	public void generate_cookie_Test()
	{
		driver.findElement(By.linkText("Generate Token")).click();
		String value = new String();
		value = driver.findElement(By.id("token")).getText();
		System.out.println(value);
		String orginal_value= value.substring(7);
		System.out.println(orginal_value);
		Cookie cookie= new Cookie("Token", orginal_value);
		driver.manage().addCookie(cookie);
		driver.findElement(By.linkText("Proceed")).click();	
		String expected_url="http://10.0.1.86/tatoc/end";
		Assert.assertEquals(expected_url, driver.getCurrentUrl(),"Whoops...Navigation Error");
	}
}
