package net.nrp.guesswhoacumen;

import android.app.Activity;
import android.os.Bundle;

import net.nrp.tasks.CallMobileQuizWS;

/**
 * Created by jbryant on 12/11/13.
 */


public class PlayGame extends Activity{
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.game);
    }
}