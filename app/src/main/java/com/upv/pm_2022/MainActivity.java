package com.upv.pm_2022;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Rich Text to Latex app. Based on 2020 Wasabeef
 * <p>
 * Add support for Latex parsing in June 2022 by Eduardo Uriegas.
 * <p>
 * Commented code is functionality not yet implemented in the HTML2LATEX parser
 */
public class MainActivity extends AppCompatActivity {

    private RichEditor mEditor; // Rich Text editor
    private Button exportBtn;
    private final String OUTPUT = "latex_file";

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

        findViewById(R.id.action_undo).setOnClickListener(view->{mEditor.undo();});
        findViewById(R.id.action_redo).setOnClickListener(view->{mEditor.redo();});
        findViewById(R.id.action_bold).setOnClickListener(view->{mEditor.setBold();});
        findViewById(R.id.action_italic).setOnClickListener(view->{mEditor.setItalic();});
        findViewById(R.id.action_strikethr).setOnClickListener(view->{mEditor.setStrikeThrough();});
        findViewById(R.id.action_underline).setOnClickListener(view->{mEditor.setUnderline();});
        findViewById(R.id.action_heading1).setOnClickListener(view->{mEditor.setHeading(1);});
        findViewById(R.id.action_heading2).setOnClickListener(view->{mEditor.setHeading(2);});
        findViewById(R.id.action_heading3).setOnClickListener(view->{mEditor.setHeading(3);});
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });
        findViewById(R.id.action_insert_bullets).setOnClickListener(view->{mEditor.setBullets();});
        findViewById(R.id.action_insert_numbers).setOnClickListener(view->{mEditor.setNumbers();});
        // TODO: Cannot dequote
        findViewById(R.id.action_blockquote).setOnClickListener(view->{mEditor.setBlockquote();});

        exportBtn = findViewById(R.id.export);
        exportBtn.setOnClickListener(view -> {
            File file = new File(Environment.getExternalStorageDirectory().toString() +
                                 '/' + OUTPUT + ".tex");
            try{
                file.delete(); file.createNewFile();
                FileWriter out = new FileWriter(Environment.getExternalStorageDirectory()
                        .toString() + '/' + OUTPUT + ".tex");
                // Generate .tex and .pdf files and show the latest
                String tex_string = HTMLParser.toLatex(mEditor.getHtml());
                out.append(tex_string); out.flush(); out.close();
                Toast.makeText(getBaseContext(), "Tex file exported into root folder",
                        Toast.LENGTH_LONG).show();
                // TODO: Execute termux command
                //texlive();
                open_file(OUTPUT + ".pdf");
            } catch ( HTMLParser.HTML2LatexException e ) {
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            } catch ( ActivityNotFoundException e ) {
                Toast.makeText(getBaseContext(), "No Application available to view PDF",
                               Toast.LENGTH_SHORT).show();
            } catch ( FileNotFoundException e ) {
                Toast.makeText(getBaseContext(), "File " + OUTPUT + ".pdf not found",
                               Toast.LENGTH_LONG).show();
            } catch ( IOException e ) {
                Toast.makeText(getBaseContext(), "Please enable file management permissions " +
                               "for this app", Toast.LENGTH_LONG).show();
            } catch ( Exception e ) {
                Toast.makeText(getBaseContext(), "An error occurred", Toast.LENGTH_SHORT)
                               .show();
                e.printStackTrace();
            }
        });
    }

    /**
     * Open a pdf in a pdf viewer
     * @see <a href="https://stackoverflow.com/questions/31621419/android-how-do-i-open-a-file-in-
     *      another-app-via-intent">Android - How do I open a file in another app via Intent?</a>
     * @param filename name of the file
     */
    public void open_file(String filename) throws IOException, ActivityNotFoundException {
        File file = new File(Environment.getExternalStorageDirectory().toString() + '/' +
                             filename);

        // Get URI and MIME type of file
        Uri uri = FileProvider.getUriForFile(this, this.getPackageName() +
                                             ".provider", file);
        String mime = getContentResolver().getType(uri);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Executes pdflatex command and generates .pdf file in main storage
     */
    public void texlive() {
        Intent intent = new Intent();
        intent.setClassName("com.termux", "com.termux.app.RunCommandService");
        intent.setAction("com.termux.RUN_COMMAND");
        intent.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/" +
                        "usr/bin/texlive/xelatex");
        intent.putExtra("RUN_COMMAND_SERVICE.EXTRA_WORKDIR", "/storage/emulated/0");
        intent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS",
                        new String[]{
                                     "-interaction", "nonstopmode", "-halt-on-error",
                                     "-file-line-error",
                                     OUTPUT + ".tex"}
                        );
        intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true);
        intent.putExtra("RUN_COMMAND_SERVICE.EXTRA_SESSION_ACTION", "0");
        intent.putExtra("RUN_COMMAND_SERVICE.EXTRA_COMMAND_LABEL", "Texlive command");
        Intent pluginResultsServiceIntent = new Intent(MainActivity.this,
                                                        PluginResultsService.class);
        int executionId = PluginResultsService.getNextExecutionId();
        pluginResultsServiceIntent.putExtra(PluginResultsService.EXTRA_EXECUTION_ID, executionId);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this,
                        executionId, pluginResultsServiceIntent, PendingIntent.FLAG_MUTABLE);
        intent.putExtra("RUN_COMMAND_SERVICE.EXTRA_PENDING_INTENT", pendingIntent);

        try{
            Log.d("Termux Call", "Sending execution command with id " + executionId);
            startService(intent);
        } catch (Exception e) {
            Log.e("Termux Call", "Failed to start execution command with id " +
                    executionId + ": " + e.getMessage());
            Log.e("Termux", "Failed to execute command ");
        }

    }
}
