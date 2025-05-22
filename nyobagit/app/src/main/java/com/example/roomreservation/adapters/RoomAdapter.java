package com.example.roomreservation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomreservation.R;
import com.example.roomreservation.db.models.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter untuk menampilkan daftar ruangan di RecyclerView.
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList = new ArrayList<>();
    private final OnRoomClickListener listener;

    /**
     * Konstruktor adapter.
     *
     * @param listener Listener untuk menangani klik pada item ruangan.
     */
    public RoomAdapter(OnRoomClickListener listener) {
        this.listener = listener;
    }

    /**
     * Mengatur data ruangan ke adapter.
     *
     * @param rooms Daftar ruangan yang akan ditampilkan.
     */
    public void setRoomList(List<Room> rooms) {
        this.roomList = rooms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.bind(room, listener);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    /**
     * ViewHolder untuk RecyclerView item.
     */
    static class RoomViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtRoomName;
        private final TextView txtLocation;
        private final TextView txtCapacity;
        private final TextView txtPrice;
        private final TextView txtStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRoomName = itemView.findViewById(R.id.tv_room_name);
            txtLocation = itemView.findViewById(R.id.tv_room_location);
            txtCapacity = itemView.findViewById(R.id.tv_room_capacity);
            txtPrice = itemView.findViewById(R.id.tv_room_price);
            txtStatus = itemView.findViewById(R.id.tv_room_status);
        }

        /**
         * Mengikat data ruangan ke tampilan item.
         *
         * @param room     Data ruangan.
         * @param listener Listener untuk menangani klik pada item.
         */
        public void bind(final Room room, final OnRoomClickListener listener) {
            txtRoomName.setText(room.nama);
            txtLocation.setText("Lokasi: " + room.lokasi);
            txtCapacity.setText("Kapasitas: " + room.kapasitas + " orang");
            txtPrice.setText("Harga: Rp " + room.harga);
            txtStatus.setText("Status: " + (room.status.equals("available") ? "Tersedia" : "Tidak Tersedia"));

            // Klik listener untuk item ruangan
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRoomClick(room);
                }
            });
        }
    }

    /**
     * Interface untuk menangani klik pada item ruangan.
     */
    public interface OnRoomClickListener {
        void onRoomClick(Room room);
    }
}

