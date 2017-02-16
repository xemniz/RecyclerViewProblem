package ru.xmn.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
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

public class MainActivity extends AppCompatActivity {
    private int itemCount = 5;
    private RecyclerView rv;
    private ViewTreeObserver.OnPreDrawListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = ((RecyclerView) findViewById(R.id.rv));
        Button remove = ((Button) findViewById(R.id.remove));
        Button measure = ((Button) findViewById(R.id.measure));
        Button requestlayout = ((Button) findViewById(R.id.requestlayout));
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
                animateRecyclerView();
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
        listener = getOnPreDrawListener();
    }

    @NonNull
    private ViewTreeObserver.OnPreDrawListener getOnPreDrawListener() {
        return new ViewTreeObserver.OnPreDrawListener() {
            private int finalHeight;
            private ValueAnimator animator;
            int mFrames = 0;
            private int recyclerViewLastHeight;

            @Override
            public boolean onPreDraw() {
                switch (mFrames++) {
                    case 0:
                        recyclerViewLastHeight = rv.getHeight();
                        finalHeight = new RecyclerViewMeasureHelper().getMeasuredHeightCustom(rv, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

                        animate(finalHeight);

                        recyclerViewLastHeight = finalHeight;

                        return true;
                    case 1:
                        return true;
                }
                rv.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }

            private void animate(int height) {
                if (animator == null) {
                    animator = ValueAnimator
                            .ofInt(rv.getHeight(), height)
                            .setDuration(300);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            rv.getLayoutParams().height = (int) animation.getAnimatedValue();
                            rv.requestLayout();
                        }
                    });
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mFrames = 0;
                            animator = null;
                            if (recyclerViewLastHeight != rv.getHeight()) {
                                finalHeight = new RecyclerViewMeasureHelper().getMeasuredHeightCustom(rv, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                                animate(finalHeight);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.setInterpolator(new FastOutSlowInInterpolator());
                    animator.start();
                }
            }
        };
    }

    private void animateRecyclerView() {
        rv.getViewTreeObserver().addOnPreDrawListener(listener);
    }
}