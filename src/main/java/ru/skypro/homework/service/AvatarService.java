package ru.skypro.homework.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.models.Avatar;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AvatarService {
    List<Avatar> getAllAvatars();


    Avatar testSave(MultipartFile file, MediaType mediaType);

    byte[] getAvatarImage(long avatarId) throws ExecutionException, InterruptedException;

    Resource getAvatarResource(long avatarId) throws JSchException, SftpException, IOException, ExecutionException, InterruptedException;

    byte[] compressImage(MultipartFile mpFile);
}
