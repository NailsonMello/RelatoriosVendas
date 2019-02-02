package roma.relatorio.nailson.relatoriosvendas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    private DatabaseReference mDatabaseFornecedores, dref;
    private FirebaseAuth mAuth;
    private RecyclerView listaforne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Lista de itens");


        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(Catalogo.this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseFornecedores = FirebaseDatabase.getInstance().getReference().child("ListaFornecedores");
        dref = FirebaseDatabase.getInstance().getReference().child("ListaFornecedores");
        listaforne = (RecyclerView) findViewById(R.id.listaCatalogo);
        listaforne.setLayoutManager(new LinearLayoutManager(this));

        carregarLista();
    }

    private void carregarLista() {

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        AlertDialog.Builder alert = new AlertDialog.Builder(Catalogo.this);
        alert.setTitle("Escolha um Vendedor");
        alert.setIcon(R.drawable.ic_user);
        View row = getLayoutInflater().inflate(R.layout.lista_users, null);
        final ListView li = (ListView)row.findViewById(R.id.testandooo);
        adapter = new ArrayAdapter<String>(Catalogo.this, android.R.layout.simple_list_item_1, list);
        li.setAdapter(adapter);


        alert.setView(row);
        final AlertDialog dialog = alert.create();
        Query query;
        dref = FirebaseDatabase.getInstance().getReference().child("ListaFornecedores");
        //query = dref.orderByChild("funcao").startAt(entregador).endAt(entregador + "\uf8ff");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String nome = dataSnapshot.child("fornecedor").getValue(String.class);
                list.add(nome);
                adapter.notifyDataSetChanged();
                li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String noome = String.valueOf(parent.getItemAtPosition(position));
                        catalog(noome);
                        dialog.dismiss();
                        //finish();
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

        dialog.show();
    }

    public void catalog(String nomeUsers){
        Query query;

        mDatabaseFornecedores = FirebaseDatabase.getInstance().getReference().child("Catalogo");
        query = mDatabaseFornecedores.orderByChild("fornecedor").startAt(nomeUsers).endAt(nomeUsers + "\uf8ff");
        FirebaseRecyclerAdapter<ModelCatalogo, Catalogo.AnuncioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelCatalogo, Catalogo.AnuncioViewHolder>
                (
                        ModelCatalogo.class,
                        R.layout.layout_catalogo,
                        Catalogo.AnuncioViewHolder.class,
                        query

                ){

            @Override
            protected void populateViewHolder(final Catalogo.AnuncioViewHolder viewHolder, final ModelCatalogo model, int position) {

                final String usuario = getRef(position).getKey();
                viewHolder.setProduto(model.getProduto());
                viewHolder.setPreco(String.valueOf(model.getPreco()));
                viewHolder.setImagem(getApplicationContext(), model.getImagem());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent img = new Intent(Catalogo.this, ImagemProduto.class);
                        img.putExtra("httpImg", model.getImagem());
                        startActivity(img);

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
        public void setProduto(String produto){

            TextView txt = (TextView)mView.findViewById(R.id.txtProduto);
            txt.setText(produto);
        }
        public void setFornecedor(String fornecedor){

            TextView txt = (TextView)mView.findViewById(R.id.txtFornecedor);
            txt.setText(fornecedor);
        }
        public void setPreco(String preco){

            TextView txt = (TextView)mView.findViewById(R.id.txtPreco);
            txt.setText(preco);
        }
        public void setImagem(Context context, String imagem){

            ImageView nomeUser = (ImageView) mView.findViewById(R.id.imgProduto);
            Picasso.with(context).load(imagem).placeholder(R.mipmap.icon).into(nomeUser);

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Catalogo.this, MainActivity.class));
        finish();

    }
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(Catalogo.this, Catalogo.class));
        finish();
        return true;
    }

}
