package com.example.thehealingmeal.email.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

@Transactional
@RequiredArgsConstructor
@Service
public class ProdEmailService implements EmailService {


    private final JavaMailSender emailSender;
    private String authNum; // 인증 번호


    // 인증번호 6자리 무작위 생성
    private void createCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder();

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = characters.length();

        for (int i = 0; i < 6; i++) {
            int idx = random.nextInt(length);
            key.append(characters.charAt(idx));
        }

        authNum = key.toString();
    }

    // 메일 양식 작성
    private MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        createCode();
        String setFrom = "HealingMeal"; // 보내는 사람
        String toEmail = email;
        String title = "HealingMeal 회원가입 인증 메일";// 제목




        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); // 수신자 추가.
        message.setSubject(title); // 제목을 설정

        // 메일 내용
        String msgOfEmail = mailContents();
        // mailContents() 메소드를 호출하여 이메일의 본문을 생성하고, 이를 msgOfEmail 변수에 저장하는 줄입니다.

        message.setFrom(setFrom); // 발신자 저장.
        message.setText(msgOfEmail, "utf-8", "html"); // 본문 설정.

        return message; // 이 message 객체는 이메일의 제목, 수신자, 발신자, 본문 등을 포함하고 있음.
    }

    private String mailContents() {
        return "<div style='margin:20px;'>" +
                "<h1> 👋🏻 HealingMeal 회원가입 인증 메일 </h1><br>" +
                "<p>아래의 코드를 인증 코드란에 적고 이메일 인증을 마쳐주세요.<p><br>" +
                "<div align='center' style='border:1px solid black; font-family:verdana';>" +
                "<div style='font-size:130%'>" +
                "<strong><br>" +
                authNum +
                "</strong><div><br/> " +
                "</div>";
    }

    @Override
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);
        //실제 메일 전송
        emailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }
}
