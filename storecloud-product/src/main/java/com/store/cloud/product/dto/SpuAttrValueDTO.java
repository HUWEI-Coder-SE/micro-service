package com.store.cloud.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品规格属性关联信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class SpuAttrValueDTO{
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品属性值关联信息id" )
    private Long spuAttrValueId;

    @Schema(description = "商品id" )
    private Long spuId;

    @Schema(description = "规格属性id" )
    private Long attrId;

    @Schema(description = "规格属性名称" )
    private String attrName;

    @Schema(description = "规格属性值id" )
    private Long attrValueId;

    @Schema(description = "规格属性值名称" )
    private String attrValueName;

	public Long getSpuAttrValueId() {
		return spuAttrValueId;
	}

	public void setSpuAttrValueId(Long spuAttrValueId) {
		this.spuAttrValueId = spuAttrValueId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public Long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Long attrValueId) {
		this.attrValueId = attrValueId;
	}

	public String getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(String attrValueName) {
		this.attrValueName = attrValueName;
	}

	@Override
	public String toString() {
		return "SpuAttrValueDTO{" +
				"spuAttrValueId=" + spuAttrValueId +
				",spuId=" + spuId +
				",attrId=" + attrId +
				",attrName=" + attrName +
				",attrValueId=" + attrValueId +
				",attrValueName=" + attrValueName +
				'}';
	}
}
