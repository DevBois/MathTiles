package mathtiles.nicholaschan.com.mathtiles;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
{
    public int numberOfCorrectQuestions = 0;
    public int numberOfIncorrectQuestions = 0;
    public int numberOfSelectedCorrect = 0;
    public int numberOfSelectedIncorrect = 0;
    public boolean finishedQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView answerText = (TextView) findViewById(R.id.answerText);
        int maxRangeOfAnswer = 30;
        int answer = (int) (Math.random()*(maxRangeOfAnswer))+1;
        answerText.setText("" + answer);

        GridView buttonLayout = (GridView) findViewById(R.id.buttonLayout);
        //have no idea what buttonLayout to null is, just alt + enter
        if(buttonLayout != null)
        {
            buttonLayout.setNumColumns(4);
        }

        ArrayList<String> questions = new ArrayList<>();
        int numberOfEmptyButtons = 16;
        while (numberOfEmptyButtons > 0)
        {
            String generatedQuestion = generateQuestions(answer);
            if(!questions.contains(generatedQuestion))
            {
                questions.add(generatedQuestion);
                numberOfEmptyButtons -= 1;
                if(parseQuestion(generatedQuestion) == answer)
                {
                    numberOfCorrectQuestions += 1;
                }
                else
                {
                    numberOfIncorrectQuestions += 1;
                }
            }
        }
        //change gridView cells to have buttons of questions
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.buttonlayout_items, questions);
        buttonLayout.setAdapter(arrayAdapter);
        answersLeft();
    }
    public void answersLeft()
    {
        TextView questionsRemaining = (TextView) findViewById(R.id.questionsRemaining);
        questionsRemaining.setText("Answers Left: " + (numberOfCorrectQuestions-numberOfSelectedCorrect));
    }
    public void selectQuestion(View view)
    {
        if(finishedQuestion == false)
        {
            //get question as string
            Button questionButton = (Button) view.findViewById(R.id.question_button);
            String selectedQuestion = questionButton.getText() + "";
            //questionButton.setClickable(false);
            //get answer as string
            TextView answerText = (TextView) findViewById(R.id.answerText);
            int answer = Integer.parseInt(answerText.getText() + "");

            //when select answer
            if (parseQuestion(selectedQuestion) == answer)
            {
                questionButton.setBackgroundColor(Color.parseColor("#6ada8e"));
                numberOfSelectedCorrect += 1;
            }
            if (parseQuestion(selectedQuestion) != answer)
            {
                questionButton.setBackgroundColor(Color.parseColor("#d73b3e"));
                numberOfSelectedIncorrect += 1;
            }
            if (numberOfSelectedCorrect == numberOfCorrectQuestions)
            {
                if (numberOfSelectedIncorrect == 0)
                {
                    Toast.makeText(MainActivity.this, "PERFECT!", Toast.LENGTH_SHORT).show();
                }
                if (numberOfSelectedIncorrect > 0)
                {
                    Toast.makeText(MainActivity.this, "MISSED: " + numberOfSelectedIncorrect + " QUESTIONS!", Toast.LENGTH_SHORT).show();
                }
                finishedQuestion = true;
            }
            answersLeft();
        }
    }
    public ArrayList<Integer> findFactors(int number)
    {
        //returns an array list of factors for an integer
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for(int i = 1; i <= number; i += 1)
        {
            if(number % i == 0)
            {
                factors.add(i);
            }
        }
        Collections.sort(factors);
        return factors;
    }
    public String generateQuestions(int answer)
    {
        String question = "";
        /*
        determines type of operation
        0-25 = addition
        26-50 = subtraction
        51-75 = multiplication
        76-100 = division
         */
        int rangeOfRandom = 100;
        int operation = (int) (Math.random()*rangeOfRandom);
        int determineCorrect = (int) (Math.random()*rangeOfRandom);
        //determines whether the question generated will be correct or wrong
        if(determineCorrect > rangeOfRandom/2)
        {
            //correct question generated
            if(operation <= 25)
            {
                //addition
                int firstNumber = (int) ((Math.random()*(answer-1))+1);
                int secondNumber = answer - firstNumber;
                question = firstNumber + " + " + secondNumber;
            }
            if(operation > 25 && operation <= 50)
            {
                //subtraction
                //strange way to generate number greater than answer
                int firstNumber = (int) ((Math.random()*(answer*2)+answer));
                int secondNumber = firstNumber - answer;
                question = firstNumber + " - " + secondNumber;
            }
            if(operation > 50 && operation <= 75)
            {
                //multiplication
                ArrayList<Integer> factors = findFactors(answer);
                if(factors.size() > 3)
                {
                    factors.remove(0); //remove 1
                    factors.remove(factors.size()-1); //remove last number
                }
                int randomIndex = (int) (Math.random()*(factors.size()));
                int firstNumber = factors.get(randomIndex);
                int secondNumber = answer/firstNumber;
                question = firstNumber + " x " + secondNumber;
            }
            if(operation > 75)
            {
                //division
                //able to change difficulty of numbers generated
                int numberOfTimes = 2;
                int maxRangeOfMultiplier = 6;
                int startingNumber = answer;
                for(int i = 0; i < numberOfTimes; i+=1)
                {
                    startingNumber *= (int) ((Math.random()*maxRangeOfMultiplier)+1);
                }
                int firstNumber = startingNumber;
                int secondNumber = startingNumber/answer;
                question = firstNumber + " ÷ " + secondNumber;
            }
        }
        else
        {
            //incorrect question generated
            int incorrectMargin= (int) ((Math.random()*10)+1);
            if(operation <= 25)
            {
                //addition
                int firstNumber = (int) (Math.random()*answer);
                int secondNumber = answer - firstNumber + incorrectMargin;
                question = firstNumber + " + " + secondNumber;
            }
            if(operation > 25 && operation <= 50)
            {
                //subtraction
                //strange way to generate number greater than answer
                int firstNumber = (int) ((Math.random()*(answer*2)+answer));
                int secondNumber = firstNumber - answer + incorrectMargin;
                question = firstNumber + " - " + secondNumber;
            }
            if(operation > 50 && operation <= 75)
            {
                //multiplication
                ArrayList<Integer> factors = findFactors(answer);
                if(factors.size() > 3)
                {
                    factors.remove(0); //remove 1
                    factors.remove(factors.size()-1); //remove last number
                }
                int randomIndex = (int) (Math.random()*(factors.size()));
                int firstNumber = factors.get(randomIndex);
                int secondNumber = (answer/firstNumber) + incorrectMargin;
                question = firstNumber + " x " + secondNumber;
            }
            if(operation > 75)
            {
                //division
                //able to change difficulty of numbers generated
                int numberOfTimes = 2;
                int maxRangeOfMultiplier = 6;
                int startingNumber = answer;
                for(int i = 0; i < numberOfTimes; i+=1)
                {
                    startingNumber *= (int) ((Math.random()*maxRangeOfMultiplier)+1);
                }
                int firstNumber = startingNumber - (int) (Math.pow(incorrectMargin, 2));
                int secondNumber = startingNumber/answer;
                question = firstNumber + " ÷ " + secondNumber;
            }
        }
        return question;
    }
    public int parseQuestion(String question)
    {
        int result = 0;
        String[] questionSplit = question.split(" ");
        int firstNumber = Integer.parseInt(questionSplit[0]);
        int secondNumber = Integer.parseInt(questionSplit[2]);
        String operation = questionSplit[1];
        switch (operation)
        {
            case "+" : result = firstNumber + secondNumber; break;
            case "-" : result = firstNumber - secondNumber; break;
            case "x" : result = firstNumber * secondNumber; break;
            case "÷" : result = firstNumber / secondNumber; break;
        }
        return result;
    }
}