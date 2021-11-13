package beast.mateenmehmood.learnurdu;

/**
 * {@link Word} is used to get default translation and the urdu translation
 */
public class Word {

    /**
     * Default Translations (e.g. one, two, three etc.)
     */
    private String mDefaultTranslation;

    /**
     * Urdu Translations (e.g. ایک, دو, تین )
     */
    private String mUrduTranslation;

    /**
     * Image for the ease
     */
    private int mImageResourceId = -1;

    /**
     * Audio resources for urdu translation
     */
    private int mAudioResourceId;

    /**
     * Public constructors of the Word
     */
    public Word(String defaultTranslation, String urduTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mUrduTranslation = urduTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    public Word(String defaultTranslation, String urduTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mUrduTranslation = urduTranslation;
        mAudioResourceId = audioResourceId;
    }

    /**
     * getter for the default translation
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * getter for the urdu translation
     */
    public String getUrduTranslation() {
        return mUrduTranslation;
    }

    /**
     * getter for the image
     * @return the image of the object
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * getter for the audio
     * @return the audio for the object
     */
    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    /**
     * Returns if the object of the class has an image
     * @return true if obj has val
     */
    public boolean hasImage () {
        return mImageResourceId != -1;
    }
}