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

@Service
@Log4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {


   private final AvatarRepository avatarRepository;

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

    /*
    public void saveAvatar(MultipartFile file, String userName) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            String userDirectory = SFTP_DIRECTORY + userName + "/avatar/";
            try {
                channelSftp.cd(userDirectory);
            } catch (SftpException e) {
                channelSftp.mkdir(userDirectory);
                channelSftp.cd(userDirectory);
            }

            InputStream inputStream = file.getInputStream();
            channelSftp.put(inputStream, userDirectory + file.getOriginalFilename());

            channelSftp.disconnect();
            session.disconnect();

            Avatar avatar = new Avatar();
            avatar.setFileSize((int) file.getSize());
            avatar.setImageType(file.getContentType());
            avatar.setAvatarPath(userDirectory + file.getOriginalFilename());
            avatarRepository.save(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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
        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow(() -> new RuntimeException("Avatar not found"));
        String avatarPath = avatar.getAvatarPath();

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;
        try {
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;

            InputStream inputStream = sftpChannel.get(avatarPath);
            return IOUtils.toByteArray(inputStream);
        } catch (JSchException | SftpException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (sftpChannel != null) {
                sftpChannel.exit();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    @Override
    public Resource getAvatarResource(long avatarId) throws JSchException, SftpException, IOException {
        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow();
        String avatarPath = avatar.getAvatarPath();

        JSch jsch = new JSch();
        Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        InputStream inputStream = channelSftp.get(avatarPath);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        byte[] data = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();
        channelSftp.disconnect();
        session.disconnect();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

        return new InputStreamResource(byteArrayInputStream);
    }

}
