package com.upv.pm_2022;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Rich Text to Latex app
 * <p>
 * Based on 2020 Wasabeef
 * <p>
 * Modified by Eduardo Uriegas in June 2022 to support Latex parsing
 * <p>
 * Commented code is functionality not yet implemented in the HTML2LATEX parser
 */
public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor; // Rich Text editor
    private TextView mPreview;  // Variable to display the Latex to
    private Button exportBtn;
    private final String OUTPUT = "output.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditor = findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(24);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        mPreview = findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(text -> {mPreview.setText(text);});

        findViewById(R.id.action_undo).setOnClickListener(view->{mEditor.undo();});
        findViewById(R.id.action_redo).setOnClickListener(view->{mEditor.redo();});
        findViewById(R.id.action_bold).setOnClickListener(view->{mEditor.setBold();});
        findViewById(R.id.action_italic).setOnClickListener(view->{mEditor.setItalic();});
//        findViewById(R.id.action_subscript).setOnClickListener(view->{mEditor.setSubscript();});
//        findViewById(R.id.action_superscript).setOnClickListener(view->{mEditor.setSuperscript();});
        findViewById(R.id.action_strikethr).setOnClickListener(view->{mEditor.setStrikeThrough();});
        findViewById(R.id.action_underline).setOnClickListener(view->{mEditor.setUnderline();});
        findViewById(R.id.action_heading1).setOnClickListener(view->{mEditor.setHeading(1);});
        findViewById(R.id.action_heading2).setOnClickListener(view->{mEditor.setHeading(2);});
        findViewById(R.id.action_heading3).setOnClickListener(view->{mEditor.setHeading(3);});
        findViewById(R.id.action_heading4).setOnClickListener(view->{mEditor.setHeading(4);});
        findViewById(R.id.action_heading5).setOnClickListener(view->{mEditor.setHeading(5);});
        findViewById(R.id.action_heading6).setOnClickListener(view->{mEditor.setHeading(6);});
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });
//        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
//            BUG: Transparent background not working, displays black Pixel 2 API 3
//            private boolean isChanged;
//            @Override
//            public void onClick(View v) {
//                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//                isChanged = !isChanged;
//            }
//        });
//        findViewById(R.id.action_indent).setOnClickListener(view->{mEditor.setIndent();});
//        findViewById(R.id.action_outdent).setOnClickListener(view->{mEditor.setOutdent();});
//        findViewById(R.id.action_align_left).setOnClickListener(view->{mEditor.setAlignLeft();});
//        findViewById(R.id.action_align_center).setOnClickListener(view->{mEditor.setAlignCenter();});
//        findViewById(R.id.action_align_right).setOnClickListener(view->{mEditor.setAlignRight();});
        findViewById(R.id.action_insert_bullets).setOnClickListener(view->{mEditor.setBullets();});
        findViewById(R.id.action_insert_numbers).setOnClickListener(view->{mEditor.setNumbers();});
        // TODO: Cannot dequote
        findViewById(R.id.action_blockquote).setOnClickListener(view->{mEditor.setBlockquote();});
        findViewById(R.id.action_insert_image).setOnClickListener(view-> {
            // TODO: Alert intent to select image, add default image
            //       Ask if this should be supported in V1 since it is tricky to generate .tex with
            //       this feature.
                mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/" +
                                        "chip.jpg", "dachshund", 320);
        });
        findViewById(R.id.action_insert_link).setOnClickListener(view -> {
            // TODO: Alert intent to select link, add default link
            mEditor.insertLink("https://github.com/", "Github");
        });

        exportBtn = findViewById(R.id.export);
        exportBtn.setOnClickListener(view -> {
            File file = new File(Environment.getExternalStorageDirectory().toString() + '/'
                    + OUTPUT);
            try{
                file.delete();
                file.createNewFile();
                FileWriter out = new FileWriter(Environment.getExternalStorageDirectory()
                        .toString() + '/' + OUTPUT);
                // TODO: Add parser
                out.append(mPreview.getText().toString()); out.flush(); out.close();
                Toast.makeText(getBaseContext(), "DB exported into root folder",
                        Toast.LENGTH_LONG).show();
            } catch ( Exception e ) { // Catch IO or SQL exception
                Toast.makeText(getBaseContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
