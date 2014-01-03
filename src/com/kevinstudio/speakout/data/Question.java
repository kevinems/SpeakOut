
package com.kevinstudio.speakout.data;

import com.kevinstudio.speakout.R.string;

import java.util.Date;

public class Question {
    
    public Question(String mContent) {
        this.mType = QuestionType.WORD;
        this.mContent = mContent;
        this.mTranslate = "";
        this.mCommonLevel = CommonLevel.THREE_STARS;
    }
    
    public Question(int mType, String mContent, String mTranslate, int mCommonLevel) {
        super();
        this.mType = mType;
        this.mContent = mContent;
        this.mTranslate = mTranslate;
        this.mCommonLevel = mCommonLevel;
    }

    /**
     * @return the mId
     */
    public long getId() {
        return mId;
    }

    /**
     * @param mId the mId to set
     */
    public void setId(long mId) {
        this.mId = mId;
    }

    /**
     * @return the mType
     */
    public int getType() {
        return mType;
    }

    /**
     * @param mType the mType to set
     */
    public void setType(int mType) {
        this.mType = mType;
    }

    /**
     * @return the mContent
     */
    public String getContent() {
        return mContent;
    }

    /**
     * @param mContent the mContent to set
     */
    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    /**
     * @return the mTranslate
     */
    public String getTranslate() {
        return mTranslate;
    }

    /**
     * @param mTranslate the mTranslate to set
     */
    public void setTranslate(String mTranslate) {
        this.mTranslate = mTranslate;
    }

    /**
     * @return the mCommonLevel
     */
    public int getCommonLevel() {
        return mCommonLevel;
    }

    /**
     * @param mCommonLevel the mCommonLevel to set
     */
    public void setCommonLevel(int mCommonLevel) {
        this.mCommonLevel = mCommonLevel;
    }

    /**
     * @return the mWrongCount
     */
    public int getWrongCount() {
        return mWrongCount;
    }

    /**
     * @param mWrongCount the mWrongCount to set
     */
    public void setWrongCount(int mWrongCount) {
        this.mWrongCount = mWrongCount;
    }

    /**
     * @return the mPractiseCount
     */
    public int getmPractiseCount() {
        return mPractiseCount;
    }

    /**
     * @param mPractiseCount the mPractiseCount to set
     */
    public void setPractiseCount(int mPractiseCount) {
        this.mPractiseCount = mPractiseCount;
    }

    /**
     * @return the mSound
     */
    public int getSound() {
        return mSound;
    }

    /**
     * @param mSound the mSound to set
     */
    public void setSound(int mSound) {
        this.mSound = mSound;
    }

    /**
     * @return the mAddDate
     */
    public Date getAddDate() {
        return mAddDate;
    }

    /**
     * @param mAddDate the mAddDate to set
     */
    public void setAddDate(Date mAddDate) {
        this.mAddDate = mAddDate;
    }

    /**
     * @return the mLastPractiseDate
     */
    public Date getLastPractiseDate() {
        return mLastPractiseDate;
    }

    /**
     * @param mLastPractiseDate the mLastPractiseDate to set
     */
    public void setLastPractiseDate(Date mLastPractiseDate) {
        this.mLastPractiseDate = mLastPractiseDate;
    }

    /**
     * @return the mFavor
     */
    public boolean isFavor() {
        return mFavor;
    }

    /**
     * @param mFavor the mFavor to set
     */
    public void setFavor(boolean mFavor) {
        this.mFavor = mFavor;
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

    // field
    private long mId = 0;

    private int mType = QuestionType.WORD;

    private String mContent = "";

    private String mTranslate = "";

    private int mCommonLevel = CommonLevel.THREE_STARS;
    
    private int mWrongCount = 0;
    
    private int mPractiseCount = 0;
    
    private int mSound = 0;
    
    private Date mAddDate = new Date();

    private Date mLastPractiseDate = new Date();
    
    private boolean mFavor = false;
    
    class QuestionType {
        public final static int WORD = 0;

        public final int SENTENCE = 1;
    };

    class CommonLevel {
        public final static int ONE_STAR = 0;

        public final static int TWO_STARS = 1;

        public final static int THREE_STARS = 2;

        public final static int FOUR_STARS = 3;

        public final static int FIVE_STARS = 4;
    };
}
