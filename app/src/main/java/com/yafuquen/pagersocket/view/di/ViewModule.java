package com.yafuquen.pagersocket.view.di;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    @Provides
    Picasso providePicasso() {
        return Picasso.get();
    }
}
