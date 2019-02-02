package roma.relatorio.nailson.relatoriosvendas;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GraficoVendas extends AppCompatActivity {

    private HorizontalBarChart barChart;
    private BarData barData;
    BarEntry barEntry1,barEntry2,barEntry3,barEntry4,barEntry5,barEntry6,barEntry7,barEntry8,barEntry9,barEntry10,barEntry11,barEntry12;
    ArrayList<BarDataSet> barDataSets;
    ArrayList<BarEntry> barEntries;
    BarDataSet barDataSet;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase, dref;
    private DatabaseReference mDataUser, count;
    private FirebaseAuth mAuth;
    private FirebaseUser user, mUser;
    private MenuItem cadastro, vinte;
    private String vendedor = null;
    String janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_vendas);


        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Grafico de vendas");
        DatabaseUtil.getDatabase();
        FirebaseApp.initializeApp(GraficoVendas.this);
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        user = mAuth.getCurrentUser();
        vendedor = getIntent().getExtras().getString("idForne");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("DesempenhoAnual").child(vendedor);

        barChart = (HorizontalBarChart)findViewById(R.id.barchart);
        carregarV();

    }

    private void carregarV() {

        mProgress.setMessage("Construindo grafico...");
        mProgress.show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue().toString();
                janeiro = dataSnapshot.child("janeiro").getValue().toString();
                fevereiro = dataSnapshot.child("fevereiro").getValue().toString();
                marco = dataSnapshot.child("marco").getValue().toString();
                abril = dataSnapshot.child("abril").getValue().toString();
                maio = dataSnapshot.child("maio").getValue().toString();
                junho = dataSnapshot.child("junho").getValue().toString();
                julho = dataSnapshot.child("julho").getValue().toString();
                agosto = dataSnapshot.child("agosto").getValue().toString();
                setembro = dataSnapshot.child("setembro").getValue().toString();
                outubro = dataSnapshot.child("outubro").getValue().toString();
                novembro = dataSnapshot.child("novembro").getValue().toString();
                dezembro = dataSnapshot.child("dezembro").getValue().toString();

                barData = new BarData(getXValues(),getBarValues(janeiro, fevereiro, marco, abril,
                        maio, junho, julho, agosto, setembro, outubro, novembro, dezembro));
                barData.setValueTextColor(Color.YELLOW);
                barChart.setData(barData);
                barChart.setBackgroundColor(Color.GRAY);
                barChart.getXAxis().setTextColor(Color.WHITE);
                barChart.setDescription("");
                barChart.setDescriptionTextSize(12);
                barChart.setGridBackgroundColor(Color.DKGRAY);
                barChart.animateXY(2000, 2000);
                barChart.invalidate();
                mProgress.dismiss();
                // Toast.makeText(GraficoVendas.this, jan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<BarDataSet> getBarValues(String janeiro, String fevereiro, String marco, String abril,
                                               String maio, String junho, String julho, String agosto, String setembro,
                                               String outubro, String novembro, String dezembro){

        barEntries = new ArrayList<>();

        barEntry1  = new BarEntry(Float.valueOf(janeiro).floatValue(), 0);
        barEntry2  = new BarEntry(Float.valueOf(fevereiro).floatValue(),1);
        barEntry3  = new BarEntry(Float.valueOf(marco).floatValue(),2);
        barEntry4  = new BarEntry(Float.valueOf(abril).floatValue(),3);
        barEntry5  = new BarEntry(Float.valueOf(maio).floatValue(),4);
        barEntry6  = new BarEntry(Float.valueOf(junho).floatValue(),5);
        barEntry7  = new BarEntry(Float.valueOf(julho).floatValue(),6);
        barEntry8  = new BarEntry(Float.valueOf(agosto).floatValue(),7);
        barEntry9  = new BarEntry(Float.valueOf(setembro).floatValue(),8);
        barEntry10 = new BarEntry(Float.valueOf(outubro).floatValue(),9);
        barEntry11 = new BarEntry(Float.valueOf(novembro).floatValue(),10);
        barEntry12 = new BarEntry(Float.valueOf(dezembro).floatValue(),11);

        barEntries.add(barEntry1);
        barEntries.add(barEntry2);
        barEntries.add(barEntry3);
        barEntries.add(barEntry4);
        barEntries.add(barEntry5);
        barEntries.add(barEntry6);
        barEntries.add(barEntry7);
        barEntries.add(barEntry8);
        barEntries.add(barEntry9);
        barEntries.add(barEntry10);
        barEntries.add(barEntry11);
        barEntries.add(barEntry12);

        barDataSet = new BarDataSet(barEntries, "Vendas do ano");
        barDataSet.setColor(Color.BLUE);
        barDataSets = new ArrayList<>();
        barDataSets.add(barDataSet);

        return barDataSets;
    }
    private ArrayList<String> getXValues(){
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add("Jan");
        xValues.add("Fev");
        xValues.add("Mar");
        xValues.add("Abr");
        xValues.add("Mai");
        xValues.add("Jun");
        xValues.add("Jul");
        xValues.add("Ago");
        xValues.add("Set");
        xValues.add("Out");
        xValues.add("Nov");
        xValues.add("Dez");
        return xValues;
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
