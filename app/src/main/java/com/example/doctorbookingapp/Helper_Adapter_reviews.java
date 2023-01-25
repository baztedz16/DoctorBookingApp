package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorbookingapp.databinding.RowSubmittedReviewsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Helper_Adapter_reviews extends RecyclerView.Adapter<Helper_Adapter_reviews.HolderSchedule> {

    String schedValue;
    Double newvalue;
    TextView patient;
    int countreserve = 0;
    private Context context;
    private ArrayList<data_patientSchedules> modelScheduleArrayList;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RowSubmittedReviewsBinding binding;

    public Helper_Adapter_reviews(Context context, ArrayList<data_patientSchedules> modelScheduleArrayList) {
        this.context = context;
        this.modelScheduleArrayList = modelScheduleArrayList;
    }

    @NonNull
    @Override
    public HolderSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowSubmittedReviewsBinding.inflate(LayoutInflater.from(context), parent, false);


        return new HolderSchedule(binding.getRoot());


    }

    @Override
    public void onBindViewHolder(@NonNull HolderSchedule holder, int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        data_patientSchedules data_patientSchedules = modelScheduleArrayList.get(position);

        firebaseAuth = FirebaseAuth.getInstance();


        holder.patient.setText("Patient Name: " + data_patientSchedules.getPatientid());
        holder.reviews.setText("Review: " + data_patientSchedules.getReviews());
        holder.ratings.setText("Rating: " + data_patientSchedules.getRating());
        patient = holder.patient;

    }


    @Override
    public int getItemCount() {
        return modelScheduleArrayList.size();
    }

    class HolderSchedule extends RecyclerView.ViewHolder {

        TextView patient, reviews, ratings;

        public HolderSchedule(@NonNull View itemView) {
            super(itemView);
            patient = binding.patientTv;
            reviews = binding.reviewTv;
            ratings = binding.ratingTv;
        }
    }
}
