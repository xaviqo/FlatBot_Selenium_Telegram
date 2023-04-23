package tech.xavi.realista.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Ad {

    @Id
    @Column(length = 32)
    String id;
    @Column
    String name;
    @Column(length = 999)
    String description;
    @Column @Enumerated(EnumType.STRING)
    PropertyType type;
    @Column
    String link;
    @Column
    int price;
    @Column
    int squareMeters;
    @Column
    int rooms;
    @Column
    int lastPriceDrop;
    @Column
    int totalPriceDrop;
    @Column
    boolean sent;

    public String getNewAdTelegramMessage(String newAdMsg){
        return String.format(
                newAdMsg,
                getName(),
                getPrice(),
                getRooms(),
                getType(),
                getSquareMeters(),
                getDescription(),
                getLink()
        );
    }

    public String getAdjPriceTelegramMessage(String adjMsg){
        return String.format(
                adjMsg,
                getName(),
                getPrice(),
                getRooms(),
                getType(),
                getSquareMeters(),
                getLastPriceDrop(),
                getTotalPriceDrop(),
                getLink()
        );
    }

}
