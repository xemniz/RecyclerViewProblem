package ru.xmn.myapplication;

import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.support.transition.TransitionSet.ORDERING_SEQUENTIAL;

public class MainActivity extends AppCompatActivity {
    private int itemCount = 5;
    private RecyclerView rv;
    private ViewTreeObserver.OnPreDrawListener listener;
    private LinearLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = ((RecyclerView) findViewById(R.id.rv));
        container = ((LinearLayout) findViewById(R.id.container));

        Button remove = ((Button) findViewById(R.id.remove));
        Button measure = ((Button) findViewById(R.id.measure));
        final Button requestlayout = ((Button) findViewById(R.id.requestlayout));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return itemCount;
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionSet set = new TransitionSet();
                set.setOrdering(ORDERING_SEQUENTIAL)
                        .addTransition(new Fade(Fade.OUT))
                        .addTransition(new ChangeBounds().setStartDelay(150))
                        .addTransition(new Fade(Fade.IN))
                        .excludeChildren(RecyclerView.class, true);
                TransitionManager.beginDelayedTransition(container, set);
                if (itemCount-- < 2) {
                    rv.getAdapter().notifyDataSetChanged();
                    itemCount = 5;
                } else {
                    rv.getAdapter().notifyItemRemoved(1);
                }

            }
        });
        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            }
        });
        requestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                rv.requestLayout();
            }
        });
    }
}