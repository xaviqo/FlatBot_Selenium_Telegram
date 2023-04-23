package tech.xavi.flatbot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import tech.xavi.flatbot.dto.FilteredAds;
import tech.xavi.flatbot.entity.Ad;
import tech.xavi.flatbot.entity.PropertyType;
import tech.xavi.flatbot.repository.AdRepository;
import tech.xavi.flatbot.util.IdGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class AdService {

    private final AdRepository adRepository;

    public void saveNewAds(Set<Ad> newAds){
        adRepository.saveAll(newAds);
    }

    public Set<Ad> createAds(List<WebElement> allElementAds){
        Set<Ad> allAds = new HashSet<>();
        allElementAds.forEach( ad -> {
            String adName = ad.findElement(By.cssSelector("a.d-ellipsis")).getAttribute("title");
            String adId = IdGenerator.generateHash(adName);
            String roomsInStr;
            try {
                roomsInStr = ad.findElement(By.className("icon-room")).findElement(By.tagName("span")).getText();
            } catch (NoSuchElementException e) {
                roomsInStr = null;
            }
            allAds.add(
                    Ad.builder()
                            .id(adId)
                            .name(adName)
                            .description(ad.findElement(By.cssSelector("p.description")).getText())
                            .type(PropertyType.findPropertyType(adName))
                            .link(ad.findElement(By.cssSelector("a.d-ellipsis")).getAttribute("href"))
                            .price(getOnlyDigits(ad.findElement(By.cssSelector("span.price")).getText()))
                            .squareMeters(getOnlyDigits(ad.findElement(By.className("icon-meter")).findElement(By.tagName("span")).getText()))
                            .rooms((roomsInStr != null) ? getOnlyDigits(roomsInStr) : 0)
                            .lastPriceDrop(0)
                            .totalPriceDrop(0)
                            .sent(false)
                            .build()
            );
        });
        return allAds;
    }

    public FilteredAds filterAds(Set<Ad> allAds){
        FilteredAds filteredAds = new FilteredAds();
        allAds.forEach( ad -> {
            if (adRepository.isPresentById(ad.getId())){
                int oldPrice = adRepository.findPriceById(ad.getId());
                if (oldPrice != ad.getPrice()) {
                    Ad oldAd = adRepository.findById(ad.getId()).get();
                    ad.setLastPriceDrop(oldPrice-ad.getPrice());
                    ad.setTotalPriceDrop((oldPrice+oldAd.getTotalPriceDrop())-ad.getPrice());
                    filteredAds.addAdjustedPrice(ad);
                } else {
                    filteredAds.addOld(ad);
                }
            } else {
                filteredAds.addNew(ad);
            }
        });
        return filteredAds;
    }

    private int getOnlyDigits(String str) throws NumberFormatException {
        if (str.contains(".")){
            str = str.replace(".","");
        }
        try {
            return Integer.parseInt(str.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            log.error("No se pudo analizar la cadena como número entero: " + str);
            e.getStackTrace();
        }
        return 0;
    }

}
