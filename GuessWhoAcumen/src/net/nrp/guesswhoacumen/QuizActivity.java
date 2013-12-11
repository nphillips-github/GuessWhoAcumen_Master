package net.nrp.guesswhoacumen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import net.nrp.datamodels.Person;
import net.nrp.interfaces.iAsyncTaskAction;
import net.nrp.tasks.CallMobileQuizWS;
import net.nrp.tasks.LoadImageTask;
import net.nrp.viewmodel.AnswerOption;
import net.nrp.viewmodel.Question;

import java.util.ArrayList;


public class QuizActivity extends SalesforceActivity {

    private RestClient client;
    public Question currentQuestion;

    public iAsyncTaskAction<Bitmap> loadFrontImage = new iAsyncTaskAction<Bitmap>() {
        @Override
        public void execute(Bitmap value) {
            ImageView qPic = (ImageView)findViewById(R.id.quizPic);
            qPic.setImageBitmap(value);
        }
    };
    public iAsyncTaskAction<ArrayList<Person>> loadQuiz = new iAsyncTaskAction<ArrayList<Person>>() {
        @Override
        public void execute(ArrayList<Person> value) {
            currentQuestion = new Question();
            if(value.size() > 0)
            {
                Integer chosen = (((Double)(Math.random() * value.size())).intValue());

                for (int c = 0; c< value.size(); c++)
                {
                    Person curVal = value.get(c);
                    if(c == chosen)
                    {
                        currentQuestion.correct_AnswerValue = curVal.iD;
                        currentQuestion.imageURL = curVal.fullPhotoUrl;
                        AnswerOption temp = new AnswerOption();
                        temp.answerText = curVal.firstName + curVal.lastName;
                        temp.answerValue = curVal.iD;
                        temp.otherAttr.put("imageUrl",curVal.fullPhotoUrl);
                        currentQuestion.answerOptions.add(temp);
                    }
                    else
                    {
                        AnswerOption temp = new AnswerOption();
                        temp.answerText = curVal.firstName + " " + curVal.lastName;
                        temp.answerValue = curVal.iD;
                        temp.otherAttr.put("imageUrl",curVal.fullPhotoUrl);
                        currentQuestion.answerOptions.add(temp);
                    }
                }
                new LoadImageTask(client,loadFrontImage).execute(currentQuestion.imageURL);
                loadQuizOptions(currentQuestion.answerOptions);
            }
        }
    };

    public View.OnClickListener checkAnswerCommand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String btnValue = view.getTag(R.string.answer_value).toString();
            checkAnswer(btnValue);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
    }

    @Override
    public void onResume() {
        // Hide everything until we are logged in
        findViewById(R.id.quizRoot).setVisibility(View.INVISIBLE);
        super.onResume();
        setupQuiz();
    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;

        // Show everything
        findViewById(R.id.quizRoot).setVisibility(View.VISIBLE);
        setupQuiz();
    }

    protected void setupQuiz()
    {
        if(client != null){
            new CallMobileQuizWS(client,loadQuiz).execute();
        }
    }
    protected void clearQuiz()
    {
        getOptionButton(0).setText("");
        getOptionButton(1).setText("");
        getOptionButton(2).setText("");
        getOptionButton(3).setText("");
        getOptionButton(0).setTag(R.string.answer_value, "");
        getOptionButton(1).setTag(R.string.answer_value, "");
        getOptionButton(2).setTag(R.string.answer_value, "");
        getOptionButton(3).setTag(R.string.answer_value, "");

    }

    protected void loadQuizOptions(ArrayList<AnswerOption> opts)
    {
        for(int a = 0; a< opts.size(); a++)
        {
            Button current = getOptionButton(a);
            if(current != null)
            {
                current.setText(opts.get(a).answerText);
                current.setTag(R.string.answer_value, opts.get(a).answerValue);
                current.setOnClickListener(checkAnswerCommand);
            }
        }
    }

    protected Button getOptionButton(int indx)
    {
        Button found = null;
        switch(indx)
        {
            case 0: found = (Button)findViewById(R.id.btnOption0); break;
            case 1: found = (Button)findViewById(R.id.btnOption1); break;
            case 2: found = (Button)findViewById(R.id.btnOption2); break;
            case 3: found = (Button)findViewById(R.id.btnOption3); break;
            default: break;
        }
        return found;
    }


    public void checkAnswer(String chosen)
    {
        if(chosen.equals(currentQuestion.correct_AnswerValue))
        {
            buildDialog(R.string.answered_correctly, R.string.answered_message);
        }
        else
        {
            buildDialog(R.string.answered_wrongly, R.string.answered_message);
        }
    }

    public void buildDialog(Integer title, Integer message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setTitle(title)
                .setNegativeButton(R.string.answered_dialog_negative,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setPositiveButton(R.string.answered_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearQuiz();
                setupQuiz();
            }
        })
        ;


        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
