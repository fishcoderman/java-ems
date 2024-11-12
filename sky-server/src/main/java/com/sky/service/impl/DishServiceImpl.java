package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

  @Autowired
  private DishMapper dishMapper;
  @Autowired
  private DishFlavorMapper dishFlavorMapper;

  /**
   * 新增菜品和对应的口味数据
   *
   * @param dishDTO
   */
  @Transactional
  public void saveWithFlavor(DishDTO dishDTO) {
    // 向菜品插入一条数据
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    dishMapper.insert(dish);

    // 获取id
    Long dishId = dish.getId();

    // 向口味表插入数据
    List<DishFlavor> dishFlavors = dishDTO.getFlavors();
    if (dishFlavors != null && dishFlavors.size() > 0) {
      dishFlavors.forEach(item -> {
        item.setDishId(dishId);
      });
      dishFlavorMapper.insertBatch(dishFlavors);
    }
  }

  /***
   *  分页查询
   * @param dishPageQueryDTO 分页参数
   * @return PageResult
   */
  public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
    PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
    Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
    long total = page.getTotal();
    List<DishVO> records = page.getResult();
    return new PageResult(total, records);
  }
}
