package com.example.ahmedkhames.pharmacyclientapp.OrdersListing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.ahmedkhames.pharmacyclientapp.R;

import java.util.ArrayList;

import io.rmiri.skeleton.Master.IsCanSetAdapterListener;

/**
 * Created by Ahmed.Khames on 1/30/2018.
 */
public class OrdersFragment extends Fragment {
    private RecyclerView reViewOdrers;
   // private List<Order> orderItem;
    private OrdersAdapter ordersAdapter;
    private ArrayList<Order> orderItem = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_order_list, container, false);
        return v;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       /* ObservableScrollView scrollView1=view.findViewById(R.id.scrollView2);
        scrollView1.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
                if (scrollState == ScrollState.UP) {
                    if (ab.isShowing()) {
                        ab.hide();
                    }
                } else if (scrollState == ScrollState.DOWN) {
                    if (!ab.isShowing()) {
                        ab.show();
                    }
                }
            }
        });*/
        reViewOdrers = (RecyclerView) view.findViewById(R.id.order_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        reViewOdrers.setLayoutManager(llm);
        reViewOdrers.setHasFixedSize(true);
        Order a= new Order();
        a.setOrderId("44555");
        a.setDate("10");
        a.setMonth("May");
        a.setPrice(Double.valueOf(2.2));
         a.setStatus("0");
        orderItem.add(a);
        Order b= new Order();
        b.setOrderId("44555");
        b.setDate("10");
        b.setMonth("June");
        b.setPrice(Double.valueOf(2.2));
        b.setStatus("1");
        orderItem.add(b);

        Order c= new Order();
        Order d= new Order();
        Order e= new Order();

        c.setOrderId("44555");
        c.setDate("10");
        c.setMonth("May");
        c.setPrice(Double.valueOf(2.2));
        c.setStatus("1");
        orderItem.add(c);
        d.setOrderId("44555");
        d.setDate("10");
        d.setMonth("May");
        d.setPrice(Double.valueOf(2.2));
        d.setStatus("0");
        orderItem.add(d);
        e.setOrderId("44555");
        e.setDate("10");
        e.setMonth("May");
        e.setPrice(Double.valueOf(2.2));
        e.setStatus("0");
        orderItem.add(e);
        initializeAdapter();

    }

    private void initializeAdapter() {
        ordersAdapter = new OrdersAdapter(getActivity(), orderItem,reViewOdrers, new IsCanSetAdapterListener() {
            @Override
            public void isCanSet() {
                reViewOdrers.setAdapter(ordersAdapter);
             //   reViewOdrers.notify();
            }
        });
    }
}
