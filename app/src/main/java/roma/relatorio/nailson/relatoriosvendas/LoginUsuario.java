package roma.relatorio.nailson.relatoriosvendas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginUsuario extends AppCompatActivity {

    private ProgressDialog mLoginProgress;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText edtSenha, edtEmail;
    private Button entrar, criarConta, RecuperarSenha;
    private final String TOPIC = "RomaBahia";
    private String emailEnviado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Tela de Login");


        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(LoginUsuario.this);
        mLoginProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuario");
        emailEnviado = getIntent().getExtras().getString("email");


        edtEmail = (EditText) findViewById(R.id.userEmail);
        edtSenha = (EditText) findViewById(R.id.userSenha);
        entrar = (Button) findViewById(R.id.btnEntrar);
        criarConta = (Button) findViewById(R.id.btnCriarConta);
        RecuperarSenha = (Button) findViewById(R.id.btnEsqSenha);
        criarConta.setEnabled(true);
        RecuperarSenha.setEnabled(false);
        //edtEmail.setText(emailEnviado);
        carregarEmail();
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        if (mUser != null) {


        }
        criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUsuario.this, CadastroUsuario.class));
                finish();
            }
        });

        RecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void carregarEmail() {
        Query query;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuario");
        query = mDatabase.orderByChild("nome").equalTo(emailEnviado);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String emm = postSnapshot.child("email").getValue().toString();

                        edtEmail.setText(emm);

                    }
                   }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void checkLogin(){

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)){
            mLoginProgress.setTitle("Entrando...");
            mLoginProgress.setMessage("Aguarde enquanto verificamos suas credenciais.");
            mLoginProgress.setCanceledOnTouchOutside(false);
            mLoginProgress.show();


            checarUsuer(email, senha);


        }



    }
    public void checarUsuer(String email, String senha){

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String online = mAuth.getCurrentUser().getUid();
                    String DeviceToken = FirebaseInstanceId.getInstance().getToken();

                    mDatabase.child(online).child("device_token").setValue(DeviceToken)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        mLoginProgress.dismiss();
                                        Intent telaInicial = new Intent(LoginUsuario.this, MainActivity.class);
                                        telaInicial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(telaInicial);
                                        finish();
                                    }

                                }
                            });

                }else {
                    mLoginProgress.hide();
                    Toast.makeText(LoginUsuario.this, "" +
                                    "E-mail ou senha incorreto, Por favor tente novamente",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(LoginUsuario.this, InicioLogin.class));
        finish();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent telaAnuncio = new Intent(LoginUsuario.this, InicioLogin.class);
        startActivity(telaAnuncio);
        finish();

    }
}

