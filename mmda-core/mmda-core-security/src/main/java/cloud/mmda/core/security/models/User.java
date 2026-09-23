package cloud.mmda.core.security.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.MasterTenancyEntity;
import cloud.mmda.core.entities.TenancyEntity;
import cloud.mmda.core.enums.MessageChannel;
import cloud.mmda.core.enums.MessageChannelSet;
import cloud.mmda.core.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户
 *
 * @author Roy Luo
 * @since 4.0
 */
@Data
@Entity
@Table(name="user",catalog = "mmda_base")
public class User extends MasterTenancyEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 用户ID
	 */

    @Id
    private long userID;
	/**
	 * 用户名称
	 */
	@Size(max=30)
    private String username;
	/**
	 * 头像
	 */
	@Size(max=255)
    private String avatar;
	/**
	 * 电话区号
	 */
	@Size(max=5)
    @Column(name="telPrefix")
    private String telPrefix;
	/**
	 * 手机号
	 */
	@Size(max=30)
    private String mobile;
	/**
	 * 邮箱
	 */
	@Size(max=255)
    private String email;
	/**
	 * 关联人：HAS_ONE Person(personID,personName)
	 */
    private Long personID;
	/**
	 * 员工否
	 */
    private boolean staff;
	/**
	 * 部门：REF Department(deptID,deptName)
	 */
    private Long deptID;
	/**
	 * 登录密码
	 */
	@NotBlank
    @Column(name="signInPwd")
    private String signInPwd;
	/**
	 * 登录密码过期
	 */
    @Column(name="signInPwdExpiredAt")
    private OffsetDateTime signInPwdExpiredAt;

    /**
     * 状态：0;NEW;注册|1;ACTIVE;激活|-1;LOCKED;锁定|-2;DEAD;已注销
     */
    @Transient
    private UserStatus status = UserStatus.NEW;
    @Column(name = "status")
    @Basic
    private short statusValue = 0;
	/**
	 * 标签
	 */
	@Size(max=255)
    private String tags;

    /**
     * 通知订阅方式：0;SYSTEM;系统|1;MAIL;邮件|2;SMS;短信|4;PUSH;推送消息
     * @see <a href="https://www.baeldung.com/jpa-persisting-enums-in-jpa">Persisting Enums in JPA</a>
     */
    @Transient
    private MessageChannelSet subscribedChannels = MessageChannelSet.of(MessageChannel.INTERNAL);
    @Column(name = "subscribedChannels")
    @Basic
    private int subscribedChannelsValue = 0;
	/**
	 * 创建人
	 */
	@Size(max=100)	 
    @CreatedBy
    private String creator;

	/**
	 * 创建时间
	 */
    @CreatedDate
    @Column(name="createdDate",columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdDate;

    @LastModifiedBy
	/**
	 * 修改人
	 */
    @Column(name="lastModifier")
	@Size(max=100)
    private String lastModifier;

	/**
	 * 最后修改
	 */
    @LastModifiedDate
    @Column(name="lastModified",columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime lastModified;

    /**
     * 角色
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();

	/**
	 * 设备
	 */
    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY)
	private Set<UserDevice> devices = new HashSet<>();
	/**
	 * 开放标识
	 */
    @OneToMany( mappedBy = "user", fetch = FetchType.EAGER)
	private Set<UserOpenIdentity> openIdentities = new HashSet<>();


    //region partition
    /**
     * 获取租户分区ID
     * 实现{@link Tenancy#getPartitionID()}接口
     */
    @JsonIgnore
    @Override
    public final long getPartitionID(){
        return userID;
    }
    /**
     * 设置租户分区ID
     * 实现{@link TenancyEntity#setPartitionID(long)}接口
     */
    @Override
    public final void setPartitionID(final long partitionID){
        userID = partitionID;
    }
    //endregion of partition

    /**
     * 作为主键
     */
    public final Long getId(){
        return userID;
    }

    @Override
    public Class<Long> getIdClass() {
        return Long.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID == user.userID;
    }

    @PostLoad
    public void fillTransient() {
        this.status = UserStatus.valueOf(statusValue);
        this.subscribedChannels = MessageChannelSet.valueOf(subscribedChannelsValue);
    }

    @PrePersist
    public void fillPersistent() {
        this.statusValue = status.getValue();
        this.subscribedChannelsValue = subscribedChannels.getValue();
    }
}
