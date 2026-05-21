package com.student.management.mapper;

import com.student.management.dto.CreateStudentRequest;
import com.student.management.dto.StudentDto;
import com.student.management.dto.UpdateStudentRequest;
import com.student.management.model.entity.Student;
/**
 * Implementim i {@link StudentMapper}. Interfejsi përdor MapStruct ({@code @Mapper});
 * ky klasë është implementimi që përdor aplikacioni (edhe IDE e njeh pa gabime).
 */
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .program(student.getProgram())
                .enrollmentYear(student.getEnrollmentYear())
                .build();
    }

    @Override
    public Student toEntity(CreateStudentRequest request) {
        if (request == null) {
            return null;
        }
        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .program(request.getProgram())
                .enrollmentYear(request.getEnrollmentYear())
                .build();
    }

    @Override
    public void updateEntity(UpdateStudentRequest request, Student student) {
        if (request == null || student == null) {
            return;
        }
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setProgram(request.getProgram());
        student.setEnrollmentYear(request.getEnrollmentYear());
    }
}
