package com.example.eventhunter.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.LoginFragmentBinding;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding.loginButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.nav_home);
        });
        binding.goToRegister.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(LoginFragmentDirections.navigateToRegister());
        });
        mViewModel.getEmailAddress().observe(getViewLifecycleOwner(), email -> {
            binding.editTextEmailAddress.setText(email);
        });
        mViewModel.getPassword().observe(getViewLifecycleOwner(), password -> {
            binding.editTextPassword.setText(password);
        });
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