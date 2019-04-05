package itg8.com.nowzonedesigndemo.audio;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.ConnectionCallback;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.audio.fragment.MeditationAllFragment;
import itg8.com.nowzonedesigndemo.sleep.adapter.ViewPagerSleepAdapter;

public class AudioActivity extends AppCompatActivity {


    private static final String TAG = AudioActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private MediaBrowserCompat mMediaBrowser;


//    @BindView(R.id.SpinView)
//    SpinKitView SpinView;
//    @BindView(R.id.img_play)
//    ImageView imgPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMediaBrowser=new MediaBrowserCompat(this,new ComponentName(this,MediaPlaybackService.class),new ConnectionCallback(){
            @Override
            public void onConnected() {
                try{
                    //Get the token whenever it is connected
                    MediaSessionCompat.Token token=mMediaBrowser.getSessionToken();
                    //THis is give us the access to everything
                    MediaControllerCompat controllerCompat=new MediaControllerCompat(AudioActivity.this,token);
                    //Convenience method to allow you to use MediaControllerCompat.getMediaController() anywhere
                    MediaControllerCompat.setMediaController(AudioActivity.this,controllerCompat);

                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(AudioActivity.class.getSimpleName(),
                            "Error creating controller", e);
                }
            }

            @Override
            public void onConnectionFailed() {
                // The attempt to connect failed completely.
                // Check the ComponentName!
            }

            @Override
            public void onConnectionSuspended() {
                // We were connected, but no longer :-(
            }
        },null);

        mMediaBrowser.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaBrowser.disconnect();
    }
}
