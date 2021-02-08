package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import home.Adrese;

public class Testiranje {

	WebDriver driver;

	@BeforeClass
	public void createDriver() {
		System.setProperty("webdriver.chrome.driver", "C:\\poi\\chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Test (priority = 1)
	public void testNevalidni() {
		File f = new File("data.xlsx"); // Ucitavanje fajla
		try {
			InputStream inp = new FileInputStream(f); // Citanje iz fajla
			XSSFWorkbook wb = new XSSFWorkbook(inp); // Pretvaranje fajla u odgovarajuci format
			Sheet sheet = wb.getSheetAt(0); // Dohvata sheet
			Row row0 = sheet.getRow(0); // Dohvata red
			Row row1 = sheet.getRow(1);
			String invalidUN = row0.getCell(0).toString(); // Dohvata celiju
			String invalidPass = row0.getCell(1).toString();

			driver.get(Adrese.URL);
			driver.findElement(By.xpath(Adrese.USERNAME_XPATH)).sendKeys(invalidUN);
			driver.findElement(By.xpath(Adrese.PASSWORD_XPATH)).sendKeys(invalidPass);
			driver.findElement(By.xpath(Adrese.LOGIN_XPATH)).click();
			
			String actual = driver.getCurrentUrl();
			String expected = "https://www.saucedemo.com/";
			Assert.assertEquals(actual, expected);
			wb.close();
			
		} catch (IOException e) {
			System.out.println("Nije pronadjen fajl!");
			e.printStackTrace();
		}
		
		
	}
	
	@Test (priority = 2) 
	public void testValidni() {
		
		SoftAssert sa = new SoftAssert();
		File f = new File("data.xlsx"); // Ucitavanje fajla
		try {
			InputStream inp = new FileInputStream(f); // Citanje iz fajla
			XSSFWorkbook wb = new XSSFWorkbook(inp); // Pretvaranje fajla u odgovarajuci format
			Sheet sheet = wb.getSheetAt(0); // Dohvata sheet
			Row row;
			for(int i = 1; i < 4; i++) {
				row = sheet.getRow(i);
				String userName = row.getCell(0).toString();
				String password = row.getCell(1).toString();
				
				driver.get(Adrese.URL);
				driver.findElement(By.xpath(Adrese.USERNAME_XPATH)).sendKeys(userName);
				driver.findElement(By.xpath(Adrese.PASSWORD_XPATH)).sendKeys(password);
				driver.findElement(By.xpath(Adrese.LOGIN_XPATH)).click();
				
				String actual = driver.getCurrentUrl();
				String expected = Adrese.LOGGEDIN_URL;
				sa.assertEquals(actual, expected);
				
				
			}
			wb.close();
			sa.assertAll();

		} catch (IOException e) {
			System.out.println("Nije pronadjen fajl!");
			e.printStackTrace();
		}

	}
	
	@Test (priority = 3)
	public void testiranjeSortiranja() {
		driver.get(Adrese.LOGGEDIN_URL);
		WebElement dropdown = driver.findElement(By.xpath(Adrese.DROPDOWN_XPATH));
		dropdown.sendKeys("p");
		
		Select se = new Select(dropdown);
		List<String> originalList = new ArrayList();
		for(WebElement we: se.getOptions()) {
		originalList.add(we.getText());
		}
		List<String> tempList = originalList;
		Collections.sort(tempList);
		Assert.assertEquals(tempList, originalList);
		
		driver.close();

	}
	
	

}
