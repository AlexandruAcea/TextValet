package screen.from.text.read.com.readtextfromscreen;

import java.text.DecimalFormat;

public class Converter {
    /**
     * Just a way for us to store related constants
     */
    public enum Unit {
        in,
        cm,
        ft,
        yd,
        m,
        mi,
        km;

        // Helper method to convert text to one of the above constants
        public static Unit fromString(String text) {
            if (text != null) {
                for (Unit unit : Unit.values()) {
                    switch (text) {
                        case "inches" : text = "in";
                            break;
                        case "inch" : text = "in";
                            break;
                        case "centimeters" : text = "cm";
                            break;
                        case "centimeter" : text = "cm";
                            break;
                        case "feet" : text = "ft";
                            break;
                        case "foot" : text = "ft";
                            break;
                        case "yards" : text = "yd";
                            break;
                        case "yard" : text = "yd";
                            break;
                        case "meters" : text = "m";
                            break;
                        case "meter" : text = "m";
                            break;
                        case "miles" : text = "mi";
                            break;
                        case "mile" : text = "mi";
                            break;
                        case "kilometers" : text = "km";
                            break;
                        case "kilometer" : text = "km";
                            break;
                    }

                    if (text.equalsIgnoreCase(unit.toString())) {
                        return unit;
                    }
                }
            }

            throw new IllegalArgumentException("Cannot find a value for " + text);
        }
    }

    // What can I multiply by to get me from my fromUnit to my toUnit?
    private final double multiplier;
    private final String unit;

    public Converter(Unit from, Unit to) {
        double constant = 1;
        // Set the multiplier, else if fromUnit = toUnit, then it is 1
        switch (from) {
            case in:
                if (to == Unit.cm) {
                    constant = 2.54;
                } else if (to == Unit.ft) {
                    constant = 0.0833333;
                } else if (to == Unit.yd) {
                    constant = 0.0277778;
                } else if (to == Unit.m) {
                    constant = 0.0254;
                } else if (to == Unit.mi) {
                    constant = 1.5783e-5;
                } else if (to == Unit.km) {
                    constant = 2.54e-5;
                }
                break;
            case cm:
                if (to == Unit.in) {
                    constant = 0.393701;
                } else if (to == Unit.ft) {
                    constant = 0.0328084;
                } else if (to == Unit.yd) {
                    constant = 0.0109361;
                } else if (to == Unit.m) {
                    constant = 0.01;
                } else if (to == Unit.mi) {
                    constant = 6.2137e-6;
                } else if (to == Unit.km) {
                    constant = 1e-5;
                }
                break;
            case ft:
                if (to == Unit.in) {
                    constant = 12;
                } else if (to == Unit.cm) {
                    constant = 30.48;
                } else if (to == Unit.yd) {
                    constant = 0.333333;
                } else if (to == Unit.m) {
                    constant = 0.3048;
                } else if (to == Unit.mi) {
                    constant = 0.000189394;
                } else if (to == Unit.km) {
                    constant = 0.0003048;
                }
                break;
            case yd:
                if (to == Unit.in) {
                    constant = 36;
                } else if (to == Unit.cm) {
                    constant = 91.44;
                } else if (to == Unit.ft) {
                    constant = 3;
                } else if (to == Unit.m) {
                    constant = 0.9144;
                } else if (to == Unit.mi) {
                    constant = 0.000568182;
                } else if (to == Unit.km) {
                    constant = 0.0009144;
                }
                break;
            case m:
                if (to == Unit.in) {
                    constant = 39.3701;
                } else if (to == Unit.cm) {
                    constant = 100;
                } else if (to == Unit.ft) {
                    constant = 3.28084;
                } else if (to == Unit.yd) {
                    constant = 1.09361;
                } else if (to == Unit.mi) {
                    constant = 0.000621371;
                } else if (to == Unit.km) {
                    constant = 0.001;
                }
                break;
            case mi:
                if (to == Unit.in) {
                    constant = 63360;
                } else if (to == Unit.cm) {
                    constant = 160934;
                } else if (to == Unit.ft) {
                    constant = 5280;
                } else if (to == Unit.yd) {
                    constant = 1760;
                } else if (to == Unit.m) {
                    constant = 1609.34;
                } else if (to == Unit.km) {
                    constant = 1.60934;
                }
                break;
            case km:
                if (to == Unit.in) {
                    constant = 39370.1;
                } else if (to == Unit.cm) {
                    constant = 100000;
                } else if (to == Unit.ft) {
                    constant = 3280.84;
                } else if (to == Unit.yd) {
                    constant = 1093.61;
                } else if (to == Unit.m) {
                    constant = 1000;
                } else if (to == Unit.mi) {
                    constant = 0.621371;
                }
                break;
        }

        multiplier = constant;
        unit = to.toString();
    }

    // Convert the unit!
    public String convert(double input) {
        String result = String.valueOf(new DecimalFormat("##.##").format(input * multiplier));
        return result + " " + unit;
    }

}

