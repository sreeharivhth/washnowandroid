package wash.midest.com.mrwashapp.screens.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import wash.midest.com.mrwashapp.R;

public class OrderDetailRow extends LinearLayout implements View.OnClickListener{

    TextView itemOne;
    TextView itemTwo;
    TextView itemThree;
    String txtOne;
    String txtTwo;
    String txtThree;

    public OrderDetailRow(Context context, String txtOne, String txtTwo, String txtThree) {
        super(context);
        this.txtOne=txtOne;
        this.txtTwo=txtTwo;
        this.txtThree=txtThree;
        initialize();
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.order_detail_row, this, true);
        itemOne=findViewById(R.id.cell_1);
        itemTwo=findViewById(R.id.cell_2);
        itemThree=findViewById(R.id.cell_3);

        itemOne.setText(txtOne);
        itemTwo.setText(txtTwo);
        itemThree.setText(txtThree);

    }

    @Override
    public void onClick(View v) {
        
    }
}
