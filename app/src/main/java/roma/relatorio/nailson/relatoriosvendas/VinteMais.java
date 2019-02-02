package roma.relatorio.nailson.relatoriosvendas;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Map;

public class VinteMais extends AppCompatActivity {
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser,  count;
    private FirebaseAuth mAuth;
    private RecyclerView listafornecedor;
    private boolean statusBtn = false;
    String nomeT;
    private MenuItem cadastro;
    private TextView statusTorrada, statuschips, statusBisc, nome;

    int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vinte_mais);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("20+");

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(VinteMais.this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            dref = FirebaseDatabase.getInstance().getReference().child("usuario").child(mAuth.getCurrentUser().getUid());
            nomeT = getIntent().getExtras().getString("nomeT");
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        String no = dataSnapshot.child("nome").getValue().toString();
                        if (!nomeT.toString().equals(no)){
                            carregarAgora();
                        }else {
                            carregarUser();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {

            startActivity(new Intent(VinteMais.this, InicioLogin.class));
        }

        listafornecedor = (RecyclerView) findViewById(R.id.listafornecedor);
        listafornecedor.setLayoutManager(new LinearLayoutManager(this));


    }


    private void carregarUser(){

        Query query;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vinte_mais");
        query = mDatabase.orderByChild("idUser").startAt(mAuth.getCurrentUser().getUid()).endAt(mAuth.getCurrentUser().getUid() + "\uf8ff");

        FirebaseRecyclerAdapter<VendasModel, VinteMais.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<VendasModel, VinteMais.AnuncioViewHolder>(
                VendasModel.class,
                R.layout.layout_vinte_mais,
                VinteMais.AnuncioViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(VinteMais.AnuncioViewHolder viewHolder, final VendasModel model, int position) {

                viewHolder.setCliente(model.getCliente());
                viewHolder.setTorrada(model.getTorrada());
                viewHolder.setchips(model.getChips());
                viewHolder.setBiscoitoTapioca(model.getBisc_tapioca());

                final String chave_ = getRef(position).getKey();
                viewHolder.btnChips.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusBtn = true;
                        mDatabase.child(chave_).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String tt = dataSnapshot.child("chips").getValue().toString();
                                String user = dataSnapshot.child("idUser").getValue().toString();
                                if (statusBtn){
                                            if (tt.toString().equals("não")) {
                                                mDatabase.child(chave_).child("chips").setValue("sim").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });
                                                mDatabase.child(chave_).child("chipsValor").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });
                                            } else {
                                                mDatabase.child(chave_).child("chips").setValue("não").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });

                                                mDatabase.child(chave_).child("chipsValor").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });

                                            }
                                            statusBtn = false;


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                viewHolder.btnTorrada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusBtn = true;
                        mDatabase.child(chave_).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String tt = dataSnapshot.child("torrada").getValue().toString();
                                if (statusBtn){

                                    if (tt.toString().equals("não")) {
                                        mDatabase.child(chave_).child("torrada").setValue("sim").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });
                                        mDatabase.child(chave_).child("torradaValor").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });
                                    }else {
                                        mDatabase.child(chave_).child("torrada").setValue("não").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });

                                        mDatabase.child(chave_).child("torradaValor").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });

                                    }
                                    statusBtn = false;

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                viewHolder.btnBiscTapioca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusBtn = true;
                        mDatabase.child(chave_).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String tt = dataSnapshot.child("bisc_tapioca").getValue().toString();
                                if (statusBtn){

                                    if (tt.toString().equals("não")) {
                                        mDatabase.child(chave_).child("bisc_tapioca").setValue("sim").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });
                                        mDatabase.child(chave_).child("bisc_tapiocaValor").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });
                                    }else {
                                        mDatabase.child(chave_).child("bisc_tapioca").setValue("não").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });

                                        mDatabase.child(chave_).child("bisc_tapiocaValor").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                }
                                            }
                                        });
                                    }
                                    statusBtn = false;

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };
        listafornecedor.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView btnTorrada;
        ImageView btnChips;
        ImageView btnBiscTapioca;

        public AnuncioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btnChips = (ImageView)mView.findViewById(R.id.statusChips);
            btnTorrada = (ImageView)mView.findViewById(R.id.statusTorrada);
            btnBiscTapioca = (ImageView)mView.findViewById(R.id.statusBisc);
        }

        public void setCliente(String cliente){
            TextView clienteM = (TextView)mView.findViewById(R.id.nomeCLiente);
            clienteM.setText(cliente);
        }

        public void setTorrada(String torrada){
            TextView clienteM = (TextView)mView.findViewById(R.id.torrada);
            clienteM.setText(torrada);

            ImageView img = (ImageView)mView.findViewById(R.id.statusTorrada);
            if (clienteM.getText().toString().equals("sim")){
               img.setImageResource(R.drawable.ic_sim);
            }else {
                img.setImageResource(R.drawable.ic_nao);
            }
        }

        public void setchips(String chips){
            TextView clienteM = (TextView)mView.findViewById(R.id.chips);
            clienteM.setText(chips);
            ImageView img = (ImageView)mView.findViewById(R.id.statusChips);
            if (clienteM.getText().toString().equals("sim")){
                img.setImageResource(R.drawable.ic_sim);
            }else {
                img.setImageResource(R.drawable.ic_nao);
            }
        }

        public void setBiscoitoTapioca(String bisc_tapioca){
            TextView clienteM = (TextView)mView.findViewById(R.id.bisc_tapioca);
            clienteM.setText(bisc_tapioca);
            ImageView img = (ImageView)mView.findViewById(R.id.statusBisc);
            if (clienteM.getText().toString().equals("sim")){
                img.setImageResource(R.drawable.ic_sim);
            }else {
                img.setImageResource(R.drawable.ic_nao);
            }
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
    private void carregarAgora() {
        Query query;
        mDataUser = FirebaseDatabase.getInstance().getReference().child("vinte_mais");
        query = mDataUser.orderByChild("vendedor").startAt(nomeT).endAt(nomeT + "\uf8ff");

        FirebaseRecyclerAdapter<VendasModel, VinteMais.AnuncioViewHolderT> firebaseRecyclerAdapterr = new FirebaseRecyclerAdapter<VendasModel, VinteMais.AnuncioViewHolderT>(
                VendasModel.class,
                R.layout.layout_vinte_mais,
                VinteMais.AnuncioViewHolderT.class,
                query
        ) {
            @Override
            protected void populateViewHolder(VinteMais.AnuncioViewHolderT viewHolder, final VendasModel model, int position) {

                viewHolder.setCliente(model.getCliente());
                viewHolder.setTorrada(model.getTorrada());
                viewHolder.setchips(model.getChips());
                viewHolder.setBiscoitoTapioca(model.getBisc_tapioca());

                final String chave_ = getRef(position).getKey();

            }
        };
        listafornecedor.setAdapter(firebaseRecyclerAdapterr);
    }

    public static class AnuncioViewHolderT extends RecyclerView.ViewHolder {

        View mView;
        ImageView btnTorrada;
        ImageView btnChips;
        ImageView btnBiscTapioca;

        public AnuncioViewHolderT(View itemView) {
            super(itemView);
            mView = itemView;
            btnChips = (ImageView)mView.findViewById(R.id.statusChips);
            btnTorrada = (ImageView)mView.findViewById(R.id.statusTorrada);
            btnBiscTapioca = (ImageView)mView.findViewById(R.id.statusBisc);
        }

        public void setCliente(String cliente){
            TextView clienteM = (TextView)mView.findViewById(R.id.nomeCLiente);
            clienteM.setText(cliente);
        }

        public void setTorrada(String torrada){
            TextView clienteM = (TextView)mView.findViewById(R.id.torrada);
            clienteM.setText(torrada);

            ImageView img = (ImageView)mView.findViewById(R.id.statusTorrada);
            if (clienteM.getText().toString().equals("sim")){
                img.setImageResource(R.drawable.ic_sim);
            }else {
                img.setImageResource(R.drawable.ic_nao);
            }
        }

        public void setchips(String chips){
            TextView clienteM = (TextView)mView.findViewById(R.id.chips);
            clienteM.setText(chips);
            ImageView img = (ImageView)mView.findViewById(R.id.statusChips);
            if (clienteM.getText().toString().equals("sim")){
                img.setImageResource(R.drawable.ic_sim);
            }else {
                img.setImageResource(R.drawable.ic_nao);
            }
        }

        public void setBiscoitoTapioca(String bisc_tapioca){
            TextView clienteM = (TextView)mView.findViewById(R.id.bisc_tapioca);
            clienteM.setText(bisc_tapioca);
            ImageView img = (ImageView)mView.findViewById(R.id.statusBisc);
            if (clienteM.getText().toString().equals("sim")){
                img.setImageResource(R.drawable.ic_sim);
            }else {
                img.setImageResource(R.drawable.ic_nao);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fhom, menu);

        cadastro = menu.findItem(R.id.action_vinte_mais);


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


            carregarDialog();
        }


        return super.onOptionsItemSelected(item);
    }

    private void carregarDialog() {
        count = FirebaseDatabase.getInstance().getReference().child("vinte_mais");
        Query query;
        query = count.orderByChild("vendedor").equalTo(nomeT);

        final AlertDialog.Builder alerta = new AlertDialog.Builder(VinteMais.this);
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.vinte_mais, null);
        alerta.setView(viewAlerta);
        statusTorrada = (TextView)viewAlerta.findViewById(R.id.statusTorrada);
        statuschips = (TextView)viewAlerta.findViewById(R.id.statusChips);
        statusBisc = (TextView)viewAlerta.findViewById(R.id.statusBisc);
        nome = (TextView)viewAlerta.findViewById(R.id.nome);
        nome.setText("Positivação "+nomeT);


        query.addValueEventListener(new ValueEventListener() {
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
                        statusTorrada.setText(String.valueOf(somaT)+" de 20");
                        // Toast.makeText(ContadorVinteMais.this, "Torradas: " + String.valueOf(somaT), Toast.LENGTH_SHORT).show();
                    }
                    if (ch.toString().equals(String.valueOf(1))){

                        statuschips.setText(String.valueOf(somaC)+" de 20");
                        //  Toast.makeText(ContadorVinteMais.this, "Chips: " + String.valueOf(somaC), Toast.LENGTH_SHORT).show();
                    }
                    if (bi.toString().equals(String.valueOf(1))){

                        statusBisc.setText(String.valueOf(somaB)+" de 20");
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
