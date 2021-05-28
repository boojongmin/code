package com.way2bit.board

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import java.util.*
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = [
    DataSourceAutoConfiguration::class,
    DataSourceTransactionManagerAutoConfiguration::class,
    HibernateJpaAutoConfiguration::class]
)
class BoardControllerTest {
    @Autowired
    private val mockMvc: MockMvc? = null
    @Autowired
    private val mapper: ObjectMapper? = null

    @Test
    @DisplayName("새글 등록")
    fun create() {
        createBoard()
    }

    @Test
    @DisplayName("목록 보기")
    fun list() {
        createBoard()
        mockMvc!!.perform(
            MockMvcRequestBuilders.get("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title"))
            .andDo(print())
    }

    private fun createBoard() {
        val dto = CreateBoardDto("title", "content", "username")
        val json = mapper!!.writeValueAsString(dto)
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/v1/boards")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andDo(print())
    }

}


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = [
    DataSourceAutoConfiguration::class,
    DataSourceTransactionManagerAutoConfiguration::class,
    HibernateJpaAutoConfiguration::class]
)
class BoardServiceTest {
    @Autowired
    private val service: BoardService? = null

    @Test
    @DisplayName("게시판 등록")
    fun createService() {
        createBoardService()
    }

    @Test
    @DisplayName("게시판 조회")
    fun listService() {
        createBoardService()
        val dtos = service!!.list()
        assertThat(dtos).hasSizeGreaterThan(0)

        val dto = dtos[0]
        assertThat(dto.id).isEqualTo(1L)
    }

    private fun createBoardService() {
        val dto = CreateBoardDto("title", "content", "username")
        service!!.create(dto)
    }
}

@Configuration
class BoardTestConfig {

    @Bean
    fun boardRepository() = TestBoardRepository()

}

class TestBoardRepository: BoardRepository {
    val list = mutableListOf<BoardEntity>()
    var id = 0L

    override fun <S : BoardEntity?> save(entity: S): S {
        entity!!.id = ++id
        list.add(entity!!)
        return entity
    }

    override fun <S : BoardEntity?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<BoardEntity> {
        return list
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableIterable<BoardEntity> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun delete(entity: BoardEntity) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<BoardEntity>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Optional<BoardEntity> {
        TODO("Not yet implemented")
    }

}
