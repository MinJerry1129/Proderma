package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.OneProductActivity;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    private ArrayList<Brand> mBrand;
    private Context mContext;

    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }
    public BrandAdapter(Context context, ArrayList<Brand> data) {
        mContext = context;
        mBrand = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_brand, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(mBrand.get(position).getmName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBrand.size();
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_brandname);
        }
    }
}