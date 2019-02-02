package roma.relatorio.nailson.relatoriosvendas;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import roma.relatorio.nailson.relatoriosvendas.Acoes.AcoesVigentes;
import roma.relatorio.nailson.relatoriosvendas.RegistroPonto.CheckIn;
import roma.relatorio.nailson.relatoriosvendas.RegistroPonto.PontosRegistrados;

public class TelaFornecedores extends AppCompatActivity {
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser, count;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private MenuItem pont;
    private String vendedor = null;
    private String userId = null;
    private RecyclerView listaforne;
    private FirebaseUser mCurrentUser;
    private RelativeLayout layouVisible;
    ProgressBar progressBar;
    private TextView txtNome;
    private Button btnFaturado, btnDevolvido, btnSair, btnRegistrados, btnAnual;
    private ImageButton RegPonto;
    private LinearLayout layoutRosangela,layoutRosangela1;
    private TextView statusTorrada, statuschips, statusBisc, nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_fornecedores);;
        ActionBar act = getSupportActionBar();
        act.setTitle("Fornecedores");

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(TelaFornecedores.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        vendedor = getIntent().getExtras().getString("idForne");
        userId = getIntent().getExtras().getString("user_id");

        txtNome = (TextView)findViewById(R.id.txtNome);
        layoutRosangela = (LinearLayout)findViewById(R.id.layoutRosangela);
        layoutRosangela1 = (LinearLayout)findViewById(R.id.layoutRosangela1);
        btnFaturado = (Button)findViewById(R.id.btnFaturados);
        btnDevolvido = (Button)findViewById(R.id.btnDevolucoes);
        btnRegistrados = (Button)findViewById(R.id.btnRegistrados);
        btnAnual = (Button)findViewById(R.id.btnanual);
        RegPonto = (ImageButton)findViewById(R.id.btnFaturados1);
        btnSair = (Button)findViewById(R.id.btnSair);
        listaforne = (RecyclerView) findViewById(R.id.listafornecedor);
        listaforne.setLayoutManager(new LinearLayoutManager(this));
        carregar();
        //listaforne.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        /*if (mAuth.getCurrentUser() != null) {
            boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARME_DISPARADO"), PendingIntent.FLAG_NO_CREATE) == null);

            if (alarmeAtivo) {
                Log.i("Script", "Novo alarme");

                   Intent intent = new Intent("ALARME_DISPARADO");
                   PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.add(Calendar.SECOND, 3);

                AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 7200000, p);
                //7200000

            } else {
                Log.i("Script", "Alarme já ativo");
            }
        }*/

       btnSair.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mAuth.signOut();
               if (mAuth.getCurrentUser() == null) {
                   Intent it = new Intent(TelaFornecedores.this, InicioLogin.class);
                   startActivity(it);
                   finish();
               }
           }
       });

        btnDevolvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaFornecedores.this, Devolucoes.class);
                it.putExtra("idForne", vendedor);
                startActivity(it);
            }
        });
        btnFaturado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaFornecedores.this, Pedidos.class);
                it.putExtra("idForne", vendedor);
                startActivity(it);
            }
        });

        btnRegistrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaFornecedores.this, PontosRegistrados.class);
                it.putExtra("nome", vendedor);
                it.putExtra("user_id", userId);
                startActivity(it);

            }
        });

        btnAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaFornecedores.this, GraficoVendas.class);
                it.putExtra("idForne", vendedor);
                startActivity(it);
            }
        });

        RegPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRoteiro();
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

                    txtNome.setText(NomeMenu);
                    if (func.toString().equals("vendedor")) {
                        carregarDialog();
                    }

                    if (NomeMenu.toString().equals("Rosangela") || NomeMenu.toString().equals("Diana")){
                        layoutRosangela.setVisibility(View.VISIBLE);
                        layoutRosangela1.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void carregarDialog() {
        Query query;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vendas");

        // query = mDatabase.orderByChild("fornecedor").startAt("Flormel"+nomeMenu).endAt("Flormel" + "\uf8ff");
        final AlertDialog.Builder alerta = new AlertDialog.Builder(TelaFornecedores.this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.pop_up_venda, null);
        alerta.setView(viewAlerta);

        final TextView vendaFalta = (TextView) viewAlerta.findViewById(R.id.vendaFaltas);
        final TextView vendabio2 = (TextView) viewAlerta.findViewById(R.id.vendabio2);
        final TextView vendafhom = (TextView) viewAlerta.findViewById(R.id.vendafhom);
        final TextView vendaflormel = (TextView) viewAlerta.findViewById(R.id.vendaflormel);
        final TextView txtClose = (TextView) viewAlerta.findViewById(R.id.txtClose);
//venda geral
        mDatabase.child("Geral"+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String nomes = dataSnapshot.child("nome").getValue().toString();

                if (vendedor.toString().equals(nomes)) {
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    Double faltaVendedor = Double.parseDouble(falta);
                    String formatado3 = nf.format(faltaVendedor);
                    vendaFalta.setText("Geral: "+formatado3);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//venda flormel
        mDatabase.child("Flormel"+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String nomes = dataSnapshot.child("nome").getValue().toString();

                if (vendedor.toString().equals(nomes)) {
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    Double faltaVendedor = Double.parseDouble(falta);
                    String formatado3 = nf.format(faltaVendedor);
                    vendaflormel.setText("Flormel: "+formatado3);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        //venda fhom
        mDatabase.child("Fhom"+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String nomes = dataSnapshot.child("nome").getValue().toString();

                if (vendedor.toString().equals(nomes)) {

                    vendafhom.setText("Fhom: "+falta);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //venda biosoft
        mDatabase.child("Biosoft"+vendedor).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String falta = dataSnapshot.child("venda_faltante").getValue().toString();
                String nomes = dataSnapshot.child("nome").getValue().toString();

                if (vendedor.toString().equals(nomes)) {
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    Double faltaVendedor = Double.parseDouble(falta);
                    String formatado3 = nf.format(faltaVendedor);
                    vendabio2.setText("Biosoft: "+formatado3);

                }
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

    public void carregar(){
        Query query;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("vendas");
        query = mDatabase.orderByChild("nome").startAt(vendedor).endAt(vendedor + "\uf8ff");
        FirebaseRecyclerAdapter<Pessoa, TelaFornecedores.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pessoa, TelaFornecedores.AnuncioViewHolder>
                (
                        Pessoa.class,
                        R.layout.layout_fornecedor,
                        TelaFornecedores.AnuncioViewHolder.class,
                        query

                ){

            @Override
            protected void populateViewHolder(final TelaFornecedores.AnuncioViewHolder viewHolder, final Pessoa model, int position) {

                final String usuario = getRef(position).getKey();
                viewHolder.setNome(model.getFornecedor());
                viewHolder.setImagem(getApplicationContext(), model.getImagem());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ven = new Intent(TelaFornecedores.this, TelaVendas.class);
                        ven.putExtra("idForne", usuario);
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

            TextView nomeUser = (TextView)mView.findViewById(R.id.txtFornecedor);
            nomeUser.setText(nome);
        }

        public void setImagem(Context context, String imagem){

            ImageView nomeUser = (ImageView) mView.findViewById(R.id.imgForneccedor);
            Picasso.with(context).load(imagem).placeholder(R.mipmap.icon).into(nomeUser);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vend, menu);
        pont = menu.findItem(R.id.action_ponto);

        if (mAuth.getCurrentUser().getUid().toString().equals(userId)){
            pont.setVisible(true);
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.action_sair){
            mAuth.signOut();
            if (mAuth.getCurrentUser() == null) {
                Intent it = new Intent(TelaFornecedores.this, InicioLogin.class);
                startActivity(it);
                finish();
            }
        }else if (id == R.id.action_faturados){
           Intent it = new Intent(TelaFornecedores.this, Pedidos.class);
           it.putExtra("idForne", vendedor);
           startActivity(it);
       }else if (id == R.id.action_devolucoes){
           Intent it = new Intent(TelaFornecedores.this, Devolucoes.class);
           it.putExtra("idForne", vendedor);
           startActivity(it);
       }else if (id == R.id.action_grafico){
           Intent it = new Intent(TelaFornecedores.this, GraficoVendas.class);
           it.putExtra("idForne", vendedor);
           startActivity(it);
       }else if (id == R.id.action_registro){
           Intent it = new Intent(TelaFornecedores.this, PontosRegistrados.class);
           it.putExtra("nome", vendedor);
           it.putExtra("user_id", userId);
           startActivity(it);
       }else if (id == R.id.action_ponto){
           mostrarRoteiro();

       }else if (id == R.id.action_acaoVigente){
           startActivity(new Intent(TelaFornecedores.this, AcoesVigentes.class));

       }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarRoteiro() {

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Escolha uma Semana");
        alert.setIcon(R.drawable.ic_lojas);
        View row = getLayoutInflater().inflate(R.layout.lista_users, null);
        final ListView li = (ListView)row.findViewById(R.id.testandooo);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        li.setAdapter(adapter);

        Query query;
        dref = FirebaseDatabase.getInstance().getReference().child("Rotas");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String semana = dataSnapshot.child("semana").getValue(String.class);

                list.add(semana);
                adapter.notifyDataSetChanged();
                li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String noome = String.valueOf(parent.getItemAtPosition(position));
                        mostrarLojas(noome);

                    }
                });



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        alert.setView(row);
        AlertDialog dialog = alert.create();
        dialog.show();



    }

    private void mostrarLojas(String semana) {
        //Toast.makeText(this, semana, Toast.LENGTH_SHORT).show();
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Escolha uma loja");
        alert.setIcon(R.drawable.ic_lojas);
        View row = getLayoutInflater().inflate(R.layout.lista_users, null);
        final ListView li = (ListView)row.findViewById(R.id.testandooo);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        li.setAdapter(adapter);
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        String dianome = "";
        int dia = c.get(c.DAY_OF_WEEK);
        switch(dia){
            case Calendar.SUNDAY: dianome = "Domingo";break;
            case Calendar.MONDAY: dianome = "Segunda";break;
            case Calendar.TUESDAY: dianome = "Terça";break;
            case Calendar.WEDNESDAY: dianome = "Quarta";break;
            case Calendar.THURSDAY: dianome = "Quinta";break;
            case Calendar.FRIDAY: dianome = "Sexta";break;
            case Calendar.SATURDAY: dianome = "Sábado";break;
        }
        // String entregador = "Quarta";
        //Toast.makeText(this, dianome, Toast.LENGTH_SHORT).show();
        Query query;
        dref = FirebaseDatabase.getInstance().getReference().child(semana);
        query = dref.orderByChild("dia").startAt(dianome).endAt(dianome + "\uf8ff");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String lojas = dataSnapshot.child("loja").getValue(String.class);
                final String nomees = dataSnapshot.child("vendedor").getValue(String.class);

                DatabaseReference userName = FirebaseDatabase.getInstance().getReference().child("usuario");
                userName.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String usuario = dataSnapshot.child("nome").getValue(String.class);

                        if (nomees.toString().equals(usuario)){
                            list.add(lojas);
                            adapter.notifyDataSetChanged();
                            li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    String noome = String.valueOf(parent.getItemAtPosition(position));
                                    Intent checkin = new Intent(TelaFornecedores.this, CheckIn.class);
                                    checkin.putExtra("nomemotorista", noome);
                                    startActivity(checkin);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        alert.setView(row);
        AlertDialog dialog = alert.create();
        dialog.show();



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

