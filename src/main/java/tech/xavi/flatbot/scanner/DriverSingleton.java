package tech.xavi.flatbot.scanner;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;

@Slf4j
public class DriverSingleton {

    private static ChromeDriver INSTANCE;

    public static synchronized ChromeDriver getInstance(){
        if (INSTANCE == null) {
            try {
                System.setProperty("webdriver.http.factory", "jdk-http-client");
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver");

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                options.setExperimentalOption("useAutomationExtension", false);
                options.addArguments("--disable-extensions");
                options.addArguments("--incognito");
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");

                ChromeDriver driver = new ChromeDriver(options);
                driver.executeCdpCommand("Network.setUserAgentOverride", Collections.singletonMap("userAgent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.53 Safari/537.36"));
                driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

                INSTANCE = driver;
            } catch (Exception exception){
                log.error("Exception configuring WebDriver");
                exception.getStackTrace();
            }
        }
        return INSTANCE;
    }





}
