package fourschlag.entities.types;

public enum KeyPerformanceIndicators {
    SALES_VOLUME(1, "sales", "Sales Volumes"),
    NET_SALES(2, "sales", "Net Sales"),
    CM1(3, "sales", "CM1"),
    PRICE(4, "sales", "Price"),
    VAR_COSTS(5, "sales", "Variable Cost"),
    CM1_SPECIFIC(6, "sales", "CM1 in €/mt"),
    CM1_PERCENT(7, "sales", "CM1 in % of Net Sales"),
    FIX_PRE_MAN_COST(1, "fixed costs", "Fixed Predet. Manufacturing Cost"),
    SHIP_COST(2, "fixed costs", "Shipping Cost"),
    SELL_COST(3, "fixed costs", "Selling Cost"),
    DIFF_ACT_PRE_MAN_COST(4, "fixed costs", "Difference actual/predet. Manufacturing cost"),
    IDLE_EQUIP_COST(5, "fixed costs", "Cost of idle equipment"),
    FIX_COST_BETWEEN_CM1_CM2(6, "fixed costs", "Fixed Cost between CM1+CM2"),
    RD_COST(7, "fixed costs", "R&D Cost"),
    ADMIN_COST_BU(8, "fixed costs", "Administration Cost BU"),
    ADMIN_COST_OD(9, "fixed costs", "Administration Cost OD"),
    ADMIN_COST_COMPANY(10, "fixed costs", "Administration Cost Company"),
    OTHER_OP_COST_BU(11, "fixed costs", "Other Operating Cost BU"),
    OTHER_OP_COST_OD(12, "fixed costs", "Other Operating Cost OD"),
    OTHER_OP_COST_COMPANY(13, "fixed costs", "Other Operating Cost Company"),
    SPEC_ITEMS(14, "fixed costs", "Special Items"),
    PROVISIONS(15, "fixed costs", "Provisions"),
    CURRENCY_GAINS(16, "fixed costs", "Currency Gains"),
    VAL_ADJUST_INVENTORIES(17, "fixed costs", "Valuation adjustment on inventories"),
    EQUITY_INCOME(18, "fixed costs", "Result from Equity-Method"),
    OTHER_FIX_COST(19, "fixed costs", "Other Fixed Cost (excl. Equity Result)"),
    FIX_COST_BELOW_CM2(20, "fixed costs", "Fixed Cost below CM2"),
    TOPDOWN_ADJUST_FIX_COSTS(21, "fixed costs", "Top Down Adjustment Fixed Cost"),
    TOTAL_FIX_COST(22, "fixed costs", "Total Fixed Cost"),
    DEPRECATION(23, "fixed costs", "Deprecation"),
    CAP_COST(24, "fixed costs", "Cost of Capital");

    private final int orderNumber;
    private final String fcType;
    private final String fullName;

    KeyPerformanceIndicators(int orderNumber, String fcType, String fullName) {
        this.orderNumber = orderNumber;
        this.fcType = fcType;
        this.fullName = fullName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getFcType() {
        return fcType;
    }

    public String getFullName() {
        return fullName;
    }
}