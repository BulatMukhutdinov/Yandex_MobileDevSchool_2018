package ru.mukhutdinov.bulat.yandextestapp.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Photo implements Serializable {

    @Getter
    @SerializedName("id")
    private final long id;

    @Getter
    @SerializedName("webformatURL")
    private final String webFormatURL;

    @Getter
    @SerializedName("tags")
    private final String tags;
}