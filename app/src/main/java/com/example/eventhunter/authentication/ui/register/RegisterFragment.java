package com.example.eventhunter.authentication.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eventhunter.R;
import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.databinding.RegisterFragmentBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static com.example.eventhunter.MainActivity.AUTH_ACTIVITY_REQUEST_EXTRA;

public class RegisterFragment extends Fragment {

    private RegisterFragmentBinding binding;

    @Injectable()
    private AuthenticationService authenticationService;

    public RegisterFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = RegisterFragmentBinding.inflate(inflater, container, false);

        binding.registerButton.setOnClickListener(view -> {
            String email = binding.editTextEmailAddressRegister.getText().toString();
            String password = binding.editTextPasswordRegister.getText().toString();
            String name = binding.editTextNameRegister.getText().toString();
            String userType = binding.userTypesSpinnerRegister.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || userType.isEmpty()) {
                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!validateEmail(email)) {
                Snackbar.make(view, "Invalid Email Address", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            authenticationService.register(email, password, name, userType, errorMessage -> {
                if (errorMessage == null) {
                    Intent intent = new Intent();
                    intent.putExtra(AUTH_ACTIVITY_REQUEST_EXTRA, "register");
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        });

        binding.goToLogin.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(RegisterFragmentDirections.navigateToLogin());
        });

        Spinner userTypesSpinner = binding.userTypesSpinnerRegister;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.user_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypesSpinner.setAdapter(adapter);

        View view = binding.getRoot();
        return view;
    }

    private boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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