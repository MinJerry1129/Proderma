package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.OneClinicActivity;
import com.mobiledevteam.proderma.home.OneProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeClinicAdapter extends RecyclerView.Adapter<HomeClinicAdapter.ViewHolder> {
    private ArrayList<HomeClinic> mClinic;
    private Context mContext;

    public HomeClinicAdapter(Context context, ArrayList<HomeClinic> data) {
        mContext = context;
        mClinic = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.itme_home_clinic, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        Glide.with(mContext)
//                .asBitmap()
//                .load(mProduct.get(position).getmImage())
//                .into(holder.image);
//        Glide.with(mContext)
//                .load(mProduct.get(position).getmImage())
//                .into(holder.image);
        Ion.with(mContext).load(mClinic.get(position).getmImage()).intoImageView(holder.image);
//        Log.d("ImageUrl::", mClinic.get(position).getmImage());
//        Picasso.with(mContext).load(mClinic.get(position).getmImage()).into(holder.image);

        holder.name.setText(mClinic.get(position).getmName());
//
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String shop_id=mProduct.get(position).getmShopId();
//                String product_id=mProduct.get(position).getmId();
//                Common.getInstance().setShopid(shop_id);
//                Common.getInstance().setProductid(product_id);

                Intent intent=new Intent(mContext, OneClinicActivity.class).putExtra("clinic_id",mClinic.get(position).getmId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mClinic.size() > 5) {
            return 5;
        } else {
            return mClinic.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_clinic);
            name = itemView.findViewById(R.id.name_clinic);
        }
    }
}
