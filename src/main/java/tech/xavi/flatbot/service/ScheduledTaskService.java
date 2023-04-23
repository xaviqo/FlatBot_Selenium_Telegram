package tech.xavi.flatbot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.xavi.flatbot.dto.FilteredAds;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduledTaskService {

    private final ScanService scanService;
    private final AdService adService;
    private final TelegramAlertService telegramAlertService;

    @Scheduled(fixedRate = 60000)
    public void run(){

        log.info("Scheduled FlatBot Task started");

        FilteredAds filteredAds = adService.filterAds(
                scanService.scanAds()
        );

        adService.saveNewAds(
                filteredAds.getNewAds()
        );

        log.info("Sending alerts via Telegram...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            telegramAlertService.sendAds(
                    filteredAds.getNewAds(),
                    filteredAds.getAdjustedPriceAds()
            );
        } catch (TelegramApiException | InterruptedException e) {
            log.error("Fatal error sending Telegram alerts");
            e.printStackTrace();
        }
        stopWatch.stop();
        long timeElapsed = stopWatch.getLastTaskTimeMillis();
        log.info("Time elapsed in sending alerts execution: " + TimeUnit.MILLISECONDS.toSeconds(timeElapsed) + " sec");

    }
}
