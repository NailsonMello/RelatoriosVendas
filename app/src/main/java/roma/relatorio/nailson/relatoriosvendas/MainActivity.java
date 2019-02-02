package roma.relatorio.nailson.relatoriosvendas;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import java.util.Calendar;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import roma.relatorio.nailson.relatoriosvendas.Acoes.AcoesVigentes;


public class MainActivity extends AppCompatActivity {
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser, count;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private MenuItem cadastro, vinte;
    private String vendedor = null;
    private RecyclerView listaforne;
    private FirebaseUser mCurrentUser;

    ProgressBar progressBar;
    private TextView txtNome;

    private TextView statusTorrada, statuschips, statusBisc, nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar act = getSupportActionBar();
        act.setTitle("Inicio");

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, InicioLogin.class));
        }

        txtNome = (TextView)findViewById(R.id.txtNome);
        listaforne = (RecyclerView) findViewById(R.id.listausuarios);
        listaforne.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        cadastro = menu.findItem(R.id.action_cadastrar);
        vinte = menu.findItem(R.id.action_vinte_mais);
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

                    txtNome.setText(NomeMenu);
                    if (func.toString().equals("vendedor")){
                        Intent intent = new Intent(MainActivity.this, TelaInicial.class);
                        intent.putExtra("idForne", NomeMenu);
                        intent.putExtra("user_id", codi);
                        startActivity(intent);
                        finish();
                        //carregar(NomeMenu);
                        //carregarDialog(NomeMenu);
                    }

                     if (func.toString().equals("admin")){
                        cadastro.setVisible(true);
                        vinte.setVisible(true);
                        carregarUsuarios();

                    }else {
                        cadastro.setVisible(false);
                        vinte.setVisible(false);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

        return true;
    }

    private void carregarUsuarios() {
        Query query;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuario");
        query = mDatabase.orderByChild("funcao").startAt("vendedor").endAt("vendedor" + "\uf8ff");

        FirebaseRecyclerAdapter<Pessoa, MainActivity.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pessoa, MainActivity.AnuncioViewHolder>
                (
                        Pessoa.class,
                        R.layout.spinnerlayout,
                        MainActivity.AnuncioViewHolder.class,
                        query

                ){

            @Override
            protected void populateViewHolder(final MainActivity.AnuncioViewHolder viewHolder, final Pessoa model, int position) {

                    final String usuario = getRef(position).getKey();

                    viewHolder.setNome(model.getNome());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ven = new Intent(MainActivity.this, TelaInicial.class);
                        ven.putExtra("idForne", model.getNome());
                        ven.putExtra("user_id", usuario);
                        startActivity(ven);
                        //finish();
                    }
                });

            }

        };
        listaforne.setAdapter(firebaseRecyclerAdapter);

    }


    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public AnuncioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNome(String nome){

            TextView nomeUser = (TextView)mView.findViewById(R.id.txt);
            nomeUser.setText(nome);



        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cadastrar) {
            startActivity(new Intent(MainActivity.this, CadastroUsuario.class));
            finish();

        }else if (id == R.id.action_sair){
            mAuth.signOut();
            if (mAuth.getCurrentUser() == null) {
                Intent it = new Intent(MainActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        }else if (id == R.id.action_vinte_mais){

            carregarVinteMais();

        }else if (id == R.id.action_catalogo){
            startActivity(new Intent(MainActivity.this, Catalogo.class));
            finish();
        }else if (id == R.id.action_acaoVigente){
            startActivity(new Intent(MainActivity.this, AcoesVigentes.class));

        }


        return super.onOptionsItemSelected(item);
    }

    private void carregarVinteMais() {
        count = FirebaseDatabase.getInstance().getReference().child("vinte_mais");
        final AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.vinte_mais_geral, null);
        alerta.setView(viewAlerta);
        statusTorrada = (TextView)viewAlerta.findViewById(R.id.statusTorrada);
        statuschips = (TextView)viewAlerta.findViewById(R.id.statusChips);
        statusBisc = (TextView)viewAlerta.findViewById(R.id.statusBisc);
        nome = (TextView)viewAlerta.findViewById(R.id.nome);
        nome.setText("Positivação geral");


        count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int somaT = 0;
                int somaC = 0;
                int somaB = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) postSnapshot.getValue();
                    Object torrada = map.get("torradaValor");
                    Object chips = map.get("chipsValor");
                    Object bisc = map.get("bisc_tapiocaValor");
                    int valorTorrada = Integer.parseInt(String.valueOf(torrada));
                    int valorchips = Integer.parseInt(String.valueOf(chips));
                    int valorBisc = Integer.parseInt(String.valueOf(bisc));
                    somaT += valorTorrada;
                    somaC += valorchips;
                    somaB += valorBisc;

                    String to = postSnapshot.child("torradaValor").getValue().toString();
                    String ch = postSnapshot.child("chipsValor").getValue().toString();
                    String bi = postSnapshot.child("bisc_tapiocaValor").getValue().toString();


                    if (to.toString().equals(String.valueOf(1))) {
                        statusTorrada.setText(String.valueOf(somaT)+" de 160");
                        // Toast.makeText(ContadorVinteMais.this, "Torradas: " + String.valueOf(somaT), Toast.LENGTH_SHORT).show();
                    }
                    if (ch.toString().equals(String.valueOf(1))){

                        statuschips.setText(String.valueOf(somaC)+" de 160");
                        //  Toast.makeText(ContadorVinteMais.this, "Chips: " + String.valueOf(somaC), Toast.LENGTH_SHORT).show();
                    }
                    if (bi.toString().equals(String.valueOf(1))){

                        statusBisc.setText(String.valueOf(somaB)+" de 160");
                        // Toast.makeText(ContadorVinteMais.this, "BiscoitoTapioca: " + String.valueOf(somaB), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final AlertDialog alertTermo = alerta.create();

        alertTermo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertTermo.show();

    }


}
    

