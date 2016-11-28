package fourschlag.entities.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedList;

@JsonIgnoreProperties(value = { "orderNumber" })
public class OutputDataType {
    private int orderNumber;
    private String kpi;
    private double m01;
    private double m02;
    private double m03;
    private double m04;
    private double m05;
    private double m06;
    private double m07;
    private double m08;
    private double m09;
    private double m10;
    private double m11;
    private double m12;
    private double m13;
    private double m14;
    private double m15;
    private double m16;
    private double m17;
    private double m18;
    private double bj2;
    private double bj3;
    private double bj4;
    private String region;
    private String subregion;
    private String sbu;
    private String entryType;
    private String productMainGroup;
    private String salesType;
    private String currency;
    private String unit;
    private String fcType;

    public OutputDataType(KeyPerformanceIndicators kpi, String sbu, String productMainGroup, String region,
                          String subregion, String salesType, String entryType, Currency currency,
                          LinkedList<Double> months, LinkedList<Double> bjValues) {
        this.kpi = kpi.getFullName();
        this.orderNumber = kpi.getOrderNumber();
        this.fcType = kpi.getFcType();
        this.sbu = sbu;
        this.productMainGroup = productMainGroup;
        this.region = region;
        this.subregion = subregion;
        this.salesType = salesType;
        this.entryType = entryType;
        this.currency = currency.getAbbreviation();
        this.unit = currency.getSymbol();
        this.setMonths(months);
        this.setBj(bjValues);
    }

    private void setMonths(LinkedList<Double> months) {
        this.m01 = months.poll();
        this.m02 = months.poll();
        this.m03 = months.poll();
        this.m04 = months.poll();
        this.m05 = months.poll();
        this.m06 = months.poll();
        this.m07 = months.poll();
        this.m08 = months.poll();
        this.m09 = months.poll();
        this.m10 = months.poll();
        this.m11 = months.poll();
        this.m12 = months.poll();
        this.m13 = months.poll();
        this.m14 = months.poll();
        this.m15 = months.poll();
        this.m16 = months.poll();
        this.m17 = months.poll();
        this.m18 = months.poll();
    }

    private void setBj(LinkedList<Double> bjValues) {
        this.bj2 = bjValues.poll();
        this.bj3 = bjValues.poll();
        this.bj4 = bjValues.poll();
    }

    /**
     * Getter for the number of months the OutputDataType is built for
     *
     * @return number of months
     */
    /* TODO: Remove 'magic numbers' and create class for constants */
    public static int getNumberOfMonths() { return 18; }

    public static int getNumberOfBj() { return 3; }

    public int getOrderNumber() { return orderNumber; }

    public String getKpi() {
        return kpi;
    }

    public double getM01() {
        return m01;
    }

    public double getM02() {
        return m02;
    }

    public double getM03() {
        return m03;
    }

    public double getM04() {
        return m04;
    }

    public double getM05() {
        return m05;
    }

    public double getM06() {
        return m06;
    }

    public double getM07() {
        return m07;
    }

    public double getM08() {
        return m08;
    }

    public double getM09() {
        return m09;
    }

    public double getM10() {
        return m10;
    }

    public double getM11() {
        return m11;
    }

    public double getM12() {
        return m12;
    }

    public double getM13() {
        return m13;
    }

    public double getM14() {
        return m14;
    }

    public double getM15() {
        return m15;
    }

    public double getM16() {
        return m16;
    }

    public double getM17() {
        return m17;
    }

    public double getM18() {
        return m18;
    }

    public double getBj2() {
        return bj2;
    }

    public double getBj3() {
        return bj3;
    }

    public double getBj4() {
        return bj4;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getSbu() {
        return sbu;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getProductMainGroup() {
        return productMainGroup;
    }

    public String getSalesType() {
        return salesType;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUnit() {
        return unit;
    }

    public String getFcType() {
        return fcType;
    }


    @Override
    public String toString() {
        return "OutputDataType{" +
                "orderNumber=" + orderNumber +
                ", kpi='" + kpi + '\'' +
                ", m01=" + m01 +
                ", m02=" + m02 +
                ", m03=" + m03 +
                ", m04=" + m04 +
                ", m05=" + m05 +
                ", m06=" + m06 +
                ", m07=" + m07 +
                ", m08=" + m08 +
                ", m09=" + m09 +
                ", m10=" + m10 +
                ", m11=" + m11 +
                ", m12=" + m12 +
                ", m13=" + m13 +
                ", m14=" + m14 +
                ", m15=" + m15 +
                ", m16=" + m16 +
                ", m17=" + m17 +
                ", m18=" + m18 +
                ", bj2=" + bj2 +
                ", bj3=" + bj3 +
                ", bj4=" + bj4 +
                ", region='" + region + '\'' +
                ", subregion='" + subregion + '\'' +
                ", sbu='" + sbu + '\'' +
                ", entryType=" + entryType +
                ", productMainGroup='" + productMainGroup + '\'' +
                ", salesType=" + salesType +
                ", currency='" + currency + '\'' +
                ", unit='" + unit + '\'' +
                ", fcType='" + fcType + '\'' +
                '}';
    }
}