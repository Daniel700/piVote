package database;

import java.util.Arrays;
import java.util.List;

/**
 * This class is needed because of internationalization of the application.
 * These variables will be used to save the same values for every language in the remote DB.
 * Created by Daniel on 25.08.2015.
 */
public abstract class FilterOptions {

    /**
     * The items in this array have to be in the same order as in strings.xml and the values must match with the corresponding string array
     */
    public final static List<String> languages = Arrays.asList("All","English", "Arabic", "Bulgarian", "Catalan", "Chinese", "Croatian",
            "Czech", "Danish", "Dutch", "Finnish", "French", "German", "Greek", "Hebrew", "Hindi", "Hungarian", "Indonesian",
            "Italian", "Japanese", "Korean", "Latvian", "Lithuanian", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian",
            "Serbian", "Slovakian", "Slovenian", "Spanish", "Swedish", "Tagalog", "Thai", "Turkish", "Ukrainian", "Vietnamese");


    /**
     * The items in this array have to be in the same order as in strings.xml and the values must match with the corresponding string array
     */
    public final static List<String> categories = Arrays.asList("All", "Economy/Finance", "Fashion/Beauty", "Food", "Health", "Miscellaneous",
            "Movies/TV", "Music", "Politics", "Society", "Sports", "Technology/Science", "World/Environment");


}
