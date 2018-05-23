package de.wolfi.utils.i18n;

public class I18n
{
    private static Locale i18nLocale;

    public static void setLocale(Locale i18nLocaleIn)
    {
        i18nLocale = i18nLocaleIn;
    }

    /**
     * format(a, b) is equivalent to String.format(translate(a), b). Args: translationKey, params...
     */
    public static String format(String translateKey, Object... parameters)
    {
    	if(parameters.length == 0) parameters = new String[0];
        return i18nLocale.formatMessage(translateKey, parameters);
    }
}
