package com.manager.course.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServices {
    private final WebClient webClient;

    public ClientServices(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api").build();
    }

    public Flux<Course> getAllCourses() {
        return webClient.get().uri("/course").retrieve().bodyToFlux(Course.class);
    }

    public Mono<Course> getCourse(Long id) {
        return webClient.get().uri("/course/" + id).retrieve().bodyToMono(Course.class);
    }

    public Mono<Course> createCourse(Course course) {
        return webClient.post().uri("/course").bodyValue(course).retrieve().bodyToMono(Course.class);
    }

    public Mono<Course> updateCourse(Long id, Course course) {
        return webClient.put().uri("/course/" + id).bodyValue(course).retrieve().bodyToMono(Course.class);
    }

    public Mono<Void> deleteCourse(Long id) {
        return webClient.delete().uri("/course/" + id).retrieve().bodyToMono(Void.class);
    }
}
