package tech.xavi.flatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.xavi.flatbot.dto.TelegramAlert;
import tech.xavi.flatbot.entity.Ad;
import tech.xavi.flatbot.repository.AdRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:flatbot.properties")
public class TelegramAlertService extends TelegramLongPollingBot {

    @Value("${tech.xavi.flatbot.telegram.token}")
    private String TOKEN;
    @Value("${tech.xavi.flatbot.telegram.bot-name}")
    private String NAME;
    @Value("${tech.xavi.flatbot.telegram.chat-id}")
    private String CHAT_ID = "";
    @Value("${tech.xavi.flatbot.telegram.message.new-add}")
    private String NEW_AD_MSG;
    @Value("${tech.xavi.flatbot.telegram.message.adjp-add}")
    private String ADJP_AD_MSG;
    @Autowired
    private AdRepository adRepository;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

    public void sendAds(Set<Ad> newAds, Set<Ad> adjustedPriceAds) throws TelegramApiException, InterruptedException {

        if (newAds.size() > 0) {
            final String newAdsIntroMessage = "Found a total of "+newAds.size()+" new ads ("+ LocalDateTime.now().format(dtf) + ")";
            execute(new TelegramAlert(CHAT_ID,newAdsIntroMessage));
            log.info(newAdsIntroMessage);
            for (Ad ad : newAds) {
                execute(new TelegramAlert(CHAT_ID,ad.getNewAdTelegramMessage(NEW_AD_MSG)));
                ad.setSent(true);
                adRepository.save(ad);
                Thread.sleep(TimeUnit.SECONDS.toMillis(4));
            }
        } else {
            final String noAdsMessage = "No new ads were found ("+ LocalDateTime.now().format(dtf) + ")";
            log.info(noAdsMessage);
            execute(new TelegramAlert(CHAT_ID,noAdsMessage));
        }

        if (adjustedPriceAds.size() > 0) {
            final String adjPriceIntroMessage = "Found a total of "+newAds.size()+" adjusted price ads ("+ LocalDateTime.now().format(dtf) + ")";
            execute(new TelegramAlert(CHAT_ID,adjPriceIntroMessage));
            log.info(adjPriceIntroMessage);
            for (Ad ad : adjustedPriceAds) {
                execute(new TelegramAlert(CHAT_ID,ad.getAdjPriceTelegramMessage(ADJP_AD_MSG)));
                ad.setSent(true);
                adRepository.save(ad);
                Thread.sleep(TimeUnit.SECONDS.toMillis(4));
            }
        } else {
            final String noAdjPriceMessage = "No adjusted price ads were found ("+ LocalDateTime.now().format(dtf) + ")";
            log.info(noAdjPriceMessage);
        }

    }

    public void sendDeletedAds(int totalDeletedAds) throws TelegramApiException {
        if (totalDeletedAds > 0) execute(new TelegramAlert(CHAT_ID,"A total of (" + totalDeletedAds + ") ads have been removed"));
    }

    @Override
    public void onUpdateReceived(Update update) {
    }
    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return NAME;
    }

}
