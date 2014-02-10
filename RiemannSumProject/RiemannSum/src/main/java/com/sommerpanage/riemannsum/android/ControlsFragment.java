package com.sommerpanage.riemannsum.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sommerpanage.android.riemannsum.R;
import com.sommerpanage.riemannsum.model.Function;

import java.util.List;

/**
 * Created by Sommer Panage on 11/1/13.
 */

public class ControlsFragment extends Fragment {

    private static final String TAG = ControlsFragment.class.getSimpleName();

    private final List<Function> mFunctions;
    private OnControlsUpdatedListener mOnControlsUpdatedListener;
    private EditText mMinText, mMaxText;
    private SeekBar mIncrementer;
    private RadioGroup mFunctionRadios;

    public void setOnControlsUpdatedListener(OnControlsUpdatedListener onControlsUpdatedListener) {
        this.mOnControlsUpdatedListener = onControlsUpdatedListener;
    }

    public interface OnControlsUpdatedListener {
        public void onControlsUpdate(Function newFunction, double newMin, double newMax, int newCount);
    }

    public ControlsFragment(List<Function> functions) {
        this.mFunctions = functions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFunctionRadios();
        setupMinMaxText();
        setupIncrementer();
        setupSubmitButton();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controls, container, false);
    }

    private void setupSubmitButton() {
        Button submitButton = (Button) getView().findViewById(R.id.submit_button);
        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Double min = parseDoubleFromTextView(mMinText);
                    final Double max = parseDoubleFromTextView(mMaxText);
                    final int functionID = mFunctionRadios.getCheckedRadioButtonId();
                    final int intervals = mIncrementer.getProgress();
                    if (min != null && max != null && functionID > 0) {
                        final Function function = mFunctions.get(functionID);
                        dismissKeyboardFromView(v);
                        mOnControlsUpdatedListener.onControlsUpdate(function, min, max, intervals);
                    } else {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.controls_invalid_input), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    private void setupMinMaxText() {
        mMinText = createEditText(true);
        mMaxText = createEditText(false);
    }

    private EditText createEditText(final boolean min) {
        EditText text = (EditText) getView().findViewById((min) ? R.id.x_min_text : R.id.x_max_text);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dismissKeyboardFromView(v);
                }
                return true;
            }
        });
        return text;
    }

    private Double parseDoubleFromTextView(TextView v) {
        Double val = null;
        try {
            val = Double.parseDouble(v.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Failed to parse x value from view: " + v.toString());
        }
        return val;
    }

    private void dismissKeyboardFromView(View v) {
        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void setupFunctionRadios() {
        RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.function_radios);
        for (Function f : mFunctions) {
            RadioButton button = new RadioButton(this.getActivity());
            button.setText(getString(f.getNameResourceId()));
            button.setContentDescription(getString(f.getPhoneticNameResourceId()));
            button.setId(mFunctions.indexOf(f));
            radioGroup.addView(button);
        }
        mFunctionRadios = radioGroup;
    }

    private void setupIncrementer() {
        SeekBar seekBar = (SeekBar) getView().findViewById(R.id.interval_count_seekbar);
        seekBar.setKeyProgressIncrement(1);
        setIncrementValue(seekBar.getProgress());
        final String longFormat = getString(R.string.controls_ax_seekbar_long_format);
        final int increment = Math.max(1, Math.round((float) seekBar.getMax() / 5));
        seekBar.setContentDescription(String.format(longFormat, seekBar.getProgress(), increment, increment));

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        setIncrementValue(progress);
                        final String shortFormat = getString(R.string.controls_ax_seekbar_short_format);
                        seekBar.setContentDescription(String.format(shortFormat, progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );
        mIncrementer = seekBar;
    }

    private void setIncrementValue(int value) {
        TextView seekValueText = (TextView) getView().findViewById(R.id.interval_count_value);
        seekValueText.setText(Integer.toString(value));
    }
}