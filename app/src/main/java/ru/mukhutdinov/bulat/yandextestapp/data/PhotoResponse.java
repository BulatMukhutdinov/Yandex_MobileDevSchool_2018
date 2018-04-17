package ru.mukhutdinov.bulat.yandextestapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PhotoResponse {
    @Getter
    @SerializedName("hits")
    private final List<Photo> photos;
}