/**
 * This class allows to convert a numeric value into words in English
 * For example: "1245" will be converted to "One thousand two hundred forty five"
 * This class is useful a scriptlet in Jasper reports
 *
 * Code based on http://www.rgagnon.com/javadetails/java-0426.html by R. Gagnon (CC)
 *
 * @author Mambu GmbH www.mambu.com
 *
 */

package com.mambu.resources;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import java.text.DecimalFormat;

public class ConvertNumbersToWords extends JRDefaultScriptlet {

    /* Numbes in English up to tens */
    private static final String[] tensNames = {
        "",
        " ten",
        " twenty",
        " thirty",
        " forty",
        " fifty",
        " sixty",
        " seventy",
        " eighty",
        " ninety"
    };

    /* Numbers in English up to 19 */
    private static final String[] numNames = {
        "",
        " one",
        " two",
        " three",
        " four",
        " five",
        " six",
        " seven",
        " eight",
        " nine",
        " ten",
        " eleven",
        " twelve",
        " thirteen",
        " fourteen",
        " fifteen",
        " sixteen",
        " seventeen",
        " eighteen",
        " nineteen"
    };

    /* Convert a number which is less than one thousand
     *
     * @param int number    Numeric value to convert into words
     *
     * @return String       The English textual representation of the number
     */
    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20){
            soFar = numNames[number % 100];
            number /= 100;
        }
        else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }

        if (number == 0) {
            return soFar;
        }

        return numNames[number] + " hundred" + soFar;
    }

    /*
     * Convert a number into its English representation
     *
     * @param long number   The number to convert
     *
     * @return String       English representation of the number
     */
    public static String convertToEnglish(long number) {
        // 0 to 999 999 999 999
        if (number == 0) {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0,3));

        // nnnXXXnnnnnn
        int millions  = Integer.parseInt(snumber.substring(3,6));

        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6,9));

        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9,12));

        String tradBillions;

        switch (billions) {
        case 0:
            tradBillions = "";
            break;
        case 1 :
            tradBillions = convertLessThanOneThousand(billions) + " billion ";
            break;
        default :
            tradBillions = convertLessThanOneThousand(billions) + " billion ";
        }

        String result =  tradBillions;

        String tradMillions;

        switch (millions) {
        case 0:
            tradMillions = "";
            break;
        case 1 :
            tradMillions = convertLessThanOneThousand(millions) + " million ";
            break;
        default :
            tradMillions = convertLessThanOneThousand(millions) + " million ";
        }

        result =  result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
        case 0:
            tradHundredThousands = "";
            break;
        case 1 :
            tradHundredThousands = "one thousand ";
            break;
        default :
            tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
        }

        result =  result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result =  result + tradThousand;

        // remove spaces
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }


}
