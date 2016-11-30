package fourschlag.entities.types;

/**
 * Enum that provides the KPIs for Sales and Fixed Costs
 */
public enum KeyPerformanceIndicators {
    SALES_VOLUME(1, "sales", "Sales Volumes", "mt"),
    NET_SALES(2, "sales", "Net Sales", "k€"),
    CM1(3, "sales", "CM1", "k€"),
    PRICE(4, "sales", "Price", "€/mt"),
    VAR_COSTS(5, "sales", "Variable Cost", "€/mt"),
    CM1_SPECIFIC(6, "sales", "CM1 in €/mt", "€/mt"),
    CM1_PERCENT(7, "sales", "CM1 in % of Net Sales", "%"),
    FIX_PRE_MAN_COST(1, "fixed costs", "Fixed Predet. Manufacturing Cost", "k€"),
    SHIP_COST(2, "fixed costs", "Shipping Cost", "k€"),
    SELL_COST(3, "fixed costs", "Selling Cost", "k€"),
    DIFF_ACT_PRE_MAN_COST(4, "fixed costs", "Difference actual/predet. Manufacturing cost", "k€"),
    IDLE_EQUIP_COST(5, "fixed costs", "Cost of idle equipment", "k€"),
    FIX_COST_BETWEEN_CM1_CM2(6, "fixed costs", "Fixed Cost between CM1+CM2", "k€"),
    RD_COST(7, "fixed costs", "R&D Cost", "k€"),
    ADMIN_COST_BU(8, "fixed costs", "Administration Cost BU", "k€"),
    ADMIN_COST_OD(9, "fixed costs", "Administration Cost OD", "k€"),
    ADMIN_COST_COMPANY(10, "fixed costs", "Administration Cost Company", "k€"),
    OTHER_OP_COST_BU(11, "fixed costs", "Other Operating Cost BU", "k€"),
    OTHER_OP_COST_OD(12, "fixed costs", "Other Operating Cost OD", "k€"),
    OTHER_OP_COST_COMPANY(13, "fixed costs", "Other Operating Cost Company", "k€"),
    SPEC_ITEMS(14, "fixed costs", "Special Items", "k€"),
    PROVISIONS(15, "fixed costs", "Provisions", "k€"),
    CURRENCY_GAINS(16, "fixed costs", "Currency Gains", "k€"),
    VAL_ADJUST_INVENTORIES(17, "fixed costs", "Valuation adjustment on inventories", "k€"),
    EQUITY_INCOME(18, "fixed costs", "Result from Equity-Method", "k€"),
    OTHER_FIX_COST(19, "fixed costs", "Other Fixed Cost (excl. Equity Result)", "k€"),
    FIX_COST_BELOW_CM2(20, "fixed costs", "Fixed Cost below CM2", "k€"),
    TOPDOWN_ADJUST_FIX_COSTS(21, "fixed costs", "Top Down Adjustment Fixed Cost", "k€"),
    TOTAL_FIX_COST(22, "fixed costs", "Total Fixed Cost", "k€"),
    DEPRECIATION(23, "fixed costs", "Depreciation", "k€"),
    CAP_COST(24, "fixed costs", "Cost of Capital", "k€");

    private final int orderNumber;
    private final String fcType;
    private final String fullName;
    private final String unit;

    /**
     * Constructor for KeyPerformanceIndicators
     *
     * @param orderNumber
     * @param fcType
     * @param fullName
     * @param unit
     */
    KeyPerformanceIndicators(int orderNumber, String fcType, String fullName, String unit) {
        this.orderNumber = orderNumber;
        this.fcType = fcType;
        this.fullName = fullName;
        this.unit = unit;
    }

    /**
     * Getter for the OrderNumber
     *
     * @return OrderNumber that is currently used
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Getter for the FcType
     *
     * @return FcType that is currently used
     */
    public String getFcType() {
        return fcType;
    }

    /**
     * Getter for the FullName
     *
     * @return FullName of the KPI currently used
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Getter for the Unit
     *
     * @return Unit that is currently used
     */
    public String getUnit() {
        return unit;
    }
}