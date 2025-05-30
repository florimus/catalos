package com.commerce.catalos.services;

import com.commerce.catalos.models.channels.ChannelVerificationResponse;

import java.util.List;

public interface ChannelService {

    public ChannelVerificationResponse verifyChannels(final List<String> channelIds, final boolean doThrowError);
}
