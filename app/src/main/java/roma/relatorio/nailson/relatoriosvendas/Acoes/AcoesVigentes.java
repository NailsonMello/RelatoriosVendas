package roma.relatorio.nailson.relatoriosvendas.Acoes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import roma.relatorio.nailson.relatoriosvendas.DatabaseUtil;
import roma.relatorio.nailson.relatoriosvendas.ImagemProduto;
import roma.relatorio.nailson.relatoriosvendas.Pessoa;
import roma.relatorio.nailson.relatoriosvendas.R;
import roma.relatorio.nailson.relatoriosvendas.TelaFornecedores;
import roma.relatorio.nailson.relatoriosvendas.TelaVendas;

public class AcoesVigentes extends AppCompatActivity {
    private ProgressDialog mProgress, dialog;;
    private DatabaseReference mDatabase, mDatabaseUser;
    private DatabaseReference mDataUser, count;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private StorageReference mStorageRef;
    private RecyclerView listaacoes;
    private FirebaseUser mCurrentUser;
    private Button btnCadAcao;
    private EditText edtAcao;
    private ImageButton btnIMG;
    private LinearLayout layoutRosangela;
    private Uri imagemUri;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acoes_vigentes);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Açoes vigentes");

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(AcoesVigentes.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("AcoesVigentes");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

        user = mAuth.getCurrentUser();

        dialog = new ProgressDialog(this);
        layoutRosangela = (LinearLayout)findViewById(R.id.layoutRosangela);
        btnCadAcao = (Button)findViewById(R.id.btnCadAcao);
        btnIMG = (ImageButton)findViewById(R.id.btnIMG);
        edtAcao = (EditText)findViewById(R.id.edtAcao);
        listaacoes = (RecyclerView) findViewById(R.id.listacoes);
        listaacoes.setLayoutManager(new LinearLayoutManager(this));
        carregar();
        btnIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriaIntent = new Intent();
                galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, GALLERY_REQUEST);

            }
        });

        btnCadAcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarAcao();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        mDataUser = FirebaseDatabase.getInstance().getReference().child("usuario");

        if (mAuth.getCurrentUser() != null) {

            mDataUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String codi = dataSnapshot.getKey();
                    String NomeMenu = dataSnapshot.child("nome").getValue().toString();
                    String EmailMenu = dataSnapshot.child("email").getValue().toString();
                    String imgMenu = dataSnapshot.child("imagem").getValue().toString();
                    String func = dataSnapshot.child("funcao").getValue().toString();


                    if (func.toString().equals("admin")) {
                        layoutRosangela.setVisibility(View.VISIBLE);
                    }else {
                        layoutRosangela.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void carregar() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("AcoesVigentes");

        FirebaseRecyclerAdapter<Pessoa, AcoesVigentes.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pessoa, AcoesVigentes.AnuncioViewHolder>
                (
                        Pessoa.class,
                        R.layout.layout_fornecedor,
                        AcoesVigentes.AnuncioViewHolder.class,
                        mDatabase

                ){

            @Override
            protected void populateViewHolder(final AcoesVigentes.AnuncioViewHolder viewHolder, final Pessoa model, int position) {

                final String usuario = getRef(position).getKey();
                viewHolder.setNome(model.getDescricao());
                viewHolder.setImagem(getApplicationContext(), model.getImagem());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ven = new Intent(AcoesVigentes.this, ImagemProduto.class);
                        ven.putExtra("httpImg", model.getImagem());
                        startActivity(ven);
                        //finish();
                    }
                });

            }

        };
        listaacoes.setAdapter(firebaseRecyclerAdapter);

    }
    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public AnuncioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNome(String nome){

            TextView nomeUser = (TextView)mView.findViewById(R.id.txtFornecedor);
            nomeUser.setText(nome);
        }
        public void setImagem(Context context, String imagem) {

            ImageView imagem_anuncio = (ImageView) mView.findViewById(R.id.imgForneccedor);
            Picasso.with(context).load(imagem).placeholder(R.mipmap.icon).into(imagem_anuncio);

        }

    }


    private void cadastrarAcao() {

        dialog.setMessage("Cadastrando ação...");
        final String desc_valor = edtAcao.getText().toString().trim();
            if(!TextUtils.isEmpty(desc_valor) && imagemUri != null) {

                dialog.show();
                StorageReference pastaImagem = mStorageRef.child("imgAcao").child(imagemUri.getLastPathSegment());

                pastaImagem.putFile(imagemUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        final DatabaseReference novoAnuncio = mDatabase.push();

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                 novoAnuncio.child("descricao").setValue(desc_valor);
                                 novoAnuncio.child("imagem").setValue(downloadUrl.toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();

                    }
                });

            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data );

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imagemUri = data.getData();
            btnIMG.setImageURI(imagemUri);

        }
    }
    @Override
    public void onBackPressed() {
        // Intent telaAnuncio = new Intent(TelaVendas.this, MainActivity.class);
        //  startActivity(telaAnuncio);
        finish();

    }
    @Override
    public boolean onSupportNavigateUp(){
        //startActivity(new Intent(TelaVendas.this, MainActivity.class));
        finish();
        return true;
    }



}
