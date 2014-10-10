package br.com.idbgames.chickachickaboomboomone;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayMusicVids {
	
	protected SoundPool sound = null;
	protected MediaPlayer player = null;
	protected VideoView mVideoView = null;
	protected Map<String, Integer> aMap = new HashMap<String, Integer>();
	protected Activity activityContext;	
	
	public PlayMusicVids(Activity aContext)
	{
		activityContext = aContext;
	}
	
	public PlayMusicVids(Activity aContext, String[] nameSoundsArray, String directory, String type)
	{
		activityContext = aContext;
		
		LoadSounds(nameSoundsArray, directory, type);
	}
	
	public void LoadSounds(String[] nameSoundsArray, String directory, String type) {	
		
		try {										 
			
		    aMap.clear();
		    
	    	sound = null;
	    	sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);		    	    	
	    	
	    	// Load all letters
		    for (int i = 0; i <= nameSoundsArray.length; i++ ) {
		    	String soundName = nameSoundsArray[i];
		    	//String file = "sounds/gp/" + soundName + ".3gp";
		    	String file = directory+"/"+soundName+"."+type;
    			AssetFileDescriptor soundFile = activityContext.getAssets().openFd(file);	    		    	    		    	    
    			aMap.put(soundName, sound.load(soundFile , 1));
    			soundFile.close();
    		}		    		   
		    
		} catch(Exception e) {
			Log.i("ERROR", e.getMessage());
		}
	}
	
	public void playMusic(String directory, String soundName, String type) {
		playMusic(directory+"/"+soundName+"."+type);
	}
	
	public void playMusic(String soundNameWithDirectoryAndType) {
		
	    try {
	    	
	    	this.stopAll();
	    	
	    	//String file = "sounds/gp/" + soundName + ".3gp";
	    	String file = soundNameWithDirectoryAndType;
	    	
	    	AssetFileDescriptor afd = activityContext.getAssets().openFd(file);
	    	player = new MediaPlayer();	    	
		    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
		    player.prepare();
		    player.start();

	    } 
	    catch (Exception e) 
	    {
	    	Log.i("ERROR", e.getMessage());
	    }
	}
	
	public void playBeep(String soundName) {
		
	    try {
	    	
	    	this.stopAll();
	    	
	    	if (player != null)
	    		if (player.isPlaying())
	    			player.stop();
	    	
	    	if (sound == null)
	    		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	    	
	    	sound.play(aMap.get(soundName), 1f, 1f, 0, 0, 1f);
	    		        	       
	    } 
	    catch (Exception e) 
	    {
	    	Log.i("ERROR", e.getMessage());
	    }
	}
	
	public void playVideo(String fullResourceValidName, int videoName, int videoView) {
			
	    try {
	    	
	    	stopAll();
	    	
		    //	Displays a video file.        
	    	//String uriPath = "android.resource://com.idbgm.abchebraico/"+soundName;
	    	String uriPath = fullResourceValidName+"/"+videoName;
	        Uri uri = Uri.parse(uriPath);
	        
	        if (mVideoView == null) 
	        	mVideoView = (VideoView)activityContext.findViewById(videoView);
	        	
	        mVideoView.setVideoURI(uri);
	        mVideoView.bringToFront();
	        
	        MediaController mc = new MediaController(activityContext);
	        mc.setAnchorView(mVideoView);
	        mc.setMediaPlayer(mVideoView);
	        
	        mVideoView.setMediaController(mc);
	        mVideoView.setVisibility(View.VISIBLE);
	        mVideoView.start();
	        
	    } catch (Exception e) {}
	}
	
	public void playVideo(String fullResourceValidName, int videoName) {
		playVideo(fullResourceValidName, videoName, mVideoView.getId());
	}
	
	public void stopAll()
	{
		// stop audio
		if (player == null)
    		player = new MediaPlayer();
    	
    	if (player.isPlaying())
    		player.stop();
    	
    	// stop video
    	if (mVideoView != null)
    	{			
			mVideoView.stopPlayback();
			mVideoView.setVisibility(View.GONE);
    	}

	}	
}
