package com.example.studentsmanagement.api;

import com.example.studentsmanagement.exceptions.InvalidUniversityClassException;
import com.example.studentsmanagement.exceptions.StudentEmptyNameException;
import com.example.studentsmanagement.exceptions.StudentNonExistException;
import com.example.studentsmanagement.model.Student;
import com.example.studentsmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/student")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/name")
    //localhost:8888/api/student/name?name=zhangsan
    public List<Student> getStudents(@RequestParam String name){
        return studentService.getStudentsByName(name);
    }

    @RequestMapping("/register")
    @PostMapping
    public ResponseEntity<String> registerStudent(@RequestBody Student student){
        try{
            Student savedStudent = studentService.addStudent(student);
            return ResponseEntity.ok("Registered student. "+student.toString());

        }catch(StudentEmptyNameException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @PostMapping(path = "assignclass/{sid}/{cid}")
    public ResponseEntity<String> assignClass(@PathVariable("sid") Long studentId,
                                              @PathVariable("cid") Long classId){
        try{
            Student updatedStudent = studentService.assignClass(studentId,classId);
            return ResponseEntity.ok("Assigned class. "+ updatedStudent.toString());

        }catch(StudentNonExistException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch(InvalidUniversityClassException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
