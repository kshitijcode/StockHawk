package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

public class QuoteWidgetRemoteViewsService extends RemoteViewsService {

    public final String LOG_TAG = QuoteWidgetRemoteViewsService.class.getSimpleName();


    private static final String[] FORECAST_COLUMNS = {
            QuoteColumns._ID,
            QuoteColumns.BIDPRICE,
            QuoteColumns.CHANGE,
            QuoteColumns.CREATED,
            QuoteColumns.ISCURRENT,
            QuoteColumns.ISUP, QuoteColumns.PERCENT_CHANGE, QuoteColumns.SYMBOL
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                Uri stockDetails = QuoteProvider.Quotes.CONTENT_URI;

                data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        FORECAST_COLUMNS, null,
                        null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_collection_item);

                views.setTextViewText(R.id.stock_symbol, data.getString(7));
                views.setTextViewText(R.id.change, data.getString(2));


                return views;
            }


            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_collection_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return Integer.parseInt(data.getString(0));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}