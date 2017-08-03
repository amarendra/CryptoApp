package amar.im.cryptoapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
    public static final String TAG = "CryptoTest";

    private EditText mClearTextET, mKeyET;
    private Button mBtn;
    private TextView mOutputTV;
    private RadioGroup mRG;

    // 1 encrypt; 0 decrypt
    private void run(int op) {
        String output = "N/A";

        try {
            if (op == 0) {
                output = KeyHelper.getDecryptedText(getInputText(true), getInputText(false));
            } else if (op == 1) {
                output = KeyHelper.getEncryptedText(getInputText(true), getInputText(false));
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        mOutputTV.setText(output);
    }

    private String getInputText(boolean textToOperateOn) {
        return textToOperateOn ? mClearTextET.getText().toString() : mKeyET.getText().toString();
    }

    private int getCryptoChoice() {
        int selectedRbId = mRG.getCheckedRadioButtonId();

        if (selectedRbId == R.id.rb_decrypt) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOutputTV = findViewById(R.id.output_text);

        mClearTextET = findViewById(R.id.input_clear_text);
        mClearTextET.setText(KeyHelper.mClearText);
        mKeyET = findViewById(R.id.input_key_text);
        mKeyET.setText(KeyHelper.mKeyString);

        mRG = findViewById(R.id.rbg_choice);

        mBtn = findViewById(R.id.submit);

        run(1); // first run shows the default/hardcoded text values encryption and decryption

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run(getCryptoChoice());
            }
        });
    }
}
