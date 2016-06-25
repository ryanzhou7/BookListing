package com.ryanzhou.company.booklisting.model;

/**
 * Created by ryanzhou on 6/24/16.
 */
public class Book {
    private String[] mAuthorNames;
    private String mTitle;

    public Book(String[] mAuthorNames, String mTitle) {
        this.mAuthorNames = mAuthorNames;
        this.mTitle = mTitle;
    }

    public String[] getmAuthorNames() {
        return mAuthorNames;
    }

    public void setmAuthorNames(String[] mAuthorNames) {
        this.mAuthorNames = mAuthorNames;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String formatAuthorNamesForDisplay() {
        if (mAuthorNames.length == 1)
            return mAuthorNames[0];
        else
            return mAuthorNames[0] + " et al";
    }

}
