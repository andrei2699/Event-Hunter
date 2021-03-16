package com.example.eventhunter.ui.register;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.LoginFragmentBinding;
import com.example.eventhunter.databinding.RegisterFragmentBinding;
import com.example.eventhunter.ui.login.LoginFragmentDirections;
import com.example.eventhunter.ui.login.LoginViewModel;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private RegisterFragmentBinding binding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       binding = RegisterFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        binding.registerButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.nav_home);
        });
        binding.goToLogin.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(RegisterFragmentDirections.navigateToLogin());
        });
        mViewModel.getEmailAddress().observe(getViewLifecycleOwner(), email -> {
            binding.editTextEmailAddressRegister.setText(email);
        });
        mViewModel.getPassword().observe(getViewLifecycleOwner(), password -> {
            binding.editTextPasswordRegister.setText(password);
        });
        mViewModel.getName().observe(getViewLifecycleOwner(), name -> {
            binding.editTextNameRegister.setText(name);
        });
        mViewModel.getUserType().observe(getViewLifecycleOwner(), userType -> {
            int index = -1;

            String[] stringArray = getResources().getStringArray(R.array.user_types_array);
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].equals(userType)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                binding.userTypesSpinnerRegister.setSelection(index);
            }
        });

        Spinner userTypesSpinner = binding.userTypesSpinnerRegister;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.user_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypesSpinner.setAdapter(adapter);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}