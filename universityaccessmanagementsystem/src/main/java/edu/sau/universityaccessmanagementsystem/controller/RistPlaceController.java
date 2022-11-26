package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.service.RistPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 16:30
 * @Version 1.0
 */
@RestController
@RequestMapping("ristPlace")
public class RistPlaceController {

    @Autowired
    private RistPlaceService ristPlaceService;

    @GetMapping("updateRistPlace")
    public Object updateRistPlace() {
        return ristPlaceService.updateRistPlace();
    }

    @GetMapping("getRistPlace")
    public Object getRistPlace() {
        return ristPlaceService.getRistPlace();
    }


}
