package com.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.student.management.dto.CreateStudentRequest;
import com.student.management.dto.StudentDto;
import com.student.management.dto.UpdateStudentRequest;
import com.student.management.mapper.StudentMapper;
import com.student.management.model.entity.Student;
import com.student.management.repository.StudentRepository;
import com.student.management.service.exception.DuplicateEmailException;
import com.student.management.service.exception.StudentNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDto studentDto;
    private CreateStudentRequest createRequest;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("Computer Science")
                .enrollmentYear(2024)
                .build();

        studentDto = StudentDto.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("Computer Science")
                .enrollmentYear(2024)
                .build();

        createRequest = CreateStudentRequest.builder()
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("Computer Science")
                .enrollmentYear(2024)
                .build();
    }

    @Test
    void createStudent_shouldReturnSavedStudent() {
        when(studentRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(studentMapper.toEntity(createRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.createStudent(createRequest);

        assertThat(result).isEqualTo(studentDto);
        verify(studentRepository).save(student);
    }

    @Test
    void createStudent_shouldThrowWhenEmailExists() {
        when(studentRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent(createRequest))
                .isInstanceOf(DuplicateEmailException.class);

        verify(studentRepository, never()).save(any());
    }

    @Test
    void getAllStudents_shouldReturnList() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        List<StudentDto> result = studentService.getAllStudents();

        assertThat(result).containsExactly(studentDto);
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.getStudentById(1L);

        assertThat(result).isEqualTo(studentDto);
    }

    @Test
    void getStudentById_shouldThrowWhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() {
        UpdateStudentRequest updateRequest = UpdateStudentRequest.builder()
                .firstName("Ana")
                .lastName("Updated")
                .email("ana@university.edu")
                .program("IT")
                .enrollmentYear(2025)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmailAndIdNot(updateRequest.getEmail(), 1L)).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.updateStudent(1L, updateRequest);

        assertThat(result).isEqualTo(studentDto);
        verify(studentMapper).updateEntity(updateRequest, student);
    }

    @Test
    void updateStudent_shouldThrowWhenDuplicateEmail() {
        UpdateStudentRequest updateRequest = UpdateStudentRequest.builder()
                .firstName("Ana")
                .lastName("Updated")
                .email("other@university.edu")
                .program("IT")
                .enrollmentYear(2025)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmailAndIdNot(updateRequest.getEmail(), 1L)).thenReturn(true);

        assertThatThrownBy(() -> studentService.updateStudent(1L, updateRequest))
                .isInstanceOf(DuplicateEmailException.class);

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_shouldDeleteWhenExists() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void deleteStudent_shouldThrowWhenNotFound() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(StudentNotFoundException.class);
    }
}
