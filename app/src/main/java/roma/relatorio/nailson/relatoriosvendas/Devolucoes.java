package roma.relatorio.nailson.relatoriosvendas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.NumberFormat;
import java.util.Calendar;

public class Devolucoes extends AppCompatActivity {
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser, count;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private MenuItem cadastro, vinte;
    private String vendedor = null;
    private RecyclerView listafaturados;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucoes);

        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Devoluções");
        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(Devolucoes.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        vendedor = getIntent().getExtras().getString("idForne");
        mProgress = new ProgressDialog(this);

        listafaturados = (RecyclerView) findViewById(R.id.listaDevolvidos);
        listafaturados.setLayoutManager(new LinearLayoutManager(this));
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = (String.format("%02d/%02d/%d", day,+month,year));

                queryPonto(date);

            }
        };
    }

    private void queryPonto(String date) {
        mProgress.setMessage("Carregando...");
        mProgress.show();
        Query query;
        mDataUser = FirebaseDatabase.getInstance().getReference().child("usuario");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("devolucoes");
        query = mDatabase.orderByChild("data_emissao").startAt(date+"'").endAt(date+"'" + "\uf8ff");
        FirebaseRecyclerAdapter<FaturadosModel, Devolucoes.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FaturadosModel, Devolucoes.AnuncioViewHolder>
                (
                        FaturadosModel.class,
                        R.layout.layout_faturados,
                        Devolucoes.AnuncioViewHolder.class,
                        query

                ){

            @Override
            protected void populateViewHolder(final Devolucoes.AnuncioViewHolder viewHolder, final FaturadosModel model, int position) {

                final String usuario = getRef(position).getKey();

                if (vendedor.toString().equals(model.getVendedor())) {
                    viewHolder.setNome(model.getCliente());
                    viewHolder.setValor(model.getValor());
                }else {
                    viewHolder.setLayoutInvisivel();
                }
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Devolucoes.this);
                        builder.setTitle("Nota: "+String.valueOf(model.getNfe()));
                        builder.setMessage(model.getCliente()+"\n\n"+model.getData_emissao());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });

                mProgress.dismiss();

            }

        };
        listafaturados.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query;
        mProgress.setMessage("Carregando...");
        mProgress.show();
        mDataUser = FirebaseDatabase.getInstance().getReference().child("usuario");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("devolucoes");
        query = mDatabase.orderByChild("vendedor").startAt(vendedor).endAt(vendedor + "\uf8ff");
        FirebaseRecyclerAdapter<FaturadosModel, Devolucoes.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FaturadosModel, Devolucoes.AnuncioViewHolder>
                (
                        FaturadosModel.class,
                        R.layout.layout_faturados,
                        Devolucoes.AnuncioViewHolder.class,
                        query

                ){

            @Override
            protected void populateViewHolder(final Devolucoes.AnuncioViewHolder viewHolder, final FaturadosModel model, int position) {

                final String usuario = getRef(position).getKey();

                if (vendedor.toString().equals(model.getVendedor())) {
                    viewHolder.setNome(model.getCliente());
                    viewHolder.setValor(model.getValor());
                }else {
                    viewHolder.setLayoutInvisivel();
                }
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Devolucoes.this);
                        builder.setTitle("Nota: "+String.valueOf(model.getNfe()));
                        builder.setMessage(model.getCliente()+"\n\n"+model.getData_emissao());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });

                if (model.getVendedor().toString().equals("")){
                    mProgress.dismiss();
                    Toast.makeText(Devolucoes.this, "Sem devoluções", Toast.LENGTH_SHORT).show();

                }
                mProgress.dismiss();

            }

        };
        listafaturados.setAdapter(firebaseRecyclerAdapter);

    }


    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public AnuncioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNome(String cliente){

            TextView nomeUser = (TextView)mView.findViewById(R.id.nomeClienteFaturado);
            nomeUser.setText(cliente);


        }

        public void setValor(double valor){
            TextView nomeUser = (TextView)mView.findViewById(R.id.valorClienteFaturado);
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            Double faltaVendedor = Double.parseDouble(String.valueOf(valor));
            String formatado3 = nf.format(faltaVendedor);
            nomeUser.setText(formatado3);

        }
        public void setLayout(){
            LinearLayout cardView = (LinearLayout)mView.findViewById(R.id.cardRota);
            cardView.setVisibility(View.VISIBLE);
            final LinearLayout linearLayout =(LinearLayout)mView.findViewById(R.id.telarota);
            linearLayout.setVisibility(View.VISIBLE);


        }
        public void setLayoutInvisivel(){
            final LinearLayout linearLayout =(LinearLayout)mView.findViewById(R.id.telarota);
            linearLayout.setVisibility(View.GONE);

            LinearLayout cardView = (LinearLayout)mView.findViewById(R.id.cardRota);
            cardView.setVisibility(View.GONE);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fat_dev, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_data){

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(Devolucoes.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


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
