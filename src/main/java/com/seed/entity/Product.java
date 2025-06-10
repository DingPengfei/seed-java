package com.seed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 绑定的用户ID
     */
    private Long bindUserId;

    /**
     * 设备唯一编码
     */
    private String productCode;

    /**
     * 芯片UID
     */
    private String chipUid;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * 自定义UID
     */
    private String customUid;

    /**
     * 空间编码，关联外键
     */
    private String spaceCode;

    /**
     * 产品型号ID
     */
    private Integer productModelId;

    /**
     * 名称
     */
    private String name;

    /**
     * 唤醒词
     */
    private String wakeupWord;

    /**
     * 配置信息
     */
    private String config;

    /**
     * 状态（00-生效；01-无效）
     */
    private String status;

    /**
     * 创建者
     */
    private String createOperatorMan;

    /**
     * 创建者id
     */
    private String createOperatorId;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改者
     */
    private String modifyOperatorMan;

    /**
     * 修改者id
     */
    private String modifyOperatorId;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 是否删除 1-删除 0-未删除
     */
    private String isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Product other = (Product) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBindUserId() == null ? other.getBindUserId() == null : this.getBindUserId().equals(other.getBindUserId()))
            && (this.getProductCode() == null ? other.getProductCode() == null : this.getProductCode().equals(other.getProductCode()))
            && (this.getChipUid() == null ? other.getChipUid() == null : this.getChipUid().equals(other.getChipUid()))
            && (this.getMacAddress() == null ? other.getMacAddress() == null : this.getMacAddress().equals(other.getMacAddress()))
            && (this.getCustomUid() == null ? other.getCustomUid() == null : this.getCustomUid().equals(other.getCustomUid()))
            && (this.getSpaceCode() == null ? other.getSpaceCode() == null : this.getSpaceCode().equals(other.getSpaceCode()))
            && (this.getProductModelId() == null ? other.getProductModelId() == null : this.getProductModelId().equals(other.getProductModelId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getWakeupWord() == null ? other.getWakeupWord() == null : this.getWakeupWord().equals(other.getWakeupWord()))
            && (this.getConfig() == null ? other.getConfig() == null : this.getConfig().equals(other.getConfig()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateOperatorMan() == null ? other.getCreateOperatorMan() == null : this.getCreateOperatorMan().equals(other.getCreateOperatorMan()))
            && (this.getCreateOperatorId() == null ? other.getCreateOperatorId() == null : this.getCreateOperatorId().equals(other.getCreateOperatorId()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getModifyOperatorMan() == null ? other.getModifyOperatorMan() == null : this.getModifyOperatorMan().equals(other.getModifyOperatorMan()))
            && (this.getModifyOperatorId() == null ? other.getModifyOperatorId() == null : this.getModifyOperatorId().equals(other.getModifyOperatorId()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBindUserId() == null) ? 0 : getBindUserId().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getChipUid() == null) ? 0 : getChipUid().hashCode());
        result = prime * result + ((getMacAddress() == null) ? 0 : getMacAddress().hashCode());
        result = prime * result + ((getCustomUid() == null) ? 0 : getCustomUid().hashCode());
        result = prime * result + ((getSpaceCode() == null) ? 0 : getSpaceCode().hashCode());
        result = prime * result + ((getProductModelId() == null) ? 0 : getProductModelId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getWakeupWord() == null) ? 0 : getWakeupWord().hashCode());
        result = prime * result + ((getConfig() == null) ? 0 : getConfig().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateOperatorMan() == null) ? 0 : getCreateOperatorMan().hashCode());
        result = prime * result + ((getCreateOperatorId() == null) ? 0 : getCreateOperatorId().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getModifyOperatorMan() == null) ? 0 : getModifyOperatorMan().hashCode());
        result = prime * result + ((getModifyOperatorId() == null) ? 0 : getModifyOperatorId().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", bindUserId=").append(bindUserId);
        sb.append(", productCode=").append(productCode);
        sb.append(", chipUid=").append(chipUid);
        sb.append(", macAddress=").append(macAddress);
        sb.append(", customUid=").append(customUid);
        sb.append(", spaceCode=").append(spaceCode);
        sb.append(", productModelId=").append(productModelId);
        sb.append(", name=").append(name);
        sb.append(", wakeupWord=").append(wakeupWord);
        sb.append(", config=").append(config);
        sb.append(", status=").append(status);
        sb.append(", createOperatorMan=").append(createOperatorMan);
        sb.append(", createOperatorId=").append(createOperatorId);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", modifyOperatorMan=").append(modifyOperatorMan);
        sb.append(", modifyOperatorId=").append(modifyOperatorId);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}