package utils;

/**
 * Created by Daniel on 07.08.2015.
 */
public enum Category {

    Economics("Economics", "Wirtschaft"), Environment("Environment", "Umwelt"),
    Global("Global", "Global"), Movies_TV("Movies/TV", "Filme/TV"), Politics("Politics", "Politik"),
    Science("Science", "Wissenschaft"), Society("Society", "Gesellschaft"),
    Technical("Technical", "Technik");

    private String lang1;
    private String lang2;

    private Category(String language1, String language2){
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
