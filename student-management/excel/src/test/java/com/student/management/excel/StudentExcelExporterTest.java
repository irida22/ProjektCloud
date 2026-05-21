package com.student.management.excel;

import static org.assertj.core.api.Assertions.assertThat;

import com.student.management.dto.StudentDto;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

class StudentExcelExporterTest {

    private final StudentExcelExporter exporter = new StudentExcelExporter();

    @Test
    void export_shouldGenerateValidExcelWithHeadersAndData() throws IOException {
        List<StudentDto> students = List.of(
                StudentDto.builder()
                        .id(1L)
                        .firstName("Ana")
                        .lastName("Hoxha")
                        .email("ana@university.edu")
                        .program("Computer Science")
                        .enrollmentYear(2024)
                        .build()
        );

        byte[] result = exporter.export(students);

        assertThat(result).isNotEmpty();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(result))) {
            var sheet = workbook.getSheetAt(0);
            assertThat(sheet.getSheetName()).isEqualTo("Students");
            assertThat(sheet.getRow(0).getCell(0).getStringCellValue()).isEqualTo("ID");
            assertThat(sheet.getRow(1).getCell(1).getStringCellValue()).isEqualTo("Ana");
            assertThat(sheet.getRow(1).getCell(3).getStringCellValue()).isEqualTo("ana@university.edu");
        }
    }

    @Test
    void export_shouldHandleEmptyList() throws IOException {
        byte[] result = exporter.export(List.of());

        assertThat(result).isNotEmpty();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(result))) {
            var sheet = workbook.getSheetAt(0);
            assertThat(sheet.getPhysicalNumberOfRows()).isEqualTo(1);
        }
    }
}
