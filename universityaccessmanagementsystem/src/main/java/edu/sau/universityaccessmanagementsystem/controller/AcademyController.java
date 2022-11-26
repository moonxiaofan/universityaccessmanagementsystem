package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.entity.Academy;
import edu.sau.universityaccessmanagementsystem.service.AcademyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 17:01
 * @Version 1.0
 */
@RestController
@RequestMapping("academy")
public class AcademyController {

    private static final Logger logger = LoggerFactory.getLogger(AcademyController.class);

    @Autowired
    private AcademyService academyService;

    @PostMapping("insert")
    private Object insertAcademy(@RequestBody Academy academy) {
        return academyService.insertAcademy(academy);
    }

    @GetMapping("getAllList")
    private Object getAllList() {
        return academyService.getAllList();
    }

    @PostMapping("edit")
    private Object editAcademy(@RequestBody Academy academy) {
        return academyService.editAcademy(academy);
    }

}
