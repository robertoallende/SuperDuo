package barqsoft.footballscores;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppWidget extends AppWidgetProvider {

    public Integer HOME_TEAM = 3;
    public Integer VISITOR_TEAM = 4;

    @Override
    public void onUpdate(Context context, AppWidgetManager mgr, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            String mainText = getMainText(context);
            if (mainText.isEmpty()) {
                String noMatches = context.getResources().getString(R.string.no_matches_today);
                views.setTextViewText(R.id.featured_match, noMatches);
                String empty = context.getResources().getString(R.string.empty);
                views.setTextViewText(R.id.more_app, empty);
                views.setTextViewText(R.id.today_title, empty);

            }
            else {
                String today = context.getResources().getString(R.string.today);
                views.setTextViewText(R.id.today_title, today);

                String moreOn = context.getResources().getString(R.string.more_app);
                views.setTextViewText(R.id.more_app, moreOn);

                views.setTextViewText(R.id.featured_match, mainText);
            }
            mgr.updateAppWidget(appWidgetId, views);
        }


    }

    private String getMainText(Context context) {
        String result = "";
        ContentResolver resolver =  context.getContentResolver();
        Uri uri = DatabaseContract.BASE_CONTENT_URI;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = format.format(new Date());
        String where = "date = " + todayDate;
        Uri qUri= Uri.parse(DatabaseContract.scores_table.buildScoreWithDate().toString());
        Cursor cursor = resolver.query(qUri,
                null,
                null,
                new String[]{todayDate},
                null);

        try {
            if (cursor == null) {
                return result;
            };
            int count = cursor.getCount();

            if (count != 0) {
                Integer position =  (int)(Math.random()*count);
                cursor.moveToPosition(position);
                result = cursor.getString(HOME_TEAM) + ' ' +
                         context.getResources().getString(R.string.versus)
                         + ' ' +
                         cursor.getString(VISITOR_TEAM);
            }
        } finally {
            cursor.close();
        }
        return result;
    }
}