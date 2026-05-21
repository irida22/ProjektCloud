package com.student.management.mapper;

import com.student.management.dto.CreateStudentRequest;
import com.student.management.dto.StudentDto;
import com.student.management.dto.UpdateStudentRequest;
import com.student.management.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface StudentMapper {

    StudentDto toDto(Student student);

    @Mapping(target = "id", ignore = true)
    Student toEntity(CreateStudentRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateStudentRequest request, @MappingTarget Student student);
}
