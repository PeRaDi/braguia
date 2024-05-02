package pt.uminho.braguia.pins.old;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pt.uminho.braguia.R;

public class PinsList extends BaseAdapter {
    private ArrayList<PinData> pinsData;
    private LayoutInflater layoutInflater;
    private Context context;
    private View.OnClickListener itemClickListener;

    static class ViewHolder {
        TextView txt_itemName;
        TextView txt_itemDescription;
        ImageView itemImage;
    }

    public PinsList(Context context, ArrayList<PinData> pinsData) {
        this.context = context;
        this.pinsData = pinsData;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<PinData> pinsData) {
        this.pinsData = pinsData;
        notifyDataSetChanged();
    }

    public int getCount() {
        return pinsData.size();
    }

    public Object getItem(int position) {
        return pinsData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_view_item, null);
            holder = new ViewHolder();
            holder.txt_itemName = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.descriptionTextView);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_itemName.setText(pinsData.get(position).getName());
        holder.txt_itemDescription.setText(pinsData.get(position).getDescription());
        if (pinsData.get(position).getImage().equals("")){
            holder.itemImage.setImageResource(R.drawable.noimage);
        }else {
            Picasso.get()
                    .load(pinsData.get(position).getImage())
                    .fit()
                    .centerCrop()
                    .into(holder.itemImage);
        }
        convertView.setOnClickListener(itemClickListener);
        return convertView;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
