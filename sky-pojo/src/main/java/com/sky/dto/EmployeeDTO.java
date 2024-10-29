package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "员工注册的数据模型")
public class EmployeeDTO implements Serializable {

  @ApiModelProperty("用户id")
  private Long id;

  @ApiModelProperty("用户username")
  private String username;

  @ApiModelProperty("用户name")
  private String name;

  @ApiModelProperty("用户phone")
  private String phone;

  @ApiModelProperty("用户sex")
  private String sex;

  @ApiModelProperty("用户idNumber")
  private String idNumber;

}
