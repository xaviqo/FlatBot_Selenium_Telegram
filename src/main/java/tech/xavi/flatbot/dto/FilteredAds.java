package tech.xavi.flatbot.dto;

import lombok.Data;
import tech.xavi.flatbot.entity.Ad;

import java.util.HashSet;
import java.util.Set;

@Data
public class FilteredAds {

    private Set<Ad> oldAds;
    private Set<Ad> newAds;
    private Set<Ad> adjustedPriceAds;

    public FilteredAds() {
        this.oldAds = new HashSet<>();
        this.newAds = new HashSet<>();
        this.adjustedPriceAds = new HashSet<>();
    }

    public void addNew(Ad ad){
        newAds.add(ad);
    }

    public void addAdjustedPrice(Ad ad){
        adjustedPriceAds.add(ad);
    }

    public void addOld(Ad ad){
        oldAds.add(ad);
    }
}
