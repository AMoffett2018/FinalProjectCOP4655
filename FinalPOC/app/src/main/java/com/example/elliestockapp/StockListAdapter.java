package com.example.elliestockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StockListAdapter extends BaseAdapter {
    Context context;
    String[] sAbb;
    String[] sName;
    String[] sPrice;
    String[] sChange;
    LayoutInflater inf;

    public StockListAdapter(Context applicationContext, String[] sAbb, String[] sName, String[] sPrice, String[] sChange) {
        this.context = applicationContext;
        this.sAbb = sAbb;
        this.sName = sName;
        this.sPrice = sPrice;
        this.sChange = sChange;
        inf = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return sAbb.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inf.inflate(R.layout.stock_listview, null);
        TextView stockName = view.findViewById(R.id.stockName);
        TextView stockAbb = view.findViewById(R.id.stockAbb);
        TextView stockPrice = view.findViewById(R.id.stockPrice);
        TextView stockChange = view.findViewById(R.id.stockChange);
        stockAbb.setText(sName[i]);
        stockPrice.setText(sPrice[i]);
        stockChange.setText(sChange[i]);
        return view;
    }
}
