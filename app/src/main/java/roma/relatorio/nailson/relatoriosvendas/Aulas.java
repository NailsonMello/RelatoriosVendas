package roma.relatorio.nailson.relatoriosvendas;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Aulas extends AppCompatActivity {

    private String vendedor = null;
    private RecyclerView listaAula;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseCurrentUser;
    private Query mQueryUsuario;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas);

        ActionBar act = getSupportActionBar();
        act.setTitle("Aulas disponiveis...");
        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(Aulas.this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        vendedor = getIntent().getExtras().getString("idForne");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Aulas");

        String CurrentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Aulas");

        listaAula = (RecyclerView) findViewById(R.id.listaAulas);
        // listaAnuncio1.setHasFixedSize(true);
        listaAula.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Pessoa, AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pessoa, AnuncioViewHolder>
                (
                        Pessoa.class,
                        R.layout.card_aulas,
                        AnuncioViewHolder.class,
                        mDatabase

                ){

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void populateViewHolder(AnuncioViewHolder viewHolder, Pessoa model, int position) {

                final String chave_anuncio = getRef(position).getKey();

                viewHolder.setTitulo(model.getTitulo());
                viewHolder.setDescricao(model.getDescricao());
                viewHolder.setVideo(getApplicationContext(), model.getFiles());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent telaAnuncio = new Intent(Aulas.this, TelaAulas.class);
                        telaAnuncio.putExtra("aula_id", chave_anuncio);
                        startActivity(telaAnuncio);
                        finish();

                    }
                });
            }

        };
        listaAula.setAdapter(firebaseRecyclerAdapter);

    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public AnuncioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitulo(String titulo){

            TextView titulo_anuncio = (TextView)mView.findViewById(R.id.titulo_aula);
            titulo_anuncio.setText(titulo);

        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void setVideo(final Context context, String files){
            final VideoView videoview = (VideoView) mView.findViewById(R.id.video_aula);
            final ProgressBar bufferProgress = (ProgressBar)mView.findViewById(R.id.bufferProgressAula);
            Uri uri = Uri.parse(files);
            videoview.setVideoURI(uri);
            videoview.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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
            videoview.setOnPreparedListener(new
                                                    MediaPlayer.OnPreparedListener()  {
                                                        @Override
                                                        public void onPrepared(MediaPlayer mp) {
                                                            mp.setVolume(0,0);
                                                             }
                                                    });
            videoview.requestFocus();
            videoview.start();

        }


        public void setDescricao(String descricao){
            TextView desc_anuncio = (TextView)mView.findViewById(R.id.descricao_aula);
            desc_anuncio.setText(descricao);
        }
    }



    @Override
    public void onBackPressed() {
         finish();

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}



