package com.sky.controller.admin.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺管理
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺管理")
public class ShopController {
  public static final String KEY = "SHOP_STATUS";

  @Autowired
  private RedisTemplate redisTemplate;

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
