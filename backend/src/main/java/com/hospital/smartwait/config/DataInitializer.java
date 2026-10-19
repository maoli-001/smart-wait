package com.hospital.smartwait.config;

import com.hospital.smartwait.model.Department;
import com.hospital.smartwait.model.Doctor;
import com.hospital.smartwait.model.Patient;
import com.hospital.smartwait.model.Schedule;
import com.hospital.smartwait.repository.DepartmentRepository;
import com.hospital.smartwait.repository.DoctorRepository;
import com.hospital.smartwait.repository.PatientRepository;
import com.hospital.smartwait.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 演示数据初始化：科室、医生、患者、今日号源排班。
 *
 * <p>默认账号（演示用）：
 *  管理员  admin / admin123
 *  医生    张医生 zhang/123、李医生 li/123、王医生 wang/123
 *  患者    张三 zhang3/123、李四 li4/123、王五 wang5/123
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;

    public DataInitializer(DepartmentRepository departmentRepository,
                           DoctorRepository doctorRepository,
                           PatientRepository patientRepository,
                           ScheduleRepository scheduleRepository) {
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            log.info("演示数据已存在，跳过初始化");
            return;
        }

        Department neiKe = departmentRepository.save(new Department("内科", "常见内科疾病门诊"));
        Department waiKe = departmentRepository.save(new Department("外科", "外科门诊"));
        Department erKe = departmentRepository.save(new Department("儿科", "儿童门诊"));

        Doctor zhang = doctorRepository.save(new Doctor("张医生", "主任医师", neiKe.getId(), "zhang", "123"));
        Doctor li = doctorRepository.save(new Doctor("李医生", "主治医师", waiKe.getId(), "li", "123"));
        Doctor wang = doctorRepository.save(new Doctor("王医生", "副主任医师", erKe.getId(), "wang", "123"));

        patientRepository.save(new Patient("张三", "13800000001", "zhang3", "123"));
        patientRepository.save(new Patient("李四", "13800000002", "li4", "123"));
        patientRepository.save(new Patient("王五", "13800000003", "wang5", "123"));

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        scheduleRepository.save(new Schedule(zhang.getId(), today, 10));
        scheduleRepository.save(new Schedule(li.getId(), today, 10));
        scheduleRepository.save(new Schedule(wang.getId(), today, 10));

        log.info("演示数据初始化完成（科室 3、医生 3、患者 3、今日排班 3）");
    }
}
