package com.sangxiang.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {

    /**
     * Set date picker divider color
     *
     * Reflection based method
     * @param datePicker
     * @param color
     * @param height
     */
    @Keep
    public static void setDatePickerDividerColor(final @NonNull DatePicker datePicker,
                                                 final @ColorRes int color,
                                                 final @DimenRes int height) {
        // Divider changing:
        Context context = datePicker.getContext();
        // Get mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // Get NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            View view = mSpinners.getChildAt(i);
            if (view != null && view instanceof NumberPicker) {
                NumberPicker picker = (NumberPicker) view;
                setNumberPickerDividerColor(picker, color);
                setNumberPickerDividerHeight(picker, height);
            }
        }
    }


    /**
     *
     * @param picker
     * @param height
     */
    @Keep
    public static void setNumberPickerDividerHeight(final @NonNull NumberPicker picker,
                                                    final @DimenRes int height) {

        Context context = picker.getContext();

        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, context.getResources().getDimensionPixelSize(height));
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     *
     * @param picker
     * @param color
     */
    @Keep
    public static void setNumberPickerDividerColor(final @NonNull NumberPicker picker,
                                                   final @ColorRes int color) {
        Context context = picker.getContext();

        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, new ColorDrawable(ContextCompat.getColor(context, color)));
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static long getTimestampFromDatePicker(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        return calendar.getTimeInMillis();
    }



    public static int getYearsBetween(long a, long b) {
        if (b < a) {
            return getYearsBetween(b, a);
        }
        a = resetToYear(a);
        b = resetToYear(b);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(a);
        int years = 0;
        while (cal.getTimeInMillis() < b) {
            // add another year
            cal.add(Calendar.YEAR, 1);
            years++;
        }
        return years;
    }

    public static int getMonthsBetween(long a, long b) {
        if (b < a) {
            return getMonthsBetween(b, a);
        }
        a = resetToMonth(a);
        b = resetToMonth(b);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(a);
        int months = 0;
        while (cal.getTimeInMillis() < b) {
            // add another year
            cal.add(Calendar.MONTH, 1);
            months++;
        }
        return months;
    }

    public static int getWeeksBetween(long a, long b) {
        if (b < a) {
            return getWeeksBetween(b, a);
        }
        a = resetToWeek(a);
        b = resetToWeek(b);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(a);
        int weeks = 0;
        while (cal.getTimeInMillis() < b) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    public static int getTrueDaysBetween(long a, long b) {
        if (b < a) {
            return -getDaysBetween(b, a);
        }
        a = resetToDay(a);
        b = resetToDay(b);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(a);
        int days = 0;
        while (cal.getTimeInMillis() < b) {
            // add another day
            cal.add(Calendar.DAY_OF_MONTH, 1);
            days++;
        }
        return days;
    }

    public static int getDaysBetween(long a, long b) {
        if (b < a) {
            return getDaysBetween(b, a);
        }
        a = resetToDay(a);
        b = resetToDay(b);

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(a);
        int days = 0;
        while (cal.getTimeInMillis() < b) {
            // add another day
            cal.add(Calendar.DAY_OF_MONTH, 1);
            days++;
        }
        return days;
    }

    public static int getHoursBetween(long a, long b) {
        if (b < a) {
            return getHoursBetween(b, a);
        }
        a = resetToHour(a);
        b = resetToHour(b);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(a);
        int hours = 0;
        while (cal.getTimeInMillis() < b) {
            // add another hour
            cal.add(Calendar.HOUR, 1);
            hours++;
        }
        return hours;
    }

    public static long resetToYear(long d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long resetToMonth(long d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long resetToWeek(long d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long resetToDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long resetToHour(long d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


}
