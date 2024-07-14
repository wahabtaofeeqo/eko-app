package com.wristband.eko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.wristband.eko.data.AppDatabase;
import com.wristband.eko.data.Result;
import com.wristband.eko.data.SessionManager;
import com.wristband.eko.databinding.ActivityMainBinding;
import com.wristband.eko.entities.Attendance;
import com.wristband.eko.entities.User;
import com.wristband.eko.vm.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel viewModel;

    private ActivityMainBinding binding;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        binding.verify.setOnClickListener(view -> {
            this.doVerify();
        });

        sessionManager = new SessionManager(this);
        binding.name.setText(getString(R.string.greeting, sessionManager.getName()));
        binding.place.setText(sessionManager.getPlace());

        List<String> list = new ArrayList<>();
        list.add("Breakfast");
        list.add("Launch");
        list.add("Dinner");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        binding.reason.setAdapter(adapter);

        viewModel.getVerification().observe(this, response -> {
            if(response == null) return;

            if(response.getStatus()) {
                binding.code.setText("");
                Toasty.success(this, response.getMessage()).show();
            }
            else {
                Toasty.error(this, response.getMessage()).show();
            }

            //
            binding.verify.setEnabled(true);
            binding.progress.setVisibility(View.GONE);
        });

        binding.code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length() >= 3) {
                    doVerify();
                }
            }
        });
    }

    private void doVerify() {
        String code = binding.code.getText().toString();
        if(!code.trim().isEmpty()) {
            binding.verify.setEnabled(false);
            binding.progress.setVisibility(View.VISIBLE);

            //
            String reason = binding.reason.getText().toString();
            viewModel.doVerify(code, Objects.requireNonNull(sessionManager.getPlace()), reason);
        }
    }
}