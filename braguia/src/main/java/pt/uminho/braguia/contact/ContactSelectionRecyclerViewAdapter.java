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
    private final List<Contact> selectedContactList;

    public ContactSelectionRecyclerViewAdapter(List<Contact> items, List<Contact> selectedContactList) {
        this.mValues = items;
        this.selectedContactList = selectedContactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentContactSelectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), selectedContactList);
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

        public void bind(Contact contact, List<Contact> selectedContactList) {
            mItem = contact;
            contactName.setText(contact.contactName);
            contactNumber.setText(contact.contactNumber);

            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()) {
                    if (selectedContactList.size() >= 3) {
                        checkBox.setChecked(false);
                        Toast.makeText(v.getContext(), "Só podem existir 3 contactos de emergência.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectedContactList.add(mItem);
                } else {
                    selectedContactList.removeIf(selectedContact -> selectedContact.id == mItem.id);
                }
            });

            checkBox.setChecked(selectedContactList.stream().anyMatch(selectedContact -> selectedContact.id == mItem.id));
        }
    }
}