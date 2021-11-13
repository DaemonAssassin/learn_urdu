package beast.mateenmehmood.learnurdu;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;
    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;
    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private final MediaPlayer.OnCompletionListener mOnCompletionListener = mp -> {
        // Now that the sound file has finished playing, release the media player resources.
        releaseMediaPlayer();
    };
    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //creating instance of AudioManager as Audio Service
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Creating an ArrayList of Word objects
        ArrayList<Word> words = new ArrayList<>();

        //Adding Value to ArrayList
        words.add(new Word("one", "ایک", R.drawable.number_one, R.raw.number1));
        words.add(new Word("two", "دو", R.drawable.number_two, R.raw.number2));
        words.add(new Word("three", "تین", R.drawable.number_three, R.raw.number3));
        words.add(new Word("four", "چار", R.drawable.number_four, R.raw.number4));
        words.add(new Word("five", "پانچ", R.drawable.number_five, R.raw.number5));
        words.add(new Word("six", "چھ", R.drawable.number_six, R.raw.number6));
        words.add(new Word("seven", "سات", R.drawable.number_seven, R.raw.number7));
        words.add(new Word("eight", "آٹھ", R.drawable.number_eight, R.raw.number8));
        words.add(new Word("nine", "نو", R.drawable.number_nine, R.raw.number9));
        words.add(new Word("ten", "دس", R.drawable.number_ten, R.raw.number10));

        //Assigning ListView to their appropriate view
        ListView listView = findViewById(R.id.list);

        //Creating an object of WordAdapter to adapt data and to print it in ListView
        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_numbers);

        //setting on item click listener to every item in the list
        listView.setOnItemClickListener((parent, view, position, id) -> {

            // Release the media player if it currently exists because we are about to
            // play a different sound file
            releaseMediaPlayer();

            // Request audio focus so in order to play the audio file. The app needs to play a
            // short audio file, so we will request audio focus with a short amount of time
            // with AUDIOFOCUS_GAIN_TRANSIENT.
            final int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { //we have focus now
                // Create and setup the MediaPlayer for the audio resource associated
                // with the current word
                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, words.get(position).getAudioResourceId());

                // Start the audio file
                mMediaPlayer.start();

                // Setup a listener on the media player, so that we can stop and release the
                // media player once the sound has finished playing.
                mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            }
        });

        //setting adapter to ListView
        listView.setAdapter(wordAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //when the activity is stopped, release the media player resources bcz we won't
        //be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}