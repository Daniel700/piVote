package utils;

/**
 * Created by Daniel on 07.08.2015.
 */
public enum Language {

    Arabic("Arabic", "Arabisch"), Bulgaria("Bulgaria", "Bulgarisch"), Catalan("Catalan", "Katalanisch"),
    Chinese("Chinese", "Chinesisch"), Croatian("Croatian", "Kroatisch"), Czech("Czech", "Tschechisch"),
    Danish("Danish", "Dänisch"), Dutch("Dutch", "Niederländisch"), Finnish("Finnish", "Finnisch"),
    French("French", "Französisch"), English("English", "Englisch"), German("German", "Deutsch"),
    Greek("Greek", "Griechisch"), Hebrew("Hebrew", "Hebräisch"), Hindi("Hindi", "Hindi"), Hungarian("Hungarian", "Ungarisch"),
    Indonesian("Indonesian", "Indonesisch"), Italian("Italian", "Italienisch"), Japanese("Japanese", "Japanisch"),
    Korean("Korean", "Koreanisch"), Latvian("Latvian", "Lettisch"), Lithuanian("Lithuanian", "Litauisch"),
    Norwegian("Norwegian", "Norwegisch"), Polish("Polish", "Polnisch"), Portuguese("Portuguese", "Portugiesisch"),
    Romanian("Romanian", "Rumänisch"), Russian("Russian", "Russisch"), Serbian("Serbian", "Serbisch"),
    Slovak("Slovak", "Slowakisch"), Slovenian("Slovenian", "Slowenisch"), Spanish("Spanish", "Spanisch"),
    Swedish("Swedish", "Schwedisch"),Tagalog("Tagalog", "Tagalog"), Thai("Thai", "Thailändisch"),
    Turkish("Turkish", "Türkisch"), Ukrainian("Ukrainian", "Ukrainisch"), Vietnamese("Vietnamese", "Vietnamesisch");


    private String lang1;
    private String lang2;

    private Language(String language1, String language2){
        this.lang1 = language1;
        this.lang2 = language2;
    }

    public String getLanguage(String langCode){

        switch (langCode) {
            case "en":
                return lang1;
            case "de":
                return lang2;
            default:
                return lang1;
        }

    }




}
