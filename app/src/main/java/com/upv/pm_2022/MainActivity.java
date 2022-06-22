package com.upv.pm_2022;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor; // Rich Text editor
    private TextView mPreview;  // Variable to display the Latex to

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
        findViewById(R.id.action_subscript).setOnClickListener(view->{mEditor.setSubscript();});
        findViewById(R.id.action_superscript).setOnClickListener(view->{mEditor.setSuperscript();});
        findViewById(R.id.action_strikethrough).setOnClickListener(view->{mEditor.setStrikeThrough();});
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
        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });
        findViewById(R.id.action_indent).setOnClickListener(view->{mEditor.setIndent();});
        findViewById(R.id.action_outdent).setOnClickListener(view->{mEditor.setOutdent();});
        findViewById(R.id.action_align_left).setOnClickListener(view->{mEditor.setAlignLeft();});
        findViewById(R.id.action_align_center).setOnClickListener(view->{mEditor.setAlignCenter();});
        findViewById(R.id.action_align_right).setOnClickListener(view->{mEditor.setAlignRight();});
        findViewById(R.id.action_blockquote).setOnClickListener(view->{mEditor.setBlockquote();});
        findViewById(R.id.action_insert_bullets).setOnClickListener(view->{mEditor.setBullets();});
        findViewById(R.id.action_insert_numbers).setOnClickListener(view->{mEditor.setNumbers();});
        findViewById(R.id.action_insert_image).setOnClickListener(view-> {
                mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/" +
                                        "chip.jpg", "dachshund", 320);
        });
        findViewById(R.id.action_insert_youtube).setOnClickListener( view -> {
                mEditor.insertYoutubeVideo("https://www.youtube.com/embed/u9CScOFfCM4");
        });
        findViewById(R.id.action_insert_audio).setOnClickListener(view -> {
                mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/" +
                                        "file_example_MP3_5MG.mp3");
        });
        findViewById(R.id.action_insert_video).setOnClickListener(view -> {
                mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/" +
                                    "1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360);
        });
        findViewById(R.id.action_insert_link).setOnClickListener(view -> {
            mEditor.insertLink("https://github.com/", "Github");
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(view->{mEditor.insertTodo();});
    }
}
