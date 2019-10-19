package com.example.androidtest;

/**
 * A blueprint for the given JSON object
 */
public class FeedItem {
    private int idRank;
    private String nameRank;
    private String iconUrL;
    private String colorText;
    private String colorBackground;

    public FeedItem(int idRank, String nameRank, String iconUrL, String colorText, String colorBackground) {
        this.idRank = idRank;
        this.nameRank = nameRank;
        this.iconUrL = iconUrL;
        this.colorText = colorText;
        this.colorBackground = colorBackground;
    }

    public int getIdRank() {
        return idRank;
    }

    public void setIdRank(int idRank) {
        this.idRank = idRank;
    }

    public String getNameRank() {
        return nameRank;
    }

    public void setNameRank(String nameRank) {
        this.nameRank = nameRank;
    }


    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public String getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    public String getIconUrL() {
        return iconUrL;
    }

    public void setIconUrL(String iconUrL) {
        this.iconUrL = iconUrL;
    }
}
