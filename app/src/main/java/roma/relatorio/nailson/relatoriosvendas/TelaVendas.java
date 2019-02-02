package roma.relatorio.nailson.relatoriosvendas;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

public class TelaVendas extends AppCompatActivity {
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
        FirebaseApp.initializeApp(TelaVendas.this);
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

        mDatabase.child(vendedor).addValueEventListener(new ValueEventListener() {
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

                if (vendedor.toString().equals("Dtc" + nome)) {
                    realizado.setText(real);

                    Double mtVendedor = Double.parseDouble(porcentagem);
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    String formatado = nf.format(mtVendedor);

                    metaVendedor.setText("Seu saldo é: " + formatado);
                    layout_linear.setVisibility(View.GONE);
                    mt.setText("A receber");
                    vd.setText("Venda em DP até hoje");
                    txtMecanica.setVisibility(View.VISIBLE);
                    txtMecanica.setText("***Mecânica*** \n R$ 2,00 por displays vendidos, pagamento quando atingir venda superior a 40 displays. Será pago sobre todos os displays.\n Só recebe quem vender mais de 40 displays.");

                }else if (vendedor.toString().equals("Vitalin" + nome)){
                    realizado.setText(positivacao);

                    Double mtVendedor = Double.parseDouble(porcentagem);
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    String formatado = nf.format(mtVendedor);

                    metaVendedor.setText("Seu saldo é: " + formatado);
                    layout_linear.setVisibility(View.GONE);
                    mt.setText("A receber");
                    vd.setText("Positivação até hoje");
                    txtMecanica.setVisibility(View.VISIBLE);
                    txtMecanica.setText("***Mecânica*** \n R$ 30,00 por cada cliente novo positivado e com venda superior a R$ 300,00.");

                }else {

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

        mDatabase.child(vendedor).addValueEventListener(new ValueEventListener() {
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
             Intent intent = new Intent(TelaVendas.this, VinteMais.class);
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
