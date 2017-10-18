package com.krifhu.chatapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private String username;
    private String user1;
    private String user2;
    private List<Messages> messages;

    public Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            context = v.getContext();
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, Messages item) {
        messages.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAdapter(String user1, String user2, List<Messages> messages) {
        //this.username = username;
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position){

        final Messages message = messages.get(position);

        if(user1.equals(message.getSender())) {
            //System.out.println("********************* IF 1, 0 Returned **************************");
            return 0;
        }
        if(user1.equals(message.getReceiver())){
            //System.out.println("********************* IF 2, 1 Returned **************************");
            return 1;
        }
        else{
            //System.out.println("********************* ELSE, 0 Returned **************************");
            return 0;

        }


    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        ViewHolder vh;

        switch (viewType){
            case 0:
                v = inflater.inflate(R.layout.user1_row_layout, parent, false);
                vh = new ViewHolder(v);
                break;
            case 1:
                v = inflater.inflate(R.layout.user2_row_layout, parent, false);
                vh = new ViewHolder(v);
                break;
            default:
                v = inflater.inflate(R.layout.user1_row_layout, parent, false);
                vh = new ViewHolder(v);
                break;
        }



        // create a new view

        //View v = inflater.inflate(R.layout.user1_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Messages message = messages.get(position);
        holder.txtHeader.setText(message.getMessageBody());
        holder.txtFooter.setText(message.getSender());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messages.size();
    }

}