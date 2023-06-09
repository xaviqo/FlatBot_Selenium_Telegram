package tech.xavi.flatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import tech.xavi.flatbot.configuration.DriverConfiguration;
import tech.xavi.flatbot.entity.Ad;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:flatbot.properties")
public class ScanService {

    private final AdService adService;

    @Value("${tech.xavi.flatbot.url-yanencontre}")
    private String URL_TO_SCAN;

    public Set<Ad> scanAds(){

        ChromeDriver driver = DriverConfiguration.getDriver();

        if (driver == null){
            log.error("Driver could not be configured, unsuccessful attempt");
            return new HashSet<>();
        }

        driver.get(URL_TO_SCAN);
        driver.manage().window().maximize();

        // pulsar aceptar cookies
        try {
            driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/div[2]/button[2]")).click();
        } catch (NoSuchElementException ignored){
        }

        Set<Ad> scannedAds = new HashSet<>();
        boolean keepScanning;
        int page = 1;
        do {
            scannedAds.addAll(
                    adService.createAds(
                            driver.findElements(By.tagName("article"))
                    )
            );
            try {
                driver.findElement(By.className("icon-arrow-right-2"));
                keepScanning = true;
                page++;
                driver.navigate().to(URL_TO_SCAN+"/pag-"+page);
            } catch (NoSuchElementException e) {
                keepScanning = false;
            }

        } while (keepScanning);

        driver.close();

        return scannedAds;

    }
}
