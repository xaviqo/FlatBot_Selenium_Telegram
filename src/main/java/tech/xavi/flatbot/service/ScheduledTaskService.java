package tech.xavi.flatbot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.xavi.flatbot.dto.FilteredAds;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduledTaskService {

    private final ScanService scanService;
    private final AdService adService;
    private final TelegramAlertService telegramAlertService;

    public void run(){

        FilteredAds filteredAds = adService.filterAds(
                scanService.scanAds()
        );

        try {
            telegramAlertService.sendAds(
                    filteredAds.getNewAds(),
                    filteredAds.getAdjustedPriceAds()
            );
        } catch (TelegramApiException | InterruptedException e) {
            log.error("Fatal error sending Telegram alerts");
            e.printStackTrace();
        }

        adService.saveNewAds(
                filteredAds.getNewAds()
        );

    }
}
