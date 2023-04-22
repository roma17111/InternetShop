package ru.skypro.homework.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;

@Service
@Log4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;
    private final Cache<Long, byte[]> avatarCache = Caffeine.newBuilder().build();
    private final ConcurrentMap<Long, CompletableFuture<byte[]>> loadingAvatar = new ConcurrentHashMap<>();
    private final Executor asyncExecutor = Executors.newCachedThreadPool();

    @Value("${sftp.host}")
    private String SFTP_HOST;

    @Value("${sftp.port}")
    private int SFTP_PORT;

    @Value("${sftp.user}")
    private String SFTP_USER;

    @Value("${sftp.password}")
    private String SFTP_PASSWORD;

    @Value("${sftp.directory}")
    private String SFTP_DIRECTORY;

    @Override
    public List<Avatar> getAllAvatars() {
        return avatarRepository.findAll();
    }

    @Override
    public void testSave(MultipartFile file, MediaType mediaType) {
        try {
            ChannelSftp channelSftp = setupJsch();
            InputStream inputStream = file.getInputStream();
            String filePath = SFTP_DIRECTORY + file.getOriginalFilename();
            channelSftp.put(inputStream, filePath);
            channelSftp.exit();

            Avatar avatar = new Avatar();
            avatar.setFileSize((int) file.getSize());
            avatar.setImageType(mediaType.toString());
            avatar.setAvatarPath(filePath);

            avatarRepository.save(avatar);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public byte[] getAvatarImage(long avatarId) throws ExecutionException, InterruptedException {
        CompletableFuture<byte[]> future = loadingAvatar.get(avatarId);
        if (future != null) {
            return future.get();
        }
        return avatarCache.get(avatarId, id -> {
            CompletableFuture<byte[]> newFuture = new CompletableFuture<>();
            loadingAvatar.put(id, newFuture);
            asyncExecutor.execute(() -> {
                byte[] result = getByteAvatarImageUncached(id);
                avatarCache.put(id, result);
                newFuture.complete(result);
                loadingAvatar.remove(id);
            });
            return null;
        });
    }

    private byte[] getByteAvatarImageUncached(Long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new RuntimeException("Avatar not found"));
        String avatarPath = avatar.getAvatarPath();
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
    }

    public Resource getAvatarResource(long avatarId) throws ExecutionException, InterruptedException {
        byte[] data = getAvatarImage(avatarId);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        return new InputStreamResource(byteArrayInputStream);
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }
}
