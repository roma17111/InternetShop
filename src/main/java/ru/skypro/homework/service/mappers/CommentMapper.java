package ru.skypro.homework.service.mappers;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment mapCcommentDtoToComment(CommentDto commentDto);
    CommentDto mapCommentToCommentDto(Comment comment);
}
