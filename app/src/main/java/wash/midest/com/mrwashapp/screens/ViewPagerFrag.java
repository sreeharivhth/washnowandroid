package wash.midest.com.mrwashapp.screens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wash.midest.com.mrwashapp.R;


public class ViewPagerFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ViewPagerFrag.class.getName();
    private String mParam1;
    private String mParam2;


    public ViewPagerFrag() {
        // Required empty public constructor
    }

    public static ViewPagerFrag newInstance(String param1, String param2) {
        ViewPagerFrag fragment = new ViewPagerFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_pager, container, false);

        TextView txtViewMain = view.findViewById(R.id.textViewOne);
        txtViewMain.setText(getArguments().getString(ARG_PARAM1));

        TextView txtViewSub = view.findViewById(R.id.subtextcontent);
        txtViewSub.setText(getArguments().getString(ARG_PARAM2));

        TextView txtViewMini = view.findViewById(R.id.minitextcontent);
        txtViewMini.setText(getActivity().getResources().getString(R.string.offer_validity));



        Log.d(TAG,"PARAM = "+ getArguments().getString(ARG_PARAM1));
        return view;
    }

}
