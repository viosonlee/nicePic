package lee.vioson.xiumm.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lifeng on 16-6-9.
 * TODO: 列表数据
 */
public class ListData implements Parcelable {

    public String href;
    public String src;
    public String alt;

    public ListData() {
    }

    protected ListData(Parcel in) {
        href = in.readString();
        src = in.readString();
        alt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(href);
        dest.writeString(src);
        dest.writeString(alt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListData> CREATOR = new Creator<ListData>() {
        @Override
        public ListData createFromParcel(Parcel in) {
            return new ListData(in);
        }

        @Override
        public ListData[] newArray(int size) {
            return new ListData[size];
        }
    };

    @Override
    public String toString() {
        return "href:" + href + "\nsrc:"
                + src + "\nalt:" + alt;
    }
}
