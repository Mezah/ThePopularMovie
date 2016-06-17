package com.hazem.androidnanodegree.thepopularmovie.Utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hazem.androidnanodegree.thepopularmovie.objects.PopMovie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mezah on 6/9/2016.
 */
public class MovieUtil {


    // from ===> http://www.codexpedia.com/android/android-download-and-save-image-through-picasso/
    public static Target picassoImageTarget(final Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir

        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        // if file exists do nothing
                        if (myImageFile.exists()) {
                            return ;
                        }
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }

    // from ===> http://www.codexpedia.com/android/android-download-and-save-image-through-picasso/
    public static File loadImageFromDevice(Context context,String imageName){

        ContextWrapper cw = new ContextWrapper(context);

        File directory = cw.getDir(PopMovie.DIRECTORY, Context.MODE_PRIVATE);

        Log.d("FILE_NAME",new File(directory, imageName).toString());

        return new File(directory, imageName);
    }
}
