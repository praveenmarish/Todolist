package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button newtask=findViewById(R.id.newtask);

        final ListView listView=findViewById(R.id.listview);
        final testadapter adapter=new testadapter();
        readinfo();
        adapter.setData(list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog= new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list);
                            }
                        })
                        .setNegativeButton("No",null)
                        .create();
                dialog.show();

            }
        });

        newtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskinput=new EditText(MainActivity.this);
                taskinput.setSingleLine();
                AlertDialog dialog= new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add a new task")
                        .setMessage("what is your new task?")
                        .setView(taskinput)
                        .setPositiveButton("Add task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskinput.getText().toString());
                                adapter.setData(list);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveinfo();
    }

    private void saveinfo(){
        try {
            File file=new File(this.getFilesDir(),"saved");
            FileOutputStream fout=new FileOutputStream(file);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fout));

            for (int i=0;i<list.size();i++){
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.close();
            fout.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readinfo(){
        File file=new File(this.getFilesDir(),"saved");
        if (!file.exists()){
            return;
        }
        try {
            FileInputStream is=new FileInputStream(file);
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String line=reader.readLine();
            while (line!=null){
                list.add(line);
                line=reader.readLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    class testadapter extends BaseAdapter{

        List<String> list=new ArrayList<>();
        void setData(List<String> mList){
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater= (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.item,parent,false);
            }
            final TextView textView=convertView.findViewById(R.id.textView);
            textView.setText(list.get(position));
            return convertView;
        }


    }
}