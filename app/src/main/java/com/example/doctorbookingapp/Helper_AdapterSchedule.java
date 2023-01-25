package com.example.doctorbookingapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorbookingapp.databinding.RowScheduleSelectionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Helper_AdapterSchedule extends RecyclerView.Adapter<Helper_AdapterSchedule.HolderSchedule> {

    String schedValue;
    Double newvalue;
    TextView okay_text, cancel_text, DoctorsName, address, rating;
    ImageView DoctorImage;
    int countreserve = 0;
    int ratingvalue;
    private Context context;
    private ArrayList<data_ModelSchedule> modelScheduleArrayList;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RowScheduleSelectionBinding binding;

    public Helper_AdapterSchedule(Context context, ArrayList<data_ModelSchedule> modelScheduleArrayList) {
        this.context = context;
        this.modelScheduleArrayList = modelScheduleArrayList;
    }

    @NonNull
    @Override
    public HolderSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowScheduleSelectionBinding.inflate(LayoutInflater.from(context), parent, false);


        return new HolderSchedule(binding.getRoot());


    }

    @Override
    public void onBindViewHolder(@NonNull HolderSchedule holder, int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        Dialog dialog = new Dialog(context);
        data_ModelSchedule modelSchedule = modelScheduleArrayList.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserDetails(modelSchedule, holder);

        String uid = modelSchedule.getUid();
        String timestamp = modelSchedule.getTimestamp();
        String time = modelSchedule.getTime();
        String amount = modelSchedule.getAmount();
        String date = modelSchedule.getDate();
        String location = modelSchedule.getLocation();
        String hospital = modelSchedule.getHospital();
        schedValue = modelSchedule.getAmount();
        countreserve = modelSchedule.getCount();

        holder.hospitalTv.setText("IR3 Hospital Solutions Consultancy And Pharmaceuticals");
        holder.timeTv.setText("Available at " + date + " from " + time);
        holder.amountTv.setText("Amount: " + amount + ".00" + " Pesos");
        holder.hospitalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Hello");
                progressDialog.show();
            }
        });
        dialog.setContentView(R.layout.doctor_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_DoctorBookingApp;

        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DoctorImage = dialog.findViewById(R.id.imageView);
                okay_text = dialog.findViewById(R.id.okay_text);
                cancel_text = dialog.findViewById(R.id.cancel_text);
                DoctorsName = dialog.findViewById(R.id.DoctorsName);
                address = dialog.findViewById(R.id.address);
                rating = dialog.findViewById(R.id.rating);
                int r = Integer.parseInt(ratings(modelSchedule.getUid()));

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(modelSchedule.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DoctorsName.setText(snapshot.child("name").getValue().toString());
                                address.setText(snapshot.child("email").getValue().toString());
                                Glide.with(context)
                                        .load(snapshot.child("profileImage").getValue())
                                        .placeholder(R.drawable.ic_person_dark)
                                        .into(DoctorImage);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                okay_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                cancel_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to book this schedule?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressDialog.setMessage("Setting your Schedule");
                                progressDialog.show();
                                String timestamp1 = String.valueOf(System.currentTimeMillis());

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("uid", uid);
                                hashMap.put("timestamp", timestamp1);
                                hashMap.put("amount", amount);
                                hashMap.put("time", time);
                                hashMap.put("date", date);
                                hashMap.put("sched_id", timestamp);
                                hashMap.put("seen", "0");
                                hashMap.put("patientid", firebaseAuth.getUid());

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules");
                                databaseReference.child("" + timestamp)
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Snackbar snackbar = Snackbar
                                                        .make(binding.getRoot(), "Schedule has been selected. Kindly Pay to Confirm.", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                                progressDialog.dismiss();
                                                deductwallet(amount);

                                                //add the Count of patient set schedule
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("count", countreserve + 1);

                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schedules");
                                                databaseReference.child(timestamp).child("count")
                                                        .setValue(countreserve + 1)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                progressDialog.dismiss();
                                                                Snackbar snackbar = Snackbar
                                                                        .make(binding.getRoot(), "Schedule availability created successfully. Thank you.", Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialog.dismiss();
                                                                Snackbar snackbar = Snackbar
                                                                        .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                                            }
                                                        });
                                                databaseReference.child(timestamp).child("user")
                                                        .setValue(firebaseAuth.getUid())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                progressDialog.dismiss();
                                                                Snackbar snackbar = Snackbar
                                                                        .make(binding.getRoot(), "Schedule availability created successfully. Thank you.", Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialog.dismiss();
                                                                Snackbar snackbar = Snackbar
                                                                        .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Snackbar snackbar = Snackbar
                                                        .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                            }
                                        });
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });


    }

    private String ratings(String doctorid) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelScheduleArrayList.clear();
                int b = 0;
                int r = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data_patientSchedules data_patientSchedules = ds.getValue(data_patientSchedules.class);
                    if (data_patientSchedules.getUid().equals(doctorid)) {

                        b = b + data_patientSchedules.getRating();
                        r = r + 1;
                        Log.i("rating", valrating(String.valueOf(b), r) + "-" + ratingvalue);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return String.valueOf(ratingvalue);
    }

    public String valrating(String val, int row) {
        Double count = Double.parseDouble(val) / row;
        rating.setText(String.valueOf(count));
        return val;

    }

    private void deductwallet(String amount) {
        String uid = firebaseAuth.getUid();
        final int[] i = {0};
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet Amount");
        databaseReference.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        newvalue = Double.parseDouble(snapshot.child("cash").getValue().toString());
                        Log.i("Value", String.valueOf(newvalue - Double.parseDouble(amount)));
                        if (i[0] == 0) {
                            if (newvalue >= Double.parseDouble(amount)) {
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Wallet Amount");
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("uid", uid);
                                hashMap.put("timestamp", timestamp);
                                hashMap.put("cash", newvalue - Double.parseDouble(amount));
                                //update value from user
                                databaseReference1.child(uid)
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar snackbar = Snackbar
                                                        .make(binding.getRoot(), "Cash in transaction successfully.", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar snackbar = Snackbar
                                                        .make(binding.getRoot(), "Cash in transaction unsuccessful.", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                                            }
                                        });
                                i[0]++;
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(binding.getRoot(), "Not Enough Balance ! ! ! ", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void loadUserDetails(data_ModelSchedule modelSchedule, HolderSchedule holder) {

        String uid = modelSchedule.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String name = "" + snapshot.child("name").getValue();
                        String location = "" + snapshot.child("location").getValue();
                        holder.nameTv.setText(name);
                        holder.locationTv.setText(location);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return modelScheduleArrayList.size();
    }

    class HolderSchedule extends RecyclerView.ViewHolder {

        TextView dateTv, timeTv, amountTv, hospitalTv, locationTv, nameTv;
        Button bookBtn;

        public HolderSchedule(@NonNull View itemView) {
            super(itemView);
            nameTv = binding.usernameTv;
            hospitalTv = binding.hospitalTv;
            locationTv = binding.locationTv;
            timeTv = binding.timeTv;
            bookBtn = binding.bookBtn;
            amountTv = binding.amountTv;
        }
    }
}
