# JSON-Web-Tokens-Exercise
JSON Web Tokens Exercise

Go over to spring initializr and create your project: https://start.spring.io/
Under project select: Maven project
Under Language select: Java
Under Spring Boot select: Whichever Version is the highest and doesn’t have (SNAPSHOT) or (M2)
Under Project Metadata, for artifact enter: jfs_jwt
Under Packaging select: Jar
Under Java select your version of Java: (8, 11, 17)
Click Add Dependencies and select: Spring Web, Spring Data JPA, and H2 Database.
Click: GENERATE




Open your zip folder and copy it to your desktop or wherever you keep your projects (make sure you unzip the folder).

Open your IntelliJ or any other IDE and select Open.

Navigate to where you copied your jfs_jwt project and select it, then click OK. Click Trust Project.


Locate your pom.xml file and add the following dependency below your spring-boot-starter-data-jpa dependency:
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
  <version>0.9.1</version>
</dependency>

Right click on your pom.xml file in your project directory window on the left and select “Maven” then “Reload Project”.

Locate your package com.example.jfs_jwt and create the following packages within this package:
config
controller
model
service

Within your config package, create a class named JwtFilter.

This class should extend GenericFilterBean and should contain the following code.
@Override
public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain )
       throws IOException, ServletException
{
   HttpServletRequest httpServletRequest = (HttpServletRequest) request;
   HttpServletResponse httpServletResponse = (HttpServletResponse) response;
   String authHeader = httpServletRequest.getHeader( "authorization" );

   if ( "OPTIONS".equals( ( httpServletRequest.getMethod() ) ) )
   {
       httpServletResponse.setStatus( HttpServletResponse.SC_OK );
   }
   else
   {
       if ( authHeader == null || !authHeader.startsWith( "Bearer " ) )
       {
           throw new ServletException( "Invalid Token!" );
       }

       String token = authHeader.substring( 7 );

       try
       {
           Claims claims = Jwts.parser().setSigningKey( "this-secret-is-not-very-secret-99" ).parseClaimsJws(
                   token ).getBody();
           request.setAttribute( "claims", claims );
       }
       catch ( SignatureException | MalformedJwtException e )
       {
           throw new ServletException( "Invalid Token!" );
       }
   }
   filterChain.doFilter( request, response );
}

Make sure you have the following imports at the top of your file:
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

Within your controller package, create the following classes:
AuthController
CourseController




Your AuthController class should have the @RestController annotation and contain the following code (You will see errors until you create and import your LoginDto and Token classes):

   @PostMapping( "/auth" )
   public Token auth( @RequestBody LoginDto loginDto )
           throws ServletException
   {
       if ( loginDto.getUsername().equals( "user@mail.com" ) && loginDto.getPassword().equals( "password" ) )
       {
           return new Token( generateToken( loginDto.getUsername() ) );
       }

       throw new ServletException( "Invalid login. Please check your email and password." );
   }

   private String generateToken( String email )
   {

       Calendar calendar = Calendar.getInstance();
       calendar.add( Calendar.HOUR, 10 );
       String secret = "this-secret-is-not-very-secret-99";
       return Jwts.builder().setSubject( email ).claim( "role", "user" ).setIssuedAt( new Date() ).setExpiration(
               calendar.getTime() ).signWith( SignatureAlgorithm.HS256, secret ).compact();
   }

Your AuthController class should have the following imports:
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.example.jfs_jwt.controller.dto.LoginDto;
import com.example.jfs_jwt.controller.dto.Token;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.ServletException;
import java.util.Calendar;
import java.util.Date;
Your CourseController class should have the @RestController annotation.

Use dependency injection to inject your CourseService class into your CourseController class 
(We will create the CourseService class shortly)

Create three methods in your CourseController class. The first method should have a return type of List<Course> and be named all(). This method should return courseService.all(). This method should have the @CrossOrigin(“*”) annotation and the @GetMapping(“/api/courses”) annotation.

The second method should have a return type of void and be named save(). This method should take a parameter of type Course named course and this parameter should have the @RequestBody annotation. This method should have the @PostMapping(“/api/courses”) annotation. This method should call courseService.save(course).

The third method should have a return type of void and should be named delete(). This method should take a parameter of type String named courseId and the parameter should have the @PathVariable(“courseId”) annotation. This method should have the @DeleteMapping(“/api/courses/{courseId}”) annotation on it. This method should call courseService.delete(courseId).

Within your controller package create another package named dto. Create the following two classes within your dto package:
LoginDto
Token

Your LoginDto class should have the following instance fields:
private final String username;
private final String password;

Create a constructor that takes in a username and a password parameter and assign these values to the corresponding instance fields.

Create getters for both the username and the password. Name these getUsername() and getPassword().

Your Token class should have an instance field of type String and should be named accessToken. This field should be private and final.

Create a constructor that takes in a String named accessToken and assign this value to the instance field accessToken. 

Create a getter named getAccessToken() that returns your accessToken.

Within your model package create a Course class. Your Course class should have the following instance fields:
final String id;
final String name;
final String objectives;

