package maha.abdelshafy.com.peerschat.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

import maha.abdelshafy.com.peerschat.R;
import maha.abdelshafy.com.peerschat.model.User;
import maha.abdelshafy.com.peerschat.ui.MainActivity;


/**
 * Implementation of App Widget functionality.
 */
public class PeersChatWidget extends AppWidgetProvider {

    private static final String ACTION_BROADCASTWIIDGET = "ACTION_BROADCASTWIIDGET";
    RemoteViews views;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {


        //Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.peers_chat_widget);
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
        new DownloadBitmap(views).execute("Peers Chat");

        views.setOnClickPendingIntent(R.id.widget_title, mainPendingIntent);
        Intent intent = new Intent(context, PeersChatWidget.class);
        intent.setAction(ACTION_BROADCASTWIIDGET);

        context.sendBroadcast(intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_BROADCASTWIIDGET.equals(intent.getAction())) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.peers_chat_widget);
            views.setTextViewText(R.id.admin_information, getAdminInformation());
            ComponentName appWidget = new ComponentName(context, PeersChatWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(appWidget, views);
        }
    }

    private String getAdminInformation() {

        DatabaseReference mRef =
                FirebaseDatabase.getInstance().getReference().child("users");
        mRef.orderByChild("email").equalTo("mahaabdelshafy@yahoo.com").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    Log.d("user:", user.getName());
                    views.setTextViewText(R.id.admin_information, user.getName() + '\n' + user.getEmail());
                } catch (Throwable b) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "load message:onCancelled");
            }
        });
        return "";
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public class DownloadBitmap extends AsyncTask<String, Void, Bitmap> {


        private RemoteViews views;

        private String url = "http://findicons.com/files/icons/2101/ciceronian/59/photos.png";

        public DownloadBitmap(RemoteViews views) {
            this.views = views;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                InputStream in = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;

            } catch (Exception e) {
                Log.e("ImageDownload", "Download failed: " + e.getMessage());
            }
            return null;
        }

    }

}