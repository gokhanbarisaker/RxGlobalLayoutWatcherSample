package com.gokhanbarisaker.globallayoutwatchersample.utility;

import android.graphics.Rect;

import com.gokhanbarisaker.globallayoutwatchersample.model.Template;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gokhanbarisaker on 4/7/15.
 */
public class TemplateFormatter
{
    private static TemplateFormatter sharedInstance;

    public static TemplateFormatter getInstance()
    {
        synchronized (TemplateFormatter.class)
        {
            if (sharedInstance == null)
            {
                sharedInstance = new TemplateFormatter();
            }
        }

        return sharedInstance;
    }

    public String format(final String pattern, Rect frame)
    {
        return StringUtils.replaceEach(pattern,
                new String[]{Template.WIDTH, Template.HEIGHT},
                new String[]{String.valueOf(frame.width()), String.valueOf(frame.height())});
    }
}
