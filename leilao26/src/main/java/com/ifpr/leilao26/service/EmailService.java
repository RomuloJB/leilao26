package com.ifpr.leilao26.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remetente;

    public void enviarEmailRecuperacaoSenha(String destinatario, String nome, String codigo, int validadeMinutos) {
        Context contexto = new Context();
        contexto.setVariable("nome", nome);
        contexto.setVariable("codigo", codigo);
        contexto.setVariable("validadeMinutos", validadeMinutos);

        // processa o template resources/templates/recuperacaoSenha.html
        String corpoHtml = templateEngine.process("recuperacaoSenha", contexto);

        enviarEmailHtml(destinatario, "Recuperação de senha - Leilão26", corpoHtml);
    }

    private void enviarEmailHtml(String destinatario, String assunto, String corpoHtml) {
        MimeMessage mensagem = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(corpoHtml, true); // true = conteúdo em HTML
            mailSender.send(mensagem);
        } catch (MessagingException e) {
            // MailPreparationException é uma MailException, assim quem chama trata um único tipo de erro
            throw new MailPreparationException("Erro ao montar o e-mail", e);
        }
    }
}
