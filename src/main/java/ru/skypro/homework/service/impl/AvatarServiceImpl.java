package ru.skypro.homework.service.impl;

import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
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

    @Override
    public List<Avatar> getAllAvatars() {
        return avatarRepository.findAll();
    }

    @Override
    public Avatar testSave(MultipartFile file, MediaType mediaType) {
        Avatar avatar = null;
        ChannelSftp channelSftp = null;
        try {
            channelSftp = setupJsch();
            InputStream inputStream = file.getInputStream();
            String filePath = SFTP_DIRECTORY + file.getOriginalFilename();
            channelSftp.put(inputStream, filePath);
            avatar = new Avatar();
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
        return avatar;
    }

    @Override
    public byte[] getAvatarImage(long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow(() -> new RuntimeException("Avatar not found"));
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

    @Override
    public byte[] compressImage(MultipartFile mpFile) {
        float quality = 0.3f;
        String imageName = mpFile.getOriginalFilename();
        String imageExtension = imageName.substring(imageName.lastIndexOf(".") + 1);
        // Returns an Iterator containing all currently registered ImageWriters that claim to be able to encode the named format.
        // You don't have to register one yourself; some are provided.
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(imageExtension).next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // Check the api value that suites your needs.
        // A compression quality setting of 0.0 is most generically interpreted as "high compression is important,"
        // while a setting of 1.0 is most generically interpreted as "high image quality is important."
        imageWriteParam.setCompressionQuality(quality);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // MemoryCacheImageOutputStream: An implementation of ImageOutputStream that writes its output to a regular
        // OutputStream, i.e. the ByteArrayOutputStream.
        ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(baos);
        // Sets the destination to the given ImageOutputStream or other Object.
        imageWriter.setOutput(imageOutputStream);
        BufferedImage originalImage = null;
        try (InputStream inputStream = mpFile.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            String info = String.format("compressImage - bufferedImage (file %s)- IOException - message: %s ", imageName, e.getMessage());
            log.error(e.getMessage());
            return baos.toByteArray();
        }
        IIOImage image = new IIOImage(originalImage, null, null);
        try {
            imageWriter.write(null, image, imageWriteParam);
        } catch (IOException e) {
            String info = String.format("compressImage - imageWriter (file %s)- IOException - message: %s ", imageName, e.getMessage());
            log.info(info);
        } finally {
            imageWriter.dispose();
        }
        return baos.toByteArray();
    }
}

