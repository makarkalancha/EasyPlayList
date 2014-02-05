package com.easyplaylist.View;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.jar.Attributes;

/**
 * Created by Makar on 2/5/14.
 */
public class InfiniteMarqueeTextView extends TextView {
    public InfiniteMarqueeTextView(Context context){
        super(context);
    }

    public InfiniteMarqueeTextView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public InfiniteMarqueeTextView(Context context, AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused){
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if(hasWindowFocus){
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }

    @Override
    public boolean isFocused(){
        return true;
    }
}
