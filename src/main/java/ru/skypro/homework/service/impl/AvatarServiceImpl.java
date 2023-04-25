package ru.skypro.homework.service.impl;

import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.repository.AvatarRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;

    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port}")
    private int sftpPort;

    @Value("${sftp.user}")
    private String sftpUser;

    @Value("${sftp.password}")
    private String sftpPassword;

    @Value("${sftp.directory}")
    private String sftpDirectory;

    @Override
    public List<Avatar> getAllAvatars() {
        return avatarRepository.findAll();
    }

    @Override
    public void testSave(MultipartFile file, MediaType mediaType) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            ChannelSftp channelSftp = null;
            try {
                channelSftp = setupJsch();
                InputStream inputStream = file.getInputStream();
                String filePath = sftpDirectory + file.getOriginalFilename();
                channelSftp.put(inputStream, filePath);
                Avatar avatar = new Avatar();
                avatar.setFileSize((int) file.getSize());
                avatar.setImageType(mediaType.toString());
                avatar.setAvatarPath(filePath);
                avatarRepository.save(avatar);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                if (channelSftp != null) {
                    channelSftp.disconnect();
                }
            }
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public byte[] getAvatarImage(long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow(() -> new RuntimeException("Avatar not found"));
        String avatarPath = avatar.getAvatarPath();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<byte[]> future = executor.submit(() -> {
            ChannelSftp sftpChannel = null;
            try {
                sftpChannel = setupJsch();
                InputStream inputStream = sftpChannel.get(avatarPath);
                return IOUtils.toByteArray(inputStream);
            } catch (JSchException | SftpException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (sftpChannel != null) {
                    sftpChannel.exit();
                }
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public Resource getAvatarResource(long avatarId) throws JSchException, SftpException, IOException {
        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow();
        String avatarPath = avatar.getAvatarPath();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ByteArrayOutputStream> future = executor.submit(() -> {
            ChannelSftp channelSftp = setupJsch();

            InputStream inputStream = channelSftp.get(avatarPath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            channelSftp.disconnect();

            return outputStream;
        });

        try {
            ByteArrayOutputStream outputStream = future.get();
            byte[] data = outputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            return new InputStreamResource(byteArrayInputStream);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, sftpPort);
        session.setPassword(sftpPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }
}

