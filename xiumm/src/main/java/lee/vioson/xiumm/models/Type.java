package lee.vioson.xiumm.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lifeng on 16-6-9.
 * TODO:
 */
public class Type implements Parcelable {
    public String href;

    public Type(String href, String title) {
        this.href = href;
        this.title = title;
    }

    public String title;

    public Type() {
    }

    protected Type(Parcel in) {
        href = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(href);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    @Override
    public String toString() {
        return "href:" + href + "\ntitle:" + title;
    }
}
