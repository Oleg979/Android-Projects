package com.olso0919.localizationapplication;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {
    Spinner choseLang;
    TextView choseTextView;
    EditText editText;
    List<RadioButton> radioButtons;
    List<TextView> resViews;
    List<Integer> locales;
    Units currentUnit;
    Map<String, Units> unitsMap = new HashMap<>();
    Map<String, Units> unitsMapRu = new HashMap<>();
    {
        unitsMap.put("meters", Units.M);
        unitsMap.put("centimeters", Units.CM);
        unitsMap.put("kilometers", Units.KM);
        unitsMap.put("miles", Units.ML);
        unitsMap.put("feet", Units.FT);
        unitsMap.put("inches", Units.INCH);
    }

    {
        unitsMapRu.put("метры", Units.M);
        unitsMapRu.put("сантиметры", Units.CM);
        unitsMapRu.put("километры", Units.KM);
        unitsMapRu.put("мили", Units.ML);
        unitsMapRu.put("футы", Units.FT);
        unitsMapRu.put("дюймы", Units.INCH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choseLang = findViewById(R.id.choseLang);
        choseTextView = findViewById(R.id.chooseTextView);
        editText = findViewById(R.id.editText);
        editText.addTextChangedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lang_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choseLang.setAdapter(adapter);
        choseLang.setSelection(0,false);
        choseLang.setOnItemSelectedListener(this);
        resViews = Stream
                        .of(R.id.res1, R.id.res2, R.id.res3, R.id.res4, R.id.res5, R.id.res6)
                        .map(this::findViewById)
                        .map(v -> (TextView) v)
                        .peek(rv -> rv.setText("0 ".concat(rv.getText().toString())))
                        .collect(Collectors.toList());
        radioButtons = Stream
                        .of(R.id.radioButton1, R.id.radioButton2, R.id.radioButton3, R.id.radioButton4, R.id.radioButton5, R.id.radioButton6)
                        .map(this::findViewById)
                        .map(v -> (RadioButton) v)
                        .peek(rb -> rb.setOnClickListener((v) -> {
                            radioButtons.forEach(_rb -> _rb.setChecked(false));
                            rb.setChecked(true);
                            currentUnit = unitsMap.getOrDefault(rb.getText().toString(), unitsMapRu.get(rb.getText().toString()));
                            onTextChanged(editText.getText().toString(), 0, 0, 0);
                        }))
                        .collect(Collectors.toList());
        radioButtons.get(0).callOnClick();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            locales = Arrays.asList(R.string.ml, R.string.ft, R.string.inch, R.string.m, R.string.km, R.string.cm);
            choseTextView.setText(R.string.choose_lang);

        } else {
            locales = Arrays.asList(R.string.ml_ru, R.string.ft_ru, R.string.inch_ru, R.string.m_ru, R.string.km_ru, R.string.cm_ru);
            choseTextView.setText(R.string.choose_lang_ru);
        }
        for(int i = 0; i < locales.size(); i++) {
            radioButtons.get(i).setText(locales.get(i));
            resViews.get(i).setText(locales.get(i));
            resViews.get(i).setText("0 ".concat(resViews.get(i).getText().toString()));
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().isEmpty() || s.toString().startsWith(".")) return;
        double current = Double.parseDouble(s.toString());
        for(int i = 0; i < resViews.size(); i++) {
            TextView tv = resViews.get(i);
            String end = tv.getText().toString().split(" ")[1];
            String begin = String.valueOf(ConversionService.round(ConversionService.convert(current, currentUnit, unitsMap.getOrDefault(end, unitsMapRu.get(end))), 4));
            tv.setText(begin.concat(" ").concat(end));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
}
