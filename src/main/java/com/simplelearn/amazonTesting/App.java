package com.simplelearn.amazonTesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;

public class App 
{
    public static void main( String[] args )
    {
    	System.setProperty("webdriver.chrome.driver",
				"C:\\rajesh-data\\Chromedriver_112\\chromedriver.exe");
    	WebDriver driver = new ChromeDriver();
		System.out.println("2. driver object created");
		//driver.manage().window().maximize();
		
		Options manage = driver.manage();
		Window window = manage.window();
		
		window.maximize();
		driver.get("https://www.amazon.in/");
		driver.findElements(By.xpath("//href[@bestsellers='bestsellers']/href/a"));

    }
}
