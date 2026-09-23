/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.models;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import cloud.mmda.core.enums.ChangeType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 修改日志
 * 
 * @remarks 修改日志。记录一个操作导致数据的变更，用于取消操作、审计和变更等。
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-09-15 09:11:47.0
 * 
 */
public class ChangeLog extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 日志标识
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long logID;
	/**
	 * 数据差异
	 */
	@JsonIgnore
	@NotNull
	@Getter @Setter
	private byte[] difference;
	/**
	 * 引用名称，对象名
	 */
	@NotNull
	@Getter @Setter
	private String refName;
	/**
	 * 引用键值，对象唯一主键
	 */
	@NotNull
	@Getter @Setter
	private String refKey;
	/**
	 * 引用失效否，引用的对象是否已删除，true代表日志可清理
	 */
	@Getter @Setter
	private boolean refDeleted;

	/**
	 * 撤消否，true代表已执行了undo
	 */
	@Getter @Setter
	private boolean undone;

	/**
	 * 前一个日志ID
	 */
	@Getter @Setter
	private Long prevLogID;

	/**
	 * 标签
	 */
	@Getter @Setter
	private String tags;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return logID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		logID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	@Override
	public final Long getId(){
		return logID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _logID = "logID";
		public static final String _difference = "difference";
		public static final String _refName = "refName";
		public static final String _refKey = "refKey";
		public static final String _refDeleted = "refDeleted";
		public static final String _undone = "undone";
		public static final String _prevLogID = "prevLogID";
		public static final String _tags = "tags";
	}
	//endregion of ~GENERATED PARTS END

	//public boolean writeDifference(Object difference) {
	//	try (var stream = new ByteArrayOutputStream(); var output = new ObjectOutputStream(stream)) {
	//		output.writeObject(difference);
	//		output.flush();
	//		this.difference = stream.toByteArray().clone();
	//		return true;
	//	}
	//	catch (IOException ex){
	//		return false;
	//	}
	//}
	//public Object readDifference() {
	//	try (var inputStream = new ObjectInputStream(new ByteArrayInputStream(difference))) {
	//		return inputStream.readObject();
	//	}
	//	catch (IOException | ClassNotFoundException ex){
	//		return null;
	//	}
	//}
	//解决	org.springframework.dao.DataIntegrityViolationException PreparedStatementCallback; Data truncation: Data too long for column 'difference'
	public boolean writeDifference(Object difference) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 GZIPOutputStream gzip = new GZIPOutputStream(stream);
			 ObjectOutputStream output = new ObjectOutputStream(gzip)) {

			output.writeObject(difference);
			output.flush();
			gzip.finish();
			this.difference = stream.toByteArray();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
	public Object readDifference() {
		try (ObjectInputStream inputStream = new ObjectInputStream(
				new GZIPInputStream(new ByteArrayInputStream(difference)))) {
			return inputStream.readObject();
		} catch (IOException | ClassNotFoundException ex) {
			return null;
		}
	}
	@AllArgsConstructor
	public static class ChangeData<D> implements Serializable {
		@Getter @Setter
		private ChangeType t;
		@Getter @Setter
		private D o;
		@Getter @Setter
		private D n;
	}

	private static ChangeData UNCHANGED = new ChangeData(ChangeType.NONE, null, null);
	public static ChangeData unchanged() {
		return UNCHANGED;
	}
	public static ChangeData changed(Object o, Object n){
		return new ChangeData(ChangeType.CHANGED, o, n);
	}
	public static ChangeData added(Object n){
		return new ChangeData(ChangeType.ADDED, null, n);
	}
	public static ChangeData removed(Object o){
		return new ChangeData(ChangeType.REMOVED, o, null);
	}
}
