package com.manager.course;

import com.manager.course.client.ClientServices;
import com.manager.course.client.Course;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class CourseApplication {

	public static List<String> parse(String input) {
		List<String> result = new ArrayList<>();
		Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(input);
		while (m.find()) {
			if (m.group(1) != null) {
				result.add(m.group(1)); // quoted text
			} else {
				result.add(m.group(2)); // unquoted word
			}
		}
		return result;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CourseApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run(ClientServices clientServices) {
		return args -> {
			while (true) {
				System.out.println("Springboot Course Manager");
				System.out.println("1. getAll");
				System.out.println("2. get {id}");
				System.out.println("3. add {courseId} \"{courseName}\" {credits}");
				System.out.println("4. edit {id} {courseId} \"{courseName}\" {credits}");
				System.out.println("5. delete {id}");
				System.out.println("6. exit");
				System.out.println("\n");

				System.out.print(">> ");
				Scanner sc = new Scanner(System.in);
				String line = sc.nextLine();

				List<String> parts = parse(line);

				if (parts.getFirst().equals("getAll")) {
					if (parts.size() != 1) {
						System.out.println("ERROR: Expected argument(s) = 1");
					} else {
						Flux<Course> res = clientServices.getAllCourses();
						try {
							List<Course> res_list = res.collectList().block();
							if (res_list != null) {
								System.out.println("Response: ");
								for (Course course : res_list) {
									System.out.println(course.toString());
								}
							}
						} catch (Exception ex) {
							System.err.println("Failed to get all courses: " + ex.getMessage());
						}
					}
				} else if (parts.getFirst().equals("get")) {
					if (parts.size() != 2) {
						System.out.println("ERROR: Expected argument(s) = 2");
					} else {
						Mono<Course> res = clientServices.getCourse(Long.parseLong(parts.get(1)));
						try {
							Course res_list = res.block();
							if (res_list != null) {
								System.out.println("Response: ");
								System.out.println(res_list.toString());
							}
						} catch (Exception ex) {
							System.err.println("Failed to get the course" + parts.get(1) + ": " + ex.getMessage());
						}
					}
				} else if (parts.getFirst().equals("add")) {
					if (parts.size() != 4) {
						System.out.println("ERROR: Expected argument(s) = 4");
					} else {
						int credits = Integer.parseInt(parts.get(3));
						Course newCourse = new Course(null, parts.get(1), parts.get(2), credits);
						Mono<Course> res = clientServices.createCourse(newCourse);
						try {
							Course res_list = res.block();
							if (res_list != null) {
								System.out.println("Response: ");
								System.out.println(res_list.toString());
							}
						} catch (Exception ex) {
							System.err.println("Failed to add the course "+ newCourse.getCourseName() + ": " + ex.getMessage());
						}
					}
				} else if (parts.getFirst().equals("edit")) {
					if (parts.size() != 5) {
						System.out.println("ERROR: Expected argument(s) = 5");
					} else {
						Long id = Long.parseLong(parts.get(1));
						int credits = Integer.parseInt(parts.get(4));
						Course updatedCourse = new Course(id, parts.get(2), parts.get(3), credits);
						Mono<Course> res = clientServices.updateCourse(id, updatedCourse);
						try {
							Course res_list = res.block();
							if (res_list != null) {
								System.out.println("Response: ");
								System.out.println(res_list.toString());
							}
						} catch (Exception ex) {
							System.err.println("Failed to edit the course "+ id + ": " + ex.getMessage());
						}
					}
				} else if (parts.getFirst().equals("delete")) {
					if (parts.size() != 2) {
						System.out.println("ERROR: Expected argument(s) = 2");
					} else {
						Long id = Long.parseLong(parts.get(1));
						try {
							clientServices.deleteCourse(id).doOnSuccess(res ->
									System.out.println("Response: OK")).block();
						} catch (Exception ex) {
							System.err.println("Failed to delete the course "+ id + ": " + ex.getMessage());
						}
					}
				} else if (parts.getFirst().equals("exit")) {
					break;
				} else {
					System.out.println("ERROR: Unknown command");
				}
			}
			System.out.println();
		};
	}
}
