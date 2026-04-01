package com.it.javaailangchain4j.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.javaailangchain4j.entity.Appointment;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);


    int countByCondition(Appointment query);
}