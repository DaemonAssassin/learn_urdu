package beast.mateenmehmood.learnurdu;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhraseActivity extends AppCompatActivity {
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

        //finding appropriate view
        ListView listView = findViewById(R.id.list);

        //creating instance of array list
        ArrayList<Word> words = new ArrayList<>();

        //adding elements in the arraylist
        words.add(new Word("Where are you going?", "آپ کہاں جا رہے ہیں؟", R.raw.phrase1));
        words.add(new Word("What is your name?", "آپ کا نام کیا ہے؟", R.raw.phrase2));
        words.add(new Word("My name is...", "میرا نام ہے...", R.raw.phrase3));
        words.add(new Word("How are you feeling?", "آپ کیسا محسوس کر رہے ہیں؟", R.raw.phrase4));
        words.add(new Word("I’m feeling good.", "میں اچھا محسوس کر رہا ہوں.", R.raw.phrase5));
        words.add(new Word("Are you coming?", "کیا آپ آ رہے ہو؟", R.raw.phrase6));
        words.add(new Word("Yes, I’m coming.", "ہاں ، میں آرہا ہوں۔", R.raw.phrase7));
        words.add(new Word("I’m coming.", "میں آرہا ہوں.", R.raw.phrase8));
        words.add(new Word("Let’s go.", "چلو.", R.raw.phrase9));
        words.add(new Word("Come here.", "ادھر آو.", R.raw.phrase10));

        //creating instance of WordAdapter
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

        //setting on item click listener to every item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT
                final int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { //we have focus now
                    // Create and setup the MediaPlayer for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(PhraseActivity.this, words.get(position).getAudioResourceId());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });

        //setting adapter to list view
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //when the activity is stopped, release the media player resources bcz we won't
        //be playing any more sounds.
        releaseMediaPlayer();
    }

    public void releaseMediaPlayer () {
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