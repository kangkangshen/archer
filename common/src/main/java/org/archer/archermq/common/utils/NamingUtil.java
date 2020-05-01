package org.archer.archermq.common.utils;

import org.archer.archermq.common.constants.Delimiters;

public class NamingUtil {


    public static String UNDER_SCORE_CASE = "under_score_case";

    public static String KEBAB_CASE = "kebab-case";

    public static String CAMEL_CASE = "camelCase";


    public static String toCamelCaseNaming(String rawNaming) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (judgeNaming(rawNaming)) {
            case "under_score_case":
                String[] underScoreCase = rawNaming.split("_");
                stringBuilder.append(underScoreCase[0].toLowerCase());
                for (int i = 1;i<underScoreCase.length;i++) {
                    String firstPart = underScoreCase[i].substring(0, 1).toUpperCase();
                    String secondPart = underScoreCase[i].substring(1).toLowerCase();
                    stringBuilder.append(firstPart).append(secondPart);
                }
                return stringBuilder.toString();
            case "kebab-case":
                String[] kebabCase = rawNaming.split("-");
                stringBuilder.append(kebabCase[0].toLowerCase());
                for (int i = 1;i<kebabCase.length;i++) {
                    String firstPart = kebabCase[i].substring(0, 1).toUpperCase();
                    String secondPart = kebabCase[i].substring(1).toLowerCase();
                    stringBuilder.append(firstPart).append(secondPart);
                }
                return stringBuilder.toString();
            default:
                return rawNaming;

        }
    }


    private static String judgeNaming(String rawNaming) {
        if (rawNaming.indexOf(Delimiters.UNDERLINE) != -1) {
            return UNDER_SCORE_CASE;
        } else if (rawNaming.indexOf(Delimiters.DASH) != -1) {
            return KEBAB_CASE;
        } else if (rawNaming.matches("/_+[a-zA-Z]/g")) {
            return CAMEL_CASE;
        }
        throw new UnsupportedOperationException(rawNaming);
    }
}
