package KGUcapstone.OutDecision.domain.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${BUCKET_NAME}")
    private String bucket;

    @Value("${DEFAULT_PROFILE_IMG}")
    private String defaultImg;

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    //NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
    public String uploadFile(MultipartFile multipartFile, String filePath) {

        String s3file;
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            String keyName = filePath + "/" + uploadFileName;

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // S3에 업로드한 폴더 및 파일 URL
            uploadFileUrl = "https://kr.object.ncloudstorage.com/"+ bucket + "/" + keyName;

        } catch (IOException e) {
            e.printStackTrace();
        }

        s3file = uploadFileUrl;
        return s3file;
    }

    // 이미지 수정으로 인해 기존 이미지 삭제 메소드
    public void deleteImage(String ImgUrl) {
        String splitStr = ".com/" + bucket + "/";
        String fileName = ImgUrl.substring(ImgUrl.lastIndexOf(splitStr) + splitStr.length());

        // 기본 프로필 이미지가 아닐 때, 삭제
        if (!fileName.equals(defaultImg)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            System.out.println(" 사진 삭제 완료 ");
        }
    }

    // 게시글 삭제 시, 옵션이미지 삭제
    public void deleteImg(String ImgUrl) {
        String splitStr = ".com/" + bucket + "/";
        String fileName = ImgUrl.substring(ImgUrl.lastIndexOf(splitStr) + splitStr.length());

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println(" 사진 삭제 완료 ");
    }
}
