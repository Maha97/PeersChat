package maha.abdelshafy.com.peerschat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maha.abdelshafy.com.peerschat.FireChatHelper.ExtraIntent;
import maha.abdelshafy.com.peerschat.R;
import maha.abdelshafy.com.peerschat.adapters.MessageChatAdapter;
import maha.abdelshafy.com.peerschat.model.Message;
import maha.abdelshafy.com.peerschat.model.User;


public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_recycler_view)
    RecyclerView mChatRecyclerview;
    @BindView(R.id.edit_text_message)
    EditText mTextMessage;
    @BindView(R.id.fab_btn_send)
    FloatingActionButton mFab_send;

    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String mRecipientId;
    private String mCurrentUserId;
    private MessageChatAdapter mMessageChatAdapter;

    private User user;
    private Toast mToast;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        setDatabaseInstance();
        setUsersId();
        setChatRecyclerview();


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long currentVisiblePosition;
        currentVisiblePosition = savedInstanceState.getLong("key");
        ((LinearLayoutManager) mChatRecyclerview.getLayoutManager())
                .scrollToPosition(((int) currentVisiblePosition));
        currentVisiblePosition = 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        long currentVisiblePosition = 0;
        currentVisiblePosition = ((LinearLayoutManager) mChatRecyclerview.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putLong("key", currentVisiblePosition);
    }

    private void setDatabaseInstance() {
        String chatRef = getIntent().getStringExtra(ExtraIntent.EXTRA_CHAT_REF);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(chatRef);
    }

    private void setUsersId() {
        mRecipientId = getIntent().getStringExtra(ExtraIntent.EXTRA_RECIPIENT_ID);
        mCurrentUserId = getIntent().getStringExtra(ExtraIntent.EXTRA_CURRENT_USER_ID);
    }

    private void setChatRecyclerview() {
        mChatRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerview.setHasFixedSize(true);
        mMessageChatAdapter = new MessageChatAdapter(new ArrayList<Message>());
        mChatRecyclerview.setAdapter(mMessageChatAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mChildEventListener = mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getSender_name().equals(mCurrentUserId)) {
                        message.setRecipientOrSenderStatus(MessageChatAdapter.SENDER);

                    } else {
                        message.setRecipientOrSenderStatus(MessageChatAdapter.RECIPIENT);
                    }
                    mMessageChatAdapter.refillAdapter(message);
                    mChatRecyclerview.scrollToPosition(mMessageChatAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
        mMessageChatAdapter.cleanUp();
    }

    @OnClick(R.id.fab_btn_send)
    public void btnSendMsgListener(View sendButton) {

        String senderMessage = mTextMessage.getText().toString().trim();

        if (!senderMessage.isEmpty()) {

            Message newMessage = new Message(senderMessage, mCurrentUserId, mRecipientId);
            mDatabaseReference.push().setValue(newMessage);

            mTextMessage.setText("");
        }
    }

}