package elamien.abdullah.shopemall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elamien.abdullah.shopemall.R;


public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrderStatusViewHolder> {

    private List<String> mOrderStatus;
    private Context mContext;

    public OrderStatusAdapter(List<String> mOrderStatus, Context mContext) {
        this.mOrderStatus = mOrderStatus;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderStatusView = LayoutInflater.from(mContext).inflate(R.layout.list_item_order_status, parent, false);
        return new OrderStatusViewHolder(orderStatusView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusViewHolder holder, int position) {
        String currentStatus = mOrderStatus.get(position);

        OrderStatus status = getOrderStatus(currentStatus);

        switch (status) {
            case PROCESS:
                holder.mOrderStatus.setText(R.string.order_status_process_label);
                holder.mOrderTracker.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_time));
                break;
            case PACKED:
                holder.mOrderStatus.setText(R.string.order_status_packed_label);
                holder.mOrderTracker.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_pack));
                break;
            case SHIPPED:
                holder.mOrderStatus.setText(R.string.order_status_shipped_label);
                holder.mOrderTracker.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_shipped));
                break;
            case ARRIVED:
                holder.mOrderStatus.setText(R.string.order_status_arrived_label);
                holder.mOrderTracker.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_arrived_country));
                break;
            case DELIVERED:
                holder.mOrderStatus.setText(R.string.order_status_delivered_label);
                holder.mOrderTracker.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_delivered_now));
                break;
        }
    }

    private OrderStatus getOrderStatus(String orderStatus) {
        OrderStatus status = OrderStatus.PROCESS;

        if (orderStatus.toLowerCase().equals(mContext.getString(R.string.order_status_packed).toLowerCase())) {
            status = OrderStatus.PACKED;
        } else if (orderStatus.toLowerCase().equals(mContext.getString(R.string.order_status_shipped).toLowerCase())) {
            status = OrderStatus.SHIPPED;
        } else if (orderStatus.toLowerCase().equals(mContext.getString(R.string.order_status_arrived).toLowerCase())) {
            status = OrderStatus.ARRIVED;
        } else if (orderStatus.toLowerCase().equals(mContext.getString(R.string.order_status_delivered).toLowerCase())) {
            status = OrderStatus.DELIVERED;
        }

        return status;
    }

    @Override
    public int getItemCount() {
        return mOrderStatus == null ? 0 : mOrderStatus.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    private enum OrderStatus {
        PROCESS,
        PACKED,
        SHIPPED,
        ARRIVED,
        DELIVERED
    }

    public class OrderStatusViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderStatusTimeLineView)
        TimelineView mOrderTracker;
        @BindView(R.id.orderStatusTextView)
        TextView mOrderStatus;

        public OrderStatusViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOrderTracker.initLine(viewType);
        }
    }
}
