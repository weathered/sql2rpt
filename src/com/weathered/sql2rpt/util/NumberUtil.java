package com.weathered.sql2rpt.util;

public class NumberUtil {
	private static final String[] NUMBERS_UP_TO_19 = { "zero", "one", "two", "three", "four", "five", "six", "seven",
			"eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
			"eighteen", "nineteen" };

	private static final String[] MULTIPLES_OF_10 = { "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy",
			"eighty", "ninety" };

	private final int number;
	private StringBuffer result = new StringBuffer();

	public static String convert(final int number) {
		return new NumberUtil(number).convert();
	}

	public NumberUtil(final int number) {
		this.number = number;
	}

	public String convert() {
		if (number > 999999999) {
			throw new IllegalArgumentException();
		}

		final int millions = number / 1000000;
		int remainder = number % 1000000;
		final int thousands = remainder / 1000;
		remainder = remainder % 1000;
		final int hundreds = remainder / 100;
		final int tensAndUnits = remainder % 100;

		appendMillions(millions);
		appendThousands(thousands);
		appendHundreds(hundreds);
		appendTensAndUnits(tensAndUnits);
		return result.toString();
	}

	private void appendMillions(final int millions) {
		if (millions > 0) {
			NumberUtil millionsConvertor = new NumberUtil(millions);
			append(millionsConvertor.convert() + " million");
		}
	}

	private void appendThousands(final int thousands) {
		if (thousands > 0) {
			NumberUtil thousandsConvertor = new NumberUtil(thousands);
			append(thousandsConvertor.convert() + " thousand");
		}
	}

	private void appendHundreds(final int hundreds) {
		if (hundreds > 0) {
			append(NUMBERS_UP_TO_19[hundreds] + " hundred");
		}
	}

	private void appendTensAndUnits(final int tensAndUnits) {
		if (tensAndUnits > 0 || result.length() == 0) {
			appendWithAnd(convertTensAndUnits(tensAndUnits));
		}
	}

	private void append(final String words) {
		appendWithSeparator(words, " ");
	}

	private void appendWithAnd(final String words) {
		appendWithSeparator(words, " and ");
	}

	private void appendWithSeparator(final String words, final String separator) {
		if (result.length() > 0) {
			result.append(separator);
		}
		result.append(words);
	}

	private String convertTensAndUnits(final int number) {
		final int tens = number / 10;
		final int units = number - tens * 10;

		if (number < 20) {
			return NUMBERS_UP_TO_19[number];
		} else if (units == 0) {
			return MULTIPLES_OF_10[tens];
		} else {
			return MULTIPLES_OF_10[tens] + "-" + NUMBERS_UP_TO_19[units];
		}
	}
}