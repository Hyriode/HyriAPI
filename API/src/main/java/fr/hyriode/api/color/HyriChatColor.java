package fr.hyriode.api.color;

import java.util.regex.Pattern;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 10:04
 */
public enum HyriChatColor {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    MAGIC('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r');

    public static final char COLOR_CHAR = '§';
    public static final HyriChatColor[] VALUES = HyriChatColor.values();

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-OR]");

    private final char code;
    private final boolean isFormat;
    private final String toString;

    HyriChatColor(char code) {
        this(code, false);
    }

    HyriChatColor(char code, boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{'§', code});
    }

    public char getChar() {
        return this.code;
    }

    public boolean isFormat() {
        return this.isFormat;
    }

    public String toString() {
        return this.toString;
    }

    public boolean isColor() {
        return !this.isFormat && this != RESET;
    }

    public static HyriChatColor getByChar(char code) {
        for (HyriChatColor color : VALUES) {
            if (color.getChar() == code) {
                return color;
            }
        }
        return null;
    }

    public static HyriChatColor getByChar(String code) {
        return getByChar(code.charAt(0));
    }

    public static String stripColor(String input) {
        return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for(int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    public static String getLastColors(String input) {
        final StringBuilder result = new StringBuilder();
        final int length = input.length();

        for (int index = length - 1; index > -1; --index) {
            final char section = input.charAt(index);

            if (section == 167 && index < length - 1) {
                final char character = input.charAt(index + 1);
                final HyriChatColor color = getByChar(character);

                if (color != null) {
                    result.insert(0, color);

                    if (color.isColor() || color.equals(RESET)) {
                        break;
                    }
                }
            }
        }

        return result.toString();
    }

}
