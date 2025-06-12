package com.manager.course.client;

public class Course {
    private Long id;
    private String courseId;
    private String courseName;
    private int credits;

    public Course(Long id, String courseId, String courseName, int credits) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }
    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCourseId() {
        return courseId;
    }
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    @Override
    public String toString() {
        return "ID: " + id + ", Course ID: " + courseId + ", Course Name: " + courseName + ", Credits: " + credits;
    }
}
