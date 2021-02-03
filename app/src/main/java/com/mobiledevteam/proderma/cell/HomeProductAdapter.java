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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    private ArrayList<HomeProduct> mProduct;
    private Context mContext;

    public HomeProductAdapter(Context context, ArrayList<HomeProduct> data) {
        mContext = context;
        mProduct = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_home_product, parent, false);
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
        Ion.with(mContext).load(mProduct.get(position).getmImage()).intoImageView(holder.image);
//        Picasso.with(mContext).load(mProduct.get(position).getmImage()).into(holder.image);

        holder.name.setText(mProduct.get(position).getmName());
//
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String shop_id=mProduct.get(position).getmShopId();
//                String product_id=mProduct.get(position).getmId();
//                Common.getInstance().setShopid(shop_id);
//                Common.getInstance().setProductid(product_id);
                Intent intent=new Intent(mContext, OneProductActivity.class).putExtra("product_id", mProduct.get(position).getmId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mProduct.size() > 10) {
            return 10;
        } else {
            return mProduct.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.name_product);
        }
    }
}