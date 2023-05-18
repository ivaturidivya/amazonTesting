package com.simplelearn.amazonTesting;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class AmazonTesting {

	Properties pr;

	WebDriver driver;

	static Connection conn;

	static Statement stmt;

	public AmazonTesting() throws Exception {
		FileReader fr = new FileReader(System.getProperty("user.dir") + "\\src\\main\\resources\\testData.properties");
		pr = new Properties();
		pr.load(fr);

		Class.forName("com.mysql.jdbc.Driver");

		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/amazon", "root", "A15gim0T!@");

		System.out.println("Connection is created successfully:");
	}

	public static void main(String[] args) throws Exception {

		AmazonTesting az = new AmazonTesting();

		stmt = (Statement) conn.createStatement();

		az.loadBorwser();

		az.testAmazon();

		System.out.println("topsubmenu END");

	}

	public void loadBorwser() throws InterruptedException {
		System.out.println("welcome to First Program");
		System.setProperty("webdriver.chrome.driver", pr.getProperty("chromdriverpath"));

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(pr.getProperty("amazonSite"));
		Thread.sleep(5000);
	}

	public void takeTheScreenShot(String imageName) throws Exception {
// Convert Web Driver object to Take Screenshot ..
		TakesScreenshot scrshot = ((TakesScreenshot) driver);
// Call getScreeenshots method to create Image File ..
		File SrcFile = scrshot.getScreenshotAs((OutputType.FILE));
// Move Image File to a new Destination ...
		File DesSrcFile = new File(System.getProperty("user.dir") + "/screenshots/" + imageName + ".png");
		FileUtils.copyFile(SrcFile, DesSrcFile);

	}

	public void testAmazon() throws Exception {

		checkAmazonLogo();

		Thread.sleep(5000);

		checkHelloSignInLangugeSelection();

		Thread.sleep(5000);

		readMenus();

		Thread.sleep(5000);

		clickAllMenuOption();

		Thread.sleep(5000);

		checkSearchButtonFunctionality();

		Thread.sleep(5000);

		closeApplication();
	}

	public void closeApplication() {
		driver.quit(); // To Quit the Driver session / Close the Chrome Browser ..
	}

	private void checkSearchButtonFunctionality() throws Exception {
		if (driver.findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]")).isDisplayed()) {
			System.out.println("Able find search input field");
			Select selectObj = new Select(driver.findElement(By.id("searchDropdownBox")));
			selectObj.selectByVisibleText("Electronics");
			Thread.sleep(3000);
			driver.findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]")).sendKeys("iphone 8 128 GB");
			Thread.sleep(3000);
			driver.findElement(By.xpath("//input[@id=\"nav-search-submit-button\"]")).click();
			Thread.sleep(3000);
			takeTheScreenShot(UUID.randomUUID().toString());
			Thread.sleep(3000);
			driver.navigate().back();
		} else {
			System.out.println("Not Able find search input field");
		}
	}

	private void clickAllMenuOption() throws InterruptedException {
		if (driver.findElement(By.xpath("//a[@id=\"nav-hamburger-menu\"]")).isDisplayed()) {
			driver.findElement(By.xpath("//a[@id=\"nav-hamburger-menu\"]")).click();
			System.out.println("Able to Click All");
			Thread.sleep(5000);
			List<WebElement> allElementsHeadings = driver.findElements(
					By.xpath("(//div[@id=\"hmenu-content\"]//ul//li//div)[@class=\"hmenu-item hmenu-title \"]"));
			System.out.println(
					"************************MAIN HEADINGS UNDER ALL START************************************");
			for (WebElement ele : allElementsHeadings) {
				if (ele.getText() != null && !ele.getText().equals("")) {
					System.out.println(ele.getText());
				}
			}
			System.out
					.println("************************MAIN HEADINGS UNDER ALL END************************************");

			driver.findElement(By.xpath("//div[@class=\"nav-sprite hmenu-close-icon\"]")).click();

		} else {
			System.out.println("Not Able to Click All");
		}
	}

	private void checkAmazonLogo() throws Exception {
		String logoExists="No";
		if (driver.findElement(By.xpath("//a[@id=\"nav-logo-sprites\"]")).isDisplayed()) {
			 logoExists ="Yes";
			System.out.println("Amazon logo exists");
		} 
		stmt.executeUpdate("insert into maininfo(actionName,isExists,date_check) values('Amazon Logo','"+logoExists+"',sysdate())");
	}

	private void readMenus() {
		List<WebElement> findElements = driver.findElements(By.xpath("//div[@id=\"nav-xshop\"]//a"));

		HashMap<String, String> linkTextWithLink = new HashMap<String, String>();

		for (int i = 0; i < findElements.size(); i++) {
			String url = findElements.get(i).getAttribute("href");
			String linkText = findElements.get(i).getText();
			if (!linkText.equals("") && linkText != null) {
				linkTextWithLink.put(linkText, url);
			}
		}

		System.out.println("**************TOP MENUS START**********************");
		for (Map.Entry<String, String> links : linkTextWithLink.entrySet()) {
			System.out.println(links.getKey() + " = " + links.getValue());
		}
		System.out.println("**************TOP MENUS END**********************");
	}

	private void checkHelloSignInLangugeSelection() throws InterruptedException, Exception {

		Actions action = new Actions(driver);
		WebElement findElement = driver.findElement(By.xpath("//span[@id=\"nav-link-accountList-nav-line-1\"]"));
		String helloSign="";
		if (findElement.isDisplayed()) {
			System.out.println("Hello, sign in exists");
			action.moveToElement(findElement).build().perform();
			helloSign="Yes";
		}else {
			helloSign="No";
		}
		
		stmt.executeUpdate("insert into maininfo(actionName,isExists,date_check) values('Hello Sign','"+helloSign+"',sysdate())");

		Thread.sleep(5000);

		action = new Actions(driver);
		findElement = driver.findElement(By.xpath("//span[@class=\"icp-nav-link-inner\"]"));
		String lang="No";
		if (findElement.isDisplayed()) {
			System.out.println("Language selection exists");
			action.moveToElement(findElement).build().perform();
			lang="Yes";
		}
		
		stmt.executeUpdate("insert into maininfo(actionName,isExists,date_check) values('Language Selection','"+lang+"',sysdate())");
	}

}