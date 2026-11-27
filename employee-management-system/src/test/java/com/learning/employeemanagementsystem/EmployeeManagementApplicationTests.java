package com.learning.employeemanagementsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.learning.employeemanagementsystem.repository.EmployeeRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeManagementApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@BeforeEach
	void clearEmployees() {
		employeeRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void createsEmployeeAndReturnsStandardResponse() throws Exception {
		mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Alex Morgan\",\"email\":\"alex@example.com\","
						+ "\"department\":\"Engineering\",\"salary\":75000,"
						+ "\"joiningDate\":\"2024-06-15\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.message").value("Employee created successfully"))
				.andExpect(jsonPath("$.status").value(201))
				.andExpect(jsonPath("$.data.joiningDate").value("2024-06-15"));
	}

	@Test
	void patchUpdatesOnlyProvidedFieldsAndSupportsSearch() throws Exception {
		String response = mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Alex Morgan\",\"email\":\"alex@example.com\","
						+ "\"department\":\"Engineering\",\"salary\":75000,"
						+ "\"joiningDate\":\"2024-06-15\"}"))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		Number id = com.jayway.jsonpath.JsonPath.read(response, "$.data.id");

		mockMvc.perform(patch("/employees/" + id.longValue())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"salary\":82000}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.name").value("Alex Morgan"))
				.andExpect(jsonPath("$.data.salary").value(82000));

		mockMvc.perform(get("/employees/search").param("name", "alex"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.length()").value(1));
	}

	@Test
	void rejectsInvalidEmployeeAndReturnsStandardErrorResponse() throws Exception {
		mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\" \",\"email\":\"invalid\","
						+ "\"department\":\"Engineering\",\"salary\":0,"
						+ "\"joiningDate\":\"2999-01-01\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.status").value(400));
	}
}
