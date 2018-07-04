package application.android.com.fatee.models.entities;

import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;

public class AboutPageElement {
    private String title;
    private Integer iconDrawable;
    private Integer colorDay;
    private Integer colorNight;
    private String value;
    private Intent intent;
    private Integer gravity;
    private Boolean autoIconColor = true;

    private View.OnClickListener onClickListener;

    public AboutPageElement() {

    }

    public AboutPageElement(String title, Integer iconDrawable) {
        this.title = title;
        this.iconDrawable = iconDrawable;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public AboutPageElement setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public Integer getGravity() {
        return gravity;
    }

    public AboutPageElement setGravity(Integer gravity) {
        this.gravity = gravity;
        return this;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public AboutPageElement setTitle(String title) {
        this.title = title;
        return this;
    }

    @DrawableRes
    @Nullable
    public Integer getIconDrawable() {
        return iconDrawable;
    }

    public AboutPageElement setIconDrawable(@DrawableRes Integer iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }

    @ColorRes
    @Nullable
    public Integer getIconTint() {
        return colorDay;
    }

    public AboutPageElement setIconTint(@ColorRes Integer color) {
        this.colorDay = color;
        return this;
    }

    @ColorRes
    public Integer getIconNightTint() {
        return colorNight;
    }

    public AboutPageElement setIconNightTint(@ColorRes Integer colorNight) {
        this.colorNight = colorNight;
        return this;
    }

    public String getValue() {
        return value;
    }

    public AboutPageElement setValue(String value) {
        this.value = value;
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    public AboutPageElement setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public Boolean getAutoApplyIconTint() {
        return autoIconColor;
    }

    public AboutPageElement setAutoApplyIconTint(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
        return this;
    }
}
