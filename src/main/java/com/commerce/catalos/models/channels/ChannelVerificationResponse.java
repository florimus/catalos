package com.commerce.catalos.models.channels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.commerce.catalos.persistence.dtos.Channel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelVerificationResponse {

    private List<Channel> verifiedChannels;

    private List<String> errors;
}
