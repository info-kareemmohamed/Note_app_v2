package com.example.notebook.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.devlomi.record_view.OnRecordListener;
import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.ViewModel.NoteState_Viewmodel;
import com.example.notebook.databinding.ActivityNoteBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private ActivityNoteBinding binding;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private String filePath = "";
    private String firstFilePathFromIntent = "";

    private int color;
    private boolean deletFirstFilePathFromIntent = false;
    private long RecordTime;

    private Note note;

    private boolean EditMode = false;

    private NoteState_Viewmodel viewmodel;

   //  private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "NoteRecorder");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Notebook);

        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setClickListenertoImageColor();

        viewmodel = new ViewModelProvider(this).get(NoteState_Viewmodel.class);
        binding.NoteRecordButton.setRecordView(binding.recordView);
        binding.seekBar.setMax(100);
        Resources resources = getResources();
        color = resources.getColor(color = R.color.card_orange, null);

        binding.seekBar.setOnSeekBarChangeListener(this);
        clickBaclListener();
        RecordListener();
        onClickListenerPlayImage();
        deletRecordListener();
        hasRecording(false);
        onClickListenerFloatingActionButton();


        getDataFormIntent();
    }


    private void getDataFormIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra("note")) {
            note = intent.getParcelableExtra("note");
            binding.NoteTitle.setText(note.getTitle());
            binding.NoteDescription.setText(note.getDescription());
            checkRecord(note.getRecord(), note.getRecordTime());
            EditMode = true;
        } else
            hasRecording(false);


    }

    private void checkRecord(String record, long recordTime) {
        if (record.equals("")) {
            hasRecording(false);

        } else {
            hasRecording(true);
            filePath = record;
            firstFilePathFromIntent = record;
            RecordTime = recordTime;
            prepareMediaPlayer();
            binding.time.setText(milliSecondsToTimer(RecordTime));
        }
    }


    private void clickBaclListener() {
        binding.NoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstFilePathFromIntent.equals(filePath)) {
                    deleteAudioFile(filePath);

                }

                finish();
            }
        });


    }


    private void RecordListener() {


        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NoteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);

                } else {
                    startRecording();
                }
            }

            @Override
            public void onCancel() {
                stopRecording();
                deleteAudioFile(filePath);
            }

            @Override
            public void onFinish(long recordTime, boolean limitReached) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.
                if (recordTime % 60 > 1) {
                    stopRecording();
                    hasRecording(true);
                    prepareMediaPlayer();
                    RecordTime = mediaPlayer.getDuration();
                    binding.time.setText(milliSecondsToTimer(RecordTime));

                }
            }

            @Override
            public void onLessThanSecond() {
                stopRecording();
                deleteAudioFile(filePath);

            }

            @Override
            public void onLock() {
                //When Lock gets activated
                Log.d("RecordView", "onLock");
            }

        });


    }

    private void hasRecording(boolean recorder) {
        if (recorder) {
            binding.NoteLayoutRecordView.setVisibility(View.GONE);
            binding.NoteLayoutVoice.setVisibility(View.VISIBLE);
        } else {
            binding.NoteLayoutRecordView.setVisibility(View.VISIBLE);
            binding.NoteLayoutVoice.setVisibility(View.GONE);

        }
    }

    private boolean deleteAudioFile(String audioFilePath) {
        File audioFile = new File(audioFilePath);

        if (audioFile.exists())
            if (audioFile.delete()) {
                filePath = "";
                RecordTime = 0;
                return true;
            }
        return false;
    }


    private String getFilePath() {
        File internalStorageDir = getFilesDir();
        String folderName = "MyFolder";
        File file = new File(internalStorageDir, folderName);
        if (!file.exists()) file.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d_MMM_yyyy_HH_mm_ss");
        String data = dateFormat.format(new Date());
        return file + "/recording_" + data + ".amr";
    }






  /*
    private String getFilePath() {
        if (!file.exists()) file.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d_MMM_yyyy_HH_mm_ss");
        String data = dateFormat.format(new Date());
        return file + "/recording_" + data + ".amr";
    }

   */






    private void prepareRecorder() {
        filePath = getFilePath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    }

    private void startRecording() {
        prepareRecorder();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    private String milliSecondsToTimer(long milliSeconds) {
        String timer;
        String seconds = "";

        int m = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int s = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (s < 10) {
            seconds = "0" + s;

        } else {
            seconds = s + "";
        }
        timer = m + ":" + seconds;
        return timer;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
            binding.time.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
        }
    };


    private void updateSeekbar() {
        if (mediaPlayer.isPlaying()) {
            binding.seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(runnable, 100);
        }

    }

    private void prepareMediaPlayer() {


        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void onClickListenerPlayImage() {
        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(runnable);
                    mediaPlayer.pause();
                    binding.play.setImageResource(R.drawable.ic_play);


                } else {
                    mediaPlayer.start();
                    binding.play.setImageResource(R.drawable.ic_pause);
                    updateSeekbar();

                }

            }


        });

    }

    private void deletRecordListener() {
        binding.NoteDeletRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstFilePathFromIntent.equals(filePath)) {
                    deleteAudioFile(filePath);
                } else {
                    deletFirstFilePathFromIntent = true;
                }
                filePath = "";
                RecordTime = 0;

                hasRecording(false);
            }
        });


    }

    private boolean checkTitle(String title) {
        if (title.equals("")) {
            Toast.makeText(getBaseContext(), "Please enter the title", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    private void insertNewDataToNote() {
        String title = binding.NoteTitle.getText().toString(), Description;
        if (checkTitle(title)) return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,d MMM yyyy HH:mm a");
        Description = binding.NoteDescription.getText().toString();
        String date = dateFormat.format(new Date());
        if (EditMode) {

            viewmodel.update_NoteState(note.getId(), Description, title, date, filePath, RecordTime, color);
        } else {

            note = new Note(title, Description, date, false, false, color, filePath, RecordTime);
            viewmodel.add_note(note);
        }

        if (deletFirstFilePathFromIntent) deleteAudioFile(firstFilePathFromIntent);

        finish();

    }

    private void onClickListenerFloatingActionButton() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertNewDataToNote();

            }
        });


    }


    private void setClickListenertoImageColor() {
        binding.NoteColorBlue.setOnClickListener(this);
        binding.NoteColorGray.setOnClickListener(this);
        binding.NoteColorGreen.setOnClickListener(this);
        binding.NoteColorOrange.setOnClickListener(this);
        binding.NoteColorRed.setOnClickListener(this);
        binding.NoteColorPurple.setOnClickListener(this);
        binding.NoteColorYellow.setOnClickListener(this);
        binding.NoteColorPurpleLight.setOnClickListener(this);
        binding.NoteColorBlueLight.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        binding.NoteColorBlue.setImageResource(0);
        binding.NoteColorGray.setImageResource(0);
        binding.NoteColorGreen.setImageResource(0);
        binding.NoteColorOrange.setImageResource(0);
        binding.NoteColorRed.setImageResource(0);
        binding.NoteColorPurple.setImageResource(0);
        binding.NoteColorYellow.setImageResource(0);
        binding.NoteColorPurpleLight.setImageResource(0);
        binding.NoteColorBlueLight.setImageResource(0);
        int id = view.getId();
        setColor(id);
        ImageView imageColor = (ImageView) view;
        imageColor.setImageResource(R.drawable.ic_baseline);

    }

    private void setColor(int id) {
        Resources resources = getResources();
        if (id == R.id.Note_color_blue) {
            color = resources.getColor(R.color.card_blue, null);
        } else if (id == R.id.Note_color_gray) {
            color = resources.getColor(R.color.card_gray, null);

        } else if (id == R.id.Note_color_green) {
            color = resources.getColor(color = R.color.card_green, null);
        } else if (id == R.id.Note_color_orange) {
            color = resources.getColor(color = R.color.card_orange, null);

        } else if (id == R.id.Note_color_purple) {
            color = resources.getColor(color = R.color.card_purple, null);

        } else if (id == R.id.Note_color_blueLight) {
            color = resources.getColor(color = R.color.card_blueLight, null);

        } else if (id == R.id.Note_color_PurpleLight) {
            color = resources.getColor(color = R.color.card_PurpleLight, null);

        } else if (id == R.id.Note_color_yellow) {
            color = resources.getColor(color = R.color.card_yellow, null);

        } else if (id == R.id.Note_color_red) {
            color = resources.getColor(color = R.color.card_red, null);

        }
    }


    /////////////////////////////////////MediaPlayer.OnCompletionListener
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        binding.play.setImageResource(R.drawable.ic_play);
        binding.seekBar.setProgress(0);
    }
////////////////////////////////////////////


    ////////////////////////////////////////////SeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            int position = (mediaPlayer.getDuration() / 100) * i;
            binding.time.setText(milliSecondsToTimer(position));
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    ////////////////////////////////////////////////////


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }
}