package com.olso0919.calculatorapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.stream.Stream;

enum Signs {
    PLUS, MINUS, MULTIPLE, DIVIDE
}

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    private String accumulator = "";
    private TextView accumulatorView;
    private TextView memoryView;
    private boolean isCurrentSet;
    private double current;
    private double memory;
    private Signs currentSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accumulatorView = findViewById(R.id.textView);
        memoryView = findViewById(R.id.textView2);
        updateAccumulator(accumulator);
        updateMemory("");
        setHandlersToDigitButtons();
    }

    public void setHandlersToDigitButtons() {
        Stream
                .of(R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                    R.id.button5, R.id.button6, R.id.button7, R.id.button8,
                    R.id.button9, R.id.button0)
                .map(this::findViewById)
                .forEach(res -> ((View) res).setOnClickListener(view -> {
                    String digit = ((Button) view).getText().toString();
                    if(accumulator.equals("0") && digit.equals("0")) return;
                    accumulator = accumulator + ((Button) view).getText().toString();
                    accumulatorView.setText(accumulator);
                }));
    }

    public void updateAccumulator(String val) {
        accumulatorView.setText(val);
        accumulator = val;
    }

    public void updateMemory(String val) {
        memoryView.setText(val);
    }

    public void onEraseClick(View view) {
        if(accumulator.isEmpty()) return;
        updateAccumulator(accumulator.substring(0, accumulator.length() - 1));
    }

    public void onDotClick(View view) {
        if(accumulator.contains(".") || accumulator.startsWith(".") || accumulator.isEmpty()) return;
        updateAccumulator(accumulator + ".");
    }
    public void onClearClick(View view) {
        updateAccumulator("");
        updateMemory("");
        isCurrentSet = false;
    }

    public void onAddClick(View view) {
        if(isCurrentSet  || accumulator.isEmpty()) return;
        isCurrentSet = true;
        currentSign = Signs.PLUS;
        memory = Double.parseDouble(accumulator);
        updateMemory(accumulator + " + ");
        updateAccumulator("");
    }

    public void onSubtractClick(View view) {
        if(isCurrentSet || accumulator.isEmpty()) return;
        isCurrentSet = true;
        currentSign = Signs.MINUS;
        memory = Double.parseDouble(accumulator);
        updateMemory(accumulator + " - ");
        updateAccumulator("");
    }

    public void onMultipleClick(View view) {
        if(isCurrentSet || accumulator.isEmpty()) return;
        isCurrentSet = true;
        currentSign = Signs.MULTIPLE;
        memory = Double.parseDouble(accumulator);
        updateMemory(accumulator + " * ");
        updateAccumulator("");
    }

    public void onDivideClick(View view) {
        if(isCurrentSet || accumulator.isEmpty()) return;
        isCurrentSet = true;
        currentSign = Signs.DIVIDE;
        memory = Double.parseDouble(accumulator);
        updateMemory(accumulator + " / ");
        updateAccumulator("");
    }

    private void showErrorNotification(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 10);
        toast.show();
    }

    public void onSqrtClick(View view) {
        if(accumulator.isEmpty()) return;
        current = Double.parseDouble(accumulator);
        if(current < 0) {
            showErrorNotification("Недопустимая операция!");
            return;
        }
        double res = Math.sqrt(current);
        updateMemory("√" + accumulator + "=");
        String updatedAcc = String.valueOf(res);
        if(updatedAcc.endsWith((".0"))) {
            updatedAcc = updatedAcc.substring(0, updatedAcc.length() - 2);
        }
        updateAccumulator(updatedAcc);
    }

    public void onReverseClick(View view) {
        if(accumulator.isEmpty()) return;
        current = Double.parseDouble(accumulator);
        if(current == 0) {
            showErrorNotification("Делить на 0 запрещено!");
            return;
        }
        double res = 1.0 / current;
        updateMemory("1 / " + accumulator + "=");
        String updatedAcc = String.valueOf(res);
        if(updatedAcc.endsWith((".0"))) {
            updatedAcc = updatedAcc.substring(0, updatedAcc.length() - 2);
        }
        updateAccumulator(updatedAcc);
    }

    public void onChangeSignClick(View view) {
        if(accumulator.isEmpty()) return;
        current = Double.parseDouble(accumulator);
        double res = -current;
        String updatedAcc = String.valueOf(res);
        if(updatedAcc.endsWith((".0"))) {
            updatedAcc = updatedAcc.substring(0, updatedAcc.length() - 2);
        }
        updateAccumulator(updatedAcc);
    }

    public void onResClick(View view) {
        if(!isCurrentSet || accumulator.isEmpty()) return;
        current = Double.parseDouble(accumulator);
        double res = 0.0;
        switch (currentSign) {
            case PLUS:
                res = memory + current;
                break;
            case MINUS:
                res =  memory - current;
                break;
            case DIVIDE:
                if(current == 0) {
                    showErrorNotification("Делить на 0 запрещено!");
                    updateAccumulator("");
                    updateMemory("");
                    isCurrentSet = false;
                    return;
                }
                res = memory / current;
                break;
            case MULTIPLE:
                res = memory * current;
                break;
        }
        isCurrentSet = false;
        updateMemory(memoryView.getText() + accumulator + " = ");
        String updatedAcc = String.valueOf(res);
        if(updatedAcc.endsWith((".0"))) {
            updatedAcc = updatedAcc.substring(0, updatedAcc.length() - 2);
        }
        updateAccumulator(updatedAcc);
    }
}
