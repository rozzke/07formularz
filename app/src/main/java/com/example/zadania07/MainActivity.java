package com.example.zadania07;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zadania07.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinners();
        setupListeners();
    }


    private void setupSpinners() {
        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(
                this, R.array.services, android.R.layout.simple_spinner_item
        );
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerService.setAdapter(serviceAdapter);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(
                this, R.array.hours, android.R.layout.simple_spinner_item
        );
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerHour.setAdapter(hourAdapter);
    }

    private void setupListeners() {
        binding.btnDate.setOnClickListener(v -> showDatePicker());

        binding.rgType.setOnCheckedChangeListener((group, checkedId) ->
                updateAddressVisibility(checkedId == R.id.rbStationary)
        );

        binding.btnReserve.setOnClickListener(v -> {
            if (validateForm()) {
                showConfirmationDialog();
            }
        });
    }


    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%02d.%02d.%04d", dayOfMonth, month + 1, year);
                    binding.tvSelectedDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void updateAddressVisibility(boolean show) {
        binding.tilAddress.setVisibility(show ? View.VISIBLE : View.GONE);
        if (!show) {
            if (binding.etAddress.getText() != null) {
                binding.etAddress.getText().clear();
            }
            binding.etAddress.setError(null);
        }
    }

    private boolean validateForm() {


        if (binding.spinnerService.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Wybierz usługę", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Wybierz datę wizyty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.spinnerHour.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Wybierz godzinę", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.etName.getText() == null || binding.etName.getText().toString().trim().isEmpty()) {
            binding.etName.setError("Podaj imię i nazwisko");
            binding.etName.requestFocus();
            return false;
        } else {
            binding.etName.setError(null);
        }

        String phone = binding.etPhone.getText().toString().trim();
        if (phone.length() < 9) {
            binding.etPhone.setError("Podaj poprawny numer telefonu");
            binding.etPhone.requestFocus();
            return false;
        } else {
            binding.etPhone.setError(null);
        }

        if (binding.rbStationary.isChecked() &&
                (binding.etAddress.getText() == null || binding.etAddress.getText().toString().trim().isEmpty())) {
            binding.etAddress.setError("Podaj adres");
            binding.etAddress.requestFocus();
            return false;
        } else {
            binding.etAddress.setError(null);
        }

        if (!binding.cbConsent.isChecked()) {
            Toast.makeText(this, "Musisz wyrazić zgodę", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showConfirmationDialog() {
        String service = binding.spinnerService.getSelectedItem().toString();
        String hour = binding.spinnerHour.getSelectedItem().toString();
        String name = binding.etName.getText().toString();
        String type = binding.rbStationary.isChecked() ? "Stacjonarna" : "Online";

        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie rezerwacji")
                .setMessage(
                        "Czy na pewno chcesz zarezerwować?\n\n" +
                                "Usługa: " + service + "\n" +
                                "Data: " + selectedDate + "  " + hour + "\n" +
                                "Pacjent: " + name + "\n" +
                                "Rodzaj: " + type
                )
                .setPositiveButton("Tak, rezerwuję", (dialog, which) -> performReservation())
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void performReservation() {
        StringBuilder summary = new StringBuilder();
        summary.append("Zarezerwowano!\n");
        summary.append("Usługa: ").append(binding.spinnerService.getSelectedItem()).append("\n");
        summary.append("Data: ").append(selectedDate).append(" ").append(binding.spinnerHour.getSelectedItem()).append("\n");
        summary.append("Pacjent: ").append(binding.etName.getText()).append("\n");
        summary.append("Tel: ").append(binding.etPhone.getText()).append("\n");
        summary.append("Rodzaj: ").append(binding.rbStationary.isChecked() ? "Stacjonarna" : "Online").append("\n");

        if (binding.rbStationary.isChecked()) {
            summary.append("Adres: ").append(binding.etAddress.getText()).append("\n");
        }

        summary.append("SMS: ").append(binding.switchSms.isChecked() ? "Tak" : "Nie");

        binding.tvSummary.setText(summary.toString());

        Snackbar.make(binding.getRoot(), "Wizyta zarezerwowana!", Snackbar.LENGTH_LONG)
                .setAction("Cofnij", v -> {
                    binding.tvSummary.setText("");
                    Toast.makeText(this, "Rezerwacja cofnięta", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}