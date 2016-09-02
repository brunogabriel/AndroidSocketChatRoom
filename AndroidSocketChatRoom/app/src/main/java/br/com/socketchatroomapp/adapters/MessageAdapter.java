package br.com.socketchatroomapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.socketchatroomapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunogabriel on 9/1/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_SEND_MESSAGE = 0;
    public static final int VIEW_TYPE_RECEIVE_MESSAGE = 1;
    public static final int VIEW_TYPE_IS_ONLINE = 2;
    public static final int VIEW_TYPE_IS_OFFLINE = 3;

    List<Object> mItens = new ArrayList<>();


    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView;
        RecyclerView.ViewHolder mHolder;

        switch (viewType) {
            case VIEW_TYPE_IS_ONLINE:
            case VIEW_TYPE_IS_OFFLINE:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_status_device, parent, false);
                mHolder = new MessageHolder(mView, parent.getContext());
                break;
            case VIEW_TYPE_RECEIVE_MESSAGE:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_receiver_message, parent, false);
                mHolder = new MessageHolder(mView, parent.getContext());
                break;
            case VIEW_TYPE_SEND_MESSAGE:
            default:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_sender_message, parent, false);
                mHolder = new MessageHolder(mView, parent.getContext());
                break;
        }

        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageHolder) {
            ((MessageHolder) holder).bindMessage((JSONObject) mItens.get(position));
        } else if (holder instanceof StatusHolder) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject jsonAtPosition = (JSONObject) mItens.get(position);
        try {
            return jsonAtPosition.getInt("appType");
        } catch (JSONException e) {
            return VIEW_TYPE_SEND_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return mItens == null ? 0: mItens.size();
    }


    public void addItem(Object element) {
        if (element != null) {
            int mFirstPosition = mItens.size();
            mItens.add(element);
            notifyItemInserted(mFirstPosition);
        }
    }

    /**
     * Holders
     */

    public class MessageHolder extends RecyclerView.ViewHolder {

        Context context;

        @BindView(R.id.messageText)
        TextView messageText;

        public MessageHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        public void bindMessage(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    messageText.setText("");
                    int type = jsonObject.getInt("appType");
                    Spannable spannableUser = new SpannableString(type == VIEW_TYPE_SEND_MESSAGE ? "You said:\n": (String)jsonObject.get("username") + " said:\n");
                    Spannable spannableMessage = new SpannableString((String)jsonObject.get("message"));

                    spannableUser.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableUser.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableMessage.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    messageText.append(spannableUser);
                    messageText.append(spannableMessage);

                } catch (Exception e) {
                    Log.e("MessageHolder", "Fail during bind: " + e.getMessage());
                }
            }
        }
    }

    public class StatusHolder extends RecyclerView.ViewHolder {
        Context context;
        public StatusHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
        }
    }
}
