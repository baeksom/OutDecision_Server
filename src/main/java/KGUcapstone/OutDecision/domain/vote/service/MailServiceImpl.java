package KGUcapstone.OutDecision.domain.vote.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService{
    private final JavaMailSender emailSender;

    @Override
    @Transactional
    public void sendNotification(String email, String title) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("outdecision2024@gmail.com");
        message.setTo(email);
        message.setSubject("[결정잘해] 📧 투표한 게시글의 투표가 마감되었습니다!");
        String emailContent = getEmailContent(title);
        message.setText(emailContent);
        try {
            emailSender.send(message);
            log.info("피드백 알림 메일 전송 완료");
        } catch (MailException e) {
            log.error("피드백 알림 메일 전송 중 오류 발생", e);
            throw new RuntimeException("피드백 알림 메일 전송 중 오류 발생", e);
        }

    }

    private String getEmailContent(String title) {
        try {
            ClassPathResource resource = new ClassPathResource("email-template.txt");
            byte[] contentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String emailTemplate = new String(contentBytes, StandardCharsets.UTF_8);

            emailTemplate = emailTemplate.replace("{title}", title);

            return emailTemplate;
        } catch (IOException e) {
            log.error("이메일 템플릿 파일을 읽어오는 도중 오류 발생", e);
            return "";
        }
    }
}
