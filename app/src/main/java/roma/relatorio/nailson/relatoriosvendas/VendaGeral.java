package roma.relatorio.nailson.relatoriosvendas;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

public class VendaGeral extends AppCompatActivity{

    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private MenuItem cadastro;
    private String vendedor = null;

    private FirebaseUser mCurrentUser;
    private TextView metaVendedor;
    private TextView diasuteis;
    private TextView realizado;
    private TextView faltante;
    private TextView vendadia;
    private TextView nomeVendedor;
    private TextView texto, txtMecanica;
    private TextView mt,vd,fb,vdi,du;
    private ImageView imgFinalizada;
    ProgressBar progressBar;
    private LinearLayout layout_linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_vendas);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Realizado");

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(VendaGeral.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vendas");
        mDataUser = FirebaseDatabase.getInstance().getReference().child("usuario").child(mAuth.getCurrentUser().getUid());
        vendedor = getIntent().getExtras().getString("idForne");
        carregarVenda();

        texto = (TextView) findViewById(R.id.texto);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        metaVendedor = (TextView) findViewById(R.id.metaVendedor);
        diasuteis = (TextView) findViewById(R.id.diasuteis);
        realizado = (TextView) findViewById(R.id.realizadoVendedor);
        faltante = (TextView) findViewById(R.id.vendafaltante);
        vendadia = (TextView) findViewById(R.id.vendadia);
        nomeVendedor = (TextView) findViewById(R.id.nomeVendedor);
        txtMecanica = (TextView) findViewById(R.id.txtMecanica);

        mt = (TextView)findViewById(R.id.mt);
        vd = (TextView)findViewById(R.id.vd);
        fb = (TextView)findViewById(R.id.fb);
        vdi = (TextView)findViewById(R.id.vdi);
        du = (TextView)findViewById(R.id.du);
        imgFinalizada = (ImageView)findViewById(R.id.imgFinalizada);
        layout_linear = (LinearLayout)findViewById(R.id.layout_linear);

    }
    private void carregarVenda() {

        mDatabase.child("Geral"+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String keyName = dataSnapshot.getKey();
                String meta = dataSnapshot.child("meta").getValue().toString();
                String dia = dataSnapshot.child("diasuteis").getValue().toString();
                String real = dataSnapshot.child("venda_geral").getValue().toString();
                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String vendad = dataSnapshot.child("venda_dia").getValue().toString();
                String nome = dataSnapshot.child("nome").getValue().toString();
                String porcentagem = dataSnapshot.child("porcentagem").getValue().toString();
                String positivacao = dataSnapshot.child("positivacao").getValue().toString();
                nomeVendedor.setText(nome);

                    mt.setText("Meta em R$");
                    vd.setText("Venda em R$ até hoje");
                    du.setText("Dias úteis restantes");
                    vdi.setText("Venda dia p/ bater meta");

                    diasuteis.setText(dia);
                    Double mtVendedor = Double.parseDouble(meta);
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    String formatado = nf.format(mtVendedor);
                    metaVendedor.setText(formatado);
                    Double realVendedor = Double.parseDouble(real);
                    String formatado1 = nf.format(realVendedor);
                    realizado.setText(formatado1);
                    Double diaVendedor = Double.parseDouble(vendad);
                    String formatado2 = nf.format(diaVendedor);
                    vendadia.setText(formatado2);
                    Double faltaVendedor = Double.parseDouble(falta);
                    String formatado3 = nf.format(faltaVendedor);
                    faltante.setText(formatado3);

                    double result = ((realVendedor / mtVendedor) * 100);
                    progressBar.setProgress((int) Double.parseDouble(String.valueOf(result)));
                    texto.setText((int) Double.parseDouble(String.valueOf(result)) + "%");
                    if (result >= 100) {
                        texto.setTextColor(R.color.colorPrimary);
                        progressBar.setProgressDrawable(getDrawable(R.drawable.custom_progressbar_verde));

                    }

                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fhom, menu);

        cadastro = menu.findItem(R.id.action_vinte_mais);

        mDatabase.child("Geral"+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String keyName = dataSnapshot.getKey();
                String meta = dataSnapshot.child("meta").getValue().toString();
                String dia = dataSnapshot.child("diasuteis").getValue().toString();
                String real = dataSnapshot.child("venda_geral").getValue().toString();
                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String vendad = dataSnapshot.child("venda_dia").getValue().toString();
                String nome = dataSnapshot.child("nome").getValue().toString();
                String porcentagem = dataSnapshot.child("porcentagem").getValue().toString();
                nomeVendedor.setText(nome);


                if (vendedor.toString().equals("Fhom"+nome)){
                    cadastro.setVisible(true);

                } else {
                    cadastro.setVisible(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_vinte_mais){
            Intent intent = new Intent(VendaGeral.this, VinteMais.class);
            intent.putExtra("nomeT", nomeVendedor.getText().toString());
            startActivity(intent);
            // finish();
        }


        return super.onOptionsItemSelected(item);
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


