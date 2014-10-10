package com.idbgm.abcsounds;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private final String ptBR = "ptbr";
	private final String enEN = "enen";
	private final String esES = "eses";	
	
	public String languageSelected = enEN; 
	
	SoundPool sound = null;
	Map<String, Integer> aMap = new HashMap<String, Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		SelectLanguage(R.id.btENEN);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}		
	
	public void onClickSelectLanguage(View v) {		
		final int id = v.getId();
		SelectLanguage(id);
	}
	
	public void SelectLanguage(int id) {	
		
		try {										
			
		    ((Button)findViewById(R.id.btPTBR)).setBackgroundResource(R.drawable.ptbr);
		    ((Button)findViewById(R.id.btENEN)).setBackgroundResource(R.drawable.enen);
		    ((Button)findViewById(R.id.btESES)).setBackgroundResource(R.drawable.eses);
		    
		    aMap.clear();
		    
		    switch(id) {
		    	case R.id.btPTBR :
		    		languageSelected = ptBR;
		    		((Button)findViewById(id)).setBackgroundResource(R.drawable.ptbr_selected);		    				    			    		
		    		break;
		    	case R.id.btENEN :
		    		languageSelected = enEN;
		    		((Button)findViewById(id)).setBackgroundResource(R.drawable.enen_selected);
		    		break;
		    	case R.id.btESES :
		    		languageSelected = esES;
		    		((Button)findViewById(id)).setBackgroundResource(R.drawable.eses_selected);
		    		break;
		    	default :
		    		languageSelected = enEN;
		    		((Button)findViewById(id)).setBackgroundResource(R.drawable.enen_selected);
		    }
		    
	    	sound = null;
	    	sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);		    
		    
		    for (int i = 65; i <= 90; i++ ) {
		    	String letter = ((char)i)+"";
		    	String file = "sounds/"+ languageSelected +"/" + letter + ".mp3";
    			AssetFileDescriptor soundFile = getAssets().openFd(file);	    		    	    		    	    
    			aMap.put(letter, sound.load(soundFile , 1));
    			soundFile.close();
    		}		    		   
		    
		} catch(Exception e) {			
		}
	
	}
	
	public void onClick(View v) {		
	    final int id = v.getId();
	    Button b = (Button)v;
	    	    
	    playBeep(b.getText().toString());
	} 
	
	public void playBeep(String soundName) {
			
	    try {
	    	
	    	if (sound == null)
	    		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

	    	sound.play(aMap.get(soundName), 1f, 1f, 0, 0, 1f);
	    		        	       
	    } catch (Exception e) {}
	}
	
	/*
	MediaPlayer m;
	public void playBeep(String soundName) {
			
	    try {	    
	    	if (m == null)
	    		m = new MediaPlayer();
	    	
	        
	        AssetFileDescriptor descriptor = getAssets().openFd("sounds/"+ languageSelected +"/" + soundName + ".3gp");
	        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        descriptor.close();

	        m.prepare();
	        m.setVolume(1f, 1f);
	        m.setLooping(false);
	        m.start();
	        
	    } catch (Exception e) {
	    	
	    } finally {	    	
	    	m = null;    
	    }	    
	}
	*/
}
