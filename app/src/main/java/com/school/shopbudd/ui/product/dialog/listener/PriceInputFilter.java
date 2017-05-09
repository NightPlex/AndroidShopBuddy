package com.school.shopbudd.ui.product.dialog.listener;

import android.text.InputFilter;
import android.text.Spanned;
import com.school.shopbudd.R;
import com.school.shopbudd.ui.product.dialog.ProductDialogCache;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class PriceInputFilter implements InputFilter {
    private static final String PERIOD = ".";
    private static final String COMMA = ",";
    private String decimalSeparator;
    private static final String INVALID_CHAR_REGEX = "[^0-9\\.\\,]+]";

    public PriceInputFilter(ProductDialogCache dialogCache) {
        NumberFormat nf = NumberFormat.getInstance();
        if (nf instanceof DecimalFormat) {
            DecimalFormatSymbols symbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
            decimalSeparator = String.valueOf(symbols.getDecimalSeparator());
        } else {
            decimalSeparator = dialogCache.getPrice().getResources().getString(R.string.number_decimal_separator);
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String cleanedString = source.toString();
        cleanedString = cleanedString.replaceAll(INVALID_CHAR_REGEX, "");
        cleanedString = cleanedString.replace(PERIOD, decimalSeparator);
        cleanedString = cleanedString.replace(COMMA, decimalSeparator);
        return cleanedString;
    }
}