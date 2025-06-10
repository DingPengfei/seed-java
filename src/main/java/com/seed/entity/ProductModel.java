package com.seed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品型号
 * @TableName product_model
 */
@TableName(value ="product_model")
@Data
public class ProductModel implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 产品中文名称
     */
    private String chName;

    /**
     * 产品英文名称
     */
    private String enName;

    /**
     * 使用说明
     */
    private String instruction;

    /**
     * 产品logo
     */
    private String logo;

    /**
     * 产品类别（0-buddy；1-servant；2-；3-）
     */
    private String category;

    /**
     * 产品型号编码
     */
    private String modelCode;

    /**
     * 产品介绍
     */
    private String info;

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
        ProductModel other = (ProductModel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getChName() == null ? other.getChName() == null : this.getChName().equals(other.getChName()))
            && (this.getEnName() == null ? other.getEnName() == null : this.getEnName().equals(other.getEnName()))
            && (this.getInstruction() == null ? other.getInstruction() == null : this.getInstruction().equals(other.getInstruction()))
            && (this.getLogo() == null ? other.getLogo() == null : this.getLogo().equals(other.getLogo()))
            && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
            && (this.getModelCode() == null ? other.getModelCode() == null : this.getModelCode().equals(other.getModelCode()))
            && (this.getInfo() == null ? other.getInfo() == null : this.getInfo().equals(other.getInfo()))
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
        result = prime * result + ((getChName() == null) ? 0 : getChName().hashCode());
        result = prime * result + ((getEnName() == null) ? 0 : getEnName().hashCode());
        result = prime * result + ((getInstruction() == null) ? 0 : getInstruction().hashCode());
        result = prime * result + ((getLogo() == null) ? 0 : getLogo().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getModelCode() == null) ? 0 : getModelCode().hashCode());
        result = prime * result + ((getInfo() == null) ? 0 : getInfo().hashCode());
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
        sb.append(", chName=").append(chName);
        sb.append(", enName=").append(enName);
        sb.append(", instruction=").append(instruction);
        sb.append(", logo=").append(logo);
        sb.append(", category=").append(category);
        sb.append(", modelCode=").append(modelCode);
        sb.append(", info=").append(info);
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