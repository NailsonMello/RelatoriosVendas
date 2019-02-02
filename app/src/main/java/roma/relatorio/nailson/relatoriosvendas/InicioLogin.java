package roma.relatorio.nailson.relatoriosvendas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InicioLogin extends AppCompatActivity {

    private Spinner spnUser;
    private Button seguir;
    String email;
    private DatabaseReference mDatabase;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_login);
        ActionBar act = getSupportActionBar();
        act.setTitle("Escolha o usuario");


        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(InicioLogin.this);
        progressBar = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuario");
        spnUser = (Spinner)findViewById(R.id.spnUser);
        seguir = (Button)findViewById(R.id.seguir);

        seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // seguirLogin();
                userEmail();
            }
        });
        progressBar.setMessage("Carregando...");
        progressBar.show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  String nome = dataSnapshot.child("nome").getValue().toString();

                //String[] list = getResources().getStringArray(R.array.usuarios);
                //ArrayAdapter<String> adapterestado = new ArrayAdapter<String>(InicioLogin.this, R.layout.spinnerlayout, R.id.txt, Collections.singletonList(nome));
               // spnUser.setAdapter(adapterestado);

                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nome").getValue(String.class);
                    areas.add(areaName);
                }

               // Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(InicioLogin.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnUser.setAdapter(areasAdapter);

                progressBar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

   /* private void seguirLogin() {

        if (spnUser.getSelectedItem().toString().equals("Nailson")) {
            email = "noslianmmello@gmail.com";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Claudia")) {
            email = "vendasba.rota11@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Mauro")) {
            email = "mauro@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Cassiara")) {
            email = "cassiara@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Magali")) {
            email = "gestor2.ba@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Sheila")) {
            email = "gestor1.ba@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Rosangela")) {
            email = "@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Patricia")) {
            email = "@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Joise")) {
            email = "@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Cristiane")) {
            email = "@romadistribuidora.com.br";
            userEmail();

        }else if (spnUser.getSelectedItem().toString().equals("Diana")) {
            email = "@romadistribuidora.com.br";
            userEmail();

        }
    }*/

    private void userEmail() {
        Intent enviarEmail = new Intent(InicioLogin.this, LoginUsuario.class);
        enviarEmail.putExtra("email", spnUser.getSelectedItem().toString());
        startActivity(enviarEmail);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
