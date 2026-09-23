package cloud.mmda.core.sql.schema;

import cloud.mmda.core.utils.NamingUtil;

import java.util.HashMap;


public class Column {
	private String name;
	private int idx;
	private boolean key;
	private String dataType;
	private boolean nullable;
	private Long maxLength;
	private String comment;
	private boolean unsigned;
	private Byte numericPrecision;
	private Byte numericScale;
	private boolean computed;
	private String formula;
	private String defaultVal;
	private boolean autoIncr;
	
	
	private static final String NL = "\n";
	private static final HashMap<String, String> dataMap = new HashMap<String, String>() {
		private static final long serialVersionUID = -616241512107189365L;

		{
			put("char", "String");
			put("varchar", "String");
			put("longtext", "String");

			put("bit", "boolean");

			put("tinyint", "byte");
			put("smallint", "short");
			put("int", "int");
			put("bigint", "long");
			put("decimal", "BigDecimal");
			put("float", "float");
			put("double", "float");

			put("year", "short");
			put("date", "Date");
			put("datetime", "Timestamp");
			put("time", "Time");
			put("timestamp", "Timestamp");

			put("binary", "Object");
		}
	};
	
	private static final HashMap<String, Integer> dataTypeMap = new HashMap<String, Integer>() {
		private static final long serialVersionUID = -616241512107189365L;

		{
			put("char", 0);
			put("varchar", 32);
			put("text", 226);
			put("longtext", 232);

			put("bit", 113);

			put("tinyint", 65);
			put("smallint", 66);
			put("int", 68);
			put("bigint", 72);
			put("decimal", 81);
			put("float", 84);
			put("double", 84);

			put("year", 161);
			put("date", 163);
			put("datetime", 184);
			put("time", 147);
			put("timestamp", 191);

			put("binary",192);
			put("blob",194);
			put("longblob",200);
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}

	public Byte getNumericPrecision() {
		return numericPrecision;
	}

	public void setNumericPrecision(Byte numericPrecision) {
		this.numericPrecision = numericPrecision;
	}

	public Byte getNumericScale() {
		return numericScale;
	}

	public void setNumericScale(Byte numericScale) {
		this.numericScale = numericScale;
	}

	public boolean isComputed() {
		return computed;
	}

	public void setComputed(boolean computed) {
		this.computed = computed;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}
	
	public boolean isAutoIncr() {
		return autoIncr;
	}

	public void setAutoIncr(boolean autoIncr) {
		this.autoIncr = autoIncr;
	}

	public Integer getDataTypeId(){
		return 0xFF & dataTypeMap.get(dataType);
//		try{
//			return 0xFF & dataTypeMap.get(dataType);
//		}
//		catch (Exception e){
//			return 0;
//		}
	}
	public String getJavaType(){
		return dataMap.get(dataType);
	}

	public boolean isJavaTypeObj(String javaType){
		return javaType.toCharArray()[0]<'a';
	}
	public String toString() {
		// data type
		if(!dataMap.containsKey(dataType)){
			System.out.println(dataType+" not defined.");
		}
		String dt = dataMap.get(dataType);
		if (nullable) {
			if (dt.equals("int")) {
				dt = "Integer";
			} else {
				dt = NamingUtil.firstLetterUpper(dt);
			}
		}

		StringBuilder sb = new StringBuilder();
		// comment
		if (comment != null && !comment.isEmpty()) {
			sb.append("/**").append(NL).append(" * ").append(comment).append(NL).append(" **/").append(NL);
		}
		// annotation
		if (!nullable) {
			sb.append("@NotNull").append(NL);
		}
		if (unsigned) {
			if (dt.equals("BigDecimal")) {
				sb.append("@DecimalMin(\"0.00\")").append(NL);
			} else {
				sb.append("@Min(0)").append(NL);
			}
		}
		if (dt.equals("String")) {
			String sz = null;
			if (nullable) {
				if(maxLength!=null) sz = String.format("@Size(max=%1$d)", maxLength);
			} else {
				if(maxLength!=null)
					sz = String.format("@Size(min=1,max=%1$d)", maxLength);
				else
					sz = "@Size(min=1)";
			}

			sb.append(sz).append(NL);
		}
//		if(dataType.equals("datetime")){
//			sb.append("@JsonFormat(timezone = \"GMT+0\")").append(NL);
//		}
		sb.append("@Getter @Setter").append(NL);//Lombok
		// private int m
		sb.append("private ").append(dt).append(' ').append(name).append(';').append(NL);
		return sb.toString();
	}
}
