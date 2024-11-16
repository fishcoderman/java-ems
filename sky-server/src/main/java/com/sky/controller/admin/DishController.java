package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关")
public class DishController {
  @Autowired
  private DishService dishService;

  /**
   * 新增菜品
   *
   * @return
   */
  @PostMapping()
  @ApiOperation("新增菜品")
  public Result<String> save(@RequestBody DishDTO dishDTO) {
    log.info("新增菜品: {}", dishDTO);
    dishService.saveWithFlavor(dishDTO);
    return Result.success();
  }

  /**
   * 菜单分页
   *
   * @param dishPageQueryDTO
   * @return
   */
  @GetMapping("/page")
  @ApiOperation("菜单分页")
  public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
    PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 删除菜品
   *
   * @param ids
   * @return
   */
  @DeleteMapping
  @ApiOperation("删除菜品")
  public Result delete(@RequestParam List<Long> ids) {
    log.info("删除菜品: {}", ids);
    dishService.deleteBath(ids);
    return Result.success();
  }

  /**
   * 根据id获取菜品
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  @ApiOperation("根据id获取菜品")
  public Result<DishVO> getById(@PathVariable Long id) {
    DishVO dish = dishService.getByIdWithFlavor(id);
    return Result.success(dish);
  }


  /**
   * 修改菜品
   *
   * @return
   */
  @PutMapping()
  @ApiOperation("修改菜品")
  public Result update(@RequestBody DishDTO dishDTO) {
    log.info("修改菜品: {}", dishDTO);
    dishService.updateWithFlavor(dishDTO);
    return Result.success();
  }

  /**
   * 根据分类id查询菜品
   *
   * @param categoryId
   * @return
   */
  @GetMapping("/list")
  @ApiOperation("根据分类id查询菜品")
  public Result<List<Dish>> list(Long categoryId) {
    List<Dish> list = dishService.list(categoryId);
    return Result.success(list);
  }

}
