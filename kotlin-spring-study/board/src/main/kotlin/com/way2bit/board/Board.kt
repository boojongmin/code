package com.way2bit.board

import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(private val service: BoardService) {

    @GetMapping
    fun list(): List<BoardListDto> {
        return service.list()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: CreateBoardDto) {
        service.create(dto)
    }
}


data class BoardListDto(
    val id: Long,
    val title: String,
    val createdAt: LocalDateTime,
    val createdBy: String,
) {
    constructor(e: BoardEntity?) : this(e!!.id!!, e!!.title, e!!.createdAt, e!!.createdBy)
}

data class CreateBoardDto(
    val title: String,
    val content: String,
    val username: String,
) {
    fun toEntity(): BoardEntity {
        return BoardEntity(null, title, content, LocalDateTime.now(), username)
    }
}

@Service
class BoardService(private val repository: BoardRepository) {
    fun create(dto: CreateBoardDto) {
        val entity: BoardEntity = dto.toEntity()
        repository.save(entity)
    }

    fun list(): List<BoardListDto> {
        val entities = repository.findAll()
        return entities.map { BoardListDto(it) }
    }
}

interface BoardRepository: CrudRepository<BoardEntity, Long> {
}


data class BoardEntity(
    var id: Long?,
    var title: String,
    var content: String,
    var createdAt: LocalDateTime,
    var createdBy: String,
)