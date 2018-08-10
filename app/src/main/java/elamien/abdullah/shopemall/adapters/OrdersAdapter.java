package elamien.abdullah.shopemall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.activities.OrderDetailsActivity;
import elamien.abdullah.shopemall.model.Order;


@SuppressWarnings("ALL")
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<Order> mUserOrdersList;
    private Context mContext;

    public OrdersAdapter(List<Order> mUserOrdersList, Context mContext) {
        this.mUserOrdersList = mUserOrdersList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ordersView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_orders, parent, false);

        return new OrdersViewHolder(ordersView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Order order = mUserOrdersList.get(position);

        holder.mOrderKeyTextView.setText(String.valueOf(mContext.getString(R.string.order_key_label) + order.getOrderKey()));

        holder.mOrderPaymentPriceTextView.setText(String.valueOf("$" + new DecimalFormat().format(order.getTotalPriceAfterDiscount())));
        if (order.getOrderStatus() != null) {
            String orderStatus = order.getOrderStatus().get(order.getOrderStatus().size() - 1);
            holder.mOrderStatusTextView.setText(orderStatus);
        } else {
            holder.mOrderStatusTextView.setText(R.string.order_status_process);
        }
    }

    @Override
    public int getItemCount() {
        return mUserOrdersList == null ? 0 : mUserOrdersList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userOrderKeyTextView)
        TextView mOrderKeyTextView;
        @BindView(R.id.userOrderPriceTextView)
        TextView mOrderPaymentPriceTextView;
        @BindView(R.id.userOrderStatusTextView)
        TextView mOrderStatusTextView;

        public OrdersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.userOrderCardView)
        public void onOrderClick() {
            Intent intent = new Intent(mContext, OrderDetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mUserOrdersList.get(getAdapterPosition()).getOrderKey());
            mContext.startActivity(intent);
        }
    }
}

