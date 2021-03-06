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

public class Campanhas extends AppCompatActivity {

    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private MenuItem cadastro;
    private String vendedor = null;
    private String forn = null;
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
        FirebaseApp.initializeApp(Campanhas.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Campanhas");
        mDataUser = FirebaseDatabase.getInstance().getReference().child("usuario").child(mAuth.getCurrentUser().getUid());
        vendedor = getIntent().getExtras().getString("idForne");
        forn = getIntent().getExtras().getString("forne");
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

        mDatabase.child(forn+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String keyName = dataSnapshot.getKey();
                //String meta = dataSnapshot.child("meta").getValue().toString();
                //String dia = dataSnapshot.child("diasuteis").getValue().toString();
                String real = dataSnapshot.child("venda_geral").getValue().toString();
                String forne = dataSnapshot.child("fornecedor").getValue().toString();
                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String vendad = dataSnapshot.child("venda_dia").getValue().toString();
                String nome = dataSnapshot.child("nome").getValue().toString();
                String porcentagem = dataSnapshot.child("porcentagem").getValue().toString();
                String positivacao = dataSnapshot.child("positivacao").getValue().toString();
                nomeVendedor.setText(nome);

                if (forne.toString().equals("Incoco200") || forne.toString().equals("Incoco500") || forne.toString().equals("Natuterra")) {
                    metaVendedor.setText(porcentagem);
                }else {

                    Double mtVendedor = Double.parseDouble(porcentagem);
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    String formatado = nf.format(mtVendedor);
                    metaVendedor.setText("Seu saldo é: " + formatado);
                }
                layout_linear.setVisibility(View.GONE);
                mt.setText("A receber");
                realizado.setText(real);
                vd.setText(falta);
                txtMecanica.setVisibility(View.VISIBLE);
                txtMecanica.setText("***Mecânica*** \n"+vendad);

                if (real.toString().equals("nao_existe")) {
                    realizado.setText(positivacao);
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

        mDatabase.child(forn+vendedor).addValueEventListener(new ValueEventListener() {
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


                if (forn.toString().equals("Fhom"+nome)){
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
            Intent intent = new Intent(Campanhas.this, VinteMais.class);
            intent.putExtra("nomeT", nomeVendedor.getText().toString());
            startActivity(intent);
            // finish();
        }


        return super.onOptionsItemSelected(item);
    }


/*
    @Override
    protected void onStart() {
        super.onStart();

        Query query;
        query = mDatabase.orderByChild("fornecedor").startAt(vendedor).endAt(vendedor + "\uf8ff");
        final AlertDialog.Builder alerta = new AlertDialog.Builder(TelaVendas.this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.pop_up_venda, null);
        alerta.setView(viewAlerta);

        final TextView vendaFalta = (TextView) viewAlerta.findViewById(R.id.vendaFaltas);
        final TextView txtClose = (TextView) viewAlerta.findViewById(R.id.txtClose);

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String falta = dataSnapshot.child("venda_faltante").getValue().toString();

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                Double faltaVendedor = Double.parseDouble(falta);
                String formatado3 = nf.format (faltaVendedor);
                vendaFalta.setText("Você falta para bater a meta: \n"+formatado3);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final AlertDialog alertTermo = alerta.create();
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertTermo.dismiss();
            }
        });
        alertTermo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertTermo.show();

    }

*/


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

