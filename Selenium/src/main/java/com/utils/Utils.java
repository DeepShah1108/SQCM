package com.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Utils {
    public static void takeScreenShot(WebDriver driver, String fileName) {

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(srcFile, new File("/Users/deepshah/Desktop/Deep/Selenium/output/" + fileName + "_screenshot.png"));
        } catch (IOException e)
        {
            System.out.println(e.getMessage());

        }
    }

}
