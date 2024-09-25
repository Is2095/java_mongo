package com.mongospring.mongospring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongospring.mongospring.model.Student;
import com.mongospring.mongospring.repository.StudentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StudentController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    StudentRepository  studentRepository;

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody Student student){

        studentRepository.save(student);
    }
    @GetMapping("/getStudent")
    public List<String> getStudentAll(){
        List<Student> estudiantes = studentRepository.findAll();
        return estudiantes.stream()
                .map(Student::getId)
                .collect(Collectors.toList());

    }
    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable ObjectId id) {
        return studentRepository.findById(id).orElse(null);
    }
    @PutMapping("/updateStudent/{id}")
    public Student updateStudent(@PathVariable ObjectId id, @RequestBody Student student){
        Student data = studentRepository.findById(id).orElse(null);
        if(data != null) {
        System.out.println(student.getName());
            data.setName(student.getName());
            data.setAddress(student.getAddress());

            studentRepository.save(data);
        }

        return null;
    }
    @GetMapping("/student")
    public List<Student> students() {
        return mongoTemplate.findAll(Student.class);
    }
    @GetMapping("/student/{id}")
    public Student getStudentTemplate(@PathVariable ObjectId id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Student.class);
    }
    @PutMapping("/student/{id}")
    public Student putStudent2(@PathVariable ObjectId id, @RequestBody String newName) throws JsonProcessingException {
       ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(newName);

        Map<String, String> datoDinamico = mapper.convertValue(root, Map.class);

        for(Map.Entry<String, String> entry : datoDinamico.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            System.out.println(key + "----" + value);
        }

        String name = root.get("name").asText();
        String address = root.get("address").asText();

//        System.out.println("Name: " + name);
//        System.out.println("Address: " + address);

//        ObjectMapper objectMapper = new ObjectMapper();
//        String newStudentName = objectMapper.readValue(newName, Map.class).get("name").toString();
//        Query query = new Query(Criteria.where("id").is(id));
//        Update update = new Update().set("name", newStudentName);
//        mongoTemplate.updateFirst(query, update, Student.class);
        return null;
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable ObjectId id) {
        studentRepository.deleteById(id);
    }
    @DeleteMapping("/student/{id}")
    public void deleteStudent2(@PathVariable ObjectId id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Student.class);
    }
}
