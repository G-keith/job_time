package com.job.mapper;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Repository
public interface SysBondMapper {

    BigDecimal findBond();
}
