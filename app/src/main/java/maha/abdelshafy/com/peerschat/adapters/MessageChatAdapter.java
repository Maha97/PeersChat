package maha.abdelshafy.com.peerschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import maha.abdelshafy.com.peerschat.R;
import maha.abdelshafy.com.peerschat.model.Message;


public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SENDER = 0;
    public static final int RECIPIENT = 1;
    private List<Message> mChatList;

    public MessageChatAdapter(List<Message> listOfFireChats) {
        mChatList = listOfFireChats;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position).getPeerStatus() == SENDER) {
            return SENDER;
        } else {
            return RECIPIENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case SENDER:
                View viewSender = inflater.inflate(R.layout.sender_message_layout, viewGroup, false);
                viewHolder = new ViewHolderSender(viewSender);
                break;
            case RECIPIENT:
                View viewRecipient = inflater.inflate(R.layout.recipient_message_layout, viewGroup, false);
                viewHolder = new ViewHolderRecipient(viewRecipient);
                break;
            default:
                viewRecipient = inflater.inflate(R.layout.recipient_message_layout, viewGroup, false);
                viewHolder = new ViewHolderRecipient(viewRecipient);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case SENDER:
                ViewHolderSender viewHolderSender = (ViewHolderSender) viewHolder;
                Message senderFireMessage = mChatList.get(position);
                viewHolderSender.getSenderMessageTextView().setText(senderFireMessage.getMessage_body());
                break;
            case RECIPIENT:
                ViewHolderRecipient viewHolderRecipient = (ViewHolderRecipient) viewHolder;
                Message recipientFireMessage = mChatList.get(position);
                viewHolderRecipient.getRecipientMessageTextView().setText(recipientFireMessage.getMessage_body());
                break;
        }


    }


    @Override
    public int getItemCount() {
        int chatSize = mChatList.size();
        return chatSize;
    }


    public void refillAdapter(Message newFireChatMessage) {
        mChatList.add(newFireChatMessage);

        notifyItemInserted(getItemCount() - 1);
    }


    public void cleanUp() {
        mChatList.clear();
    }


    public class ViewHolderSender extends RecyclerView.ViewHolder {

        private TextView mSenderMessageTextView;

        public ViewHolderSender(View itemView) {
            super(itemView);
            mSenderMessageTextView = (TextView) itemView.findViewById(R.id.text_view_sender_message);
        }

        public TextView getSenderMessageTextView() {
            return mSenderMessageTextView;
        }

    }


    public class ViewHolderRecipient extends RecyclerView.ViewHolder {

        private TextView mRecipientMessageTextView;

        public ViewHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.text_view_recipient_message);
        }

        public TextView getRecipientMessageTextView() {
            return mRecipientMessageTextView;
        }

    }
}