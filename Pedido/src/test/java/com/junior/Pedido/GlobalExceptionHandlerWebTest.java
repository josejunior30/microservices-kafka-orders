package com.junior.Pedido;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.junior.Pedido.exceptions.GlobalExceptionHandler;


@WebMvcTest(controllers = FakeTestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerWebTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void deveRetornar404ComApiError() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("Pedido não encontrado. ID=10"))
                .andExpect(jsonPath("$.path").value("/test/notfound"));
    }

    @Test
    void deveRetornar422ComApiError() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Business rule violation"))
                .andExpect(jsonPath("$.message").value("Não pode mudar status"))
                .andExpect(jsonPath("$.path").value("/test/business"));
    }

    @Test
    void deveRetornar409ComApiError() throws Exception {
        mockMvc.perform(get("/test/integrity"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Data integrity violation"))
                .andExpect(jsonPath("$.message").value("Operação não pôde ser concluída por restrição de integridade."))
                .andExpect(jsonPath("$.path").value("/test/integrity"));
    }
}