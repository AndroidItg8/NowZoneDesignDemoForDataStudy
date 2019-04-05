package itg8.com.nowzonedesigndemo.audio;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.util.List;

import itg8.com.nowzonedesigndemo.R;

/**
 * Created by swapnilmeshram on 30/11/17.
 */

public class MediaPlaybackService extends MediaBrowserServiceCompat {

    private static final String MY_EMPTY_MEDIA_ROOT_ID = "my_empty_media";
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateStateBuilder;
    private android.support.v4.media.session.MediaSessionCompat.Callback mSessionCallback=new MediaSessionCompat.Callback() {
        @Override
        public void onPrepare() {
            super.onPrepare();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        downloadOrLoadAllFilesFromRaw();
        //Create mediaSessionCompat.
        mMediaSession=new MediaSessionCompat(this,MediaBrowserCompat.class.getSimpleName());
        //Enable callback from media buttons and transportControls
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS|
        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set initial playbackState with ACTION_PLAY, so media buttons can start the player
        mStateStateBuilder=new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY|PlaybackStateCompat.ACTION_PLAY_PAUSE);

        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(mSessionCallback);

        setSessionToken(mMediaSession.getSessionToken());
    }

    private void downloadOrLoadAllFilesFromRaw() {
        int ss = getResources().getIdentifier("anapan",
                "raw", getPackageName());

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
       // return null== no one can connect so we'll return something
        if(allowBrowsing(clientPackageName,clientUid)) {
            return new BrowserRoot(getString(R.string.app_name), null);
        }else {
            return new BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null);
        }
    }

    private boolean allowBrowsing(String clientPackageName, int clientUid) {
        return true;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        //Here we will send media List downloaded(or assets) music files

        result.sendResult(null);
    }


}
