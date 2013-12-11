package net.nrp.viewmodel;

import java.util.HashMap;

/**
 * Created by mchin on 12/10/13.
 */
public class AnswerOption {
    public String answerText;
    public String answerValue;
    public HashMap<String,String> otherAttr;
    public AnswerOption()
    {
        otherAttr = new HashMap<String, String>();
    }
}
