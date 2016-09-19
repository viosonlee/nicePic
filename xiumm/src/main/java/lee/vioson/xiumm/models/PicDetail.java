package lee.vioson.xiumm.models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lifeng on 16-6-10.
 * TODO:
 */
public class PicDetail implements Parcelable {
    public String src;
    public String alt;

    public PicDetail() {
    }

    protected PicDetail(Parcel in) {
        src = in.readString();
        alt = in.readString();
    }

    public static final Creator<PicDetail> CREATOR = new Creator<PicDetail>() {
        @Override
        public PicDetail createFromParcel(Parcel in) {
            return new PicDetail(in);
        }

        @Override
        public PicDetail[] newArray(int size) {
            return new PicDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(src);
        dest.writeString(alt);
    }

    @Override
    public String toString() {
        return "src:" + src + "\nalt:" + alt;
    }
}
