package com.dgproduction.erdeepak_kumar.tourismapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity {
TextView name, place,type,description;
ListView guidelist;
ViewPager viewPager;
DatabaseHelper gdb;
Places_DatabaseHelper pdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedetail);
        name=findViewById(R.id.tv_name_placedetail);
        place=findViewById(R.id.tv_place_placedetail);
        type=findViewById(R.id.tv_type_placedetail);
        description=findViewById(R.id.tv_description_placedetail);
        guidelist=findViewById(R.id.lv_guide_placedetail);
        viewPager=findViewById(R.id.viewpager);


        gdb = new DatabaseHelper(this);

        Bundle extras =getIntent().getExtras();
        if(extras!=null)
        {
            Places details = (Places) this.getIntent().getSerializableExtra("PLACESDETAILS");

            name.setText(details.getName());
            place.setText(details.getPlace());
            description.setText(details.getDescription());
            type.setText(details.getType());
            ImageAdapter adapter = new ImageAdapter(this,new String[]{details.getUrl1(),details.getUrl2(),details.getUrl3()});
            viewPager.setAdapter(adapter);
            //location.setText(details.getLanguage());
            Toast.makeText(getApplicationContext(),details.getName()+"\n"+details.getPlace(),Toast.LENGTH_SHORT).show();
            //Glide.with(this).load("https://image.tmdb.org/t/p/w500"+details.getPoster_path()).into(image);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"ERRor",Toast.LENGTH_SHORT).show();

        }
        
        
//ListView
        final ArrayList<Guide> guidedetail= gdb.getAllGuide();
        final MyListAdapter adapter= new MyListAdapter(this,guidedetail);
        guidelist.setAdapter(adapter);
        guidelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapter.getItem(i).getName();
                final int pos=i;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlaceDetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.cutom_dialog_guide_display, null);
                final EditText name = (EditText) mView.findViewById(R.id.et_name_dialog_gd);
                final EditText area = (EditText) mView.findViewById(R.id.et_area_dialog_gd);
                final EditText price = (EditText) mView.findViewById(R.id.et_price_dialog_gd);
                final EditText mobile = (EditText) mView.findViewById(R.id.et_mobile_gd);
                final EditText regno=(EditText) mView.findViewById(R.id.et_reg_dialog_gd);
                final RatingBar rating = (RatingBar) mView.findViewById(R.id.rat_dialog_gd);


                name.setText(guidedetail.get(pos).getName().toString());
                area.setText(guidedetail.get(pos).getArea().toString());
                price.setText(guidedetail.get(pos).getPackage_price().toString());
                mobile.setText(guidedetail.get(pos).getMobile_no().toString());
                regno.setText(guidedetail.get(pos).getRegno().toString());
                rating.setRating(guidedetail.get(pos).getRating());


                Button mmsg = (Button) mView.findViewById(R.id.btn_msg_dialog_gd);
                Button mcall = (Button) mView.findViewById(R.id.btn_call_dialog_gd);

                mcall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+ Uri.encode(mobile.getText().toString().trim())));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);


                    }
                });
                mmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,guidedetail.get(pos).getName());
                        startActivity(intent);


                    }
                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });


    }
}
