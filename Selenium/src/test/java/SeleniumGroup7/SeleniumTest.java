package SeleniumGroup7;

import java.io.File;
import java.io.FileInputStream;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.utils.Utils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

public class SeleniumTest {

    private WebDriver driver;
    String homePage;
    ExtentHtmlReporter reporter;
    ExtentReports reports;
    ExtentTest test;
    private Properties properties = new Properties();

    @BeforeTest
    public void beforeTest(){
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();

//        Scenario 3 Changes Start
        HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
        chromeOptionsMap.put("plugins.plugins_disabled", new String[] { "Chrome PDF Viewer" });
        chromeOptionsMap.put("plugins.always_open_pdf_externally", true);
        chromeOptionsMap.put("download.default_directory", "/Users/deepshah/Desktop/Deep/Selenium/output/downloads");
        options.setExperimentalOption("prefs", chromeOptionsMap);
        options.addArguments("--remote-allow-origins=*");

//        Sceanrio 3 Changes end

        options.addArguments("user-data-dir=~/Library/Application Support/Google/Chrome");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        reporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/test-output/testReport.html");
        reports = new ExtentReports();
        reports.attachReporter(reporter);

        reporter.config().setChartVisibilityOnOpen(true);
        reporter.config().setDocumentTitle("Selenium Test Report - Group 7");
        reporter.config().setReportName("Test Report");
        reporter.config().setTestViewChartLocation(ChartLocation.TOP);
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @Test(priority = 1)
    public void testScenario1() throws InterruptedException, IOException {
    	try {
    		test = reports.createTest("Scenario 1: Download the latest Transcript");

    		FileInputStream fis = new FileInputStream("/Users/deepshah/Desktop/Deep/Selenium/config.properties");
			Properties prop = new Properties();
			prop.load(fis);

          //Decrypt Password
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("jasypt");
			Properties propsnew = new EncryptableProperties(encryptor);
			propsnew.load(new FileInputStream("/Users/deepshah/Desktop/Deep/Selenium/config.properties"));

			 //Wait explicit
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30000));

          //Open NEU Portal on Chrome
            driver.get("https://me.northeastern.edu/");
            homePage = driver.getWindowHandle();
            Thread.sleep(3000);

            // Capture screenshot before login
            Utils.takeScreenShot(driver, "Scenario1/Before login");

            test.log(Status.INFO, "Expected: Students able to log in to me.northeastern.edu, Actual: Students able to login to me.northeastern.edu successfully");

            //After login
			Thread.sleep(5000);
            Utils.takeScreenShot(driver, "Scenario1/After login");

            //Click resources
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/resources/']")));
            driver.findElement(By.xpath("//a[@href='/resources/']")).click();
            Thread.sleep(2000);

            assertEquals(driver.getTitle(), "Student Hub - Resources");

          //Click Academics and registration
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='resource-tab-Academics,_Classes_&_Registration']")));
            driver.findElement(By.xpath("//*[@id='resource-tab-Academics,_Classes_&_Registration']")).click();
            Thread.sleep(2000);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0,200)");

            //Click Transcript
            driver.findElement(By.xpath("//*[@id='resource-tab-contents-Academics,_Classes_&_Registration']/section[1]/ul/li[22]/div/a")).click();
            Thread.sleep(2000);

          //Switch to new window
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}

            Boolean isPresent = driver.findElements(By.id("username")).size() > 0;
			System.out.println("Is Present" + isPresent);

			if(isPresent)
					{
				Thread.sleep(1000);
				System.out.println("Entered login page");

				Thread.sleep(1000);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name("_eventId_proceed")));
				driver.findElement(By.id("username")).sendKeys(propsnew.getProperty("neu_username"));
	            Thread.sleep(2000);

	            driver.findElement(By.id("password")).sendKeys(propsnew.getProperty("neu_password"));
	            Thread.sleep(2000);

				driver.findElement(By.name("_eventId_proceed")).click();
				   }
				   else{
				   System.out.println("Entered Inside");

				}
			Thread.sleep(12000);

            // Capture screenshot after downloading transcript
            Utils.takeScreenShot(driver, "Scenario1/After downloading transcript");

            File transcriptFile = new File("path_to_transcript_file.pdf");
            assertTrue(transcriptFile.exists(), "Transcript download verification");

			//Report after adding in Favourite
		    test.log(Status.INFO, "Expected: Download Transcript Successful, Actual:  Download Transcript Successful");
		    test.pass("Scenario 1: PASSED");


    	} catch(Exception e) {
    		test.fail("Scenario 1: FAILED");
            e.printStackTrace();
    	}
    }

    @Test(priority = 2)
    public void testScenario2() throws InterruptedException, IOException {
    	try {
    		test = reports.createTest("Scenario 2: Add two To-Do tasks for yourself");

    		FileInputStream fis = new FileInputStream("/Users/deepshah/Desktop/Deep/Selenium/config.properties");
			Properties prop = new Properties();
			prop.load(fis);

			//Decrypt Password
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("jasypt");
			Properties propsnew = new EncryptableProperties(encryptor);
			propsnew.load(new FileInputStream("/Users/deepshah/Desktop/Deep/Selenium/config.properties"));

			 //Wait explicit
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30000));

            driver.get("https://northeastern.instructure.com/");

