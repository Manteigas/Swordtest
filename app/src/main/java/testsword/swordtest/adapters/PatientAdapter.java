package testsword.swordtest.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import testsword.swordtest.R;
import testsword.swordtest.models.Patient;

/**
 * Created by André Manteigas on 27/09/2017.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

    private Context mContext;
    private List<Patient> patientList;

    public PatientAdapter(Context context, List<Patient> patientList) {
        this.mContext = context;
        this.patientList = patientList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (patientList.get(position).getPhoto()) {
            case 1: Picasso.with(mContext).load(R.drawable.photo1).into(holder.ivPatientPhoto); break;
            case 2: Picasso.with(mContext).load(R.drawable.photo2).into(holder.ivPatientPhoto); break;
            case 3: Picasso.with(mContext).load(R.drawable.photo3).into(holder.ivPatientPhoto); break;
            case 4: Picasso.with(mContext).load(R.drawable.photo4).into(holder.ivPatientPhoto); break;
            case 5: Picasso.with(mContext).load(R.drawable.photo5).into(holder.ivPatientPhoto); break;
            case 6: Picasso.with(mContext).load(R.drawable.photo6).into(holder.ivPatientPhoto); break;
            case 7: Picasso.with(mContext).load(R.drawable.photo7).into(holder.ivPatientPhoto); break;
            case 8: Picasso.with(mContext).load(R.drawable.photo8).into(holder.ivPatientPhoto); break;
        }

        holder.tvPatientName.setText(patientList.get(position).getName());

        int age = Calendar.getInstance().get(Calendar.YEAR) - patientList.get(position).getBirthday().get(Calendar.YEAR);
        String sAge = age + " " + mContext.getString(R.string.adapter_years);

        holder.tvPatientAge.setText(sAge);
        holder.tvPatientCompliance.setText(patientList.get(position).getCompliance());
        holder.tvPatientCompliance.setTextColor(getComplianceColor(patientList.get(position).getCompliance()));

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public void addAll(List<Patient> list) {
        this.patientList = list;
    }

    public void clear() {
        this.patientList = new ArrayList<>();
    }

    private int getComplianceColor(String compliance) {
        int complianceColorResourceId;

        switch (compliance) {
            case "A": complianceColorResourceId = R.color.sgreen; break;
            case "B": complianceColorResourceId = R.color.sgreen_ligth; break;
            case "C": complianceColorResourceId = R.color.sorenge; break;
            case "D": complianceColorResourceId = R.color.sorenge; break;
            case "F": complianceColorResourceId = R.color.sred; break;
            default: complianceColorResourceId = R.color.sred; break;
        }

        return ContextCompat.getColor(mContext, complianceColorResourceId);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPatientPhoto;
        public TextView tvPatientName, tvPatientAge, tvPatientCompliance, tvPatientLastSession,
                tvStatus, tvTitleCompliance, tvTitleLastSession;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivPatientPhoto = (ImageView) itemView.findViewById(R.id.iv_userphoto);
            tvPatientName = (TextView) itemView.findViewById(R.id.tv_patient_name);
            tvPatientAge = (TextView) itemView.findViewById(R.id.tv_patient_age);
            tvPatientCompliance = (TextView) itemView.findViewById(R.id.tv_patient_compliance);
            tvPatientLastSession = (TextView) itemView.findViewById(R.id.tv_patient_lastsession);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvTitleCompliance = (TextView) itemView.findViewById(R.id.tv_title_compliance);
            tvTitleLastSession = (TextView) itemView.findViewById(R.id.tv_title_lastsession);

            Typeface avenirHeavy = Typeface.createFromAsset(mContext.getAssets(), "AvenirLTStd-Heavy.otf");
            tvPatientName.setTypeface(avenirHeavy);
            tvPatientCompliance.setTypeface(avenirHeavy);

            Typeface avenirBookOblique = Typeface.createFromAsset(mContext.getAssets(), "AvenirLTStd-BookOblique.otf");
            tvPatientAge.setTypeface(avenirBookOblique);
            tvStatus.setTypeface(avenirBookOblique);

            Typeface avenirBook = Typeface.createFromAsset(mContext.getAssets(), "AvenirLTStd-Book.otf");
            tvTitleCompliance.setTypeface(avenirBook);
            tvTitleLastSession.setTypeface(avenirBook);

            Typeface avenirMedium = Typeface.createFromAsset(mContext.getAssets(), "AvenirLTStd-Medium.otf");
            tvPatientLastSession.setTypeface(avenirMedium);


        }
    }
}
