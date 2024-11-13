package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺管理")
public class ShopController {
  public static final String KEY = "SHOP_STATUS";

  @Autowired
  private RedisTemplate redisTemplate;

  /**
   * 设置店铺营业状态
   *
   * @param status
   * @return status
   */
  @PutMapping("/{status}")
  @ApiOperation("设置店铺营业状态")
  public Result<Integer> setStatus(@PathVariable Integer status) {
    log.info("设置店铺营业状态:{}", status == 1 ? "营业中" : "打烊中");
    redisTemplate.opsForValue().set(KEY, status);
    return Result.success(status);
  }

  /**
   * 设置店铺营业状态
   *
   * @return
   */
  @GetMapping("/status")
  @ApiOperation("获取店铺营业状态")
  public Result<Integer> getStatus() {
    Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
    log.info("获取店铺营业状态:{}", status == 1 ? "营业中" : "打烊中");
    return Result.success(status);
  }
}
