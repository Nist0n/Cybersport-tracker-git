package ru.mirea.pavlovve.cybersporttracker.presentation.common;

import java.util.Locale;

/**
 * Отображаемые названия регионов, которые приходят из API в виде кодов
 * (EMEA, KOREA, NORTH AMERICA...).
 */
public final class RegionNames {

    private RegionNames() {
    }

    public static String display(String region) {
        if (region == null || region.trim().isEmpty()) {
            return "—";
        }
        switch (region.trim().toUpperCase(Locale.ROOT)) {
            case "EMEA":
                return "Европа";
            case "KOREA":
                return "Корея";
            case "NORTH AMERICA":
                return "Северная Америка";
            case "CHINA":
                return "Китай";
            case "BRAZIL":
                return "Бразилия";
            case "VIETNAM":
                return "Вьетнам";
            case "JAPAN":
                return "Япония";
            case "OCEANIA":
                return "Океания";
            case "INTERNATIONAL":
                return "Международный";
            case "COMMONWEALTH OF INDEPENDENT STATES":
                return "СНГ";
            case "LATIN AMERICA":
            case "LATIN AMERICA NORTH":
            case "LATIN AMERICA SOUTH":
                return "Латинская Америка";
            case "HONG KONG, MACAU, TAIWAN":
                return "Гонконг, Макао, Тайвань";
            default:
                return region;
        }
    }
}
