package com.example.school.service;

import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;
import com.example.school.repository.StudentRepository;
import com.example.school.service.StudentH2Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
public class StudentH2Service implements StudentRepository {

  @Autowired
  private JdbcTemplate db;

  @Override
  public ArrayList<Student> getStudents() {
    return (ArrayList<Student>) db.query("select * from student", new StudentRowMapper());
  }

  @Override
  public Student getStudentById(int studentId) {
    try {
      Student student = db.queryForObject("select * from student where studentId = ?", new StudentRowMapper(),
          studentId);
      return student;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Student addSingleStudent(Student student) {
    db.update("insert into student(studentName , gender, standard) value (?,?,?)", student.getStudentName(),
        student.getGender(), student.getStandard());
    return db.queryForObject("select * fom student where studentName = ? and gender= ? and standard =?",
        new StudentRowMapper(),
        student.getStudentName(), student.getGender(), student.getStandard());

  }

  @Override
  public String addMultipleStudents(ArrayList<Student> studentsList) {

    for (Student eachStudent : studentsList) {

      db.update("insert into student(studentName, gender,standard) values (?,?,?)", eachStudent.getStudentName(),
          eachStudent.getGender(), eachStudent.getStandard());

    }
    String responseMessage = String.format("Successfully added %d students", studentsList.size());
    return responseMessage;
  }

  @Override
  public Student updateStudent(int studentId, Student student) {
    if (student.getStudentName() != null) {
      db.update("update student set studentName = ? where studentId = ?", student.getStudentName(), studentId);

    }
    if (student.getGender() != null) {
      db.update("update student set Gender = ? where studentId = ?", student.getGender(), studentId);

    }
    if (student.getStandard() != 0) {
      db.update("update student set Standard = ? where studentId = ?", student.getStandard(), studentId);

    }
    return getStudentById(studentId);
  }

  @Override
  public void deleteStudent(int studentId) {
    db.update("delete from student where studentId =  ?", studentId);
  }

}
