package ru.skypro.homework.service.impl;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {


   private final AvatarRepository avatarRepository;

    private final Map<Long, byte[]> avatarCache = new ConcurrentHashMap<>();

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
    public void testSave(MultipartFile file,
                         MediaType mediaType) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            InputStream inputStream = file.getInputStream();
            String filePath = SFTP_DIRECTORY + file.getOriginalFilename();
            channelSftp.put(inputStream, filePath);

            channelSftp.disconnect();
            session.disconnect();

         //   avatar.setId(new Random().nextInt(Integer.MAX_VALUE));
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
    public byte[] getAvatarImage(long avatarId) {
        return avatarCache.computeIfAbsent(avatarId, this::getByteAvatarImageUncached);
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

    @Override
    public Resource getAvatarResource(long avatarId) throws JSchException, SftpException, IOException{
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
