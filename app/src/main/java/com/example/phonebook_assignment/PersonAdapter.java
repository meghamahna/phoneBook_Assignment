package com.example.phonebook_assignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonAdapter extends ArrayAdapter {

    Context mContext;
    int layoutRes;
    List<Person> persons;
    //SQLiteDatabase mDatabase;

    DatabaseHelper mDatabase;


    public PersonAdapter( Context mContext, int layoutRes, List<Person> persons, DatabaseHelper mDatabase) {
        super(mContext, layoutRes,persons);
        this.mContext = mContext;
        this.layoutRes = layoutRes;
        this.persons = persons;
        this.mDatabase = mDatabase;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(layoutRes, null);
        TextView firstName = v.findViewById(R.id.first_name);
        TextView lastName = v.findViewById(R.id.last_name);
        TextView phone = v.findViewById(R.id.phone_number);
        TextView addr = v.findViewById(R.id.address);

        final Person person = persons.get(position);
        firstName.setText(person.getFirstName());
        lastName.setText(String.valueOf(person.getLastName()));
        phone.setText(person.getPhoneNumber());
        addr.setText(person.getAddress());
        return v;

    }





    public void updatePerson(final Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_layout_update_person, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final EditText ETfirstname = v.findViewById(R.id.updateFirstName);
        final EditText ETlastname = v.findViewById(R.id.updateLastName);
        final EditText ETaddress = v.findViewById(R.id.updateAddress);
        final EditText ETphone = v.findViewById(R.id.updatePhoneNumber);


        ETfirstname.setText(person.getFirstName());
        ETlastname.setText(person.getLastName());
        ETaddress.setText(person.getAddress());
        ETphone.setText(person.getPhoneNumber());

        v.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname1 = ETfirstname.getText().toString().trim();
                String lastname1 = ETlastname.getText().toString().trim();
                String address1 = ETaddress.getText().toString().trim();
                String phone1 = ETphone.getText().toString().trim();

                if (firstname1.isEmpty()){
                    ETfirstname.setError("firstname field is empty");
                    ETfirstname.requestFocus();
                    return;
                }
                if (lastname1.isEmpty()){
                    ETlastname.setError("lastname field is empty");
                    ETlastname.requestFocus();
                    return;
                }
                if (address1.isEmpty()){
                    ETaddress.setError("address field is empty");
                    ETaddress.requestFocus();
                    return;
                }
                if (phone1.isEmpty()){
                    ETphone.setError("phone field is empty");
                    ETphone.requestFocus();
                    return;
                }
/*
                String sql = " UPDATE employees SET name =?,salary =?,department=? WHERE id = ?";
              mDatabase.execSQL(sql,new String[]{ name,salary,dept, String.valueOf(employee.getId())});
                Toast.makeText(mContext, "employee update", Toast.LENGTH_SHORT).show();

 */
                if(mDatabase.updatePerson(person.getId(), firstname1, lastname1, phone1, address1)){
                    Toast.makeText(mContext, "person update", Toast.LENGTH_SHORT).show();
                    loadPersons();
                }
                else
                    Toast.makeText(mContext, "person not update", Toast.LENGTH_SHORT).show();

                // loadEmployees();
                alertDialog.dismiss();

            }
        });



    }

    private void loadPersons() {

/*
        String sql = "SELECT * FROM employees";
        Cursor cursor = mDatabase.rawQuery(sql, null);

 */
        Cursor cursor = mDatabase.getAllPersons();

        if(cursor.moveToFirst()){
            persons.clear();
            do{
                persons.add(new Person(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }while (cursor.moveToNext());

            cursor.close();
        }
        notifyDataSetChanged();



    }

    public  void update(ArrayList<Person> results){
        persons = new ArrayList<>();
        persons.addAll(results);
        notifyDataSetChanged();
    }

}
