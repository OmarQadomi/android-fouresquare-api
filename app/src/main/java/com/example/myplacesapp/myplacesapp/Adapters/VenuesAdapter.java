package com.example.myplacesapp.myplacesapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.myplacesapp.myplacesapp.DataModels.Venue;
import com.example.myplacesapp.myplacesapp.R;

import java.util.List;

/**
 * Created by 1234485 on 3/25/2017.
 */

public class VenuesAdapter  extends RecyclerView.Adapter<VenuesAdapter.MyViewHolder> {

        private List<Venue>venuesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name,address,phone;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.tv_name);
                address = (TextView) view.findViewById(R.id.tv_address);
                phone = (TextView) view.findViewById(R.id.tv_phone);
            }
        }


        public VenuesAdapter(List<Venue> venuesList) {
            this.venuesList = venuesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.venue_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Venue venue = venuesList.get(position);
            holder.name.setText(venue.getName()!=null?venue.getName():"");
            holder.address.setText(venue.getLocation().getAddress()!=null?venue.getLocation().getAddress():"");
            holder.phone.setText(venue.getContacts().getFormattedPhone()!=null?venue.getContacts().getFormattedPhone():"");
        }

        @Override
        public int getItemCount() {
            return venuesList.size();
        }

}
