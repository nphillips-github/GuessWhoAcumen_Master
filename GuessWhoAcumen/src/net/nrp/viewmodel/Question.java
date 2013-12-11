package net.nrp.viewmodel;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by mchin on 12/10/13.
 */
public class Question {
    public String imageURL;
    public ArrayList<AnswerOption> answerOptions;
    public String correct_AnswerValue;
    public Bitmap bitmap = null;

    public Question(){
        answerOptions = new ArrayList<AnswerOption>();
    }
}
