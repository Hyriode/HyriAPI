package fr.hyriode.hyriapi.cosmetics;

public interface IHyriCosmetic {

    String getName();
    ECosmeticType getCosmeticType();

    void setName(String name);
    void setType(ECosmeticType type);
}
