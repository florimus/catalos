package com.commerce.catalos.services;

import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.emails.EmailRequest;

public interface CustomEmailService {
    String createEmailService(final CreateCustomAppRequest createCustomAppRequest);

    void sendEmail(final EmailRequest emailRequest);
}
