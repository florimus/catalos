package com.commerce.catalos.models.channels;

import com.commerce.catalos.persistances.dtos.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelVerificationResponse {

    private List<Channel> verifiedChannels;

    private List<String> errors;
}
