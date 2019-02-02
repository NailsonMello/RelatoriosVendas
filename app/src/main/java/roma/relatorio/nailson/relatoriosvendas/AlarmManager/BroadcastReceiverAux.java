package roma.relatorio.nailson.relatoriosvendas.AlarmManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import roma.relatorio.nailson.relatoriosvendas.InicioLogin;
import roma.relatorio.nailson.relatoriosvendas.MainActivity;
import roma.relatorio.nailson.relatoriosvendas.R;
import roma.relatorio.nailson.relatoriosvendas.VinteMais;

public class BroadcastReceiverAux extends BroadcastReceiver {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    public void onReceive(final Context context, Intent intent) {
        /*
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("usuario");
        Log.i("Script", "-> Alarme");
        mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String func = dataSnapshot.child("funcao").getValue().toString();

                    if (func.toString().equals("vendedor")) {
                        gerarNotificacao(context, new Intent(context, VinteMais.class), "Relatório de venda", "20+", "Clique para visualizar a venda de Biscoito de tapioca, chips e torrada Fhom");
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
      }


    public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);
        //RemoteViews lay = new RemoteViews(context.getPackageName(),R.layout.layout_noti);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.iconess);
        builder.setTicker(ticker);
        builder.setContentTitle(titulo);
        builder.setContentText(descricao);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.iconess));
        builder.setStyle(new NotificationCompat.BigTextStyle()
        .bigText(descricao));
        builder.setContentIntent(p);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.iconess, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        }
        catch(Exception e){}
    }
}
