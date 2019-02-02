package roma.relatorio.nailson.relatoriosvendas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ContadorVinteMaisVendedor extends AppCompatActivity {

    private DatabaseReference mDatabase,  count;
    private TextView statusTorrada, statuschips, statusBisc, nome;
    private String vendedor = null;
    private MenuItem cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_vinte_mais_vendedor);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);

        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(ContadorVinteMaisVendedor.this);
        vendedor = getIntent().getExtras().getString("idForne");
        act.setTitle("Geral "+vendedor);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuario");

        statusTorrada = (TextView)findViewById(R.id.statusTorrada);
        statuschips = (TextView)findViewById(R.id.statusChips);
        statusBisc = (TextView)findViewById(R.id.statusBisc);
        nome = (TextView)findViewById(R.id.nome);
        nome.setText("Positivação "+vendedor);
        mostrarPos();

    }

    private void mostrarPos() {
        count = FirebaseDatabase.getInstance().getReference().child("vinte_mais");
        Query query;
        query = count.orderByChild("vendedor").equalTo(vendedor);
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
    }

    @Override
    public void onBackPressed() {
        Intent telaAnuncio = new Intent(ContadorVinteMaisVendedor.this, MainActivity.class);
        startActivity(telaAnuncio);
        finish();

    }
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(ContadorVinteMaisVendedor.this, MainActivity.class));
        finish();
        return true;
    }

}

