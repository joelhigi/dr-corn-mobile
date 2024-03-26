package com.jax.drcorn;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.MyViewHolder> {

    String data1[], data2[];
    int images[];
    Context context;
    public DiseaseAdapter(Context ct, String s1[], String s2[], int img[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.disName1.setText(data1[position]);
        holder.shortDesc2.setText(data2[position]);
        holder.disImage.setImageResource(images[position]);

        holder.disLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DiseaseDetails.class);
                intent.putExtra("title",data1[position]);
                intent.putExtra("desc",data2[position]);
                intent.putExtra("image",images[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView disName1, shortDesc2;
        ImageView disImage;
        ConstraintLayout disLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            disName1 = itemView.findViewById(R.id.disname);
            shortDesc2 = itemView.findViewById(R.id.shortdesc);
            disImage = itemView.findViewById(R.id.disImage);
            disLayout = itemView.findViewById(R.id.disLayout);
        }
    }
}
