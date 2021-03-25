package com.example.eventhunter.authentication.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.LoginFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import static com.example.eventhunter.MainActivity.AUTH_ACTIVITY_REQUEST_EXTRA;

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
            Intent intent = new Intent();
            intent.putExtra(AUTH_ACTIVITY_REQUEST_EXTRA, "login");
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
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