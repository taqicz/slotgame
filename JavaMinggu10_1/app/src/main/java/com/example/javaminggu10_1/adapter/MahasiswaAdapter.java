package com.example.javaminggu10_1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaminggu10_1.R;
import com.example.javaminggu10_1.model.Mahasiswa;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {
    private List<Mahasiswa> mahasiswaList;
    private OnItemActionListener listener;
    private Context context;

    // Listener interface untuk aksi edit/delete
    public interface OnItemActionListener {
        void onEdit(Mahasiswa mahasiswa);
        void onDelete(Mahasiswa mahasiswa, int position);
    }

    public MahasiswaAdapter(List<Mahasiswa> mahasiswaList, OnItemActionListener listener) {
        this.mahasiswaList = mahasiswaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mahasiswa mhs = mahasiswaList.get(position);
        holder.tvNama.setText(mhs.getNama());
        holder.tvNrp.setText("NRP: " + mhs.getNrp());
        holder.tvEmail.setText("Email: " + mhs.getEmail());
        holder.tvJurusan.setText("Jurusan: " + mhs.getJurusan());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(mhs);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(mhs, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return mahasiswaList.size();
    }

    public void setData(List<Mahasiswa> newList) {
        mahasiswaList = newList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mahasiswaList.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNrp, tvEmail, tvJurusan;
        Button btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNrp = itemView.findViewById(R.id.tvNrp);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}