package com.example.yashrajendratiwari;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yashrajendratiwari.databinding.RvMessageRowBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class RVMessageAdapter extends RecyclerView.Adapter<RVMessageAdapter.ViewHolder> {

    private List<String> messages = new ArrayList<>();
    private static final int ME = 0;
    private static final int YOU = 1;
    private MediaPlayer mediaPlayer = null;
    private final String TAG = getClass().getName();

    Context context;

    public RVMessageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RvMessageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.rv_message_row, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position % 2 == 0) {
            shLMRMessage(holder, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
            holder.binding.tvLMessage.setText(messages.get(position));
        } else if (position % 3 == 0) {
            shLMRMessage(holder, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
            holder.binding.tvMiddle.setText(messages.get(position).substring(0, 10));
        } else if (position % 5 == 0) {
            shLMRMessage(holder, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
            holder.binding.pgR.setVisibility(View.VISIBLE);
            Picasso.get().load("https://thumbor.forbes.com/thumbor/1280x868/https%3A%2F%2Fspecials-images.forbesimg.com%2Fdam%2Fimageserve%2F42977075%2F960x0.jpg%3Ffit%3Dscale")
                    .into(holder.binding.ivR,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.binding.pgR.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(android.R.drawable.stat_notify_error).fit().into(holder.binding.ivR);
                                    holder.binding.ivR.setBackgroundColor(context.getResources().getColor(R.color.error_gray));
                                    holder.binding.pgR.setVisibility(View.GONE);
                                    Log.d(TAG, "onError: " + e.getMessage());
                                }
                            });
        } else if (position % 7 == 0) {
            shLMRMessage(holder, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
            holder.binding.pgL.setVisibility(View.VISIBLE);
            Picasso.get().load("https://thumbor.forbes.com/thumbor/1280x868/https%3A%2F%2Fspecials-images.forbesimg.com%2Fdam%2Fimageserve%2F42977075%2F960x0.jpg%3Ffit%3Dscale")
                    .into(holder.binding.ivL,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.binding.pgL.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(android.R.drawable.stat_notify_error).fit().into(holder.binding.ivL);
                                    holder.binding.ivL.setBackgroundColor(context.getResources().getColor(R.color.error_gray));
                                    holder.binding.pgR.setVisibility(View.GONE);
                                    Log.d(TAG, "onError:" + e.getMessage());
                                }
                            });
        } else {
            shLMRMessage(holder, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
            holder.binding.tvRMessage.setText(messages.get(position));
        }
    }

    private void shLMRMessage(ViewHolder holder, int left, int middle, int right, int imageLeft, int imageRight) {
        holder.binding.tvLMessage.setVisibility(left);
        holder.binding.tvMiddle.setVisibility(middle);
        holder.binding.tvRMessage.setVisibility(right);
        holder.binding.flLImage.setVisibility(imageLeft);
        holder.binding.flRImage.setVisibility(imageRight);
    }

    boolean flip = false;

    public void addMessage(String message) {

        messages.add(message);
        notifyItemInserted(messages.size() - 1); //starts from zero
        vibrate();
        if (flip) {
            playMusic(YOU);
            flip = false;
        } else {
            playMusic(ME);
            flip = true;
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RvMessageRowBinding binding;

        public ViewHolder(RvMessageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, 100)); // 255 MAX
        } else {
            //deprecated in API 26
            v.vibrate(50);
        }
    }

    private void playMusic(int type) {
        if (type == ME) {
            mediaPlayer = MediaPlayer.create(context, R.raw.tanya);
        } else if (type == YOU) {
            mediaPlayer = MediaPlayer.create(context, R.raw.deepak);

        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });
        mediaPlayer.start();

    }
}
