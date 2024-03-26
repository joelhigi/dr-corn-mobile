package com.jax.drcorn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder>{
    private Context context;
    private Activity activity;
    private ArrayList record_id, disease_name, pred_value, disease_date;

    Animation translate_anim;

    RecordAdapter(Activity activity, Context context, ArrayList record_id, ArrayList disease_name, ArrayList pred_value, ArrayList disease_date){
        this.activity = activity;
        this.context = context;
        this.record_id = record_id;
        this.disease_name = disease_name;
        this.pred_value = pred_value;
        this.disease_date = disease_date;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.record_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.record_id_text.setText(String.valueOf(record_id.get(position)));
        holder.disease_name_text.setText(String.valueOf(disease_name.get(position)));

        holder.pred_value_text.setText(String.valueOf(pred_value.get(position)));
        holder.disease_date_text.setText(String.valueOf(disease_date.get(position)));
        holder.disLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PastRecordDetails.class);
                intent.putExtra("id", String.valueOf(record_id.get(position)));
                intent.putExtra("Disease", String.valueOf(disease_name.get(position)));
                intent.putExtra("Pred", String.valueOf(pred_value.get(position)));
                intent.putExtra("Date", String.valueOf(disease_date.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return record_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView record_id_text, disease_name_text, pred_value_text, disease_date_text;
        LinearLayout disLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            record_id_text = itemView.findViewById(R.id.record_id_text);
            disease_name_text = itemView.findViewById(R.id.disease_name_text);
            pred_value_text = itemView.findViewById(R.id.pred_value_text);
            disease_date_text = itemView.findViewById(R.id.disease_date_text);

            disLayout = itemView.findViewById(R.id.disLayout);

            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            disLayout.setAnimation(translate_anim);
        }
    }
}
