package com.example.notebook.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note  implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private  long Id;

    private String Title;
    private String Description;
    private String Date;
    private boolean Archived;

    private long RecordTime;
    private boolean Favourites;
    private int Color;
    private String record;


    public Note(long id, String title, String description, String date, boolean archived, boolean Favourites,int color, String record,long RecordTime) {
        Id = id;
        Title = title;
        Description = description;
        Date = date;
        Archived = archived;
        Color = color;
        this.record = record;
        this.Favourites=Favourites;
        this.RecordTime=RecordTime;
    }

    public Note(String title, String description, String date, boolean archived, boolean Favourites,int color, String record,long RecordTime) {
        Title = title;
        Description = description;
        Date = date;
        Archived = archived;
        Color = color;
        this.record = record;
        this.Favourites=Favourites;
        this.RecordTime=RecordTime;
    }


    public Note() {
    }

    protected Note(Parcel in) {
        Id = in.readLong();
        Title = in.readString();
        Description = in.readString();
        Date = in.readString();
        Archived = in.readByte() != 0;
        RecordTime = in.readLong();
        Favourites = in.readByte() != 0;
        Color = in.readInt();
        record = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public boolean isArchived() {
        return Archived;
    }

    public void setArchived(boolean archived) {
        Archived = archived;
    }

    public long getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(long recordTime) {
        RecordTime = recordTime;
    }

    public boolean isFavourites() {
        return Favourites;
    }

    public void setFavourites(boolean favourites) {
        Favourites = favourites;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(Id);
        parcel.writeString(Title);
        parcel.writeString(Description);
        parcel.writeString(Date);
        parcel.writeByte((byte) (Archived ? 1 : 0));
        parcel.writeLong(RecordTime);
        parcel.writeByte((byte) (Favourites ? 1 : 0));
        parcel.writeInt(Color);
        parcel.writeString(record);
    }
}
