package roma.relatorio.nailson.relatoriosvendas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import roma.relatorio.nailson.relatoriosvendas.Acoes.AcoesVigentes;
import roma.relatorio.nailson.relatoriosvendas.RegistroPonto.CheckIn;
import roma.relatorio.nailson.relatoriosvendas.RegistroPonto.PontosRegistrados;

public class TelaInicial extends AppCompatActivity {

    private MenuItem pont;
    private String vendedor = null;
    private String userId = null;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser, count;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private CardView pedidos, pontos, cota_fornecedor,cota_geral, cadastro_cliente, campanha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        ActionBar act = getSupportActionBar();
        act.setTitle("Tela Inicial");

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(TelaInicial.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        vendedor = getIntent().getExtras().getString("idForne");
        userId = getIntent().getExtras().getString("user_id");

        pedidos = (CardView)findViewById(R.id.pedidos);
        pontos = (CardView)findViewById(R.id.pontos);
        cota_fornecedor = (CardView)findViewById(R.id.cota_fornecedor);
        cota_geral = (CardView)findViewById(R.id.cota_geral);
        cadastro_cliente = (CardView)findViewById(R.id.cadastro_cliente);
        campanha = (CardView)findViewById(R.id.campanhas);

        pontos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{"Registrar ponto", "Pontos registrados"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicial.this);

                builder.setTitle("Escolha Uma Opção");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Click Event for each item.
                        if(i == 0){
                            mostrarRoteiro();
                        }

                        if(i == 1){
                            Intent it = new Intent(TelaInicial.this, PontosRegistrados.class);
                            it.putExtra("nome", vendedor);
                            it.putExtra("user_id", userId);
                            startActivity(it);

                        }

                    }
                });

                builder.show();

            }
        });
        campanha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFornecedores();
            }
        });
        cadastro_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(TelaInicial.this);
                alerta.setTitle("OPS...");
                alerta.setIcon(android.R.drawable.ic_menu_info_details);
                alerta.setMessage("Cadastro de cliente em construção");
                alerta.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alerta.show();

            }
        });
        cota_geral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaInicial.this, VendaGeral.class);
                it.putExtra("idForne", vendedor);
                startActivity(it);
            }
        });
        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{"Pedidos faturados", "Pedidos devolvidos"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicial.this);

                builder.setTitle("Escolha Uma Opção");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Click Event for each item.
                        if(i == 0){
                            Intent it = new Intent(TelaInicial.this, Pedidos.class);
                            it.putExtra("idForne", vendedor);
                            startActivity(it);
                        }

                        if(i == 1){
                            Intent it = new Intent(TelaInicial.this, Devolucoes.class);
                            it.putExtra("idForne", vendedor);
                            startActivity(it);
                        }

                    }
                });

                builder.show();
            }
        });
        cota_fornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarForne();
            }
        });
        //Toast.makeText(this, "nome:"+vendedor, Toast.LENGTH_SHORT).show();

    }

    private void carregarForne() {
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        Query query;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Escolha um Fornecedor");
        View row = getLayoutInflater().inflate(R.layout.lista_motoristas, null);
        final ListView li = (ListView)row.findViewById(R.id.testandooo);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        li.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference().child("CotaFornecedor");
        query = dref.orderByChild("nome").startAt(vendedor).endAt(vendedor + "\uf8ff");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String nome = dataSnapshot.child("fornecedor").getValue(String.class);
                list.add(nome);
                adapter.notifyDataSetChanged();
                li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String noome = String.valueOf(parent.getItemAtPosition(position));
                        Intent motr = new Intent(TelaInicial.this, CotaFornecedor.class);
                        motr.putExtra("idForne", vendedor);
                        motr.putExtra("forne", noome);
                        startActivity(motr);

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

    private void carregarFornecedores() {
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        Query query;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Escolha um Fornecedor");
        View row = getLayoutInflater().inflate(R.layout.lista_motoristas, null);
        final ListView li = (ListView)row.findViewById(R.id.testandooo);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        li.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference().child("Campanhas");
        query = dref.orderByChild("nome").startAt(vendedor).endAt(vendedor + "\uf8ff");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String nome = dataSnapshot.child("fornecedor").getValue(String.class);
                list.add(nome);
                adapter.notifyDataSetChanged();
                li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String noome = String.valueOf(parent.getItemAtPosition(position));
                        Intent motr = new Intent(TelaInicial.this, Campanhas.class);
                        motr.putExtra("idForne", vendedor);
                        motr.putExtra("forne", noome);
                        startActivity(motr);

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
                Intent it = new Intent(TelaInicial.this, InicioLogin.class);
                startActivity(it);
                finish();
            }
        }else if (id == R.id.action_aulas){
            Intent it = new Intent(TelaInicial.this, Aulas.class);
            it.putExtra("idForne", vendedor);
            startActivity(it);
        }else if (id == R.id.action_faturados){
            Intent it = new Intent(TelaInicial.this, Pedidos.class);
            it.putExtra("idForne", vendedor);
            startActivity(it);
        }else if (id == R.id.action_devolucoes){
            Intent it = new Intent(TelaInicial.this, Devolucoes.class);
            it.putExtra("idForne", vendedor);
            startActivity(it);
        }else if (id == R.id.action_grafico){
            Intent it = new Intent(TelaInicial.this, GraficoVendas.class);
            it.putExtra("idForne", vendedor);
            startActivity(it);
        }else if (id == R.id.action_registro){
            Intent it = new Intent(TelaInicial.this, PontosRegistrados.class);
            it.putExtra("nome", vendedor);
            it.putExtra("user_id", userId);
            startActivity(it);
        }else if (id == R.id.action_ponto){
            mostrarRoteiro();

        }else if (id == R.id.action_acaoVigente){
            startActivity(new Intent(TelaInicial.this, AcoesVigentes.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarRoteiro() {

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
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
        android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();



    }

    private void mostrarLojas(String semana) {
        //Toast.makeText(this, semana, Toast.LENGTH_SHORT).show();
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
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
                                    Intent checkin = new Intent(TelaInicial.this, CheckIn.class);
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
        android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();



    }

}
