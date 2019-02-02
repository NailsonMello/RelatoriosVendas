package roma.relatorio.nailson.relatoriosvendas;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TelaAulas extends AppCompatActivity {

    private VideoView videoView;
    private TextView tituloTelaAula, descricaoTelaAula, currentTimer, durationTimer;
    private Button btn_questionario;
    private ImageView btnfull, btnPlay;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;

    private String aula = null;
    private ProgressBar bufferProgress, currentProgress;
    private boolean isPlaying;
    private int current = 0;
    private int duration = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_aulas);
        final ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);

        isPlaying = false;

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(TelaAulas.this);
        aula = getIntent().getExtras().getString("aula_id");
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Aulas").child(aula);


            videoView = (VideoView)findViewById(R.id.videoView);
            tituloTelaAula = (TextView)findViewById(R.id.tituloTelaAula);
            descricaoTelaAula = (TextView)findViewById(R.id.descricaoTelaAula);
            btn_questionario = (Button)findViewById(R.id.btn_questionario);
            btnfull = (ImageView)findViewById(R.id.btnFull);
            bufferProgress = (ProgressBar)findViewById(R.id.bufferProgress);
            currentProgress = (ProgressBar)findViewById(R.id.videoProgress);
            currentProgress.setMax(100);
            currentTimer = (TextView)findViewById(R.id.currenTimer);
            durationTimer = (TextView)findViewById(R.id.durationTimer);
            btnPlay = (ImageView)findViewById(R.id.btnPlay);

            mDatabase.addValueEventListener(new ValueEventListener() {


                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String key = dataSnapshot.getKey();
                final String video = dataSnapshot.child("files").getValue().toString();
                String titulo = dataSnapshot.child("titulo").getValue().toString();
                String descricao = dataSnapshot.child("descricao").getValue().toString();
                act.setTitle(titulo);
                tituloTelaAula.setText(titulo);
                descricaoTelaAula.setText(descricao);
                Uri uri = Uri.parse(video);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {

                        if (i == mediaPlayer.MEDIA_INFO_BUFFERING_START){
                            bufferProgress.setVisibility(View.VISIBLE);
                            return true;

                        }else if (i == mediaPlayer.MEDIA_INFO_BUFFERING_END){
                            bufferProgress.setVisibility(View.INVISIBLE);
                            return true;
                        }
                        bufferProgress.setVisibility(View.INVISIBLE);
                        return false;

                    }
                });

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        duration = mediaPlayer.getDuration()/1000;


                        String durationString = String.format("%02d:%02d",duration/60, duration % 60);

                        durationTimer.setText(durationString);



                    }
                });

                videoView.start();
                isPlaying = true;

                btnPlay.setImageResource(R.drawable.ic_pause);
               // new VideoProgress().execute();
                btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPlaying){
                            videoView.pause();
                            isPlaying = false;
                            btnPlay.setImageResource(R.drawable.ic_play);
                        }else {
                            videoView.start();
                            isPlaying = true;
                            btnPlay.setImageResource(R.drawable.ic_pause);
                        }
                    }
                });

                btnfull.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW );
                        intent.setDataAndType(Uri.parse(video), "video/*");
                        startActivity(intent);
                    }
                });
                btn_questionario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent telaQuestionario = new Intent(TelaAulas.this, Question.class);
                        telaQuestionario.putExtra("aula_id", key);
                        startActivity(telaQuestionario);
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent it = new Intent(TelaAulas.this, Aulas.class);
        it.putExtra("idForne", aula);
        startActivity(it);
        finish();

    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent it = new Intent(TelaAulas.this, Aulas.class);
        it.putExtra("idForne", aula);
        startActivity(it);
        finish();

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        isPlaying = false;
    }

    public class VideoProgress extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            do {

                if (isPlaying) {
                    current = videoView.getCurrentPosition() / 1000;
                    publishProgress(current);
                }
            }while (currentProgress.getProgress() <= 100);


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            try{
                int currenPercent = values[0] * 100/duration;
                currentProgress.setProgress(currenPercent);
                String currentString = String.format("%02d:%02d",values[0]/60, values[0] % 60);
                currentTimer.setText(currentString);

                if (currentTimer.getText().toString().equals(durationTimer.getText().toString())){
                    btn_questionario.setVisibility(View.VISIBLE);
                }else {
                    btn_questionario.setVisibility(View.GONE);
                }

            }catch (Exception e){

            }
        }
    }
}
