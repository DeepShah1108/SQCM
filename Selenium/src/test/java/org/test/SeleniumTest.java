package org.test;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.utils.Utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class SeleniumTest {
    private WebDriver driver;
    String homePage;
    ExtentHtmlReporter reporter;
    ExtentReports reports;
    ExtentTest test;
    private Properties properties = new Properties();


    @BeforeTest
    public void beforeTest() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=~/Library/Application Support/Google/Chrome");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        reporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/testReport.html");
        reports = new ExtentReports();
        reports.attachReporter(reporter);

        reporter.config().setChartVisibilityOnOpen(true);
        reporter.config().setDocumentTitle("Selenium Test Report - Group 7");
        reporter.config().setReportName("Test Report");
        reporter.config().setTestViewChartLocation(ChartLocation.TOP);
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        System.out.println("Finished Before Test");
    }

    @Test(priority = 1)
    public void test1() throws InterruptedException, IOException {
        try{
            test = reports.createTest("Scenario 1: Download the latest Transcript");
            FileInputStream fis = new FileInputStream("/Users/deepshah/Desktop/SQCM/Selenium/TestData.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            XSSFRow cellData = sheet.getRow(sheet.getLastRowNum());
            String passWord = cellData.getCell(1).getStringCellValue();

            FileInputStream fis1 = new FileInputStream("/Users/deepshah/Desktop/SQCM/Selenium/TestData.xlsx");
            Properties prop = new Properties();
            prop.load(fis1);
            System.out.println("Finished reading the file");

            //Decrypt Password
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword("jasypt");
            Properties propsnew = new EncryptableProperties(encryptor);
            propsnew.load(new FileInputStream("/Users/deepshah/Desktop/SQCM/Selenium/config.properties"));

            //Wait explicit
            Duration customDuration = Duration.ofMillis(30000);
            WebDriverWait wait = new WebDriverWait(driver, customDuration);

            //Open NEU Portal on Chrome
            driver.get("https://me.northeastern.edu");
            homePage = driver.getWindowHandle();
            Thread.sleep(3000);

            //Find usernam`
//            driver.findElement(By.name("loginfmt")).sendKeys(userName);
//            driver.findElement(By.id("idSIButton9")).click();
//            driver.findElement(By.name("passwd")).sendKeys(passWord);
//            Thread.sleep(15000);
//            driver.findElement(By.xpath("//input[@type='submit']")).click();
//            Thread.sleep(3000);

            driver.findElement(By.xpath("//a[@href='/resources/']")).click();
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id='resource-tab-Academics,_Classes_&_Registration']")).click();
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id='resource-tab-contents-Academics,_Classes_&_Registration']/section[1]/ul/li[22]/div/a")).click();
            Thread.sleep(3000);




        } catch (Exception e) {
            test.fail("Scenario 1: FAILED");
            e.printStackTrace();
        }
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
        //reports.flush();
    }

    @AfterTest
    public void afterClass() {
        System.out.println("Inside After class");
        reports.flush();
    }
}
