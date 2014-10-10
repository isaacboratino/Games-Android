package br.com.idbgames.chickachickaboomboomone;



import java.io.Console;
import java.lang.reflect.Method;
import java.util.Random;

import br.com.idbgames.chickachickaboomboomone.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

public class MainActivity extends Activity implements OnTouchListener {
	
	protected int layoutWidth;	
	protected int layoutHeight;
	protected int letterWidth;
	protected int letterHeight;
	protected int videoMusicaSelecionada;
	protected int lettersTelaArr[];
	
	protected int imagesSpinnerArr[] = {R.drawable.ccbb1, R.drawable.ccbb2, 
            							R.drawable.ccbb3, R.drawable.ccbb4};
	protected String labelsSpinnerArr[] = {"1","2","3","4"};
		
	protected Spinner mySpinner;
	protected PlayMusicVids playMusicVids;
	protected MainActivity main;
	
	// referencia ao layout mprincipal
	protected RelativeLayout layoutPrincipal;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main = this;
		
		layoutPrincipal = (RelativeLayout)findViewById(R.id.layoutPrincipal);

		// Fiz isso para pegar o tamanho real da tela
		layoutPrincipal.post(new Runnable() { 
        	
	        public void run() { 
	        	
	            Rect rect = new Rect(); 
	            Window win = getWindow();  // Get the Window
	            win.getDecorView().getWindowVisibleDisplayFrame(rect); 
	            
	            // Get the height of Status Bar 
	            int statusBarHeight = rect.top; 
	            
	            // Get the height occupied by the decoration contents 
	            int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop(); 
	            
	            // Calculate titleBarHeight by deducting statusBarHeight from contentViewTop  
	            int titleBarHeight = contentViewTop - statusBarHeight; 	            
	 
	            // By now we got the height of titleBar & statusBar
	            // Now lets get the screen size
	            DisplayMetrics metrics = new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(metrics);   
	            int screenHeight = metrics.heightPixels;
	            int screenWidth = metrics.widthPixels;
	 
	            // Now calculate the height that our layout can be set
	            // If you know that your application doesn't have statusBar added, then don't add here also. Same applies to application bar also 
	            layoutHeight = screenHeight - (titleBarHeight + statusBarHeight);  
	            layoutWidth = screenWidth;
	            
	            // Calculo 13% do tamanho da tela para ser o tamanho de cada letra
	    		letterWidth = (int)layoutWidth * 15 / 100;
	    	    letterHeight = (int)layoutHeight * 15 / 100;
	    	    
	    	 // Array com todas as letras que serao arrastaveis na tela
	    		int lettersImgArr[] = {  R.drawable.an,R.drawable.bn,R.drawable.cn,
	    								 R.drawable.dn,R.drawable.en,R.drawable.fn,
	    								 R.drawable.gn,R.drawable.hn,R.drawable.in,
	    								 R.drawable.jn,R.drawable.kn,R.drawable.ln,
	    								 R.drawable.mn,R.drawable.nn,
	    								 R.drawable.on,R.drawable.pn,R.drawable.qn,
	    								 R.drawable.rn,R.drawable.sn,R.drawable.tn,
	    								 R.drawable.un,R.drawable.vn,R.drawable.wn,
	    								 R.drawable.xn,R.drawable.yn,R.drawable.zn};
	    	    
	    		// Defino array que ira conter todas as letras adicionadas na tela
	    		// para poder fazer a caida delas para o solo novamente, no chicka chicka boom boom
	    		lettersTelaArr = new int[lettersImgArr.length];
	    	    
	    	    // Coloco todas as letras na tela para serem arrastadas
	    		GenerateLettersTouch(layoutPrincipal, lettersImgArr);
	    		
	    		// Define o spinner
	    		mySpinner = (Spinner)findViewById(R.id.videoMusicaSpinner);
	            mySpinner.setAdapter(new MySpinnerAdapter(MainActivity.this, 
	            		                                  R.layout.row, 
	            		                                  labelsSpinnerArr));
	            
	            // Faz o load das musicas e sons e prepara o player de video
	    		playMusicVids = new PlayMusicVids(main);
	    		PlayMusica("mccbb1");
	    		
	            // Seta o evento de seleção
	            mySpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                        	videoMusicaSelecionada = mySpinner.getSelectedItemPosition()+1;
                        	PlayMusica("mccbb"+videoMusicaSelecionada);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        	videoMusicaSelecionada = 0;
                        }
                    }
                );
	        
	        } 
       });	

	}
	
	private void GenerateLettersTouch(RelativeLayout rl, int lettersImgArr[]) {
		
		// Varro o array de letras para pintar cada uma delas na tela
		for (int i = lettersImgArr.length-1; i >= 0 ; i--)
		{			
			ImageView imgView = new ImageView(this);
			imgView.setId(12+i);
		    imgView.setImageResource(lettersImgArr[i]); // seta a imagem na imageview
		    
		    RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(letterWidth,letterHeight);
		    imgView.setLayoutParams(parms);
		    
		    // defino o posicionamento da letra na tela, apenas na região baixa da tela.
		    PosicionaLetraTela(imgView);
		    
		    // torna a letra arrastavel
		    imgView.setOnTouchListener(this);
		    
		    // torna a imagem visivel
			imgView.setVisibility(View.VISIBLE);
			
			// adiciono ela ao layout principal
			rl.addView(imgView);
			
			// adiciono lettra no array, para depois poder dar respaw ou boom boom
			lettersTelaArr[i] = imgView.getId();			
		}
		
	}
		
	private void PosicionaLetraTela(ImageView imgView) {
		
		Random random = new Random();
		
		float yPercent = layoutHeight*15/100;
		float minY = (layoutHeight-yPercent)-yPercent;
		float maxY = layoutHeight-yPercent;
				
		float finalY = minY + random.nextFloat() * (maxY - minY);
		
		// defino a região onde as letras devem aparecer
		float xPercent = layoutWidth*10/100;
		float minX = xPercent;
		float maxX = (layoutWidth-imgView.getWidth())-xPercent;
				
		float finalX = minX + random.nextFloat() * (maxX - minX);	
		
		imgView.setX(finalX);
		imgView.setY(finalY);
		
	}	
	
	float x, y = 0.0f;
	boolean moving = false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {		
		
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				moving = true;
				break;
				
			case MotionEvent.ACTION_MOVE:
				if (moving)
				{
					x = event.getRawX()-v.getWidth()/2;
					y = (event.getRawY()-v.getHeight()*4/2);			
					v.setX(x);
					v.setY(y);
				}
				break;
				
			case MotionEvent.ACTION_UP:
				moving = false;
				break;
		}
		return true;
	}
	
	public void btPlayMusica_onClick(View v)
	{
		//playMusicVids.playBeep("ccbb-explosion");
		PlayMusica("mccbb"+videoMusicaSelecionada);
	}
	
	private void PlayMusica(String nomeMusica) 
	{
		Button btMusicaPlay = (Button)findViewById(R.id.playMusicaButton);
		Button btVideoPlay = (Button)findViewById(R.id.playVideoButton);
		
		if (btMusicaPlay.getText().equals("Play"))
		{			
			playMusicVids.stopAll();

			// Roda o video
			playMusicVids.playMusic("musicas", nomeMusica, "3gp");

			//troca o botão do player do video, para ser o stop do video			
			btMusicaPlay.setText("Stop");
			btMusicaPlay.bringToFront();

			// seta o botão do video para play
			btVideoPlay.setText("Video");
		}
		else
		{
			playMusicVids.stopAll();
			btMusicaPlay.setText("Play");
		}
	}
		
	public void btPlayVideo_onClick(View v)
	{
		Button btVideoPlay = (Button)findViewById(R.id.playVideoButton);
		Button btMusicaPlay = (Button)findViewById(R.id.playMusicaButton);
		
		if (btVideoPlay.getText().equals("Video"))
		{
			int idVideoSelecionado = 0;
			switch (videoMusicaSelecionada)
			{
				case 1: idVideoSelecionado = R.raw.ccbb1; break;
				case 2: idVideoSelecionado = R.raw.ccbb2; break;
				case 3: idVideoSelecionado = R.raw.ccbb3; break;
				case 4: idVideoSelecionado = R.raw.ccbb4; break;
			}
			
			playMusicVids.stopAll();
			
			// Roda o video
			playMusicVids.playVideo("android.resource://br.com.idbgames.chickachickaboomboomone", 
									idVideoSelecionado,
	                				R.id.videoPlayer);
			
			//troca o botão do player do video, para ser o stop do video			
			btVideoPlay.setText("Fechar");
			btMusicaPlay.setText("Play");
			btVideoPlay.bringToFront();
		}
		else
		{
			playMusicVids.stopAll();
			btVideoPlay.setText("Video");			
			btMusicaPlay.setText("Play");
		}
	}
	
	public void chickaBoomButton_onClick(View v) 
	{
		
		Button btVideoPlay = (Button)findViewById(R.id.playVideoButton);
		Button btMusicaPlay = (Button)findViewById(R.id.playMusicaButton);
		
		playMusicVids.stopAll();
		btVideoPlay.setText("Video");			
		btMusicaPlay.setText("Play");
		
		playMusicVids.playMusic("musicas", "boom", "3gp");		
		// Reposiciona as letras em seus lugares
		setTimer(7000);	    
	}
	
	// SetTimeout do android :\
	protected Handler taskHandler = new Handler();
	protected void setTimer( long time ) {
		Runnable t = new Runnable() {
		    	public void run() {
		    		runNextTask();
		    	}
	        };
		taskHandler.postAtTime(t, SystemClock.uptimeMillis() + time);
	}
	 
	protected void runNextTask() {
				
		// Reposiciona as letras em seus lugares
		for (int i = 0; i < lettersTelaArr.length; i++)
	    {
	    	ImageView imgView = (ImageView)findViewById(lettersTelaArr[i]);	    
	    	PosicionaLetraTela(imgView);
	    }		
		
	};
	
	public class MySpinnerAdapter extends ArrayAdapter<String> 
	{
		 
        public MySpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }
 
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
 
        public View getCustomView(int position, View convertView, ViewGroup parent) {
 
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);

            ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(imagesSpinnerArr[position]);
 
            return row;
       }
	}
}