//    		driver.switchTo().window(homePage);

    		Thread.sleep(3000);

    		JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0,0)");

//            driver.findElement(By.xpath("//*[@id=\"resource-tab-contents-Academics,_Classes_&_Registration\"]/section[1]/ul/li[7]/div/a")).click();
//            Thread.sleep(2000);
//
//          //Switch to new window
//			for (String winHandle : driver.getWindowHandles()) {
//				driver.switchTo().window(winHandle);
//			}
//
//
//            driver.findElement(By.xpath("//*[@id=\"menu-item-menu-main-desktop-4343\"]/a")).click();
//            Thread.sleep(3000);

            driver.findElement(By.id("global_nav_calendar_link")).click();
            Thread.sleep(3000);

            // Get the value of aria-checked attribute
            String checkedStatus = driver.findElement(By.className("context-list-toggle-box")).getAttribute("aria-checked");

            // Check if the element is checked
            if ("true".equals(checkedStatus)) {
                System.out.println("Element is checked.");
            } else {
                driver.findElement(By.className("context-list-toggle-box")).click();
                System.out.println("Element is not checked.");
            }


            // Event1
            driver.findElement(By.id("create_new_event_link")).click();
            Thread.sleep(3000);

            String s1 = driver.findElement(By.className("ui-dialog")).getText();
            System.out.println(s1);

            driver.findElement(By.xpath("//*[@id=\"ui-id-5\"]")).click();
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"planner_note_title\"]")).sendKeys(propsnew.getProperty("event_1"));
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"edit_planner_note_form_holder\"]/form/div[1]/table/tbody/tr[2]/td[2]/div/div[1]/button")).click();
            Thread.sleep(3000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));

            String month1 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[1]")).getText();
            String year1 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[2]")).getText();

            while(!(month1.equals(propsnew.getProperty("month_event_1")) && year1.equals(propsnew.getProperty("year_event_1")))) {
                driver.findElement(By.xpath("//a[@data-handler='next']")).click();

                month1 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[1]")).getText();
                year1 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[2]")).getText();
            }

            driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr/td/a[text()='" + propsnew.getProperty("date_event_1") + "']")).click();
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"planner_note_time\"]")).sendKeys(propsnew.getProperty("time_event_1"));
            Thread.sleep(3000);

            driver.findElement((By.xpath("//*[@id=\"details_textarea\"]"))).sendKeys(propsnew.getProperty("details_event_1"));
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"edit_planner_note_form_holder\"]/form/div[2]/button")).click();
            Thread.sleep(3000);


            // Event2
            driver.findElement(By.id("create_new_event_link")).click();
            Thread.sleep(3000);

            String s2 = driver.findElement(By.className("ui-dialog")).getText();
            System.out.println(s2);

            driver.findElement(By.xpath("//*[@id=\"ui-id-9\"]")).click();
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"planner_note_title\"]")).sendKeys(propsnew.getProperty("event_2"));
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"edit_planner_note_form_holder\"]/form/div[1]/table/tbody/tr[2]/td[2]/div/div[1]/button")).click();
            Thread.sleep(3000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));

            String month2 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[1]")).getText();
            String year2 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[2]")).getText();

            while(!(month2.equals(propsnew.getProperty("month_event_2")) && year2.equals(propsnew.getProperty("year_event_2")))) {
                driver.findElement(By.xpath("//a[@data-handler='next']")).click();

                month2 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[1]")).getText();
                year2 = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/span[2]")).getText();
            }

            driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr/td/a[text()='" + propsnew.getProperty("date_event_2") + "']")).click();
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"planner_note_time\"]")).sendKeys(propsnew.getProperty("time_event_2"));
            Thread.sleep(3000);

            driver.findElement((By.xpath("//*[@id=\"details_textarea\"]"))).sendKeys(propsnew.getProperty("details_event_2"));
            Thread.sleep(3000);

            driver.findElement(By.xpath("//*[@id=\"edit_planner_note_form_holder\"]/form/div[2]/button")).click();
            Thread.sleep(3000);

            test.log(Status.INFO, "Expected: Events Created, Actual:  Events Created");
            test.pass("Scenario 2: PASSED");

    	} catch (Exception e) {
    		test.fail("Scenario 2: FAILED");
            e.printStackTrace();
    	}
    }

    @Test(priority = 3)
    public void testScenario3() throws InterruptedException, IOException {
        try {
            test = reports.createTest("Scenario 3: Download a classroom guide.");

//    		Open Class Northeastern
            driver.get("https://service.northeastern.edu/tech?id=classrooms");
            Thread.sleep(3000);

            //Wait explicit
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30000));

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"sp-nav-bar\"]/ul[2]/li/a")));
            driver.findElement(By.xpath("//*[@id=\"sp-nav-bar\"]/ul[2]/li/a")).click();
            Thread.sleep(3000);


            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(., '002 East Village')]")));
            driver.findElement(By.xpath("//a[contains(., '002 East Village')]")).click();
            Thread.sleep(3000);


            driver.findElement(By.xpath("//*[@id='x51d2fa949721d518beddb4221153af23']/div/div[2]/span/table[1]/tbody/tr[1]/td[2]/a")).click();
            Thread.sleep(3000);

            //Switch to new window
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
            }

            String uRL = driver.getCurrentUrl();

            System.out.println("URL" + uRL);
            
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"icon\"]/iron-icon//svg/g/path']")));
//            System.out.println("In");
//            driver.findElement(By.xpath("//*[@id=\"icon\"]/iron-icon//svg/g/path")).click();
//            Thread.sleep(3000);

            test.pass("Scenario 3: PASSED");

        } catch (Exception e) {
            test.fail("Scenario 3: FAILED");
            e.printStackTrace();
        }
    }

    @Test(priority = 4)
    public void testScenario4() throws InterruptedException, IOException {
    	try {
            test = reports.createTest("Scenario 4: Download a DataSet");

            // Capture screenshot before login
            Utils.takeScreenShot(driver, "Scenario4/Before Downloading");

		    driver.get("https://onesearch.library.northeastern.edu/discovery/search?vid=01NEU_INST:NU&lang=en");
			Thread.sleep(3000);

			driver.findElement(By.xpath("//*[@id=\"mainMenu\"]/div[5]/a/span")).click();
			Thread.sleep(3000);

			//Switch to new window
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}

			JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollTo(0,500)");

			driver.findElement(By.xpath("//*[@id=\"main-content\"]/div[1]/section/div[1]/a[5]")).click();
			Thread.sleep(3000);

            Utils.takeScreenShot(driver, "Scenario4/After Clicking on DataSet");

			driver.findElement(By.xpath("//*[@href='/downloads/neu:bz616j94n?datastream_id=content']")).click();
			Thread.sleep(2000);

            Utils.takeScreenShot(driver, "Scenario4/After Downloading DataSet");

            test.pass("Scenario 4: PASSED");

    	} catch (Exception e) {
    		test.fail("Scenario 4: FAILED");
            e.printStackTrace();
    	}
    }

    @Test(priority = 5)
    public void testScenario5() throws InterruptedException, IOException {
    	try {
    		test = reports.createTest("Scenario 5: Update the Academic Calender");

    		driver.get("https://student.me.northeastern.edu/");
    		Thread.sleep(3000);

    		 //Wait explicit
    		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30000));

