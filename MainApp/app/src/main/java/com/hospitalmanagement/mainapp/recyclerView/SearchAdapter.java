package com.hospitalmanagement.mainapp.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.pojo.Member;
import com.hospitalmanagement.mainapp.pojo.SearchName;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchName> memberNames;
    private LayoutInflater layoutInflater;
    private RelativeLayout searchLayout;
    private MaterialSpinner informationSpinner;
    private RecyclerViewObjectId recyclerViewObjectId;

    public SearchAdapter(List<SearchName> memberNames, LayoutInflater layoutInflater, RelativeLayout searchLayout, MaterialSpinner informationSpinner, RecyclerViewObjectId recyclerViewObjectId) {
        this.memberNames = memberNames;
        this.layoutInflater = layoutInflater;
        this.searchLayout = searchLayout;
        this.informationSpinner = informationSpinner;
        this.recyclerViewObjectId = recyclerViewObjectId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.familydetail_searchviewitems, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final SearchName family = memberNames.get(position);

        holder.familyNumber.setText("कुटुंब (Family) " + (position + 1));

        for(Member searchName : family.getMembers()) {

            TextView textView = ((TextView) layoutInflater.inflate(R.layout.familydetail_itemtextview, holder.linearLayout, true).findViewById(R.id.setTextName));
            textView.setId(ViewCompat.generateViewId());
            textView.setText(searchName.getMembername());

        }

        holder.familyItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchLayout.setVisibility(View.GONE);
                informationSpinner.setVisibility(View.VISIBLE);
                recyclerViewObjectId.onFamilyItemClick(family.getId());

            }
        });

    }

    @Override
    public int getItemCount() {

        return this.memberNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linearLayout;
        public TextView familyNumber;
        private MaterialCardView familyItemView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            linearLayout = itemView.findViewById(R.id.searchFamilyItemView);
            familyNumber = itemView.findViewById(R.id.setFamilyNumber);
            familyItemView = itemView.findViewById(R.id.familyItemView);
        }
    }

    public void updateList(List<SearchName> updatedList) {
        this.memberNames = updatedList;
        notifyDataSetChanged();
    }

}
