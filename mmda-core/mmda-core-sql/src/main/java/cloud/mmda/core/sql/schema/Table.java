package cloud.mmda.core.sql.schema;

import java.sql.Timestamp;
import java.util.List;

public final class Table {
	private String db;
	private String name;
	private String type;
	private String comment;
	private Timestamp createTime;
	private Timestamp lastModified;
	private List<Column> columns;

	public String getDb(){
		return db;
	}
	public void setDb(String db){
		this.db = db;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {return type;}
	public void setType(String type){this.type = type;}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
		if(comment==null || comment.isEmpty()) return;
		if(comment.startsWith("@")){
			int spaceIndex = comment.indexOf(' ');
			if(spaceIndex>0){
				this.name = comment.substring(1, spaceIndex);//有大小写
			}
		}
	}
	public Timestamp getCreateTime(){
		return createTime;
	}
	public void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}
	public Timestamp getLastModified(){
		return lastModified;
	}
	public void setLastModified(Timestamp lastModified){
		this.lastModified = lastModified;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public boolean hasColumns(){
		return this.columns!=null && !this.columns.isEmpty();
	}
}
