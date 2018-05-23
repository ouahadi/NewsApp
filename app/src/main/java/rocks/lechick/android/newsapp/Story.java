package rocks.lechick.android.newsapp;

/**
 * Created by alek on 21/05/2018.
 */

public class Story {

    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;
    private String mSummary;
    private String mUrl;

    public Story(String vTitle, String vSection, String vDate, String vSummary, String vUrl){
        mTitle = vTitle;
        mSection = vSection;
        mDate = vDate;
        mSummary = vSummary;
        mUrl = vUrl;
    }
    /*
    **Get the title of the article
     */
    public String getmTitle(){return mTitle;}
    /*
    **Get the section name of the article
     */
    public String getmSection(){return mSection;}
    /*
    **Get the date of the publication of the article
    */
    public String getmDate(){return mDate;}
    /*
    **Get the summary text
     */
    public String getmSummary(){return mSummary;}
    /*
    **Get the URL of the original article
     */
    public String getmUrl(){return mUrl;}

    public Story(String vTitle, String vSection, String vAuthor, String vDate, String vSummary, String vUrl){
        mAuthor = vAuthor;
        mTitle = vTitle;
        mSection = vSection;
        mDate = vDate;
        mSummary = vSummary;
        mUrl = vUrl;
    }

    /*
    Get the author of the article
     */
    public String getmAuthor(){return mAuthor;}

    public Story(String vTitle, String vSection, String vSummary, String vUrl){
        mTitle = vTitle;
        mSection = vSection;
        mSummary = vSummary;
        mUrl = vUrl;
    }

}
