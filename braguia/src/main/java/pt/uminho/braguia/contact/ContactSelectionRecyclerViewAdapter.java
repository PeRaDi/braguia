package pt.uminho.braguia.contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import pt.uminho.braguia.databinding.FragmentContactSelectionBinding;

import java.util.List;

public class ContactSelectionRecyclerViewAdapter extends RecyclerView.Adapter<ContactSelectionRecyclerViewAdapter.ViewHolder> {

    private final List<Contact> mValues;
    private List<Contact> selectedContactList;

    public ContactSelectionRecyclerViewAdapter(List<Contact> items, List<Contact> selectedContactList) {
        mValues = items;
        this.selectedContactList = selectedContactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentContactSelectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.contactName.setText(mValues.get(position).contactName);
        holder.contactNumber.setText(mValues.get(position).contactNumber);

        holder.checkBox.setOnClickListener(v -> {
            if(holder.checkBox.isChecked()) {
                if(selectedContactList.size() == 3) {
                    holder.checkBox.setChecked(false);
                    Toast.makeText(v.getContext(), "Só podem existir 3 contactos de emergência.", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedContactList.add(mValues.get(position));
            } else {
                selectedContactList.remove(mValues.get(position));
            }
        });

        holder.checkBox.setChecked(selectedContactList.contains(mValues.get(position)));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView contactName;
        public final TextView contactNumber;
        public final CheckBox checkBox;
        public Contact mItem;

        public ViewHolder(FragmentContactSelectionBinding binding) {
            super(binding.getRoot());
            contactName = binding.lblContactName;
            contactNumber = binding.lblContactNumber;

            checkBox = binding.checkBox;
        }
    }
}