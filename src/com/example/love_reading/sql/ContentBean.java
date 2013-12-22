package com.example.love_reading.sql;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * ∆’Õ®JavaBean
 *
 */
public class ContentBean implements Serializable {
	
    private String mTitle="";
    private Bitmap mBitmap;
    private String mAuthor="";
    private String mPublisher="";
    private String mPublishDate="";
    private String mISBN="";
    private String mSummary="";
    private String mPrice="";
    private String mPages="";

    public void setTitle(String Title)
    {
        mTitle=Title;
    }
    public void setBitmap(Bitmap bitmap)
    {
        mBitmap=bitmap;
    }
    public void setAuthor(String Author)
    {
        mAuthor=Author;
    }
    public void setISBN(String ISBN)
    {
        mISBN=ISBN;
    }
    public void setPublishDate(String PublishDate)
    {
        mPublishDate=PublishDate;
    }
    public void setPublisher(String Publisher)
    {
        mPublisher=Publisher;
    }
    public void setSummary(String Summary)
    {
        mSummary=Summary;
    }
    public void setPrice(String Price)
    {
        mPrice=Price;
    }
    public void setPages(String Pages)
    {
        mPages=Pages;
    }
    
    public String getTitle()
    {
         return mTitle;
    }
    public Bitmap getBitmap()
    {
        return mBitmap;
    }
    public String getAuthor()
    {
        return mAuthor;
    }

    public String getISBN()
    {
        return mISBN;
    }
    public String getPublishDate()
    {
        return mPublishDate;
    }
    public String getPublisher()
    {
        return mPublisher;
    }
    public String getSummary()
    {
        return mSummary;
    }
    public String getPrice()
    {
        return mPrice;
    }
    public String getPages()
    {
        return mPages;
    }
}
