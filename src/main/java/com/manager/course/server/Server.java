package com.manager.course.server;

import com.manager.course.client.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class Server {
    private final ArrayList<Course> courses = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public Server() {
        courses.add(new Course(counter.incrementAndGet(),"IF1210", "Algoritma dan Pemrograman 1", 3));
        courses.add(new Course(counter.incrementAndGet(),"IF1220", "Matematika Diskrit", 3));
        courses.add(new Course(counter.incrementAndGet(),"IF1221", "Logika Komputasional", 2));
        courses.add(new Course(counter.incrementAndGet(),"IF1230", "Organisasi dan Arsitektur Komputer", 3));
    }

    @GetMapping("/course")
    public List<Course> getCourse() {
        return courses;
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courses.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        course.setId(counter.incrementAndGet());
        courses.add(course);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        Optional<Course> course = courses.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (course.isPresent()) {
            Course existingCourse = course.get();
            existingCourse.setCourseName(updatedCourse.getCourseName());
            existingCourse.setCourseId(updatedCourse.getCourseId());
            existingCourse.setCredits(updatedCourse.getCredits());
            return ResponseEntity.ok(course.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable Long id) {
        boolean rm = courses.removeIf(p -> p.getId().equals(id));
        if (rm) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