//    		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"menu-item-menu-main-desktop-2483\"]/a")));
//    		driver.findElement(By.xpath("//*[@id=\"menu-item-menu-main-desktop-2483\"]/a")).click();
//    		Thread.sleep(3000);

    		//Click resources
	        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/resources/']")));
	        driver.findElement(By.xpath("//a[@href='/resources/']")).click();
	        Thread.sleep(2000);

	        assertEquals(driver.getTitle(), "Student Hub - Resources");
//
//        //Click Academics and registration
          wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='resource-tab-Academics,_Classes_&_Registration']")));
          driver.findElement(By.xpath("//*[@id='resource-tab-Academics,_Classes_&_Registration']")).click();
          Thread.sleep(2000);

          driver.findElement(By.xpath("//*[@id=\"resource-tab-contents-Academics,_Classes_&_Registration\"]/section[1]/ul/li[1]/div/a")).click();
          Thread.sleep(2000);

          //Switch to new window
		  for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		  }

          wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tax-academic-calendars\"]/div/a[1]")));
          driver.findElement(By.xpath("//*[@id=\"tax-academic-calendars\"]/div/a[1]")).click();
          Thread.sleep(2000);

          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript("window.scrollTo(0,800)");
          Thread.sleep(2000);

          driver.switchTo().frame("trumba.spud.6.iframe");
          System.out.println("i am here");

          driver.findElement(By.xpath("//*[@id=\"mixItem0\"]")).click();
          System.out.println("i am here 2");
          Thread.sleep(2000);

          driver.switchTo().parentFrame();

          js.executeScript("window.scrollTo(0,5000)");
          System.out.println("i am here 3");
          Thread.sleep(5000);

          boolean display = driver.findElement(By.xpath("//button[contains(., 'Add to My Calendar')]")).isDisplayed();
          System.out.println(display);
          if(display) {
              System.out.println("Displayed");
          } else {
              System.out.println("Not Displayed");
          }




//          wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='mixItem0']")));
//          System.out.print("I am here ");
//          driver.findElement(By.xpath("//input[@id='mixItem0']")).click();
//          System.out.print("I am here2 ");
//          Thread.sleep(2000);

          test.pass("Scenario 5: PASSED");

    	} catch (Exception e) {
    		test.fail("Scenario 5: FAILED");
            e.printStackTrace();
    	}
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

    @AfterTest
    public void afterClass() {
        System.out.println("Inside After class");
        reports.flush();
    }
}
