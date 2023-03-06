package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto) {

        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(studentDto.getAddress());
        addressRepository.save(student.getAddress());
        studentRepository.save(student);
        return "Student added";
    }

    @PutMapping("/{id}")
    public boolean upStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isPresent()) {
            Student student = byId.get();
            student.setLastName(studentDto.getLastName());
            student.setFirstName(studentDto.getFirstName());
            Address addressDto = studentDto.getAddress();
            Address address = student.getAddress();

            address.setStreet(addressDto.getStreet());
            List<Subject> subject = student.getSubjects();
            List<Subject> subject1 = studentDto.getSubjectList();
            for (int i = 0; i < subject.size(); i++) {
                for (int j = 0; j < subject1.size(); j++) {
                    if (i == j) {
                        subject.get(i).setName(subject1.get(j).getName());
                    }
                }
            }
            subjectRepository.saveAll(subject);
            return true;
        }
        return false;
    }

    @DeleteMapping("/{id}")
    public String deleted(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "Student deleted";
    }





}
