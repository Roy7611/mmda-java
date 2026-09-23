package cloud.mmda.core.enums;

/**
 * 元界面视图，
 */
public enum MetaUiView {
    LIST(1, "list","列表"),
    DETAILS(2, "details","详情"),
    EDIT(4, "edit","编辑"),
    CREATE(5, "create","创建"),

    SELECT(16, "select","选择"),
    SELECT_ONE(17, "selectOne","单选"),
    SELECT_MANY(18, "selectMany","多选"),

    SEARCH(32, "search","搜索");

    private int value;
    private String code;
    private String text;

    public int getValue() { return value; }
    public String getCode() { return code; }
    public String getText() { return text; }

    MetaUiView(int value, String code, String text){
        this.value = value;
        this.code = code;
        this.text = text;
    }

    public boolean hasFlag(MetaUiView view){
        return (view.getValue() & this.value) == view.getValue();
    }

    /**
     * 客户端都是小写的习惯
     * @return
     */
    @Override
    public String toString() {
        return this.getCode();
    }
}
