
package com.kevinstudio.speakout.data;

import java.util.Date;

public class Question {
    
    // field
    private long id = 0;

    private String content = "";

    private int commonLevel = CommonLevel.THREE_STARS;
    
    private int wrongCount = 0;
    
    private int practiseCount = 0;
    
    private int sound = 0;
    
    private Date createdDate = new Date();

    private Date lastPractiseDate = new Date();
    
    private boolean favor = false;
    
    public Question(String aContent) {
        content = aContent;
    }
    
    public Question(String aContent, int aCommonLevel) {
        content = aContent;
        commonLevel = aCommonLevel;
    }

    /**
     * @return the mId
     */
    public long getId() {
        return id;
    }

    /**
     * @param mId the mId to set
     */
    public void setId(long aId) {
        this.id = aId;
    }

    /**
     * @return the mContent
     */
    public String getContent() {
        return content;
    }

    /**
     * @param mContent the mContent to set
     */
    public void setContent(String aContent) {
        content = aContent;
    }

    /**
     * @return the mCommonLevel
     */
    public int getCommonLevel() {
        return commonLevel;
    }

    /**
     * @param mCommonLevel the mCommonLevel to set
     */
    public void setCommonLevel(int aCommonLevel) {
        commonLevel = aCommonLevel;
    }

    /**
     * @return the mWrongCount
     */
    public int getWrongCount() {
        return wrongCount;
    }

    /**
     * @param mWrongCount the mWrongCount to set
     */
    public void setWrongCount(int aWrongCount) {
        wrongCount = aWrongCount;
    }

    /**
     * @return the mPractiseCount
     */
    public int getmPractiseCount() {
        return practiseCount;
    }

    /**
     * @param mPractiseCount the mPractiseCount to set
     */
    public void setPractiseCount(int aPractiseCount) {
        practiseCount = aPractiseCount;
    }

    /**
     * @return the mSound
     */
    public int getSound() {
        return sound;
    }

    /**
     * @param mSound the mSound to set
     */
    public void setSound(int aSound) {
        this.sound = aSound;
    }

    /**
     * @return the mAddDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param mAddDate the mAddDate to set
     */
    public void setCreatedDate(Date aAddDate) {
        this.createdDate = aAddDate;
    }

    /**
     * @return the mLastPractiseDate
     */
    public Date getLastPractiseDate() {
        return lastPractiseDate;
    }

    /**
     * @param mLastPractiseDate the mLastPractiseDate to set
     */
    public void setLastPractiseDate(Date aLastPractiseDate) {
        this.lastPractiseDate = aLastPractiseDate;
    }

    /**
     * @return the mFavor
     */
    public boolean isFavor() {
        return favor;
    }

    /**
     * @param mFavor the mFavor to set
     */
    public void setFavor(boolean aFavor) {
        this.favor = aFavor;
    }
    
    public static boolean isContentValid(String aContent) {
        boolean result = false;
        
        if (!aContent.isEmpty()) {
            result = true;
        }
        return result;
    }
    
    public static String getValidContent(String aString) {
        String string  = aString;
        string.replaceAll("\\p{P}", "").trim();
        return string;
    }
}
