package roma.relatorio.nailson.relatoriosvendas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Question extends AppCompatActivity {

    private String aula = null;
    private TextView mQuestion, score;

    private Button mButtonChoice1, mButtonChoice2, mButtonChoice3, mButtonChoice4;

    private int mScore = 0;
    private int mQuestionNumber = 1;
    private int CountQuestion;
    private String mAnswer;

    private DatabaseReference mQuestionRef, mCountQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        aula = getIntent().getExtras().getString("aula_id");

        mQuestion = (TextView)findViewById(R.id.question);
        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mButtonChoice3 = (Button)findViewById(R.id.choice3);
        mButtonChoice4 = (Button)findViewById(R.id.choice4);
        score = (TextView)findViewById(R.id.score);
        mQuestionRef = FirebaseDatabase.getInstance().getReference().child("Questionario");
        mCountQuestion = FirebaseDatabase.getInstance().getReference().child("Questionario");
        mCountQuestion.child(aula).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    //Log.e(String.valueOf(snap.child(currentUserId).toString()),snap.getChildrenCount() + "");
                    CountQuestion = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                    //Toast.makeText(Question.this, "QTD: "+CountQuestion, Toast.LENGTH_SHORT).show();

                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        UpdateQuestion();

        mButtonChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonChoice1.getText().toString().equals(mAnswer)){
                mScore = mScore + 1;
                    UpdateScore(mScore);
                    UpdateQuestion();
                }else {
                    UpdateQuestion();
                }
            }
        });

        mButtonChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonChoice2.getText().toString().equals(mAnswer)){
                    mScore = mScore + 1;
                    UpdateScore(mScore);
                    UpdateQuestion();
                }else {
                    UpdateQuestion();
                }
            }
        });

        mButtonChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonChoice3.getText().toString().equals(mAnswer)){
                    mScore = mScore + 1;
                    UpdateScore(mScore);
                    UpdateQuestion();
                }else {
                    UpdateQuestion();
                }
            }
        });

        mButtonChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonChoice4.getText().toString().equals(mAnswer)){
                    mScore = mScore + 1;
                    UpdateScore(mScore);
                    UpdateQuestion();
                }else {
                    UpdateQuestion();
                }
            }
        });
    }

    private void UpdateScore(int scores){

        score.setText(""+ mScore);

    }

    private void UpdateQuestion(){
        mQuestionRef.child(aula).child(String.valueOf(mQuestionNumber)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String question = dataSnapshot.child("pergunta").getValue().toString();
                    String choice1 = dataSnapshot.child("questao1").getValue().toString();
                    String choice2 = dataSnapshot.child("questao2").getValue().toString();
                    String choice3 = dataSnapshot.child("questao3").getValue().toString();
                    String choice4 = dataSnapshot.child("questao4").getValue().toString();
                    mAnswer = dataSnapshot.child("resposta").getValue().toString();

                    mQuestion.setText(question);
                    mButtonChoice1.setText(choice1);
                    mButtonChoice2.setText(choice2);
                    mButtonChoice3.setText(choice3);
                    mButtonChoice4.setText(choice4);

                }else {

                    AlertDialog.Builder alerta = new AlertDialog.Builder(Question.this);
                    alerta.setTitle("Olá");
                    alerta.setIcon(android.R.drawable.ic_menu_info_details);
                    alerta.setMessage("Vc acertou: "+mScore+" questões!!!");
                    alerta.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    alerta.show();
                    //Toast.makeText(Question.this, "Vc acertou: "+mScore+" questões!!!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            mQuestionNumber++;

    }
}
