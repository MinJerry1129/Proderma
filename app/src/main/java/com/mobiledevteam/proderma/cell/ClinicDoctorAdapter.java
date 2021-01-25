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
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.clinic.ClinicDoctorEditActivity;
import com.mobiledevteam.proderma.home.OneClinicActivity;

import java.util.ArrayList;

public class ClinicDoctorAdapter extends RecyclerView.Adapter<ClinicDoctorAdapter.ViewHolder> {
    private ArrayList<ClinicDoctor> mDoctor;
    private Context mContext;

    public ClinicDoctorAdapter(Context context, ArrayList<ClinicDoctor> data) {
        mContext = context;
        mDoctor = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_clinic_doctor, parent, false);
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
        Ion.with(mContext).load(mDoctor.get(position).getmImage()).intoImageView(holder.image);
        holder.name.setText(mDoctor.get(position).getmName());
        holder.age.setText(mDoctor.get(position).getmAge());
        holder.info.setText(mDoctor.get(position).getmInfo());

//
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = Common.getInstance().getClinicpagetype();
                if (status.equals("home")){
                    Intent intent=new Intent(mContext, ClinicDoctorEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
//                String shop_id=mProduct.get(position).getmShopId();
//                String product_id=mProduct.get(position).getmId();
//                Common.getInstance().setShopid(shop_id);
//                Common.getInstance().setProductid(product_id);

//                Intent intent=new Intent(mContext, OneProductActivity.class).putExtra("ProductId",mProduct.get(position).getmId());
//                Intent intent=new Intent(mContext, OneClinicActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDoctor.size() > 5) {
            return 5;
        } else {
            return mDoctor.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView age;
        TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_doctor);
            name = itemView.findViewById(R.id.txt_doctorname);
            age = itemView.findViewById(R.id.txt_doctorage);
            info = itemView.findViewById(R.id.txt_doctorinfo);
        }
    }
}
