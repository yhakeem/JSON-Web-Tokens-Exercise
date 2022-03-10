package com.example.jfs_jwt.controller;

import com.example.jfs_jwt.config.JwtFilter;
import com.example.jfs_jwt.model.Course;
import com.example.jfs_jwt.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CourseController {
   CourseService courseService;

//dependancy injection of course service into coursecontroller
@Autowired
public CourseController(CourseService courseService) {
    this.courseService = courseService;
}

@CrossOrigin("*")
@GetMapping("/api/courses")
public List<Course> all(){
    return courseService.all();
}


@PostMapping("/api/courses")
public void  save(@RequestBody Course course ){
    courseService.save(course);
}

@DeleteMapping("/api/courses/{courseId}")
public void delete(@PathVariable("courseId") String courseId){
    courseService.delete(courseId);
}




}
