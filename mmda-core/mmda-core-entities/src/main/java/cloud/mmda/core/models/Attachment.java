/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.CompositeKey;
import cloud.mmda.core.entities.CompositeTenancyKey;
import cloud.mmda.core.entities.TenancyEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * 附件
 *
 * @remarks 附件
 *
 * @author mmda codebot
 * @version 4.0.0
 * @since 2023-11-28 00:20:35.0
 *
 */
public class Attachment extends TenancyEntity<Attachment.Key> {
    //region ~GENERATED PARTS BEGIN
    /**
     * 对象名称
     */
    @NotBlank
    @Size(min=1,max=30)
    @Getter @Setter
    private String objName;
    /**
     * 对象标识
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private long objID;
    /**
     * 附件名称
     */
    @NotBlank
    @Size(min=1,max=255)
    @Getter @Setter
    private String fileName;
    /**
     * 文件大小
     */
    @NotNull
    @Getter @Setter
    private long fileSize;
    /**
     * 上传人
     */
    @Size(max=50)
    @Getter @Setter
    private String uploader;
    /**
     * 上传时间
     */
    @NotNull
    @Getter @Setter
    private Timestamp uploadTime;
    //region partition
    /**
     * 获取租户分区ID
     * 实现{@link Tenancy#getPartitionID()}接口
     */
    @JsonIgnore
    @Override
    public final long getPartitionID(){
        return objID;
    }
    /**
     * 设置租户分区ID
     * 实现{@link TenancyEntity#setPartitionID(long)}接口
     */
    @Override
    public final void setPartitionID(final long partitionID){
        objID = partitionID;
    }

    //endregion of partition

    public interface Key extends CompositeTenancyKey {
        String getObjName();
        long getObjID();
        String getFileName();
    }

    /**
     * 主键类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    static final class KeyImpl implements Key {
        /**
         * 对象名称
         */
        @NotBlank
        @Size(min=1,max=30)
        @Getter @Setter
        private String objName;
        /**
         * 对象标识
         */
        @NotNull
        @Min(0)
        @Getter @Setter
        private long objID;
        /**
         * 附件名称
         */
        @NotBlank
        @Size(min=1,max=255)
        @Getter @Setter
        private String fileName;
        /**
         * 获取租户分区ID
         * 实现{@link Tenancy#getPartitionID()}接口
         */
        @JsonIgnore
        @Override
        public final long getPartitionID(){
            return objID;
        }
        /**
         * 转为逗号隔开的字符串形式
         * @return 返回字符串，如k1,k2
         */
        @Override
        public String toString() {
            return join(objName,objID,fileName);
        }

        /**
         * 解析字符串
         * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
         * @return 主键对象
         */
        public static final KeyImpl parse(String ks) {
            Objects.requireNonNull(ks);
            String[] keys = CompositeKey.split(ks);
            return new KeyImpl(keys[0],Long.parseLong(keys[1]),keys[2]);
        }
    }
	/**
	 * 作为主键
	 */
    @Override
	public final Key getId(){
		return new KeyImpl(objName, objID, fileName);
	}

    @Override
    public Class<Key> getIdClass() {
        return Key.class;
    }

    /**
	 * 元数据包括字段和子表关系名称
	 */
	public static final class Meta {
		public static final String _objName = "objName";
		public static final String _objID = "objID";
		public static final String _fileName = "fileName";
		public static final String _fileSize = "fileSize";
		public static final String _uploader = "uploader";
		public static final String _uploadTime = "uploadTime";
        private Meta(){}
	}
	//endregion of ~GENERATED PARTS END

}