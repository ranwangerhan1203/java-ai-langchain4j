package com.it.javaailangchain4j.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.javaailangchain4j.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}