package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorbookingapp.databinding.RowScheduleSelectionDoctorsBinding;
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

public class Helper_AdapterSchedule_doctors extends RecyclerView.Adapter<Helper_AdapterSchedule_doctors.HolderSchedule> {

    String schedValue;
    Double newvalue;
    TextView patient;
    int countreserve = 0;
    private Context context;
    private ArrayList<data_patientSchedules> modelScheduleArrayList;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RowScheduleSelectionDoctorsBinding binding;

    public Helper_AdapterSchedule_doctors(Context context, ArrayList<data_patientSchedules> modelScheduleArrayList) {
        this.context = context;
        this.modelScheduleArrayList = modelScheduleArrayList;
    }

    @NonNull
    @Override
    public HolderSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowScheduleSelectionDoctorsBinding.inflate(LayoutInflater.from(context), parent, false);


        return new HolderSchedule(binding.getRoot());


    }

    @Override
    public void onBindViewHolder(@NonNull HolderSchedule holder, int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        data_patientSchedules data_patientSchedules = modelScheduleArrayList.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserDetails(data_patientSchedules, holder);

        String uid = data_patientSchedules.getUid();
        String timestamp = data_patientSchedules.getTimestamp();
        String time = data_patientSchedules.getTime();
        String amount = data_patientSchedules.getAmount();
        String date = data_patientSchedules.getDate();
        schedValue = data_patientSchedules.getAmount();
        countreserve = Integer.parseInt(data_patientSchedules.getSeen());
        holder.locationTv.setText("Successfully Paid");

        holder.hospitalTv.setText("IR3 Hospital Solutions Consultancy And Pharmaceuticals");
        holder.timeTv.setText("Scheduled at " + date + " from " + time);
        patient = holder.patient;
        loadUserDetailsProfile(data_patientSchedules.getPatientid());
        holder.hospitalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Hello");
                progressDialog.show();
            }
        });
        holder.donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setMessage("Are you sure this schedule is done?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules").child(data_patientSchedules.getSched_id());
                                databaseReference.child("seen").setValue("1");
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

//        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                builder1.setMessage("Are you Sure you want to take This Scedule?");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                progressDialog.setMessage("Setting your Schedule");
//                                progressDialog.show();
//                                String timestamp1 = String.valueOf(System.currentTimeMillis());
//
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("uid", uid);
//                                hashMap.put("timestamp", timestamp1);
//                                hashMap.put("amount", amount);
//                                hashMap.put("time", time);
//                                hashMap.put("date",date);
//                                hashMap.put("sched_id", timestamp);
//
//                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules");
//                                databaseReference.child("" + timestamp)
//                                        .setValue(hashMap)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                progressDialog.dismiss();
//                                                Snackbar snackbar = Snackbar
//                                                        .make(binding.getRoot(), "Schedule has been selected. Kindly Pay to Confirm.", Snackbar.LENGTH_LONG);
//                                                snackbar.show();
//                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                                progressDialog.dismiss();
//                                                deductwallet(amount);
//
//                                                //add the Count of patient set schedule
//                                                HashMap<String, Object> hashMap = new HashMap<>();
//                                                hashMap.put("count", countreserve+1);
//
//                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schedules");
//                                                databaseReference.child(timestamp).child("count")
//                                                        .setValue(countreserve+1)
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void unused) {
//                                                                progressDialog.dismiss();
//                                                                Snackbar snackbar = Snackbar
//                                                                        .make(binding.getRoot(), "Schedule availability created successfully. Thank you.", Snackbar.LENGTH_LONG);
//                                                                snackbar.show();
//                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                                                }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                progressDialog.dismiss();
//                                                                Snackbar snackbar = Snackbar
//                                                                        .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
//                                                                snackbar.show();
//                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                                            }
//                                                        });
//                                                databaseReference.child(timestamp).child("user")
//                                                        .setValue(firebaseAuth.getUid())
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void unused) {
//                                                                progressDialog.dismiss();
//                                                                Snackbar snackbar = Snackbar
//                                                                        .make(binding.getRoot(), "Schedule availability created successfully. Thank you.", Snackbar.LENGTH_LONG);
//                                                                snackbar.show();
//                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                progressDialog.dismiss();
//                                                                Snackbar snackbar = Snackbar
//                                                                        .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
//                                                                snackbar.show();
//                                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                                            }
//                                                        });
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                progressDialog.dismiss();
//                                                Snackbar snackbar = Snackbar
//                                                        .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
//                                                snackbar.show();
//                                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                            }
//                                        });
//                                dialog.cancel();
//                            }
//                        });
//
//                builder1.setNegativeButton(
//                        "No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
//
//
//            }
//        });


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

    private void loadUserDetailsProfile(String userid) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String name = "" + snapshot.child("name").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String userType = "" + snapshot.child("userType").getValue();
                        String location = "" + snapshot.child("location").getValue();
                        String contactNumber = "" + snapshot.child("contactNumber").getValue();
                        patient.setText("Patient: " + snapshot.child("name").getValue().toString());
//                        binding.emailTv.setText(email);
//                        binding.usernameTv.setText(name);
//                        binding.clientProfileTv.setText(name + " Profile");
//                        binding.locationTv.setText(location);
//                        binding.contactNumberTv.setText(contactNumber);
//
//                        Glide.with(Activity_PatientProfile.this)
//                                .load(profileImage)
//                                .placeholder(R.drawable.ic_person_dark)
//                                .into(binding.profileTv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadUserDetails(data_patientSchedules modelSchedule, HolderSchedule holder) {

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

        TextView timeTv, amountTv, hospitalTv, locationTv, nameTv, patient;
        Button donebtn;

        public HolderSchedule(@NonNull View itemView) {
            super(itemView);
            nameTv = binding.usernameTv;
            hospitalTv = binding.hospitalTv;
            locationTv = binding.locationTv;
            timeTv = binding.timeTv;
            patient = binding.patient;
            donebtn = binding.donebtn;
        }
    }
}
