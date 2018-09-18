class Buildings {

    static double[] convert(String input) {

        double[] number = new double[2];
        number[0] = Double.parseDouble(input.split(" ")[0].replaceAll("[^\\d.]", ""));
        if (input.contains(" "))
        switch (input.split(" ")[1]) {
            case "million":
            case "Million":
                number[1] = 2;
                break;
            case "billion":
            case "Billion":
                number[1] = 3;
                break;
            case "trillion":
            case "Trillion":
                number[1] = 4;
                break;
            case "Quadrillion":
            case "quadrillion":
                number[1] = 5;
                break;
            case "quintillion":
            case "Quintillion":
                number[1] = 6;
                break;
            case "Sextillion":
            case "sextillion":
                number[1] = 7;
                break;
            case "Septillion":
            case "septillion":
                number[1] = 8;
                break;
            case "Octillion":
            case "octillion":
                number[1] = 9;
                break;
            default:
                number[1] = 0;
                break;
        }
        else number[1] = 0;
        //System.out.println(". The price is: " + number[0] + " and " + number[1] + ".");
        return number;
    }


    static int compare(String firstprice, String secondprice) {
        double[] first = convert(firstprice);
        double[] second = convert(secondprice);
        if (first[1] > second[1])
            return 1;
        if (second[1] > first[1])
            return -1;
        if (first[0] > second[0])
            return 1;
        if (second[0] > first[0])
            return -1;
        return 0;
    }

    static double divide(String bigger, String smaller) {
        double[] first = convert(bigger);
        double[] second = convert(smaller);
        double result;
        result = first[0] / second[0];

        for (int i = 0; i < first[1] - second[1]; i++) {
            result *= 1000;
        }
        return result;
    }

    static String substract(String bigger, String smaller) {
        double[] first = convert(bigger);
        double[] second = convert(smaller);

        if (first[1] == second[1])
            return (first[0] - second[0]) + " " + bigger.split(" ")[1];
        else
            return bigger;
    }

}

