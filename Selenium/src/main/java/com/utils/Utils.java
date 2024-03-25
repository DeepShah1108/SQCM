package com.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.io.File;

public class Utils {
    public static void takeScreenShot(WebDriver driver, String fileName) {

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(srcFile, new File("/Users/deepshah/Desktop/SQCM/Selenium/output/" + fileName + "_screenshot.png"));
        } catch (IOException e)
        {
            System.out.println(e.getMessage());

        }
    }

}
