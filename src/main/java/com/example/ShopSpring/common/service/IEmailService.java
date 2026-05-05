package com.example.ShopSpring.common.service;


public interface IEmailService {
    void sendEmailAsync(String to, String subject, String content);
}
