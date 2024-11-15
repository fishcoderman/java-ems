package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

  @Autowired
  private DishMapper dishMapper;
  @Autowired
  private DishFlavorMapper dishFlavorMapper;
  @Autowired
  private SetmealDishMapper setmealDishMapper;

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

  /**
   * 删除菜品
   *
   * @param ids
   * @return
   */
  @Transactional
  public void deleteBath(List<Long> ids) {
    // 判断菜品是否在售卖中
    for (Long id : ids) {
      Dish dish = dishMapper.getById(id);
      if (dish.getStatus() == StatusConstant.ENABLE) {
        throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
      }
    }
    // 查询当前的菜品列表是不是被套餐关联了
    List<Long> setmealIds = setmealDishMapper.getSetmealsIdsByDishIds(ids);
    if (setmealIds != null && setmealIds.size() > 0) {
      throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
    }

    //    for (Long id : ids) {
    //      // 删除菜品
    //      dishMapper.deleteById(id);
    //      // 删除口味数据
    //      dishFlavorMapper.deleteByDishId(id);
    //    }

    // 删除菜品
    dishMapper.deleteByIds(ids);

    // 删除口味数据
    dishFlavorMapper.deleteByDishIds(ids);
  }

  /**
   * 根据id获取菜品
   *
   * @param id
   * @return
   */
  public DishVO getByIdWithFlavor(Long id) {
    Dish dish = dishMapper.getById(id);
    List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

    DishVO dishVO = new DishVO();
    BeanUtils.copyProperties(dish, dishVO);
    dishVO.setFlavors(dishFlavors);

    return dishVO;
  }

  /**
   * 修改菜品
   *
   * @param dishDTO
   */
  public void updateWithFlavor(DishDTO dishDTO) {
    // 修改菜品信息
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    dishMapper.update(dish);
    // 删除原有口味数据
    dishFlavorMapper.deleteByDishId(dishDTO.getId());
    // 重新插入口味数据
    List<DishFlavor> dishFlavors = dishDTO.getFlavors();
    if (dishFlavors != null && dishFlavors.size() > 0) {
      dishFlavors.forEach(item -> {
        item.setDishId(dishDTO.getId());
      });
      dishFlavorMapper.insertBatch(dishFlavors);
    }
  }

  /**
   * 条件查询菜品和口味
   *
   * @param dish
   * @return
   */
  public List<DishVO> listWithFlavor(Dish dish) {
    List<Dish> dishList = dishMapper.list(dish);

    List<DishVO> dishVOList = new ArrayList<>();

    for (Dish d : dishList) {
      DishVO dishVO = new DishVO();
      BeanUtils.copyProperties(d, dishVO);

      //根据菜品id查询对应的口味
      List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

      dishVO.setFlavors(flavors);
      dishVOList.add(dishVO);
    }

    return dishVOList;
  }
}
