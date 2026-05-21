package com.student.management.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.management.dto.CreateStudentRequest;
import com.student.management.dto.StudentDto;
import com.student.management.dto.UpdateStudentRequest;
import com.student.management.excel.StudentExcelExporter;
import com.student.management.service.StudentService;
import com.student.management.web.exception.GlobalExceptionHandler;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private StudentService studentService;

    @Mock
    private StudentExcelExporter studentExcelExporter;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createStudent_shouldReturn201() throws Exception {
        CreateStudentRequest request = CreateStudentRequest.builder()
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("CS")
                .enrollmentYear(2024)
                .build();

        StudentDto response = StudentDto.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("CS")
                .enrollmentYear(2024)
                .build();

        when(studentService.createStudent(any())).thenReturn(response);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ana"));
    }

    @Test
    void getAllStudents_shouldReturn200() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(
                StudentDto.builder().id(1L).firstName("Ana").lastName("Hoxha")
                        .email("a@u.edu").program("CS").enrollmentYear(2024).build()
        ));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Ana"));
    }

    @Test
    void updateStudent_shouldReturn200() throws Exception {
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .firstName("Ana")
                .lastName("Updated")
                .email("ana@university.edu")
                .program("IT")
                .enrollmentYear(2025)
                .build();

        when(studentService.updateStudent(eq(1L), any())).thenReturn(
                StudentDto.builder().id(1L).firstName("Ana").lastName("Updated")
                        .email("ana@university.edu").program("IT").enrollmentYear(2025).build()
        );

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Updated"));
    }

    @Test
    void deleteStudent_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }
}