Create a constructor that takes in the following parameters and assign them to their corresponding instance fields:
String id
String name
String objectives

Create a getter for each of these that return the appropriate values and name them as follows:
getId()
getName()
getObjectives()

Create a toString method. See below:

@Override
public String toString()
{
   return "Course{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", objectives='" + objectives + '\'' + '}';
}

Within your service package create an interface named CourseService and a class named CourseServiceImpl.



Your CourseService interface should contain the following code:
package com.example.jfs_jwt.service;
import com.example.jfs_jwt.model.Course;
import java.util.List;

public interface CourseService
{
   List<Course> all();

   void save( Course course );

   void delete( String courseId );
}

Your CourseServiceImpl class should contain the following code:
package com.example.jfs_jwt.service;
import com.example.jfs_jwt.model.Course;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl
       implements CourseService
{
   private final Map<String, Course> courseList = new HashMap<>();

   public CourseServiceImpl()
   {
       courseList.put( "INTRO-CS-1", new Course( "INTRO-CS-1", "Introduction to Computer Science",
               "-Explain the basics about how the internet works\n"
                       + "-Explain the difference between a client and a server\n"
                       + "-Explain the TCP/IP protocol on a basic level\n"
                       + "-Explain the HTTP protocol\n"
                       + "-Explain and use the HTTP methods GET / POST / PUT / DELETE\n"
                       + "-Use HTTP methods GET and POST with an HTTP Client (Postman)\n"
                       + "-Use developer tools\n"
                       + "-Describe the difference between a Website and a Web Application\n"
                       + "-Give examples of Web Applications and Web Sites" ) );
       courseList.put( "INTRO-CS-2", new Course( "INTRO-CS-2", "Introduction to Algorithms",
               "-Explain what an algorithm is \n"
                       + "-Explain the structure of an algorithm\n"
                       + "-Explain what code comments are and how to write them in JavaScript\n"
                       + "-Describe what reserved words in JavaScript are and give examples\n"
                       + "-Use primitive types to create variables and algorithms\n"
                       + "-Declare and use variables of different types\n"
                       + "-Write simple and correct programs using JavaScript\n"
                       + "-Use the Javascript prompt function to capture a user input" ) );
       courseList.put( "INTRO-CS-3", new Course( "INTRO-CS-3", "Algorithm Design and Problem Solving - Introduction",
               "-Declare and use conditionals\n"
                       + "-Define functions using JavaScript\n"
                       + "-Write algorithms that solve mathematical problems\n"
                       + "-Manipulate Strings with JavaScript\n"
                       + "-Write algorithms that solve problems using String Functions\n"
                       + "-Use the Web debugger for JavaScript code on the Browser\n"
                       + "-Write algorithms that solve problems taking user inputs" ) );
       courseList.put( "INTRO-CS-4", new Course( "INTRO-CS-4", "Algorithm Design and Problem Solving - Advanced",
               "-Define and use arrays for numeric values\n"
                       + "-Define and use arrays for String values\n"
                       + "-Write algorithms that solve mathematical problems using arrays\n"
                       + "-Read, understand and fix code written by someone else\n"
                       + "-Write algorithms that solve problems using logical expresions \n"
                       + "-Explain and do code refactoring to improve code\n"
                       + "-Iterate arrays and modify its data\n"
                       + "-Write algorithms that solve basic sorting algorithms using arrays" ) );
   }

   @Override
   public List<Course> all()
   {
       return new ArrayList<>( courseList.values() );
   }

   @Override
   public void save( Course course )
   {
       courseList.put( course.getId(), course );
   }

   @Override
   public void delete( String courseId )
   {
       courseList.remove( courseId );
   }
}

Finally your JfsJwtApplication class (This is where you main method is located)you should have the following code after the main method:
@Bean
public FilterRegistrationBean<JwtFilter> jwtFilter()
{
  FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
  registrationBean.setFilter( new JwtFilter() );
  registrationBean.addUrlPatterns( "/api/*" );
  return registrationBean;
}

Now go back to your CourseController class and import any packages/classes that are needed.

Finally go back to your AuthController class and import any packages/classes that are needed.  

Time to test your code. Run your application and go over to Postman.

You will need to make a Post Request to receive your JWT. From within Postman, select the dropdown arrow next to GET and change it to POST. 


Continued on the next page...








Enter the following for your request URL:
http://localhost:8080/auth
Select Body.
Select raw.
Use the dropdown arrow next to Text to select JSON.
Enter the following with the text box:
{
    "username":"user@mail.com",
    "password":"password"
}
Click Send. You should receive your access token in the request Body.

Now click on the + button towards the top of your screen in postman so you can create a GET request.
 
 
Put the following in the field for request URL:
   http://localhost:8080/api/courses
 
Select Headers and in the key column at the bottom enter “Authorization” and in the value column enter “Bearer yourJSONWebToken” without any quotes (Make sure you have a space between “Bearer” and your Token).
 
Click Send and you should receive a list of courses in your response body.  
		
 

Create a repository in your GitHub and push your code there. Share the link to your repository in the “JAVA-WEB-5 Discussion Forum: BSM Connection” in Canvas.

