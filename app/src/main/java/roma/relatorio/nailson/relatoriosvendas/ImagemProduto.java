package roma.relatorio.nailson.relatoriosvendas;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImagemProduto extends AppCompatActivity {

    private PhotoView photoView;
    String mHttpImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem_produto);
        ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle("Imagem Ação");

        mHttpImg = getIntent().getExtras().getString("httpImg");
        photoView = (PhotoView)findViewById(R.id.photo_view);
        Picasso.with(ImagemProduto.this).load(mHttpImg).placeholder(R.mipmap.icon).into(photoView);


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

}
