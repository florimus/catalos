package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.models.channels.ChannelVerificationResponse;
import com.commerce.catalos.persistances.dtos.Channel;
import com.commerce.catalos.persistances.repositories.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelVerificationResponse verifyChannels(List<String> channelIds, boolean doThrowError) {
        if (channelIds.isEmpty()) {
            Logger.error("03b59e6a-825d-4fd8-a09e-e5d752fa46a9", "Channel Ids Mandatory");
            if (doThrowError) {
                throw new BadRequestException("No Channels specified");
            }
            return ChannelVerificationResponse.builder()
                    .verifiedChannels(List.of()).errors(List.of("All"))
                    .build();
        }
        List<Channel> channels = channelRepository.findChannelsByIdAndEnabled(channelIds, true);

        ChannelVerificationResponse.ChannelVerificationResponseBuilder responseBuilder = ChannelVerificationResponse
                .builder();
        responseBuilder.verifiedChannels(channels);

        if (channelIds.size() > channels.size()) {
            List<String> errors = channelIds.stream()
                    .filter(id -> channels.stream().noneMatch(c -> c.getId().equals(id)))
                    .toList();
            Logger.error("d8c13b39-4162-4391-8234-437ef2d14ec7", "Error in some channels", errors.toString());
            responseBuilder.errors(errors);
            if (doThrowError) {
                throw new BadRequestException("Channel errors: " + errors.toString());
            }
        }

        return responseBuilder.build();
    }
}
