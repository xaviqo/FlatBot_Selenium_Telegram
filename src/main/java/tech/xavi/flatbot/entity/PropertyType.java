package tech.xavi.flatbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PropertyType {
    PISO("piso"),
    ATICO("atico"),
    CASA("casa"),
    ESTUDIO("estudio"),
    DUPLEX("dúplex"),
    OTROS(null)
    ;
    private String type;

    public static PropertyType findPropertyType(String addName){
        for(PropertyType type : values()){
            if (type.getType() != null){
                if (addName.toLowerCase().contains(type.getType())) return type;
            }
        }
        return OTROS;
    }
}
