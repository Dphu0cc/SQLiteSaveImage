package com.example.sqlitesaveimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DoVatAdapter extends BaseAdapter {

    private Context context;
    private int layout;

    private List<DoVat> doVatList;

    public DoVatAdapter(Context context, int layout, List<DoVat> doVatList) {
        this.context = context;
        this.layout = layout;
        this.doVatList = doVatList;
    }



    @Override
    public int getCount() {
        return doVatList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView txtTen, txtMoTa;
        ImageView imgHinh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtTen = view.findViewById(R.id.textViewTen);
            holder.txtMoTa = view.findViewById(R.id.textViewMoTa);
            holder.imgHinh = view.findViewById(R.id.imageHinhCustom);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        DoVat doVat = doVatList.get(i);
        holder.txtTen.setText(doVat.getTen());
        holder.txtMoTa.setText(doVat.getMota());

        // chuyển byte[] sang bitmap
        byte[] hinhAnh = doVat.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);

        holder.imgHinh.setImageBitmap(bitmap);

        return view;
    }
}
