package tech.xavi.realista.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import tech.xavi.realista.configuration.ScannerConfiguration;
import tech.xavi.realista.entity.Ad;
import tech.xavi.realista.scanner.DriverSingleton;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ScanService {

    private final AdService adService;

    public Set<Ad> scanAds(){

        ChromeDriver driver = DriverSingleton.getInstance();
        String urlToScan = ScannerConfiguration.getUrlToScan();

        driver.get(urlToScan);
        driver.manage().window().maximize();

        // pulsar aceptar cookies
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/div[2]/button[2]")).click();;

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
                driver.navigate().to(urlToScan+"/pag-"+page);
            } catch (NoSuchElementException e) {
                keepScanning = false;
            }

        } while (keepScanning);

        return scannedAds;

    }
}
