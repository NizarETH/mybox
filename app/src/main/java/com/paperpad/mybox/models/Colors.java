package com.paperpad.mybox.models;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@DatabaseTable(tableName = "Colors")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
        "background_color",
        "alternate_background_color",
        "title_color",
        "text_color",
        "navigation_color",
        "dark_navigation_color"
})

public class Colors {

    private static final Orientation DEFAULT_ORIENTATION = GradientDrawable.Orientation.TOP_BOTTOM;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    @JsonProperty("background_color")
    private String background_color;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    @JsonProperty("alternate_background_color")
    private String alternate_background_color;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    @JsonProperty("title_color")
    private String title_color;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    @JsonProperty("text_color")
    private String text_color;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    @JsonProperty("navigation_color")
    private String navigation_color;

    public String getDark_navigation_color() {
        if(dark_navigation_color.isEmpty())
            dark_navigation_color = navigation_color;
        return dark_navigation_color;
    }

    public void setDark_navigation_color(String dark_navigation_color) {
        this.dark_navigation_color = dark_navigation_color;
    }

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    @JsonProperty("dark_navigation_color")
    private String dark_navigation_color;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Colors(){
                background_color = "FFFFFF";
                alternate_background_color = "F3F1ED";
                title_color = "444444";
                text_color = "777777";
                navigation_color = "605D5B";
                dark_navigation_color = "605D5B";

    }


    @JsonProperty("background_color")
    public String getBackground_color() {
        return background_color;
    }

    @JsonProperty("background_color")
    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    @JsonProperty("alternate_background_color")
    public String getAlternate_background_color() {
        return alternate_background_color;
    }

    @JsonProperty("alternate_background_color")
    public void setAlternate_background_color(String alternate_background_color) {
        this.alternate_background_color = alternate_background_color;
    }

    @JsonProperty("title_color")
    public String getTitle_color() {
        return title_color;
    }

    @JsonProperty("title_color")
    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    @JsonProperty("text_color")
    public String getText_color() {
        return text_color;
    }

    @JsonProperty("text_color")
    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    @JsonProperty("navigation_color")
    public String getNavigation_color() {
        return navigation_color;
    }

    @JsonProperty("navigation_color")
    public void setNavigation_color(String navigation_color) {
        this.navigation_color = navigation_color;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public GradientDrawable makeGradientToColor(String color) {
        GradientDrawable drawable = new GradientDrawable(DEFAULT_ORIENTATION, new int[] {0x44aabbcc,0x44222222});
        drawable.setCornerRadius(0f);
        drawable.setColorFilter(getColor(color), android.graphics.PorterDuff.Mode.OVERLAY);
        return drawable;
    }

    public int getColor(String color) {
        color = validateColor(color);
        return Color.parseColor("#"+color);
    }



    public String validateColor(String color) {
        if (color == null || (color.isEmpty() || color.equals("0")) || color.length() % 2 != 0 ) {

            color = "000000";
        }else if (color.length() == 6) {
        }else if (color.length() == 4) {
            color = "00"+color;

        }else if (color.length() == 2) {
            color = "0000"+color;
        }
        return color;
    }


    public int getColor(String color, String alpha) {
        return Color.parseColor("#"+alpha+validateColor(color));
    }


}
