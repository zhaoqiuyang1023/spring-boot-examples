package com.neo.controller;

import com.neo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class SendEmail {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MailService mailService;

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(HttpServletRequest request) throws IOException {

        Context context = new Context();
        context.setVariable("userName", "赵小明");

        System.out.println("URI:" + request.getRequestURI());
        System.out.println("URL:" + request.getRequestURL());

        String Ip = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        System.out.println(Ip);
        context.setVariable("url", Ip + "/receive/" + "赵小明");
        context.setVariable("date", new Date());
        String emailContent = templateEngine.process("thymeleaf", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo("1186389154@qq.com");
            helper.setSubject("主题");
            helper.setText(emailContent, true);
            helper.addInline("image1", new ClassPathResource("static/image1.jpg"));
            helper.addInline("image2", new ClassPathResource("static/favicon.ico"));
            Resource resource = new ClassPathResource("static/favicon.ico");
            helper.addAttachment(resource.getFilename(), resource.getFile());
            mailSender.send(message);
            logger.info("嵌入模板资源的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送嵌入模板的邮件时发生异常！", e);
        }
        return "ok";
    }

    @RequestMapping("/receive/{name}")
    public String receive(@PathVariable String name) {

        return name;
    }

    @RequestMapping("/static")
    public String send(HttpServletRequest request) throws IOException {
        String rscId = "neo006";
        // String content="<html><body>这是有图片的邮件：<img src='cid:neo006'></body></html>";
        InputStream inputStream = new ClassPathResource("templates/emailTemplate.html").getInputStream();
        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.print(content);
       // FileSystemResource file = new FileSystemResource(new File(filePath));
        Resource resource = new ClassPathResource("static/favicon.ico");
        File file = resource.getFile();

        mailService.sendInlineResourceMail("1186389154@qq.com", "主题：这是有图片的邮件", content, file.getPath(), rscId);
        return "ok";
    }


}
