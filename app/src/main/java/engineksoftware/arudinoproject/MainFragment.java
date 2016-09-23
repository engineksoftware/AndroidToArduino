package engineksoftware.arudinoproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);
        Button convert = (Button) view.findViewById(R.id.buttonConvert);
        final EditText base10 = (EditText) view.findViewById(R.id.editBase10);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(base10.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please enter a number from 0-255.",Toast.LENGTH_SHORT).show();
                }else
                if(!base10.getText().toString().matches("\\d+(?:\\.\\d+)?")){
                    Toast.makeText(getActivity(),"Please enter a number from 0-255.",Toast.LENGTH_SHORT).show();
                }else{
                    ((MainActivity)getActivity()).sendBase10(base10.getText().toString());
                }

            }
        });

        return view;
    }
}
