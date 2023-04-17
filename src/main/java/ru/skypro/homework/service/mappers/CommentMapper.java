package ru.skypro.homework.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.mdels.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment mapCcommentDtoToComment(CommentDto commentDto);
    CommentDto mapCommentToCommentDto(Comment comment);
}
