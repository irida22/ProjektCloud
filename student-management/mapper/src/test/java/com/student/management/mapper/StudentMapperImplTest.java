package com.student.management.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.student.management.dto.CreateStudentRequest;
import com.student.management.dto.UpdateStudentRequest;
import com.student.management.model.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentMapperImplTest {

    private StudentMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new StudentMapperImpl();
    }

    @Test
    void toDto_shouldMapAllFields() {
        Student student = Student.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("Computer Science")
                .enrollmentYear(2024)
                .build();

        var dto = mapper.toDto(student);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("Ana");
        assertThat(dto.getLastName()).isEqualTo("Hoxha");
        assertThat(dto.getEmail()).isEqualTo("ana@university.edu");
        assertThat(dto.getProgram()).isEqualTo("Computer Science");
        assertThat(dto.getEnrollmentYear()).isEqualTo(2024);
    }

    @Test
    void toDto_shouldReturnNullWhenStudentIsNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    void toEntity_shouldMapCreateRequest() {
        CreateStudentRequest request = CreateStudentRequest.builder()
                .firstName("Besnik")
                .lastName("Krasniqi")
                .email("besnik@university.edu")
                .program("Engineering")
                .enrollmentYear(2023)
                .build();

        Student entity = mapper.toEntity(request);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getFirstName()).isEqualTo("Besnik");
        assertThat(entity.getLastName()).isEqualTo("Krasniqi");
        assertThat(entity.getEmail()).isEqualTo("besnik@university.edu");
        assertThat(entity.getProgram()).isEqualTo("Engineering");
        assertThat(entity.getEnrollmentYear()).isEqualTo(2023);
    }

    @Test
    void updateEntity_shouldUpdateExistingStudent() {
        Student student = Student.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Hoxha")
                .email("ana@university.edu")
                .program("CS")
                .enrollmentYear(2024)
                .build();

        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .firstName("Ana")
                .lastName("Updated")
                .email("ana@university.edu")
                .program("IT")
                .enrollmentYear(2025)
                .build();

        mapper.updateEntity(request, student);

        assertThat(student.getLastName()).isEqualTo("Updated");
        assertThat(student.getProgram()).isEqualTo("IT");
        assertThat(student.getEnrollmentYear()).isEqualTo(2025);
    }
}
